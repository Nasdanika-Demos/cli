package org.nasdanika.launcher.demo.tests;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.nasdanika.common.Diagnostic;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.common.Supplier;
import org.nasdanika.drawio.Document;
import org.nasdanika.html.bootstrap.Theme;
import org.nasdanika.models.app.Action;
import org.nasdanika.models.app.AppFactory;
import org.nasdanika.models.app.Label;
import org.nasdanika.models.app.gen.AppSiteGenerator;
import org.nasdanika.models.app.graph.drawio.DrawioHtmlAppGenerator;

public class TestDrawioActionSiteGenerator {
			
	@Test
//	@Disabled
	public void testGenerateAwsSite() throws Exception {
		generateDrawioActionSite("aws-2");
	}
	
	@Test
//	@Disabled
	public void testGenerateIbsSite() throws Exception {
		generateDrawioActionSite("internet-banking-system");
	}
	
	private void generateDrawioActionSite(String diagramName) throws Exception {
		Document document = Document.load(new File("test-data/" + diagramName + ".drawio").getCanonicalFile()); 
		DrawioHtmlAppGenerator actionGenerator = new DrawioHtmlAppGenerator();
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();		
		Supplier<Collection<Label>> labelSupplier = actionGenerator.createLabelsSupplier(document, progressMonitor);
		Consumer<Diagnostic> diagnosticConsumer = d -> d.dump(System.out, 0);
		Collection<Label> labels = labelSupplier.call(progressMonitor, diagnosticConsumer);
		
		Action rootAction = AppFactory.eINSTANCE.createAction();
		rootAction.setText("Nasdanika");		
		rootAction.getChildren().addAll(labels);
		
		String siteMapDomain = "https://nasdanika.org/demos/" + diagramName;		
		AppSiteGenerator actionSiteGenerator = new AppSiteGenerator();		
		
		Map<String, Collection<String>> errors = actionSiteGenerator.generate(
				rootAction, // URI.appendFragment("/"), 
				Theme.Cerulean.pageTemplateCdnURI, 
				siteMapDomain, 
				new File("target/drawio-action-site/" + diagramName), 
				new File("target/doc-site-work-dir/" + diagramName), 
				false);
				
		int errorCount = 0;
		for (Entry<String, Collection<String>> ee: errors.entrySet()) {
			System.err.println(ee.getKey());
			for (String error: ee.getValue()) {
				System.err.println("\t" + error);
				++errorCount;
			}
		}
		
		System.out.println(errorCount);
	}

}
