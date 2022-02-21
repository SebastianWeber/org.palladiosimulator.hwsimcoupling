package org.palladiosimulator.hwsimcoupling.configuration.ui.handler;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.hwsimcoupling.configuration.ui.AvailableExtensionUI;
import org.palladiosimulator.hwsimcoupling.configuration.ui.EclipseCommandUI;

public class OpenAvailableExtensionUI extends EclipseCommandHandler {

	@Override
	protected EclipseCommandUI getEclipseCommandUI(Shell shell) {
		return new AvailableExtensionUI(shell);
	}

	@Override
	protected String getShellTitle() {
		return "Available Extensions";
	}

	@Override
	protected Layout getShellLayout() {
		return new GridLayout();
	}

}
