package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.hwsimcoupling.configuration.HWsimCouplingManager;

public class ConfigurationManager{
	
	private List<TabItemWithParameterList> tabItemsWithParameterList;
	
	public ConfigurationManager(Shell shell) {
		this.tabItemsWithParameterList = new ArrayList<TabItemWithParameterList>();
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		Map<String, Map<String, String>> profiles = HWsimCouplingManager.getParameters();
		for (Entry<String, Map<String, String>> profile : profiles.entrySet()) {
			TabItemWithParameterList tabItem = new TabItemWithParameterList(tabFolder, profile.getKey(), profile.getValue());
			this.tabItemsWithParameterList.add(tabItem);
		}
		
	    Button saveButton = new Button(shell, SWT.NONE);
	    saveButton.setText("Save");
	    saveButton.addSelectionListener(new SelectionListenerDummy() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				save(tabFolder);
			}
		});
	    
	    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 4;
        saveButton.setLayoutData(gridData);
		
		Menu menu = new Menu (shell, SWT.POP_UP);
		tabFolder.setMenu (menu);
		MenuItem itemDel = new MenuItem (menu, SWT.PUSH);
		itemDel.setText ("Delete Profile");
		itemDel.addListener (SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TabItem tabItem = tabFolder.getItem(tabFolder.getSelectionIndex());
				int indexToRemove = -1;
				for (int i = 0; i < tabItemsWithParameterList.size(); i++) {
					if (tabItemsWithParameterList.get(i).equalsTabItem(tabItem)) {
						indexToRemove = i;
					}
				}
				if (indexToRemove != -1) {
					tabItemsWithParameterList.remove(indexToRemove);
				}
				tabItem.dispose();
			}
		});
		MenuItem itemAdd = new MenuItem (menu, SWT.PUSH);
		itemAdd.setText ("Add Profile");
		itemAdd.addListener (SWT.Selection, event -> addTab(tabFolder));
	}
	
	private void addTab(TabFolder tabFolder) {
		this.tabItemsWithParameterList.add(new TabItemWithParameterList(tabFolder, "", new HashMap<String, String>()));
	}
	
	private void save(TabFolder tabFolder) {
		Map<String, Map<String, String>> profiles = new HashMap<String, Map<String, String>>();
		for (TabItemWithParameterList tabItem : this.tabItemsWithParameterList) {
			profiles.put(tabItem.getText(), tabItem.getParameters());
		}
		HWsimCouplingManager.saveParameters(profiles);
	}
	
	private abstract class SelectionListenerDummy implements SelectionListener {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}
	
	private void addEmptyLabel(Composite composite) {
		new Label(composite, SWT.NONE);
	}
	
	private class TabItemWithParameterList {

		private TabItem tabItem;
		private ParameterList parameterList;
		private Composite composite;
		
		public TabItemWithParameterList(TabFolder parent, String name, Map<String, String> parameters) {
			tabItem = new TabItem(parent, SWT.NONE);
			composite = new Composite(parent, SWT.NONE);
			GridLayout gridLayout = new GridLayout();
		    gridLayout.numColumns = 1;
		    composite.setLayout(gridLayout);
			Composite profileEditing = new Composite(composite, SWT.NONE);
		    GridLayout gridLayoutProfileEditing = new GridLayout();
		    gridLayoutProfileEditing.numColumns = 2;
		    profileEditing.setLayout(gridLayoutProfileEditing);
		    Text tabName = new Text(profileEditing, SWT.BORDER);
			tabName.setEditable(true);
		    Button saveEditingButton = new Button(profileEditing, SWT.NONE);
		    saveEditingButton.setText("Save Tab Name");
		    saveEditingButton.addSelectionListener(new SelectionListenerDummy() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					tabItem.setText(tabName.getText());
				}
				
			});
		    tabItem.setText(name);
			parameterList = new ParameterList(composite, tabItem, parameters);
			tabItem.setControl(composite);
		}
		
		public Map<String, String> getParameters(){
			return parameterList.getParameters();
		}
		
		public String getText() {
			return tabItem.getText();
		}
		
		public boolean equalsTabItem(TabItem tabItem) {
			return this.tabItem.equals(tabItem);
		}
		
	}
	
	private class ParameterList {
		
		private Table table;
		private Composite composite;
		
		
		public ParameterList(Composite parent, TabItem tabItem, Map<String, String> parameters) {
			composite = new Composite(parent, SWT.BORDER);
			GridLayout gridLayout = new GridLayout();
		    gridLayout.numColumns = 1;
		    composite.setLayout(gridLayout);
			
			table = new Table (composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
			table.setLinesVisible (true);
			table.setHeaderVisible (true);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			data.heightHint = 200;
			table.setLayoutData(data);
			String[] titles = {"Key", "Value"};
			for (String title : titles) {
				TableColumn column = new TableColumn (table, SWT.NONE);
				column.setText (title);
			}
			
			
			for (Entry<String, String> parameter : parameters.entrySet()) {
				TableItem item = new TableItem (table, SWT.NONE);
				item.setText (0, parameter.getKey());
				item.setText (1, parameter.getValue());
			}

			for (int i=0; i<titles.length; i++) {
				table.getColumn(i).pack();
			}
			
			Composite add = new Composite(composite, SWT.NONE);
			GridLayout gridLayoutAdd = new GridLayout();
		    gridLayoutAdd.numColumns = 4;
		    add.setLayout(gridLayoutAdd);
		    
		    Label keyLabel = new Label(add, SWT.NONE);
		    keyLabel.setText("Key");
		    Label valueLabel = new Label(add, SWT.NONE);
		    valueLabel.setText("Value");
		    addEmptyLabel(add);
		    addEmptyLabel(add);
		    
		    Text key = new Text(add, SWT.BORDER);
		    key.setEditable(true);
		    Text value = new Text(add, SWT.BORDER);
		    value.setEditable(true);
		    Button addButton = new Button(add, SWT.NONE);
		    addButton.setText("Add Parameter");
		    addButton.addSelectionListener(new SelectionListenerDummy() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText (0, key.getText());
					item.setText (1, value.getText());
					key.setText("");
					value.setText("");
					
				}
				
			});
		    
		    Button delButton = new Button(add, SWT.NONE);
		    delButton.setText("Delete selected Parameter");
		    delButton.addSelectionListener(new SelectionListenerDummy() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					table.remove(table.getSelectionIndices());
					
				}

			});
		    
		}
		
		public Map<String, String> getParameters(){
			Map<String, String> parameters = new HashMap<String, String>();
			for (TableItem tableItem : table.getItems()) {
				parameters.put(tableItem.getText(0), tableItem.getText(1));
			}
			return parameters;
		}
		
	}

}
