/* 文件名：       EditorAopDllServiceImpl.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。 
 */

package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.db.pojo.TRetCode;
import dev.util.CommonUtil;
import dev.util.DevLogger;

/**
 * Retcode表数据库操作类
 * <p>
 * 实现了EditorRetcodeService接口，主要实现对Retcode表数据库操作的封装
 * 
 * @see#queryRetcodeByIdOrName
 * @see#queryProjectId
 * @see#updateRetcodeById
 * */
public class EditorRetcodeServiceImpl implements EditorRetcodeService {

	/**
	 * 用于对Retcode表进行查询操作<br>
	 * 根据Retcode表项的ID或者Name找到所需要的数据放入一个Map变量中
	 * 
	 * @param id
	 *            要查询的数据的标识
	 * @param name
	 *            要查询的数据的名称
	 * @param ps
	 *            要查询的数据库的配置信息
	 * @return 一个map变量，存放查询到的数据对，KEY值是数据的列名
	 * */
	public Map<String, String> queryRetcodeByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		// 打开数据库连接
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		// 设置数据库查询语句
		String sql = "select retcodeid,retcodevalue,retcodedesc,retcodelevel from retcode where ";
		// 判断是通过名称还是标识查询
		if (id.equals("") && name.equals("")) {
			return map;
		}
		if (id.equals("") || name.equals("")) {
			sql += " retcodeid='" + id + "' or retcodevalue='" + name + "'";
		}
		DevLogger.printDebugMsg(sql);
		try {
			dbConnectImpl.openConn(ps);
			// 用数据库命令进行查询，查到的结果放到ResyltSet里
			ResultSet rs = dbConnectImpl.retrive(sql);
			// 将获得的数据写到map里
			if (rs.next() && rs.getString(1) != null) {
				map.put("ID", rs.getString(1) != null ? rs.getString(1) : "");
				map.put("NAME", rs.getString(2) != null ? rs.getString(2) : "");
				map.put("retcodedesc",
						rs.getString(3) != null ? rs.getString(3) : "");
				map.put("retcodelevel",
						rs.getString(4) != null ? rs.getString(4) : "");
			} else
				map = null;
		} finally {
			if (dbConnectImpl != null) {
				try {
					dbConnectImpl.closeConn();
				} catch (SQLException e) {

					e.printStackTrace();
					DevLogger.printError(e);
				}// 断开数据库连接
			}
		}
		return map;// 返回map
	}

	/**
	 * 用于对Retcode表进行修改操作<br>
	 * 根据Retcode表项的ID将要修改的数据放入对应的Retcode表中
	 * 
	 * @param id
	 *            要修改的数据的标识
	 * @param datalist
	 *            修改后的数据集合
	 * @param ps
	 *            要修改的数据库的配置信息
	 * @return 没有返回值
	 * */
	public void updateRetcodeById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// 打开数据库连接
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		try {
			dbConnectImpl.openConn(ps);
			// 用数据库命令将datalist中的数据写入数据库
			dbConnectImpl
					.setPrepareSql("update  retcode set retcodevalue=?,retcod"
							+ "edesc=?where retcodeID='" + id + "'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if (dbConnectImpl != null) {
				try {
					// 关闭数据库连接
					dbConnectImpl.closeConn();
				} catch (SQLException e) {

					e.printStackTrace();
					DevLogger.printError(e);
				}
			}
		}
	}

	@Override
	public void insertRetCode(TRetCode retCode, String prjId)
			throws SQLException {
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		PreferenceStore ps = CommonUtil.initPs(prjId);

		try {
			dbConnectImpl.openConn(ps);
			dbConnectImpl
					.setPrepareSql("insert into retCode (RETCODEID, RETCODEVALUE, RETCODEDESC,"
							+ "RETCODELEVEL) values(?,?,?,?)");
			dbConnectImpl.setPreparedString(1, retCode.getRetCodeId());
			dbConnectImpl.setPreparedString(2, retCode.getRetCodeValue());
			dbConnectImpl.setPreparedString(3, retCode.getRetCodeDesc());
			dbConnectImpl.setPreparedString(4, retCode.getRetCodeLevel());
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if (dbConnectImpl != null) {
				try {
					dbConnectImpl.closeConn();
				} catch (SQLException e) {

					e.printStackTrace();
					DevLogger.printError(e);
				}
			}
		}
	}
}
