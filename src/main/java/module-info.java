import org.nasdanika.capability.CapabilityFactory;
import org.nasdanika.launcher.demo.ModuleGraphCommandFactory;
import org.nasdanika.launcher.demo.rules.DemoRuleSetCapabilityFactory;
import org.nasdanika.launcher.demo.rules.InspectYamlCommandFactory;
import org.nasdanika.launcher.demo.rules.ListRulesCommandFactory;
import org.nasdanika.launcher.demo.rules.inspectors.ReflectiveInspectorFactory;

module org.nasdanika.launcher.demo {
	
	requires org.nasdanika.launcher;
	requires org.nasdanika.models.rules.cli;
	requires org.nasdanika.models.echarts.graph;
	
	opens org.nasdanika.launcher.demo to info.picocli;
	opens org.nasdanika.launcher.demo.rules; // to info.picocli, org.nasdanika.common;
	opens org.nasdanika.launcher.demo.rules.inspectors to org.nasdanika.common; // For inspector reflection
	
	provides CapabilityFactory with 
		InspectYamlCommandFactory,
		ReflectiveInspectorFactory,
		ModuleGraphCommandFactory,
		ListRulesCommandFactory,
		DemoRuleSetCapabilityFactory;
		
}