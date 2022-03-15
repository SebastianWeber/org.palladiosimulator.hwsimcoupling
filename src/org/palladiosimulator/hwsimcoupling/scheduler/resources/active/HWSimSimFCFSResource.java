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
import de.uka.ipd.sdq.scheduler.resources.active.SimFCFSResource;
import de.uka.ipd.sdq.simucomframework.model.SimuComModel;

public class HWSimSimFCFSResource extends SimFCFSResource {

    private static DemandCache demandCache;

    public HWSimSimFCFSResource(SchedulerModel model, String name, String id, long capacity,
            IResourceTableManager resourceTableManager) {
        super(model, name, id, capacity, resourceTableManager);
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
