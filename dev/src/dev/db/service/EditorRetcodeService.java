package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;
/**
 * Retcode编辑器数据库操作接口
 * <p>接口里定义了三个方法：<br>
 * 1.queryRetcodeByIdOrName方法对数据库Retcode表进行查询操作<br>
 * 2.queryProjectId方法对数据库Project表进行查询操作<br>
 * 3.updateRetcodeById方法对数据库Retcode表进行修改操作<br>
 * */
public interface EditorRetcodeService {
	public Map<String,String> queryRetcodeByIdOrName(String id,String name,PreferenceStore ps) throws SQLException;
	public String queryProjectId(PreferenceStore ps) throws SQLException;
	public void updateRetcodeById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
}



