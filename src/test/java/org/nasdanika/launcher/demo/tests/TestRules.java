package org.nasdanika.launcher.demo.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.Test;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.models.rules.Rule;
import org.nasdanika.models.rules.RuleSet;
import org.nasdanika.ncore.util.NcoreUtil;

public class TestRules {
	
	@Test
	public void testLoadingRuleSet() {
//		System.out.println(DemoRuleSetCapabilityFactory.class.getResource("demo-rule-set.yml"));
//		System.out.println(DemoRuleSetCapabilityFactory.class.getClassLoader().getResource("org/nasdanika/launcher/demo/rules/demo-rule-set.yml"));
		
		CapabilityLoader capabilityLoader = new CapabilityLoader();
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		Iterable<CapabilityProvider<Object>> ruleSetProviders = capabilityLoader.load(ServiceCapabilityFactory.createRequirement(RuleSet.class), progressMonitor);
		Collection<Throwable> failures = new ArrayList<>();
		for (CapabilityProvider<Object> provider: ruleSetProviders) {
			provider.getPublisher().subscribe(rs -> dump((RuleSet) rs), failures::add);
		}
		for (Throwable th: failures) {
			th.printStackTrace();
		}
		assertTrue(failures.isEmpty(), failures.toString());
	}
	
	protected void dump(RuleSet ruleSet) {
		ResourceSet resourceSet = ruleSet.eResource().getResourceSet();
		File dump = new File("target/rule-set.xml");
		Resource resource = resourceSet.createResource(URI.createFileURI(dump.getAbsolutePath()));
		resource.getContents().add(EcoreUtil.copy(ruleSet));
		try {
			resource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(ruleSet.getId());
		System.out.println(NcoreUtil.getIdentifiers(ruleSet));
		System.out.println(ruleSet.getDocumentation());
		
		for (Rule rule: ruleSet.getRules()) {
			System.out.println(rule.getId());
			System.out.println(NcoreUtil.getIdentifiers(rule));
			System.out.println(rule.getSeverity().getName());
		}
		
	}

}
