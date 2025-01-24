package org.nasdanika.launcher.demo.http;

import java.util.concurrent.CompletionStage;

import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.http.HttpServerRouteBuilder;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder;

public class DemoReflectiveHttpRoutesFactory extends ServiceCapabilityFactory<Void, HttpServerRouteBuilder> {
		
	@Override
	public boolean isFor(Class<?> type, Object requirement) {
		return HttpServerRouteBuilder.class == type && requirement == null;
	}

	@Override
	protected CompletionStage<Iterable<CapabilityProvider<HttpServerRouteBuilder>>> createService(
			Class<HttpServerRouteBuilder> serviceType, 
			Void serviceRequirement, 
			Loader loader,
			ProgressMonitor progressMonitor) {
		
		ReflectiveHttpServerRouteBuilder builder = new ReflectiveHttpServerRouteBuilder();
		builder.addTargets("/reflective", new DemoReflectiveHttpRoutes());				
		return wrap(builder);
	}
	
}