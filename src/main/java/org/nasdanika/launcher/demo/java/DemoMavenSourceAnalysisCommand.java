package org.nasdanika.launcher.demo.java;

import java.util.Date;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.cli.Description;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.models.gitlab.Project;
import org.nasdanika.models.gitlab.cli.GitLabRetrospectCommand;
import org.nasdanika.models.gitlab.util.GitLabURIHandler;
import org.nasdanika.models.java.CompilationUnit;
import org.nasdanika.models.java.cli.MavenVisitCommand;

import picocli.CommandLine.Command;

@Command(
		description = "Demo source analysis command",
		name = "demo-analyze",
		mixinStandardHelpOptions = true)
@ParentCommands(GitLabRetrospectCommand.class)
@Description(icon = "https://docs.nasdanika.org/images/java-script.svg")
public class DemoMavenSourceAnalysisCommand extends MavenVisitCommand {

	public DemoMavenSourceAnalysisCommand(CapabilityLoader capabilityLoader) {
		super(capabilityLoader);
	}

	public DemoMavenSourceAnalysisCommand() {

	}

	@Override
	protected <T> Visitor<T> createVisitor(
			GitLabURIHandler gitLabURIHandler, 
			Date since, 
			Date until, 
			Project project,
			URI baseURI, 
			Model model, 
			ResourceSet resourceSet,
			ProgressMonitor progressMonitor) {
		
		return new Visitor<T>() {

			@Override
			public boolean shallVisit(
					URI uri, 
					String path, 
					ProgressMonitor progressMonitor) {				
				System.out.println("> " + uri + " -> " + path);				
				Build build = model.getBuild();
				String sourceDir = build == null ? "src/main/java/" : build.getSourceDirectory();				
				if (path != null 
						&& path.startsWith(sourceDir) 
						&& path.endsWith(CompilationUnit.JAVA_EXTENSION)) {
					return true; // We are interested in sources
				}
				return uri.toString().endsWith("/");
			}

			@Override
			public void visit(
					URI uri, 
					String path, 
					EObject obj, 
					ProgressMonitor progressMonitor) {
				System.out.println(">> "  + uri + " -> " + path + ": " + obj);				
			}

			@Override
			public T getResult() {
				return null;
			}
			
		};
	}		

}
