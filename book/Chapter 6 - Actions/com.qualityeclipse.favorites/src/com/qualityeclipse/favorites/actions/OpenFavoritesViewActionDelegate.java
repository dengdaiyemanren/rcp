package com.qualityeclipse.favorites.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import com.qualityeclipse.favorites.FavoritesLog;
import com.qualityeclipse.favorites.views.FavoritesView;

public class OpenFavoritesViewActionDelegate
      implements IWorkbenchWindowActionDelegate {

   private IWorkbenchWindow window;

   public void init(IWorkbenchWindow window) {
      this.window = window;
   }

   public void selectionChanged(IAction action, ISelection selection) {
   }

   public void run(IAction action) {

      // Get the active page.
      if (window == null)
         return;
      IWorkbenchPage page = window.getActivePage();
      if (page == null)
         return;
   
      // Open and activate the Favorites view.
      try {
         page.showView(FavoritesView.ID);
      }
      catch (PartInitException e) {
         FavoritesLog.logError("Failed to open the Favorites view", e);
      }
   }

   public void dispose() {
   }
}
