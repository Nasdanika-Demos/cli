package org.nasdanika.launcher.demo;

import java.io.File;

import org.nasdanika.cli.Description;
import org.nasdanika.cli.ParentCommands;
import org.nasdanika.cli.RootCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "annotated-element-provider-test")
@ParentCommands(RootCommand.class)
public class IAnnotatedElementProviderTestCommand {
    @Option(names = "-a") int a;
    @Option(names = "-b") long b;
    @Option(names = "-c")
    void setC(long c) {
    	
    }
    
    @Command
    void commit(
		@Option(names = {"-m", "--message"})
		@Description("Commit message detailed description")
		String commitMessage,
    		
        @Option(names = "--squash", paramLabel = "<commit>") 
		@Description("Squash detailed description")
		String squash,
		
        @Parameters(paramLabel = "<file>") 
		@Description("File detailed description")
		File[] files) {
        // ... implement business logic
    }       
}
