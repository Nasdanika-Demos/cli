import org.nasdanika.capability.CapabilityFactory;
import org.nasdanika.launcher.demo.GitLabDemoRetrospectCommandFactory;
import org.nasdanika.launcher.demo.IAnnotatedElementProviderTestCommandFactory;
import org.nasdanika.launcher.demo.ModuleGraphCommandFactory;
import org.nasdanika.launcher.demo.ai.EnvironmentVariableKeyCredentialCapabilityFactory;
import org.nasdanika.launcher.demo.ai.OpenAIAdaEmbeddingsCapabilityFactory;
import org.nasdanika.launcher.demo.ai.OpenAIGpt4oChatCapabilityFactory;
import org.nasdanika.launcher.demo.ai.PdfIndexerCommandFactory;
import org.nasdanika.launcher.demo.drawio.DemoDiagramRoutesBuilderFactory;
import org.nasdanika.launcher.demo.http.DemoReflectiveHttpRoutesFactory;
import org.nasdanika.launcher.demo.java.DemoMavenSourceAnalysisCommandFactory;
import org.nasdanika.launcher.demo.rules.DemoRuleSetCapabilityFactory;
import org.nasdanika.launcher.demo.rules.InspectYamlCommandFactory;
import org.nasdanika.launcher.demo.rules.ListInspectableRulesCommandFactory;
import org.nasdanika.launcher.demo.rules.ListRulesCommandFactory;
import org.nasdanika.launcher.demo.rules.inspectors.ReflectiveInspectorFactory;

module org.nasdanika.launcher.demo {
	
	requires org.nasdanika.launcher;
	requires org.nasdanika.models.echarts.graph;
	requires org.nasdanika.models.app.graph;
//	requires java.sql;
//	requires java.xml;
	requires java.xml.bind;
	requires org.nasdanika.models.java.cli;
	requires reactor.netty.http;
	requires org.nasdanika.http;
	requires org.nasdanika.cli;
	requires org.nasdanika.models.app.cli;
	requires org.nasdanika.models.rules.cli;
	requires org.nasdanika.models.architecture;
	requires org.nasdanika.models.ecore.cli;
	requires org.nasdanika.ai.cli;
	requires org.nasdanika.models.pdf;
	requires org.nasdanika.ai.openai;
	
	opens org.nasdanika.launcher.demo to info.picocli;
	opens org.nasdanika.launcher.demo.java to info.picocli;
	opens org.nasdanika.launcher.demo.rules; // to info.picocli, org.nasdanika.common;
	opens org.nasdanika.launcher.demo.rules.inspectors to org.nasdanika.common; // For inspector reflection
	opens org.nasdanika.launcher.demo.drawio; // For processor instantiation and resource loading
	opens org.nasdanika.launcher.demo.http to org.nasdanika.common; // For inspector reflection
	opens org.nasdanika.launcher.demo.ai;
	
	provides CapabilityFactory with 
		EnvironmentVariableKeyCredentialCapabilityFactory,
		InspectYamlCommandFactory,
		ReflectiveInspectorFactory,
		ModuleGraphCommandFactory,
		ListRulesCommandFactory,
		ListInspectableRulesCommandFactory,
		DemoRuleSetCapabilityFactory,
		GitLabDemoRetrospectCommandFactory,
		DemoMavenSourceAnalysisCommandFactory,
		DemoDiagramRoutesBuilderFactory,
		IAnnotatedElementProviderTestCommandFactory,
		DemoReflectiveHttpRoutesFactory,
		OpenAIAdaEmbeddingsCapabilityFactory,
		OpenAIGpt4oChatCapabilityFactory,
		PdfIndexerCommandFactory;		
}