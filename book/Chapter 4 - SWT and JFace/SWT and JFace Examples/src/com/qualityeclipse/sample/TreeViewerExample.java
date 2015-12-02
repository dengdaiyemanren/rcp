package com.qualityeclipse.sample;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TreeViewerExample {
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setText("Tree Viewer Example");
		shell.setBounds(100, 100, 200, 200);
		shell.setLayout(new FillLayout());

		final TreeViewer treeViewer = new TreeViewer (shell, SWT.SINGLE);
		treeViewer.setLabelProvider(new PersonListLabelProvider());
		treeViewer.setContentProvider(new PersonTreeContentProvider());
		treeViewer.setInput(Person.example());

		shell.open();
		while (!shell.isDisposed ()) {
		   if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
