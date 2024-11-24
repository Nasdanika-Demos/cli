package org.nasdanika.launcher.demo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nasdanika.capability.CapabilityFactory;
import org.nasdanika.cli.SubCommandCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;

import picocli.CommandLine;

public class GitLabDemoRetrospectCommandFactory extends SubCommandCapabilityFactory<GitLabDemoRetrospectCommand> {

	@Override
	protected Class<GitLabDemoRetrospectCommand> getCommandType() {
		return GitLabDemoRetrospectCommand.class;
	}
	
	@Override
	protected CompletionStage<GitLabDemoRetrospectCommand> doCreateCommand(
			List<CommandLine> parentPath,
			CapabilityFactory.Loader loader,
			ProgressMonitor progressMonitor) {
		return CompletableFuture.completedStage(new GitLabDemoRetrospectCommand(loader.getCapabilityLoader()));
	}

}
