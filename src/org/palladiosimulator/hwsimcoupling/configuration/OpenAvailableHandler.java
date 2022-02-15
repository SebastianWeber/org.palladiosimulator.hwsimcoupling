package org.palladiosimulator.hwsimcoupling.configuration;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.hwsimcoupling.configuration.ui.AvailableManager;
import org.palladiosimulator.hwsimcoupling.configuration.ui.UIUtility;

public class OpenAvailableHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent executionEvent) throws ExecutionException {
		Shell shell = new Shell (UIUtility.getDisplay());
		shell.setText("Available HWsim");
		shell.setLayout(new GridLayout());
		new AvailableManager(shell);
		shell.pack();
		shell.open();
		return null;
	}


}
