package org.nasdanika.launcher.demo.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.ProgressMonitorMixIn;
import org.nasdanika.cli.RootCommand;
import org.nasdanika.common.NasdanikaException;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.launcher.demo.ModuleVersionProvider;
import org.nasdanika.models.rules.Rule;
import org.nasdanika.models.rules.RuleSet;
import org.nasdanika.models.rules.cli.AbstractRuleCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;

@Command(
		description = "Lists available rules",
		name = "list-rules",
		versionProvider = ModuleVersionProvider.class,
		mixinStandardHelpOptions = true)
@ParentCommands(RootCommand.class)
public class ListRulesCommand extends AbstractRuleCommand {
	
	@Option(names = {"-o", "--output"}, description = "Output file")
	private File output;
	
	@Mixin
	private ProgressMonitorMixIn progressMonitorMixIn;	 

	@Override
	public Integer call() throws Exception {
		CapabilityLoader capabilityLoader = new CapabilityLoader();
		ProgressMonitor progressMonitor = progressMonitorMixIn.createProgressMonitor(1);
		Iterable<CapabilityProvider<Object>> ruleSetProviders = capabilityLoader.load(ServiceCapabilityFactory.createRequirement(RuleSet.class), progressMonitor);
		Collection<RuleSet> ruleSets = Collections.synchronizedCollection(new ArrayList<>());
		for (CapabilityProvider<Object> provider: ruleSetProviders) {
			provider.getPublisher().subscribe(rs -> ruleSets.add((RuleSet) rs), error -> error.printStackTrace());
		}
		if (output == null) {
			generateReport(ruleSets, System.out, progressMonitor);
		} else {
			try (PrintStream out = new PrintStream(output)) {
				generateReport(ruleSets, out, progressMonitor);
			} catch (FileNotFoundException e) {
				throw new NasdanikaException(e);
			}
		}
		return 0;
	}

	protected void generateReport(
			Collection<RuleSet> ruleSets, 
			PrintStream out, 
			ProgressMonitor progressMonitor) {
		
		for (RuleSet ruleSet: ruleSets) {
			out.println(ruleSet.getId() + ": " + ruleSet.getName());				
			for (Rule rule: ruleSet.getRules()) {
				out.println("\t" + rule.getId() + ": " + rule.getName());
			}
		}				
	}

}
