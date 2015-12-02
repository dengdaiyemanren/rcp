package com.qualityeclipse.favorites.propertyTester;

import org.eclipse.core.expressions.PropertyTester;

/**
 * Tests whether an object is part of the Favorites collection.
 */
public class FavoritesTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if ("isFavorite".equals(property)) {
			// determine if the favorites collection contains the receiver
			return false;
		}
		if ("notFavorite".equals(property)) {
			// determine if the favorites collection contains the receiver
			return true;
		}
		return false;
	}

}
