package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.qualityeclipse.favorites.FavoritesLog;
import com.qualityeclipse.favorites.views.FavoritesView;

/**
 * Handler to open the "Favorites" view
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenFavoritesViewHandler extends AbstractHandler {

	/**
	 * Open the favorites view.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Get the active window

		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		if (window == null)
			return null;

		// Get the active page

		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return null;

		// Open and activate the Favorites view

		try {
			page.showView(FavoritesView.ID);
		} catch (PartInitException e) {
			FavoritesLog.logError("Failed to open the Favorites view", e);
		}
		return null;
	}
}
