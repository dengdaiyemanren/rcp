package com.qualityeclipse.favorites.test;

import junit.framework.TestCase;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import com.qualityeclipse.favorites.views.FavoritesView;

/**
 * The class <code>FavoritesViewTest</code> contains tests 
 * for the class {@link 
 *    com.qualityeclipse.favorites.views.FavoritesView}.
 *
 * @pattern JUnit Test Case
 * @generatedBy CodePro Studio
 */
public class FavoritesViewTest extends AbstractFavoritesTest
{
   /**
    * The object that is being tested.
    *
    * @see com.qualityeclipse.favorites.views.FavoritesView
    */
   private FavoritesView testView;

   /**
    * Construct new test instance.
    *
    * @param name the test name
    */
   public FavoritesViewTest(String name) {
      super(name);
   }


   /**
    * Perform pre-test initialization.
    *
    * @throws Exception
    *
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      // Initialize the test fixture for each test 
      // that is run.
      waitForJobs();
      testView = (FavoritesView) 
         PlatformUI
            .getWorkbench()
            .getActiveWorkbenchWindow()
            .getActivePage()
            .showView(FavoritesView.ID);

      // Delay for 3 seconds so that 
      // the Favorites view can be seen.
      waitForJobs();
      delay(3000);

      // Add additional setup code here.
   }

   /**
    * Perform post-test cleanup.
    *
    * @throws Exception
    *
    * @see TestCase#tearDown()
    */
   protected void tearDown() throws Exception {
      super.tearDown();
      // Dispose of test fixture.
      waitForJobs();
      PlatformUI
         .getWorkbench()
         .getActiveWorkbenchWindow()
         .getActivePage()
         .hideView(testView);
      // Add additional teardown code here.
   }

   /**
    * Run the view test.
    */
   public void testView() {
      TableViewer viewer = testView.getFavoritesViewer();

      Object[] expectedContent = 
         new Object[] { "One", "Two", "Three" };
      Object[] expectedLabels = 
         new String[] { "One", "Two", "Three" };
      
      // Assert valid content.
      IStructuredContentProvider contentProvider = 
         (IStructuredContentProvider)
            viewer.getContentProvider();
      assertEquals(expectedContent, 
         contentProvider.getElements(viewer.getInput()));
      
      // Assert valid labels.
      ILabelProvider labelProvider = 
         (ILabelProvider) viewer.getLabelProvider();
      for (int i = 0; i < expectedLabels.length; i++)
         assertEquals(expectedLabels[i], 
            labelProvider.getText(expectedContent[i]));
   }
}