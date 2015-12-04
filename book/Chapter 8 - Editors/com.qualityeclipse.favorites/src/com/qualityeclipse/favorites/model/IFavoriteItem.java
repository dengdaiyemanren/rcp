package com.qualityeclipse.favorites.model;

import org.eclipse.core.runtime.IAdaptable;

public interface IFavoriteItem
   extends IAdaptable
{
   String getName();
   void setName(String newName);
   String getLocation();
   boolean isFavoriteFor(Object obj);
   FavoriteItemType getType();
   String getInfo();

   static IFavoriteItem[] NONE = new IFavoriteItem[] {};
}