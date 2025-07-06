package org.nasdanika.launcher.demo.http;

import org.json.JSONObject;
import org.nasdanika.exec.content.ContentFactory;
import org.nasdanika.exec.content.Markdown;
import org.nasdanika.html.HTMLFactory;
import org.nasdanika.html.HTMLPage;
import org.nasdanika.html.bootstrap.BootstrapFactory;
import org.nasdanika.html.bootstrap.Breakpoint;
import org.nasdanika.html.bootstrap.Size;
import org.nasdanika.html.bootstrap.Theme;
import org.nasdanika.http.HttpServerRouteBuilder;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder.Route;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder.RouteBuilder;
import org.nasdanika.models.bootstrap.Column;
import org.nasdanika.models.bootstrap.ColumnWidth;
import org.nasdanika.models.bootstrap.Container;
import org.nasdanika.models.bootstrap.Page;
import org.nasdanika.models.bootstrap.Row;
import org.nasdanika.models.html.HtmlFactory;
import org.nasdanika.models.html.Tag;
import org.nasdanika.ncore.NcoreFactory;
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
	
	// --- Test HTML producers ---
	
	/**
	 * Testing producer creation using HtlmGenerator
	 * @param request
	 * @param response
	 * @return
	 */
	@Route
	public Page getPage(
			HttpServerRequest request, 
			HttpServerResponse response) {
		
		Tag tag = HtmlFactory.eINSTANCE.createTag();
		tag.setName("b");
		tag.getAttributes().put("style", org.nasdanika.ncore.String.wrap("margin:5px"));
		tag.getContent().add(org.nasdanika.ncore.String.wrap("Hello World"));
		
		org.nasdanika.models.bootstrap.BootstrapFactory bootstrapFactory = org.nasdanika.models.bootstrap.BootstrapFactory.eINSTANCE;
		Page page = bootstrapFactory.createPage();
		page.setName("Test page");
		page.setTheme(Theme.Cerulean);		
		
		Container container = bootstrapFactory.createContainer();
		container.setFluid(true);
		page.getBody().add(container);
		
		Row row = bootstrapFactory.createRow();
		container.getRows().add(row);
		
		Column col = bootstrapFactory.createColumn();
		row.getColumns().add(col);
		
		col.getContent().add(tag);
		
		ColumnWidth colWidth = bootstrapFactory.createColumnWidth();
		colWidth.setWidth(Size.S0.name());
		colWidth.setBreakpoint(Breakpoint.DEFAULT.name());
		col.getWidth().add(colWidth);
								
		return page;
	}	
	
	/**
	 * Testing producer creation using HtlmGenerator
	 * @param request
	 * @param response
	 * @return
	 */
	@Route
	public org.nasdanika.ncore.String getNcoreString(
			HttpServerRequest request, 
			HttpServerResponse response) {
		org.nasdanika.ncore.String result = NcoreFactory.eINSTANCE.createString();
		result.setValue("Hello World!");
		return result;
	}
	
	/**
	 * Testing producer creation using HtlmGenerator
	 * @param request
	 * @param response
	 * @return
	 */
	@Route
	public Markdown getMarkdown(
			HttpServerRequest request, 
			HttpServerResponse response) {

		org.nasdanika.exec.content.Text text = ContentFactory.eINSTANCE.createText();
		text.setContent("Hello *World*");
		
		Markdown markdown = ContentFactory.eINSTANCE.createMarkdown();
		markdown.setSource(text);
		return markdown;
	}
	
}
