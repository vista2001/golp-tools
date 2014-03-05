/* �ļ�����       DeleteHandler.java
 * ������           ���ļ���������DeleteHandler������ʵ����ɾ����ť��Handler��
 * �����ˣ�       zxh
 * ����ʱ�䣺   2013.11.29
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.21
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������
 */

package dev.commands;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.TreeNode;
import dev.model.resource.AopNodes;
import dev.model.resource.DataItemNodes;
import dev.model.resource.DllNodes;
import dev.model.resource.RetCodeNodes;
import dev.model.resource.ServerNodes;
import dev.model.resource.TFMNodes;
import dev.model.resource.TradeNodes;
import dev.util.DevLogger;
import dev.views.NavView;

public class DeleteHandler extends AbstractHandler {

	PreferenceStore ps;
	DbConnectImpl dbConnectImpl;
	TreeViewer tv;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		List<ResourceLeafNode> nodes = ((IStructuredSelection) selection)
				.toList();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(nodes.get(0).getRootProject()
				.getId());
		String dbfiles = project.getLocationURI().toString().substring(6) + '/'
				+ nodes.get(0).getRootProject().getId() + ".properties";
		DevLogger.printDebugMsg("dbfiles===" + dbfiles);
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		}
		dbConnectImpl = DbConnFactory.dbConnCreator();
		try {
			dbConnectImpl.openConn(ps);
		} catch (SQLException e1) {
			DevLogger.printError(e1);
			e1.printStackTrace();
		}

		IViewPart view = window.getActivePage().findView(NavView.ID);
		NavView v = (NavView) view;
		tv = v.getTreeViewer();
		if (nodes.get(0).parent instanceof AopNodes) {
			for (int i = 0; i < nodes.size(); i++)
				deleteAop(nodes.get(i));
			tv.refresh();
		} else if (nodes.get(0).parent instanceof DllNodes) {
			for (int i = 0; i < nodes.size(); i++)
				deleteAopDll(nodes.get(i));
			tv.refresh();
		} else if (nodes.get(0).parent instanceof ServerNodes) {
			try {
				for (int i = 0; i < nodes.size(); i++)
					deleteServer(nodes.get(i));
			} catch (SQLException e) {

				e.printStackTrace();
				DevLogger.printError(e);
			}
			tv.refresh();
		} else if (nodes.get(0).parent instanceof TradeNodes) {
			try {
				for (int i = 0; i < nodes.size(); i++)
					deleteTrade(nodes.get(i));
			} catch (SQLException e) {

				e.printStackTrace();
				DevLogger.printError(e);
			}
			tv.refresh();
		} else if (nodes.get(0).parent instanceof RetCodeNodes) {
			for (int i = 0; i < nodes.size(); i++)
				deleteRetCode(nodes.get(i));
			tv.refresh();
		} else if (nodes.get(0).parent instanceof DataItemNodes) {
			try {
				for (int i = 0; i < nodes.size(); i++)
					deleteDateitem(nodes.get(i));
			} catch (SQLException e) {

				e.printStackTrace();
				DevLogger.printError(e);
			}
			tv.refresh();

		} else if (nodes.get(0).parent instanceof TFMNodes) {
			try {
				for (int i = 0; i < nodes.size(); i++)
					deleteTFM(nodes.get(i));
			} catch (SQLException e) {

				e.printStackTrace();
				DevLogger.printError(e);
			}
			tv.refresh();
		}
		return null;
	}

	public void deleteAop(ResourceLeafNode aop) {
		try {
			String sql = "delete from aop where aopid='" + aop.id + "'";
			removeChild(aop);
			tv.remove(aop);
			dbConnectImpl.retrive(sql);
		} catch (SQLException e) {

			e.printStackTrace();
			DevLogger.printError(e);

		}
		closeEditor(aop);
	}

	public void deleteRetCode(ResourceLeafNode retcode) {
		String sql = "delete from retcode where retcodeid='" + retcode.id + "'";
		removeChild(retcode);
		tv.remove(retcode);
		try {
			dbConnectImpl.retrive(sql);
		} catch (SQLException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		}
		closeEditor(retcode);
	}

	public void deleteAopDll(ResourceLeafNode aopDll) {
		String sql = "select aopid from aop where upaopdll='" + aopDll.id + "'";
		try {
			ResultSet rs = dbConnectImpl.retrive(sql);
			if (rs.next() && rs.getString(1) != null) {
				String aops = "";
				do {
					aops += "          " + rs.getString(1) + "\n";
				} while (rs.next());
				DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.YES, "ɾ��", "�˶�̬��"
						+ aopDll.id + "��������ԭ�ӽ���:\n" + aops
						+ "ɾ����̬��֮ǰ�����޸�����ԭ�ӽ���������̬��");
				return;
			}
			/*
			 * List<TreeNode> tem=aopDll.getParent().getParent().children;
			 * for(i=0;i<tem.size();i++){
			 * if(tem.get(i).getName().equals("ԭ�ӽ���")) break; }
			 * tem=tem.get(i).children; for(String node:trades){
			 * for(i=0;i<tem.size();i++) { if(tem.get(i).equals(node))
			 * deleteAop((ResourceLeafNode)tem.get(i)); } }
			 */

			sql = "delete from aopdll where aopdllid='" + aopDll.id + "'";
			try {
				dbConnectImpl.retrive(sql);
			} catch (SQLException e) {

				e.printStackTrace();
				DevLogger.printError(e);
			}
			removeChild(aopDll);
			tv.remove(aopDll);
		} catch (SQLException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		}
		closeEditor(aopDll);
	}

	public void deleteDateitem(ResourceLeafNode dataitem) throws SQLException {
		String sql = "select inputdata,outputdata from aop";
		ResultSet rs = dbConnectImpl.retrive(sql);
		List<String> dataitems = new ArrayList<String>();
		while (rs.next() && rs.getString(1) != null) {
			dataitems.add(rs.getString(1));
			dataitems.add(rs.getString(2));
		}
		sql = "select tradeinputdata, tradeoutputdata from trade";
		rs = dbConnectImpl.retrive(sql);
		while (rs.next() && rs.getString(1) != null) {
			dataitems.add(rs.getString(1));
			dataitems.add(rs.getString(2));
		}
		for (int i = 0; i < dataitems.size(); i++) {
			String[] tems = dataitems.get(i).split("\\|");
			for (String tem : tems) {
				if (tem.split("@")[0].equals(dataitem.id)) {
					DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.YES, "ɾ��", "������"
							+ dataitem.id + "���ڱ�ʹ�ã��޷�ɾ��");
					return;
				}
			}
		}

		sql = "delete from dataitem where dataitemid='" + dataitem.id + "'";
		removeChild(dataitem);
		tv.remove(dataitem);
		try {
			dbConnectImpl.retrive(sql);
		} catch (SQLException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		}
		closeEditor(dataitem);
	}

	public void deleteTrade(ResourceLeafNode trade) throws SQLException {
		String sql = "select trademode from trade where tradeid='" + trade.id
				+ "'";
		ResultSet rs = dbConnectImpl.retrive(sql);
		if (rs.next() && rs.getString(1).equals("0")) {
			int result = DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.YES | SWT.NO,
					"ɾ��", "����" + trade.id + "������ͼ������ɾ���˽��׽���ͬ�󶨵�����ͼһ��ɾ��");
			if (result == SWT.NO)
				return;
			else if (result == SWT.YES) {
				sql = "select tfmid from t_tfm where tradeid='" + trade.id
						+ "'";
				rs = dbConnectImpl.retrive(sql);
				if (!(rs.next() && rs.getString(1) != null))
					return;
				List<TreeNode> nodes = trade.getParent().getParent()
						.getChildren();
				List<TreeNode> tfmNodes = null;
				for (int i = 0; i < nodes.size(); i++) {
					if (nodes.get(i).name.equals("����ͼ")) {
						tfmNodes = nodes.get(i).getChildren();
						break;
					}
				}
				for (int i = 0; i < tfmNodes.size(); i++) {
					if (tfmNodes.get(i).getId().equals(rs.getString(1))) {
						deleteTFM((ResourceLeafNode) tfmNodes.get(i));
						break;
					}
				}
			}
		}

		sql = "delete from trade where tradeid='" + trade.id + "'";
		dbConnectImpl.retrive(sql);
		removeChild(trade);
		tv.remove(trade);
		closeEditor(trade);
	}

	public void deleteServer(ResourceLeafNode server) throws SQLException {
		String sql = "select tradeid from trade where upserverid='" + server.id
				+ "'";
		ResultSet rs = dbConnectImpl.retrive(sql);
		String trades = "";
		while (rs.next() && rs.getString(1) != null) {
			trades += rs.getString(1) + "\n";
		}
		if (trades.length() != 0) {
			DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.YES, "ɾ��", "�������"
					+ server.id + "�Ѿ����������½���:\n" + trades
					+ "ɾ������֮ǰ�����޸��������׵������������");
			return;
		}

		sql = "delete from server where serverid='" + server.id + "'";
		dbConnectImpl.retrive(sql);
		removeChild(server);
		tv.remove(trades);
		closeEditor(server);
	}

	public void deleteTFM(ResourceLeafNode tfm) throws SQLException {
		String sql = "delete from t_tfm where tfmid='" + tfm.id + "'";
		dbConnectImpl.retrive(sql);
		removeChild(tfm);
		tv.remove(tfm);
		closeEditor(tfm);
	}

	public void removeChild(ResourceLeafNode node) {
		List<TreeNode> children = node.parent.getChildren();

		children.remove(node);
	}

	public void closeEditor(ResourceLeafNode node) {
		// if(PlatformUI.getWorkbench().getActiveWorkbenchWindow().
		// getActivePage().getActiveEditor().getEditorInput().getName().equals(node.id)){
		// IEditorPart
		// editor=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor,
		// false);
		// }
		// else
		if (find(node) != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().closeEditor(find(node), false);
		}
	}

	public IEditorPart find(ResourceLeafNode node) {
		IEditorReference[] editors = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();
		for (IEditorReference editor : editors)
			try {
				if (editor.getEditorInput().getName().equals(node.id))
					return editor.getEditor(false);
			} catch (PartInitException e) {

				e.printStackTrace();
				DevLogger.printError(e);
			}
		return null;
	}
}
