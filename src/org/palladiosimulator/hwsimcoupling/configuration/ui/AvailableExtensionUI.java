package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.palladiosimulator.hwsimcoupling.configuration.ProfileCache;

public class AvailableExtensionUI extends EclipseCommandUI {

	public AvailableExtensionUI(Composite parent) {
		super(parent);
		
	}

	@Override
	public Control createUI() {
		return createTable();
	}
	
	private Control createTable() {
		Table table = UIUtility.createTable(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		String[] titles = {"HWSim", "Parameters"};
		Map<String, String> parameters = ProfileCache.getInstance().getRequiredParameters();
		UIUtility.setTableData(table, titles, parameters);
		return parent;
	}
	


}
