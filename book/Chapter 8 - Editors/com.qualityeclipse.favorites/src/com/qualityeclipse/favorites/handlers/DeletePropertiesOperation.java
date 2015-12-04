package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.qualityeclipse.favorites.editors.PropertyCategory;
import com.qualityeclipse.favorites.editors.PropertyElement;
import com.qualityeclipse.favorites.editors.PropertyEntry;
import com.qualityeclipse.favorites.editors.PropertyFile;

/**
 * An operation for deleting properties appropriate for undo/redo.
 * 
 * @see DeletePropertiesHandler
 */
public class DeletePropertiesOperation extends AbstractOperation
{
   private final PropertyElement[] elements;
   private PropertyElement[] parents;
   private int[] indexes;

   public DeletePropertiesOperation(PropertyElement[] elements) {
      super(getLabelFor(elements));
      this.elements = elements;
   }

   public IStatus execute(IProgressMonitor monitor, IAdaptable info)
         throws ExecutionException {

      // If a UI context has been provided,
      // then prompt the user to confirm the operation.

      if (info != null) {
         Shell shell = (Shell) info.getAdapter(Shell.class);
         if (shell != null) {
            if (!MessageDialog.openQuestion(shell, "Remove properties",
                  "Do you want to remove the currently selected properties?"))
               return Status.CANCEL_STATUS;
         }
      }

      // Perform the operation.

      return redo(monitor, info);
   }

   public IStatus redo(IProgressMonitor monitor, IAdaptable info)
         throws ExecutionException {

      // Perform the operation, providing feedback to the user
      // through the progress monitor if one is provided.

      parents = new PropertyElement[elements.length];
      indexes = new int[elements.length];

      if (monitor != null)
         monitor.beginTask("Remove properties", elements.length);

      Shell shell = (Shell) info.getAdapter(Shell.class);
      shell.setRedraw(false);
      try {
         for (int i = elements.length; --i >= 0;) {
            parents[i] = elements[i].getParent();
            PropertyElement[] children = parents[i].getChildren();
            for (int index = 0; index < children.length; index++) {
               if (children[index] == elements[i]) {
                  indexes[i] = index;
                  break;
               }
            }
            elements[i].removeFromParent();

            if (monitor != null)
               monitor.worked(1);
         }
      }
      finally {
         shell.setRedraw(true);
      }

      if (monitor != null)
         monitor.done();

      return Status.OK_STATUS;
   }

   public IStatus undo(IProgressMonitor monitor, IAdaptable info)
         throws ExecutionException {
      Shell shell = (Shell) info.getAdapter(Shell.class);
      shell.setRedraw(false);
      try {
         for (int i = 0; i < elements.length; i++) {
            if (parents[i] instanceof PropertyCategory)
               ((PropertyCategory) parents[i]).addEntry(indexes[i],
                     (PropertyEntry) elements[i]);
            else
               ((PropertyFile) parents[i]).addCategory(indexes[i],
                     (PropertyCategory) elements[i]);
         }
      }
      finally {
         shell.setRedraw(true);
      }
      return Status.OK_STATUS;
   }

   private static String getLabelFor(PropertyElement[] elements) {
      if (elements.length == 1) {
         PropertyElement first = elements[0];
         if (first instanceof PropertyEntry) {
            PropertyEntry propEntry = (PropertyEntry) first;
            return "Remove property " + propEntry.getKey();
         }
         if (first instanceof PropertyCategory) {
            PropertyCategory propCat = (PropertyCategory) first;
            return "Remove category " + propCat.getName();
         }
      }
      return "Remove properties";
   }
}
