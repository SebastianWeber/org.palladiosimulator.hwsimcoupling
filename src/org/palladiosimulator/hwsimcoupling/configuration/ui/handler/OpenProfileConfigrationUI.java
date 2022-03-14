package org.palladiosimulator.hwsimcoupling.configuration.ui.handler;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.hwsimcoupling.configuration.ui.ProfileConfigurationUI;
import org.palladiosimulator.hwsimcoupling.configuration.ui.EclipseCommandUI;

public class OpenProfileConfigrationUI extends EclipseCommandHandler {

    @Override
    protected EclipseCommandUI getEclipseCommandUI(Shell shell) {
        return new ProfileConfigurationUI(shell);
    }

    @Override
    protected String getShellTitle() {
        return "HWSimCoupling Profile Manager";
    }

    @Override
    protected Layout getShellLayout() {
        return new GridLayout();
    }

}
