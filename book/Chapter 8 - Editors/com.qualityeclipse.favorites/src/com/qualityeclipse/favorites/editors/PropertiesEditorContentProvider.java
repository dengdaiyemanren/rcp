package com.qualityeclipse.favorites.editors;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A content provider mediates between the viewer's model and the
 * viewer itself. In our case, this content provider extracts property
 * elements to be displayed one per row in the editor.
 */
public class PropertiesEditorContentProvider
   implements ITreeContentProvider
{
   public void inputChanged(
      Viewer viewer, Object oldInput, Object newInput
   ) { }
   
   public Object[] getElements(Object element) {
      return getChildren(element);
   }
   
   public Object[] getChildren(Object element) {
      if (element instanceof PropertyElement)
         return ((PropertyElement) element).getChildren();
      return null;
   }
   
   public Object getParent(Object element) {
      if (element instanceof PropertyElement)
         return ((PropertyElement) element).getParent();
      return null;
   }
   
   public boolean hasChildren(Object element) {
      if (element instanceof PropertyElement)
         return ((PropertyElement) element).getChildren().length > 0;
      return false;
   }
   
   public void dispose() {
   }
}
