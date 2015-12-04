package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;

import com.qualityeclipse.favorites.model.FavoritesManager;

/**
 * Paste the current contents of the clipboard into the Favorites view by adding
 * those objects to the collection of Favorites objects.
 */
public class PasteFavoritesHandler extends ClipboardHandler
{
   /**
    * Called by the superclass {@link #execute(ExecutionEvent)} method so that
    * this method can concentrate on the operation and does not have to manage
    * clipboard creation and disposal.
    */
   protected Object execute(ExecutionEvent event, Clipboard clipboard)
         throws ExecutionException {
      paste(clipboard, ResourceTransfer.getInstance());
      paste(clipboard, JavaUI.getJavaElementClipboardTransfer());
      return null;
   }

   private void paste(Clipboard clipboard, Transfer transfer) {
      Object[] elements = (Object[]) clipboard.getContents(transfer);
      if (elements != null && elements.length != 0)
         FavoritesManager.getManager().addFavorites(elements);
   }
}
