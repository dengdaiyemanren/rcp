package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.qualityeclipse.favorites.model.FavoritesManager;

/**
 * Remove each currently selected object from the Favorites collection if it has
 * not already been removed.
 */
public class RemoveFavoritesHandler extends AbstractHandler
{
   public Object execute(ExecutionEvent event)
         throws ExecutionException {
      ISelection selection = HandlerUtil.getCurrentSelection(event);
      if (selection instanceof IStructuredSelection)
         FavoritesManager.getManager().removeFavorites(
               ((IStructuredSelection) selection).toArray());
      return null;
   }
}
