package com.qualityeclipse.favorites.editors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A label provider maps an element of the viewer's model to an
 * optional image and optional text string used to display the element
 * in the viewer's control. In our case, this label provider extracts
 * text from the {@link PropertyElement} to display in the editor.
 */
public class PropertiesEditorLabelProvider extends LabelProvider
   implements ITableLabelProvider
{
   public Image getColumnImage(Object element, int columnIndex) {
      return null;
   }
   
   public String getColumnText(Object element, int columnIndex) {
      if (element instanceof PropertyCategory) {
         PropertyCategory category =
            (PropertyCategory) element;
         switch (columnIndex) {
            case 0 :
               return category.getName();
            case 1 :
               return "";
         }
      }
   
      if (element instanceof PropertyEntry) {
         PropertyEntry entry = (PropertyEntry) element;
         switch (columnIndex) {
            case 0 :
               return entry.getKey();
            case 1 :
               return entry.getValue();
         }
      }
   
      if (element == null)
         return "<null>";
      return element.toString();
   }
}