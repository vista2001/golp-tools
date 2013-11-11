package dev.db.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jface.preference.PreferenceStore;

import dev.model.base.ResourceLeafNode;

public interface NavViewDaoService {
	public LinkedHashMap<String,String> getResourceNodeChild(PreferenceStore ps,String tableName);
	
}
