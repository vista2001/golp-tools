/* 文件名：       DbConnectImpl.java
 * 修改人：       rxy
 * 修改时间：   2013.11.27
 * 修改内容：    1.将main函数注释掉。
 *         2.用DebugOut.println方法替换System.out.println方法；
 *         3.增加静态方法initPs；
 *         4.在openConn(PreferenceStore ps)方法中，取消对dbType的处理；
 *         5.修改retrive方法，调用this.conn.createStatement方法时加入参数
 *         ResultSet.TYPE_SCROLL_INSENSITIVE和ResultSet.CONCUR_READ_ONLY。
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
    // 注意设置为false时，如果数据库为access，那么再关闭链接时会抛出异常
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

    /** 创建<code>JDBC</code>的链接 */
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
     * 关闭<code>JDBC</code>链接
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
     * 向数据库提交操作
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
     * 为链接创建sql的PreparedStatement
     * 
     * @throws SQLException
     */
    @Override
    public void setPrepareSql(String sql) throws SQLException
    {
        this.pstmt = this.conn.prepareStatement(sql);
    }

    /**
     * 设置PreparedStatement的String类型参数值
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
     * 设置PreparedStatement的int类型参数值
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
     * 设置PreparedStatement的long类型参数值
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
     * 设置PreparedStatement的double类型参数值
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
     * 执行PreparedStatement除查询外的语句，返回操作的行数
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
     * 执行除查询外的语句，返回操作的行数
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
     * 获得查询结果
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
     * 获得PreparedStatement查询结果
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
