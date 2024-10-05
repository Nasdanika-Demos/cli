package org.nasdanika.launcher.demo.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.Test;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.common.Context;
import org.nasdanika.common.Diagnostic;
import org.nasdanika.common.MutableContext;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.html.model.app.gen.AppSiteGenerator;
import org.nasdanika.html.model.app.graph.emf.HtmlAppGenerator;
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
		resource.getContents().add(EcoreUtil.copy(ruleSet.resolve()));
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
	
	@Test
	public void testGenerateRuleSetDoc() throws Exception {
		CapabilityLoader capabilityLoader = new CapabilityLoader();
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		Iterable<CapabilityProvider<Object>> ruleSetProviders = capabilityLoader.load(ServiceCapabilityFactory.createRequirement(RuleSet.class), progressMonitor);
		Collection<Throwable> failures = new ArrayList<>();
		Collection<RuleSet> ruleSets = new ArrayList<>();
		for (CapabilityProvider<Object> provider: ruleSetProviders) {
			provider.getPublisher().subscribe(rs -> ruleSets.add((RuleSet) rs), failures::add);
		}
		for (Throwable th: failures) {
			th.printStackTrace();
		}
		assertTrue(failures.isEmpty(), failures.toString());
		
		MutableContext context = Context.EMPTY_CONTEXT.fork();
		Consumer<Diagnostic> diagnosticConsumer = d -> d.dump(System.out, 0);		
		
		for (RuleSet ruleSet: ruleSets) {
			File actionModelsDir = new File("target\\action-models\\");
			actionModelsDir.mkdirs();
									
			File output = new File(actionModelsDir, ruleSet.getId() + "-actions.xmi");
					
			HtmlAppGenerator htmlAppGenerator = HtmlAppGenerator.load(
					Collections.singleton(ruleSet), 
					context, 
					null, 
					null, 
					null, 
					diagnosticConsumer, 
					progressMonitor);
			
			htmlAppGenerator.generateHtmlAppModel(
					diagnosticConsumer, 
					output,
					progressMonitor);
					
			// Generating a web site
			String rootActionResource = "test-data/doc-gen/actions.yml";
			URI rootActionURI = URI.createFileURI(new File(rootActionResource).getAbsolutePath());//.appendFragment("/");
			
			String pageTemplateResource = "test-data/doc-gen/page-template.yml";
			URI pageTemplateURI = URI.createFileURI(new File(pageTemplateResource).getAbsolutePath());//.appendFragment("/");
			
			String siteMapDomain = "https://architecture.models.nasdanika.org/demos/internet-banking-system";		
			AppSiteGenerator actionSiteGenerator = new AppSiteGenerator() {
				
				protected boolean isDeleteOutputPath(String path) {
					return !"CNAME".equals(path);				
				};
				
			};		
			
			Map<String, Collection<String>> errors = actionSiteGenerator.generate(
					rootActionURI, 
					pageTemplateURI, 
					siteMapDomain, 
					new File("target/rule-set-doc"), // Publishing to the repository's docs directory for GitHub pages 
					new File("target/doc-gen-work-dir"), 
					true);
					
			int errorCount = 0;
			for (Entry<String, Collection<String>> ee: errors.entrySet()) {
				System.err.println(ee.getKey());
				for (String error: ee.getValue()) {
					System.err.println("\t" + error);
					++errorCount;
				}
			}
			
			System.out.println("There are " + errorCount + " site errors");
		}
		
	}
	

}
