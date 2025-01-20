package org.nasdanika.launcher.demo.drawio;

import org.eclipse.emf.common.util.URI;
import org.nasdanika.common.Util;
import org.nasdanika.http.DiagramRoutesBuilderFactory;

public class DemoDiagramRoutesBuilderFactory extends DiagramRoutesBuilderFactory {

	public DemoDiagramRoutesBuilderFactory() {
		super(
				URI
					.createURI("system.drawio")
					.resolve(Util.createClassURI(DemoDiagramRoutesBuilderFactory.class)), 
				"processor", 
				"route");
	}
	
}