package org.nasdanika.launcher.demo.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.RootCommand;
import org.nasdanika.common.NasdanikaException;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.launcher.demo.ModuleVersionProvider;
import org.nasdanika.models.rules.InspectionResult;
import org.nasdanika.models.rules.cli.AbstractInspectionCommand;
import org.nasdanika.ncore.util.DirectoryContentFileURIHandler;
import org.nasdanika.ncore.util.NcoreYamlHandler;
import org.nasdanika.ncore.util.YamlResourceFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
		description = "Demo of YAML inspection",
		name = "inspect-yaml",
		versionProvider = ModuleVersionProvider.class,
		mixinStandardHelpOptions = true)
@ParentCommands(RootCommand.class)
public class InspectYamlCommand extends AbstractInspectionCommand {
	
	@Parameters(description = {
			"Files and directories",
			"to inspect"
			},
			arity = "1..*")
	File[] inputs;	

	@Override
	protected List<URI> getInputs() {
		List<URI> ret = new ArrayList<>();
		for (File input: inputs) {
			URI uri = URI.createFileURI(input.getAbsolutePath());
			if (input.isDirectory()) {
				uri = uri.appendSegment("");
			}
			ret.add(uri);
		}
		return ret;
	}
	
	@Option(names = {"-o", "--output"}, description = "Output file")
	private File output;
	
	@Override
	protected ResourceSet createResourceSet(ProgressMonitor progressMonitor) {
		ResourceSet resourceSet = super.createResourceSet(progressMonitor);
		// Basic YAML. Add semantic handlers for your problem domain as needed (you'd need to create them). 
		YamlResourceFactory yamlResourceFactory = new YamlResourceFactory(new NcoreYamlHandler());
		Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put("yml", yamlResourceFactory);
		extensionToFactoryMap.put("yaml", yamlResourceFactory);
		
		// To load directories as resources in order to traverse them
		resourceSet.getURIConverter().getURIHandlers().add(0, new DirectoryContentFileURIHandler()); 
		return resourceSet;
	}

	@Override
	protected boolean isIncluded(String path) {
		String[] includes = getResourceIncludes();
		if (includes == null) {
			return path.endsWith(".yml") || path.endsWith(".yaml");
		}
		return super.isIncluded(path);
	}

	@Override
	protected void generateReport(
			Map<Resource, List<Entry<Notifier, List<InspectionResult>>>> results,
			ProgressMonitor progressMonitor) {
		
		if (output == null) {
			generateTextReport(results, System.out, progressMonitor);
		} else {
			try (PrintStream out = new PrintStream(output)) {
				generateTextReport(results, out, progressMonitor);
			} catch (FileNotFoundException e) {
				throw new NasdanikaException(e);
			}
		}
	}

}
