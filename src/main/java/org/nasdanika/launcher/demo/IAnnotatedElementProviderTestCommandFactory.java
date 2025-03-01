package org.nasdanika.launcher.demo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nasdanika.cli.SubCommandCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;

import picocli.CommandLine;

public class IAnnotatedElementProviderTestCommandFactory extends SubCommandCapabilityFactory<IAnnotatedElementProviderTestCommand> {

	@Override
	protected Class<IAnnotatedElementProviderTestCommand> getCommandType() {
		return IAnnotatedElementProviderTestCommand.class;
	}
	
	@Override
	protected CompletionStage<IAnnotatedElementProviderTestCommand> doCreateCommand(
			List<CommandLine> parentPath, 
			Loader loader,
			ProgressMonitor progressMonitor) {
		return CompletableFuture.completedStage(new IAnnotatedElementProviderTestCommand());
	}

}