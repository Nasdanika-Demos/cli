package org.nasdanika.launcher.demo.rules.inspectors;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.ServiceCapabilityFactory;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.models.rules.Inspector;
import org.nasdanika.models.rules.InspectorCapabilityFactory;
import org.nasdanika.models.rules.reflection.InspectorSet;
import org.nasdanika.models.rules.reflection.RuleManager;

public class ReflectiveInspectorFactory extends InspectorCapabilityFactory<Object> {

	@Override
	protected CompletionStage<Iterable<CapabilityProvider<Inspector<Object>>>> createService(
			Class<Inspector<Object>> serviceType, 
			Predicate<Inspector<Object>> serviceRequirement,
			BiFunction<Object, ProgressMonitor, CompletionStage<Iterable<CapabilityProvider<Object>>>> resolver,
			ProgressMonitor progressMonitor) {

		Inspector<Object> inspector = new InspectorSet(
			RuleManager.LOADING_RULE_MANAGER, 
			serviceRequirement,
			false, 
			progressMonitor, 
			new ReflectiveInspectors());
		
		return wrap(inspector);
	}

}
