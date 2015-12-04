package com.qualityeclipse.favorites.util;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import com.qualityeclipse.favorites.FavoritesLog;

/**
 * Utility class for dealing with editors
 */
public class EditorUtil
{
   /**
    * Open an editor on the first selected element
    * 
    * @param page
    *           the page in which the editor should be opened
    * @param selection
    *           the selection containing the object to be edited
    */
   public static void openEditor(IWorkbenchPage page,
         ISelection selection) {

      // Get the first element.

      if (!(selection instanceof IStructuredSelection))
         return;
      Iterator<?> iter = ((IStructuredSelection) selection).iterator();
      if (!iter.hasNext())
         return;
      Object elem = iter.next();
      // Adapt the first element to a file.

      if (!(elem instanceof IAdaptable))
         return;

      IFile file = (IFile) ((IAdaptable) elem).getAdapter(IFile.class);
      if (file == null)
         return;

      // Open an editor on that file.

      try {
         IDE.openEditor(page, file);
      }
      catch (PartInitException e) {
         FavoritesLog.logError(
            "Open editor failed: " + file.toString(), e);
      }
   }
}
