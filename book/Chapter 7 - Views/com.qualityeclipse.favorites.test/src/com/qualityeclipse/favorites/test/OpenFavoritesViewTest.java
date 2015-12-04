package com.qualityeclipse.favorites.test;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.Before;
import org.junit.Test;

import com.qualityeclipse.favorites.handlers.OpenFavoritesViewHandler;
import com.qualityeclipse.favorites.views.FavoritesView;

public class OpenFavoritesViewTest extends AbstractFavoritesTest {

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() throws Exception {

		// Ensure that the view is not open.
		waitForJobs();
		IViewPart view = getJavaPage().findView(FavoritesView.ID);
		if (view != null)
			getJavaPage().hideView(view);
		waitForJobs();
	}

	/**
	 * Run the view test.
	 */
	@Test
	public void testOpenFavoritesView() throws Exception {

		// Setup execution context
		IWorkbenchWindow window = getJavaPage().getWorkbenchWindow();
		EvaluationContext context = new EvaluationContext(null, window);
		context.addVariable(ISources.ACTIVE_WORKBENCH_WINDOW_NAME, window);
		ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null, context);
		
		// Execute the operation.
		new OpenFavoritesViewHandler().execute(event);

		// Test that the operation completed successfully.
		waitForJobs();
		IViewPart view = getJavaPage().findView(FavoritesView.ID);
		assertTrue(view != null);
	}
}
