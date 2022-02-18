package org.palladiosimulator.hwsimcoupling.configuration.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.hwsimcoupling.configuration.ui.DemandCacheManager;
import org.palladiosimulator.hwsimcoupling.configuration.ui.UIUtility;

public class OpenDemandCacheHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent executionEvent) throws ExecutionException {
		Shell shell = new Shell (UIUtility.getDisplay());
		shell.setText("Available HWsim");
		shell.setLayout(new GridLayout());
		new DemandCacheManager(shell);
		shell.pack();
		shell.open();
		return null;
	}

}
