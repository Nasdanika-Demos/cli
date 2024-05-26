package org.nasdanika.launcher.demo.rules;

import java.util.Collection;

import org.nasdanika.models.rules.RuleSet;
import org.nasdanika.models.rules.cli.RuleSetsCommandFactory;

public class ListRulesCommandFactory extends RuleSetsCommandFactory<ListRulesCommand> {

	@Override
	protected Class<ListRulesCommand> getCommandType() {
		return ListRulesCommand.class;
	}

	@Override
	protected ListRulesCommand createCommand(Collection<RuleSet> ruleSets) {
		return new ListRulesCommand(ruleSets);
	}

}
