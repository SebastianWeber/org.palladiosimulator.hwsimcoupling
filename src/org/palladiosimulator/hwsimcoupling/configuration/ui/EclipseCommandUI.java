package org.palladiosimulator.hwsimcoupling.configuration.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class EclipseCommandUI {
	
	protected Composite parent;
	
	public EclipseCommandUI(Composite parent) {
		this.parent = parent;
	}
		
	public abstract Control createUI();
	
}
