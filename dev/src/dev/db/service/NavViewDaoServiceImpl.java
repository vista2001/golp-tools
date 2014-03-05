/* 文件名：       NavViewDaoServiceImpl.java
 * 修改人：       rxy
 * 修改时间：   2013.12.9
 * 修改内容：   因为数据库中流程图的表名是T_TFM，而该表中，流程图的ID的列名是TFMID，所以不适用于以下方法：
 *         sql="select * from "+tableName+" order by "+tableName+"id"，故增加
 *         了对查询流程图的特殊处理。
 */

package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.util.DevLogger;

public class NavViewDaoServiceImpl implements NavViewDaoService {

	@Override
	public LinkedHashMap<String, String> getResourceNodeChild(
			PreferenceStore ps, String tableName) throws SQLException {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		String sql = "select * from " + tableName + " order by " + tableName
				+ "id";
		if (tableName.equals("T_TFM")) {
			sql = "select * from t_tfm order by tfmid";
		}
		try {
			dbConnectImpl.openConn(ps);
			ResultSet rs = dbConnectImpl.retrive(sql);
			while (rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
			}
		} finally {
			try {
				if (dbConnectImpl != null) {
					dbConnectImpl.closeConn();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				DevLogger.printError(e);
			}
		}
		return map;
	}

}
