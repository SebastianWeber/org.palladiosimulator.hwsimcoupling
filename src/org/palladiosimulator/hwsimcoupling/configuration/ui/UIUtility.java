package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class UIUtility {

	public static Display getDisplay() {
	      Display display = Display.getCurrent();
	      if (display == null)
	         display = Display.getDefault();
	      return display;		
	   }
	
	public static void setTableData(Table table, String[] titles, Map<String, String> contents) {
		for (String title : titles) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
		}
		
		for (Entry<String, String> parameter : contents.entrySet()) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, parameter.getKey());
			item.setText (1, parameter.getValue());
		}
		
		for (int i=0; i<titles.length; i++) {
			table.getColumn(i).pack();
		}
	}
	
	public static Table createTable(Composite parent, int style) {
		Table table = new Table (parent, style);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		return table;
	}
	
	public static Menu addMenu(Composite parent, Table table) {
		Menu menu = new Menu(parent);
		table.setMenu(menu);
		return menu;
	}
	
	public static MenuItem addMenuItem(Menu menu, String title) {
		MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
		menuItem.setText(title);
		return menuItem;
	}
	
	public static void setMenuItemSelectionListener(MenuItem menuItem, SelectionListener selectionListener) {
		menuItem.addSelectionListener(selectionListener);
	}
	
	public static Composite createCompositeWithLayout(Composite parent, int style, int numColumns) {
		Composite composite = new Composite(parent, style);
		setGridLayout(composite, numColumns);
		return composite;
	}
	
	public static void setGridLayout(Composite composite, int numColumns) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = numColumns;
		composite.setLayout(gridLayout);
	}
	
	public static void addEmptyLabel(Composite composite) {
		new Label(composite, SWT.NONE);
	}
	
	public static Label createLabel(Composite parent, int style, String text) {
		Label label = new Label(parent, style);
		label.setText(text);
		return label;
	}
	
	public static Text createEditableText(Composite parent, int style) {
		Text text = new Text(parent, style);
		text.setEditable(true);
		return text;
	}
	
	public static Button createButton(Composite parent, int style, String text, SelectionListener selectionListener) {
		Button button = new Button(parent, style);
		button.setText(text);
		button.addSelectionListener(selectionListener);
	    return button;
	}
	
	public static TabItem createTabItemWithControl(TabFolder tabFolder, int style, String text, Control control) {
		TabItem tabItem = new TabItem(tabFolder, style);
		tabItem.setText(text);
		tabItem.setControl(control);
		return tabItem;
	}
	

	
}
