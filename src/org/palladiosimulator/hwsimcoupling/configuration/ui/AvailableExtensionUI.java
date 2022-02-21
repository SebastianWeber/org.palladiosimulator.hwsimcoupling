package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.palladiosimulator.hwsimcoupling.configuration.ProfileCache;

public class AvailableExtensionUI extends EclipseCommandUI {

	public AvailableExtensionUI(Shell shell) {
		super(shell);
		
	}

	@Override
	public void createUI() {
		Table table = new Table (shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		
		String[] titles = {"HWSim", "Parameters"};
		for (String title : titles) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
		}
		
		Map<String, String> parameters = ProfileCache.getInstance().getRequiredParameters();
		for (Entry<String, String> parameter : parameters.entrySet()) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, parameter.getKey());
			item.setText (1, parameter.getValue());
		}
		
		for (int i=0; i<titles.length; i++) {
			table.getColumn(i).pack();
		}
	}


}
