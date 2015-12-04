package com.qualityeclipse.favorites.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;

import com.qualityeclipse.favorites.views.FavoritesViewNameFilter;

/**
 * This action prompts the user for a string with wildcards used to filter the
 * favorites view. Any favorite item with a name matching the specified string
 * will be filtered from the Favorites view.
 * <p>
 * This could (should?) be implemented as a subclass of {@link ContributionItem}
 * but instead we subclass Action to illustrate the older approach.
 */
public class FavoritesViewFilterAction extends Action
{
   private final Shell shell;
   private final FavoritesViewNameFilter nameFilter;

   public FavoritesViewFilterAction(StructuredViewer viewer,
         String text) {
      super(text);
      shell = viewer.getControl().getShell();
      nameFilter = new FavoritesViewNameFilter(viewer);
   }

   public void run() {
      InputDialog dialog =
            new InputDialog(shell, "Favorites View Filter",
                  "Enter a name filter pattern"
                        + " (* = any string, ? = any character)"
                        + System.getProperty("line.separator")
                        + "or an empty string for no filtering:",
                  nameFilter.getPattern(), null);
      if (dialog.open() == InputDialog.OK)
         nameFilter.setPattern(dialog.getValue().trim());
   }

   public void saveState(IMemento memento) {
      nameFilter.saveState(memento);
   }

   public void init(IMemento memento) {
      nameFilter.init(memento);
   }
}
