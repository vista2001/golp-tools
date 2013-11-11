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
 * AopDll�����ݿ������
 * <p>ʵ����EditorAopDllService�ӿڣ���Ҫʵ�ֶ�AopDll�����ݿ�����ķ�װ
 * @see#queryAopDllByIdOrName
 * @see#queryProjectId
 * @see#updateAopDllById
 * */
public class EditorAopDllServiceImpl implements EditorsAopDllService {

	/**
	 * ���ڶ�AopDll����в�ѯ����<br>
	 * ����AopDll�����ID����Name�ҵ�����Ҫ�����ݷ���һ��Map������
	 * @param id     Ҫ��ѯ�����ݵı�ʶ
	 * @param name   Ҫ��ѯ�����ݵ�����
	 * @param ps     Ҫ��ѯ�����ݿ��������Ϣ
	 * @return һ��map��������Ų�ѯ�������ݶԣ�KEYֵ�����ݵ�����
	 * */
	public Map<String, String> queryAopDllByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String, String>();
		//�����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		//�������ݿ��ѯ���
		String sql="select aopdllid,dllname,dlldesc,dlltype,dlllevel from Aopdll where ";
		//�ж���ͨ�����ƻ��Ǳ�ʶ��ѯ
		if(id.equals("") && name.equals("")){
			return map;
		}
		if(id.equals("") || name.equals("")){
			sql+=" aopdllid='"+id+"' or dllname='"+name+"'";
		}
		System.out.println(sql);
		try {
			//�����ݿ�������в�ѯ���鵽�Ľ���ŵ�ResyltSet��
			ResultSet rs = dbConnectImpl.retrive(sql);
			//����õ�����д��map��
			if(rs.next()&&rs.getString(1)!=null){
				map.put("ID",rs.getString(1) !=null?rs.getString(1):"");
				map.put("NAME",rs.getString(2)!=null?rs.getString(2):"");
				map.put("dlldesc", rs.getString(3)!=null?rs.getString(3):"");
				map.put("dlltype", rs.getString(4)!=null?rs.getString(4):"");
				map.put("dlllevel", rs.getString(5)!=null?rs.getString(5):"");
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
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		//�����ݿ�����
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
	 * ���ڶ�AopDll������޸Ĳ���<br>
	 * ����AopDll�����ID��Ҫ�޸ĵ����ݷ����Ӧ��AopDll����
	 * @param id         Ҫ�޸ĵ����ݵı�ʶ
	 * @param datalist   �޸ĺ�����ݼ���
	 * @param ps         Ҫ�޸ĵ����ݿ��������Ϣ
	 * @return û�з���ֵ
	 * */
	public void updateAopDllById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		//�����ݿ�����
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			//�����ݿ����datalist�е�����д�����ݿ�
			dbConnectImpl.setPrepareSql("update  AOPDLL set dllname=?,dlldesc=? where dllid='"+id+"'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();//�ر����ݿ�����
			}
		}
	}
}

