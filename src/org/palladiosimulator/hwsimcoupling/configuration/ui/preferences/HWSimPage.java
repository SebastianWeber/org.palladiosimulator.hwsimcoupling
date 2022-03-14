package org.palladiosimulator.hwsimcoupling.configuration.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class HWSimPage extends PreferencePage implements IWorkbenchPreferencePage {

    public HWSimPage() {
        super();
    }

    public HWSimPage(String title) {
        super(title);
    }

    public HWSimPage(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    public void init(IWorkbench workbench) {
        noDefaultAndApplyButton();
    }

    @Override
    protected Control createContents(Composite parent) {
        return null;
    }

}
