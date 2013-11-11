package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;

public class EditorsDaoServiceImpl implements EditorsDaoService {
	@Override
	public Map<String,String> queryTradeByIdOrName(String id,String name,PreferenceStore ps) throws SQLException{
		Map<String,String> map=new HashMap<String, String>();
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		String sql="select * from trade where ";
		if(id.equals("") && name.equals("")){
			return map;
		}
		if(id.equals("") || name.equals("")){
			sql+=" tradeid='"+id+"' or tradename='"+name+"'";
		}
		if(!id.equals("") && !name.equals("")){
			sql+=" tradeid='"+id+"' and tradename='"+name+"'";
		}
		System.out.println(sql);
		try {
			ResultSet rs = dbConnectImpl.retrive(sql);
			while(rs.next()){
				map.put("tradeLevelText",rs.getString(15) );
				map.put("ID",rs.getString(1));
				map.put("NAME", rs.getString(2));
				map.put("upServerCombo", rs.getString(4));
				map.put("tradeModelCombo", rs.getString(5)!=null?rs.getString(5):"");
				map.put("tradeServerModelCombo", rs.getString(6)!=null?rs.getString(6):"");
				map.put("trancationFlagText", rs.getString(7)!=null?rs.getString(7):"");
				map.put("tradeExtFlagText", rs.getString(13)!=null?rs.getString(13):"");
				map.put("tradeExtNodesText", rs.getString(14)!=null?rs.getString(14):"");
				map.put("inputDataText", rs.getString(8)!=null?rs.getString(8):"");
				map.put("outputDataText", rs.getString(9)!=null?rs.getString(9):"");
				map.put("preconditionText", rs.getString(10)!=null?rs.getString(10):"");
				map.put("postconditionText", rs.getString(11)!=null?rs.getString(11):"");
				map.put("callServiceText", rs.getString(12)!=null?rs.getString(12):"");
				map.put("tradeDescText", rs.getString(3)!=null?rs.getString(3):"");
			}
		}finally{
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();
			}
		}
		return map;
	}

	@Override
	public String queryProjectId(PreferenceStore ps) throws SQLException {
		String prjid="";
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			ResultSet rs = dbConnectImpl.retrive("select prjid from project");
			if(rs.next()){
				prjid=rs.getString(1);
			}
		} finally {
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();
			}
		}
		return prjid;
	}

	@Override
	public void updateTradeById(String id, List<String> datalist,PreferenceStore ps)
			throws SQLException {
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			dbConnectImpl.setPrepareSql("update trade set tradeName=?,upServer=?,tradeModel=?," +
					"tradeServerModel=?,inputData=?,outputData=?," +
					"callServices=?,precondition=?,postcondition=?,tradeDesc=? where tradeid='"+id+"'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.setPreparedString(3, datalist.get(2));
			dbConnectImpl.setPreparedString(4, datalist.get(3));
			dbConnectImpl.setPreparedString(5, datalist.get(4));
			dbConnectImpl.setPreparedString(6, datalist.get(5));
			dbConnectImpl.setPreparedString(7, datalist.get(6));
			dbConnectImpl.setPreparedString(8, datalist.get(7));
			dbConnectImpl.setPreparedString(9, datalist.get(8));
			dbConnectImpl.setPreparedString(10, datalist.get(9));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();
			}
		}
	}

	@Override
	public HashMap<String, String> queryServerByIdOrName(String key, int type,
			PreferenceStore ps) throws SQLException
	{
		// TODO 自动生成的方法存根
		HashMap<String, String> map = new HashMap<String, String>();
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		String sql = "select * from server where";
		//type为0代表根据Id查询，type为1代表根据Name查询
		if (type == 0)
		{
			sql += " serverid='" + key + "'";
		}
		if (type == 1)
		{
			sql += " servername='" + key + "'";
		}
		System.out.println(sql);
		try
		{
			ResultSet rs = dbConnectImpl.retrive(sql);
			if (rs.next() && rs.getString(1) != null)
			{
//				map.put("SERVERID", rs.getString(1));
				map.put("ID", rs.getString(1));
//				map.put("SERVERNAME", rs.getString(2));
				map.put("NAME", rs.getString(2));
				map.put("SERVERDESC", rs.getString(3));
				String SERVERSPECLIBPATH = "";
				String SERVERSPECLIBNAME = "";
				if (rs.getString(4) != null)
				{
					String SERVERSPECLIB = rs.getString(4);
					SERVERSPECLIBPATH = SERVERSPECLIB.substring(1,
							SERVERSPECLIB.indexOf("]"));
					SERVERSPECLIBNAME = SERVERSPECLIB.substring(
							SERVERSPECLIB.lastIndexOf("[") + 1,
							SERVERSPECLIB.length() - 1);
				}
				map.put("SERVERSPECLIBPATH", SERVERSPECLIBPATH);
				map.put("SERVERSPECLIBNAME", SERVERSPECLIBNAME);
				map.put("SERVERSPECAOPDLLS",
						rs.getString(5) != null ? rs.getString(5) : "");
				map.put("CALLBACKSOURCE",
						rs.getString(6) != null ? rs.getString(6) : "");
				map.put("OTHFUNSOURCE",
						rs.getString(7) != null ? rs.getString(7) : "");
				map.put("SERVERLEVEL", rs.getString(8));
			}
			else
			{
				map = null;
			}
		} finally
		{
			if (dbConnectImpl != null)
			{
				dbConnectImpl.closeConn();
			}
		}
		return map;
	}

	@Override
	public void updateServerById(List<String> datalist,
			PreferenceStore ps) throws SQLException
	{
		// TODO 自动生成的方法存根
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try
		{
			dbConnectImpl
					.setPrepareSql("update SERVER set SERVERNAME=?,SERVERDESC=?,SERVERSPECLIB=?,SERVERSPECAOPDLLS=?,CALLBACKSOURCE=?,OTHFUNSROURCE=? where SERVERID=?");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.setPreparedString(3, datalist.get(2));
			dbConnectImpl.setPreparedString(4, datalist.get(3));
			dbConnectImpl.setPreparedString(5, datalist.get(4));
			dbConnectImpl.setPreparedString(6, datalist.get(5));
			dbConnectImpl.setPreparedString(7, datalist.get(6));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally
		{
			if (dbConnectImpl != null)
			{
				dbConnectImpl.closeConn();
			}
		}

	}
}
