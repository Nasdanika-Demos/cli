package org.nasdanika.launcher.demo.ai;

import java.util.List;
import java.util.concurrent.CompletionStage;

import org.nasdanika.ai.Chat;
import org.nasdanika.ai.Embeddings;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.cli.SubCommandCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;

import io.opentelemetry.api.OpenTelemetry;
import picocli.CommandLine;

public class ListModelsCommandFactory extends SubCommandCapabilityFactory<ListModelsCommand> {

	@Override
	protected Class<ListModelsCommand> getCommandType() {
		return ListModelsCommand.class;
	}
	
	@Override
	protected CompletionStage<ListModelsCommand> doCreateCommand(
			List<CommandLine> parentPath,
			Loader loader,
			ProgressMonitor progressMonitor) {
		
		Requirement<Embeddings.Requirement, Embeddings> embeddingsRequirement = ServiceCapabilityFactory.createRequirement(Embeddings.class);			
		CompletionStage<List<Embeddings>> embeddingsCS = loader.loadAll(embeddingsRequirement, progressMonitor);
		
		Requirement<Chat.Requirement, Chat> chatRequirement = ServiceCapabilityFactory.createRequirement(Chat.class);			
		CompletionStage<List<Chat>> chatCS = loader.loadAll(chatRequirement, progressMonitor);
		
		record Config(List<Embeddings> embeddings, List<Chat> chat) {}		
		
		CompletionStage<Config> configCS = embeddingsCS.thenCombine(chatCS, Config::new);		
		
		Requirement<Object, OpenTelemetry> openTelemetryRequirement = ServiceCapabilityFactory.createRequirement(OpenTelemetry.class);
		CompletionStage<OpenTelemetry> openTelemetryCS = loader.loadOne(openTelemetryRequirement, progressMonitor);
		
		return configCS.thenCombine(
				openTelemetryCS,
				(config, openTelemetry) -> new ListModelsCommand(
					config.embeddings(), 
					config.chat(),
					openTelemetry, 
					loader.getCapabilityLoader()));
	}

}
