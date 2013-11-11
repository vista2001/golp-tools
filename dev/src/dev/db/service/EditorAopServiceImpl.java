package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
/**
 * AOP�����ݿ������
 * <p>ʵ����EditorAopService�ӿڣ���Ҫʵ�ֶ�AOP�����ݿ�����ķ�װ
 * @see#queryAopByIdOrName
 * @see#queryProjectId
 * @see#updateAopById
 * */
public class EditorAopServiceImpl implements EditorAopService {

	/**
	 * ���ڶ�AOP����в�ѯ����<br>
	 * ����AOP�����ID����Name�ҵ�����Ҫ�����ݷ���һ��Map������
	 * @param id     Ҫ��ѯ�����ݵı�ʶ
	 * @param name   Ҫ��ѯ�����ݵ�����
	 * @param ps     Ҫ��ѯ�����ݿ��������Ϣ
	 * @return һ��map��������Ų�ѯ�������ݶԣ�KEYֵ�����ݵ�����
	 * */
	public Map<String, String> queryAopByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String, String>();
		//�����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		//�������ݿ��ѯ���
		String sql="select aoplevel,aoperrrecover,updll,aopid" +
					",aopname,aopexts,aopdesc,precondition,postcondition from aop where ";
		//�ж���ͨ�����ƻ��Ǳ�ʶ��ѯ
		if(id.equals("") && name.equals("")){
			return map;
		}
		if(id.equals("") || name.equals("")){
			sql+=" aopid='"+id+"' or aopname='"+name+"'";
		}
		System.out.println(sql);
		try {
			//�����ݿ�������в�ѯ���鵽�Ľ���ŵ�ResyltSet��
			ResultSet rs = dbConnectImpl.retrive(sql);
			//����õ�����д��map��
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
			}
			else
				map=null;
		}finally{
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();//�Ͽ����ݿ�����
			}
		}
		return map;//����map
	}

	/**
	 * ���ڶ�Project����в�ѯ��������������̵ı�ʶ<br>
	 * �������ݿ��������Ϣ�ҵ����ݿ��Project���ӱ����ҵ����̵ı�ʶ������
	 * @param ps   Ҫ��ѯ�����ݵ�������Ϣ
	 * @return һ��string����������Ź��̵ı�ʶ
	 * */
	public String queryProjectId(PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		String prjid="";//�Դ洢���̱�ʶ���ַ�����ʼ��
		//�����ݿ�����
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			//��ѯProject���ù��̵ı�ʶ
			ResultSet rs = dbConnectImpl.retrive("select prjid from project");
			if(rs.next()){
				prjid=rs.getString(1);//����ʶ�����ַ���
			}
		} finally {
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();//�ر����ݿ�����
			}
		}
		return prjid;//���ع��̱�ʶ
	}

	/**
	 * ���ڶ�AOP������޸Ĳ���<br>
	 * ����AOP�����ID��Ҫ�޸ĵ����ݷ����Ӧ��AOP����
	 * @param id         Ҫ�޸ĵ����ݵı�ʶ
	 * @param datalist   �޸ĺ�����ݼ���
	 * @param ps         Ҫ�޸ĵ����ݿ��������Ϣ
	 * @return û�з���ֵ
	 * */
	public void updateAopById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		//�����ݿ�����
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			//�����ݿ����datalist�е�����д�����ݿ�
			dbConnectImpl.setPrepareSql("update  AOP set aopname=?,aopdesc=?,aoperrrecover=?,precondition=?,postcondition=? where aopid='"+id+"'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.setPreparedString(3, datalist.get(2));
			dbConnectImpl.setPreparedString(4, datalist.get(3));
			dbConnectImpl.setPreparedString(5, datalist.get(4));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();//�ر����ݿ�����
			}
		}
	}

}
