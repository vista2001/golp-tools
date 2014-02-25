/* 文件名：       EntryFunction.java
 * 描述：           该文件定义了类EntryFunction，该类实现了生成交易的入口函数。
 * 创建人：       zxh
 * 创建时间：   2013.11.27
 * 修改人：       rxy
 * 修改时间：   2013.12.21
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。
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
 * 生成交易入口函数的实现类
 * <p>
 * 根据选择的服务程序生成此服务程序的交易入口函数，一个交易一个交易入口函数 ，函数的名称为"TH"+交易的ID
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

	// 对实例进行初始化
	public EntryFunction(ResourceLeafNode server) throws TemplateException,
			IOException {
		this.server = server;
	}

	/**
	 * 生成交易入口函数
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
	 * 根据模板替换变量生成交易入口函数
	 * 
	 * @throws TemplateException
	 * @throws IOException
	 */
	private void initData() throws TemplateException, IOException {
		// 连接数据库
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

		// 初始化替换变量的map
		root = new HashMap();
		String sql;
		// 从数据库查询替换所需的数据
		if (server.getParent().name.equals("交易")) {
			sql = "select tradeid,trademode,tradeinputdata,tradeoutputdata from "
					+ "trade where tradeid='" + server.id + "'";
		} else {
			sql = "select tradeid,trademode,tradeinputdata,tradeoutputdata from "
					+ "trade where upserverID='" + server.id + "'";
		}
		try {
			dbConnectImpl.openConn(ps);
			rs = dbConnectImpl.retrive(sql);

			// 根据要求将取出的数据按交易转换成要求的格式替换模板，然后写入文件
			while (rs.next() && rs.getString(1) != null) {
				// 如果交易是由流程图驱动
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
				// 如果函数是由函数驱动
				else
					trade = new Trade(rs.getString(1), "THT_FUNC", "TP"
							+ rs.getString(1), rs.getString(3), rs.getString(4));
				// 将套替换的数据放入map中
				root.put("trade", trade);
				// 写出到文件
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
	 * 载入模板
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
