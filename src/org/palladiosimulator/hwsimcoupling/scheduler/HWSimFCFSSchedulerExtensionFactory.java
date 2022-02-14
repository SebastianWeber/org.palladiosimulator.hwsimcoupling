package org.palladiosimulator.hwsimcoupling.scheduler;

import org.palladiosimulator.hwsimcoupling.scheduler.resources.active.HWSimSimFCFSResource;

import de.uka.ipd.sdq.scheduler.IActiveResource;
import de.uka.ipd.sdq.scheduler.SchedulerModel;
import de.uka.ipd.sdq.scheduler.factory.SchedulerExtensionFactory;
import de.uka.ipd.sdq.scheduler.resources.active.IResourceTableManager;

public class HWSimFCFSSchedulerExtensionFactory implements SchedulerExtensionFactory {

	public HWSimFCFSSchedulerExtensionFactory() {
		
	}
	
	@Override
	public IActiveResource getExtensionScheduler(SchedulerModel model, String resourceName, String resourceId,
			long numberOfCores, IResourceTableManager resourceTableManager) {

		return new HWSimSimFCFSResource(model, resourceId, resourceId, numberOfCores, resourceTableManager);
	}

}
