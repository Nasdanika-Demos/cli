package org.nasdanika.launcher.demo;

import java.io.IOException;
import java.util.Date;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.capability.ServiceCapabilityFactory.Requirement;
import org.nasdanika.capability.emf.ResourceSetRequirement;
import org.nasdanika.cli.CommandBase;
import org.nasdanika.cli.Description;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.models.gitlab.Project;
import org.nasdanika.models.gitlab.cli.GitLabContributorCommand.Result;
import org.nasdanika.models.gitlab.cli.GitLabRetrospectCommand;
import org.nasdanika.models.gitlab.util.GitLabURIHandler;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(
		description = "Demo retrospect command",
		name = "demo",
		mixinStandardHelpOptions = true)
@ParentCommands(GitLabRetrospectCommand.class)
@Description(icon = "https://docs.nasdanika.org/images/demo.svg")
public class GitLabDemoRetrospectCommand extends CommandBase {
	
	public GitLabDemoRetrospectCommand(CapabilityLoader capabilityLoader) {
		super(capabilityLoader);
	}
	
	@ParentCommand
	private GitLabRetrospectCommand parent;
	
	protected String apply(GitLabURIHandler gitLabURIHandler, Date since, Date until, Project project) throws IOException {
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		Requirement<ResourceSetRequirement, ResourceSet> requirement = ServiceCapabilityFactory.createRequirement(ResourceSet.class);		
		ResourceSet resourceSet = capabilityLoader.loadOne(requirement, progressMonitor);
		resourceSet.getURIConverter().getURIHandlers().add(0, gitLabURIHandler);
		URI modelURI = URI.createURI(GitLabURIHandler.GITLAB_URI_SCHEME + "://bank/main/retrospect-" + Long.toString(System.currentTimeMillis(), Character.MAX_RADIX) + ".xml");
		Resource model = resourceSet.createResource(modelURI);
		model.getContents().add(project);
		model.save(null);
		return "Hello";		
	}

	@Override
	public Integer call() throws Exception {
		Result<String> result = parent.apply(this::apply);				
		System.out.println(result);
		return 0;
	}
	
	
}
