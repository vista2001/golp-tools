package dev.commands;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.generate.tps.TpsMaker;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ServerNodes;
import dev.util.DevLogger;
import dev.views.NavView;
import freemarker.template.TemplateException;

public class MakefileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {

		List<TreeNode> obj;

		NavView view = (NavView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		List<TreeNode> project = ((RootNode) view.getTreeViewer().getInput())
				.getChildren();
		boolean hasServer = false;
		for (int j = 0; j < project.size(); j++) {
			List<TreeNode> types = project.get(j).getChildren();
			for (int i = 0; i < types.size(); i++) {
				if (types.get(i) instanceof ServerNodes) {
					LinkedHashMap<String, String> map = getServerFromDatabase((ServerNodes) types
							.get(i));
					types.get(i).removeAllChildren();
					if (!map.isEmpty()) {
						for (String key : map.keySet()) {
							ResourceLeafNode node = new ResourceLeafNode(
									map.get(key), key, types.get(i));
							types.get(i).getChildren().add(node);
						}
						view.getTreeViewer().refresh();
						hasServer = true;
					}
				}
			}
		}
		if (!hasServer) {
			DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.OK, "�Ҳ����������", "��û�д����������\n�봴�������������ִ�д˲���");
			return null;
		}
		MakefileDialog dialog = new MakefileDialog(Display.getCurrent()
				.getActiveShell());
		dialog.open();
		obj = dialog.getSelected();
		if (obj.size() == 0)
			return obj;
		String information = "";
		int count = 0;
		try {
			for (count = 0; count < obj.size(); count++) {

				// �½�����tps.mk��tps.LST���ʵ������ʼ��������tps.mk��tps.LST
				TpsMaker tps = new TpsMaker((ResourceLeafNode) obj.get(count));
				tps.generate();
				information += obj.get(count).id + "\n";
			}
		} catch (TemplateException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		} catch (IOException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		} catch (SQLException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		} finally {
			MessageBox box = new MessageBox(Display.getCurrent()
					.getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
			if (count == obj.size()) {
				box.setText("�ɹ�");
				information = "����makefile�����ɹ�";
			} else {
				box.setText("ʧ��");
				information = "���ɵ�" + count
						+ "makefile������������,���������Ƿ���Ϲ涨\n�ɹ����ɣ�\n" + information
						+ "\nʧ��" + (obj.size() - count) + "��";
			}
			box.setMessage(information);
			box.open();
			;
		}
		return obj;
	}

	public LinkedHashMap<String, String> getServerFromDatabase(ServerNodes obj) {
		String prjId = obj.getParent().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(prjId);
		String propertyPath = project.getLocationURI().toString().substring(6)
				+ '/' + prjId + ".properties";
		PreferenceStore ps = new PreferenceStore(propertyPath);
		try {
			ps.load();
		} catch (IOException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		}
		DbConnectImpl impl = DbConnFactory.dbConnCreator();
		ResultSet rs = null;
		try {
			impl.openConn(ps);
			rs = impl
					.retrive("select servername,serverid from server order by serverid");
		} catch (SQLException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		}
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		try {
			while (rs.next() && rs.getString(1).length() != 0) {
				map.put(rs.getString(2), rs.getString(1));
			}
		} catch (SQLException e) {

			e.printStackTrace();
			DevLogger.printError(e);
		}
		return map;
	}
}
