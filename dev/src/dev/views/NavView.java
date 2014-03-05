/* �ļ�����       NavView.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.6
 * �޸����ݣ�   1.�޸�createTreeLeafs���������Ӷ�����ͼ�Ĵ���
 *         2.�޸�createPartControl�������������´�������
 *         viewer�����getTree�������õ�����tree�������tree
 *         �������mouseDown���¼����������ڴ˼�������ʵ���м���
 *         ������ͼ��NavView����
 *         3.�޸����б༭���Ĵ򿪷�ʽ����ԭ���Ĳ������Ϊ���
 *         4.��DebugOut.println�����滻System.out.println������
 *         5.����˫�����̽ڵ�ʱ��������⣻
 *         6.���Ƶ��������е������ڵ�ʱ���Ѵ򿪱༭�����л���
 *         7.ͳһʹ��File.separator��
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
					// ��tree��������ʱ������NavView��
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
					case "�������":
						editorInput = new ServerEditorInput(resourceLeafNode);
						break;
					case "����":
						editorInput = new TradeEditorInput(resourceLeafNode);
						break;
					case "��̬��":
						editorInput = new AopDllEditorInput(resourceLeafNode);
						break;
					case "ԭ�ӽ���":
						editorInput = new AopEditorInput(resourceLeafNode);
						break;
					case "������":
						editorInput = new DataItemEditorInput(resourceLeafNode);
						break;
					case "��Ӧ��":
						editorInput = new RetCodeEditorInput(resourceLeafNode);
						break;
					case "����ͼ":
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
		expandAllAction.setText("ȫ���۵�");
		expandAllAction.setToolTipText("ȫ���۵�");
		expandAllAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/collapse.gif"));

		collapseAllAction = new Action() {
			public void run() {
				viewer.expandAll();
				DevLogger.printDebugMsg("ȫ���۵��Ѿ�ִ��");
			}
		};
		collapseAllAction.setText("ȫ��չ��");
		collapseAllAction.setToolTipText("ȫ��չ��");
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
		case "������":
			editorInput = new DataItemEditorInput((node));
			page.openEditor(editorInput, DataItemEditor.ID, true);
			break;
		case "��Ӧ��":
			editorInput = new RetCodeEditorInput(node);
			page.openEditor(editorInput, RetCodeEditor.ID, true);
			break;
		case "��̬��":
			editorInput = new AopDllEditorInput(node);
			page.openEditor(editorInput, AopDllEditor.ID, true);
			break;
		case "ԭ�ӽ���":
			editorInput = new AopEditorInput(node);
			page.openEditor(editorInput, AopEditor.ID, true);
			break;
		case "�������":
			editorInput = new ServerEditorInput(node);
			page.openEditor(editorInput, ServerEditor.ID, true);
			break;
		case "����":
			editorInput = new TradeEditorInput(node);
			page.openEditor(editorInput, TradeEditor.ID, true);
			break;
		case "����ͼ":
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
		case "������":
			tableName = "DataItem";
			break;
		case "��Ӧ��":
			tableName = "RetCode";
			break;
		case "��̬��":
			tableName = "AopDll";
			break;
		case "ԭ�ӽ���":
			tableName = "Aop";
			break;
		case "�������":
			tableName = "Server";
			break;
		case "����":
			tableName = "Trade";
			break;
		case "����ͼ":
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
