/* �ļ�����       EditorDataitemServiceImpl.java
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
import dev.db.pojo.TDataItem;
import dev.util.CommonUtil;
import dev.util.DevLogger;

/**
 * Dataitem�����ݿ������
 * <p>
 * ʵ����EditorDataitemService�ӿڣ�ʵ�ֶ�Dataitem�����ݿ�����ķ�װ
 * 
 * @see#queryDataitemByIdOrName
 * @see#queryProjectId
 * @see#updateDataitemById
 * */
public class EditorDataitemServiceImpl implements EditorDataitemService {

	/**
	 * ���ڶ�Dataitem����в�ѯ����<br>
	 * ����Dataitem�����ID����Name�ҵ�����Ҫ�����ݷ���һ��Map������
	 * 
	 * @param id
	 *            Ҫ��ѯ�����ݵı�ʶ
	 * @param name
	 *            Ҫ��ѯ�����ݵ�����
	 * @param ps
	 *            Ҫ��ѯ�����ݿ��������Ϣ
	 * @return һ��map��������Ų�ѯ�������ݶԣ�KEYֵ�����ݵ�����
	 * */
	public Map<String, String> queryDataitemByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		// �������ݿ��ѯ���
		String sql = "select dataitemid,dataname,datadesc,datalvl,datatype,datalen,dataaop,fmlid, isPublished from dataitem where ";
		// �ж���ͨ�����ƻ��Ǳ�ʶ��ѯ
		if (id.equals("") && name.equals("")) {
			return map;
		}
		if (id.equals("") || name.equals("")) {
			sql += " dataitemid='" + id + "' or dataname='" + name + "'";
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
				map.put("datadesc", rs.getString(3) != null ? rs.getString(3)
						: "");
				map.put("datatype", rs.getString(4) != null ? rs.getString(5)
						: "");
				map.put("datalvl", rs.getString(5) != null ? rs.getString(4)
						: "");
				map.put("datalen", rs.getString(6) != null ? rs.getString(6)
						: "");
				map.put("dataaop", rs.getString(7) != null ? rs.getString(7)
						: "");
				map.put("fmlid", rs.getString(8) != null ? rs.getString(8) : "");
				map.put("isPublished",
						rs.getString(9) != null ? rs.getString(9) : "0");
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
	 * ���ڶ�Dataitem������޸Ĳ���<br>
	 * ����Dataitem�����ID��Ҫ�޸ĵ����ݷ����Ӧ��Dataitem����
	 * 
	 * @param id
	 *            Ҫ�޸ĵ����ݵı�ʶ
	 * @param datalist
	 *            �޸ĺ�����ݼ���
	 * @param ps
	 *            Ҫ�޸ĵ����ݿ��������Ϣ
	 * @return û�з���ֵ
	 * */
	public void updateDataitemById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// �����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		try {
			dbConnectImpl.openConn(ps);
			// �����ݿ����datalist�е�����д�����ݿ�
			dbConnectImpl
					.setPrepareSql("update dataitem set dataname=?,datadesc=?,"
							+ "datatype=?,datalen=?,dataAop=?,fmlid=? where dataitemid='"
							+ id + "'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.setPreparedString(3, datalist.get(2));
			dbConnectImpl.setPreparedString(4, datalist.get(3));
			dbConnectImpl.setPreparedString(5, datalist.get(4));
			dbConnectImpl.setPreparedLong(6, Long.parseLong(datalist.get(5)));
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
	public void insertDataItem(TDataItem dataItem, String prjId)
			throws SQLException {
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		PreferenceStore ps = CommonUtil.initPs(prjId);
		try {
			dbConnectImpl.openConn(ps);
			dbConnectImpl
					.setPrepareSql("insert into dataitem (DATAITEMID, DATANAME, DATADESC,"
							+ "DATALVL, DATATYPE, DATALEN, DATAAOP, FMLID, ISPUBLISHED) values(?,?,?,?,?,?,?,?,?)");
			dbConnectImpl.setPreparedInt(1, dataItem.getDataItemId());
			dbConnectImpl.setPreparedString(2, dataItem.getDataName());
			dbConnectImpl.setPreparedString(3, dataItem.getDataDesc());
			dbConnectImpl.setPreparedString(4, dataItem.getDataLvL());
			dbConnectImpl.setPreparedString(5, dataItem.getDataType());
			dbConnectImpl.setPreparedInt(6, dataItem.getDataLen());
			dbConnectImpl.setPreparedString(7, dataItem.getDataAop());
			dbConnectImpl.setPreparedLong(8, dataItem.getFmlId());
			dbConnectImpl.setPreparedString(9, dataItem.getIsPublished());
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
