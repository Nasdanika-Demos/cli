package org.nasdanika.launcher.demo.rules;

import org.eclipse.emf.common.util.URI;
import org.nasdanika.common.Util;
import org.nasdanika.models.rules.util.RuleSetCapabilityFactory;

public class DemoRuleSetCapabilityFactory extends RuleSetCapabilityFactory {

	@Override
	protected URI getRuleSetURI() {
		return URI.createURI("demo-rule-set.yml").resolve(Util.createClassURI(getClass()));
	}

}
