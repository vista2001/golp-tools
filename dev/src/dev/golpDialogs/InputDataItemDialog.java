/* 文件名：       InputDataItemDialog.java
 * 描述：           该文件定义了类InputDataItemDialog，该类实现了在新建交易向导中，
 *         让用户选择输入数据项的功能。输入数据项之间用竖线（|）分隔,输入数据项的格式形如：DataItemID@a，
 *         若必须，则a为1，否则a为0。
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
 * InputDataItemDialoge类，该类实现了在新建交易向导中，让用户选择输入数据项的功能。
 * 
 * @author rxy
 */
public class InputDataItemDialog extends TitleAreaDialog
{
    /**
     * 用来显示所有数据项的列表（org.eclipse.swt.widgets.List类型），支持多选。
     */
    private List allDataItemList;

    /**
     * 用来显示输入数据项的列表（org.eclipse.swt.widgets.List类型），支持多选。
     */
    private List inputDataItemList;

    /**
     * 添加按钮，点击该按钮，会将所有数据项列表中选中的项添加到输入数据项列表。
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
     * 用来存储输入数据项的列表（java.util.List<String>类型），通过调用本类中的getListForReturn方法可以得到该列表。
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
        setTitle("输入数据项");
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(4, false));
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                1));

        Label allDataItemLabel = new Label(container, SWT.NONE);
        allDataItemLabel.setText("所有数据项：");
        new Label(container, SWT.NONE);

        Label inputDataItemLabel = new Label(container, SWT.NONE);
        inputDataItemLabel.setText("输入数据项：");
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

        inputDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.MULTI);
        inputDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 7));

        // 将alreadyHaveList中的内容添加到inputDataItemList
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
        delButton.setText("删除");

        Label needLabel = new Label(container, SWT.NONE);
        needLabel.setText("是否必须：");

        needCombo = new Combo(container, SWT.READ_ONLY);
        needCombo.setEnabled(false);
        needCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
                1, 1));
        needCombo.add("0-否");
        needCombo.add("1-是");

        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
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
                        // 以下代码块确保了不会将重复的项添加到inputDataItemList中。
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
                            // 将s（数据项ID）拼接上“@”，添加到inputDataItemList中。
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
                // 若inputDataItemList中，有条目被选中。
                if (inputDataItemList.getSelectionCount() > 0)
                {
                    delButton.setEnabled(true);
                    needCombo.setEnabled(true);

                    // 若只选中了一项。
                    if (inputDataItemList.getSelectionCount() == 1)
                    {
                        String[] tmp = (inputDataItemList.getSelection())[0]
                                .split("@");

                        // tmp数组的长度为2，则表示当前inputDataItemList中选中的那一项的内容为以下格式：DataItemID@a,
                        // 表示该条目已经设置过是否必须，则应当将needCombo的值设为a。
                        if (tmp.length == 2)
                        {
                            needCombo.select(Integer.parseInt(tmp[1]));
                        }
                        // tmp数组的长度1，则表示当前inputDataItemList中选中的那一项的内容为以下格式：DataItemID@，
                        // 表示该条目尚未设置过是否必须，则应该将needCombo置为空。
                        else
                        {
                            needCombo.deselectAll();
                        }

                    } 
                    
                    //若选中了多项，将needCombo置为空。
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
                    
                    //在将inputDataItemList中选中的条目都删除后，inputDataItemList中，将没有条目被选中，
                    //所以，应该将needCombo置空，并将delButton和needCombo设为不可用。
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
                //若没有下边这条if语句，则会发生类似以下错误：假设当前inputDataItemList中有两项，内容分别是1000@1和1001，
                //当用户点击了第一条，则由于为inputDataItemList添加过监听器，该监听器会将needCombo设为“1-是”，
                //此时就触发了needCombo的Modify事件（needCombo原本是空），然后本注释下边的代码块就会被执行，
                //此段代码块会取出1000@1中的1000，然后拼接上“@”，再将拼接上needCombo中当前选择项（“1-是”）的索引“1”，
                //最后形成了字符串“1000@1”，然后用此字符串来设置inputDataItemList当前选中项的内容，虽然是多此一举，
                //但此时inputDataItemList选中项的内容依然是1000@1，至少显示结果还是正确的。然后用户再点击inputDataItemList中的第二项，
                //即1001，此时inputDataItemList的监听器会将needCombo置空，此举又触发了needCombo的Modify事件，
                //然后此注释下边的代码块又会被执行，最终会形成字符串1001@-1（因为此时needCombo为空，其索引就是-1），
                //然后inputDataItemList当前选中项的内容，就会由原来的1001变为1001@-1，这显然是一种错误。
                //所以使用下边的这条if语句避免了上述情况的发生。
                //注：之所以此处的逻辑比较复杂，并且还做了一些多此一举的操作，是因为触发needCombo的Modify事件有2种情况，
                //一种是用户点击了inputDataItemList中的条目，inputDataItemList的监听器改变了needCombo的内容，
                //另一种是用户通过点击needCombo，改变了needCombo的内容。理想状态下，希望needCombo的Modify监听器
                //只处理前述第二种情况，但目前并无法区分前述两种情况，所以才使用了下边欠妥的处理方式，考虑在以后使用自定义事件来改善。
                if (needCombo.getSelectionIndex() != -1)
                {
                    if (inputDataItemList.getSelectionCount() > 0)
                    {
                        //对inputDataItemList中所有选中的项都进行设置。
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
        this.setMessage("请选择输入数据项，并为每一项选择是否必须。"
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
