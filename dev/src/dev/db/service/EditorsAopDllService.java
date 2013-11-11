package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;
/**
 * AopDll编辑器数据库操作接口
 * <p>接口里定义了三个方法：<br>
 * 1.queryAopDllByIdOrName方法对数据库AopDll表进行查询操作<br>
 * 2.queryProjectId方法对数据库Project表进行查询操作<br>
 * 3.updateAopDllById方法对数据库AopDll表进行修改操作<br>
 **/
public interface EditorsAopDllService {
	public Map<String,String> queryAopDllByIdOrName(String id,String name,PreferenceStore ps) throws SQLException; 
	public String queryProjectId(PreferenceStore ps) throws SQLException;
	public void updateAopDllById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
}
