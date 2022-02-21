package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.palladiosimulator.hwsimcoupling.util.impl.DemandCacheImpl;

public class DemandCacheUI extends EclipseCommandUI {
	
	private DemandCacheImpl demandCacheImpl;

	public DemandCacheUI(Shell shell) {
		super(shell);
		demandCacheImpl = DemandCacheImpl.getInstance();
		
	}

	@Override
	public void createUI() {
		Table table = new Table (shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		
		String[] titles = {"Parameters", "Demand"};
		for (String title : titles) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
		}
		
		Map<String, String> parameters = demandCacheImpl.getDemands();
		for (Entry<String, String> parameter : parameters.entrySet()) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, parameter.getKey());
			item.setText (1, parameter.getValue());
		}
		
		for (int i=0; i<titles.length; i++) {
			table.getColumn(i).pack();
		}
		
		Menu menu = new Menu (shell, SWT.POP_UP);
		table.setMenu (menu);
		MenuItem itemDel = new MenuItem (menu, SWT.PUSH);
		itemDel.setText ("Delete cached demand");
		itemDel.addListener (SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				String key = table.getItem(table.getSelectionIndex()).getText(0);
				parameters.remove(key);
				demandCacheImpl.removeDemand(key);
				table.remove(table.getSelectionIndex());
			}
		});
	}
		
}
