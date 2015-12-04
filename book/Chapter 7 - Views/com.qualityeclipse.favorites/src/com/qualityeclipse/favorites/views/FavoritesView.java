package com.qualityeclipse.favorites.views;

import java.util.Comparator;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

import com.qualityeclipse.favorites.actions.FavoritesViewFilterAction;
import com.qualityeclipse.favorites.contributions.RemoveFavoritesContributionItem;
import com.qualityeclipse.favorites.handlers.RemoveFavoritesHandler;
import com.qualityeclipse.favorites.handlers.RenameFavoritesHandler;
import com.qualityeclipse.favorites.model.FavoritesManager;
import com.qualityeclipse.favorites.model.IFavoriteItem;
import com.qualityeclipse.favorites.util.EditorUtil;

/**
 * This is the primary class behind the Favorites view. It creates the view
 * controls, associates those controls with a model, defines how the model
 * content should be displayed, etc.
 */

public class FavoritesView extends ViewPart
{
   public static final String ID =
         "com.qualityeclipse.favorites.views.FavoritesView";

   private TableViewer viewer;
   private TableColumn typeColumn;
   private TableColumn nameColumn;
   private TableColumn locationColumn;
   private FavoritesViewSorter sorter;

   private FavoritesViewFilterAction filterAction;

   private IHandler removeHandler;
   private RemoveFavoritesContributionItem removeContributionItem;

   private ISelectionListener pageSelectionListener;
   
   private IMemento memento;
   
   /**
    * The constructor.
    */
   public FavoritesView() {
   }

   /**
    * This is a callback that will allow us to create the viewer and initialize
    * it.
    */
   public void createPartControl(Composite parent) {
	   
      createTableViewer(parent);
      
      createTableSorter();
      
      createContributions();
      
      createContextMenu();
      
      createToolbarButtons();
      
      createViewPulldownMenu();
      
      hookKeyboard();
      
      hookGlobalHandlers();
      
      hookDragAndDrop();
      
      createInlineEditor();
      
      hookMouse();
      
      hookPageSelection();
   }

   private void createTableViewer(Composite parent) {
      viewer =
            new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
                  | SWT.MULTI | SWT.FULL_SELECTION);
      final Table table = viewer.getTable();

      TableColumnLayout layout = new TableColumnLayout();
      parent.setLayout(layout);

      typeColumn = new TableColumn(table, SWT.LEFT);
      typeColumn.setText("");
      layout.setColumnData(typeColumn, new ColumnPixelData(18));

      nameColumn = new TableColumn(table, SWT.LEFT);
      nameColumn.setText("Name");
      layout.setColumnData(nameColumn, new ColumnWeightData(4));

      locationColumn = new TableColumn(table, SWT.LEFT);
      locationColumn.setText("Location");
      layout.setColumnData(locationColumn, new ColumnWeightData(9));

      table.setHeaderVisible(true);
      table.setLinesVisible(false);

      viewer.setContentProvider(new FavoritesViewContentProvider());
      viewer.setLabelProvider(new FavoritesViewLabelProvider());
      viewer.setInput(FavoritesManager.getManager());

      getSite().setSelectionProvider(viewer);
   }

   private void createTableSorter() {
      Comparator<IFavoriteItem> nameComparator =
            new Comparator<IFavoriteItem>() {
               public int compare(IFavoriteItem i1, IFavoriteItem i2) {
                  return i1.getName().compareTo(i2.getName());
               }
            };
      Comparator<IFavoriteItem> locationComparator =
            new Comparator<IFavoriteItem>() {
               public int compare(IFavoriteItem i1, IFavoriteItem i2) {
                  return i1.getLocation().compareTo(i2.getLocation());
               }
            };
      Comparator<IFavoriteItem> typeComparator =
            new Comparator<IFavoriteItem>() {
               public int compare(IFavoriteItem i1, IFavoriteItem i2) {
                  return i1.getType().compareTo(i2.getType());
               }
            };
      sorter =
            new FavoritesViewSorter(viewer, new TableColumn[] {
                  nameColumn, locationColumn, typeColumn },
                  new Comparator[] {
                        nameComparator, locationComparator,
                        typeComparator });
      if (memento != null)
         sorter.init(memento);
      viewer.setSorter(sorter);
   }

   private void createContributions() {
      removeHandler = new RemoveFavoritesHandler();
      removeContributionItem =
            new RemoveFavoritesContributionItem(getViewSite(),
                  removeHandler);
   }

   private void createContextMenu() {
      MenuManager menuMgr = new MenuManager("#PopupMenu");
      menuMgr.setRemoveAllWhenShown(true);
      menuMgr.addMenuListener(new IMenuListener() {
         public void menuAboutToShow(IMenuManager m) {
            FavoritesView.this.fillContextMenu(m);
         }
      });
      Menu menu = menuMgr.createContextMenu(viewer.getControl());
      viewer.getControl().setMenu(menu);
      getSite().registerContextMenu(menuMgr, viewer);
   }

   private void fillContextMenu(IMenuManager menuMgr) {
      menuMgr.add(new Separator("edit"));
      menuMgr.add(removeContributionItem);
      menuMgr.add(new Separator(
            IWorkbenchActionConstants.MB_ADDITIONS));
   }

   private void createToolbarButtons() {
      IToolBarManager toolBarMgr =
            getViewSite().getActionBars().getToolBarManager();
      toolBarMgr.add(new GroupMarker("edit"));
      toolBarMgr.add(removeContributionItem);
   }

   private void createViewPulldownMenu() {
      IMenuManager menu =
            getViewSite().getActionBars().getMenuManager();
      filterAction =
            new FavoritesViewFilterAction(viewer, "Filter...");
       if (memento != null)
         filterAction.init(memento);
      menu.add(filterAction);
   }

   private void hookKeyboard() {
      viewer.getControl().addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent event) {
            handleKeyReleased(event);
         }
      });
   }

   protected void handleKeyReleased(KeyEvent event) {
      if (event.keyCode == SWT.F2 && event.stateMask == 0) {
         new RenameFavoritesHandler().editElement(this);
      }
      if (event.character == SWT.DEL && event.stateMask == 0) {
         removeContributionItem.run();
      }
   }

   private void hookGlobalHandlers() {
      // getViewSite().getActionBars().setGlobalActionHandler(
      // ActionFactory.CUT.getId(), cutAction);
      // getViewSite().getActionBars().setGlobalActionHandler(
      // ActionFactory.COPY.getId(), copyAction);
      // getViewSite().getActionBars().setGlobalActionHandler(
      // ActionFactory.PASTE.getId(), pasteAction);

      final IHandlerService handlerService =
            (IHandlerService) getViewSite().getService(
                  IHandlerService.class);
      viewer.addSelectionChangedListener(new ISelectionChangedListener() {
         private IHandlerActivation removeActivation;

         public void selectionChanged(SelectionChangedEvent event) {
            if (event.getSelection().isEmpty()) {
               if (removeActivation != null) {
                  handlerService.deactivateHandler(removeActivation);
                  removeActivation = null;
               }
            }
            else {
               if (removeActivation == null) {
                  removeActivation =
                        handlerService.activateHandler(
                              IWorkbenchActionDefinitionIds.DELETE,
                              removeHandler);
               }
            }
         }
      });
   }

   private void hookDragAndDrop() {
      new FavoritesDragSource(viewer);
      new FavoritesDropTarget(viewer);
   }

   private void hookMouse() {
      viewer.getTable().addMouseListener(new MouseAdapter() {
         public void mouseDoubleClick(MouseEvent e) {
            EditorUtil.openEditor(getSite().getPage(),
                  viewer.getSelection());
         }
      });
   }

   private void hookPageSelection() {
      pageSelectionListener = new ISelectionListener() {
         public void selectionChanged(
            IWorkbenchPart part,
            ISelection selection) {
               pageSelectionChanged(part, selection);
         }
      };
      getSite().getPage().addPostSelectionListener(
         pageSelectionListener);
   }

   protected void pageSelectionChanged(
      IWorkbenchPart part,
      ISelection selection
   ) {
      if (part == this)
         return;
      if (!(selection instanceof IStructuredSelection))
         return;
      IStructuredSelection sel = (IStructuredSelection) selection;
      IFavoriteItem[] items = FavoritesManager.getManager()
         .existingFavoritesFor(sel.iterator());
      if (items.length > 0)
         viewer.setSelection(new StructuredSelection(items), true);
   }
   
   private void createInlineEditor() {
      TableViewerColumn column =
            new TableViewerColumn(viewer, nameColumn);

      column.setLabelProvider(new ColumnLabelProvider() {
         public String getText(Object element) {
            return ((IFavoriteItem) element).getName();
         }
      });

      column.setEditingSupport(new EditingSupport(viewer) {
         TextCellEditor editor = null;

         protected boolean canEdit(Object element) {
            return true;
         }

         protected CellEditor getCellEditor(Object element) {
            if (editor == null) {
               Composite table = (Composite) viewer.getControl();
               editor = new TextCellEditor(table);
            }
            return editor;
         }

         protected Object getValue(Object element) {
            return ((IFavoriteItem) element).getName();
         }

         protected void setValue(Object element, Object value) {
            ((IFavoriteItem) element).setName((String) value);
            viewer.refresh(element);
         }
      });

      viewer.getColumnViewerEditor().addEditorActivationListener(
            new ColumnViewerEditorActivationListener() {
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
            });
   }

   /**
    * Passing the focus request to the viewer's control.
    */
   public void setFocus() {
      viewer.getControl().setFocus();
   }

   /**
    * For testing purposes only.
    * 
    * @return the table viewer in the Favorites view
    */
   public TableViewer getFavoritesViewer() {
      return viewer;
   }

   /**
    * Disposes of this view. Clients should not call this method; it is called
    * by the Eclipse infrastructure.
    * <p>
    * This is the last method called on the view. At this point the part
    * controls (if they were ever created) have been disposed as part of an SWT
    * composite. There is no guarantee that createPartControl() has been called,
    * so the part controls may never have been created.
    * <p>
    * Within this method a part may release any resources, fonts, images,
    * etc.&nbsp; held by this part. It is also very important to deregister all
    * listeners from the workbench.
    */
   public void dispose() {
      if (pageSelectionListener != null)
       getSite().getPage().removePostSelectionListener(
       pageSelectionListener);
      super.dispose();
   }
   
   /**
    * Saves the view state such as sort order and filter state 
    * within a memento.
    */
   public void saveState(IMemento memento) {
      super.saveState(memento);
      sorter.saveState(memento);
      filterAction.saveState(memento);
   }

   /**
    * Initializes this view with the given view site. A memento is passed to the
    * view which contains a snapshot of the views state such as sort order and
    * filter state from a previous session. In our case, the sort and filter
    * state cannot be initialized immediately, so we cache the memento for
    * later.
    */
   public void init(IViewSite site, IMemento memento)
         throws PartInitException {
      super.init(site, memento);
      this.memento = memento;
   }

   /**
    * Answer the currently selected favorite items
    */
   public IStructuredSelection getSelection() {
      return (IStructuredSelection) viewer.getSelection();
   }

   /**
    * Report selection changes to the specified listener
    * 
    * @param selectionListener
    *           the listener (not <code>null</code>)
    */
   public void addSelectionChangedListener(
         ISelectionChangedListener listener) {
      viewer.addSelectionChangedListener(listener);
   }
}