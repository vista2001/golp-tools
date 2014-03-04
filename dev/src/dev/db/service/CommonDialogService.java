/* �ļ�����       CommonDialogService.java
 * ������           ���ļ������˽ӿ�CommonDialogService���ýӿ��й涨��һЩ���ڷ������ݿ�ķ�����
 * �����ˣ�       rxy
 * ����ʱ�䣺   2014.1.2
 */

package dev.db.service;

import java.sql.SQLException;
import java.util.List;

import dev.db.pojo.TAopDll;
import dev.db.pojo.TDataItem;
import dev.db.pojo.TServer;
import dev.db.pojo.TTrade;

/**
 * �ӿ�CommonDialogService�й涨��һЩ���ڷ������ݿ�ķ�����
 * @author rxy
 */
public interface CommonDialogService
{
    /**
     * ��ѯĳ�ض����̵���������е����м�¼��һ����¼��Ӧһ��TDataItem���������������¼����
     * List&ltTDataItem&gt����ʽ���ء�
     * @param prjId �ض����̵Ĺ���Id
     * @return ���������������¼��List&ltTDataItem&gt����
     * @throws SQLException
     */
    public List<TDataItem> dataItemDialogQuery(String prjId) throws SQLException;
    
    /**
     * ��ѯĳ�ض����̵ķ��������е����м�¼��һ����¼��Ӧһ��TServer���������������¼����
     * List&ltTServer&gt����ʽ���ء�
     * @param prjId �ض����̵Ĺ���Id
     * @return ���������������¼��List&ltTServer&gt����
     * @throws SQLException
     */
    public List<TServer> serverDialogQuery(String prjId) throws SQLException;
    
    /**
     * ��ѯĳ�ض����̵Ľ��ױ��е����м�¼��һ����¼��Ӧһ��TTrade���������������¼����
     * List&ltTTrade&gt����ʽ���ء�
     * @param prjId �ض����̵Ĺ���Id
     * @param type ��ѯ�����ͣ���ΪConstants.ALL�����ʾ��ѯ���еĽ��ף���ΪConstants.TFM_BINDING��
     * ���ʾֻ��ѯ������ͼʵ�ֵ���δ��ĳ����ͼ�󶨵Ľ���
     * @return ���������������¼��List&ltTTrade&gt����
     * @throws SQLException
     */
    public List<TTrade> tradeDialogQuery(String prjId, int type) throws SQLException;
    
    /**
     * ��ѯĳ�ض����̵�ԭ�ӽ��׶�̬����е����м�¼��һ����¼��Ӧһ��TAopDll���������������¼����
     * List&ltTAopDll&gt����ʽ���ء�
     * @param prjId �ض����̵Ĺ���Id
     * @return ���������������¼��List&ltTAopDll&gt����
     * @throws SQLException
     */
    public List<TAopDll> aopDllQuery(String prjId) throws SQLException;
    
    /**
     * �÷����������½�����ĳ���̵�������ʱ��ȷ�����½����������Id��ȷ���ķ����ǣ������ݿ��в�ѯ�ù�����
     * ��������Id�����ֵ�������ֵ��1��Ϊ�½��������Id�����������Ϊ�գ����ѯ���̱��DATAIDSTART
     * �ֶΣ����ֶε�ֵ��Ϊ�½����������Id�����յĽ����װ��һ��TDataItem�����С�
     * @param prjId �ض����̵Ĺ���Id
     * @return ��װ�������½����������Id��TDataItem����
     * @throws SQLException
     */
    public TDataItem getNewDataItemId(String prjId) throws SQLException;
    
    /**
     * �÷����������½�����ĳ���̵�ԭ�ӽ��׶�̬��ʱ��ȷ�����½���ԭ�ӽ��׶�̬���Id��ȷ���ķ����ǣ������ݿ��в�ѯ�ù�����
     * ��ԭ�ӽ��׶�̬��Id�����ֵ�������ֵ��1��Ϊ�½�ԭ�ӽ��׶�̬���Id�����������Ϊ�գ����ѯ���̱��AOPDLLIDSTART
     * �ֶΣ����ֶε�ֵ��Ϊ�½���ԭ�ӽ��׶�̬���Id�����յĽ����װ��һ��TAopDll�����С�
     * @param prjId �ض����̵Ĺ���Id
     * @return ��װ�������½����������Id��TAopDll����
     * @throws SQLException
     */
    public TAopDll getNewAopDllId(String prjId) throws SQLException;
   
    /**
     * �÷��������������������״̬�ĶԻ����У��û����ok������趨�Ľ���������ݿ�״̬�ֶ�
     * @param prjId �ض����̵Ĺ���Id
     * @param published �Ѿ�������������
     * @param nonPublished δ������������
     * @throws SQLException
     */
    public void updateDataItemState(String prjId, String[] published, String[] nonPublished) throws SQLException;
}
