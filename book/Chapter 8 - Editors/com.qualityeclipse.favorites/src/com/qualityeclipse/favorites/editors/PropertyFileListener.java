package com.qualityeclipse.favorites.editors;

/**
 * Used by {@link ProperyFile} to notify registered listeners, such as
 * {@link PropertiesEditor}, that changes have occurred in the model
 */
public interface PropertyFileListener
{
   void keyChanged(
      PropertyCategory category,
      PropertyEntry entry);
   
   void valueChanged(
      PropertyCategory category,
      PropertyEntry entry);
   
   void nameChanged(
      PropertyCategory category);
   
   void entryAdded(
      PropertyCategory category,
      PropertyEntry entry);
   
   void entryRemoved(
      PropertyCategory category,
      PropertyEntry entry);
   
   void categoryAdded(
      PropertyCategory category);
   
   void categoryRemoved(
      PropertyCategory category);
}