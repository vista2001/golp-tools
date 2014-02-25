/* 文件名：       retcodeEditor.java
 * 修改人：       rxy
 * 修改时间：   2013.11.29
 * 修改内容：   1.将dllDesc的属性更改为SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI；
 *         2.修改init方法中的setPartName，增加对所属工程的显示，这样就解决了从导航中打开一个编辑器后，点击该编辑器
 *         的关闭按钮时，编辑器并未关闭，而是在编辑器的标题中显示了更多的内容，产生该问题的原因在于init方法和setFocus
 *         方法在调用setPartName时参数不一致；
 *         3.修改了部分布局；
 *         4.覆盖父类中的dispose方法，使得在所有编辑器都关闭时，恢复工具的标题“GOLP TOOL”；
 *         5.在进行级别（APP/GOLP）的判断时，不再直接使用0、1等数字，改为使用dev.util.Constants类的常量；
 *         6.在UI中统一使用0-APP、1-GOLP这种表示；
 *         7.用DebugOut.println方法替换System.out.println方法；
 *         8.增加编辑器的滚动功能；
 *         9.统一使用File.separator；
 *         10.增加对动态库路径的处理；
 *         11.增加访问远程目录的功能。
 * 修改人：       zxh
 * 修改时间：   2013.12.2
 * 修改内容：   1.修改变量命名
 */

package dev.editors.aopDll;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import dev.db.service.EditorAopDllServiceImpl;
import dev.editors.IGetUpProject;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.remote.RemoteDialog;
import dev.util.Constants;
import dev.util.DebugOut;
import dev.views.NavView;

/**
 * AopDll表编辑器类
 * <p>
 * 这个类继承了EditorPart类，与AopDllEditorInput类一起完成AopDll编辑器的功能<br>
 * 在编辑器初始化的时候，通过AopDllEditorInput类传入的数据在Init方法中对编辑器
 * 进行初始化，然后在createPartControl方法中完成对编辑器的控件的构造，包括设
 * 置控件（主要是“查询”“解锁”“修改”按钮）的行为和文本框的输入限制，在createPartControl
 * 方法的最后调用dataInit方法完成从数据库中获得数据并填入文本框中，完成对整个 编辑器的初始化。
 * 
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class AopDllEditor extends EditorPart implements ISearch, IGetUpProject
{
    public AopDllEditor()
    {
    }

    public static final String ID = "dev.editor.AopDll.AopDllEditor"; // 编辑器类的标识
    private Text upProjectText; // 表项部分所属工程文本框
    private Text dllLevelText; // 原子交易库级别文本框
    private Text dllIdText; // 原子交易库标识文本框
    private Text dllTypeText; // 原子交易库类型文本框
    private Text dllNameText; // 原子交易库名称文本框
    private Text dllDescText; // 原子交易库说明文本框
    private Button saveButton; // 修改按钮
    private Button unlockButton; // 解锁按钮
    private Button restoreButton; // 恢复按钮
    private AopDllEditorInput input; // Input对象
    private EditorAopDllServiceImpl impl; // 数据库操作类对象
    private PreferenceStore ps; // 数据库配置信息
    private Map<String, String> map; // 存储查询的数据的Map
    private Map<String, String> restoreMap; // 恢复按钮的数据的Map
    private boolean bDirty = false; // 是否修改的标识
    private Search search;
    private Text dllPathText;
    private Button dllPathButton;

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        try
        {
            saveDate();
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
        this.input = (AopDllEditorInput) input; // 对Input初始化
        this.setPartName("动态库" + " " + this.input.getName() + " " + "所属工程"
                + " " + this.input.getSource().getRootProject().getId()); // 设置编辑器标题
    }

    @Override
    public boolean isDirty()
    {
        return bDirty;
    }

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
        Group searchDllGroup = new Group(composite, SWT.NONE);
        searchDllGroup.setText("动态库查询");
        searchDllGroup.setLayout(new GridLayout(7, false));
        searchDllGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 1, 1));

        search = new Search();
        search.setUpProject(input.getSource().getRootProject().getId());
        search.setEditorId(ID);
        search.setEditor(this);
        search.createPartControl(searchDllGroup);

        Group dllGroup = new Group(composite, SWT.NONE);
        GridData gd_dllGroup = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                1);
        gd_dllGroup.widthHint = 545;
        dllGroup.setLayoutData(gd_dllGroup);
        dllGroup.setLayout(new GridLayout(8, false));
        dllGroup.setText("原子交易库");
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);

        Label lblNewLabel_1 = new Label(dllGroup, SWT.NONE);
        GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 2, 1);
        gd_lblNewLabel_1.widthHint = 80;
        lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);

        Label upProjectLabel = new Label(dllGroup, SWT.NONE);
        upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        upProjectLabel.setText("所属工程");

        upProjectText = new Text(dllGroup, SWT.BORDER);
        upProjectText.setEnabled(false);
        GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_upProject.widthHint = 70;
        upProjectText.setLayoutData(gd_upProject);

        Label lblNewLabel = new Label(dllGroup, SWT.NONE);
        GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 2, 1);
        gd_lblNewLabel.widthHint = 80;
        lblNewLabel.setLayoutData(gd_lblNewLabel);

        Label dllLevelLabel = new Label(dllGroup, SWT.NONE);
        dllLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        dllLevelLabel.setText("动态库级别");

        dllLevelText = new Text(dllGroup, SWT.BORDER);
        dllLevelText.setEnabled(false);
        GridData gd_dllLevel = new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1);
        gd_dllLevel.widthHint = 70;
        dllLevelText.setLayoutData(gd_dllLevel);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);

        Label dllIdLabel = new Label(dllGroup, SWT.NONE);
        dllIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        dllIdLabel.setText("动态库标识");

        dllIdText = new Text(dllGroup, SWT.BORDER);
        dllIdText.setEnabled(false);
        GridData gd_dllId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
                1);
        gd_dllId.widthHint = 70;
        dllIdText.setLayoutData(gd_dllId);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);

        Label dllTypeLabel = new Label(dllGroup, SWT.NONE);
        dllTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        dllTypeLabel.setText("动态库类型");

        dllTypeText = new Text(dllGroup, SWT.BORDER);
        dllTypeText.setEnabled(false);
        GridData gd_dllType = new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1);
        gd_dllType.widthHint = 70;
        dllTypeText.setLayoutData(gd_dllType);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);

        Label dllNameLabel = new Label(dllGroup, SWT.NONE);
        dllNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        dllNameLabel.setText("*动态库名称");

        dllNameText = new Text(dllGroup, SWT.BORDER);
        dllNameText.setEnabled(false);
        GridData gd_dllName = new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1);
        gd_dllName.widthHint = 70;
        dllNameText.setLayoutData(gd_dllName);
//        dllNameText.addVerifyListener(new VerifyListener()
//        {
//            // 设置文本框的输入限制
//            @Override
//            public void verifyText(VerifyEvent e)
//            {
//                if (e.character != 8)
//                    e.doit = dllNameText.getText().length() <= 32;
//            }
//        });
        dllNameText.addModifyListener(new ModifyListener()
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
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        
        Label dllPathLabel = new Label(dllGroup, SWT.NONE);
        dllPathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        dllPathLabel.setText("*动态库路径");
        
        dllPathText = new Text(dllGroup, SWT.BORDER);
        dllPathText.setEnabled(false);
        dllPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
        dllPathText.addModifyListener(new ModifyListener()
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
        
        dllPathButton = new Button(dllGroup, SWT.NONE);
        dllPathButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ArrayList<String> paths = new ArrayList<String>();
                RemoteDialog remoteDialog = new RemoteDialog(getSite().getShell(),
                        upProjectText.getText(), null, RemoteDialog.REMOTEDIALOG_FILE,
                        RemoteDialog.REMOTEDIALOG_SINGLE, paths);
                remoteDialog.open();
                if(paths.size() == 1)
                {
                    dllPathText.setText(paths.get(0));
                }
            }
        });
        dllPathButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        dllPathButton.setEnabled(false);
        dllPathButton.setText("选择");
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        
        Label dllDescLabel = new Label(dllGroup, SWT.NONE);
        dllDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
                false, 1, 1));
        dllDescLabel.setText("*描述");

        dllDescText = new Text(dllGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
                | SWT.MULTI);
        dllDescText.setEnabled(false);
        GridData gd_dllDesc = new GridData(SWT.FILL, SWT.FILL, false, false, 5,
                3);
        gd_dllDesc.widthHint = 81;
        gd_dllDesc.heightHint = 29;
        dllDescText.setLayoutData(gd_dllDesc);
        // dllDescText.addVerifyListener(new VerifyListener()
        // {
        // // 设置文本框的输入限制
        // @Override
        // public void verifyText(VerifyEvent e)
        // {
        // if (e.character != 8)
        // e.doit = dllNameText.getText().length() <= 128;
        // }
        // });
        dllDescText.addModifyListener(new ModifyListener()
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
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        /*
         * 为“解锁”按钮设置行为，当按下按钮时，若按钮为“解锁”，则改为锁定，并将可以
         * 编辑的控件设置为可用，将“修改”按钮设置为可用；否则改为解锁，将可以编辑的 控件设置为不可用，将修改按钮设置为不可用。
         */
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        /*
         * 为“修改”按钮设置行为，按下修改按钮，若动态库名称文本框为空，则弹出警告对话框，
         * 否则将可编辑的文本框里的数据放入一个List中调用EditorAopDllServiceImpl类的
         * updateAopDllById方法将List里的数据替换标识为AopId 的数据。
         */
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        
        unlockButton = new Button(dllGroup, SWT.NONE);
        GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 2, 1);
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
                    dllNameText.setEnabled(true);
                    dllDescText.setEnabled(true);
                    dllPathText.setEnabled(true);
                    dllPathButton.setEnabled(true);
                } else
                {
                    nButton.setText("编辑");
                    dllNameText.setEnabled(false);
                    dllDescText.setEnabled(false);
                    dllPathText.setEnabled(false);
                    dllPathButton.setEnabled(false);
                }

            }
        });
        
        saveButton = new Button(dllGroup, SWT.NONE);
        GridData gd_saveBtn = new GridData(SWT.CENTER, SWT.CENTER, false,
                false, 1, 1);
        gd_saveBtn.widthHint = 80;
        saveButton.setLayoutData(gd_saveBtn);
        saveButton.setText("保存");
        saveButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                // super.widgetSelected(e);
                try
                {
                    saveDate();
                } catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        restoreButton = new Button(dllGroup, SWT.NONE);
        GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_restoreBtn.widthHint = 80;
        restoreButton.setLayoutData(gd_restoreBtn);
        restoreButton.setText("恢复");
        // 设置恢复按钮行为
        restoreButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                // super.widgetSelected(e);
                dllNameText.setText(restoreMap.get("NAME"));
                dllDescText.setText(restoreMap.get("dlldesc"));
                dllPathText.setText(restoreMap.get("dllpath"));
                setDirty(false);
            }
        });
        new Label(dllGroup, SWT.NONE);
        
        scrolledComposite.setContent(composite);
        scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));
        
        try
        {
            datainit(input.getName());
        } catch (SQLException e1)
        {
            e1.printStackTrace();
        }
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
                "GOLP TOOL" + " " + "动态库" + " " + input.getName() + " " + "所属工程"
                        + " " + upProjectText.getText());// 设置工具的名称
        setPartName("动态库" + " " + input.getName() + " " + "所属工程" + " " + upProjectText.getText());
        setSelectNode(input.getSource());
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
        ResourceLeafNode rln = ((AopDllEditorInput) input).getSource();
        String prjId = rln.getRootProject().getId();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot prjRoot = workspace.getRoot();
        IProject project = prjRoot.getProject(prjId);
        String dbfiles = project.getLocationURI().toString().substring(6) + File.separator
                + prjId + ".properties";
        DebugOut.println("dbfiles===" + dbfiles);
        ps = new PreferenceStore(dbfiles);
        try
        {
            ps.load();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        // 对数据库进行查询，放入Map
        impl = new EditorAopDllServiceImpl();
        map = impl.queryAopDllByIdOrName(name, "", ps);
        restoreMap = map;
        // 依次给控件赋值
        dllIdText.setText(map.get("ID"));
        dllDescText.setText(map.get("dlldesc"));
        dllNameText.setText(map.get("NAME"));
        dllTypeText.setText(map.get("dlltype"));
        dllPathText.setText(map.get("dllpath"));
        // 若aoplevel为Constants.APP则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
        if (map.get("dlllevel").equals(Constants.APP))
        	dllLevelText.setText("0-APP");
        else
        {
        	dllLevelText.setText("1-GOLP");
        	saveButton.setVisible(false);
        	unlockButton.setVisible(false);
        	restoreButton.setVisible(false);
        }
        upProjectText.setText(input.getSource().getRootProject().getId());
        // upProject1.setText(upProject.getText());
        setDirty(false);
    }

    /**
     * 保存数据的方法 用于保存按钮和doSave方法的保存操作，按下修改按钮，若原子交易名称文本框为空，则弹出警告对话框，
     * 否则将可编辑的文本框里的数据放入一个List中调用EditorDllServiceImpl类的updateDllById方法将
     * List里的数据替换标识为DllId的数据。
     * 
     * @throws SQLException
     */
    private void saveDate() throws SQLException
    {
        if (dllNameText.getText().trim().isEmpty()
           || dllDescText.getText().trim().isEmpty()
           || dllPathText.getText().isEmpty())
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "警告", "必填项不能为空");
        }
        else
        {
            List<String> datalist = new ArrayList<String>();
            datalist.add(dllNameText.getText());
            datalist.add(dllDescText.getText());
            datalist.add(dllPathText.getText());
            try
            {
                impl.updateAopDllById(dllIdText.getText(), datalist, ps);
            } catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            restoreMap = impl.queryAopDllByIdOrName(dllIdText.getText(), "", ps);
            setDirty(false);
        }
    }

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
        return dllIdText.getText();
    }

    @Override
    public String getTargetName()
    {
        return dllNameText.getText();
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
    	dllIdText.setText(map.get("ID"));
    	dllDescText.setText(map.get("dlldesc"));
        dllNameText.setText(map.get("NAME"));
        dllTypeText.setText(map.get("dlltype"));
        dllPathText.setText(map.get("dllpath"));
        /*
         * 判断dlllevel是否为Constants.APP，是则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
         * 否则为"GOLP",将“解锁”与“修改”按钮设置为不可见
         */
        if (map.get("dlllevel").equals(Constants.APP))
        {
        	dllLevelText.setText("0-APP");
        	saveButton.setVisible(true);
        	unlockButton.setVisible(true);
        	restoreButton.setVisible(true);
        } else
        {
        	dllLevelText.setText("1-GOLP");
        	saveButton.setVisible(false);
        	unlockButton.setVisible(false);
        	restoreButton.setVisible(false);
        }
        input.setName(dllIdText.getText());
    }

    @Override
    public void setEditorPartName(String name)
    {
        setPartName("动态库" + dllIdText.getText() + "所属工程" + upProjectText.getText());
    }

    @Override
    public void setEnable(boolean b)
    {
        if (!b)
        {
        	unlockButton.setText("编辑");
            dllNameText.setEnabled(false);
            dllDescText.setEnabled(false);
            dllPathText.setEnabled(false);
            dllPathButton.setEnabled(false);
            // setDirty(false);
        } 
        else
        {
        	unlockButton.setText("锁定");
            dllNameText.setEnabled(true);
            dllDescText.setEnabled(true);
            dllPathText.setEnabled(true);
            dllPathButton.setEnabled(true);
            // setDirty(false);
        }

    }

    @Override
    public void save()
    {
        try
        {
            saveDate();
        } catch (SQLException e)
        {
            e.printStackTrace();
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
    public String getUpProject()
    {
        return upProjectText.getText();
    }
}
