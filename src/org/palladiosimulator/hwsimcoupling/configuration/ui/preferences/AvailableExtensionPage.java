package org.palladiosimulator.hwsimcoupling.configuration.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.palladiosimulator.hwsimcoupling.configuration.ui.AvailableExtensionUI;

public class AvailableExtensionPage extends PreferencePage implements IWorkbenchPreferencePage {

	public AvailableExtensionPage() {
		super();
	}

	public AvailableExtensionPage(String title) {
		super(title);
	}

	public AvailableExtensionPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
		noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {
		return new AvailableExtensionUI(parent).createUI();
	}

}
