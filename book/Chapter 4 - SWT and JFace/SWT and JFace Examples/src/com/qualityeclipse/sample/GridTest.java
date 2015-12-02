/*
 * Created on Aug 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.qualityeclipse.sample;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GridTest {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell();
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		shell.setText("GridLayout test");
		{
			final Label label = new Label(shell, SWT.NONE);
			final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 3;
			label.setLayoutData(gridData);
			label.setText("label");
		}
		{
			final Text text = new Text(shell, SWT.BORDER);
			final GridData gridData =
				new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			gridData.horizontalSpan = 3;
			text.setLayoutData(gridData);
			text.setText("horizontalSpan = 3 Text");
		}
		{
			final Group group = new Group(shell, SWT.NONE);
			group.setText("Left group");
			group.setLayoutData(
				new GridData(
					GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		}
		{
			final Composite composite = new Composite(shell, SWT.NONE);
			composite.setLayout(new GridLayout());
			composite.setLayoutData(
				new GridData(
					GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_FILL));
			{
				final Button button = new Button(composite, SWT.NONE);
				button.setLayoutData(
					new GridData(
						GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_END));
				button.setText("button");
			}
			{
				final Button button = new Button(composite, SWT.NONE);
				button.setLayoutData(new GridData());
				button.setText("button");
			}
			{
				final Button button = new Button(composite, SWT.NONE);
				button.setLayoutData(
					new GridData(
						GridData.GRAB_VERTICAL
							| GridData.VERTICAL_ALIGN_BEGINNING));
				button.setText("button");
			}
		}
		{
			final Group group = new Group(shell, SWT.NONE);
			group.setText("Right group");
			group.setLayoutData(
				new GridData(
					GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		}
		// DESIGNER: Add controls before this line.
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
