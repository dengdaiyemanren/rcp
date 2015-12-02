/*
 * Created on Jun 20, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.qualityeclipse.sample;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FillLayoutExample {
	public static void main(String[] args) {
		Button button;
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setText("FillLayout Example");
		shell.setBounds(100, 100, 400, 75);
		shell.setLayout(new FillLayout());
		for (int i = 1; i <= 8; i++) {
			button = new Button (shell, SWT.PUSH);
			button.setText ("B" + i);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					System.out.println(((Button)event.widget).getText() + " was clicked!");
				}
			});
		}
		shell.open();
		while (!shell.isDisposed ()) {
		   if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
