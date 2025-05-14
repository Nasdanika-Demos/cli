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

public class ChatServerCommandFactory extends SubCommandCapabilityFactory<ChatServerCommand> {

	@Override
	protected Class<ChatServerCommand> getCommandType() {
		return ChatServerCommand.class;
	}
	
	@Override
	protected CompletionStage<ChatServerCommand> doCreateCommand(
			List<CommandLine> parentPath,
			Loader loader,
			ProgressMonitor progressMonitor) {
		
		Requirement<Embeddings.Requirement, Embeddings> embeddingsRequirement = ServiceCapabilityFactory.createRequirement(Embeddings.class);			
		CompletionStage<Embeddings> embeddingsCS = loader.loadOne(embeddingsRequirement, progressMonitor);
		
		Requirement<Chat.Requirement, Chat> chatRequirement = ServiceCapabilityFactory.createRequirement(Chat.class);			
		CompletionStage<Chat> chatCS = loader.loadOne(chatRequirement, progressMonitor);
		
		record Config(Embeddings embeddings, Chat chat) {}		
		
		CompletionStage<Config> configCS = embeddingsCS.thenCombine(chatCS, Config::new);		
		
		Requirement<Object, OpenTelemetry> openTelemetryRequirement = ServiceCapabilityFactory.createRequirement(OpenTelemetry.class);
		CompletionStage<OpenTelemetry> openTelemetryCS = loader.loadOne(openTelemetryRequirement, progressMonitor);
		
		return configCS.thenCombine(
				openTelemetryCS,
				(config, openTelemetry) -> new ChatServerCommand(
					config.embeddings(), 
					config.chat(),
					openTelemetry, 
					loader.getCapabilityLoader()));
	}

}
