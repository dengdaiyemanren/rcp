package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

/**
 * Remove each currently selected object from the Favorites collection and place
 * that information on the clipboard.
 */
public class CutFavoritesHandler extends AbstractHandler
{
   IHandler copy = new CopyFavoritesHandler();
   IHandler remove = new RemoveFavoritesHandler();

   public Object execute(ExecutionEvent event)
         throws ExecutionException {
      copy.execute(event);
      remove.execute(event);
      return null;
   }

   public void dispose() {
      copy.dispose();
      remove.dispose();
      super.dispose();
   }
}
