package org.nasdanika.launcher.demo.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.RootCommand;
import org.nasdanika.common.NasdanikaException;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.common.Util;
import org.nasdanika.launcher.demo.ModuleVersionProvider;
import org.nasdanika.models.rules.Inspector;
import org.nasdanika.models.rules.Rule;
import org.nasdanika.models.rules.RuleSet;
import org.nasdanika.models.rules.cli.AbstractInspectorCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
		description = "Lists available rules",
		name = "list-inspectable-rules",
		versionProvider = ModuleVersionProvider.class,
		mixinStandardHelpOptions = true)
@ParentCommands(RootCommand.class)
public class ListInspectableRulesCommand extends AbstractInspectorCommand {
	
	public ListInspectableRulesCommand(CapabilityLoader capabilityLoader) {
		this.capabilityLoader = capabilityLoader;
	}
	
	@Option(names = {"-o", "--output"}, description = "Output file")
	private File output;	

	@Override
	public Integer call() throws Exception {
		ProgressMonitor progressMonitor = progressMonitorMixIn.createProgressMonitor(1);
		Inspector<Object> inspector = loadInspector(progressMonitor);
		Map<EObject, List<Rule>> grouped = Util.groupBy(inspector.getRules(), EObject::eContainer);
		if (output == null) {
			generateReport(grouped, System.out, progressMonitor);
		} else {
			try (PrintStream out = new PrintStream(output)) {
				generateReport(grouped, out, progressMonitor);
			} catch (FileNotFoundException e) {
				throw new NasdanikaException(e);
			}
		}
		return 0;
	}

	protected void generateReport(
			Map<EObject, List<Rule>> grouped, 
			PrintStream out, 
			ProgressMonitor progressMonitor) {
		
		for (Entry<EObject, List<Rule>> ge: grouped.entrySet().stream().sorted(this::compareRuleSetNames).toList()) {
			if (ge.getKey() instanceof RuleSet) {
				RuleSet ruleSet = (RuleSet) ge.getKey();
				out.println(ruleSet.getId() + ": " + ruleSet.getName());				
			} else {
				out.println("---");
			}
			for (Rule rule: ge.getValue()) {
				out.println("\t" + rule.getId() + ": " + rule.getName());
			}
		}				
	}
	
	protected int compareRuleSetNames(Map.Entry<EObject, List<Rule>> a, Map.Entry<EObject, List<Rule>> b) {
		EObject aKey = a.getKey();
		EObject bKey = b.getKey();
		if (aKey == null) {
			if (bKey == null) {
				return a.hashCode() - b.hashCode();
			}
			return 1;
		}
		if (bKey == null) {
			return -1;
		}
		if (aKey instanceof RuleSet) {
			if (bKey instanceof RuleSet) {
				String aName = ((RuleSet) aKey).getName();
				String bName = ((RuleSet) bKey).getName();
				if (aName == null) {
					if (bName == null) {
						return aKey.hashCode() - bKey.hashCode();
					}
					return 1;
				}
				
				if (bName == null) {
					return -1;
				}
				return aName.compareTo(bName);
			}
			return -1;
		}
		if (bKey instanceof RuleSet) {
			return 1;
		}
		return aKey.hashCode() - bKey.hashCode();		
	}

}
