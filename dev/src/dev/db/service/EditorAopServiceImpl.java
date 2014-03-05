/* �ļ�����       EditorAopServiceImpl.java
 * �޸��ˣ�       zxh
 * �޸�ʱ�䣺   2013.12.2
 * �޸����ݣ�  1.Ϊ��ȡ���޸����ݿ��������������������������ֶΣ�
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
import dev.db.pojo.TAop;
import dev.util.CommonUtil;
import dev.util.DevLogger;

/**
 * AOP�����ݿ������
 * <p>
 * ʵ����EditorAopService�ӿڣ���Ҫʵ�ֶ�AOP�����ݿ�����ķ�װ
 * 
 * @see#queryAopByIdOrName
 * @see#queryProjectId
 * @see#updateAopById
 * */
public class EditorAopServiceImpl implements EditorAopService {

	/**
	 * ���ڶ�AOP����в�ѯ����<br>
	 * ����AOP�����ID����Name�ҵ�����Ҫ�����ݷ���һ��Map������
	 * 
	 * @param id
	 *            Ҫ��ѯ�����ݵı�ʶ
	 * @param name
	 *            Ҫ��ѯ�����ݵ�����
	 * @param ps
	 *            Ҫ��ѯ�����ݿ��������Ϣ
	 * @return һ��map��������Ų�ѯ�������ݶԣ�KEYֵ�����ݵ�����
	 * */
	public Map<String, String> queryAopByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {

		Map<String, String> map = new HashMap<String, String>();
		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		// �������ݿ��ѯ���
		String sql = "select aoplevel,aoperrrecover,upaopdll,aopid"
				+ ",aopname,aopexts,aopdesc,precondition,postcondition"
				+ ",aopretval,inputdata,outputdata from aop where ";
		// �ж���ͨ�����ƻ��Ǳ�ʶ��ѯ
		if (id.equals("") && name.equals("")) {
			return map;
		}
		if (id.equals("") || name.equals("")) {
			sql += " aopid='" + id + "' or aopname='" + name + "'";
		}
		DevLogger.printDebugMsg(sql);
		try {
			dbConnectImpl.openConn(ps);
			// �����ݿ�������в�ѯ���鵽�Ľ���ŵ�ResyltSet��
			ResultSet rs = dbConnectImpl.retrive(sql);
			// ����õ�����д��map��
			if (rs.next() && rs.getString(1) != null) {
				map.put("aoplevel", rs.getString(1) != null ? rs.getString(1)
						: "");
				map.put("aoperrrecover",
						rs.getString(2) != null ? rs.getString(2) : "");
				map.put("updll", rs.getString(3) != null ? rs.getString(3) : "");
				map.put("ID", rs.getString(4) != null ? rs.getString(4) : "");
				map.put("NAME", rs.getString(5) != null ? rs.getString(5) : "");
				map.put("aopexts", rs.getString(6) != null ? rs.getString(6)
						: "");
				map.put("aopdesc", rs.getString(7) != null ? rs.getString(7)
						: "");
				map.put("precondition",
						rs.getString(8) != null ? rs.getString(8) : "");
				map.put("postcondition",
						rs.getString(9) != null ? rs.getString(9) : "");
				map.put("aopretval",
						rs.getString(10) != null ? rs.getString(10) : "");
				map.put("inputdata",
						rs.getString(11) != null ? rs.getString(11) : "");
				map.put("outputdata",
						rs.getString(12) != null ? rs.getString(12) : "");
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
	 * ���ڶ�AOP������޸Ĳ���<br>
	 * ����AOP�����ID��Ҫ�޸ĵ����ݷ����Ӧ��AOP����
	 * 
	 * @param id
	 *            Ҫ�޸ĵ����ݵı�ʶ
	 * @param datalist
	 *            �޸ĺ�����ݼ���
	 * @param ps
	 *            Ҫ�޸ĵ����ݿ��������Ϣ
	 * @return û�з���ֵ
	 * */
	public void updateAopById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {

		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		try {
			dbConnectImpl.openConn(ps);
			// �����ݿ����datalist�е�����д�����ݿ�
			dbConnectImpl.setPrepareSql("update  AOP set aopname=?,ao"
					+ "pdesc=?,aoperrrecover=?,precondition=?,postcondition"
					+ "=?,aopretval=?,inputdata=?,outputdata=? where aopid='"
					+ id + "'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.setPreparedString(3, datalist.get(2));
			dbConnectImpl.setPreparedString(4, datalist.get(3));
			dbConnectImpl.setPreparedString(5, datalist.get(4));
			dbConnectImpl.setPreparedString(6, datalist.get(5));
			dbConnectImpl.setPreparedString(7, datalist.get(6));
			dbConnectImpl.setPreparedString(8, datalist.get(7));
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
	public void insertAop(TAop aop, String prjId) throws SQLException {
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		PreferenceStore ps = CommonUtil.initPs(prjId);

		try {
			dbConnectImpl.openConn(ps);
			dbConnectImpl
					.setPrepareSql("insert into aop (AOPID, AOPNAME, AOPDESC, INPUTDATA,"
							+ "OUTPUTDATA, PRECONDITION, POSTCONDITION, AOPERRRECOVER, AOPEXTS,"
							+ "UPAOPDLL, AOPLEVEL, AOPRETVAL) values(?,?,?,?,?,?,?,?,null,?,?,?)");
			dbConnectImpl.setPreparedString(1, aop.getAopId());
			dbConnectImpl.setPreparedString(2, aop.getAopName());
			dbConnectImpl.setPreparedString(3, aop.getAopDesc());
			dbConnectImpl.setPreparedString(4, aop.getInputData());
			dbConnectImpl.setPreparedString(5, aop.getOutputData());
			dbConnectImpl.setPreparedString(6, aop.getPreCondition());
			dbConnectImpl.setPreparedString(7, aop.getPostCondition());
			dbConnectImpl.setPreparedString(8, aop.getAopErrRecover());
			dbConnectImpl.setPreparedInt(9, aop.getUpAopDll());
			dbConnectImpl.setPreparedString(10, aop.getAopLevel());
			dbConnectImpl.setPreparedString(11, aop.getAopRetVal());
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
