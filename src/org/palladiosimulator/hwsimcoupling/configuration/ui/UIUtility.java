package org.palladiosimulator.hwsimcoupling.configuration.ui;

import org.eclipse.swt.widgets.Display;

public class UIUtility {

	public static Display getDisplay() {
	      Display display = Display.getCurrent();
	      if (display == null)
	         display = Display.getDefault();
	      return display;		
	   }
	
}
