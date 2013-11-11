package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;
/**
 * AOP�༭�����ݿ�����ӿ�
 * <p>�ӿ��ﶨ��������������<br>
 * 1.queryAopByIdOrName���������ݿ�AOP����в�ѯ����<br>
 * 2.queryProjectId���������ݿ�Project����в�ѯ����<br>
 * 3.updateAopById���������ݿ�AOP������޸Ĳ���<br>
 * */
public interface EditorAopService {
	public Map<String,String> queryAopByIdOrName(String id,String name,PreferenceStore ps) throws SQLException;
	public String queryProjectId(PreferenceStore ps) throws SQLException;
	public void updateAopById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
}
