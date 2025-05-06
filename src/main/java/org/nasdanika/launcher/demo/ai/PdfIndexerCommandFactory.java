package org.nasdanika.launcher.demo.ai;

import java.util.List;
import java.util.concurrent.CompletionStage;

import org.nasdanika.capability.CapabilityFactory;
import org.nasdanika.capability.ServiceCapabilityFactory;
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
		
		return openTelemetryCS.thenApply(openTelemetry -> new PdfIndexerCommand(openTelemetry, loader.getCapabilityLoader()));
	}

}
