package org.palladiosimulator.hwsimcoupling.scheduler.resources.active;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import org.palladiosimulator.hwsimcoupling.configuration.Parameter;
import org.palladiosimulator.hwsimcoupling.exceptions.DemandCalculationFailureException;

import de.uka.ipd.sdq.simucomframework.model.SimuComModel;
import de.uka.ipd.sdq.simucomframework.resources.SimulatedResourceContainer;

public interface ResourceModelHelper {

    public default String getCurrentContainerID(SimuComModel simuComModel) {
        Optional<SimulatedResourceContainer> simContainer = simuComModel.getResourceRegistry()
            .getSimulatedResourceContainers()
            .stream()
            .filter(c -> c.getActiveResources()
                .stream()
                .anyMatch(r -> r.getUnderlyingResource() == this))
            .findFirst();
        if (simContainer.isEmpty()) {
            throw new DemandCalculationFailureException("Failed to get resource container of the scheduler");
        }
        return simContainer.get()
            .getResourceContainerID();
    }

    public default Map<String, Serializable> addCurrentContainerID(SimuComModel simuComModel,
            Map<String, Serializable> map) {
        map.put(Parameter.CONTAINERID.getKeyword(), getCurrentContainerID(simuComModel));
        return map;
    }

}
