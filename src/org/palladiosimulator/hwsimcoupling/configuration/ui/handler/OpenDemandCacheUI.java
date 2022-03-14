package org.palladiosimulator.hwsimcoupling.configuration.ui.handler;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.hwsimcoupling.configuration.ui.DemandCacheUI;
import org.palladiosimulator.hwsimcoupling.configuration.ui.EclipseCommandUI;

public class OpenDemandCacheUI extends EclipseCommandHandler {

    @Override
    protected EclipseCommandUI getEclipseCommandUI(Shell shell) {
        return new DemandCacheUI(shell);
    }

    @Override
    protected String getShellTitle() {
        return "Cached demands";
    }

    @Override
    protected Layout getShellLayout() {
        return new GridLayout();
    }

}
