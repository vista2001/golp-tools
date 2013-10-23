package dev.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import dev.Activator;
import dev.actions.PrjPropertyAction;
import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.editors.AopEditor;
import dev.editors.Trade.TradeEditor;
import dev.editors.Trade.TradeEditorInput;
import dev.model.base.ResourceLeafNode;
import dev.model.base.ResourceNode;
import dev.model.base.RootNode;
import dev.model.provider.TreeContentProvider;
import dev.model.provider.TreeLabelProvider;
import dev.model.resource.AopNodes;
import dev.model.resource.DataItemNodes;
import dev.model.resource.DllNodes;
import dev.model.resource.ProjectNode;
import dev.model.resource.RetCodeNodes;
import dev.model.resource.ServerNodes;
import dev.model.resource.TFMNodes;
import dev.model.resource.TradeNodes;

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
				getSite().getPage().findView(NavView.ID).setFocus();
			}
		});
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
		getSite().setSelectionProvider(viewer);
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
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}*/

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(expandAllAction);
		manager.add(collapseAllAction);
		manager.add(new Separator());
		//drillDownAdapter.addNavigationActions(manager);
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
				IWorkbenchPage page = getViewSite().getPage();
				if (obj instanceof ResourceLeafNode) {
					try {
						TradeNodes rsn=(TradeNodes) ((ResourceLeafNode) obj).getParent();
						String name=rsn.getName();
						switch (name) {
						case "交易":
							openEditor(page,obj);
							break;
						default:
							break;
						}
						
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
		String parentName=((ResourceLeafNode)obj).getParent().getName();
		switch (parentName) {
		case "数据项":
			
			break;
		case "响应码" :
			break;
		case "服务程序" :
			break;
		case "交易" :
			//editorInput = new TradeEditorInput(((ResourceLeafNode)obj).id);
			editorInput = new TradeEditorInput((ResourceLeafNode)obj);
			break;
		default:
			break;
		}
		IEditorPart editorPart = page.findEditor(editorInput);
		if(editorPart!=null){
			page.bringToTop(editorPart);
		}else{
				page.openEditor(editorInput,TradeEditor.ID);
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
			System.out.println(iProject.getName());
			treeRoot.add(new ProjectNode(iProject.getName(),iProject.getName(),treeRoot));
		}
	}
	private void createTreeLeafs(ResourceNode obj){
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator() ;
		dbConnectImpl.openConn();
		if(obj instanceof TradeNodes){
			try {
				ResultSet rs=dbConnectImpl.retrive("select * from trade");
				List<ResourceLeafNode> l=new ArrayList<ResourceLeafNode>();
				while(rs.next()){
					l.add(new ResourceLeafNode(rs.getString(2), rs.getString(1), (TradeNodes)obj));
				}
				if(!l.isEmpty()){
					obj.removeAllChildren();
						for (ResourceLeafNode resourceLeafNode : l) {
							((TradeNodes) obj).add(resourceLeafNode);
						}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}