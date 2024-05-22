package org.nasdanika.launcher.demo.rules.inspectors;

import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.nasdanika.models.rules.reflection.Inspector;
import org.nasdanika.models.rules.reflection.RuleSet;
import org.nasdanika.ncore.util.YamlResource;

/**
 * Testing implicit rule set first
 */

@RuleSet("""		
		name: Sample Java Rules
		severities:
		  error:
		    name: Error
		    description: Artifacts with this severity are not allowed to be further processed (e.g. deployed, published to a repository) 
		documentation:
		  exec.content.Markdown:
		    source:
		      exec.content.Text: |
		        TODO:
		        
		        * specRef attribute to RuleSet and Rule - support of loading from classloader resources 
		        * Generation of HTML documentation
		""")
public class ReflectiveInspectors {
	
	@Inspector(value = """
			name: Invalid YAML
			documentation:
			  exec.content.Markdown:
			    source:
			      exec.content.Text: |
			        YAML with syntax errors, e.g. duplicate keys.
			""",
			severity = "error",
			condition = "!errors.isEmpty()") 	
	public Collection<String> yamlInspector(YamlResource yamlResource) {
		return yamlResource.getErrors().stream().map(Diagnostic::getMessage).toList();
	}
	
}
