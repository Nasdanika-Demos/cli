package org.nasdanika.launcher.demo.http;

import org.json.JSONObject;
import org.nasdanika.html.HTMLFactory;
import org.nasdanika.html.HTMLPage;
import org.nasdanika.html.bootstrap.BootstrapFactory;
import org.nasdanika.http.HttpServerRouteBuilder;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder.Route;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder.RouteBuilder;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerRoutes;

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
	
	@RouteBuilder("field-route-builder")
	public HttpServerRouteBuilder routeBuilder = routes -> {
		routes.get("/hello", (request, response) -> response.sendString(Mono.just("Hello from field route builder!")));				
	};

	@RouteBuilder("getter-route-builder")
	public HttpServerRouteBuilder getRouteBuilder() {
		return  routes -> {
			routes.get("/hello", (request, response) -> response.sendString(Mono.just("Hello from getter route builder!")));
		};
	};	

	@RouteBuilder("route-builder-method")
	public void buildRoutes(HttpServerRoutes routes) {
		routes.get("/hello", (request, response) -> response.sendString(Mono.just("Hello from route builder method!")));
	};
	
	@Route
	public JSONObject getApiSearch(
			HttpServerRequest request, 
			HttpServerResponse response) {
		JSONObject result = new JSONObject();
		result.put("result", "Hello World!");
		return result;
	}
	
	@Route("index.html")
	public HTMLPage getHome(
			HttpServerRequest request, 
			HttpServerResponse response) {
		
		HTMLPage ret = BootstrapFactory.INSTANCE.bootstrapCdnHTMLPage();
		ret.body(HTMLFactory.INSTANCE.tag("b", "Hello world!"));
		return ret;
	}	
	
	@Route("do-something-else")
	public Mono<String> doSomethingElse(
			HttpServerRequest request, 
			HttpServerResponse response) {
		return Mono.just("do someting else");
	}	
	
}
