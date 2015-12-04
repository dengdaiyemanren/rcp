package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.qualityeclipse.favorites.views.FavoritesView;

/**
 * Open an inline editing area over-top the currently selected item in the
 * Favorites view.
 */
public class RenameFavoritesHandler extends AbstractHandler
{
   private static final int COLUMN_TO_EDIT = 1;

   public Object execute(ExecutionEvent event)
         throws ExecutionException {
      IWorkbenchPart part = HandlerUtil.getActivePart(event);
      if (!(part instanceof FavoritesView))
         return null;
      editElement((FavoritesView) part);
      return null;
   }

   public void editElement(FavoritesView favoritesView) {
      TableViewer viewer =
            favoritesView.getFavoritesViewer();
      IStructuredSelection selection =
            (IStructuredSelection) viewer.getSelection();
      if (!selection.isEmpty())
         viewer.editElement(selection.getFirstElement(), COLUMN_TO_EDIT);
   }
}
