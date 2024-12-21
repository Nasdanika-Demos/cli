package org.nasdanika.launcher.demo.java;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nasdanika.capability.CapabilityFactory;
import org.nasdanika.cli.Description;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.SubCommandCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.models.gitlab.cli.GitLabContributorCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;

public class DemoMavenSourceAnalysisCommandFactory extends SubCommandCapabilityFactory<DemoMavenSourceAnalysisCommand> {

	@Override
	protected Class<DemoMavenSourceAnalysisCommand> getCommandType() {
		return DemoMavenSourceAnalysisCommand.class;
	}
	
	@Override
	protected CompletionStage<DemoMavenSourceAnalysisCommand> doCreateCommand(
			List<CommandLine> parentPath,
			CapabilityFactory.Loader loader,
			ProgressMonitor progressMonitor) {
		return CompletableFuture.completedStage(new DemoMavenSourceAnalysisCommand(loader.getCapabilityLoader()));
	}

}
