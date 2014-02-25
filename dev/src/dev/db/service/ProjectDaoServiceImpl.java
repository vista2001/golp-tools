package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.db.pojo.TProject;
import dev.util.CommonUtil;

public class ProjectDaoServiceImpl implements ProjectDaoService
{
    public Map<String, String> queryProject(PreferenceStore ps)
            throws SQLException
    {
        Map<String, String> map = new HashMap<String, String>();
        // 打开数据库连接
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        // 设置数据库查询语句
        String sql = "select PRJID, PRJNAME, PRJDESC, APPHOME, GOLPHOME from project";
        try
        {
            dbConnectImpl.openConn(ps);
            // 用数据库命令进行查询，查到的结果放到ResyltSet里
            ResultSet rs = dbConnectImpl.retrive(sql);
            // 将获得的数据写到map里
            if (rs.next() && rs.getString(1) != null)
            {
                map.put("PRJID", rs.getString(1) != null ? rs.getString(1) : "");
                map.put("PRJNAME", rs.getString(2) != null ? rs.getString(2)
                        : "");
                map.put("PRJDESC", rs.getString(3) != null ? rs.getString(3)
                        : "");
                map.put("APPHOME", rs.getString(4) != null ? rs.getString(4)
                        : "");
                map.put("GOLPHOME", rs.getString(5) != null ? rs.getString(5)
                        : "");
            }
            else
            {
                map = null;
            }
        }
        finally
        {
            if (dbConnectImpl != null)
            {
                try
                {
                    // 断开数据库连接
                    dbConnectImpl.closeConn();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public void updateProject1(List<String> datalist, PreferenceStore ps)
            throws SQLException
    {
        // 打开数据库连接
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        try
        {
            dbConnectImpl.openConn(ps);
            // 用数据库命令将datalist中的数据写入数据库
            dbConnectImpl
                    .setPrepareSql("update project set PRJNAME=?, PRJDESC=?");
            dbConnectImpl.setPreparedString(1, datalist.get(0));
            dbConnectImpl.setPreparedString(2, datalist.get(1));
            dbConnectImpl.executeExceptPreparedQuery();
        }
        finally
        {
            if (dbConnectImpl != null)
            {
                try
                {
                    dbConnectImpl.closeConn();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }// 关闭数据库连接
            }
        }
    }

    public void updateProject2(List<String> datalist, PreferenceStore ps)
            throws SQLException
    {
        // 打开数据库连接
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        try
        {
            dbConnectImpl.openConn(ps);
            // 用数据库命令将datalist中的数据写入数据库
            dbConnectImpl
                    .setPrepareSql("update project set APPHOME=?, GOLPHOME=?");
            dbConnectImpl.setPreparedString(1, datalist.get(0));
            dbConnectImpl.setPreparedString(2, datalist.get(1));
            dbConnectImpl.executeExceptPreparedQuery();
        }
        finally
        {
            if (dbConnectImpl != null)
            {
                try
                {
                    dbConnectImpl.closeConn();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }// 关闭数据库连接
            }
        }
    }

    @Override
    public void insertProject(TProject project) throws SQLException
    {
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        PreferenceStore ps = CommonUtil.initPs(project.getPrjId());
        try
        {
            dbConnectImpl.openConn(ps);
            dbConnectImpl
                    .setPrepareSql("insert into project (prjId,prjName,prjDesc,appHome,golpHome) values(?,?,?,?,?)");
            dbConnectImpl.setPreparedString(1, project.getPrjId());
            dbConnectImpl.setPreparedString(2, project.getPrjName());
            dbConnectImpl.setPreparedString(3, project.getPrjDesc());
            dbConnectImpl.setPreparedString(4, project.getAppHome());
            dbConnectImpl.setPreparedString(5, project.getGolpHome());
            dbConnectImpl.executeExceptPreparedQuery();
        }
        finally
        {
            if (dbConnectImpl != null)
            {
                try
                {
                    dbConnectImpl.closeConn();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public String queryProjectId(String dbAddress, String dbPort,
            String dbInstance, String dbUser, String dbPwd) throws SQLException
    {
        String prjId = null;
        DbConnectImpl dbConnImpl = new DbConnectImpl();
        String url = "jdbc:oracle:thin:@" + dbAddress + ":" + dbPort + ":"
                + dbInstance;
        String driver = "oracle.jdbc.driver.OracleDriver";
        dbConnImpl.openConn(url, dbUser, dbPwd, driver);
        try
        {
            ResultSet rs = dbConnImpl.retrive("select prjId from project");
            if (rs.next() && rs.getString(1) != null)
            {
                prjId = rs.getString(1);
            }
            else
            {
                prjId = null;
            }
        }
        finally
        {
            if(dbConnImpl != null)
            {
                try
                {
                    dbConnImpl.closeConn();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                } 
            }
        }
        return prjId;
    }
}
