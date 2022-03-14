package org.palladiosimulator.hwsimcoupling.scheduler;

import org.palladiosimulator.hwsimcoupling.scheduler.resources.active.HWSimSimProcessorSharingResource;

import de.uka.ipd.sdq.scheduler.IActiveResource;
import de.uka.ipd.sdq.scheduler.SchedulerModel;
import de.uka.ipd.sdq.scheduler.factory.SchedulerExtensionFactory;
import de.uka.ipd.sdq.scheduler.resources.active.IResourceTableManager;

public class HWSimProcessorSharingSchedulerExtensionFactory implements SchedulerExtensionFactory {

    public HWSimProcessorSharingSchedulerExtensionFactory() {

    }

    @Override
    public IActiveResource getExtensionScheduler(SchedulerModel model, String resourceName, String resourceId,
            long numberOfCores, IResourceTableManager resourceTableManager) {

        return new HWSimSimProcessorSharingResource(model, resourceId, resourceId, numberOfCores, resourceTableManager);
    }

}
