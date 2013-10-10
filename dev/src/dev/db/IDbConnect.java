package dev.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDbConnect {
	/** 创建<code>JDBC</code>的链接 */
	public Connection getDbConnection();

	public boolean isDbAutoCommit();

	public void openConn();

	/**
	 * 关闭<code>JDBC</code>链接
	 * 
	 * @throws SQLException
	 */
	public void closeConn() throws SQLException;

	/**
	 * 向数据库提交操作
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException;

	/**
	 * 为链接创建sql的PreparedStatement
	 * 
	 * @throws SQLException
	 */
	public void setPrepareSql(String sql) throws SQLException;

	/**
	 * 设置PreparedStatement的String类型参数值
	 * @return 
	 * 
	 * @throws Exception
	 */
	public boolean setPreparedString(int i, String inString) throws SQLException;

	/**
	 * 设置PreparedStatement的int类型参数值
	 * 
	 * @throws Exception
	 * */
	public boolean setPreparedInt(int i, int in) throws SQLException;

	/**
	 * 设置PreparedStatement的double类型参数值
	 * 
	 * @throws Exception
	 * */
	public boolean setPreparedDouble(int i, double in) throws SQLException;

	/**
	 * 执行PreparedStatement除查询外的语句，返回操作的行数
	 * 
	 * @throws SQLException
	 */
	public int executeExceptPreparedQuery() throws SQLException;

	/**
	 * 执行除查询外的语句，返回操作的行数
	 * 
	 * @throws SQLException
	 */
	public int executeExceptQuery(String sql) throws SQLException;

	/**
	 * 获得查询结果
	 * 
	 * @throws SQLException
	 */
	public ResultSet retrive(String sql) throws SQLException;

	/**
	 * 获得PreparedStatement查询结果
	 * 
	 * @throws SQLException
	 */
	public ResultSet retrivePrepared() throws SQLException;
}
