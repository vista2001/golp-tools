package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
/**
 * AOP表数据库操作类
 * <p>实现了EditorAopService接口，主要实现对AOP表数据库操作的封装
 * @see#queryAopByIdOrName
 * @see#queryProjectId
 * @see#updateAopById
 * */
public class EditorAopServiceImpl implements EditorAopService {

	/**
	 * 用于对AOP表进行查询操作<br>
	 * 根据AOP表项的ID或者Name找到所需要的数据放入一个Map变量中
	 * @param id     要查询的数据的标识
	 * @param name   要查询的数据的名称
	 * @param ps     要查询的数据库的配置信息
	 * @return 一个map变量，存放查询到的数据对，KEY值是数据的列名
	 * */
	public Map<String, String> queryAopByIdOrName(String id, String name,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String, String>();
		//打开数据库连接
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		//设置数据库查询语句
		String sql="select aoplevel,aoperrrecover,updll,aopid" +
					",aopname,aopexts,aopdesc,precondition,postcondition from aop where ";
		//判断是通过名称还是标识查询
		if(id.equals("") && name.equals("")){
			return map;
		}
		if(id.equals("") || name.equals("")){
			sql+=" aopid='"+id+"' or aopname='"+name+"'";
		}
		System.out.println(sql);
		try {
			//用数据库命令进行查询，查到的结果放到ResyltSet里
			ResultSet rs = dbConnectImpl.retrive(sql);
			//将获得的数据写到map里
			if (rs.next() && rs.getString(1) != null) {
				map.put("aoplevel", rs.getString(1) != null ? rs.getString(1)
						: "");
				map.put("aoperrrecover",
						rs.getString(2) != null ? rs.getString(2) : "");
				map.put("updll", rs.getString(3) != null ? rs.getString(3) : "");
				map.put("ID", rs.getString(4) != null ? rs.getString(4) : "");
				map.put("NAME", rs.getString(5) != null ? rs.getString(5) : "");
				map.put("aopexts", rs.getString(6) != null ? rs.getString(6)
						: "");
				map.put("aopdesc", rs.getString(7) != null ? rs.getString(7)
						: "");
				map.put("precondition",
						rs.getString(8) != null ? rs.getString(8) : "");
				map.put("postcondition",
						rs.getString(9) != null ? rs.getString(9) : "");
			}
			else
				map=null;
		}finally{
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();//断开数据库连接
			}
		}
		return map;//返回map
	}

	/**
	 * 用于对Project表进行查询操作获得所属工程的标识<br>
	 * 根据数据库的配置信息找到数据库的Project表，从表里找到工程的标识并返回
	 * @param ps   要查询的数据的配置信息
	 * @return 一个string变量，存放着工程的标识
	 * */
	public String queryProjectId(PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		String prjid="";//对存储工程标识的字符串初始化
		//打开数据库连接
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			//查询Project表获得工程的标识
			ResultSet rs = dbConnectImpl.retrive("select prjid from project");
			if(rs.next()){
				prjid=rs.getString(1);//将标识放入字符串
			}
		} finally {
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();//关闭数据库连接
			}
		}
		return prjid;//返回工程标识
	}

	/**
	 * 用于对AOP表进行修改操作<br>
	 * 根据AOP表项的ID将要修改的数据放入对应的AOP表中
	 * @param id         要修改的数据的标识
	 * @param datalist   修改后的数据集合
	 * @param ps         要修改的数据库的配置信息
	 * @return 没有返回值
	 * */
	public void updateAopById(String id, List<String> datalist,
			PreferenceStore ps) throws SQLException {
		// TODO Auto-generated method stub
		//打开数据库连接
		DbConnectImpl dbConnectImpl=DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			//用数据库命令将datalist中的数据写入数据库
			dbConnectImpl.setPrepareSql("update  AOP set aopname=?,aopdesc=?,aoperrrecover=?,precondition=?,postcondition=? where aopid='"+id+"'");
			dbConnectImpl.setPreparedString(1, datalist.get(0));
			dbConnectImpl.setPreparedString(2, datalist.get(1));
			dbConnectImpl.setPreparedString(3, datalist.get(2));
			dbConnectImpl.setPreparedString(4, datalist.get(3));
			dbConnectImpl.setPreparedString(5, datalist.get(4));
			dbConnectImpl.executeExceptPreparedQuery();
		} finally {
			if(dbConnectImpl!=null){
				dbConnectImpl.closeConn();//关闭数据库连接
			}
		}
	}

}
