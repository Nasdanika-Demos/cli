package org.nasdanika.launcher.demo.ai;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import org.nasdanika.ai.Chat;
import org.nasdanika.ai.TextFloatVectorEmbeddingModel;
import org.nasdanika.ai.Model;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.RootCommand;
import org.nasdanika.cli.TelemetryCommand;
import org.nasdanika.common.Util;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import picocli.CommandLine.Command;

@Command(
		description = "Lists available AI models",
		name = "list-models")
@ParentCommands(RootCommand.class)
public class ListModelsCommand extends TelemetryCommand {
	
	private List<TextFloatVectorEmbeddingModel> embeddingModel;
	private List<Chat> chats;
	
	protected String getInstrumentationScopeName() {
		return getClass().getName();
	}
	
	protected String getInstrumentationScopeVersion() {
		String[] version = spec.version();
		if (version == null) {
			return null;
		}
		return String.join(System.lineSeparator(), version);
	}	

	public ListModelsCommand(
			List<TextFloatVectorEmbeddingModel> embeddingModel,
			List<Chat> chats,
			OpenTelemetry openTelemetry,
			CapabilityLoader capabilityLoader) {
		super(openTelemetry, capabilityLoader);
		this.embeddingModel = embeddingModel;
		this.chats = chats;
	}

	@Override
	protected Integer execute(Span commandSpan) throws Exception {
		if (embeddingModel != null && !embeddingModel.isEmpty()) {
			List<TextFloatVectorEmbeddingModel> sel = embeddingModel
				.stream()
				.filter(Objects::nonNull)
				.sorted(this::compareModels)
				.toList();
			
			System.out.println("Embeddings");
			for (Entry<String, List<TextFloatVectorEmbeddingModel>> pe: Util.groupBy(sel, Model::getProvider).entrySet()) {
				System.out.println("  " + pe.getKey());
				for (TextFloatVectorEmbeddingModel e: pe.getValue()) {
					System.out.println("    " + e.getName());
					if (!Util.isBlank(e.getVersion())) {
						System.out.println("      Version:" + e.getName());
					}
					System.out.println("      Dimensions:" + e.getDimensions());
					System.out.println("      Max input tokens:" + e.getMaxInputTokens());					
				}
			}						
		}
		
		if (chats != null && !chats.isEmpty()) {
			List<Chat> sel = chats
				.stream()
				.filter(Objects::nonNull)
				.sorted(this::compareModels)
				.toList();
			
			System.out.println("Chat");
			for (Entry<String, List<Chat>> pe: Util.groupBy(sel, Model::getProvider).entrySet()) {
				System.out.println("  " + pe.getKey());
				for (Chat e: pe.getValue()) {
					System.out.println("    " + e.getName());
					if (!Util.isBlank(e.getVersion())) {
						System.out.println("      Version:" + e.getName());
					}
					System.out.println("      Max input tokens:" + e.getMaxInputTokens());					
					System.out.println("      Max output tokens:" + e.getMaxOutputTokens());
				}
			}					
		}
		
		return 0;
	}	
	
	protected int compareModels(Model a, Model b) {
		if (a == b) {
			return 0;
		}
		if (a == null) {
			return 1;
		}
		if (b == null) {
			return -1;
		}
		int cmp = compareStrings(a.getProvider(), b.getProvider());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareStrings(a.getName(), b.getName());
		if (cmp != 0) {
			return cmp;
		}
		cmp = compareStrings(a.getVersion(), b.getVersion());
		if (cmp != 0) {
			return cmp;
		}
		return b.getMaxInputTokens() - a.getMaxInputTokens();
	}
	
	protected int compareStrings(String a, String b) {
		if (Objects.equals(a, b)) {
			return 0;
		}
		if (a == null) {
			return 1;
		}
		if (b == null) {
			return -1;
		}
		return a.compareTo(b);
	}

}
