package org.nasdanika.launcher.demo.drawio;

import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.nasdanika.capability.CapabilityFactory.Loader;
import org.nasdanika.common.Invocable;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.drawio.Node;
import org.nasdanika.graph.Element;
import org.nasdanika.graph.processor.ConnectionProcessorConfig;
import org.nasdanika.graph.processor.NodeProcessorConfig;
import org.nasdanika.graph.processor.ProcessorConfig;
import org.nasdanika.graph.processor.ProcessorElement;
import org.nasdanika.graph.processor.ProcessorInfo;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

/**
 * Diagram element processor which processes HTTP requests 
 */
public class SystemHttpHandler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {
	
	private String amount;
	
	@ProcessorElement
	public void setElement(Node element) {
		this.amount = element.getProperty("amount");
	}

	/**
	 * This is the constructor signature for graph processor classes which are to be instantiated by URIInvocableCapabilityFactory (org.nasdanika.capability.factories.URIInvocableCapabilityFactory).
	 * Config may be of specific types {@link ProcessorConfig} - {@link NodeProcessorConfig} or {@link ConnectionProcessorConfig}.  
	 * @param loader
	 * @param loaderProgressMonitor
	 * @param data
	 * @param fragment
	 * @param config
	 * @param infoProvider
	 * @param endpointWiringStageConsumer
	 * @param wiringProgressMonitor
	 */
	public SystemHttpHandler(
			Loader loader,
			ProgressMonitor loaderProgressMonitor,
			Object data,
			String fragment,
			ProcessorConfig config,
			BiConsumer<Element, BiConsumer<ProcessorInfo<Invocable>, ProgressMonitor>> infoProvider,
			Consumer<CompletionStage<?>> endpointWiringStageConsumer,
			ProgressMonitor wiringProgressMonitor) {

	}

	@Override
	public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
		return response.sendString(Mono.just("Account: " + request.param("account") + ", Amount: " + amount));
	}

}
