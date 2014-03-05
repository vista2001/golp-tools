/* 文件名：       NavView.java
 * 修改人：       rxy
 * 修改时间：   2013.12.6
 * 修改内容：   1.修改createTreeLeafs方法，增加对流程图的处理；
 *         2.修改createPartControl方法，增加以下处理：调用
 *         viewer对象的getTree方法，得到对象tree，给这个tree
 *         对象添加mouseDown的事件监听器，在此监听器的实现中激活
 *         导航视图（NavView）；
 *         3.修改所有编辑器的打开方式，由原来的不激活变为激活；
 *         4.用DebugOut.println方法替换System.out.println方法；
 *         5.改正双击工程节点时报错的问题；
 *         6.完善单击导航中的三级节点时，已打开编辑器的切换；
 *         7.统一使用File.separator。
 */

package dev.views;

import java.sql.SQLException;
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import dev.Activator;
import dev.db.service.NavViewDaoServiceImpl;
import dev.diagram.ui.DiagramEditor;
import dev.diagram.ui.DiagramEditorInput;
import dev.editors.aop.AopEditor;
import dev.editors.aop.AopEditorInput;
import dev.editors.aopDll.AopDllEditor;
import dev.editors.aopDll.AopDllEditorInput;
import dev.editors.dataItem.DataItemEditor;
import dev.editors.dataItem.DataItemEditorInput;
import dev.editors.retCode.RetCodeEditor;
import dev.editors.retCode.RetCodeEditorInput;
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
import dev.util.CommonUtil;
import dev.util.DevLogger;

public class NavView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "dev.views.NavView";
	private TreeViewer viewer;
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
		Tree tree = viewer.getTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					// 当tree获得鼠标点击时，激活NavView。
					NavView.this.getSite().getWorkbenchWindow().getActivePage()
							.showView(ID);
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
			}
		});

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
				StructuredSelection s = (StructuredSelection) event
						.getSelection();
				if (s.getFirstElement() instanceof ResourceLeafNode) {
					ResourceLeafNode resourceLeafNode = (ResourceLeafNode) s
							.getFirstElement();
					IEditorInput editorInput = null;
					switch (resourceLeafNode.getParent().getName()) {
					case "服务程序":
						editorInput = new ServerEditorInput(resourceLeafNode);
						break;
					case "交易":
						editorInput = new TradeEditorInput(resourceLeafNode);
						break;
					case "动态库":
						editorInput = new AopDllEditorInput(resourceLeafNode);
						break;
					case "原子交易":
						editorInput = new AopEditorInput(resourceLeafNode);
						break;
					case "数据项":
						editorInput = new DataItemEditorInput(resourceLeafNode);
						break;
					case "响应码":
						editorInput = new RetCodeEditorInput(resourceLeafNode);
						break;
					case "流程图":
						editorInput = new DiagramEditorInput(resourceLeafNode);
						break;
					default:
					}
					if (editorInput != null) {
						IEditorPart editorPart = getSite().getWorkbenchWindow()
								.getActivePage().findEditor(editorInput);
						if (editorPart != null) {
							getSite().getWorkbenchWindow().getActivePage()
									.bringToTop(editorPart);
							getSite().getShell().setText(
									"GOLP TOOL" + " " + editorPart.getTitle());
						}
					}
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
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.add(expandAllAction);
		menuMgr.add(collapseAllAction);
		menuMgr.add(new Separator());
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuMgr.add(new Separator());
		// menuMgr.add(new PrjPropertyAction(getSite().getWorkbenchWindow()));
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
				viewer.expandAll();
				DevLogger.printDebugMsg("全部折叠已经执行");
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
				IWorkbenchPage page = getViewSite().getWorkbenchWindow()
						.getActivePage();
				if (obj instanceof ResourceLeafNode) {
					try {
						openEditor(page, obj);
					} catch (PartInitException e) {
						e.printStackTrace();
						DevLogger.printError(e);
					}
				}
				if (obj instanceof ResourceNode
						&& !obj.getClass().getSimpleName()
								.equals("ProjectNode")) {
					createTreeLeafs((ResourceNode) obj);
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

	private void openEditor(IWorkbenchPage page, Object obj)
			throws PartInitException {
		IEditorInput editorInput = null;
		ResourceLeafNode node = (ResourceLeafNode) obj;
		String name = node.getParent().getName();
		switch (name) {
		case "数据项":
			editorInput = new DataItemEditorInput((node));
			page.openEditor(editorInput, DataItemEditor.ID, true);
			break;
		case "响应码":
			editorInput = new RetCodeEditorInput(node);
			page.openEditor(editorInput, RetCodeEditor.ID, true);
			break;
		case "动态库":
			editorInput = new AopDllEditorInput(node);
			page.openEditor(editorInput, AopDllEditor.ID, true);
			break;
		case "原子交易":
			editorInput = new AopEditorInput(node);
			page.openEditor(editorInput, AopEditor.ID, true);
			break;
		case "服务程序":
			editorInput = new ServerEditorInput(node);
			page.openEditor(editorInput, ServerEditor.ID, true);
			break;
		case "交易":
			editorInput = new TradeEditorInput(node);
			page.openEditor(editorInput, TradeEditor.ID, true);
			break;
		case "流程图":
			editorInput = new DiagramEditorInput(node);
			page.openEditor(editorInput, DiagramEditor.ID, true);
			break;
		default:
			break;
		}
	}

	public TreeViewer getTreeViewer() {
		return this.viewer;
	}

	private void initData() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject[] projects = prjRoot.getProjects();
		treeRoot = new RootNode("root", "root", null);
		for (IProject iProject : projects) {
			treeRoot.add(new ProjectNode(iProject.getName(),
					iProject.getName(), treeRoot));
		}
	}

	private void createTreeLeafs(ResourceNode obj) {

		String prjId = obj.getParent().getId();
		PreferenceStore ps = CommonUtil.initPs(prjId);
		NavViewDaoServiceImpl navViewDaoServiceImpl = new NavViewDaoServiceImpl();
		String tableName = obj.getName();
		switch (tableName) {
		case "数据项":
			tableName = "DataItem";
			break;
		case "响应码":
			tableName = "RetCode";
			break;
		case "动态库":
			tableName = "AopDll";
			break;
		case "原子交易":
			tableName = "Aop";
			break;
		case "服务程序":
			tableName = "Server";
			break;
		case "交易":
			tableName = "Trade";
			break;
		case "流程图":
			tableName = "T_TFM";
			break;
		default:
		}
		LinkedHashMap<String, String> map;
		try {
			map = navViewDaoServiceImpl.getResourceNodeChild(ps, tableName);
			if (!map.isEmpty()) {
				obj.removeAllChildren();
				for (String key : map.keySet()) {
					ResourceLeafNode rln = new ResourceLeafNode(map.get(key),
							key, obj);
					obj.getChildren().add(rln);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}

	}
}
