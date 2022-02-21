package org.palladiosimulator.hwsimcoupling.configuration.ui;

import org.eclipse.swt.widgets.Shell;

public abstract class EclipseCommandUI {
	
	protected Shell shell;
	
	public EclipseCommandUI(Shell shell) {
		this.shell = shell;
	}
	
	public abstract void createUI();
	
}
