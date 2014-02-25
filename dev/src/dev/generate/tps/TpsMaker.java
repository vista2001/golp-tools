/* 文件名：       TpsMaker.java
 * 描述：           该文件定义了类TpsMaker，该类实现了生成tps.mk和tps.LST。
 * 创建人：       zxh
 * 创建时间：   2013.11.27
 * 修改人：       rxy
 * 修改时间：   2013.12.21
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。
 */

package dev.generate.tps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
 * 生成tps.mk和tps.LST的实现类
 * <p>
 * 一个服务程序生成一个tps.mk和一个tps.LST
 * 
 * @see initData
 * @see generate
 * @see init
 */
public class TpsMaker {
	public Template tpsmk;
	public Template tpslst;
	public ResourceLeafNode server;
	public Map root;
	public ResultSet rs;

	// 构造函数
	public TpsMaker(ResourceLeafNode server) throws TemplateException,
			IOException, SQLException {
		this.server = server;

	}

	/**
	 * 载入模板并生成tps.mk和tps.LST
	 */
	public void generate() throws TemplateException, IOException, SQLException {
		init();
		initData();
	}

	/**
	 * 根据模板替换变量生成tps.mk和tps.LST
	 */
	private void initData() throws TemplateException, IOException, SQLException {
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
		dbConnectImpl.openConn(ps);
		// 对替换模板的map进行初始化
		root = new HashMap();
		// 查询需要的数据
		List<String> VPATH = new ArrayList<String>();
		List<String> trades = new ArrayList<String>();
		String sql = "select tradeid from trade where upserverid='" + server.id
				+ "'";
		rs = dbConnectImpl.retrive(sql);
		List<String> objs = new ArrayList<String>();
		while (rs.next() && rs.getString(1) != null) {
			objs.add("TP" + rs.getString(1));
			trades.add(rs.getString(1));
		}

		sql = "select callbacksource,othfunsource from server where serverid='"
				+ server.id + "'";
		rs = dbConnectImpl.retrive(sql);
		while (rs.next() && rs.getString(1) != null) {
			String[] callbacksource = rs.getString(1).split("\\|");
			for (String str : callbacksource) {
				// if(!objs.contains(getName(str)))
				objs.add(getName(str));
				// if(!VPATH.contains(getPath(str)))
				VPATH.add(getPath(str));
			}
			String[] otherfunsource = rs.getString(2).split("\\|");
			for (String str : otherfunsource) {
				objs.add(getName(str));
				// if(!VPATH.contains(getPath(str)))
				VPATH.add(getPath(str));
			}
		}

		List<String> tradeObjs = new ArrayList<String>();
		sql = "select tradesrcpath from trade where upserverid='" + server.id
				+ "'" + "and trademode='1'";
		rs = dbConnectImpl.retrive(sql);
		while(rs.next() && rs.getString(1) != null) {
			String[] tradeObj = rs.getString(1).split("\\|");
			for (String str : tradeObj) {
				tradeObjs.add(getName(str));
				VPATH.add(getPath(str));
			}
		}

		List<String> specObjs = new ArrayList<String>();
		sql = "select ServerSpecObj from server where serverid='" + server.id
				+ "'";
		rs = dbConnectImpl.retrive(sql);
		if (rs.next() && rs.getString(1) != null) {
			String[] specObj = rs.getString(1).split("\\|");
			for (String str : specObj) {
				specObjs.add(getName(str));
			}
		}

		List<String> specInclude = new ArrayList<String>();
		List<String> SpecMarco = new ArrayList<String>();
		sql = "select ServerSpecInclude,ServerSpecMarco from server where serverid='"
				+ server.id + "'";
		rs = dbConnectImpl.retrive(sql);
		if (rs.next() && rs.getString(1) != null) {
			String[] include = rs.getString(1).split("\\|");
			String[] marco = rs.getString(2).split("\\|");
			for (String str : include) {
				specInclude.add(str);
			}
			for (String str : marco) {
				String[] tem = str.split("\"");
				if (tem.length == 2)
					SpecMarco.add(tem[0] + "\\\"" + tem[1] + "\\\"");
				else
					SpecMarco.add(str);
			}
		}

		List<String> spathes = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		sql = "select SERVERSPECLIB from server where serverid='" + server.id
				+ "'";
		rs = dbConnectImpl.retrive(sql);
		if (rs.next() && rs.getString(1) != null) {
			String[] lib = rs.getString(1).split("\\]\\[");
			DebugOut.println(lib[0]);
			String[] spath = lib[0].substring(1).split("\\|");
			for (String str : spath) {
				spathes.add(str);
			}
			String[] name = lib[1].substring(0, lib[1].length() - 2).split(
					"\\|");
			for (String str : name) {
				names.add(str);
			}
		}
		dbConnectImpl.closeConn();
		// 将查到的数据放入map中
		removeDuplicate(objs);
		removeDuplicate(tradeObjs);
		removeDuplicate(specObjs);
		removeDuplicate(VPATH);
		removeDuplicate(specInclude);
		removeDuplicate(SpecMarco);
		removeDuplicate(spathes);
		removeDuplicate(names);
		removeDuplicate(trades);
		System.out.println(VPATH);
		root.put("SAMP", server.name);
		root.put("OBJS", objs);
		root.put("tradeObjs", tradeObjs);
		root.put("specObjs", specObjs);
		root.put("pathes", VPATH);
		root.put("hpathes", specInclude);
		root.put("Mpathes", SpecMarco);
		root.put("spathes", spathes);
		root.put("names", names);
		root.put("trades", trades);

		// 将根据模板生成的数据写入文件
		String path = FmlId.getPath();
		File filePath = new File(path + "/" + server.getRootProject().getId()
				+ "/appHome//build/AGsvr_" + server.getId() + "/");
		filePath.mkdirs();
		File mkOutFile = new File(path + "/" + server.getRootProject().getId()
				+ "/appHome/build/AGsvr_" + server.getId() + "/" + "/tps.mk");
		File lstOutFile = new File(path + "/" + server.getRootProject().getId()
				+ "/appHome/build/AGsvr_" + server.getId() + "/" + "/tps.LST");
		Writer out = null;
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				mkOutFile), "UTF-8"));
		tpsmk.process(root, out);
		out.close();
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				lstOutFile), "UTF-8"));
		tpslst.process(root, out);
		out.close();
	}

	/**
	 * 载入模板
	 */
	private void init() {
		try {
			Configuration tempConfiguration = new Configuration();
			tempConfiguration.setClassicCompatible(true);
			tempConfiguration.setClassForTemplateLoading(this.getClass(),
					"/dev/generate/tps");
			tempConfiguration.setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			tempConfiguration.setNumberFormat("");
			// tempConfiguration.setDefaultEncoding("utf-8");
			tpsmk = tempConfiguration.getTemplate("/tpsmk.mdl");
			tpslst = tempConfiguration.getTemplate("/tpsLST.mdl");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 将绝对路径中的文件名取出来
	 * 
	 * @param str
	 *            路径名
	 * @return
	 */
	private String getName(String str) {
		String temStr = str;
		temStr = temStr.substring(temStr.lastIndexOf("/") + 1);
		temStr = temStr.substring(0, temStr.lastIndexOf("."));
		return temStr;
	}

	/**
	 * 将绝对路径中的文件夹路径取出
	 * 
	 * @param str
	 * @return
	 */
	private String getPath(String str) {
		String temStr = str;
		temStr = temStr.substring(0, temStr.lastIndexOf("/"));

		return temStr;
	}

	private void removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
	}
}
