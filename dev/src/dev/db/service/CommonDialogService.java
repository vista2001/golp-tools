/* 文件名：       CommonDialogService.java
 * 描述：           该文件定义了接口CommonDialogService，该接口中规定了一些用于访问数据库的方法。
 * 创建人：       rxy
 * 创建时间：   2014.1.2
 */

package dev.db.service;

import java.sql.SQLException;
import java.util.List;

import dev.db.pojo.TAopDll;
import dev.db.pojo.TDataItem;
import dev.db.pojo.TServer;
import dev.db.pojo.TTrade;

/**
 * 接口CommonDialogService中规定了一些用于访问数据库的方法。
 * @author rxy
 */
public interface CommonDialogService
{
    /**
     * 查询某特定工程的数据项表中的所有记录，一条记录对应一个TDataItem对象，所有数据项记录将以
     * List&ltTDataItem&gt的形式返回。
     * @param prjId 特定工程的工程Id
     * @return 包含所有数据项记录的List&ltTDataItem&gt对象
     * @throws SQLException
     */
    public List<TDataItem> dataItemDialogQuery(String prjId) throws SQLException;
    
    /**
     * 查询某特定工程的服务程序表中的所有记录，一条记录对应一个TServer对象，所有数据项记录将以
     * List&ltTServer&gt的形式返回。
     * @param prjId 特定工程的工程Id
     * @return 包含所有数据项记录的List&ltTServer&gt对象
     * @throws SQLException
     */
    public List<TServer> serverDialogQuery(String prjId) throws SQLException;
    
    /**
     * 查询某特定工程的交易表中的所有记录，一条记录对应一个TTrade对象，所有数据项记录将以
     * List&ltTTrade&gt的形式返回。
     * @param prjId 特定工程的工程Id
     * @param type 查询的类型，若为Constants.ALL，则表示查询所有的交易，若为Constants.TFM_BINDING，
     * 则表示只查询用流程图实现的且未被某流程图绑定的交易
     * @return 包含所有数据项记录的List&ltTTrade&gt对象
     * @throws SQLException
     */
    public List<TTrade> tradeDialogQuery(String prjId, int type) throws SQLException;
    
    /**
     * 查询某特定工程的原子交易动态库表中的所有记录，一条记录对应一个TAopDll对象，所有数据项记录将以
     * List&ltTAopDll&gt的形式返回。
     * @param prjId 特定工程的工程Id
     * @return 包含所有数据项记录的List&ltTAopDll&gt对象
     * @throws SQLException
     */
    public List<TAopDll> aopDllQuery(String prjId) throws SQLException;
    
    /**
     * 该方法用于在新建属于某工程的数据项时，确定所新建的数据项的Id，确定的方法是：在数据库中查询该工程下
     * 的数据项Id的最大值，该最大值加1即为新建数据项的Id，若数据项表为空，则查询工程表的DATAIDSTART
     * 字段，该字段的值即为新建的数据项的Id，最终的结果封装在一个TDataItem对象中。
     * @param prjId 特定工程的工程Id
     * @return 封装了正在新建的数据项的Id的TDataItem对象
     * @throws SQLException
     */
    public TDataItem getNewDataItemId(String prjId) throws SQLException;
    
    /**
     * 该方法用于在新建属于某工程的原子交易动态库时，确定所新建的原子交易动态库的Id，确定的方法是：在数据库中查询该工程下
     * 的原子交易动态库Id的最大值，该最大值加1即为新建原子交易动态库的Id，若数据项表为空，则查询工程表的AOPDLLIDSTART
     * 字段，该字段的值即为新建的原子交易动态库的Id，最终的结果封装在一个TAopDll对象中。
     * @param prjId 特定工程的工程Id
     * @return 封装了正在新建的数据项的Id的TAopDll对象
     * @throws SQLException
     */
    public TAopDll getNewAopDllId(String prjId) throws SQLException;
   
    /**
     * 该方法用于在设置数据项发布状态的对话框中，用户点击ok后根据设定的结果更新数据库状态字段
     * @param prjId 特定工程的工程Id
     * @param published 已经发布的数据项
     * @param nonPublished 未发布的数据项
     * @throws SQLException
     */
    public void updateDataItemState(String prjId, String[] published, String[] nonPublished) throws SQLException;
}
