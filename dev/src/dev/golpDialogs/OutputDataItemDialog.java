/* �ļ�����       OutputDataItemDialog.java
 * ������           ���ļ���������OutputDataItemDialog������ʵ�������½��������У�
 *         ���û�ѡ�����������Ĺ��ܡ����������֮�������ߣ�|���ָ�,���������ĸ�ʽ���磺DataItemID@a@b��
 *         �����룬��aΪ1������aΪ0���������ݴ����������и��ƣ���bΪ1������bΪ0��
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.4
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.23
 * �޸����ݣ�   �����ı���������ʾ���������ϸ��Ϣ�� 
 */

package dev.golpDialogs;

import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dev.db.pojo.TDataItem;
import dev.db.service.CommonDialogServiceImpl;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;

/**
 * OutputDataItemDialog�࣬����ʵ�������½��������У����û�ѡ�����������Ĺ��ܡ�
 * 
 * @author rxy
 */
public class OutputDataItemDialog extends TitleAreaDialog
{
    /**
     * ������ʾ������������б�org.eclipse.swt.widgets.List���ͣ���֧�ֶ�ѡ��
     */
    private List allDataItemList;

    /**
     * ������ʾ�����������б�org.eclipse.swt.widgets.List���ͣ���֧�ֶ�ѡ��
     */
    private List outputDataItemList;

    /**
     * ��Ӱ�ť������ð�ť���Ὣ�����������б���ѡ�е�����ӵ�����������б�
     */
    private Button addButton;

    /**
     * ɾ����ť������ð�ť���Ὣ�����������б���ѡ�е���Ӹ��б���ɾ����
     */
    private Button delButton;

    /**
     * ���������������Ƿ����������б�������Ϊ��0-��1-�ǡ���
     */
    private Combo needCombo;

    /**
     * ������������������Ƿ������������ݵ������б�������Ϊ��0-��1-�ǡ���
     */
    private Combo fromReqCombo;

    /**
     * �����洢�����������б�java.util.List<String>���ͣ���ͨ�����ñ����е�getListForReturn�������Եõ����б�
     */
    java.util.List<String> listForReturn = null;

    java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();

    /**
     * �ڱ���Ĺ��캯���У��Ὣ���캯���Ĳ���alreadyHaveString�����ߣ�|���ָ��󣬱����ڴ��б�java.util.List<
     * String>���ͣ��С�
     */
    private java.util.List<String> alreadyHaveList = new ArrayList<String>();

    /**
     * ��������Id
     */
    private String prjId;
    
    /**
     * ���������ڴ洢ÿһ�����������ϸ��Ϣ
     */
    private String[] dataItemInfo;
    
    /**
     * ������ʾ��������ϸ��Ϣ���ı���
     */
    private Text dataItemInfoText;
    
    /**
     * �ú������ظ���ĳ�Ա����listForReturn��
     * 
     * @return ����ĳ�Ա����listForReturn��
     */
    public java.util.List<String> getListForReturn()
    {
        return listForReturn;
    }

    /**
     * ���캯�����ڸú����У��Ὣ����alreadyHaveString�����ߣ�|���ָ��󣬱�����alreadyHaveList�С�
     * 
     * @param parentShell
     * @param obj
     * @param alreadyHaveString
     *            ������ַ�����
     */
    public OutputDataItemDialog(Shell parentShell, Object obj,
            String alreadyHaveString, String prjId)
    {
        super(parentShell);
        setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.PRIMARY_MODAL);
        setHelpAvailable(false);
        this.prjId = prjId;
        String[] tmp = alreadyHaveString.split("\\|");
        for (String s : tmp)
        {
            alreadyHaveList.add(s);
        }
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        setTitle("���������");
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(4, false));
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                1));

        Label allDataItemLabel = new Label(container, SWT.NONE);
        allDataItemLabel.setText("���������");

        new Label(container, SWT.NONE);

        Label outputDataItemLabel = new Label(container, SWT.NONE);
        outputDataItemLabel.setText("��������");

        new Label(container, SWT.NONE);

        allDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.MULTI);
        allDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 5));

        addButton = new Button(container, SWT.NONE);
        addButton.setEnabled(false);
        GridData gd_addButton = new GridData(SWT.CENTER, SWT.CENTER, false,
                false, 1, 5);
        gd_addButton.widthHint = 60;
        addButton.setLayoutData(gd_addButton);
        addButton.setText("���>>");

        outputDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.MULTI);
        outputDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 7));

        // ��alreadyHaveList�е�������ӵ�outputDataItemList
        for (String s : alreadyHaveList)
        {
            if (s != "")
            {
                outputDataItemList.add(s);
            }
        }

        delButton = new Button(container, SWT.NONE);
        delButton.setEnabled(false);
        GridData gd_delButton = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_delButton.widthHint = 60;
        delButton.setLayoutData(gd_delButton);
        delButton.setText("ɾ��");

        Label needLabel = new Label(container, SWT.NONE);
        needLabel.setText("�Ƿ���룺");

        needCombo = new Combo(container, SWT.READ_ONLY);
        needCombo.setEnabled(false);
        needCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
                1, 1));
        needCombo.add("0-��");
        needCombo.add("1-��");

        Label fromReqLabel = new Label(container, SWT.NONE);
        fromReqLabel.setText("��������Դ��");

        fromReqCombo = new Combo(container, SWT.READ_ONLY);
        fromReqCombo.setEnabled(false);
        fromReqCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
                false, 1, 1));
        fromReqCombo.add("1-��������");
        fromReqCombo.add("2-������");
        
        Label dataItemInfoLabel = new Label(container, SWT.NONE);
        dataItemInfoLabel.setText("��������ϸ��Ϣ��");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        dataItemInfoText = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        dataItemInfoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        allDataItemList.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                // ֻ�е�allDataItemList������Ŀ��ѡ��ʱ����Ӱ�ť��addButton���ſ��á�
                if (allDataItemList.getSelectionCount() > 0)
                {
                    addButton.setEnabled(true);
                    if(allDataItemList.getSelectionCount() == 1)
                    {
                        dataItemInfoText.setText(dataItemInfo[allDataItemList.getSelectionIndex()]);
                    }
                    else
                    {
                        dataItemInfoText.setText("");
                    }
                }
            }
        });

        addButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (allDataItemList.getSelectionCount() > 0)
                {

                    for (String s : allDataItemList.getSelection())
                    {
                        // ���´����ȷ���˲��Ὣ�ظ�������ӵ�outputDataItemList�С�
                        boolean repeat = false;
                        for (String s1 : outputDataItemList.getItems())
                        {
                            if ((s1.split("@"))[0].equals(s))
                            {
                                repeat = true;
                                break;
                            }
                        }
                        if (!repeat)
                        {
                            // ��s��������ID��ƴ���ϡ�@@������ӵ�outputDataItemList�С�
                            outputDataItemList.add(s + "@@");
                        }

                    }
                }
            }
        });

        outputDataItemList.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                // ��outputDataItemList�У�����Ŀ��ѡ�С�
                if (outputDataItemList.getSelectionCount() > 0)
                {
                    delButton.setEnabled(true);
                    needCombo.setEnabled(true);
                    fromReqCombo.setEnabled(true);

                    // ��ֻѡ����һ�
                    if (outputDataItemList.getSelectionCount() == 1)
                    {
                        String s3 = (outputDataItemList.getSelection())[0];
                        String[] tmp = s3.split("@");

                        // tmp����ĳ���Ϊ3�����ʾ��ǰoutputDataItemList��ѡ�е���һ�������Ϊ���¸�ʽ֮һ��DataItemID@a@b��
                        // DataItemID@@b��
                        if (tmp.length == 3)
                        {
                            // ��ѡ�����ݵĸ�ʽΪDataItemID@@b����Ӧ����needCombo�ÿգ�
                            // ��fromReqCombo��ֵ��Ϊb��
                            if (s3.indexOf("@") == s3.lastIndexOf("@") - 1)
                            {
                                needCombo.deselectAll();
                                fromReqCombo.select(Integer.parseInt(tmp[2]) - 1);
                            }
                            // ��ѡ�����ݵĸ�ʽΪDataItemID@a@b����Ӧ����needCombo��ֵ��Ϊa��
                            // ��fromReqCombo��ֵ��Ϊb��
                            else
                            {
                                needCombo.select(Integer.parseInt(tmp[1]));
                                fromReqCombo.select(Integer.parseInt(tmp[2]) - 1);
                            }
                        }
                        // tmp����ĳ���Ϊ2�����ʾ��ǰoutputDataItemList��ѡ�е���һ�������Ϊ���¸�ʽ��DataItemID@a@��
                        // ��Ӧ�ý�needCombo��ֵ��Ϊa����fromReqCombo�ÿա�
                        else if (tmp.length == 2)
                        {
                            needCombo.select(Integer.parseInt(tmp[1]));
                            fromReqCombo.deselectAll();

                        }
                        // tmp����ĳ���Ϊ1�����ʾ��ǰoutputDataItemList��ѡ�е���һ�������Ϊ���¸�ʽ��DataItemID@@��
                        // ��Ӧ�ý�needCombo��fromReqCombo���ÿա�
                        else
                        {
                            needCombo.deselectAll();
                            fromReqCombo.deselectAll();
                        }
                    }
                    // ��ѡ���˶����needCombo��fromReqCombo���ÿա�
                    else
                    {
                        needCombo.deselectAll();
                        fromReqCombo.deselectAll();
                    }
                }
            }
        });

        delButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (outputDataItemList.getSelectionCount() > 0)
                {
                    int[] indices = outputDataItemList.getSelectionIndices();
                    outputDataItemList.remove(indices);

                    // �ڽ�outputDataItemList��ѡ�е���Ŀ��ɾ����outputDataItemList�У���û����Ŀ��ѡ�У�
                    // ���ԣ�Ӧ�ý�needCombo��fromReqCombo�ÿգ�����delButton��ͬneedCombo��fromReqCombo����Ϊ�����á�
                    delButton.setEnabled(false);
                    needCombo.deselectAll();
                    needCombo.setEnabled(false);
                    fromReqCombo.deselectAll();
                    fromReqCombo.setEnabled(false);
                }
            }
        });

        needCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                // ����Ϊ�δ˴�Ҫ�жϲ�����-1����ο�InputDataItemDialog.java��needCombo.addModifyListener()��ע�͡�
                if (needCombo.getSelectionIndex() != -1)
                {
                    if (outputDataItemList.getSelectionCount() > 0)
                    {
                        // ��outputDataItemList������ѡ�е���������á�
                        for (int i : outputDataItemList.getSelectionIndices())
                        {
                            String s = outputDataItemList.getItem(i);
                            String sub1 = s.substring(0, s.indexOf("@") + 1);
                            String sub2 = s.substring(s.lastIndexOf("@"),
                                    s.length());
                            outputDataItemList.setItem(i,
                                    sub1 + needCombo.getSelectionIndex() + sub2);
                        }
                    }
                }
            }
        });

        fromReqCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                // ����Ϊ�δ˴�Ҫ�жϲ�����-1����ο�InputDataItemDialog.java��needCombo.addModifyListener()��ע�͡�
                if (fromReqCombo.getSelectionIndex() != -1)
                {
                    if (outputDataItemList.getSelectionCount() > 0)
                    {
                        // ��outputDataItemList������ѡ�е���������á�
                        for (int i : outputDataItemList.getSelectionIndices())
                        {
                            String s = outputDataItemList.getItem(i);
                            String sub = s.substring(0, s.lastIndexOf("@") + 1);
                            outputDataItemList.setItem(i,
                                    sub + (fromReqCombo.getSelectionIndex() + 1));
                        }
                    }
                }
            }
        });

        initData(prjId);
        this.setMessage("��ѡ������������Ϊÿһ��ѡ���Ƿ������Ƿ������������ݡ�" 
                        + System.getProperty("line.separator")
                        + "ע�⣺�ظ�����������ᱻ��ӣ�");

        return area;

    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void okPressed()
    {
        // String[] aops = outputDataItemList.getSelection();
        listForReturn = new ArrayList<String>();
        for (String s : outputDataItemList.getItems())
        {
            listForReturn.add(s);
        }
        notifyInformDialogEvent();
        super.okPressed();
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize()
    {
        return new Point(600, 500);
    }

    /**
     * �����ݿ��ж�ȡ����������ļ�¼�������������ID�������򣬽�������������ID��ʾ��allDataItemList�С�
     */
    private void initData(String prjId)
    {
        CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
        try
        {
            java.util.List<TDataItem> dataItems = commonDialogServiceImpl.dataItemDialogQuery(prjId);
            if(dataItems.isEmpty() == false)
            {
                dataItemInfo = new String[dataItems.size()];
                int i = 0;
                for(TDataItem dataItem : dataItems)
                {
                    allDataItemList.add(dataItem.getDataItemId() + "");
                    dataItemInfo[i] = dataItem.toString();
                    i++;
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addInformDialogListener(InformDialogListener dl)
    {
        listeners.add(dl);
    }

    public void notifyInformDialogEvent()
    {
        for (InformDialogListener idl : listeners)
        {
            idl.handleEvent(new InformDialogEvent(this));
        }
    }
}
