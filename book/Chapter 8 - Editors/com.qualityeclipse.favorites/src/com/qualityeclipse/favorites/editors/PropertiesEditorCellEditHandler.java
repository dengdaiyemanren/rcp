package com.qualityeclipse.favorites.editors;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Open an inline editing area over-top the currently selected item in the
 * Properties editor.
 */
public class PropertiesEditorCellEditHandler extends AbstractHandler
{
   public Object execute(ExecutionEvent event)
         throws ExecutionException {
      IWorkbenchPart part = HandlerUtil.getActivePart(event);
      if (!(part instanceof PropertiesEditor))
         return null;
      editElement((PropertiesEditor) part);
      return null;
   }

   public void editElement(PropertiesEditor editor) {
   }
}
