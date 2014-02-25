/* 文件名：       EditorsDaoServiceImpl.java
 * 修改人：       rxy
 * 修改时间：   2013.11.27
 * 修改内容：    1.将该源文件中出现的所有“SERVERSPECAOPDLLS”替换为
 *         “SERVERSPECINCLUDE”；
 *         2.修改queryServerByIdOrName()方法和updateServerById()方法，
 *         增加对数据库中server表新增的字段SERVERSPECMARCO和SERVERSPECOBJ的处理；
 *         3.修改了queryTradeByIdOrName方法和updateTradeById方法，增加了对TradeSrcPath(源文件路径)的处理；
 *         4.用DebugOut.println方法替换System.out.println方法。 
 */

package dev.db.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.util.CommonUtil;
import dev.util.DebugOut;

public class EditorsDaoServiceImpl implements EditorsDaoService
{
    @Override
    public Map<String, String> queryTradeByIdOrName(String id, String name,
            PreferenceStore ps) throws SQLException
    {
        Map<String, String> map = new HashMap<String, String>();
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        String sql = "select TRADEID, TRADENAME, TRADEDESC, UPSERVERID,"
                + "TRADEMODE, TRADESERVERMODE, TRADEINPUTDATA,"
                + "TRADEOUTPUTDATA, PRECONDITION, POSTCONDITION,"
                + "CALLSERVICES, TRADELEVEL, TRADESRCPATH from trade where ";
        if (id.equals("") && name.equals(""))
        {
            return map;
        }
        if (id.equals("") || name.equals(""))
        {
            sql += " tradeid='" + id + "' or tradename='" + name + "'";
        }
        if (!id.equals("") && !name.equals(""))
        {
            sql += " tradeid='" + id + "' and tradename='" + name + "'";
        }
        DebugOut.println(sql);
        try
        {
            dbConnectImpl.openConn(ps);
            ResultSet rs = dbConnectImpl.retrive(sql);
            while (rs.next())
            {
                map.put("ID", rs.getString(1));
                map.put("NAME", rs.getString(2));
                map.put("tradeDescText",
                        rs.getString(3) != null ? rs.getString(3) : "");
                map.put("upServerCombo", rs.getString(4));
                map.put("tradeModelCombo",
                        rs.getString(5) != null ? rs.getString(5) : "");
                map.put("tradeServerModelCombo",
                        rs.getString(6) != null ? rs.getString(6) : "");
                map.put("inputDataText",
                        rs.getString(7) != null ? rs.getString(7) : "");
                map.put("outputDataText",
                        rs.getString(8) != null ? rs.getString(8) : "");
                map.put("preconditionText",
                        rs.getString(9) != null ? rs.getString(9) : "");
                map.put("postconditionText",
                        rs.getString(10) != null ? rs.getString(10) : "");
                map.put("callServiceText",
                        rs.getString(11) != null ? rs.getString(11) : "");
                map.put("tradeLevelText", rs.getString(12));
                map.put("tradeSrcPathText",
                        rs.getString(13) != null ? rs.getString(13) : "");
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
    public String queryProjectId(PreferenceStore ps) throws SQLException
    {
        String prjid = "";
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        try
        {
            dbConnectImpl.openConn(ps);
            ResultSet rs = dbConnectImpl.retrive("select prjid from project");
            if (rs.next())
            {
                prjid = rs.getString(1);
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
        return prjid;
    }

    @Override
    public void updateTradeById(String id, List<String> datalist,
            PreferenceStore ps) throws SQLException
    {
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        try
        {
            dbConnectImpl.openConn(ps);
            dbConnectImpl
                    .setPrepareSql("update trade set tradeName=?,upServerId=?,tradeMode=?,"
                            + "tradeServerMode=?,tradeInputData=?,tradeOutputData=?,callServices=?,precondition=?,"
                            + "postcondition=?,tradeDesc=?,tradeSrcPath=? where tradeid='"
                            + id + "'");
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
            dbConnectImpl.setPreparedString(11, datalist.get(10));
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
    public void insertTrade(List<String> datalist, String prjId)
            throws SQLException
    {
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        PreferenceStore ps = CommonUtil.initPs(prjId);
        try
        {
            dbConnectImpl.openConn(ps);
            dbConnectImpl
                    .setPrepareSql("insert into trade (TRADEID, TRADENAME, TRADEDESC, UPSERVERID,"
                            + "TRADEMODE, TRADESERVERMODE, TRADEINPUTDATA, TRADEOUTPUTDATA, PRECONDITION,"
                            + "POSTCONDITION, CALLSERVICES, TRADELEVEL, TRADESRCPATH) "
                            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
            dbConnectImpl.setPreparedString(11, datalist.get(10));
            dbConnectImpl.setPreparedString(12, datalist.get(11));
            dbConnectImpl.setPreparedString(13, datalist.get(12));
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
