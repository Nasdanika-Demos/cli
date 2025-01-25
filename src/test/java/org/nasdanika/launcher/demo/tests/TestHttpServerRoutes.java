package org.nasdanika.launcher.demo.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.emf.common.util.URI;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nasdanika.capability.CapabilityLoader;
import org.nasdanika.common.Component;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.drawio.Document;
import org.nasdanika.drawio.processor.ElementProcessorFactory;
import org.nasdanika.graph.Element;
import org.nasdanika.graph.processor.ProcessorConfig;
import org.nasdanika.graph.processor.ProcessorInfo;
import org.nasdanika.http.HttpServerRouteBuilder;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder;
import org.nasdanika.launcher.demo.http.DemoReflectiveHttpRoutes;
import org.nasdanika.launcher.demo.http.ReflectorSuperFactory;

import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

public class TestHttpServerRoutes {	
	
	private static void start(
			Element element, 
			Map<org.nasdanika.graph.Element,Component> components,
			ProgressMonitor progressMonitor) {
		Component component = components.get(element);
		if (component != null) {
			component.start(progressMonitor);
		}
		for (Element child: element.getChildren()) {
			start(child, components, progressMonitor);
		}		
	}	
	
	@Test
	@Disabled
	public void testDrawioRoutes() throws Exception {
		Document document = Document.load(
				URI.createFileURI(new File("test-data/drawio-http/diagram.drawio").getCanonicalPath()), 
				null, 
				null);
		
		Map<org.nasdanika.graph.Element,Component> components = new HashMap<>();
		
		ElementProcessorFactory<Object> elementProcessorFactory = new ElementProcessorFactory<Object>(
				document , 
				new CapabilityLoader(), 
				"processor") {

			/**
			 * This override is needed to collect processors implementing {@link Component}
			 */
			@Override
			protected Object doCreateProcessor(
					ProcessorConfig config, 
					boolean parallel,
					BiConsumer<org.nasdanika.graph.Element, BiConsumer<ProcessorInfo<Object>, ProgressMonitor>> infoProvider,
					Consumer<CompletionStage<?>> endpointWiringStageConsumer, 
					ProgressMonitor progressMonitor) {
				
				Object processor = super.doCreateProcessor(
						config, 
						parallel, 
						infoProvider, 
						endpointWiringStageConsumer, 
						progressMonitor);
				
				if (processor instanceof Component) {
					components.put(config.getElement(), (Component) processor);
				}
				return processor;
			}
		};
			
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		
		Map<Element, ProcessorInfo<Object>> processors = elementProcessorFactory.createProcessors(
				null, 
				null, 
				progressMonitor);
		
		// Starting
		start(document, components, progressMonitor);
		
		DisposableServer server = HttpServer
				.create()
				.port(8080)
				.route(routes -> HttpServerRouteBuilder.buildRoutes(processors.values(), "route", routes))
				.bindNow();
		
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
        	LineReader lineReader = LineReaderBuilder
        			.builder()
                    .terminal(terminal)
                    .build();
        	
        	String prompt = "http-server>";
            while (true) {
                String line = null;
                line = lineReader.readLine(prompt);
                System.out.println("Got: " + line);
                if ("exit".equals(line)) {
                	break;
                }
            }
        }
        
        server.dispose();
        server.onDispose().block();
						
		// Stopping 
		document.accept(e -> {
			Component component = components.get(e);
			if (component != null) {
				component.stop(progressMonitor);
			}
		});
			
		// Closing
		document.accept(e -> {
			Component component = components.get(e);
			if (component != null) {
				component.close(progressMonitor);
			}
		});
	}

	@Test
	@Disabled
	public void testDrawioRoutesSimple() throws Exception {
		Document document = Document.load(
				URI.createFileURI(new File("test-data/drawio-http/diagram.drawio").getCanonicalPath()), 
				null, 
				null);
		
		ElementProcessorFactory<Object> elementProcessorFactory = new ElementProcessorFactory<Object>(
				document, 
				new CapabilityLoader(), 
				"processor");
			
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		
		Map<Element, ProcessorInfo<Object>> processors = elementProcessorFactory.createProcessors(
				null, 
				null, 
				progressMonitor);
		
		DisposableServer server = HttpServer
				.create()
				.port(8080)
				.route(routes -> HttpServerRouteBuilder.buildRoutes(processors.values(), "route", routes))
				.bindNow();
		
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
        	LineReader lineReader = LineReaderBuilder
        			.builder()
                    .terminal(terminal)
                    .build();
        	
        	String prompt = "http-server>";
            while (true) {
                String line = null;
                line = lineReader.readLine(prompt);
                System.out.println("Got: " + line);
                if ("exit".equals(line)) {
                	break;
                }
            }
        }
        
        server.dispose();
        server.onDispose().block();
	}

	@Test
	@Disabled
	public void testDrawioRouteBuilder() throws Exception {
		Document document = Document.load(
				URI.createFileURI(new File("test-data/drawio-http/route-builder.drawio").getCanonicalPath()), 
				null, 
				null);
		
		ElementProcessorFactory<Object> elementProcessorFactory = new ElementProcessorFactory<Object>(
				document, 
				new CapabilityLoader(), 
				"processor");
			
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		
		Map<Element, ProcessorInfo<Object>> processors = elementProcessorFactory.createProcessors(
				null, 
				null, 
				progressMonitor);
		
		DisposableServer server = HttpServer
				.create()
				.port(8080)
				.route(routes -> HttpServerRouteBuilder.buildRoutes(processors.values(), "route", routes))
				.bindNow();
		
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
        	LineReader lineReader = LineReaderBuilder
        			.builder()
                    .terminal(terminal)
                    .build();
        	
        	String prompt = "http-server>";
            while (true) {
                String line = null;
                line = lineReader.readLine(prompt);
                System.out.println("Got: " + line);
                if ("exit".equals(line)) {
                	break;
                }
            }
        }
        
        server.dispose();
        server.onDispose().block();
	}

	@Test
	@Disabled
	public void testReflectiveRoutesBuilder() throws Exception {
		ReflectiveHttpServerRouteBuilder builder = new ReflectiveHttpServerRouteBuilder();
		builder.addTargets("/reflective", new DemoReflectiveHttpRoutes());
		
		DisposableServer server = HttpServer
				.create()
				.port(8080)
				.route(builder::buildRoutes)
				.bindNow();
		
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
        	LineReader lineReader = LineReaderBuilder
        			.builder()
                    .terminal(terminal)
                    .build();
        	
        	String prompt = "http-server>";
            while (true) {
                String line = null;
                line = lineReader.readLine(prompt);
                System.out.println("Got: " + line);
                if ("exit".equals(line)) {
                	break;
                }
            }
        }
        
        server.dispose();
        server.onDispose().block();
	}

	@Test
//	@Disabled
	public void testReflectorFactory() throws Exception {
		ReflectiveHttpServerRouteBuilder builder = new ReflectiveHttpServerRouteBuilder();
		builder.addTargets("/reflective", new ReflectorSuperFactory());
		
		DisposableServer server = HttpServer
				.create()
				.port(8080)
				.route(builder::buildRoutes)
				.bindNow();
		
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {
        	LineReader lineReader = LineReaderBuilder
        			.builder()
                    .terminal(terminal)
                    .build();
        	
        	String prompt = "http-server>";
            while (true) {
                String line = null;
                line = lineReader.readLine(prompt);
                System.out.println("Got: " + line);
                if ("exit".equals(line)) {
                	break;
                }
            }
        }
        
        server.dispose();
        server.onDispose().block();
	}
	
}
