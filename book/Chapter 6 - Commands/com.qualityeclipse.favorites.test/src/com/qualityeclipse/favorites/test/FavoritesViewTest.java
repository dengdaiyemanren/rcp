package com.qualityeclipse.favorites.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.qualityeclipse.favorites.views.FavoritesView;

/**
 * The class <code>FavoritesViewTest</code> contains tests for the class
 * {@link  com.qualityeclipse.favorites.views.FavoritesView}.
 * 
 * @pattern JUnit Test Case
 * @generatedBy CodePro Studio
 */

public class FavoritesViewTest extends AbstractFavoritesTest {
	/**
	 * The object that is being tested.
	 * 
	 * @see com.qualityeclipse.favorites.views.FavoritesView
	 */
	private FavoritesView testView;

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() throws Exception {

		// Initialize the test fixture for each test
		// that is run.
		waitForJobs();
		testView = (FavoritesView) getJavaPage().showView(FavoritesView.ID);

		// Delay for 3 seconds so that
		// the Favorites view can be seen.

		waitForJobs();
		delay(3000);

		// Add additional setup code here.
	}

	/**
	 * Run the view test.
	 */
	@Test
	public void testView() {
		TableViewer viewer = testView.getFavoritesViewer();
		Object[] expectedContent = new Object[] { "One", "Two", "Three" };
		Object[] expectedLabels = new String[] { "One", "Two", "Three" };

		// Assert valid content.
		IStructuredContentProvider contentProvider = (IStructuredContentProvider) viewer
				.getContentProvider();
		assertArrayEquals(expectedContent, contentProvider.getElements(viewer
				.getInput()));

		// Assert valid labels.
		ITableLabelProvider labelProvider = (ITableLabelProvider) viewer
				.getLabelProvider();
		for (int i = 0; i < expectedLabels.length; i++)
			assertEquals(expectedLabels[i], labelProvider.getColumnText(
					expectedContent[i], 1));
	}

	/**
	 * Perform post-test cleanup.
	 */
	@After
	public void tearDown() throws Exception {

		// Dispose of test fixture.
		waitForJobs();
		getJavaPage().hideView(testView);

		// Add additional teardown code here.
	}
}