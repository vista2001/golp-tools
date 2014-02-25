package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.pojo.TDataItem;
/**
 * Dataitem�༭�����ݿ�����ӿ�
 * <p>�ӿ��ﶨ��������������<br>
 * 1.queryDataitemByIdOrName���������ݿ�Dataitem����в�ѯ����<br>
 * 2.queryProjectId���������ݿ�Project����в�ѯ����<br>
 * 3.updateDataitemById���������ݿ�Dataitem������޸Ĳ���<br>
 * */
public interface EditorDataitemService {
	public Map<String,String> queryDataitemByIdOrName(String id,String name,PreferenceStore ps) throws SQLException;
	public void updateDataitemById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
	public void insertDataItem(TDataItem dataItem, String prjId) throws SQLException;
}
