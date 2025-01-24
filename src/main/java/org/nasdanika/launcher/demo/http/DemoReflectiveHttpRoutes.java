package org.nasdanika.launcher.demo.http;

import org.nasdanika.http.ReflectiveHttpServerRouteBuilder.Route;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@Route("/test/")
public class DemoReflectiveHttpRoutes {

	/**
	 * In this handler method GET is derived from the get prefix
	 * and path is derived from Hello 
	 */
	@Route
	public Publisher<Void> getHello(
			HttpServerRequest request, 
			HttpServerResponse response) {
		return response.sendString(Mono.just("getHello()"));
	}
	
	/**
	 * In this handler the HTTP method is GET because the 
	 * first segment "hola" doesn't  match any HTTP method.
	 * The path is "hola/soy/dora"
	 */
	@Route
	public Publisher<Void> holaSoyDora(
			HttpServerRequest request, 
			HttpServerResponse response) {
		return response.sendString(Mono.just("holaSoyDora()"));
	}
	
	/**
	 * In this handler the path is explicitly specified
	 * by the Route annotation 
	 */
	@Route("do-something")
	public Publisher<Void> doSomething(
			HttpServerRequest request, 
			HttpServerResponse response) {
		return response.sendString(Mono.just("do someting"));
	}
	
}
