/* �ļ�����       InputDataItemDialog.java
 * ������           ���ļ���������InputDataItemDialog������ʵ�������½��������У�
 *         ���û�ѡ������������Ĺ��ܡ�����������֮�������ߣ�|���ָ�,����������ĸ�ʽ���磺DataItemID@a��
 *         �����룬��aΪ1������aΪ0��
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
 * InputDataItemDialoge�࣬����ʵ�������½��������У����û�ѡ������������Ĺ��ܡ�
 * 
 * @author rxy
 */
public class InputDataItemDialog extends TitleAreaDialog
{
    /**
     * ������ʾ������������б�org.eclipse.swt.widgets.List���ͣ���֧�ֶ�ѡ��
     */
    private List allDataItemList;

    /**
     * ������ʾ������������б�org.eclipse.swt.widgets.List���ͣ���֧�ֶ�ѡ��
     */
    private List inputDataItemList;

    /**
     * ��Ӱ�ť������ð�ť���Ὣ�����������б���ѡ�е�����ӵ������������б�
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
     * �����洢������������б�java.util.List<String>���ͣ���ͨ�����ñ����е�getListForReturn�������Եõ����б�
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
    public InputDataItemDialog(Shell parentShell, Object obj,
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
        setTitle("����������");
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(4, false));
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                1));

        Label allDataItemLabel = new Label(container, SWT.NONE);
        allDataItemLabel.setText("���������");
        new Label(container, SWT.NONE);

        Label inputDataItemLabel = new Label(container, SWT.NONE);
        inputDataItemLabel.setText("���������");
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

        inputDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.MULTI);
        inputDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 7));

        // ��alreadyHaveList�е�������ӵ�inputDataItemList
        for (String s : alreadyHaveList)
        {
            if (s != "")
            {
                inputDataItemList.add(s);
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

        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
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
                        // ���´����ȷ���˲��Ὣ�ظ�������ӵ�inputDataItemList�С�
                        boolean repeat = false;
                        for (String s1 : inputDataItemList.getItems())
                        {
                            if ((s1.split("@"))[0].equals(s))
                            {
                                repeat = true;
                                break;
                            }
                        }
                        if (!repeat)
                        {
                            // ��s��������ID��ƴ���ϡ�@������ӵ�inputDataItemList�С�
                            inputDataItemList.add(s + "@");
                        }

                    }
                }
            }
        });

        inputDataItemList.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                // ��inputDataItemList�У�����Ŀ��ѡ�С�
                if (inputDataItemList.getSelectionCount() > 0)
                {
                    delButton.setEnabled(true);
                    needCombo.setEnabled(true);

                    // ��ֻѡ����һ�
                    if (inputDataItemList.getSelectionCount() == 1)
                    {
                        String[] tmp = (inputDataItemList.getSelection())[0]
                                .split("@");

                        // tmp����ĳ���Ϊ2�����ʾ��ǰinputDataItemList��ѡ�е���һ�������Ϊ���¸�ʽ��DataItemID@a,
                        // ��ʾ����Ŀ�Ѿ����ù��Ƿ���룬��Ӧ����needCombo��ֵ��Ϊa��
                        if (tmp.length == 2)
                        {
                            needCombo.select(Integer.parseInt(tmp[1]));
                        }
                        // tmp����ĳ���1�����ʾ��ǰinputDataItemList��ѡ�е���һ�������Ϊ���¸�ʽ��DataItemID@��
                        // ��ʾ����Ŀ��δ���ù��Ƿ���룬��Ӧ�ý�needCombo��Ϊ�ա�
                        else
                        {
                            needCombo.deselectAll();
                        }

                    } 
                    
                    //��ѡ���˶����needCombo��Ϊ�ա�
                    else
                    {
                        needCombo.deselectAll();
                    }
                }
            }
        });

        delButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (inputDataItemList.getSelectionCount() > 0)
                {
                    int[] indices = inputDataItemList.getSelectionIndices();
                    inputDataItemList.remove(indices);
                    
                    //�ڽ�inputDataItemList��ѡ�е���Ŀ��ɾ����inputDataItemList�У���û����Ŀ��ѡ�У�
                    //���ԣ�Ӧ�ý�needCombo�ÿգ�����delButton��needCombo��Ϊ�����á�
                    delButton.setEnabled(false);
                    needCombo.deselectAll();
                    needCombo.setEnabled(false);
                }
            }
        });

        needCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                //��û���±�����if��䣬��ᷢ���������´��󣺼��赱ǰinputDataItemList����������ݷֱ���1000@1��1001��
                //���û�����˵�һ����������ΪinputDataItemList��ӹ����������ü������ὫneedCombo��Ϊ��1-�ǡ���
                //��ʱ�ʹ�����needCombo��Modify�¼���needComboԭ���ǿգ���Ȼ��ע���±ߵĴ����ͻᱻִ�У�
                //�˶δ�����ȡ��1000@1�е�1000��Ȼ��ƴ���ϡ�@�����ٽ�ƴ����needCombo�е�ǰѡ�����1-�ǡ�����������1����
                //����γ����ַ�����1000@1����Ȼ���ô��ַ���������inputDataItemList��ǰѡ��������ݣ���Ȼ�Ƕ��һ�٣�
                //����ʱinputDataItemListѡ�����������Ȼ��1000@1��������ʾ���������ȷ�ġ�Ȼ���û��ٵ��inputDataItemList�еĵڶ��
                //��1001����ʱinputDataItemList�ļ������ὫneedCombo�ÿգ��˾��ִ�����needCombo��Modify�¼���
                //Ȼ���ע���±ߵĴ�����ֻᱻִ�У����ջ��γ��ַ���1001@-1����Ϊ��ʱneedComboΪ�գ�����������-1����
                //Ȼ��inputDataItemList��ǰѡ��������ݣ��ͻ���ԭ����1001��Ϊ1001@-1������Ȼ��һ�ִ���
                //����ʹ���±ߵ�����if����������������ķ�����
                //ע��֮���Դ˴����߼��Ƚϸ��ӣ����һ�����һЩ���һ�ٵĲ���������Ϊ����needCombo��Modify�¼���2�������
                //һ�����û������inputDataItemList�е���Ŀ��inputDataItemList�ļ������ı���needCombo�����ݣ�
                //��һ�����û�ͨ�����needCombo���ı���needCombo�����ݡ�����״̬�£�ϣ��needCombo��Modify������
                //ֻ����ǰ���ڶ����������Ŀǰ���޷�����ǰ��������������Բ�ʹ�����±�Ƿ�׵Ĵ���ʽ���������Ժ�ʹ���Զ����¼������ơ�
                if (needCombo.getSelectionIndex() != -1)
                {
                    if (inputDataItemList.getSelectionCount() > 0)
                    {
                        //��inputDataItemList������ѡ�е���������á�
                        for (int i : inputDataItemList.getSelectionIndices())
                        {
                            String s1 = (inputDataItemList.getItem(i)
                                    .split("@"))[0];
                            inputDataItemList.setItem(i,
                                    s1 + "@" + needCombo.getSelectionIndex());
                        }
                    }
                }
            }
        });

        initData(prjId);
        this.setMessage("��ѡ�������������Ϊÿһ��ѡ���Ƿ���롣"
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
        listForReturn = new ArrayList<String>();
        for (String s : inputDataItemList.getItems())
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
