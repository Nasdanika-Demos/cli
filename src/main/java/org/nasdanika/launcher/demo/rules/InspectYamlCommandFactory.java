package org.nasdanika.launcher.demo.rules;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nasdanika.cli.SubCommandCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;

import picocli.CommandLine;

public class InspectYamlCommandFactory extends SubCommandCapabilityFactory<InspectYamlCommand> {

	@Override
	protected CompletionStage<InspectYamlCommand> doCreateCommand(
			List<CommandLine> parentPath, 
			Loader loader,
			ProgressMonitor progressMonitor) {
		return CompletableFuture.completedStage(new InspectYamlCommand());			
	}

	@Override
	protected Class<InspectYamlCommand> getCommandType() {
		return InspectYamlCommand.class;
	}

}
