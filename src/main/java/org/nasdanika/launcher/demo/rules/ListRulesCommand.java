package org.nasdanika.launcher.demo.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;

import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.RootCommand;
import org.nasdanika.common.NasdanikaException;
import org.nasdanika.launcher.demo.ModuleVersionProvider;
import org.nasdanika.models.rules.Rule;
import org.nasdanika.models.rules.RuleSet;
import org.nasdanika.models.rules.cli.AbstractRuleCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
		description = "Lists available rules",
		name = "list-rules",
		versionProvider = ModuleVersionProvider.class,
		mixinStandardHelpOptions = true)
@ParentCommands(RootCommand.class)
public class ListRulesCommand extends AbstractRuleCommand {
	
	private Collection<RuleSet> ruleSets;
	
	public ListRulesCommand(Collection<RuleSet> ruleSets) {
		this.ruleSets = ruleSets;
	}
	
	@Option(names = {"-o", "--output"}, description = "Output file")
	private File output;

	@Override
	public Integer call() throws Exception {
		if (output == null) {
			generateReport(ruleSets, System.out);
		} else {
			try (PrintStream out = new PrintStream(output)) {
				generateReport(ruleSets, out);
			} catch (FileNotFoundException e) {
				throw new NasdanikaException(e);
			}
		}
		return 0;
	}

	protected void generateReport(
			Collection<RuleSet> ruleSets, 
			PrintStream out) {
		
		for (RuleSet ruleSet: ruleSets) {
			out.println(ruleSet.getId() + ": " + ruleSet.getName());				
			for (Rule rule: ruleSet.getRules()) {
				out.println("\t" + rule.getId() + ": " + rule.getName());
			}
		}				
	}

}
