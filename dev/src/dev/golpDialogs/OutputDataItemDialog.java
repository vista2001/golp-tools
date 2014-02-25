/* 文件名：       OutputDataItemDialog.java
 * 描述：           该文件定义了类OutputDataItemDialog，该类实现了在新建交易向导中，
 *         让用户选择输出数据项的功能。输出数据项之间用竖线（|）分隔,输出数据项的格式形如：DataItemID@a@b，
 *         若必须，则a为1，否则a为0，若该数据从请求数据中复制，则b为1，否则b为0。
 * 创建人：       rxy
 * 创建时间：   2013.12.4
 * 修改人：       rxy
 * 修改时间：   2013.12.23
 * 修改内容：   增加文本框，用于显示数据项的详细信息。 
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
 * OutputDataItemDialog类，该类实现了在新建交易向导中，让用户选择输出数据项的功能。
 * 
 * @author rxy
 */
public class OutputDataItemDialog extends TitleAreaDialog
{
    /**
     * 用来显示所有数据项的列表（org.eclipse.swt.widgets.List类型），支持多选。
     */
    private List allDataItemList;

    /**
     * 用来显示输出数据项的列表（org.eclipse.swt.widgets.List类型），支持多选。
     */
    private List outputDataItemList;

    /**
     * 添加按钮，点击该按钮，会将所有数据项列表中选中的项添加到输出数据项列表。
     */
    private Button addButton;

    /**
     * 删除按钮，点击该按钮，会将输入数据项列表中选中的项从该列表中删除。
     */
    private Button delButton;

    /**
     * 用来设置数据项是否必须的下拉列表，其内容为“0-否、1-是”。
     */
    private Combo needCombo;

    /**
     * 用来设置输出数据项是否来自请求数据的下拉列表，其内容为“0-否、1-是”。
     */
    private Combo fromReqCombo;

    /**
     * 用来存储输出数据项的列表（java.util.List<String>类型），通过调用本类中的getListForReturn方法可以得到该列表。
     */
    java.util.List<String> listForReturn = null;

    java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();

    /**
     * 在本类的构造函数中，会将构造函数的参数alreadyHaveString以竖线（|）分隔后，保存在此列表（java.util.List<
     * String>类型）中。
     */
    private java.util.List<String> alreadyHaveList = new ArrayList<String>();

    /**
     * 所属工程Id
     */
    private String prjId;
    
    /**
     * 该数组用于存储每一个数据项的详细信息
     */
    private String[] dataItemInfo;
    
    /**
     * 用于显示数据项详细信息的文本框
     */
    private Text dataItemInfoText;
    
    /**
     * 该函数返回该类的成员变量listForReturn。
     * 
     * @return 该类的成员变量listForReturn。
     */
    public java.util.List<String> getListForReturn()
    {
        return listForReturn;
    }

    /**
     * 构造函数，在该函数中，会将参数alreadyHaveString以竖线（|）分隔后，保存在alreadyHaveList中。
     * 
     * @param parentShell
     * @param obj
     * @param alreadyHaveString
     *            传入的字符串。
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
        setTitle("输出数据项");
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(4, false));
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                1));

        Label allDataItemLabel = new Label(container, SWT.NONE);
        allDataItemLabel.setText("所有数据项：");

        new Label(container, SWT.NONE);

        Label outputDataItemLabel = new Label(container, SWT.NONE);
        outputDataItemLabel.setText("输出数据项：");

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
        addButton.setText("添加>>");

        outputDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.MULTI);
        outputDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 7));

        // 将alreadyHaveList中的内容添加到outputDataItemList
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
        delButton.setText("删除");

        Label needLabel = new Label(container, SWT.NONE);
        needLabel.setText("是否必须：");

        needCombo = new Combo(container, SWT.READ_ONLY);
        needCombo.setEnabled(false);
        needCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
                1, 1));
        needCombo.add("0-否");
        needCombo.add("1-是");

        Label fromReqLabel = new Label(container, SWT.NONE);
        fromReqLabel.setText("数据项来源：");

        fromReqCombo = new Combo(container, SWT.READ_ONLY);
        fromReqCombo.setEnabled(false);
        fromReqCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
                false, 1, 1));
        fromReqCombo.add("1-请求数据");
        fromReqCombo.add("2-交换区");
        
        Label dataItemInfoLabel = new Label(container, SWT.NONE);
        dataItemInfoLabel.setText("数据项详细信息：");
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
                // 只有当allDataItemList中有条目被选中时，添加按钮（addButton）才可用。
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
                        // 以下代码块确保了不会将重复的项添加到outputDataItemList中。
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
                            // 将s（数据项ID）拼接上“@@”，添加到outputDataItemList中。
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
                // 若outputDataItemList中，有条目被选中。
                if (outputDataItemList.getSelectionCount() > 0)
                {
                    delButton.setEnabled(true);
                    needCombo.setEnabled(true);
                    fromReqCombo.setEnabled(true);

                    // 若只选中了一项。
                    if (outputDataItemList.getSelectionCount() == 1)
                    {
                        String s3 = (outputDataItemList.getSelection())[0];
                        String[] tmp = s3.split("@");

                        // tmp数组的长度为3，则表示当前outputDataItemList中选中的那一项的内容为以下格式之一：DataItemID@a@b、
                        // DataItemID@@b。
                        if (tmp.length == 3)
                        {
                            // 若选中内容的格式为DataItemID@@b，则应当将needCombo置空，
                            // 将fromReqCombo的值设为b。
                            if (s3.indexOf("@") == s3.lastIndexOf("@") - 1)
                            {
                                needCombo.deselectAll();
                                fromReqCombo.select(Integer.parseInt(tmp[2]) - 1);
                            }
                            // 若选中内容的格式为DataItemID@a@b，则应当将needCombo的值设为a，
                            // 将fromReqCombo的值设为b。
                            else
                            {
                                needCombo.select(Integer.parseInt(tmp[1]));
                                fromReqCombo.select(Integer.parseInt(tmp[2]) - 1);
                            }
                        }
                        // tmp数组的长度为2，则表示当前outputDataItemList中选中的那一项的内容为以下格式：DataItemID@a@，
                        // 则应该将needCombo的值设为a，将fromReqCombo置空。
                        else if (tmp.length == 2)
                        {
                            needCombo.select(Integer.parseInt(tmp[1]));
                            fromReqCombo.deselectAll();

                        }
                        // tmp数组的长度为1，则表示当前outputDataItemList中选中的那一项的内容为以下格式：DataItemID@@，
                        // 则应该将needCombo和fromReqCombo都置空。
                        else
                        {
                            needCombo.deselectAll();
                            fromReqCombo.deselectAll();
                        }
                    }
                    // 若选中了多项，将needCombo和fromReqCombo都置空。
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

                    // 在将outputDataItemList中选中的条目都删除后，outputDataItemList中，将没有条目被选中，
                    // 所以，应该将needCombo和fromReqCombo置空，并将delButton连同needCombo和fromReqCombo都设为不可用。
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
                // 关于为何此处要判断不等于-1，请参考InputDataItemDialog.java中needCombo.addModifyListener()的注释。
                if (needCombo.getSelectionIndex() != -1)
                {
                    if (outputDataItemList.getSelectionCount() > 0)
                    {
                        // 对outputDataItemList中所有选中的项都进行设置。
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
                // 关于为何此处要判断不等于-1，请参考InputDataItemDialog.java中needCombo.addModifyListener()的注释。
                if (fromReqCombo.getSelectionIndex() != -1)
                {
                    if (outputDataItemList.getSelectionCount() > 0)
                    {
                        // 对outputDataItemList中所有选中的项都进行设置。
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
        this.setMessage("请选择输出数据项，并为每一项选择是否必须和是否来自请求数据。" 
                        + System.getProperty("line.separator")
                        + "注意：重复的数据项将不会被添加！");

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
     * 从数据库中读取所有数据项的记录，并按数据项的ID进行排序，将排序后的数据项ID显示在allDataItemList中。
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
