/* �ļ�����       EditorAopDllServiceImpl.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   1.�޸ı��ļ��е�sql��ѯ����е�dllidΪaopdllid��
 *         2.��DebugOut.println�����滻System.out.println������ 
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
import dev.db.pojo.TAopDll;
import dev.util.CommonUtil;
import dev.util.DevLogger;

/**
 * AopDll�����ݿ������
 * <p>
 * ʵ����EditorAopDllService�ӿڣ���Ҫʵ�ֶ�AopDll�����ݿ�����ķ�װ
 * 
 * @see#queryAopDllByIdOrName
 * @see#queryProjectId
 * @see#updateAopDllById
 * */
public class EditorAopDllServiceImpl implements EditorsAopDllService {

	/**
	 * ���ڶ�AopDll����в�ѯ����<br>
	 * ����AopDll�����ID����Name�ҵ�����Ҫ�����ݷ���һ��Map������
	 * 
	 * @param id
	 *            Ҫ��ѯ�����ݵı�ʶ
	 * @param name
	 *            Ҫ��ѯ�����ݵ�����
	 * @param ps
	 *            Ҫ��ѯ�����ݿ��������Ϣ
	 * @return һ��map��������Ų�ѯ�������ݶԣ�KEYֵ�����ݵ�����
	 * */
	public Map<String, String> queryAopDllByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		// �������ݿ��ѯ���
		String sql = "select aopdllid,aopdllname,aopdlldesc,aopdlltype,aopdlllevel,aopdllpath from Aopdll where ";
		// �ж���ͨ�����ƻ��Ǳ�ʶ��ѯ
		if (id.equals("") && name.equals("")) {
			return map;
		}
		if (id.equals("") || name.equals("")) {
			sql += " aopdllid='" + id + "' or aopdllname='" + name + "'";
		}
		DevLogger.printDebugMsg(sql);
		try {
			dbConnectImpl.openConn(ps);
			// �����ݿ�������в�ѯ���鵽�Ľ���ŵ�ResyltSet��
			ResultSet rs = dbConnectImpl.retrive(sql);
			// ����õ�����д��map��
			if (rs.next() && rs.getString(1) != null) {
				map.put("ID", rs.getString(1) != null ? rs.getString(1) : "");
				map.put("NAME", rs.getString(2) != null ? rs.getString(2) : "");
				map.put("dlldesc", rs.getString(3) != null ? rs.getString(3)
						: "");
				map.put("dlltype", rs.getString(4) != null ? rs.getString(4)
						: "");
				map.put("dlllevel", rs.getString(5) != null ? rs.getString(5)
						: "");
				map.put("dllpath", rs.getString(6) != null ? rs.getString(6)
						: "");
			} else
				map = null;
		} finally {
			if (dbConnectImpl != null) {
				try {
					// �Ͽ����ݿ�����
					dbConnectImpl.closeConn();
				} catch (SQLException e) {

					e.printStackTrace();
					DevLogger.printError(e);
				}
			}
		}
		return map;// ����map
	}

	/**
	 * ���ڶ�AopDll������޸Ĳ���<br>
	 * ����AopDll�����ID��Ҫ�޸ĵ����ݷ����Ӧ��AopDll����
	 * 
	 * @param id
	 *            Ҫ�޸ĵ����ݵı�ʶ
	 * @param datalist
	 *            �޸ĺ�����ݼ���
	 * @param ps
	 *            Ҫ�޸ĵ����ݿ��������Ϣ
	 * @return û�з���ֵ
	 * */
	public void updateAopDllById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		try {
			dbConnectImpl.openConn(ps);
			// �����ݿ����datalist�е�����д�����ݿ�
			dbConnectImpl
					.setPrepareSql("update  AOPDLL set aopdllname=?, aopdlldesc=?, aopdllpath=? where aopdllid='"
							+ id + "'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.setPreparedString(3, datalist.get(2));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if (dbConnectImpl != null) {
				try {
					dbConnectImpl.closeConn();
				} catch (SQLException e) {

					e.printStackTrace();
					DevLogger.printError(e);
				}// �ر����ݿ�����
			}
		}
	}

	@Override
	public void insertAopDll(TAopDll aopDll, String prjId) throws SQLException {
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		PreferenceStore ps = CommonUtil.initPs(prjId);
		try {
			dbConnectImpl.openConn(ps);
			dbConnectImpl
					.setPrepareSql("insert into aopdll (AOPDLLID, AOPDLLNAME, AOPDLLDESC,"
							+ "AOPDLLTYPE, AOPDLLLEVEL, AOPDLLPATH) values(?,?,?,?,?,?)");
			dbConnectImpl.setPreparedInt(1, aopDll.getAopDllId());
			dbConnectImpl.setPreparedString(2, aopDll.getAopDllName());
			dbConnectImpl.setPreparedString(3, aopDll.getAopDllDesc());
			dbConnectImpl.setPreparedString(4, aopDll.getAopDllType());
			dbConnectImpl.setPreparedString(5, aopDll.getAopDllLevel());
			dbConnectImpl.setPreparedString(6, aopDll.getAopDllPath());
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
