package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.palladiosimulator.hwsimcoupling.CacheInitializer;
import org.palladiosimulator.hwsimcoupling.util.DemandCache;

public class DemandCacheUI extends EclipseCommandUI {

    private DemandCache demandCache;
    private Map<String, String> demands;
    private Table table;

    public DemandCacheUI(Composite parent) {
        super(parent);
        demandCache = CacheInitializer.getInstance()
            .getDemandCache();
    }

    @Override
    public Control createUI() {
        table = UIUtility.createTable(parent, SWT.SINGLE | SWT.FULL_SELECTION);

        String[] titles = { "Parameters", "Demand" };
        demands = demandCache.getSerializedDemands();
        UIUtility.setTableData(table, titles, demands);
        Menu menu = UIUtility.addMenu(parent, table);
        MenuItem itemDel = UIUtility.addMenuItem(menu, "Delete cached demand");
        MenuItem itemCopyKey = UIUtility.addMenuItem(menu, "Copy selected key");
        MenuItem itemCopyDemand = UIUtility.addMenuItem(menu, "Copy selected demand");
        UIUtility.setMenuItemSelectionListener(itemDel, new DeleteItemSelectionListener());
        UIUtility.setMenuItemSelectionListener(itemCopyKey, new CopyKeySelectionListener());
        UIUtility.setMenuItemSelectionListener(itemCopyDemand, new CopyDemandSelectionListener());

        return table;
    }

    private class DeleteItemSelectionListener extends DefaultSelectionListener {

        @Override
        public void widgetSelected(SelectionEvent e) {
            String key = table.getItem(table.getSelectionIndex())
                .getText(0);
            demands.remove(key);
            demandCache.removeDemand(key);
            table.remove(table.getSelectionIndex());
        }

    }

    private class CopyKeySelectionListener extends DefaultSelectionListener {

        @Override
        public void widgetSelected(SelectionEvent e) {
            StringSelection data = new StringSelection(table.getItem(table.getSelectionIndex())
                .getText(0));
            Clipboard cb = Toolkit.getDefaultToolkit()
                .getSystemClipboard();
            cb.setContents(data, data);
        }

    }

    private class CopyDemandSelectionListener extends DefaultSelectionListener {

        @Override
        public void widgetSelected(SelectionEvent e) {
            StringSelection data = new StringSelection(table.getItem(table.getSelectionIndex())
                .getText(1));
            Clipboard cb = Toolkit.getDefaultToolkit()
                .getSystemClipboard();
            cb.setContents(data, data);
        }

    }

}
