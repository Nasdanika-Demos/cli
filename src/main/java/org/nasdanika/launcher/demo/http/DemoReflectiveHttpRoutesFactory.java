package org.nasdanika.launcher.demo.http;

import java.util.concurrent.CompletionStage;

import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.http.HttpServerRouteBuilder;
import org.nasdanika.http.ReflectiveHttpServerRouteBuilder;
import org.nasdanika.http.TelemetryFilter;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

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
		
		Requirement<Object, OpenTelemetry> openTelemetryRequirement = ServiceCapabilityFactory.createRequirement(OpenTelemetry.class);
		CompletionStage<OpenTelemetry> openTelemetryCS = loader.loadOne(openTelemetryRequirement, progressMonitor);		
		
		return wrapCompletionStage(openTelemetryCS.thenApply(openTelemetry -> {
			Tracer tracer = openTelemetry.getTracer(DemoReflectiveHttpRoutesFactory.class.getName());
			TelemetryFilter telemetryFilter = new TelemetryFilter(
					tracer, 
					openTelemetry.getPropagators().getTextMapPropagator(), 
					(k, v) -> System.out.println(k + ": " + v), 
					true);
			ReflectiveHttpServerRouteBuilder builder = new ReflectiveHttpServerRouteBuilder(telemetryFilter);
			builder.addTargets("/reflective", new DemoReflectiveHttpRoutes());				
			return builder;
		}));
	}
	
}