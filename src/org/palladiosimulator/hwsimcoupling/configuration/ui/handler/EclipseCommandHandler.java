package org.palladiosimulator.hwsimcoupling.configuration.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.hwsimcoupling.configuration.ui.EclipseCommandUI;
import org.palladiosimulator.hwsimcoupling.configuration.ui.UIUtility;

public abstract class EclipseCommandHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent executionEvent) throws ExecutionException {
		Shell shell = new Shell (UIUtility.getDisplay());
		shell.setText(getShellTitle());
		shell.setLayout(getShellLayout());
		getEclipseCommandUI(shell).createUI();
		shell.pack();
		shell.open();
		return null;
	}

	protected abstract EclipseCommandUI getEclipseCommandUI(Shell shell);
	protected abstract String getShellTitle();
	protected abstract Layout getShellLayout();

}
