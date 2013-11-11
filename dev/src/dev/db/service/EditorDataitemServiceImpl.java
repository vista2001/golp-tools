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
 * Dataitem�����ݿ������
 * <p>ʵ����EditorDataitemService�ӿڣ�ʵ�ֶ�Dataitem�����ݿ�����ķ�װ
 * @see#queryDataitemByIdOrName
 * @see#queryProjectId
 * @see#updateDataitemById
 * */
public class EditorDataitemServiceImpl implements EditorDataitemService {

	/**
	 * ���ڶ�Dataitem����в�ѯ����<br>
	 * ����Dataitem�����ID����Name�ҵ�����Ҫ�����ݷ���һ��Map������
	 * @param id     Ҫ��ѯ�����ݵı�ʶ
	 * @param name   Ҫ��ѯ�����ݵ�����
	 * @param ps     Ҫ��ѯ�����ݿ��������Ϣ
	 * @return һ��map��������Ų�ѯ�������ݶԣ�KEYֵ�����ݵ�����
	 * */
	public Map<String, String> queryDataitemByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String, String>();
		//�����ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		//�������ݿ��ѯ���
		String sql="select dataitemid,dataname,datadesc,datalvl,datatype,datalen,dataaop,fmlid from dataitem where ";
		//�ж���ͨ�����ƻ��Ǳ�ʶ��ѯ
		if(id.equals("") && name.equals("")){
			return map;
		}
		if(id.equals("") || name.equals("")){
			sql+=" dataitemid='"+id+"' or dataname='"+name+"'";
		}
		System.out.println(sql);
		try {
			//�����ݿ�������в�ѯ���鵽�Ľ���ŵ�ResyltSet��
			ResultSet rs = dbConnectImpl.retrive(sql);
			//����õ�����д��map��
			if(rs.next()&&rs.getString(1)!=null){
				map.put("id",rs.getString(1)!=null?rs.getString(1):"" );
				map.put("name",rs.getString(2)!=null?rs.getString(2):"");
				map.put("datadesc", rs.getString(3)!=null?rs.getString(3):"");
				map.put("datatype", rs.getString(4)!=null?rs.getString(5):"");
				map.put("datalvl", rs.getString(5)!=null?rs.getString(4):"");
				map.put("datalen", rs.getString(6)!=null?rs.getString(6):"");
				map.put("dataaop", rs.getString(7)!=null?rs.getString(7):"");
				map.put("fmlid", rs.getString(8)!=null?rs.getString(8):"");
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
	 * ���ڶ�Dataitem������޸Ĳ���<br>
	 * ����Dataitem�����ID��Ҫ�޸ĵ����ݷ����Ӧ��Dataitem����
	 * @param id         Ҫ�޸ĵ����ݵı�ʶ
	 * @param datalist   �޸ĺ�����ݼ���
	 * @param ps         Ҫ�޸ĵ����ݿ��������Ϣ
	 * @return û�з���ֵ
	 * */
	public void updateDataitemById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		//�����ݿ�����
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			//�����ݿ����datalist�е�����д�����ݿ�
			dbConnectImpl.setPrepareSql("update  dataitem set dataname=?,datadesc=?," +
							          		"datatype=?,datalen=?,dataAop=? where dataitemid='"+id+"'");
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
