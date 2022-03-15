package org.palladiosimulator.hwsimcoupling.configuration.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.hwsimcoupling.CacheInitializer;
import org.palladiosimulator.hwsimcoupling.configuration.ProfileCache;

public class ProfileConfigurationUI extends EclipseCommandUI {

    private List<TabItemWithParameterList> tabItemsWithParameterList;
    private TabFolder tabFolder;
    private ProfileCache profileCache;

    public ProfileConfigurationUI(Composite parent) {
        super(parent);
        this.tabItemsWithParameterList = new ArrayList<TabItemWithParameterList>();
        this.profileCache = CacheInitializer.getInstance()
            .getProfileCache();
    }

    private void addTab() {
        this.tabItemsWithParameterList.add(new TabItemWithParameterList(tabFolder, "", new HashMap<String, String>()));
    }

    private void deleteCurrentlySelectedTab() {
        TabItem tabItem = tabFolder.getItem(tabFolder.getSelectionIndex());
        int indexToRemove = -1;
        for (int i = 0; i < tabItemsWithParameterList.size(); i++) {
            if (tabItemsWithParameterList.get(i)
                .equalsTabItem(tabItem)) {
                indexToRemove = i;
            }
        }
        if (indexToRemove != -1) {
            tabItemsWithParameterList.remove(indexToRemove);
        }
        tabItem.dispose();
    }

    private void save(TabFolder tabFolder) {
        profileCache.clearCache();
        for (TabItemWithParameterList tabItem : this.tabItemsWithParameterList) {
            profileCache.addProfile(tabItem.getText(), tabItem.getParameters());
        }
        profileCache.saveProfiles();
    }

    @Override
    public Control createUI() {
        tabFolder = new TabFolder(parent, SWT.NONE);
        Map<String, Map<String, String>> profiles = profileCache.getProfiles();
        for (Entry<String, Map<String, String>> profile : profiles.entrySet()) {
            TabItemWithParameterList tabItem = new TabItemWithParameterList(tabFolder, profile.getKey(),
                    profile.getValue());
            this.tabItemsWithParameterList.add(tabItem);
        }

        Button saveButton = UIUtility.createButton(parent, SWT.NONE, "Save", new SaveSelectionListener());

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 4;
        saveButton.setLayoutData(gridData);

        Menu menu = new Menu(parent);
        tabFolder.setMenu(menu);
        MenuItem itemDel = UIUtility.addMenuItem(menu, "Delete Profile");
        UIUtility.setMenuItemListener(itemDel, SWT.Selection, new DeleteTabItemListener());

        MenuItem itemAdd = UIUtility.addMenuItem(menu, "Add Profile");
        UIUtility.setMenuItemListener(itemAdd, SWT.Selection, new AddTabItemListener());

        return tabFolder;
    }

    private class SaveSelectionListener extends DefaultSelectionListener {
        @Override
        public void widgetSelected(SelectionEvent e) {
            save(tabFolder);
        }
    }

    private class DeleteTabItemListener implements Listener {
        @Override
        public void handleEvent(Event event) {
            deleteCurrentlySelectedTab();
        }
    }

    private class AddTabItemListener implements Listener {
        @Override
        public void handleEvent(Event event) {
            addTab();
        }
    }

    private class TabItemWithParameterList {

        private TabItem tabItem;
        private Text tabName;
        private ParameterList parameterList;
        private Composite composite;

        public TabItemWithParameterList(TabFolder parent, String name, Map<String, String> map) {
            composite = UIUtility.createCompositeWithLayout(parent, SWT.NONE, 1);
            tabItem = UIUtility.createTabItemWithControl(parent, SWT.NONE, name, composite);
            Composite profileEditing = UIUtility.createCompositeWithLayout(composite, SWT.NONE, 2);
            tabName = UIUtility.createEditableText(profileEditing, SWT.BORDER);
            UIUtility.createButton(profileEditing, SWT.NONE, "Save Tab Name", new SetTabItemNameSelectionListener());
            parameterList = new ParameterList(composite, tabItem, map);
        }

        public Map<String, String> getParameters() {
            return parameterList.getParameters();
        }

        public String getText() {
            return tabItem.getText();
        }

        public boolean equalsTabItem(TabItem tabItem) {
            return this.tabItem.equals(tabItem);
        }

        private class SetTabItemNameSelectionListener extends DefaultSelectionListener {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tabItem.setText(tabName.getText());
            }
        }

    }

    private class ParameterList {

        private Table table;
        private Composite composite;

        private Text key;
        private Text value;
        private Button addButton;
        private Button cancelButton;
        private Button delButton;

        private boolean editingMode;

        public ParameterList(Composite parent, TabItem tabItem, Map<String, String> parameters) {
            composite = UIUtility.createCompositeWithLayout(parent, SWT.BORDER, 1);
            table = UIUtility.createTable(composite, SWT.SINGLE | SWT.FULL_SELECTION);

            String[] titles = { "Key", "Value" };
            UIUtility.setTableData(table, titles, parameters);

            Composite add = UIUtility.createCompositeWithLayout(parent, SWT.NONE, 5);

            UIUtility.createLabel(add, SWT.NONE, "Key");
            UIUtility.createLabel(add, SWT.NONE, "Value");

            UIUtility.addEmptyLabel(add);
            UIUtility.addEmptyLabel(add);
            UIUtility.addEmptyLabel(add);

            key = UIUtility.createEditableText(add, SWT.BORDER);
            value = UIUtility.createEditableText(add, SWT.BORDER);

            addButton = UIUtility.createButton(add, SWT.NONE, "Add Parameter", new AddSelectionListener());
            cancelButton = UIUtility.createButton(add, SWT.NONE, "Cancel Editing", new CancelSelectionListener());
            cancelButton.setVisible(false);
            delButton = UIUtility.createButton(add, SWT.NONE, "Delete selected Parameter",
                    new DeleteSelectionListener());
            delButton.setVisible(false);

            editingMode = false;
            table.addSelectionListener(new TableSelectionListener());

        }

        public Map<String, String> getParameters() {
            Map<String, String> parameters = new HashMap<String, String>();
            for (TableItem tableItem : table.getItems()) {
                parameters.put(tableItem.getText(0), tableItem.getText(1));
            }
            return parameters;
        }

        private void enterEditingMode() {
            key.setText(table.getItem(table.getSelectionIndex())
                .getText(0));
            value.setText(table.getItem(table.getSelectionIndex())
                .getText(1));
            editingMode = true;
            addButton.setText("Edit Parameter");
            cancelButton.setVisible(true);
            delButton.setVisible(true);
        }

        private void cancelEditingMode() {
            key.setText("");
            value.setText("");
            editingMode = false;
            cancelButton.setVisible(false);
            delButton.setVisible(false);
            addButton.setText("Add Parameter");
        }

        private void addTableItem() {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, key.getText());
            item.setText(1, value.getText());
            key.setText("");
            value.setText("");
        }

        private void editTableItem() {
            TableItem item = table.getItem(table.getSelectionIndex());
            item.setText(0, key.getText());
            item.setText(1, value.getText());
            cancelEditingMode();
        }

        private class AddSelectionListener extends DefaultSelectionListener {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (editingMode) {
                    editTableItem();
                } else {
                    addTableItem();
                }
            }
        }

        private class CancelSelectionListener extends DefaultSelectionListener {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (editingMode) {
                    cancelEditingMode();
                }
            }
        }

        private class DeleteSelectionListener extends DefaultSelectionListener {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.remove(table.getSelectionIndices());
                cancelEditingMode();
            }
        }

        private class TableSelectionListener extends DefaultSelectionListener {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enterEditingMode();
            }
        }
    }

}
