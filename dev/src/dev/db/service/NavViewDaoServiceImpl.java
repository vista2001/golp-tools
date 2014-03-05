/* �ļ�����       NavViewDaoServiceImpl.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.9
 * �޸����ݣ�   ��Ϊ���ݿ�������ͼ�ı�����T_TFM�����ñ��У�����ͼ��ID��������TFMID�����Բ����������·�����
 *         sql="select * from "+tableName+" order by "+tableName+"id"��������
 *         �˶Բ�ѯ����ͼ�����⴦��
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
