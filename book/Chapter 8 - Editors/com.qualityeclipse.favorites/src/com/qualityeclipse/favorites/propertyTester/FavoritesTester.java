package com.qualityeclipse.favorites.propertyTester;

import org.eclipse.core.expressions.PropertyTester;

import com.qualityeclipse.favorites.model.FavoritesManager;
import com.qualityeclipse.favorites.model.IFavoriteItem;

/**
 * Tests whether an object is part of the Favorites collection.
 */
public class FavoritesTester extends PropertyTester {

	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		boolean found = false;
		IFavoriteItem[] favorites = FavoritesManager.getManager().getFavorites();
		for (int i = 0; i < favorites.length; i++) {
			IFavoriteItem item = favorites[i];
			found = item.isFavoriteFor(receiver);
			if (found)
				break;
		}
		
		if ("isFavorite".equals(property))
			return found;
		
		if ("notFavorite".equals(property))
			return !found;
		
		return false;
	}

}
