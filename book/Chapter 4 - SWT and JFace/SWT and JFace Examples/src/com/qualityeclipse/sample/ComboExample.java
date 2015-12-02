/*
 * Created on Jun 20, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.qualityeclipse.sample;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ComboExample {
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setText("Combo Example");
		shell.setBounds(100, 100, 200, 100);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		final Combo combo1 = new Combo (shell, SWT.READ_ONLY);
		final Combo combo2 = new Combo (shell, SWT.DROP_DOWN);
		final Label label = new Label (shell, SWT.CENTER);
		combo1.setItems(new String[] {"First", "Second", "Third"});
		combo1.setText("First");
		combo1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				label.setText("Selected: " + combo1.getText());
			}
		});
		combo2.setItems(new String[] {"First", "Second", "Third"});
		combo2.setText("First");
		combo2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				label.setText("Entered: " + combo2.getText());
			}
		});
		shell.open();
		while (!shell.isDisposed ()) {
		   if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
