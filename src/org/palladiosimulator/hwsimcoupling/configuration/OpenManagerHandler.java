package org.palladiosimulator.hwsimcoupling.configuration;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.hwsimcoupling.configuration.ui.ConfigurationManager;

public class OpenManagerHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent executionEvent) throws ExecutionException {
		Shell shell = new Shell (getDisplay());
		shell.setText("HWSimCoupling Profile Manager");
		shell.setLayout(new GridLayout());
		new ConfigurationManager(shell);
		shell.pack();
		shell.open();
		return null;
	}
	
	private Display getDisplay() {
	      Display display = Display.getCurrent();
	      //may be null if outside the UI thread
	      if (display == null)
	         display = Display.getDefault();
	      return display;		
	   }


}
