/* �ļ�����       CommonDialogServiceImpl.java
 * ������           ���ļ���������CommonDialogServiceImpl������ʵ���˽ӿ�CommonDialogService��
 *         �ڸýӿ��й涨��һЩ���ڷ������ݿ�ķ�����
 * �����ˣ�       rxy
 * ����ʱ�䣺   2014.1.2
 */

package dev.db.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.db.pojo.TAopDll;
import dev.db.pojo.TDataItem;
import dev.db.pojo.TServer;
import dev.db.pojo.TTrade;
import dev.util.CommonUtil;
import dev.util.Constants;

/**
 * ��CommonDialogServiceImplʵ���˽ӿ�CommonDialogService�е����з����� ��Щ���������ڷ������ݿ⡣
 * 
 * @author rxy
 */
public class CommonDialogServiceImpl implements CommonDialogService
{

    @Override
    public List<TDataItem> dataItemDialogQuery(String prjId)
            throws SQLException
    {
        List<TDataItem> contents = new ArrayList<TDataItem>();
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        ResultSet rs = null;
        PreferenceStore ps = CommonUtil.initPs(prjId);
        try
        {
            dbConnectImpl.openConn(ps);
            rs = dbConnectImpl.retrive("select dataItemId, dataName, dataType,"
                                       + "dataLen, fmlId, dataAop, dataDesc,  "
                                       + "dataLvL, isPublished from dataItem order by "
                                       + "dataItemId");

            while (rs.next())
            {
                TDataItem dataItem = new TDataItem();
                dataItem.setDataItemId(rs.getInt(1));
                dataItem.setDataName(rs.getString(2));
                dataItem.setDataType(rs.getString(3));
                dataItem.setDataLen(rs.getInt(4));
                dataItem.setFmlId(rs.getLong(5));
                dataItem.setDataAop(rs.getString(6));
                dataItem.setDataDesc(rs.getString(7));
                dataItem.setDataLvL(rs.getString(8));
                dataItem.setIsPublished(rs.getString(9));
                contents.add(dataItem);
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
        return contents;
    }

    @Override
    public List<TServer> serverDialogQuery(String prjId) throws SQLException
    {
        List<TServer> contents = new ArrayList<TServer>();
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        ResultSet rs = null;
        PreferenceStore ps = CommonUtil.initPs(prjId);
        try
        {
            dbConnectImpl.openConn(ps);
            rs = dbConnectImpl.retrive("select serverId, serverName, serverDesc"
                                       + " from server order by serverId");

            while (rs.next())
            {
                TServer server = new TServer();
                server.setServerId(rs.getString(1));
                server.setServerName(rs.getString(2));
                server.setServerDesc(rs.getString(3));
                contents.add(server);
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
        return contents;
    }

    @Override
    public List<TTrade> tradeDialogQuery(String prjId, int type)
            throws SQLException
    {
        List<TTrade> contents = new ArrayList<TTrade>();
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        ResultSet rs = null;
        PreferenceStore ps = CommonUtil.initPs(prjId);
        String sql = null;
        if (type == Constants.ALL)
        {
            sql = "select tradeId, tradeName, tradeDesc from trade order by tradeId";
        }
        else if (type == Constants.TFM_BINDING)
        {
            sql = "select tradeId, tradeName, tradeDesc from trade where tradeid in "
                  + "(select tradeid from trade where trademode='0' minus select "
                  + "tradeid from t_tfm) order by tradeid";
        }
        if (sql != null)
        {
            try
            {
                dbConnectImpl.openConn(ps);
                rs = dbConnectImpl.retrive(sql);

                while (rs.next())
                {
                    TTrade trade = new TTrade();
                    trade.setTradeId(rs.getInt(1));
                    trade.setTradeName(rs.getString(2));
                    trade.setTradeDesc(rs.getString(3));
                    contents.add(trade);
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
        }
        return contents;
    }

    @Override
    public List<TAopDll> aopDllQuery(String prjId) throws SQLException
    {
        List<TAopDll> contents = new ArrayList<TAopDll>();
        DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
        PreferenceStore ps = CommonUtil.initPs(prjId);
        ResultSet rs = null;
        try
        {
            dbConnectImpl.openConn(ps);
            rs = dbConnectImpl.retrive("select AOPDLLID from AOPDLL order by "
                                       + "AOPDLLID");
            while (rs.next())
            {
                TAopDll aopDll = new TAopDll();
                aopDll.setAopDllId(rs.getInt(1));
                contents.add(aopDll);
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
        return contents;
    }

    @Override
    public TDataItem getNewDataItemId(String prjId) throws SQLException
    {
        TDataItem dataItem = new TDataItem();
        ResultSet rs = null;
        DbConnectImpl dbConnectImpl = new DbConnectImpl();
        PreferenceStore ps = CommonUtil.initPs(prjId);
        String Sql = "select max(DATAITEMID) from DATAITEM where DATALVL=0";
        try
        {
            dbConnectImpl.openConn(ps);
            rs = dbConnectImpl.retrive(Sql);

            // ��DATAITEM��Ϊ��
            if (rs.next() && rs.getString(1) != null)
            {
                dataItem.setDataItemId(rs.getInt(1) + 1);
            }
            // ��DATAITEM��Ϊ��
            else
            {
                Sql = "select dataidstart from project";
                rs = dbConnectImpl.retrive(Sql);
                rs.next();
                dataItem.setDataItemId(rs.getInt(1));
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
        return dataItem;
    }

    @Override
    public TAopDll getNewAopDllId(String prjId) throws SQLException
    {
        TAopDll aopDll = new TAopDll();
        ResultSet rs = null;
        DbConnectImpl dbConnectImpl = new DbConnectImpl();
        PreferenceStore ps = CommonUtil.initPs(prjId);
        String Sql = "select max(AOPDLLID) from AOPDLL";
        try
        {
            dbConnectImpl.openConn(ps);
            rs = dbConnectImpl.retrive(Sql);

            // ��AOPDLL��Ϊ��
            if (rs.next() && rs.getString(1) != null)
            {
                // DebugOut.println(rs.getInt(1));
                aopDll.setAopDllId(rs.getInt(1) + 1);
            }
            // ��AOPDLL��Ϊ��
            else
            {
                Sql = "select AOPDLLIDSTART from project";
                rs = dbConnectImpl.retrive(Sql);
                rs.next();
                aopDll.setAopDllId(rs.getInt(1));
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
        return aopDll;
    }

	@Override
	public void updateDataItemState(String prjId,String[] published,String[] nonPublished) throws SQLException {
		DbConnectImpl dbConnectImpl = new DbConnectImpl();
        PreferenceStore ps = CommonUtil.initPs(prjId);
        try
        {
        	dbConnectImpl.openConn(ps);
        	for(String tmp : published){
        		String sql ="update dataItem set ISPUBLISHED ='1' where DATAITEMID ="+Integer.parseInt(tmp);
        		dbConnectImpl.executeExceptQuery(sql);
        	}
        	for(String tmp : nonPublished){
        		String sql ="update dataItem set ISPUBLISHED ='0' where DATAITEMID ="+Integer.parseInt(tmp);
        		dbConnectImpl.executeExceptQuery(sql);
        	}
        	dbConnectImpl.commit();
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
