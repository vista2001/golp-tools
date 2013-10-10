package dev.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDbConnect {
	/** ����<code>JDBC</code>������ */
	public Connection getDbConnection();

	public boolean isDbAutoCommit();

	public void openConn();

	/**
	 * �ر�<code>JDBC</code>����
	 * 
	 * @throws SQLException
	 */
	public void closeConn() throws SQLException;

	/**
	 * �����ݿ��ύ����
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException;

	/**
	 * Ϊ���Ӵ���sql��PreparedStatement
	 * 
	 * @throws SQLException
	 */
	public void setPrepareSql(String sql) throws SQLException;

	/**
	 * ����PreparedStatement��String���Ͳ���ֵ
	 * @return 
	 * 
	 * @throws Exception
	 */
	public boolean setPreparedString(int i, String inString) throws SQLException;

	/**
	 * ����PreparedStatement��int���Ͳ���ֵ
	 * 
	 * @throws Exception
	 * */
	public boolean setPreparedInt(int i, int in) throws SQLException;

	/**
	 * ����PreparedStatement��double���Ͳ���ֵ
	 * 
	 * @throws Exception
	 * */
	public boolean setPreparedDouble(int i, double in) throws SQLException;

	/**
	 * ִ��PreparedStatement����ѯ�����䣬���ز���������
	 * 
	 * @throws SQLException
	 */
	public int executeExceptPreparedQuery() throws SQLException;

	/**
	 * ִ�г���ѯ�����䣬���ز���������
	 * 
	 * @throws SQLException
	 */
	public int executeExceptQuery(String sql) throws SQLException;

	/**
	 * ��ò�ѯ���
	 * 
	 * @throws SQLException
	 */
	public ResultSet retrive(String sql) throws SQLException;

	/**
	 * ���PreparedStatement��ѯ���
	 * 
	 * @throws SQLException
	 */
	public ResultSet retrivePrepared() throws SQLException;
}
