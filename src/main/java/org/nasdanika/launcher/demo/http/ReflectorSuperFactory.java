package org.nasdanika.launcher.demo.http;

import org.nasdanika.common.Reflector;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder.Route;

/**
 * Demo of a hierarchical routing with factory
 */
@Route("/super-factory")
public class ReflectorSuperFactory {
	
	@Route("/super-demo")
	@Reflector.Factory
	public ReflectorFactory getRoutes() {
		return new ReflectorFactory();
	}	

}
