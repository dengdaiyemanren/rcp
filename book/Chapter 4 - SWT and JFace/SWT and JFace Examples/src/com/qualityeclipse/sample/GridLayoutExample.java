/*
 * Created on Jun 20, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.qualityeclipse.sample;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GridLayoutExample {
	public static void main(String[] args) {
		Label label;
		Text text;
		GridData gridData;
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setText("GridLayout Example");
		shell.setBounds(100, 100, 200, 100);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		shell.setLayout(layout);

		label = new Label(shell, SWT.LEFT);
		label.setText("Enter your first and last name");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		label.setLayoutData(gridData);

		label = new Label(shell, SWT.LEFT);
		label.setText("First:");
		text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		text.setLayoutData(gridData);

		label = new Label(shell, SWT.LEFT);
		label.setText("Last:");
		text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		text.setLayoutData(gridData);

		shell.open();
		while (!shell.isDisposed ()) {
		   if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
