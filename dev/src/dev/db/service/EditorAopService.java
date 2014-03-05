package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.pojo.TAop;

/**
 * AOP编辑器数据库操作接口
 * <p>
 * 接口里定义了三个方法：<br>
 * 1.queryAopByIdOrName方法对数据库AOP表进行查询操作<br>
 * 2.queryProjectId方法对数据库Project表进行查询操作<br>
 * 3.updateAopById方法对数据库AOP表进行修改操作<br>
 * */
public interface EditorAopService {
	public Map<String, String> queryAopByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException;

	public void updateAopById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException;

	public void insertAop(TAop aop, String prjId) throws SQLException;
}
