package org.nasdanika.launcher.demo.ai;

import java.util.List;
import java.util.concurrent.CompletionStage;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.nasdanika.capability.CapabilityFactory;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.capability.emf.ResourceSetRequirement;
import org.nasdanika.cli.SubCommandCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;

import io.opentelemetry.api.OpenTelemetry;
import picocli.CommandLine;

public class PdfIndexerCommandFactory extends SubCommandCapabilityFactory<PdfIndexerCommand> {

	@Override
	protected Class<PdfIndexerCommand> getCommandType() {
		return PdfIndexerCommand.class;
	}
	
	@Override
	protected CompletionStage<PdfIndexerCommand> doCreateCommand(
			List<CommandLine> parentPath,
			CapabilityFactory.Loader loader,
			ProgressMonitor progressMonitor) {
		
		Requirement<Object, OpenTelemetry> openTelemetryRequirement = ServiceCapabilityFactory.createRequirement(OpenTelemetry.class);
		CompletionStage<OpenTelemetry> openTelemetryCS = loader.loadOne(openTelemetryRequirement, progressMonitor);

		Requirement<ResourceSetRequirement, ResourceSet> resourceSetRequirement = ServiceCapabilityFactory.createRequirement(ResourceSet.class);		
		CompletionStage<ResourceSet > resourceSetCS = loader.loadOne(resourceSetRequirement, progressMonitor);
		
		
		return openTelemetryCS.thenCombine(resourceSetCS, (openTelemetry, resourceSet) -> new PdfIndexerCommand(resourceSet, openTelemetry, loader.getCapabilityLoader()));
	}

}
