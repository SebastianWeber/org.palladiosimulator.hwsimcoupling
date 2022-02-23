package org.palladiosimulator.hwsimcoupling;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.palladiosimulator.hwsimcoupling.configuration.ui.DemandCacheUI;

public class cachedDemandsPage extends PreferencePage implements IWorkbenchPreferencePage {

	public cachedDemandsPage() {
		super();
	}

	public cachedDemandsPage(String title) {
		super(title);
	}

	public cachedDemandsPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
		noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {
		return new DemandCacheUI(parent).createUI();
	}

}
