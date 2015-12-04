package com.qualityeclipse.favorites.test;

import java.util.Collections;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qualityeclipse.favorites.handlers.AddToFavoritesHandler;
import com.qualityeclipse.favorites.model.FavoritesManager;
import com.qualityeclipse.favorites.views.FavoritesView;

/**
 * Test exercising the menu item that adds objects to the Favorites view
 */
public class AddToFavoritesTest extends AbstractFavoritesTest {
	private static final String NAVIGATOR_ID = "org.eclipse.ui.views.ResourceNavigator";
	private IProject project;
   private FavoritesView testView;

   /**
    * Create and cache the project used in this test
    */
	@Before
	public void setUp() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject("TestProj");
		if (!project.exists())
			project.create(null);
		if (!project.isAccessible())
			project.open(null);

      // Initialize the test fixture for each test 
      // that is run.
      waitForJobs();
      testView = (FavoritesView) 
         getJavaPage().showView(FavoritesView.ID);

      // Delay for 3 seconds so that 
      // the Favorites view can be seen.
      waitForJobs();
      delay(3000);
	}

	@Test
	public void testExecuteExecutionEvent() throws Exception {
		
      // Assert valid content before the operation
      assertFavoritesViewContent(
         testView, 
         new Object[] {},
         new String[] {});
   
		// Show the resource navigator and select the project.
		IViewPart navigator = getJavaPage().showView(NAVIGATOR_ID);
		StructuredSelection selection = new StructuredSelection(project);
		((ISetSelectionTarget) navigator).selectReveal(selection);

		// Setup execution context
		IWorkbenchWindow window = getJavaPage().getWorkbenchWindow();
		EvaluationContext context = new EvaluationContext(null, window);
		context.addVariable(ISources.ACTIVE_CURRENT_SELECTION_NAME, selection);
		ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP,
				null, context);

		// Execute the operation.
		new AddToFavoritesHandler().execute(event);
      
      // Assert valid content after the operation
      assertFavoritesViewContent(
         testView, 
         new Object[] {FavoritesManager.getManager().newFavoriteFor(project)},
         new String[] {project.getName()});
	}

	@After
	public void tearDown() throws Exception {

		// Wait for a bit for the system to catch up
		// so that the delete operation does not collide
		// with any background tasks.
		delay(3000);
		waitForJobs();

		project.delete(true, true, null);
	}

}
