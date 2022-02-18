package org.palladiosimulator.hwsimcoupling.scheduler;

import org.palladiosimulator.hwsimcoupling.scheduler.resources.active.HWSimSimDelayResource;

import de.uka.ipd.sdq.scheduler.IActiveResource;
import de.uka.ipd.sdq.scheduler.SchedulerModel;
import de.uka.ipd.sdq.scheduler.factory.SchedulerExtensionFactory;
import de.uka.ipd.sdq.scheduler.resources.active.IResourceTableManager;

public class HWSimDelaySchedulerExtensionFactory implements SchedulerExtensionFactory {

	public HWSimDelaySchedulerExtensionFactory() {
		
	}
	
	@Override
	public IActiveResource getExtensionScheduler(SchedulerModel model, String resourceName, String resourceId,
			long numberOfCores, IResourceTableManager resourceTableManager) {
		return new HWSimSimDelayResource(model, resourceId, resourceId, resourceTableManager);
	}

}
