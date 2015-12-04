package com.qualityeclipse.favorites.editors;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.qualityeclipse.favorites.FavoritesLog;
import com.qualityeclipse.favorites.views.AltClickCellEditListener;

/**
 * Editor for modifying *.property files containing content in the
 * typical key=value format
 */
public class PropertiesEditor extends MultiPageEditorPart
{
   private TreeViewer treeViewer;
   private TextEditor textEditor;

   private TreeColumn keyColumn;
   private TreeColumn valueColumn;

   private PropertiesEditorContentProvider treeContentProvider;
   private PropertiesEditorLabelProvider treeLabelProvider;
   
   private boolean isPageModified;
   
   private UndoActionHandler undoAction;
   private RedoActionHandler redoAction;
   private IUndoContext undoContext;
   
   /**
    * When a user edits a value, the model generates a change event to
    * notify registered listeners. This change listener receives
    * notification events and updates the tree appropriately.
    */
   private final PropertyFileListener propertyFileListener =
      new PropertyFileListener()
   {
      public void keyChanged(
         PropertyCategory category, PropertyEntry entry
      ) {
         treeViewer.refresh(entry);
         treeModified();
      }

      public void valueChanged(
         PropertyCategory category, PropertyEntry entry
      ) {
         treeViewer.refresh(entry);
         treeModified();
      }

      public void nameChanged(PropertyCategory category) {
         treeViewer.refresh(category);
         treeModified();
      }

      public void entryAdded(
         PropertyCategory category, PropertyEntry entry
      ) {
         treeViewer.refresh();
         treeModified();
      }

      public void entryRemoved(
         PropertyCategory category, PropertyEntry entry
      ) {
         treeViewer.refresh();
         treeModified();
      }

      public void categoryAdded(PropertyCategory category) {
         treeViewer.refresh();
         treeModified();
      }

      public void categoryRemoved(PropertyCategory category) {
         treeViewer.refresh();
         treeModified();
      }
   };

   /**
    * Initializes this editor with the given editor site and input.
    * This method is automatically called shortly after the part is
    * instantiated, marking the start of the part's lifecycle.
    * In our case, we assert that we are editing a file.
    */
   public void init(IEditorSite site, IEditorInput input)
      throws PartInitException
   {
      if (!(input instanceof IFileEditorInput))
         throw new PartInitException(
            "Invalid Input: Must be IFileEditorInput");
      super.init(site, input);
   }

   /**
    * Creates the Source and Properties pages 
    * of this multi-page editor.
    */
   protected void createPages() {
      createPropertiesPage();
      createSourcePage();
      updateTitle();
      initTreeContent();
      initTreeEditors();
      createContextMenu();
      initKeyBindingContext();
      initUndoRedo();
   }

   void createPropertiesPage() {
      Composite treeContainer = new Composite(getContainer(), SWT.NONE);
      TreeColumnLayout layout = new TreeColumnLayout();
      treeContainer.setLayout(layout);
      
      treeViewer = new TreeViewer(treeContainer, SWT.MULTI | SWT.FULL_SELECTION);
      Tree tree = treeViewer.getTree();
      tree.setHeaderVisible(true);
      
      keyColumn = new TreeColumn(tree, SWT.NONE);
      keyColumn.setText("Key");
      layout.setColumnData(keyColumn, new ColumnWeightData(2));
   
      valueColumn = new TreeColumn(tree, SWT.NONE);
      valueColumn.setText("Value");
      layout.setColumnData(valueColumn, new ColumnWeightData(3));
      
      int index = addPage(treeContainer);
      setPageText(index, "Properties");
      getSite().setSelectionProvider(treeViewer);
   }

   void createSourcePage() {
      try {
         textEditor = new TextEditor();
         int index = addPage(textEditor, getEditorInput());
         setPageText(index, "Source");
      }
      catch (PartInitException e) {
         FavoritesLog.logError("Error creating nested text editor",e);
      }
   }
   
   /**
    * Update the editor's title based upon the content being edited.
    */
   void updateTitle() {
      IEditorInput input = getEditorInput();
      setPartName(input.getName());
      setTitleToolTip(input.getToolTipText());
   }

   /**
    * Initialize the model behind the editor.
    */
   void initTreeContent() {
      treeContentProvider = new PropertiesEditorContentProvider();
      treeViewer.setContentProvider(treeContentProvider);
      treeLabelProvider = new PropertiesEditorLabelProvider();
      treeViewer.setLabelProvider(treeLabelProvider);
      
      // Reset the input from the text editor’s content
      // after the editor initialization has completed.
      treeViewer.setInput(new PropertyFile(""));
      treeViewer.getTree().getDisplay().asyncExec(new Runnable() {
         public void run() {
            updateTreeFromTextEditor();
         }
      });
      treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
   }

   /**
    * Initialize the cell editors in the tree
    */
   private void initTreeEditors() {
      TreeViewerColumn column1 = new TreeViewerColumn(treeViewer, keyColumn);
      TreeViewerColumn column2 = new TreeViewerColumn(treeViewer, valueColumn);
      
      column1.setLabelProvider(new ColumnLabelProvider() {
         public String getText(Object element) {
            return treeLabelProvider.getColumnText(element, 0);
         }
      });
      column2.setLabelProvider(new ColumnLabelProvider() {
         public String getText(Object element) {
            return treeLabelProvider.getColumnText(element, 1);
         }
      });
      
      column1.setEditingSupport(new EditingSupport(treeViewer) {
         TextCellEditor editor = null;

         protected boolean canEdit(Object element) {
            return true;
         }

         protected CellEditor getCellEditor(Object element) {
            if (editor == null) {
               Composite tree = (Composite) treeViewer.getControl();
               editor = new TextCellEditor(tree);
               editor.setValidator(new ICellEditorValidator() {
                  public String isValid(Object value) {
                     if (((String) value).trim().length() == 0)
                        return "Key must not be empty string";
                     return null;
                  }
               });
               editor.addListener(new ICellEditorListener() {
                  public void applyEditorValue() {
                     setErrorMessage(null);
                  }
                  public void cancelEditor() {
                     setErrorMessage(null);
                  }
                  public void editorValueChanged(
                        boolean oldValidState,
                        boolean newValidState) {
                     setErrorMessage(editor.getErrorMessage());
                  }
                  private void setErrorMessage(String errorMessage) {
                     getEditorSite().getActionBars()
                           .getStatusLineManager()
                           .setErrorMessage(errorMessage);
                  }
               });
            }
            return editor;
         }

         protected Object getValue(Object element) {
            return treeLabelProvider.getColumnText(element, 0);
         }

         protected void setValue(Object element, Object value) {
            if (value == null)
               return;
            String text = ((String) value).trim();
            if (element instanceof PropertyCategory)
               ((PropertyCategory) element).setName(text);
            if (element instanceof PropertyEntry)
               ((PropertyEntry) element).setKey(text);
         }
      });
      
      column2.setEditingSupport(new EditingSupport(treeViewer) {
         TextCellEditor editor = null;

         protected boolean canEdit(Object element) {
            return element instanceof PropertyEntry;
         }

         protected CellEditor getCellEditor(Object element) {
            if (editor == null) {
               Composite tree = (Composite) treeViewer.getControl();
               editor = new TextCellEditor(tree);
            }
            return editor;
         }

         protected Object getValue(Object element) {
            return treeLabelProvider.getColumnText(element, 1);
         }

         protected void setValue(Object element, Object value) {
            String text = ((String) value).trim();
            if (element instanceof PropertyEntry)
               ((PropertyEntry) element).setValue(text);
         }
      });
      
      treeViewer.getColumnViewerEditor().addEditorActivationListener(
            new AltClickCellEditListener());
   }
   
   /**
    * Create the context menu for the editor. Because contributors can
    * add and remove menu items based on the selection, its contents
    * cannot be determined until just after the user clicks the right
    * mouse button and just before the menu is displayed.
    * @see #fillContextMenu(IMenuManager)
    */
   private void createContextMenu() {
      MenuManager menuMgr = new MenuManager("#PopupMenu");
      menuMgr.setRemoveAllWhenShown(true);
      menuMgr.addMenuListener(new IMenuListener() {
         public void menuAboutToShow(IMenuManager m) {
            PropertiesEditor.this.fillContextMenu(m);
         }
      });
      Tree tree = treeViewer.getTree();
      Menu menu = menuMgr.createContextMenu(tree);
      tree.setMenu(menu);
      getSite().registerContextMenu(menuMgr,treeViewer);
   }
   
   /**
    * Dynamically build the context menu content just after the user
    * clicks the right mouse button and just before the menu is displayed.
    * @see #createContextMenu()
    */
   private void fillContextMenu(IMenuManager menuMgr) {
      menuMgr.add(undoAction);
      menuMgr.add(redoAction);
      menuMgr.add(new Separator("edit"));
      menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
   }

   /**
    * Activate the "com.qualityeclipse.properties.editor.context" key binding
    * context and activate the undo/redo handlers when the treeViewer gains
    * focus and deactivate the context and handlers when the treeViewer looses
    * focus.
    */
   private void initKeyBindingContext() {
      final IContextService service =
            (IContextService) getSite().getService(IContextService.class);
      
      treeViewer.getControl().addFocusListener(new FocusListener() {
         IContextActivation currentContext = null;

         public void focusGained(FocusEvent e) {
            if (currentContext == null)
               currentContext =
                     service.activateContext("com.qualityeclipse.properties.editor.context");
         }

         public void focusLost(FocusEvent e) {
            if (currentContext != null)
               service.deactivateContext(currentContext);
         }
      });
   }

   /**
    * Initialize fields for undo/redo operations
    */
   private void initUndoRedo() {
      undoContext = new ObjectUndoContext(this);
      undoAction = new UndoActionHandler(getSite(), undoContext);
      redoAction = new RedoActionHandler(getSite(), undoContext);
   }
   
   private void setTreeUndoRedo() {
      final IActionBars actionBars = getEditorSite().getActionBars();
      actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);
      actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);
      actionBars.updateActionBars();
   }

   private void setTextEditorUndoRedo() {
      final IActionBars actionBars = getEditorSite().getActionBars();
      IAction undoAction2 = textEditor.getAction(ActionFactory.UNDO.getId());
      actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction2);
      IAction redoAction2 = textEditor.getAction(ActionFactory.REDO.getId());
      actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction2);
      actionBars.updateActionBars();
      getOperationHistory().dispose(undoContext, true, true, false);
   }
   
   /**
    * Called when the properties page should be updated
    * based upon new input from the source page
    */
   void updateTreeFromTextEditor() {
      PropertyFile propertyFile = (PropertyFile) treeViewer.getInput();
      propertyFile.removePropertyFileListener(propertyFileListener);
      propertyFile = new PropertyFile(
         textEditor
            .getDocumentProvider()
            .getDocument(textEditor.getEditorInput())
            .get());
      treeViewer.setInput(propertyFile);
      propertyFile.addPropertyFileListener(propertyFileListener);
   }
   
   /**
    * Called when the source page should be updated
    * based upon new input from the properties page.
    */
   void updateTextEditorFromTree() {
      textEditor
         .getDocumentProvider()
         .getDocument(textEditor.getEditorInput())
         .set(((PropertyFile) treeViewer.getInput()).asText());
   }
   
   /**
    * Called when a cell editor has modified the editor model.
    */
   public void treeModified() {
      boolean wasDirty = isDirty();
      isPageModified = true;
      if (!wasDirty)
         firePropertyChange(IEditorPart.PROP_DIRTY);
   }
   
   /**
    * Handles a property change notification from a nested editor. In
    * our case, the <code>isPageModified</code> field is adjusted as
    * appropriate and superclass is called to notify listeners of the
    * change.
    */
   protected void handlePropertyChange(int propertyId) {
      if (propertyId == IEditorPart.PROP_DIRTY)
         isPageModified = isDirty();
      super.handlePropertyChange(propertyId);
   }
   
   /**
    * Returns whether the contents of this editor have changed since
    * the last save operation. If this value changes the editor must
    * fire a property listener event with <code>PROP_DIRTY</code>.
    */
   public boolean isDirty() {
      return isPageModified || super.isDirty();
   }

   /**
    * Notifies this multi-page editor that the page with the given id has been
    * activated. This method is called when the user selects a different tab.
    */
   protected void pageChange(int newPageIndex) {
      switch (newPageIndex) {
         case 0 :
            if (isDirty())
               updateTreeFromTextEditor();
            setTreeUndoRedo();
            break;
         case 1 :
            if (isPageModified)
               updateTextEditorFromTree();
            setTextEditorUndoRedo();
            break;
      }
      isPageModified = false;
      
      super.pageChange(newPageIndex);
   }

   /**
    * Asks this part to take focus within the workbench.
    * In our case, focus is redirected to the appropriate
    * control based upon what page is currently selected.
    */
   public void setFocus() {
      switch (getActivePage()) {
         case 0:
            treeViewer.getTree().setFocus();
            break;
         case 1:
            textEditor.setFocus();
            break;
      }
   }
   
   /**
    * Sets the cursor and selection state for an editor to 
    * reveal the position of the given marker.
    */
   public void gotoMarker(IMarker marker) {
      setActivePage(1);
      ((IGotoMarker) getAdapter(IGotoMarker.class))
         .gotoMarker(marker);
   }

   /**
    * Returns whether the "Save As" operation is supported by this part.
    */
   public boolean isSaveAsAllowed() {
      return true;
   }

   /**
    * Saves the contents of this part. If the save is successful, the
    * part should fire a property changed event reflecting the new
    * dirty state (<code>PROP_DIRTY</code> property).
    */
   public void doSave(IProgressMonitor monitor) {
      if (getActivePage() == 0 && isPageModified)
         updateTextEditorFromTree();
      isPageModified = false;
      textEditor.doSave(monitor);
   }

   /**
    * Saves the contents of this part to another object. Implementors
    * are expected to open a "Save As" dialog where the user will be
    * able to select a new name for the contents. After the selection
    * is made, the contents should be saved to that new name. If the
    * save is successful, the part fires a property changed event
    * reflecting the new dirty state (<code>PROP_DIRTY</code>
    * property).
    */
   public void doSaveAs() {
      if (getActivePage() == 0 && isPageModified)
         updateTextEditorFromTree();
      isPageModified = false;
      textEditor.doSaveAs();
      setInput(textEditor.getEditorInput());
      updateTitle();
   }

   /**
    * Get the operation history from the workbench.
    * This is also known as the undo/redo manager
    */
   public IOperationHistory getOperationHistory() {
      
      // The workbench provides its own undo/redo manager
      //return PlatformUI.getWorkbench()
      //   .getOperationSupport().getOperationHistory();
      
      // which, in this case, is the same as the default undo manager
      return OperationHistoryFactory.getOperationHistory();
   }

   /**
    * Answer an undo context used to "tag" operations as being
    * applicable to this editor. The undo context is used to filter
    * the history of operations available for undo or redo so that
    * only operations appropriate for a given undo context are shown
    * when the application is presenting that context.
    */
   public IUndoContext getUndoContext() {
      
      // For workbench-wide operations, we should return
      //return PlatformUI.getWorkbench()
      //   .getOperationSupport().getUndoContext();
      
      // but our operations are all local, so return our own content
      return undoContext;
   }
}
