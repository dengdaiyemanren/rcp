package com.qualityeclipse.favorites.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;

import com.qualityeclipse.favorites.model.FavoritesManager;

/**
 * Provide support for dragging the objects into the Favorites view from another
 * view.
 */
public class FavoritesDropTarget extends DropTargetAdapter
{
   public FavoritesDropTarget(TableViewer viewer) {
      DropTarget target =
            new DropTarget(viewer.getControl(), DND.DROP_MOVE
                  | DND.DROP_COPY);
      target.setTransfer(new Transfer[] {
            ResourceTransfer.getInstance(),
            JavaUI.getJavaElementClipboardTransfer() });
      target.addDropListener(this);
   }

   public void dragEnter(DropTargetEvent event) {
      if (event.detail == DND.DROP_MOVE
            || event.detail == DND.DROP_DEFAULT) {
         if ((event.operations & DND.DROP_COPY) != 0)
            event.detail = DND.DROP_COPY;
         else
            event.detail = DND.DROP_NONE;
      }
   }

   public void drop(DropTargetEvent event) {
      FavoritesManager manager = FavoritesManager.getManager();
      if (JavaUI.getJavaElementClipboardTransfer().isSupportedType(
            event.currentDataType)
            && (event.data instanceof IJavaElement[])) {
         manager.addFavorites((IJavaElement[]) event.data);
         event.detail = DND.DROP_COPY;
      }
      else if (ResourceTransfer.getInstance().isSupportedType(
            event.currentDataType)
            && (event.data instanceof IResource[])) {
         manager.addFavorites((IResource[]) event.data);
         event.detail = DND.DROP_COPY;
      }
      else
         event.detail = DND.DROP_NONE;
   }
}
