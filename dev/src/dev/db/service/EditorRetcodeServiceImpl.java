/* �ļ�����       EditorAopDllServiceImpl.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
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
 * Retcode�����ݿ������
 * <p>
 * ʵ����EditorRetcodeService�ӿڣ���Ҫʵ�ֶ�Retcode�����ݿ�����ķ�װ
 * 
 * @see#queryRetcodeByIdOrName
 * @see#queryProjectId
 * @see#updateRetcodeById
 * */
public class EditorRetcodeServiceImpl implements EditorRetcodeService {

	/**
	 * ���ڶ�Retcode����в�ѯ����<br>
	 * ����Retcode�����ID����Name�ҵ�����Ҫ�����ݷ���һ��Map������
	 * 
	 * @param id
	 *            Ҫ��ѯ�����ݵı�ʶ
	 * @param name
	 *            Ҫ��ѯ�����ݵ�����
	 * @param ps
	 *            Ҫ��ѯ�����ݿ��������Ϣ
	 * @return һ��map��������Ų�ѯ�������ݶԣ�KEYֵ�����ݵ�����
	 * */
	public Map<String, String> queryRetcodeByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		// �������ݿ��ѯ���
		String sql = "select retcodeid,retcodevalue,retcodedesc,retcodelevel from retcode where ";
		// �ж���ͨ�����ƻ��Ǳ�ʶ��ѯ
		if (id.equals("") && name.equals("")) {
			return map;
		}
		if (id.equals("") || name.equals("")) {
			sql += " retcodeid='" + id + "' or retcodevalue='" + name + "'";
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
				}// �Ͽ����ݿ�����
			}
		}
		return map;// ����map
	}

	/**
	 * ���ڶ�Retcode������޸Ĳ���<br>
	 * ����Retcode�����ID��Ҫ�޸ĵ����ݷ����Ӧ��Retcode����
	 * 
	 * @param id
	 *            Ҫ�޸ĵ����ݵı�ʶ
	 * @param datalist
	 *            �޸ĺ�����ݼ���
	 * @param ps
	 *            Ҫ�޸ĵ����ݿ��������Ϣ
	 * @return û�з���ֵ
	 * */
	public void updateRetcodeById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		try {
			dbConnectImpl.openConn(ps);
			// �����ݿ����datalist�е�����д�����ݿ�
			dbConnectImpl
					.setPrepareSql("update  retcode set retcodevalue=?,retcod"
							+ "edesc=?where retcodeID='" + id + "'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if (dbConnectImpl != null) {
				try {
					// �ر����ݿ�����
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
