package dev.views;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import dev.Activator;
import dev.actions.PrjPropertyAction;
import dev.db.service.NavViewDaoServiceImpl;
import dev.editors.aop.AopEditor;
import dev.editors.aop.AopEditorInput;
import dev.editors.aopDll.AopDllEditor;
import dev.editors.aopDll.AopDllEditorInput;
import dev.editors.dataItem.DataItemEditor;
import dev.editors.dataItem.DataItemInput;
import dev.editors.retCode.RetCodeEditor;
import dev.editors.retCode.RetCodeInput;
import dev.editors.server.ServerEditor;
import dev.editors.server.ServerEditorInput;
import dev.editors.trade.TradeEditor;
import dev.editors.trade.TradeEditorInput;
import dev.model.base.ResourceLeafNode;
import dev.model.base.ResourceNode;
import dev.model.base.RootNode;
import dev.model.provider.TreeContentProvider;
import dev.model.provider.TreeLabelProvider;
import dev.model.resource.ProjectNode;

public class NavView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "dev.views.NavView";
	private TreeViewer viewer;
	//private DrillDownAdapter drillDownAdapter;
	private Action expandAllAction;
	private Action collapseAllAction;
	private Action doubleClickAction;
	private RootNode treeRoot;

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public NavView() {
	}

	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setSorter(new NameSorter());
		initData();
		viewer.setInput(treeRoot);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection s = (StructuredSelection) event.getSelection();
				if (s.getFirstElement() instanceof ResourceLeafNode) {
					TradeEditorInput editorInput = new TradeEditorInput((ResourceLeafNode) s.getFirstElement());
					
					TradeEditor tradeEditor = (TradeEditor) getSite()
							.getWorkbenchWindow().getActivePage()
							.findEditor(editorInput);
					getSite().getWorkbenchWindow().getActivePage()
							.bringToTop(tradeEditor);
				}
			}
		});
		getSite().setSelectionProvider(viewer);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void hookContextMenu() {
		/*MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				NavView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);*/
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.add(expandAllAction);
		menuMgr.add(collapseAllAction);
		menuMgr.add(new Separator());
		//drillDownAdapter.addNavigationActions(menuMgr);
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuMgr.add(new Separator());
		menuMgr.add(new PrjPropertyAction(getSite().getWorkbenchWindow()));//rxy
		//getSite().setSelectionProvider(viewer);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(expandAllAction);
		manager.add(new Separator());
		manager.add(collapseAllAction);
	}

/*	private void fillContextMenu(IMenuManager manager) {
		manager.add(expandAllAction);
		manager.add(collapseAllAction);
		manager.add(new Separator());
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}*/

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(expandAllAction);
		manager.add(collapseAllAction);
		manager.add(new Separator());
	}

	private void makeActions() {
		expandAllAction = new Action() {
			public void run() {
				viewer.collapseAll();
			}
		};
		expandAllAction.setText("全部折叠");
		expandAllAction.setToolTipText("全部折叠");
		expandAllAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/collapse.gif"));

		collapseAllAction = new Action() {
			public void run() {
				viewer.expandAll();System.out.println("全部折叠已经执行");
			}
		};
		collapseAllAction.setText("全部展开");
		collapseAllAction.setToolTipText("全部展开");
		collapseAllAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/expand.gif"));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				IWorkbenchPage page = getViewSite().getWorkbenchWindow().getActivePage();
				if (obj instanceof ResourceLeafNode) {
					try {
						openEditor(page,obj);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
				if(obj instanceof ResourceNode){
					createTreeLeafs((ResourceNode)obj);
					viewer.refresh();
				}
				
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"NavView", message);
	}
	
	private void openEditor(IWorkbenchPage page,Object obj) throws PartInitException{
		IEditorInput editorInput=null;
		ResourceLeafNode node=(ResourceLeafNode)obj;
		String name=node.getParent().getName();
		switch (name) {
		case "数据项":
			editorInput = new DataItemInput((node));
			page.openEditor(editorInput,DataItemEditor.ID,false);
			break;
		case "响应码" :
			editorInput = new RetCodeInput(node);
			page.openEditor(editorInput,RetCodeEditor.ID,false);
			break;
		case "动态库" :
			editorInput = new AopDllEditorInput(node);
			page.openEditor(editorInput,AopDllEditor.ID,false);
			break;
		case "原子交易" :
			editorInput=new AopEditorInput(node);
			page.openEditor(editorInput,AopEditor.ID,false);
			break;
		case "服务程序" :
			editorInput= new ServerEditorInput(node);
			page.openEditor(editorInput, ServerEditor.ID,false);
			break;
		case "交易" :
			editorInput = new TradeEditorInput(node);
			page.openEditor(editorInput,TradeEditor.ID,false);
			break;
		default:
			break;
		}
	}

	public TreeViewer getTreeViewer() {
		return this.viewer;
	}
	
	private void initData(){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject[] projects=prjRoot.getProjects();
		treeRoot=new RootNode("root", "root", null);
		for (IProject iProject : projects) {
			treeRoot.add(new ProjectNode(iProject.getName(),iProject.getName(),treeRoot));
		}
	}
	private void createTreeLeafs(ResourceNode obj){
		
		String prjId = obj.getParent().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject project = root.getProject(prjId);
	    String propertyPath = project.getLocationURI().toString().substring(6) + '/' + prjId +".properties";
	    PreferenceStore ps = new PreferenceStore(propertyPath);
	    try {
			ps.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    NavViewDaoServiceImpl navViewDaoServiceImpl=new NavViewDaoServiceImpl();
	    String tableName=obj.getName();
	    switch(tableName){
	    case "数据项":
	    	tableName="DataItem";
			break;
		case "响应码" :
			tableName="RetCode";
			break;
		case "动态库" :
			tableName="AopDll";
			break;
		case "原子交易" :
			tableName="Aop";
			break;
	    case "服务程序":
	    	tableName="Server";
	    	break;
	    case "交易":
	    	tableName="Trade";
	    }
	    LinkedHashMap<String, String> map=navViewDaoServiceImpl.getResourceNodeChild(ps, tableName);
	    if(!map.isEmpty()){
			obj.removeAllChildren();
			for (String key : map.keySet()) {
				ResourceLeafNode rln =new ResourceLeafNode(map.get(key), key, obj);
				obj.getChildren().add(rln);
			}
		}
	}
}