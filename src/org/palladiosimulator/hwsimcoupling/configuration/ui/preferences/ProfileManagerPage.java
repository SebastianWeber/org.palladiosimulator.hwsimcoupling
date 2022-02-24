package org.palladiosimulator.hwsimcoupling.configuration.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.palladiosimulator.hwsimcoupling.configuration.ui.ProfileConfigurationUI;

public class ProfileManagerPage extends PreferencePage implements IWorkbenchPreferencePage {
	
	public ProfileManagerPage() {
		super();
	}

	public ProfileManagerPage(String title) {
		super(title);
	}

	public ProfileManagerPage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	public void init(IWorkbench workbench) {
		noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {
		return new ProfileConfigurationUI(parent).createUI();
	}

}
