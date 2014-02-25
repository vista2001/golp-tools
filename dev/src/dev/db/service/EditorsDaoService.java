package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

public interface EditorsDaoService {
	//Trade相关部分
	public Map<String,String> queryTradeByIdOrName(String id,String name,PreferenceStore ps) throws SQLException;
	public String queryProjectId(PreferenceStore ps) throws SQLException;
	public void updateTradeById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
	public void insertTrade(List<String> datalist, String prjId) throws SQLException;
}

