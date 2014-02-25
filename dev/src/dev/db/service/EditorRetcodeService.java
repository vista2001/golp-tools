package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.pojo.TRetCode;
/**
 * Retcode�༭�����ݿ�����ӿ�
 * <p>�ӿ��ﶨ��������������<br>
 * 1.queryRetcodeByIdOrName���������ݿ�Retcode����в�ѯ����<br>
 * 2.queryProjectId���������ݿ�Project����в�ѯ����<br>
 * 3.updateRetcodeById���������ݿ�Retcode������޸Ĳ���<br>
 * */
public interface EditorRetcodeService {
	public Map<String,String> queryRetcodeByIdOrName(String id,String name,PreferenceStore ps) throws SQLException;
	public void updateRetcodeById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
	public void insertRetCode(TRetCode retCode, String prjId) throws SQLException;
}



