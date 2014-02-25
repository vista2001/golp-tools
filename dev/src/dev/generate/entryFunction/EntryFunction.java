/* �ļ�����       EntryFunction.java
 * ������           ���ļ���������EntryFunction������ʵ�������ɽ��׵���ں�����
 * �����ˣ�       zxh
 * ����ʱ�䣺   2013.11.27
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.21
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������
 */

package dev.generate.entryFunction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.generate.fml.FmlId;
import dev.model.base.ResourceLeafNode;
import dev.util.DebugOut;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * ���ɽ�����ں�����ʵ����
 * <p>
 * ����ѡ��ķ���������ɴ˷������Ľ�����ں�����һ������һ��������ں��� ������������Ϊ"TH"+���׵�ID
 * 
 * @see init
 * @see initData
 * @see generate
 * @author zxh
 * 
 */
public class EntryFunction {

	Template testfile;
	ResourceLeafNode server;
	Map root;
	ResultSet rs;
	Trade trade;

	// ��ʵ�����г�ʼ��
	public EntryFunction(ResourceLeafNode server) throws TemplateException,
			IOException {
		this.server = server;
	}

	/**
	 * ���ɽ�����ں���
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 * @throws NoTfmException
	 */
	public void generate() throws TemplateException, IOException {
		init();
		initData();
	}

	/**
	 * ����ģ���滻�������ɽ�����ں���
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 */
	private void initData() throws TemplateException, IOException {
		// �������ݿ�
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(server.getRootProject().getId());
		String dbfiles = project.getLocationURI().toString().substring(6) + '/'
				+ server.getRootProject().getId() + ".properties";
		DebugOut.println("dbfiles===" + dbfiles);
		PreferenceStore ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		// ��ʼ���滻������map
		root = new HashMap();
		String sql;
		// �����ݿ��ѯ�滻���������
		if (server.getParent().name.equals("����")) {
			sql = "select tradeid,trademode,tradeinputdata,tradeoutputdata from "
					+ "trade where tradeid='" + server.id + "'";
		} else {
			sql = "select tradeid,trademode,tradeinputdata,tradeoutputdata from "
					+ "trade where upserverID='" + server.id + "'";
		}
		try {
			dbConnectImpl.openConn(ps);
			rs = dbConnectImpl.retrive(sql);

			// ����Ҫ��ȡ�������ݰ�����ת����Ҫ��ĸ�ʽ�滻ģ�壬Ȼ��д���ļ�
			while (rs.next() && rs.getString(1) != null) {
				// ���������������ͼ����
				if (rs.getString(2).equals("0")) {
					sql = "select tfmid from t_tfm where tradeid='"
							+ rs.getString(1) + "'";
					dbConnectImpl.openConn(ps);
					ResultSet result = dbConnectImpl.retrive(sql);
					if (result.next() && result.getString(1) != null)
						trade = new Trade(rs.getString(1), "THT_FLOWCHART",
								result.getString(1), rs.getString(3),
								rs.getString(4));
					else
						throw new IOException();
				}
				// ����������ɺ�������
				else
					trade = new Trade(rs.getString(1), "THT_FUNC", "TP"
							+ rs.getString(1), rs.getString(3), rs.getString(4));
				// �����滻�����ݷ���map��
				root.put("trade", trade);
				// д�����ļ�
				String path = FmlId.getPath();
				File filePath = new File(path +"/"+server.getRootProject().getId()+ "/appHome/build/AGservices/");
				File outFile = new File(path +"/"+server.getRootProject().getId()+ "/appHome/build/AGservices/TH"
						+ trade.tradeID + ".cpp");
				filePath.mkdirs();
				Writer out = null;
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outFile), "UTF-8"));
				testfile.process(root, out);
				out.close();
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * ����ģ��
	 */
	private void init() {
		try {
			Configuration tempConfiguration = new Configuration();
			tempConfiguration.setClassicCompatible(true);
			tempConfiguration.setClassForTemplateLoading(this.getClass(),
					"/dev/generate/EntryFunction");
			tempConfiguration.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			tempConfiguration.setNumberFormat("");
			// tempConfiguration.setDefaultEncoding("utf-8");
			testfile = tempConfiguration.getTemplate("/svcHandler.mdl");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
