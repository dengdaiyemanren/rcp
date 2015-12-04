/**
 * 
 */
package com.qualityeclipse.favorites.views;

import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

public final class AltClickCellEditListener extends
      ColumnViewerEditorActivationListener
{
   public void beforeEditorActivated(
         ColumnViewerEditorActivationEvent event) {
      if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION) {
         if (!(event.sourceEvent instanceof MouseEvent))
            event.cancel = true;
         else {
            MouseEvent mouseEvent =
                  (MouseEvent) event.sourceEvent;
            if ((mouseEvent.stateMask & SWT.ALT) == 0)
               event.cancel = true;
         }
      }
      else if (event.eventType != ColumnViewerEditorActivationEvent.PROGRAMMATIC)
         event.cancel = true;
   }

   public void afterEditorActivated(
         ColumnViewerEditorActivationEvent event) {
   }

   public void beforeEditorDeactivated(
         ColumnViewerEditorDeactivationEvent event) {
   }

   public void afterEditorDeactivated(
         ColumnViewerEditorDeactivationEvent event) {
   }
}