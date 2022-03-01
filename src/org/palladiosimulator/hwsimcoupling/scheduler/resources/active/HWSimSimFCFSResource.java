package org.palladiosimulator.hwsimcoupling.scheduler.resources.active;

import java.io.Serializable;
import java.util.Map;

import org.palladiosimulator.hwsimcoupling.configuration.ExtensionManager;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.DemandCache.RESOURCE;
import org.palladiosimulator.hwsimcoupling.util.impl.DemandCacheImpl;

import de.uka.ipd.sdq.scheduler.ISchedulableProcess;
import de.uka.ipd.sdq.scheduler.SchedulerModel;
import de.uka.ipd.sdq.scheduler.resources.active.IResourceTableManager;
import de.uka.ipd.sdq.scheduler.resources.active.SimFCFSResource;
import de.uka.ipd.sdq.simucomframework.model.SimuComModel;

public class HWSimSimFCFSResource extends SimFCFSResource implements ResourceModelHelper {
	
	private static DemandCacheImpl demandCache;
	
	public HWSimSimFCFSResource(SchedulerModel model, String name, String id, long capacity,
			IResourceTableManager resourceTableManager) {
		super(model, name, id, capacity, resourceTableManager);
		demandCache = DemandCacheImpl.getInstance();
	}
	
	@Override
	protected void doProcessing(ISchedulableProcess process, int resourceServiceID,
            Map<String, Serializable> parameterMap, double demand) {
		addCurrentContainerID((SimuComModel)this.getModel(), parameterMap);
		CommandHandler commandHandler = ExtensionManager.getINSTANCE().getCommandHandler(parameterMap);
		this.doProcessing(process, resourceServiceID, demandCache.get(parameterMap, RESOURCE.CPU, commandHandler));

    }
}
