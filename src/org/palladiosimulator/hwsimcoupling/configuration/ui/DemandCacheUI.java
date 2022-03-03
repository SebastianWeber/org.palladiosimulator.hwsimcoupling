package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.palladiosimulator.hwsimcoupling.configuration.ProfileCache;
import org.palladiosimulator.hwsimcoupling.util.impl.DemandCacheImpl;

public class DemandCacheUI extends EclipseCommandUI {
	
	private DemandCacheImpl demandCacheImpl;
	private Map<String, String> demands;
	private Table table;

	public DemandCacheUI(Composite parent) {
		super(parent);
		demandCacheImpl = DemandCacheImpl.getInstance(ProfileCache.getInstance());
		
	}

	@Override
	public Control createUI() {
		table = UIUtility.createTable(parent, SWT.SINGLE | SWT.FULL_SELECTION);

		String[] titles = {"Parameters", "Demand"};
		demands = demandCacheImpl.getDemands();
		UIUtility.setTableData(table, titles, demands);
		Menu menu = UIUtility.addMenu(parent, table);
		MenuItem itemDel = UIUtility.addMenuItem(menu, "Delete cached demand");
		UIUtility.setMenuItemSelectionListener(itemDel, new DeleteItemSelectionListener());

		return table;
	}
	
	private class DeleteItemSelectionListener extends DefaultSelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			String key = table.getItem(table.getSelectionIndex()).getText(0);
			demands.remove(key);
			demandCacheImpl.removeDemand(key);
			table.remove(table.getSelectionIndex());
		}

	}
		
}
