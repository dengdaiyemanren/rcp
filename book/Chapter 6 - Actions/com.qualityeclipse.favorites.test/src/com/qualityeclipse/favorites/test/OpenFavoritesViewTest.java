package com.qualityeclipse.favorites.test;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.qualityeclipse.favorites.actions.OpenFavoritesViewActionDelegate;
import com.qualityeclipse.favorites.views.FavoritesView;

/**
 * Test for exercizing the OpenFavoritesViewActionDelegate
 */
public class OpenFavoritesViewTest extends AbstractFavoritesTest {
   /**
    * Construct new test instance.
    * 
    * @param name the test name
    */
   public OpenFavoritesViewTest(String name) {
      super(name);
   }

   /**
    * Ensure the system state before the test
    */
   protected void setUp() throws Exception {
      super.setUp();

      // Ensure that the view is not open.
      waitForJobs();
      IWorkbenchPage page = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage();
      IViewPart view = page.findView(FavoritesView.ID);
      if (view != null)
         page.hideView(view);

      // Delay for 3 seconds so that
      // the Favorites view can be seen.
      waitForJobs();
      delay(3000);
   }

   /**
    * Exercize the OpenFavoritesViewActionDelegate class
    */
   public void testOpenFavoritesView() {

      // Execute the operation.
      (new Action("OpenFavoritesViewTest") {
         public void run() {
            IWorkbenchWindowActionDelegate delegate = 
               new OpenFavoritesViewActionDelegate();
            delegate.init(PlatformUI.getWorkbench()
               .getActiveWorkbenchWindow());
            delegate.selectionChanged(this, StructuredSelection.EMPTY);
            delegate.run(this);
         }
      }).run();
   
      // Test that the operation completed successfully.
      waitForJobs();
      IWorkbenchPage page = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage();
      assertTrue(page.findView(FavoritesView.ID) != null);
   }
}
