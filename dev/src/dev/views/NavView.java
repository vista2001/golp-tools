package dev.views;

import java.sql.ResultSet;
import java.sql.SQLException;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import dev.actions.prjPropertyAction;
import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.editors.AopEditor;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.provider.TreeContentProvider;
import dev.model.provider.TreeLabelProvider;
import dev.model.resource.ProjectNode;
import dev.model.resource.ServerNodes;

public class NavView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "dev.views.NavView";
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
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
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new TreeContentProvider());
		viewer.setLabelProvider(new TreeLabelProvider());
		viewer.setSorter(new NameSorter());
		initData();
		viewer.setInput(treeRoot);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		IWorkspace workspace=ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		System.out.println("root is:"+root.exists());
		System.out.println("root name is:"+root.getName());
		System.out.println("root location is:"+root.getLocationURI().toString());
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
		drillDownAdapter.addNavigationActions(menuMgr);
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuMgr.add(new Separator());
		menuMgr.add(new prjPropertyAction(getSite().getWorkbenchWindow()));//rxy
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
		drillDownAdapter.addNavigationActions(manager);
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
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
/*				if (obj instanceof TreeObject) {
					if (!(obj instanceof TreeParent)) {
						IWorkbenchPage page = getViewSite().getPage();
						try {
							openEditor(page,obj);
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				}*/
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
		
		/*if (((TreeObject) obj).getParent().getName().equals("服务")) {
			editorInput = new ServerEditorInput(((TreeObject) obj).getName());
		}
		if (((TreeObject) obj).getParent().getName().equals("交易")) {
			showMessage("Double-click detected on "+ obj.toString());
		}
		if (((TreeObject) obj).getParent().getName().equals("原子交易")) {
			//System.out.println("h");
			editorInput = new AopEditorInput(((TreeObject) obj).getName());
		}
		if (((TreeObject) obj).getParent().getName().equals("动态库")) {
			showMessage("Double-click detected on "+ obj.toString());
		}
		if (((TreeObject) obj).getParent().getName().equals("数据项")) {
			showMessage("Double-click detected on "+ obj.toString());
		}
		if (((TreeObject) obj).getParent().getName().equals("返回码")) {
			showMessage("Double-click detected on "+ obj.toString());
		}
		*/
		IEditorPart editorPart = page.findEditor(editorInput);
		if(editorPart!=null){
			page.bringToTop(editorPart);
		}else{
				//page.openEditor(editorInput,ServerEditor.ID);
				page.openEditor(editorInput,AopEditor.ID);
				
		}
	}

	public TreeViewer getTreeViewer() {
		// TODO Auto-generated method stub
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
		
		/*
		root = new RootNode("root", "root", null);
		ProjectNode prj1 = new ProjectNode("prj1", "prj1", root);
		
		ServerNodes s1=(ServerNodes) prj1.getChildren().get(4);
		ResourceLeafNode rln1 = new ResourceLeafNode("rln1", "rln1", s1);
		ResourceLeafNode rln2 = new ResourceLeafNode("rln2", "rln2", s1);
		s1.add(rln1);
		s1.add(rln2);
		root.add(prj1);
		
		DbConnImpl dbConnImpl=DbConnFactory.dbConnCreator();
		dbConnImpl.openConn();
		try {
			ResultSet serverRs=dbConnImpl.retrive("select * from server order by serverId");
			ResultSet tradeRs=dbConnImpl.retrive("select * from trade order by tradeId");
			ResultSet aopRs=dbConnImpl.retrive("select * from aop order by Id");
			ResultSet dllRs=dbConnImpl.retrive("select * from dll order by dllid");
			
			
			while(serverRs.next()){
				ResourceLeafNode o=new ResourceLeafNode(serverRs.getString(1),serverRs.getString(2),s1);
				
				s1.add(o);
			}
			while(tradeRs.next()){
				TreeObject o=new TreeObject(serverRs.getString(1));
				trade.addChild(o);
			}
			while(aopRs.next()){
				TreeObject o=new TreeObject(aopRs.getString(1));
				aop.addChild(o);
			}
			while(dllRs.next()){
				TreeObject o=new TreeObject(serverRs.getString(1));
				dll.addChild(o);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
	}
	
}