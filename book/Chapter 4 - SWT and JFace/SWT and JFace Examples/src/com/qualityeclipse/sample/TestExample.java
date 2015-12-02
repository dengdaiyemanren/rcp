/*
 * Created on Jun 20, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.qualityeclipse.sample;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestExample {
	public static void main(String[] args) {
		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setText("Drag Example");
		shell.setLayout (new FillLayout ());
		
		final Decorations decoration = new Decorations (shell, SWT.SHELL_TRIM);
		decoration.setLayout (new FillLayout ());
		
		//decoration.setEnabled (false);
		final Composite composite = new Composite (decoration, SWT.SHELL_TRIM);
		composite.setEnabled (false);
		for (int i = 1; i < 4; i++) {
			Button button = new Button (composite, SWT.PUSH);
			int position = (i * 60) - 50;
			button.setBounds(position,position,50,50);
			button.setText ("Button " + i);
		}
		final Point [] offset = new Point [1];
		Listener listener = new Listener () {
			Control dragControl;
			public void handleEvent (Event event) {
				switch (event.type) {
					case SWT.MouseDown:
						Control[] children = composite.getChildren();
						for (int i = 0; i < children.length; i++) {
							Rectangle rect = children[i].getBounds ();
							if (rect.contains (event.x, event.y)) {
								dragControl = children[i];
								Point pt1 = children[i].toDisplay (0, 0);
								Point pt2 = decoration.toDisplay (event.x, event.y); 
								offset [0] = new Point (pt2.x - pt1.x, pt2.y - pt1.y);
							}
						}
						break;
					case SWT.MouseMove:
						if (offset [0] != null) {
							Point pt = offset [0];
							dragControl.setLocation (event.x - pt.x, event.y - pt.y);
						}
						break;
					case SWT.MouseUp:
						offset [0] = null;
						dragControl = null;
						break;
				}
			}
		};
		decoration.addListener (SWT.MouseDown, listener);
		decoration.addListener (SWT.MouseUp, listener);
		decoration.addListener (SWT.MouseMove, listener);
		shell.setSize (300, 300);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();

	}
}
