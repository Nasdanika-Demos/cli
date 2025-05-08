package org.nasdanika.launcher.demo.ai;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.json.JSONObject;
import org.nasdanika.ai.cli.HnswIndexCommandBase;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.RootCommand;
import org.nasdanika.common.NasdanikaException;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.common.Util;
import org.nasdanika.launcher.demo.ModuleVersionProvider;
import org.nasdanika.models.pdf.Article;
import org.nasdanika.models.pdf.Document;
import org.nasdanika.models.pdf.Page;
import org.nasdanika.models.pdf.Paragraph;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import reactor.core.publisher.Flux;

@Command(
	description = {
		"Creates a vector index and a URI -> text",
		"mapping from a PDF file or a directory of",
		"PDF files"
	}, 
	versionProvider = ModuleVersionProvider.class,
	mixinStandardHelpOptions = true,
	name = "pdf-indexer")	
@ParentCommands(RootCommand.class)
public class PdfIndexerCommand extends HnswIndexCommandBase {
	
	public enum Granularity {
		document, 
		page, 
		article, 
		paragraph
	}

	protected ResourceSet resourceSet;

	public PdfIndexerCommand(
			ResourceSet resourceSet,
			OpenTelemetry openTelemetry, 
			CapabilityLoader capabilityLoader) {
		super(openTelemetry, capabilityLoader);
		this.resourceSet = resourceSet;
	}
	
	@Parameters(
		description = "URI to plain text map JSON output",
		index = "1",
		arity = "1")
	private File textMap;
	
	@Parameters(
		description = "Input <base uri>=<file or directory>",
		index = "2",
		arity = "1..*")
	private Map<String,File> inputs;
	
	@Option(
		names = "--granularity",
		description = {
			"Text granularity",
			"Valid values: ${COMPLETION-CANDIDATES}",
			"Default value: ${DEFAULT-VALUE}"
		})
	protected Granularity granularity = Granularity.page;	

	@Override
	protected Flux<Entry<String, String>> getItems(Span commandSpan, ProgressMonitor progressMonitor) {
		Map<String, String> itemMap = new LinkedHashMap<>();
		JSONObject textMap = new JSONObject();
		for (Entry<String,File> ie: inputs.entrySet()) {
			Util.walk(
				ie.getKey(),
				(file,path) ->  {
					if (file.isFile() && path.toLowerCase().endsWith(".pdf")) {
						for (Entry<String, String> textItem: getPdfText(path, file, resourceSet, commandSpan, progressMonitor)) {
							itemMap.put(textItem.getKey(), textItem.getValue());
							textMap.put(textItem.getKey(), textItem.getValue());
						}
					}
				}, 
				ie.getValue());
		}
		try (Writer writer = new FileWriter(this.textMap)) {
			textMap.write(writer, 2, 0);			
		} catch (IOException e) {
			throw new NasdanikaException("Error writing text map: " + e, e);
		}
		return Flux.fromIterable(itemMap.entrySet());
	}
		
	protected String getWordSeparator() {
		return " ";	
	}
	
	protected String getLineSeparator() {
		return System.lineSeparator();
	}
	
	protected String getParagraphSeparator() {
		return getLineSeparator() + getLineSeparator();
	}	
	
	protected String getArticleSeparator() {
		return getParagraphSeparator() + getLineSeparator();
	}	
	
	protected String getPageSeparator() {
		return getArticleSeparator() + getLineSeparator();
	}	

	/**
	 * URI to text map
	 * @param uri
	 * @param value
	 * @param commandSpan
	 * @param progressMonitor
	 * @return
	 */
	protected List<Map.Entry<String,String>> getPdfText(
			String uri, 
			File file,
			ResourceSet resourceSet,			
			Span commandSpan, 
			ProgressMonitor progressMonitor) {
		List<Map.Entry<String,String>> results = new ArrayList<>();
		Tracer tracer = getTracer();
		Span loadSpan = tracer
			.spanBuilder("load-pdf")
			.setAttribute("uri", uri)
			.setParent(Context.current().with(commandSpan))
			.startSpan();
		
		try (Scope scope = loadSpan.makeCurrent()) {
			URI docURI = URI.createFileURI(file.getCanonicalPath());
			Resource pdfResource = resourceSet.getResource(docURI, true);
			Document pdfDocument = (Document) pdfResource.getContents().get(0);
			switch (granularity) {
			case article:
				for (Page page: pdfDocument.getPages()) {
					for (Article article: page.getArticles()) {
						String articleText = article.getText(getParagraphSeparator(), getLineSeparator(), getWordSeparator());
						results.add(Map.entry(uri + "#" + pdfResource.getURIFragment(article), articleText));						
					}
				}
				break;
			case document:
				String documentText = pdfDocument.getText(
						getPageSeparator(), 
						getArticleSeparator(), 
						getParagraphSeparator(), 
						getLineSeparator(), 
						getWordSeparator());
				results.add(Map.entry(uri, documentText));
				break;
			case page:
				for (Page page: pdfDocument.getPages()) {
					String pageText = page.getText(
						getArticleSeparator(),
						getParagraphSeparator(), 
						getLineSeparator(), 
						getWordSeparator());
					results.add(Map.entry(uri + "#" + pdfResource.getURIFragment(page), pageText));						
				}
				break;
			case paragraph:
				for (Page page: pdfDocument.getPages()) {
					for (Article article: page.getArticles()) {
						for (Paragraph paragraph: article.getParagraphs()) {
							String paragraphText = paragraph.getText(getLineSeparator(), getWordSeparator());
							results.add(Map.entry(uri + "#" + pdfResource.getURIFragment(paragraph), paragraphText));
						}
					}
				}
				break;
			default:
				throw new UnsupportedOperationException("Unsupported granularity: " + granularity);
			}			
		} catch (IOException e) {
			loadSpan.recordException(e);
			throw new NasdanikaException("Failed to load PDF '" + file.getAbsolutePath() + "': " + e, e);
		} finally {
			loadSpan.end();
		}
		
		return results;
	}		

}
