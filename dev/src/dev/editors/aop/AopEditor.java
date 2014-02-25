/* 文件名：       TradeEditor.java
 * 修改人：       rxy
 * 修改时间：   2013.11.29
 * 修改内容：   1.将AopDesc的属性更改为SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI；
 *         2.将String数组ErrRecoverItem的内容修改为"0-第一种","1-第二种"；
 *         3.对该文件中所有出现以下代码的地方进修修改，
 *         AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover")-1)]);
 *         去掉后边的减1，以此来解决新建原子交易时，选择第一种恢复机制，而后用编辑器打开时提示数据越界的问题；
 *         4.修改init方法中的setPartName，增加对所属工程的显示，这样就解决了从导航中打开一个编辑器后，点击该编辑器
 *         的关闭按钮时，编辑器并未关闭，而是在编辑器的标题中显示了更多的内容，产生该问题的原因在于init方法和setFocus
 *         方法在调用setPartName时参数不一致；
 *         5.修改了部分布局；
 *         6.覆盖父类中的dispose方法，使得在所有编辑器都关闭时，恢复工具的标题“GOLP TOOL”；
 *         7.增加对AopRetVal（原子交易返回值）的处理；
 *         8.在进行级别（APP/GOLP）的判断时，不再直接使用0、1等数字，改为使用dev.util.Constants类的常量；
 *         9.在UI中统一使用0-APP、1-GOLP这种表示；
 *         10.用DebugOut.println方法替换System.out.println方法；
 *         11.增加滚动功能；
 *         12.统一使用File.separator。
 * 修改人：       zxh
 * 修改时间：   2013.12.2
 * 修改内容：   1.修改变量命名。
 * 		   2.输入数据项，输出数据项加对话框。
 */

package dev.editors.aop;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import dev.db.service.EditorAopServiceImpl;
import dev.editors.IGetUpProject;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.golpDialogs.InputDataItemDialog;
import dev.golpDialogs.OutputDataItemDialog;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;
import dev.model.base.ResourceLeafNode;
import dev.util.Constants;
import dev.util.DebugOut;
import dev.views.NavView;

/**
 * Aop表编辑器类
 * <p>
 * 这个类继承了EditorPart类，与AopEditorInput类一起完成Aop编辑器的功能<br>
 * 在编辑器初始化的时候，通过AopEditorInput类传入的数据在Init方法中对编辑器
 * 进行初始化，然后在createPartControl方法中完成对编辑器的控件的构造，包括设
 * 置控件（主要是“查询”“解锁”“修改”按钮）的行为和文本框的输入限制，在createPartControl
 * 方法的最后调用dataInit方法完成从数据库中获得数据并填入文本框中，完成对整个 编辑器的初始化。
 * 
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#addText
 * @see#doSave
 * @see#setFocus
 * @see#lock
 * @see#unlock
 * @see#saveData
 * */
public class AopEditor extends EditorPart implements ISearch, IGetUpProject
{
    public static final String ID = "dev.editor.aop.AopEditor";// 编辑器类的标识
    private Text upProjectText; // 表项部分所属工程文本框
    private Text aopLevelText; // 原子交易级别文本框
    private Text upDllText; // 所属动态库文本框
    private Text aopIdText; // 原子交易标识文本框
    private Text aopNameText; // 原子交易名称文本框
    private Text aopExtsText; // 扩展点列表文本框
    private Text aopDescText; // 原子交易说明文本框
    private Button saveButton; // 修改按钮
    private Button unlockButton; // 解锁按钮
    private Button restoreButton; // 恢复按钮
    private Button inputDataButton;// 输入数据项选择按钮
    private Button outputDataButton;// 输出数据项选择按钮
    private Button aopExtsButton;// 原子交易扩展点列表详情按钮
    private PreferenceStore ps; // 数据库配置信息
    private EditorAopServiceImpl impl; // 数据库操作类对象
    private Map<String, String> map; // 存储查询到数据的Map
    private Map<String, String> restoreMap; // 用于恢复的数据的Map
    String[] ErrRecoverItem = { "0-第一种", "1-第二种" }; // 下拉菜单选项字符串数组
    private Combo aopErrRecoverCombo; // 下拉菜单
    private List<Control> list = new ArrayList<Control>(); // 可编辑控件列表
    private AopEditorInput input; // Input类对象
    private Text inputDataText; // 输入数据项文本框
    private Text outputDataText; // 输出数据项文本框
    private Text preConditionText; // 前置条件文本框
    private Text postConditionText; // 后置条件文本框
    private boolean bDirty = false; // 是否修改判断
    private Search search;
    private Text aopRetValText;

    public AopEditor()
    {
    }

    /**
     * 用于在关闭编辑器时保存编辑器的内容<br>
     * 当isDirty为true时，关闭编辑器会提示是否保存修改，如果选择"是"，则会执行saveData方法。
     * 
     * @return 没有返回值
     */
    @Override
    public void doSave(IProgressMonitor monitor)
    {
        try
        {
            saveData();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void doSaveAs()
    {

    }

    /**
     * 用于对编辑器进行初始化<br>
     * 根据site,input对象对编辑器进行初始化，打开编辑器的时候首先调用的方法
     * 
     * @param site
     *            编辑器的site
     * @param input
     *            编辑器配套的input
     * @return 没有返回值
     * */
    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException
    {
        this.setSite(site); // 设置Site
        this.setInput(input); // 设置Input
        this.input = (AopEditorInput) input; // 对Input初始化
        this.setPartName("原子交易" + " " + this.input.getName() + " " + "所属工程"
                + " " + this.input.getSource().getRootProject().getId()); // 设置编辑器标题
    }

    /**
     * /** 将要解锁的控件放入一个List里
     * 
     * @param list
     * @return 没有返回值
     */
    private void addText(List<Control> list)
    {
        list.add(aopNameText);
        list.add(aopDescText);
        list.add(preConditionText);
        list.add(postConditionText);
        list.add(aopRetValText);
        list.add(inputDataButton);
        list.add(outputDataButton);
        // TODO 原子交易扩展点功能待实现
        // list.add(aopExtsButton);
        list.add(inputDataText);
        list.add(outputDataText);
    }

    /**
     * 判断是否编辑器被修改<br>
     * 如果返回值是true，关闭编辑器时会提示是否保存。
     * 
     * @return 如果编辑器被修改，返回“true”，否则返回“false”
     */
    @Override
    public boolean isDirty()
    {
        return bDirty;
    }

    /**
     * 设置编辑器的状态<br>
     * 将isDirty(),“修改”按钮修改成b的状态。
     * 
     * @param b
     *            要修改成的状态
     * @return 没有返回值
     */
    public void setDirty(boolean b)
    {
        bDirty = b;
        saveButton.setEnabled(b);
        restoreButton.setEnabled(b);
        if (b)
            firePropertyChange(PROP_DIRTY);
        else
            firePropertyChange(PROP_INPUT);
    }

    @Override
    public boolean isSaveAsAllowed()
    {
        return false;
    }

    /**
     * 在编辑器上创建各种空间，设置布局，以及对空间进行初始化<br>
     * 将需要的控件创建在parent里，所有控件的行为都在这个方法里被设定。这个方法在调用完Init后背调用
     * 
     * @param parent
     *            所有控件的parent.
     * @return 没有返回值
     * */
    @Override
    public void createPartControl(Composite parent)
    {
        ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        Composite composite = new Composite(scrolledComposite, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        // 创建编辑器里的控件
        Group searchAopGroup = new Group(composite, SWT.NONE);
        searchAopGroup.setText("原子交易查询");
        searchAopGroup.setLayout(new GridLayout(7, false));
        searchAopGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 1, 1));

        search = new Search();
        search.setUpProject(input.getSource().getRootProject().getId());
        search.setEditorId(ID);
        search.setEditor(this);
        search.createPartControl(searchAopGroup);

        Group aopGroup = new Group(composite, SWT.NONE);
        aopGroup.setLayout(new GridLayout(7, false));
        GridData gd_AOPGroup = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                1);
        gd_AOPGroup.widthHint = 562;
        aopGroup.setLayoutData(gd_AOPGroup);
        aopGroup.setText("原子交易表");

        Label lblNewLabel_1 = new Label(aopGroup, SWT.NONE);
        GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 2, 1);
        gd_lblNewLabel_1.widthHint = 80;
        lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);

        Label upProjectLabel = new Label(aopGroup, SWT.NONE);
        upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        upProjectLabel.setText("所属工程");

        upProjectText = new Text(aopGroup, SWT.BORDER);
        upProjectText.setEnabled(false);
        GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_upProject.widthHint = 70;
        upProjectText.setLayoutData(gd_upProject);

        Label lblNewLabel = new Label(aopGroup, SWT.NONE);
        GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_lblNewLabel.widthHint = 80;
        lblNewLabel.setLayoutData(gd_lblNewLabel);

        Label aopLevelTextLabel = new Label(aopGroup, SWT.NONE);
        aopLevelTextLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        aopLevelTextLabel.setText("原子交易级别");

        aopLevelText = new Text(aopGroup, SWT.BORDER);
        aopLevelText.setEnabled(false);
        GridData gd_AopLevelText = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_AopLevelText.widthHint = 70;
        aopLevelText.setLayoutData(gd_AopLevelText);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label aopErrRecoverLabel = new Label(aopGroup, SWT.DROP_DOWN);
        aopErrRecoverLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        aopErrRecoverLabel.setText("*错误恢复机制");

        aopErrRecoverCombo = new Combo(aopGroup, SWT.READ_ONLY);
        aopErrRecoverCombo.setEnabled(false);
        GridData gd_AopErrRecover = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_AopErrRecover.widthHint = 53;
        aopErrRecoverCombo.setLayoutData(gd_AopErrRecover);
        aopErrRecoverCombo.setItems(ErrRecoverItem);
        aopErrRecoverCombo.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!bDirty)
                    setDirty(true);
            }
        });
        new Label(aopGroup, SWT.NONE);

        Label upDllLabel = new Label(aopGroup, SWT.NONE);
        upDllLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        upDllLabel.setText("所属动态库");

        upDllText = new Text(aopGroup, SWT.BORDER);
        upDllText.setEnabled(false);
        GridData gd_upDll = new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
                1);
        gd_upDll.widthHint = 70;
        upDllText.setLayoutData(gd_upDll);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label aopIdLabel = new Label(aopGroup, SWT.NONE);
        aopIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        aopIdLabel.setText("原子交易标识");

        aopIdText = new Text(aopGroup, SWT.BORDER);
        aopIdText.setEnabled(false);
        GridData gd_AopId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
                1);
        gd_AopId.widthHint = 70;
        aopIdText.setLayoutData(gd_AopId);
        aopIdText.addVerifyListener(new VerifyListener()
        {
            // 设置文本框的输入限制
            @Override
            public void verifyText(VerifyEvent e)
            {
                if (e.character != 8)
                    e.doit = aopIdText.getText().length() <= 32;
            }
        });
        new Label(aopGroup, SWT.NONE);

        Label aopNameLabel = new Label(aopGroup, SWT.NONE);
        aopNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        aopNameLabel.setText("*原子交易名称");

        aopNameText = new Text(aopGroup, SWT.BORDER);
        aopNameText.setEnabled(false);
        GridData gd_AopName = new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1);
        gd_AopName.widthHint = 70;
        aopNameText.setLayoutData(gd_AopName);
        // aopNameText.addVerifyListener(new VerifyListener() {
        // // 设置文本框的输入限制
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopNameText.getText().length() <= 32;
        // }
        // });
        // 当文本框被修改时，设置编辑器的状态为被修改
        aopNameText.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!isDirty())
                {
                    setDirty(true);
                }
            }
        });
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label aopExtsLabel = new Label(aopGroup, SWT.NONE);
        aopExtsLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        aopExtsLabel.setText("扩展点列表");

        aopExtsText = new Text(aopGroup, SWT.BORDER);
        aopExtsText.setEnabled(false);
        aopExtsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 3, 1));

        aopExtsButton = new Button(aopGroup, SWT.NONE);
        aopExtsButton.setEnabled(false);
        GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_btnNewButton.widthHint = 80;
        aopExtsButton.setLayoutData(gd_btnNewButton);
        aopExtsButton.setText("详情");
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label inputDataLabel = new Label(aopGroup, SWT.NONE);
        inputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        inputDataLabel.setText("*输入数据项");

        inputDataText = new Text(aopGroup, SWT.BORDER | SWT.READ_ONLY);
        inputDataText.setEnabled(false);
        inputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 3, 1));
        inputDataText.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!bDirty)
                    setDirty(true);
            }
        });
        inputDataButton = new Button(aopGroup, SWT.NONE);
        inputDataButton.setEnabled(false);
        GridData gd_inputDataButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_inputDataButton.widthHint = 80;
        inputDataButton.setLayoutData(gd_inputDataButton);
        inputDataButton.setText("...");

        inputDataButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = upProjectText.getText();
                InputDataItemDialog inputDataItemDialog = new InputDataItemDialog(
                        e.display.getActiveShell(), e.getSource(),
                        inputDataText.getText(), upProjectId);
                inputDataItemDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((InputDataItemDialog) dm
                                        .getdm()).getListForReturn();
                                String s = "";
                                for (String string : l)
                                {
                                    if (s.equals(""))
                                    {
                                        s += string;
                                    } else
                                    {
                                        s += "|" + string;
                                    }
                                }
                                inputDataText.setText(s);
                            }
                        });
                inputDataItemDialog.open();
            }
        });

        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label outputDataLabel = new Label(aopGroup, SWT.NONE);
        outputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        outputDataLabel.setText("*输出数据项");

        outputDataText = new Text(aopGroup, SWT.BORDER | SWT.READ_ONLY);
        outputDataText.setEnabled(false);
        outputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 3, 1));
        outputDataText.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!bDirty)
                    setDirty(true);
            }
        });
        outputDataButton = new Button(aopGroup, SWT.NONE);
        outputDataButton.setEnabled(false);
        GridData gd_outputDataButton = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 1, 1);
        gd_outputDataButton.widthHint = 80;
        outputDataButton.setLayoutData(gd_outputDataButton);
        outputDataButton.setText("...");
        outputDataButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = upProjectText.getText();
                OutputDataItemDialog outputDataItemDialog = new OutputDataItemDialog(
                        e.display.getActiveShell(), e.getSource(), outputDataText.getText(), upProjectId);
                outputDataItemDialog.addInformDialogListener(new InformDialogListener()
                {

                    @Override
                    public void handleEvent(InformDialogEvent dm)
                    {
                        java.util.List<String> l = ((OutputDataItemDialog) dm
                                .getdm()).getListForReturn();
                        String s = "";
                        for (String string : l)
                        {
                            if (s.equals(""))
                            {
                                s += string;
                            } else
                            {
                                s += "|" + string;
                            }
                        }
                        outputDataText.setText(s);
                    }
                });
                outputDataItemDialog.open();
            }
        });
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label aopRetValLabel = new Label(aopGroup, SWT.NONE);
        aopRetValLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        aopRetValLabel.setText("*原子交易返回值");
        aopRetValLabel.setToolTipText("返回值必须是整数，值之间用竖线分隔，默认值放在第一个");

        aopRetValText = new Text(aopGroup, SWT.BORDER);
        aopRetValText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });
        aopRetValText.setEnabled(false);
        aopRetValText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 4, 1));
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label prePositionLabel = new Label(aopGroup, SWT.NONE);
        prePositionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        prePositionLabel.setText("前置条件");

        preConditionText = new Text(aopGroup, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        preConditionText.setEnabled(false);
        preConditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 4, 3));
        // preConditionText.addVerifyListener(new VerifyListener() {
        // // 设置文本框输入限制
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopDescText.getText().length() <= 128;
        // }
        // });
        // 当文本框被修改时，设置编辑器的状态为被修改
        preConditionText.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!isDirty())
                {
                    setDirty(true);
                    firePropertyChange(PROP_DIRTY);
                    saveButton.setEnabled(true);
                }
            }
        });
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label postPositionLabel = new Label(aopGroup, SWT.NONE);
        postPositionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        postPositionLabel.setText("后置条件");

        postConditionText = new Text(aopGroup, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        postConditionText.setEnabled(false);
        postConditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 4, 3));
        // postConditionText.addVerifyListener(new VerifyListener() {
        // // 设置文本框输入限制
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopDescText.getText().length() <= 128;
        // }
        // });
        // 当文本框被修改时，设置编辑器的状态为被修改
        postConditionText.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!isDirty())
                {
                    setDirty(true);
                }
            }
        });
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label aopDescLabel = new Label(aopGroup, SWT.NONE);
        aopDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
                false, 1, 1));
        aopDescLabel.setText("*描述");

        aopDescText = new Text(aopGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
                | SWT.MULTI);
        aopDescText.setEnabled(false);
        // aopDescText.addVerifyListener(new VerifyListener() {
        // // 设置文本框输入限制
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopDescText.getText().length() < 128;
        // }
        // });
        // 当文本框被修改时，设置编辑器的状态为被修改
        aopDescText.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!isDirty())
                {
                    setDirty(true);
                }
            }
        });
        aopDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 4, 3));
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        unlockButton = new Button(aopGroup, SWT.NONE);
        GridData gd_clearBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false,
                1, 1);
        gd_clearBtn.widthHint = 80;
        unlockButton.setLayoutData(gd_clearBtn);
        unlockButton.setText("编辑");
        unlockButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                Button nButton = ((Button) e.getSource());
                if (nButton.getText() == "编辑")
                {
                    nButton.setText("锁定");
                    aopErrRecoverCombo.setEnabled(true);
                    unlock(list);
                } else
                {
                    nButton.setText("编辑");
                    aopErrRecoverCombo.setEnabled(false);
                    lock(list);
                }

            }
        });

        saveButton = new Button(aopGroup, SWT.NONE);
        saveButton.setEnabled(false);
        GridData gd_saveBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false,
                1, 1);
        gd_saveBtn.widthHint = 80;
        saveButton.setLayoutData(gd_saveBtn);
        saveButton.setText("保存");
        saveButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                try
                {
                    saveData();
                } catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        restoreButton = new Button(aopGroup, SWT.NONE);
        GridData gd_restoreButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_restoreButton.widthHint = 80;
        restoreButton.setLayoutData(gd_restoreButton);
        restoreButton.setText("恢复");
        // 设置恢复按钮行为
        restoreButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                aopNameText.setText(restoreMap.get("NAME"));
                aopDescText.setText(restoreMap.get("aopdesc"));
                aopExtsText.setText(restoreMap.get("aopexts"));
                preConditionText.setText(restoreMap.get("precondition"));
                postConditionText.setText(restoreMap.get("postcondition"));
                aopRetValText.setText(restoreMap.get("aopretval"));
                inputDataText.setText(restoreMap.get("inputdata"));
                outputDataText.setText(restoreMap.get("outputdata"));
                aopErrRecoverCombo.setText(ErrRecoverItem[Integer
                        .parseInt(restoreMap.get("aoperrrecover"))]);
                setDirty(false);
            }
        });
        new Label(aopGroup, SWT.NONE);
        addText(list);
        try
        {
            datainit(input.getName());
        } catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        scrolledComposite.setContent(composite);
        scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));
    }

    /**
     * 当编辑器选中的时候调用<br>
     * 当编辑器的窗口被选中时会自动调用这个方法，执行方法中的语句
     * 
     * @return 没有返回值
     * */
    @Override
    public void setFocus()
    {
        getSite().getShell().setText(
                "GOLP TOOL" + " " + "原子交易" + " " + input.getName() + " "
                        + "所属工程" + " " + upProjectText.getText());// 设置工具的标题
        setSelectNode(input.getSource());
        setPartName("原子交易" + " " + input.getName() + " " + "所属工程" + " "
                + upProjectText.getText());
    }

    /**
     * 对编辑器的控件进行初始化<br>
     * 首先找到数据库的配置信息，然后根据name与配置信息调用EditorAopServiceImpl类
     * 的queryAopByIdOrName方法得到初始化的数据放入Map，再一次填入控件中。
     * 
     * @param name
     *            要查询的数据的标识
     * @return 没有返回值
     * */
    private void datainit(String name) throws SQLException
    {
        // 获得数据库的配置信息
        ResourceLeafNode rln = ((AopEditorInput) input).getSource();
        String prjId = rln.getRootProject().getId();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot prjRoot = workspace.getRoot();
        IProject project = prjRoot.getProject(prjId);
        String dbfiles = project.getLocationURI().toString().substring(6)
                + File.separator + prjId + ".properties";
        DebugOut.println("dbfiles===" + dbfiles);
        ps = new PreferenceStore(dbfiles);
        try
        {
            ps.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        impl = new EditorAopServiceImpl();
        // 对数据库进行查询，放入Map
        map = impl.queryAopByIdOrName(name, "", ps);
        restoreMap = map;
        // 依次给控件赋值
        aopIdText.setText(map.get("ID"));
        aopNameText.setText(map.get("NAME"));
        aopDescText.setText(map.get("aopdesc"));
        aopErrRecoverCombo.setText(ErrRecoverItem[Integer.parseInt(map
                .get("aoperrrecover"))]);
        aopExtsText.setText(map.get("aopexts"));
        // 若AopLevelText为Constants.APP则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
        if (map.get("aoplevel").equals(Constants.APP))
            aopLevelText.setText("0-APP");
        else
        {
            aopLevelText.setText("1-GOLP");
            saveButton.setVisible(false);
            unlockButton.setVisible(false);
            restoreButton.setVisible(false);
        }
        upDllText.setText(map.get("updll"));
        aopRetValText.setText(map.get("aopretval"));
        preConditionText.setText(map.get("precondition"));
        postConditionText.setText(map.get("postcondition"));
        inputDataText.setText(map.get("inputdata"));
        outputDataText.setText(map.get("outputdata"));
        upProjectText.setText(input.getSource().getRootProject().getId());
        setDirty(false);
        saveButton.setEnabled(isDirty());
    }

    /**
     * 将控件设置为不可用<br>
     * 用for each循环将List中的空间依次设为不可用。
     * 
     * @param list
     *            要设置的空间的列表
     * @return 没有返回值
     * */
    private void lock(List<Control> list)
    {
        for (Control i : list)
        {
            i.setEnabled(false);
        }
    }

    /**
     * 将控件设置为可用<br>
     * 用for each循环将List中的空间依次设为可用。
     * 
     * @param list
     *            要设置的空间的列表
     * @return 没有返回值
     * */
    private void unlock(List<Control> list)
    {
        for (Control i : list)
            i.setEnabled(true);
    }

    /**
     * 保存数据的方法 用于保存按钮和doSave方法的保存操作，按下修改按钮，若原子交易名称文本框为空，则弹出警告对话框，
     * 否则将可编辑的文本框里的数据放入一个List中调用EditorAopServiceImpl类的updateAopById方法将
     * List里的数据替换标识为AopId的数据。
     * 
     * @throws SQLException
     */
    private void saveData() throws SQLException
    {
        if (aopNameText.getText().trim().isEmpty()
           || aopDescText.getText().trim().isEmpty()
           || aopRetValText.getText().trim().isEmpty()
           || inputDataText.getText().trim().isEmpty()
           || outputDataText.getText().trim().isEmpty()
           || aopErrRecoverCombo.getText().trim().isEmpty())
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "警告", "必填项不能为空");
        } 
        else
        {
            List<String> datalist = new ArrayList<String>();
            datalist.add(aopNameText.getText());
            datalist.add(aopDescText.getText());
            datalist.add(aopErrRecoverCombo.getText().substring(0, 1));
            datalist.add(preConditionText.getText());
            datalist.add(postConditionText.getText());
            datalist.add(aopRetValText.getText().trim());
            datalist.add(inputDataText.getText());
            datalist.add(outputDataText.getText());
            try
            {
                impl.updateAopById(aopIdText.getText(), datalist, ps);
            } catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            setDirty(false);
            restoreMap = impl.queryAopByIdOrName(aopIdText.getText(), "", ps);
        }
    }

    @Override
    public void dispose()
    {
        // 该编辑器被关闭后，恢复工具的标题
        if (getEditorSite().getPage().getActiveEditor() == null)
        {
            getSite().getShell().setText("GOLP TOOL");
        }
    }

    @Override
    public void setSelectNode(ResourceLeafNode node)
    {
        IViewPart view = getSite().getWorkbenchWindow().getActivePage()
                .findView(NavView.ID);
        if (view != null)
        {
            NavView v = (NavView) view;
            TreeViewer tv = v.getTreeViewer();
            StructuredSelection s = new StructuredSelection(node);
            tv.setSelection(s, true);
        }
    }

    @Override
    public String getTargetId()
    {
        return aopIdText.getText();
    }

    @Override
    public String getTargetName()
    {
        return aopNameText.getText();
    }

    @Override
    public void setTargetMap(Map<String, String> map)
    {
        this.map = map;
        this.restoreMap = map;
    }

    @Override
    public PreferenceStore getPs()
    {
        return ps;
    }

    @Override
    public void setUnLockButtonText(String text)
    {
        unlockButton.setText(text);
    }

    @Override
    public void setThisNode(ResourceLeafNode node)
    {
        input.setSource(node);
    }

    @Override
    public ResourceLeafNode getThisNode()
    {
        return input.getSource();
    }

    @Override
    public void setInputNode(ResourceLeafNode node)
    {
        input.setSource(node);
    }

    @Override
    public int showMessage(int style, String title, String message)
    {
        MessageBox box = new MessageBox(getSite().getShell(), style);
        box.setText(title);
        box.setMessage(message);
        return box.open();
    }

    @Override
    public Search getSearch()
    {
        return search;
    }

    @Override
    public void setControlsText()
    {
        aopIdText.setText(map.get("ID"));
        aopNameText.setText(map.get("NAME"));
        aopDescText.setText(map.get("aopdesc"));
        aopErrRecoverCombo.setText(ErrRecoverItem[Integer.parseInt(map
                .get("aoperrrecover"))]);
        aopExtsText.setText(map.get("aopexts"));
        /*
         * 判断AopLevelText是否为Constants.APP，是则设置为"APP"，将“解锁”于“修改”按钮设置为可见，
         * 否则为"GOLP",将“解锁”与“修改”按钮设置为不可见
         */
        if (map.get("aoplevel").equals(Constants.APP))
        {
            aopLevelText.setText("0-APP");
            saveButton.setVisible(true);
            unlockButton.setVisible(true);
            restoreButton.setVisible(true);
        } else
        {
            aopLevelText.setText("1-GOLP");
            saveButton.setVisible(false);
            unlockButton.setVisible(false);
            restoreButton.setVisible(false);
        }
        upDllText.setText(map.get("updll"));
        aopRetValText.setText(map.get("aopretval"));
        preConditionText.setText(map.get("precondition"));
        postConditionText.setText(map.get("postcondition"));
        input.setName(aopIdText.getText());
        setDirty(false);
    }

    @Override
    public void setEditorPartName(String name)
    {
        setPartName("原子交易" + aopIdText.getText() + "所属工程"
                + upProjectText.getText());
    }

    @Override
    public void setEnable(boolean b)
    {
        if (!b)
        {
            unlockButton.setText("编辑");
            aopErrRecoverCombo.setEnabled(false);
            lock(list);
        } else
        {
            unlockButton.setText("锁定");
            aopErrRecoverCombo.setEnabled(true);
            unlock(list);
        }

    }

    @Override
    public void save()
    {
        try
        {
            saveData();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getUpProject()
    {
        return upProjectText.getText();
    }
}
