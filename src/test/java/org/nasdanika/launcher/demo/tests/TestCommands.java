package org.nasdanika.launcher.demo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;
import org.nasdanika.launcher.demo.IAnnotatedElementProviderTestCommand;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.IAnnotatedElementProvider;
import picocli.CommandLine.Model.OptionSpec;


public class TestCommands {
	
    @Test
    public void testAnnotatedElementAccess() throws Exception {
    	IAnnotatedElementProviderTestCommand command = new IAnnotatedElementProviderTestCommand();
        CommandLine commandLine = new CommandLine(command);
        CommandSpec spec = commandLine.getCommandSpec();
        for (OptionSpec option: spec.options()) {
        	assertTrue(option.setter() instanceof IAnnotatedElementProvider);
        	assertTrue(option.getter() instanceof IAnnotatedElementProvider);
        	        
        	String optionName = option.names()[0];
			AnnotatedElement setterAnnotatedElement = ((IAnnotatedElementProvider) option.setter()).getAnnotatedElement();
			AnnotatedElement getterAnnotatedElement = ((IAnnotatedElementProvider) option.getter()).getAnnotatedElement();
			if ("-a".equals(optionName) || "-b".equals(optionName)) {
	        	assertTrue(setterAnnotatedElement instanceof Field);
	        	assertTrue(getterAnnotatedElement instanceof Field);
	        	
	        	Field setterField = (Field) setterAnnotatedElement;
	        	assertEquals(IAnnotatedElementProviderTestCommand.class, setterField.getDeclaringClass());
	        	assertEquals(optionName.substring(1), setterField.getName());
				
	        	Field getterField = (Field) getterAnnotatedElement;
	        	assertEquals(IAnnotatedElementProviderTestCommand.class, getterField.getDeclaringClass());
	        	assertEquals(optionName.substring(1), getterField.getName());								
			} else if ("-c".equals(optionName)) {
	        	assertTrue(setterAnnotatedElement instanceof Method);
	        	assertTrue(getterAnnotatedElement instanceof Method);
	        	
	        	Method setterMethod = (Method) setterAnnotatedElement;
	        	assertEquals(IAnnotatedElementProviderTestCommand.class, setterMethod.getDeclaringClass());
	        	assertEquals("setC", setterMethod.getName());
				
	        	Method getterMethod = (Method) getterAnnotatedElement;
	        	assertEquals(IAnnotatedElementProviderTestCommand.class, getterMethod.getDeclaringClass());
	        	assertEquals("setC", getterMethod.getName());								
			} else {
				fail("Unexpected option: " + optionName);
	        }        	        	        	
        }
        
        // Testing sub-command
        for (Entry<String, CommandLine> subCommandEntry: commandLine.getSubcommands().entrySet()) {
        	if ("commit".equals(subCommandEntry.getKey())) {
        		CommandLine subCommand = subCommandEntry.getValue();
                CommandSpec subCommandSpec = subCommand.getCommandSpec();
                Object userObject = subCommandSpec.userObject();
                assertTrue(userObject instanceof Method);
				for (OptionSpec option: subCommandSpec.options()) {
					System.out.println(option.shortestName());
                }
        	} else {
				fail("Unexpected sub-command: " + subCommandEntry.getKey());        		
        	}
        }
        
    }
	

}
