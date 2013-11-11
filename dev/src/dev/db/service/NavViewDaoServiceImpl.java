package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.model.base.ResourceLeafNode;

public class NavViewDaoServiceImpl implements NavViewDaoService {

	@Override
	public LinkedHashMap<String,String> getResourceNodeChild(PreferenceStore ps,
			String tableName) {
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		String sql="select * from "+tableName+" order by "+tableName+"id";
		try {
			ResultSet rs = dbConnectImpl.retrive(sql);
			while(rs.next()){
				map.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

}
