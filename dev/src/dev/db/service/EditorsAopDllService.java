package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;
/**
 * AopDll�༭�����ݿ�����ӿ�
 * <p>�ӿ��ﶨ��������������<br>
 * 1.queryAopDllByIdOrName���������ݿ�AopDll����в�ѯ����<br>
 * 2.queryProjectId���������ݿ�Project����в�ѯ����<br>
 * 3.updateAopDllById���������ݿ�AopDll������޸Ĳ���<br>
 **/
public interface EditorsAopDllService {
	public Map<String,String> queryAopDllByIdOrName(String id,String name,PreferenceStore ps) throws SQLException; 
	public String queryProjectId(PreferenceStore ps) throws SQLException;
	public void updateAopDllById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
}
