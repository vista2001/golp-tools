/* �ļ�����       DbConnectImpl.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.27
 * �޸����ݣ�    1.��main����ע�͵���
 *         2.��DebugOut.println�����滻System.out.println������
 *         3.���Ӿ�̬����initPs��
 *         4.��openConn(PreferenceStore ps)�����У�ȡ����dbType�Ĵ���
 *         5.�޸�retrive����������this.conn.createStatement����ʱ�������
 *         ResultSet.TYPE_SCROLL_INSENSITIVE��ResultSet.CONCUR_READ_ONLY��
 */

package dev.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jface.preference.PreferenceStore;

public class DbConnectImpl implements IDbConnect
{
    private static final String PROPERTIES_PATH = "/dev/db/dbConnect.properties";
    private static final String ORACLC = "jdbc:oracle:thin:@";
    private static final String ORACLC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private Connection conn = null;
    private String url = null;
    private String driver = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    // ע������Ϊfalseʱ��������ݿ�Ϊaccess����ô�ٹر�����ʱ���׳��쳣
    private boolean autoCommit = true;
    private String username;
    private String password;

    public DbConnectImpl()
    {
    }

    public void setAutoCommit(boolean autoCommit)
    {
        this.autoCommit = autoCommit;
    }

    /** ����<code>JDBC</code>������ */
    @Override
    public Connection getDbConnection()
    {
        return this.conn;
    }

    @Override
    public boolean isDbAutoCommit()
    {
        return this.autoCommit;
    }

    @Override
    public void openConn() throws SQLException
    {
        /*
         * Properties properties =
         * ReadProperties.readProperties(PROPERTIES_PATH); this.url =
         * properties.getProperty("url"); this.driver =
         * properties.getProperty("driver"); this.url=
         * "jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=d:\\golp.accdb"
         * ; this.driver="sun.jdbc.odbc.JdbcOdbcDriver"; try {
         * Class.forName(driver).newInstance(); this.conn =
         * DriverManager.getConnection(url);
         * this.conn.setAutoCommit(autoCommit);
         */
        try
        {
            this.url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
            this.driver = "oracle.jdbc.driver.OracleDriver";
            this.username = "golp";
            this.password = "golp";
            Class.forName(driver).newInstance();
            this.conn = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void openConn(PreferenceStore ps) throws SQLException
    {

        try
        {
            // ps.load();
            String dbAddress = ps.getString("dbAddress");
            String dbInstance = ps.getString("dbInstance");
            String dbUser = ps.getString("dbUser");
            String dbPwd = ps.getString("dbPwd");
            // String dbType=ps.getString("dbType");
            String dbPort = ps.getString("dbPort");
            // if(dbType.equals("oracle")){
            // this.url = ORACLC+dbAddress+":"+dbPort+":"+dbInstance;
            // DebugOut.println(url);
            // }
            this.url = ORACLC + dbAddress + ":" + dbPort + ":" + dbInstance;
            this.driver = ORACLC_DRIVER;
            this.username = dbUser;
            this.password = dbPwd;
            Class.forName(driver).newInstance();
            this.conn = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void openConn(String url, String username, String password,
            String driver)
    {
        try
        {
            Class.forName(driver).newInstance();
            this.conn = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * �ر�<code>JDBC</code>����
     * 
     * @throws SQLException
     */
    @Override
    public void closeConn() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
        }
    }

    /**
     * �����ݿ��ύ����
     * 
     * @throws SQLException
     */
    @Override
    public void commit() throws SQLException
    {
        try
        {
            this.conn.commit();
        }
        catch (SQLException e)
        {
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
    public void setPrepareSql(String sql) throws SQLException
    {
        this.pstmt = this.conn.prepareStatement(sql);
    }

    /**
     * ����PreparedStatement��String���Ͳ���ֵ
     * 
     * @throws Exception
     */
    @Override
    public boolean setPreparedString(int i, String inString)
            throws SQLException
    {
        if (this.pstmt != null)
        {
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
    public boolean setPreparedInt(int i, int in) throws SQLException
    {
        if (this.pstmt != null)
        {
            this.pstmt.setInt(i, in);
            return true;
        }
        return false;
    }
    
    /**
     * ����PreparedStatement��long���Ͳ���ֵ
     * 
     * @throws Exception
     * */
    @Override
    public boolean setPreparedLong(int i, long in) throws SQLException
    {
        if (this.pstmt != null)
        {
            this.pstmt.setLong(i, in);
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
    public boolean setPreparedDouble(int i, double in) throws SQLException
    {
        if (this.pstmt != null)
        {
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
    public int executeExceptPreparedQuery() throws SQLException
    {
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
    public int executeExceptQuery(String sql) throws SQLException
    {
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
    public ResultSet retrive(String sql) throws SQLException
    {
        this.stmt = this.conn.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        // DebugOut.println(this.stmt);
        this.rs = this.stmt.executeQuery(sql);
        return this.rs;
    }

    /**
     * ���PreparedStatement��ѯ���
     * 
     * @throws SQLException
     */
    @Override
    public ResultSet retrivePrepared() throws SQLException
    {
        this.rs = this.pstmt.executeQuery();
        return this.rs;
    }
}
