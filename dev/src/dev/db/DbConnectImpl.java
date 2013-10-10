package dev.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


public class DbConnectImpl implements IDbConnect {
	private static final String PROPERTIES_PATH = "/dev/db/dbConnect.properties";
	private Connection conn = null;
	private String url = null;
	private String driver = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	// ע������Ϊfalseʱ��������ݿ�Ϊaccess����ô�ٹر�����ʱ���׳��쳣
	private boolean autoCommit = true;

	public DbConnectImpl() {
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	/** ����<code>JDBC</code>������ */
	@Override
	public Connection getDbConnection() {
		return this.conn;
	}

	@Override
	public boolean isDbAutoCommit() {
		return this.autoCommit;
	}

	@Override
	public void openConn() {
/*		Properties properties = ReadProperties.readProperties(PROPERTIES_PATH);
		this.url = properties.getProperty("url");
		this.driver = properties.getProperty("driver");*/
		this.url="jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=d:\\golp.accdb";
		this.driver="sun.jdbc.odbc.JdbcOdbcDriver";
		try {
			Class.forName(driver).newInstance();
			this.conn = DriverManager.getConnection(url);
			this.conn.setAutoCommit(autoCommit);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ر�<code>JDBC</code>����
	 * 
	 * @throws SQLException
	 */
	@Override
	public void closeConn() throws SQLException {
		if (this.conn != null) {
			this.conn.close();
		}
	}

	/**
	 * �����ݿ��ύ����
	 * 
	 * @throws SQLException
	 */
	@Override
	public void commit() throws SQLException {
		try {
			this.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			this.conn.rollback();
		}
	}

	/**
	 * Ϊ���Ӵ���sql��PreparedStatement
	 * 
	 * @throws SQLException
	 */
	@Override
	public void setPrepareSql(String sql) throws SQLException {
		this.pstmt = this.conn.prepareStatement(sql);
	}

	/**
	 * ����PreparedStatement��String���Ͳ���ֵ
	 * 
	 * @throws Exception
	 */
	@Override
	public boolean setPreparedString(int i, String inString)
			throws SQLException {
		if (this.pstmt != null) {
			this.pstmt.setString(i, inString);
			return true;
		}
		return false;
	}

	/**
	 * ����PreparedStatement��int���Ͳ���ֵ
	 * 
	 * @throws Exception
	 * */
	@Override
	public boolean setPreparedInt(int i, int in) throws SQLException {
		if (this.pstmt != null) {
			this.pstmt.setInt(i, in);
			return true;
		}
		return false;
	}

	/**
	 * ����PreparedStatement��double���Ͳ���ֵ
	 * 
	 * @throws Exception
	 * */
	@Override
	public boolean setPreparedDouble(int i, double in) throws SQLException {
		if (this.pstmt != null) {
			this.pstmt.setDouble(i, in);
			return true;
		}
		return false;
	}

	/**
	 * ִ��PreparedStatement����ѯ�����䣬���ز���������
	 * 
	 * @throws SQLException
	 */
	public int executeExceptPreparedQuery() throws SQLException {
		int count = 0;
		count = this.pstmt.executeUpdate();
		return count;
	}

	/**
	 * ִ�г���ѯ�����䣬���ز���������
	 * 
	 * @throws SQLException
	 */
	@Override
	public int executeExceptQuery(String sql) throws SQLException {
		int count = 0;
		this.stmt = this.conn.createStatement();
		count = this.stmt.executeUpdate(sql);
		return count;
	}

	/**
	 * ��ò�ѯ���
	 * 
	 * @throws SQLException
	 */
	@Override
	public ResultSet retrive(String sql) throws SQLException {
		this.stmt = this.conn.createStatement();
		//System.out.println(this.stmt);
		this.rs = this.stmt.executeQuery(sql);
		return this.rs;
	}

	/**
	 * ���PreparedStatement��ѯ���
	 * 
	 * @throws SQLException
	 */
	@Override
	public ResultSet retrivePrepared() throws SQLException {
		this.rs = this.pstmt.executeQuery();
		return this.rs;
	}

	public static void main(String[] args) throws SQLException {
		ResultSet rs = null;
		DbConnectImpl dbConn = DbConnFactory.dbConnCreator();
		dbConn.openConn();
		try {
			rs = dbConn.retrive("select count(*) from test");
			// dbConn.setPrepareSql("select count(*) from server where 1=?");
			// dbConn.setPreparedInt(1, 0);
			// rs=dbConn.retrivePrepared();
			// dbConn.executeExceptQuery("delete from server");
			// rs = dbConn.retrive("select count(*) from server1");
			
			 /*stmt = con.createStatement(); 
			 rs = stmt.executeQuery("select count(*) from server");*/
			 
			if (rs.next()) {
				System.out.println(rs.getInt(1));
			}
			dbConn.executeExceptQuery("insert into test  values('11','aa',null)");
		} catch (SQLException e) {
			//LogUtils.logError(e);
			System.out.println("access err:" + e.getErrorCode());
			System.out.println("access errmsg:" + e.getMessage());
			System.out.println("access sqlstat:" + e.getSQLState());
			e.printStackTrace();
			
			/*String��message��=��"Error��while��initializing��log��properties."��+e.getMessage();
			IStatus��status��=��new��Status(IStatus.ERROR,getDefault().getBundle().getSymbolicName(),IStatus.ERROR,��message,��e);
			getLog().log(status);*/
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dbConn.closeConn();
	}

}
