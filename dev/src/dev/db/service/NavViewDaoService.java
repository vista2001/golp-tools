package dev.db.service;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.eclipse.jface.preference.PreferenceStore;

public interface NavViewDaoService {
	public LinkedHashMap<String, String> getResourceNodeChild(
			PreferenceStore ps, String tableName) throws SQLException;

}
