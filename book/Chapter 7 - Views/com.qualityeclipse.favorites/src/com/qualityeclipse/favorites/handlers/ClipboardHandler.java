package com.qualityeclipse.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Superclass for clipboard related handlers providing safe creation and
 * disposal of the {@link Clipboard}.
 */
public abstract class ClipboardHandler extends AbstractHandler
{
   public Object execute(ExecutionEvent event)
         throws ExecutionException {
      Clipboard clipboard =
            new Clipboard(HandlerUtil.getActiveShell(event)
                  .getDisplay());
      try {
         return execute(event, clipboard);
      }
      finally {
         clipboard.dispose();
      }
   }

   protected abstract Object execute(ExecutionEvent event,
         Clipboard clipboard) throws ExecutionException;
}