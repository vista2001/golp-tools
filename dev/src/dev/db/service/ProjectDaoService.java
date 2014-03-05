package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.pojo.TProject;

public interface ProjectDaoService {
	public Map<String, String> queryProject(PreferenceStore ps)
			throws SQLException;

	public void updateProject1(List<String> datalist, PreferenceStore ps)
			throws SQLException;

	public void updateProject2(List<String> datalist, PreferenceStore ps)
			throws SQLException;

	public void insertProject(TProject project) throws SQLException;
}
