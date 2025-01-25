package org.nasdanika.launcher.demo.http;

import org.nasdanika.common.Reflector;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder.Route;

/**
 * Demo of a hierarchical routing with factory
 */
@Route("/factory")
public class ReflectorFactory {
	
	@Route("/demo")
	@Reflector.Factory
	public DemoReflectiveHttpRoutes getRoutes() {
		return new DemoReflectiveHttpRoutes();
	}	

}
