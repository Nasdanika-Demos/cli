package org.nasdanika.launcher.demo.rules;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nasdanika.cli.SubCommandCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;

import picocli.CommandLine;

public class ListInspectableRulesCommandFactory extends SubCommandCapabilityFactory<ListInspectableRulesCommand> {

	@Override
	protected CompletionStage<ListInspectableRulesCommand> doCreateCommand(
			List<CommandLine> parentPath, 
			Loader loader,
			ProgressMonitor progressMonitor) {

		return CompletableFuture.completedStage(new ListInspectableRulesCommand(loader.getCapabilityLoader()));			
	}

	@Override
	protected Class<ListInspectableRulesCommand> getCommandType() {
		return ListInspectableRulesCommand.class;
	}

}
