package org.palladiosimulator.hwsimcoupling.scheduler.resources.active;

import java.io.Serializable;
import java.util.Map;

import org.palladiosimulator.hwsimcoupling.configuration.HWsimCouplingManager;
import org.palladiosimulator.hwsimcoupling.util.CommandHandler;
import org.palladiosimulator.hwsimcoupling.util.MapHelper;
import org.palladiosimulator.hwsimcoupling.util.DemandCache.RESOURCE;
import org.palladiosimulator.hwsimcoupling.util.impl.DemandCacheImpl;

import de.uka.ipd.sdq.scheduler.ISchedulableProcess;
import de.uka.ipd.sdq.scheduler.SchedulerModel;
import de.uka.ipd.sdq.scheduler.resources.active.IResourceTableManager;
import de.uka.ipd.sdq.scheduler.resources.active.SimProcessorSharingResource;

public class HWSimSimProcessorSharingResource extends SimProcessorSharingResource {
	
	private static DemandCacheImpl demandCache;
	
	public HWSimSimProcessorSharingResource(SchedulerModel model, String name, String id, long capacity,
			IResourceTableManager resourceTableManager) {
		super(model, name, id, capacity, resourceTableManager);
		demandCache = HWsimCouplingManager.getSharedDemandCacheImpl();
	}
	
	@Override
	protected void doProcessing(ISchedulableProcess process, int resourceServiceID,
            Map<String, Serializable> parameterMap, double demand) {
		CommandHandler commandHandler = HWsimCouplingManager.getCommandHandler(MapHelper.get_required_value_from_map(parameterMap, "hwsim"));
		this.doProcessing(process, resourceServiceID, demandCache.get(parameterMap, RESOURCE.CPU, commandHandler));

    }
}
