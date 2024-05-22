package org.nasdanika.launcher.demo.rules.inspectors;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.models.rules.Inspector;
import org.nasdanika.models.rules.reflection.InspectorSet;
import org.nasdanika.models.rules.reflection.RuleManager;

public class ReflectiveInspectorFactory extends ServiceCapabilityFactory<Object, Inspector<Object>> {

	@Override
	public boolean isForServiceType(Class<?> type) {
		return type == Inspector.class;
	}

	@Override
	protected CompletionStage<Iterable<CapabilityProvider<Inspector<Object>>>> createService(
			Class<Inspector<Object>> serviceType, 
			Object serviceRequirement,
			BiFunction<Object, ProgressMonitor, CompletionStage<Iterable<CapabilityProvider<Object>>>> resolver,
			ProgressMonitor progressMonitor) {

		Inspector<Object> inspector = new InspectorSet(
			RuleManager.LOADING_RULE_MANAGER, 
			false, 
			progressMonitor, 
			new ReflectiveInspectors());
		
		return wrap(inspector);
	}

}
