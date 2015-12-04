package com.qualityeclipse.favorites.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.qualityeclipse.favorites.editors.PropertiesEditor;
import com.qualityeclipse.favorites.editors.PropertyElement;

/**
 * Handler for deleting selected entries from the Properties editor
 */
public class DeletePropertiesHandler extends AbstractHandler
{
   public Object execute(ExecutionEvent event) throws ExecutionException {
      ISelection selection = HandlerUtil.getCurrentSelection(event);
      if (!(selection instanceof IStructuredSelection))
         return null;
      final IEditorPart editor = HandlerUtil.getActiveEditor(event);
      if (!(editor instanceof PropertiesEditor))
         return null;
      return execute((PropertiesEditor) editor, (IStructuredSelection) selection);
   }

   private Object execute(final PropertiesEditor editor, IStructuredSelection selection) {

      // Build an array of properties to be removed.
      Iterator<?> iter = selection.iterator();
      int size = selection.size();
      PropertyElement[] elements = new PropertyElement[size];
      for (int i = 0; i < size; i++)
         elements[i] = (PropertyElement) ((Object) iter.next());

      // Build the operation to be performed.
      DeletePropertiesOperation op = new DeletePropertiesOperation(elements);
      op.addContext(editor.getUndoContext());

      // The progress monitor so the operation can inform the user.
      IProgressMonitor monitor =
            editor.getEditorSite()
                  .getActionBars()
                  .getStatusLineManager()
                  .getProgressMonitor();

      // An adapter for providing UI context to the operation.
      IAdaptable info = new IAdaptable() {
         public Object getAdapter(Class adapter) {
            if (Shell.class.equals(adapter))
               return editor.getSite().getShell();
            return null;
         }
      };

      // Execute the operation.
      try {
         editor.getOperationHistory().execute(op, monitor, info);
      }
      catch (ExecutionException e) {
         MessageDialog.openError(editor.getSite().getShell(), "Remove Properties Error",
               "Exception while removing properties: " + e.getMessage());
      }

      return null;
   }
}
