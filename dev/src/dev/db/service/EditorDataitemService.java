package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.pojo.TDataItem;
/**
 * Dataitem编辑器数据库操作接口
 * <p>接口里定义了三个方法：<br>
 * 1.queryDataitemByIdOrName方法对数据库Dataitem表进行查询操作<br>
 * 2.queryProjectId方法对数据库Project表进行查询操作<br>
 * 3.updateDataitemById方法对数据库Dataitem表进行修改操作<br>
 * */
public interface EditorDataitemService {
	public Map<String,String> queryDataitemByIdOrName(String id,String name,PreferenceStore ps) throws SQLException;
	public void updateDataitemById(String id,List<String> datalist,PreferenceStore ps) throws SQLException;
	public void insertDataItem(TDataItem dataItem, String prjId) throws SQLException;
}
