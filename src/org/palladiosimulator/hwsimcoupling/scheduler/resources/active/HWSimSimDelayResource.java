package org.palladiosimulator.hwsimcoupling.scheduler.resources.active;

import java.io.Serializable;
import java.util.Map;

import org.palladiosimulator.hwsimcoupling.CacheInitializer;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.DemandCache;
import org.palladiosimulator.hwsimcoupling.util.DemandCache.RESOURCE;
import de.uka.ipd.sdq.scheduler.ISchedulableProcess;
import de.uka.ipd.sdq.scheduler.SchedulerModel;
import de.uka.ipd.sdq.scheduler.resources.active.IResourceTableManager;
import de.uka.ipd.sdq.scheduler.resources.active.SimDelayResource;
import de.uka.ipd.sdq.simucomframework.model.SimuComModel;

public class HWSimSimDelayResource extends SimDelayResource {

    private static DemandCache demandCache;

    public HWSimSimDelayResource(SchedulerModel model, String name, String id,
            IResourceTableManager resourceTableManager) {
        super(model, name, id, resourceTableManager);
        demandCache = CacheInitializer.getInstance()
            .getDemandCache();
    }

    @Override
    protected void doProcessing(ISchedulableProcess process, int resourceServiceID,
            Map<String, Serializable> parameterMap, double demand) {
        ResourceModelHelper.addCurrentContainerID((SimuComModel) this.getModel(), this, parameterMap);
        CommandHandler commandHandler = CacheInitializer.getInstance()
            .getExtensionCache()
            .getCommandHandler(parameterMap, CacheInitializer.getInstance()
                .getProfileCache());
        this.doProcessing(process, resourceServiceID, demandCache.get(parameterMap, RESOURCE.CPU, commandHandler));

    }
}
