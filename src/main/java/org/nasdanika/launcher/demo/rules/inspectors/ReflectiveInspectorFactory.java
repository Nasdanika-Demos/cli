package org.nasdanika.launcher.demo.rules.inspectors;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.nasdanika.capability.CapabilityProvider;
import org.nasdanika.capability.CapabilityFactory.Loader;
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
			Loader loader,
			ProgressMonitor progressMonitor) {

		InspectorSet inspector = new InspectorSet(
			RuleManager.LOADING_RULE_MANAGER, 
			serviceRequirement,
			false, 
			progressMonitor, 
			new DemoInspectors());
		
		return (serviceRequirement == null || serviceRequirement.test(inspector)) && !inspector.isEmpty() ? wrap(inspector) : empty();
	}

}
