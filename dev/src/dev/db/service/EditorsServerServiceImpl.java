package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.db.pojo.TServer;
import dev.util.CommonUtil;
import dev.util.DebugOut;

public class EditorsServerServiceImpl implements EditorsServerService
{
    @Override
    public HashMap<String, String> queryServerByIdOrName(String key, int type,
            PreferenceStore ps) throws SQLException
    {
        HashMap<String, String> map = new HashMap<String, String>();
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        String sql = "select SERVERID, SERVERNAME, SERVERDESC, SERVERSPECLIB,"
                + "SERVERSPECINCLUDE, CALLBACKSOURCE, OTHFUNSOURCE, SERVERLEVEL,"
                + "SERVERSPECMARCO, SERVERSPECOBJ from server where";
        // type为0代表根据Id查询，type为1代表根据Name查询
        if (type == 0)
        {
            sql += " serverid='" + key + "'";
        }
        if (type == 1)
        {
            sql += " servername='" + key + "'";
        }
        DebugOut.println(sql);
        try
        {
            dbConnectImpl.openConn(ps);
            ResultSet rs = dbConnectImpl.retrive(sql);
            if (rs.next() && rs.getString(1) != null)
            {
                map.put("ID", rs.getString(1));
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
                map.put("SERVERSPECINCLUDE",
                        rs.getString(5) != null ? rs.getString(5) : "");
                map.put("CALLBACKSOURCE",
                        rs.getString(6) != null ? rs.getString(6) : "");
                map.put("OTHFUNSOURCE",
                        rs.getString(7) != null ? rs.getString(7) : "");
                map.put("SERVERLEVEL", rs.getString(8));
                map.put("SERVERSPECMARCO",
                        rs.getString(9) != null ? rs.getString(9) : "");
                map.put("SERVERSPECOBJ",
                        rs.getString(10) != null ? rs.getString(10) : "");
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

    @Override
    public void updateServerById(List<String> datalist, PreferenceStore ps)
            throws SQLException
    {
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        try
        {
            dbConnectImpl.openConn(ps);
            dbConnectImpl
                    .setPrepareSql("update SERVER set SERVERNAME=?,SERVERDESC=?,"
                            + "SERVERSPECLIB=?,SERVERSPECINCLUDE=?,CALLBACKSOURCE=?,"
                            + "OTHFUNSOURCE=?,SERVERSPECMARCO=?,SERVERSPECOBJ=?"
                            + " where SERVERID=?");
            dbConnectImpl.setPreparedString(1, datalist.get(0));
            dbConnectImpl.setPreparedString(2, datalist.get(1));
            dbConnectImpl.setPreparedString(3, datalist.get(2));
            dbConnectImpl.setPreparedString(4, datalist.get(3));
            dbConnectImpl.setPreparedString(5, datalist.get(4));
            dbConnectImpl.setPreparedString(6, datalist.get(5));
            dbConnectImpl.setPreparedString(7, datalist.get(6));
            dbConnectImpl.setPreparedString(8, datalist.get(7));
            dbConnectImpl.setPreparedString(9, datalist.get(8));
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

    @Override
    public void insertServer(TServer server, String prjId) throws SQLException
    {
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        PreferenceStore ps = CommonUtil.initPs(prjId);
        try
        {
            dbConnectImpl.openConn(ps);
            dbConnectImpl
                    .setPrepareSql("insert into server (SERVERID, SERVERNAME, SERVERDESC,"
                            + "SERVERSPECLIB, SERVERSPECINCLUDE, CALLBACKSOURCE, OTHFUNSOURCE,"
                            + "SERVERLEVEL, SERVERSPECMARCO, SERVERSPECOBJ) values(?,?,?,?,?,?,?,?,?,?)");
            dbConnectImpl.setPreparedString(1, server.getServerId());
            dbConnectImpl.setPreparedString(2, server.getServerName());
            dbConnectImpl.setPreparedString(3, server.getServerDesc());
            dbConnectImpl.setPreparedString(4, server.getServerSpecLib());
            dbConnectImpl.setPreparedString(5, server.getServerSpecInclude());
            dbConnectImpl.setPreparedString(6, server.getCallbackSource());
            dbConnectImpl.setPreparedString(7, server.getOthFunSource());
            dbConnectImpl.setPreparedString(8, server.getServerLevel());
            dbConnectImpl.setPreparedString(9, server.getServerSpecMarco());
            dbConnectImpl.setPreparedString(10, server.getServerSpecObj());
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
}
