/*
 * Created on Jun 20, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.qualityeclipse.sample;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FormLayoutExample {
	public static void main(String[] args) {
		FormData formData;
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setText("FormLayout Example");
		shell.setBounds(100, 100, 220, 180);
		shell.setLayout(new FormLayout());

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Cancel");
		formData = new FormData();
		formData.right = new FormAttachment(100,-5);
		formData.bottom = new FormAttachment(100,-5);
		cancelButton.setLayoutData(formData);

		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText("OK");
		formData = new FormData();
		formData.right = new FormAttachment(cancelButton, -5);
		formData.bottom = new FormAttachment(100,-5);
		okButton.setLayoutData(formData);

		Text text = new Text(shell, SWT.MULTI | SWT.BORDER);
		formData = new FormData();
		formData.top = new FormAttachment(0,5);
		formData.bottom = new FormAttachment(cancelButton, -5);
		formData.left = new FormAttachment(0,5);
		formData.right = new FormAttachment(100,-5);
		text.setLayoutData(formData);

		shell.open();
		while (!shell.isDisposed ()) {
		   if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
