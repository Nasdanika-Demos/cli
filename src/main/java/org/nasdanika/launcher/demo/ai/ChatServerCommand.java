package org.nasdanika.launcher.demo.ai;

import java.io.File;
import java.util.Collection;

import org.nasdanika.common.Description;
import org.nasdanika.http.AbstractHttpServerCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import reactor.netty.http.server.HttpServerRoutes;

@Command(
		description = "Serves HTTP routes for AI chat",
		name = "chat-server")
public class ChatServerCommand extends AbstractHttpServerCommand {
	
	public ChatServerCommand() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Parameters(
		index =  "0",	
		arity = "1",
		description = "Index file")
	private File index;
	
	
	protected void buildRoutes(HttpServerRoutes routes) {
	}
	
	@Override
	public Integer call() throws Exception {
		startServer(this::buildRoutes);
		return 0;
	}

}
