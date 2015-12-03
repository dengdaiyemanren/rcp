package com.qualityeclipse.favorites.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;

import com.qualityeclipse.favorites.actions.AddToFavoritesActionDelegate;

/**
 * Test exercising the menu item that adds objects to the Favorites view
 */
public class AddToFavoritesTest extends AbstractFavoritesTest {
   protected IProject project;

   public AddToFavoritesTest(String name) {
      super(name);
   }

   /**
    * Create and cache the project used in this test
    */
   protected void setUp() throws Exception {
      super.setUp();
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      project = root.getProject("TestProj");
      project.create(null);
      project.open(null);
   }

   /**
    * Remove the project used in this test
    */
   protected void tearDown() throws Exception {
      super.tearDown();

      // Wait for a bit for the system to catch up
      // so that the delete operation does not collide
      // with any background tasks.
      delay(3000);
      waitForJobs();

      project.delete(true, true, null);
   }

   /**
    * Exercise the menu item that adds objects to the Favorites view
    * then assert that the object was indeed added.
    */
   public void testAddToFavorites() throws CoreException {
   
      // Show the resource navigator and select the project.
      IViewPart navigator = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage().showView(
                  "org.eclipse.ui.views.ResourceNavigator");
      StructuredSelection selection = new StructuredSelection(project);
      ((ISetSelectionTarget) navigator).selectReveal(selection);
   
      // Execute the action.
      final IObjectActionDelegate delegate 
         = new AddToFavoritesActionDelegate();
      IAction action = new Action("Test Add to Favorites") {
         public void run() {
            delegate.run(this);
         }
      };
      delegate.setActivePart(action, navigator);
      delegate.selectionChanged(action, selection);
      action.run();
   
      // Add code here at a later time to verify that the
      // Add to Favorites action correctly added the
      // appropriate values to the Favorites view.
   }
}
