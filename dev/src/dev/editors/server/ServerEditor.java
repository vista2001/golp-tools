/* 文件名：       ServerEditor.java
 * 修改人：       rxy
 * 修改时间：   2013.11.27
 * 修改内容：    1.将该源文件中出现的所有“SERVERSPECAOPDLLS”替换为
 *         “SERVERSPECINCLUDE”；
 *         2.将服务程序个性依赖宏定义、服务程序额外依赖目标文件加入到此编辑器中 ；
 *         3.将serverDescText的属性更改为SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI；
 *         4.增加此编辑器的滚动功能；
 *         5.在进行级别（APP/GOLP）的判断时，不再直接使用0、1等数字，改为使用dev.util.Constants类的常量；
 *         6.用DebugOut.println方法替换System.out.println方法。 
 */

package dev.editors.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import dev.db.service.EditorsServerServiceImpl;
import dev.editors.IGetUpProject;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;
import dev.model.base.ResourceLeafNode;
import dev.util.CommonUtil;
import dev.util.Constants;
import dev.views.NavView;

/**
 * 该类定义了服务程序所对应的编辑器
 */
public class ServerEditor extends EditorPart implements ISearch, IGetUpProject
{
    public static final String ID = "dev.editors.server.serverEditor";
    /**
     * 该ps用来装载工程对应的properties文件
     */
    private PreferenceStore ps;
    private ServerEditorInput input;
    /**
     * 该thisNode用来保存从ServerEditorInput获取到的ResourceLeafNode
     */
    private ResourceLeafNode thisNode;
    private boolean dirty = false;
    private String serverId;
    private String upProject;
    private Search search;
    /**
     * 该map用来接收查询或初始化编辑器时从数据库得到的服务程序对应的信息
     */
    private HashMap<String, String> map;
    private Text serverUpProjectText1;
    private Combo serverLevelCombo;
    private Text serverIdText1;
    private Text serverNameText1;
    private Text serverSpeclibPathText;
    private Text serverSpeclibNameText;
    private Text serverSpecIncludePathText;
    private Text serverSpecMarcoText;
    private Text callBackSourceText;
    private Text othFunSourceText;
    private Text serverDescText;
    private Button serverSpeclibPathButton;
    private Button serverSpeclibNameButton;
    private Button serverSpecIncludePathButton;
    private Button serverSpecMarcoButton;
    private Button serverSpecObjButton;
    private Text serverSpecObjText;
    private Button callBackSourceButton;
    private Button othFunSourceButton;
    private Button unLockButton;
    private Button saveButton;
    private Button recoverButton;
    /**
     * 该controls用来引用当前编辑器页面中，所有受“锁定/编辑”按钮控制的控件
     */
    private List<Control> controls;

    public Text getServerUpProjectText1()
    {
        return serverUpProjectText1;
    }

    public Text getServerIdText1()
    {
        return serverIdText1;
    }

    public Text getServerNameText1()
    {
        return serverNameText1;
    }

    /**
     * 当编辑器中的内容被修改后，手动调用该方法，其中设置了dirty标志位的状态、“保存”按钮是否可用并调用firePropertyChange()函数
     * 
     * @param dirty
     *            根据该值来进行具体设置
     */
    @Override
    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
        saveButton.setEnabled(dirty);
        recoverButton.setEnabled(dirty);
        firePropertyChange(PROP_DIRTY);
    }

    public ServerEditor()
    {
    }

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        save();
    }

    @Override
    public void doSaveAs()
    {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException
    {
        this.setSite(site);
        this.setInput(input);
        serverId = input.getName();
        thisNode = ((ServerEditorInput) input).getNode();
        this.input = (ServerEditorInput) input;
        // 初始化ps
        // initps();
        ps = CommonUtil.initPs(thisNode.getParent().getParent().getId());
        // 初始化该编辑器初次显示时，所应提供的数据
        initData();
        this.setPartName("服务程序" + " " + input.getName() + " " + "所属工程" + " "
                + upProject);
    }

    // /**
    // * 该方法先计算出该服务程序所属工程的配置文件路径，然后用该路径初始化ps,并加载ps
    // */
    // private void initps()
    // {
    // String prjId = thisNode.getParent().getParent().getId();
    // IWorkspace workspace = ResourcesPlugin.getWorkspace();
    // IWorkspaceRoot root = workspace.getRoot();
    // IProject project = root.getProject(prjId);
    // String propertyPath = project.getLocationURI().toString().substring(6) +
    // File.separator + prjId +".properties";
    // //DebugOut.println(propertyPath);
    // ps = new PreferenceStore(propertyPath);
    // try
    // {
    // ps.load();
    // }
    // catch (IOException e)
    // {
    // e.printStackTrace();
    // }
    // }

    @Override
    public boolean isDirty()
    {
        return dirty;
    }

    @Override
    public boolean isSaveAsAllowed()
    {
        return false;
    }

    /**
     * 该方法将编辑器页面中所有受“锁定/编辑”按钮控制的控件放入变量controls中
     */
    private void getControls()
    {
        controls = new ArrayList<Control>();
        controls.add(serverLevelCombo);
        controls.add(serverNameText1);
        controls.add(serverSpeclibPathText);
        controls.add(serverSpeclibPathButton);
        controls.add(serverSpeclibNameText);
        controls.add(serverSpeclibNameButton);
        controls.add(serverSpecIncludePathText);
        controls.add(serverSpecIncludePathButton);
        controls.add(serverSpecMarcoText);
        controls.add(serverSpecMarcoButton);
        controls.add(serverSpecObjText);
        controls.add(serverSpecObjButton);
        controls.add(callBackSourceText);
        controls.add(callBackSourceButton);
        controls.add(othFunSourceText);
        controls.add(othFunSourceButton);
        controls.add(serverDescText);
    }

    /**
     * 设置页面内所有受“锁定/编辑”按钮控制的控件的可用性
     * 
     * @param b
     *            若该值为true,则相应的按钮可用,false则相反
     */
    @Override
    public void setEnable(boolean b)
    {
        for (Control control : controls)
        {
            control.setEnabled(b);
        }
    }

    /**
     * 初始化该编辑器初次显示时，所应提供的数据
     */
    private void initData()
    {
        EditorsServerServiceImpl editorsServerServiceImpl = new EditorsServerServiceImpl();
        try
        {
            map = editorsServerServiceImpl.queryServerByIdOrName(serverId, 0, ps);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        upProject = thisNode.getParent().getParent().getId();

    }

    @Override
    public void createPartControl(Composite parent)
    {
        ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        Composite composite = new Composite(scrolledComposite, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));

        Group serverSearchGroup = new Group(composite, SWT.NONE);
        serverSearchGroup.setText("服务程序查询");
        serverSearchGroup.setLayout(new GridLayout(7, false));
        serverSearchGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 1, 1));

        search = new Search();
        search.setUpProject(upProject);
        search.setEditorId(ID);
        search.setEditor(this);
        search.createPartControl(serverSearchGroup);

        Group serverEditGroup = new Group(composite, SWT.NONE);
        serverEditGroup.setText("服务程序");
        serverEditGroup.setLayout(new GridLayout(7, false));
        serverEditGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 1));

        Label spaceLabel1 = new Label(serverEditGroup, SWT.NONE);
        GridData gd_spaceLabel1 = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 2, 1);
        gd_spaceLabel1.widthHint = 80;
        spaceLabel1.setLayoutData(gd_spaceLabel1);

        Label serverUpProjectLabel1 = new Label(serverEditGroup, SWT.NONE);
        serverUpProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverUpProjectLabel1.setText("所属工程");

        serverUpProjectText1 = new Text(serverEditGroup, SWT.BORDER);
        serverUpProjectText1.setEnabled(false);
        GridData gd_serverUpProjectText1 = new GridData(SWT.FILL, SWT.CENTER,
                false, false, 1, 1);
        gd_serverUpProjectText1.widthHint = 70;
        serverUpProjectText1.setLayoutData(gd_serverUpProjectText1);
        serverUpProjectText1.setText(upProject);

        Label spaceLabel = new Label(serverEditGroup, SWT.NONE);
        GridData gd_spaceLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_spaceLabel.widthHint = 80;
        spaceLabel.setLayoutData(gd_spaceLabel);

        Label serverLevelLabel = new Label(serverEditGroup, SWT.NONE);
        serverLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverLevelLabel.setText("服务程序级别");

        serverLevelCombo = new Combo(serverEditGroup, SWT.READ_ONLY);
        serverLevelCombo.setEnabled(false);
        GridData gd_serverLevelCombo = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 1, 1);
        gd_serverLevelCombo.widthHint = 53;
        serverLevelCombo.setLayoutData(gd_serverLevelCombo);
        serverLevelCombo.add("0-APP");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverIdLabel1 = new Label(serverEditGroup, SWT.NONE);
        serverIdLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        serverIdLabel1.setText("服务程序标识");

        serverIdText1 = new Text(serverEditGroup, SWT.BORDER);
        serverIdText1.setEnabled(false);
        GridData gd_serverIdText1 = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_serverIdText1.widthHint = 70;
        serverIdText1.setLayoutData(gd_serverIdText1);
        new Label(serverEditGroup, SWT.NONE);

        Label serverNameLabel1 = new Label(serverEditGroup, SWT.NONE);
        serverNameLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverNameLabel1.setText("*服务程序名称");

        serverNameText1 = new Text(serverEditGroup, SWT.BORDER);
        serverNameText1.setEnabled(false);
        GridData gd_serverNameText1 = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_serverNameText1.widthHint = 70;
        serverNameText1.setLayoutData(gd_serverNameText1);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpeclibPathLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpeclibPathLabel.setLayoutData(new GridData(SWT.RIGHT,
                SWT.CENTER, false, false, 1, 1));
        serverSpeclibPathLabel.setText("个性依赖库路径");

        serverSpeclibPathText = new Text(serverEditGroup, SWT.BORDER);
        serverSpeclibPathText.setEnabled(false);
        GridData gd_serverSpeclibPathText = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 3, 1);
        gd_serverSpeclibPathText.widthHint = 241;
        serverSpeclibPathText.setLayoutData(gd_serverSpeclibPathText);

        serverSpeclibPathButton = new Button(serverEditGroup, SWT.NONE);
        serverSpeclibPathButton.setEnabled(false);
        GridData gd_serverSpeclibPathButton = new GridData(SWT.LEFT,
                SWT.CENTER, false, false, 1, 1);
        gd_serverSpeclibPathButton.widthHint = 80;
        serverSpeclibPathButton.setLayoutData(gd_serverSpeclibPathButton);
        serverSpeclibPathButton.setText("选择");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpeclibNameLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpeclibNameLabel.setLayoutData(new GridData(SWT.RIGHT,
                SWT.CENTER, false, false, 1, 1));
        serverSpeclibNameLabel.setText("个性依赖库名称");

        serverSpeclibNameText = new Text(serverEditGroup, SWT.BORDER);
        serverSpeclibNameText.setEnabled(false);
        GridData gd_serverSpeclibNameText = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 3, 1);
        gd_serverSpeclibNameText.widthHint = 241;
        serverSpeclibNameText.setLayoutData(gd_serverSpeclibNameText);

        serverSpeclibNameButton = new Button(serverEditGroup, SWT.NONE);
        serverSpeclibNameButton.setEnabled(false);
        GridData gd_serverSpeclibNameButton = new GridData(SWT.LEFT,
                SWT.CENTER, false, false, 1, 1);
        gd_serverSpeclibNameButton.widthHint = 80;
        serverSpeclibNameButton.setLayoutData(gd_serverSpeclibNameButton);
        serverSpeclibNameButton.setText("选择");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpecIncludePathLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpecIncludePathLabel.setLayoutData(new GridData(SWT.RIGHT,
                SWT.CENTER, false, false, 1, 1));
        serverSpecIncludePathLabel.setText("个性依赖头文件路径");

        serverSpecIncludePathText = new Text(serverEditGroup, SWT.BORDER);
        serverSpecIncludePathText.setEnabled(false);
        GridData gd_serverSpecIncludePathText = new GridData(SWT.LEFT,
                SWT.CENTER, false, false, 3, 1);
        gd_serverSpecIncludePathText.widthHint = 241;
        serverSpecIncludePathText.setLayoutData(gd_serverSpecIncludePathText);

        serverSpecIncludePathButton = new Button(serverEditGroup, SWT.NONE);
        serverSpecIncludePathButton.setEnabled(false);
        GridData gd_serverSpecIncludePathButton = new GridData(SWT.LEFT,
                SWT.CENTER, false, false, 1, 1);
        gd_serverSpecIncludePathButton.widthHint = 80;
        serverSpecIncludePathButton
                .setLayoutData(gd_serverSpecIncludePathButton);
        serverSpecIncludePathButton.setText("选择");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpecMarcoLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpecMarcoLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverSpecMarcoLabel.setText("个性依赖宏定义");

        serverSpecMarcoText = new Text(serverEditGroup, SWT.BORDER);
        serverSpecMarcoText.setEnabled(false);
        GridData gd_serverSpecMarcoText = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 3, 1);
        gd_serverSpecMarcoText.widthHint = 241;
        serverSpecMarcoText.setLayoutData(gd_serverSpecMarcoText);

        serverSpecMarcoButton = new Button(serverEditGroup, SWT.NONE);
        serverSpecMarcoButton.setEnabled(false);
        GridData gd_serverSpecMarcoButton = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 1, 1);
        gd_serverSpecMarcoButton.widthHint = 80;
        serverSpecMarcoButton.setLayoutData(gd_serverSpecMarcoButton);
        serverSpecMarcoButton.setText("选择");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpecObjLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpecObjLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverSpecObjLabel.setText("额外依赖目标文件");

        serverSpecObjText = new Text(serverEditGroup, SWT.BORDER);
        serverSpecObjText.setEnabled(false);
        GridData gd_serverSpecObjText = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 3, 1);
        gd_serverSpecObjText.widthHint = 241;
        serverSpecObjText.setLayoutData(gd_serverSpecObjText);

        serverSpecObjButton = new Button(serverEditGroup, SWT.NONE);
        serverSpecObjButton.setEnabled(false);
        GridData gd_serverSpecObjButton = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 1, 1);
        gd_serverSpecObjButton.widthHint = 80;
        serverSpecObjButton.setLayoutData(gd_serverSpecObjButton);
        serverSpecObjButton.setText("选择");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label callBackSourceLabel = new Label(serverEditGroup, SWT.NONE);
        callBackSourceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        callBackSourceLabel.setText("回调的源程序");

        callBackSourceText = new Text(serverEditGroup, SWT.BORDER);
        callBackSourceText.setEnabled(false);
        GridData gd_callBackSourceText = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 3, 1);
        gd_callBackSourceText.widthHint = 241;
        callBackSourceText.setLayoutData(gd_callBackSourceText);

        callBackSourceButton = new Button(serverEditGroup, SWT.NONE);
        callBackSourceButton.setEnabled(false);
        GridData gd_callBackSourceButton = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 1, 1);
        gd_callBackSourceButton.widthHint = 80;
        callBackSourceButton.setLayoutData(gd_callBackSourceButton);
        callBackSourceButton.setText("选择");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label othFunSourceLabel = new Label(serverEditGroup, SWT.NONE);
        othFunSourceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        othFunSourceLabel.setText("其他函数程序");

        othFunSourceText = new Text(serverEditGroup, SWT.BORDER);
        othFunSourceText.setEnabled(false);
        GridData gd_othFunSourceText = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 3, 1);
        gd_othFunSourceText.widthHint = 241;
        othFunSourceText.setLayoutData(gd_othFunSourceText);

        othFunSourceButton = new Button(serverEditGroup, SWT.NONE);
        othFunSourceButton.setEnabled(false);
        GridData gd_othFunSourceButton = new GridData(SWT.LEFT, SWT.CENTER,
                false, false, 1, 1);
        gd_othFunSourceButton.widthHint = 80;
        othFunSourceButton.setLayoutData(gd_othFunSourceButton);
        othFunSourceButton.setText("选择");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverDescLabel = new Label(serverEditGroup, SWT.NONE);
        serverDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverDescLabel.setText("*描述");

        serverDescText = new Text(serverEditGroup, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        serverDescText.setEnabled(false);
        GridData gd_serverDescText = new GridData(SWT.LEFT, SWT.FILL, false,
                false, 4, 3);
        gd_serverDescText.widthHint = 307;
        serverDescText.setLayoutData(gd_serverDescText);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        unLockButton = new Button(serverEditGroup, SWT.NONE);
        GridData gd_unLockButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_unLockButton.widthHint = 80;
        unLockButton.setLayoutData(gd_unLockButton);
        unLockButton.setText("编辑");

        unLockButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {

                if (unLockButton.getText().equals("编辑"))
                {
                    setEnable(true);
                    unLockButton.setText("锁定");
                } else
                {
                    setEnable(false);
                    unLockButton.setText("编辑");
                }
            }
        });

        saveButton = new Button(serverEditGroup, SWT.NONE);
        saveButton.setEnabled(false);
        GridData gd_saveButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_saveButton.widthHint = 80;
        saveButton.setLayoutData(gd_saveButton);
        saveButton.setText("保存");

        saveButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                save();
            }
        });

        recoverButton = new Button(serverEditGroup, SWT.NONE);
        recoverButton.setEnabled(false);
        GridData gd_recoverButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_recoverButton.widthHint = 80;
        recoverButton.setLayoutData(gd_recoverButton);
        recoverButton.setText("恢复");

        recoverButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setControlsText();
                setDirty(false);
            }
        });
        new Label(serverEditGroup, SWT.NONE);

        serverNameText1.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        serverSpeclibPathText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        serverSpeclibNameText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        serverSpecIncludePathText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        serverSpecMarcoText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        serverSpecObjText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        callBackSourceText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        othFunSourceText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        serverDescText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                setDirty(true);
            }
        });

        serverSpeclibPathButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ServerSpeclibPathDialog serverSpeclibPathDialog = new ServerSpeclibPathDialog(
                        e.display.getActiveShell(), e.getSource(),
                        serverSpeclibPathText.getText().trim(), serverUpProjectText1.getText());
                serverSpeclibPathDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((ServerSpeclibPathDialog) dm
                                        .getdm()).listForReturn;
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
                                serverSpeclibPathText.setText(s);
                            }
                        });
                serverSpeclibPathDialog.open();
            }
        });

        serverSpeclibNameButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ServerSpeclibNameDialog serverSpeclibNameDialog = new ServerSpeclibNameDialog(
                        e.display.getActiveShell(), e.getSource(),
                        serverSpeclibNameText.getText().trim());
                serverSpeclibNameDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((ServerSpeclibNameDialog) dm
                                        .getdm()).listForReturn;
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
                                serverSpeclibNameText.setText(s);
                            }
                        });
                serverSpeclibNameDialog.open();
            }
        });

        serverSpecIncludePathButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ServerSpecIncludePathDialog serverSpecIncludePathDialog = new ServerSpecIncludePathDialog(
                        e.display.getActiveShell(), e.getSource(),
                        serverSpecIncludePathText.getText().trim(), serverUpProjectText1.getText());
                serverSpecIncludePathDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((ServerSpecIncludePathDialog) dm
                                        .getdm()).listForReturn;
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
                                serverSpecIncludePathText.setText(s);
                            }
                        });
                serverSpecIncludePathDialog.open();
            }
        });

        serverSpecMarcoButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ServerSpecMarcoDialog serverSpecMarcoDialog = new ServerSpecMarcoDialog(
                        e.display.getActiveShell(), e.getSource(),
                        serverSpecMarcoText.getText().trim());
                serverSpecMarcoDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((ServerSpecMarcoDialog) dm
                                        .getdm()).listForReturn;
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
                                serverSpecMarcoText.setText(s);
                            }
                        });
                serverSpecMarcoDialog.open();
            }
        });

        serverSpecObjButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ServerSpecObjDialog serverSpecObjDialog = new ServerSpecObjDialog(
                        e.display.getActiveShell(), e.getSource(),
                        serverSpecObjText.getText().trim(), serverUpProjectText1.getText());
                serverSpecObjDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((ServerSpecObjDialog) dm
                                        .getdm()).listForReturn;
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
                                serverSpecObjText.setText(s);
                            }
                        });
                serverSpecObjDialog.open();
            }
        });

        callBackSourceButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                CallBackSourceDialog callBackSourceDialog = new CallBackSourceDialog(
                        e.display.getActiveShell(), e.getSource(),
                        callBackSourceText.getText().trim(), serverUpProjectText1.getText());
                callBackSourceDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((CallBackSourceDialog) dm
                                        .getdm()).listForReturn;
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
                                callBackSourceText.setText(s);
                            }
                        });
                callBackSourceDialog.open();
            }
        });

        othFunSourceButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                OthFunSourceDialog othFunSourceDialog = new OthFunSourceDialog(
                        e.display.getActiveShell(), e.getSource(),
                        othFunSourceText.getText().trim(), serverUpProjectText1.getText());
                othFunSourceDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((OthFunSourceDialog) dm
                                        .getdm()).listForReturn;
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
                                othFunSourceText.setText(s);
                            }
                        });
                othFunSourceDialog.open();
            }
        });

        scrolledComposite.setContent(composite);
        scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));

        getControls();
        setControlsText();

    }

    /**
     * 用成员变量map中的值，填充页面的控件
     */
    @Override
    public void setControlsText()
    {
        if (map.get("SERVERLEVEL").equals(Constants.APP))
        {
            serverLevelCombo.setText("0-APP");
        } else if (map.get("SERVERLEVEL").equals(Constants.GOLP))
        {
            serverLevelCombo.setText("1-GOLP");
            unLockButton.setEnabled(false);
        }
        // serverIdText1.setText(map.get("SERVERID"));
        serverIdText1.setText(map.get("ID"));
        // serverNameText1.setText(map.get("SERVERNAME"));
        serverNameText1.setText(map.get("NAME"));
        serverSpeclibPathText.setText(map.get("SERVERSPECLIBPATH"));
        serverSpeclibNameText.setText(map.get("SERVERSPECLIBNAME"));
        serverSpecIncludePathText.setText(map.get("SERVERSPECINCLUDE"));
        serverSpecMarcoText.setText(map.get("SERVERSPECMARCO"));
        serverSpecObjText.setText(map.get("SERVERSPECOBJ"));
        callBackSourceText.setText(map.get("CALLBACKSOURCE"));
        othFunSourceText.setText(map.get("OTHFUNSOURCE"));
        serverDescText.setText(map.get("SERVERDESC"));
        setDirty(false);
    }

    @Override
    public void setFocus()
    {
        getSite().getShell().setText(
                "GOLP TOOL" + " " + "服务程序" + " " + getServerIdText1().getText()
                        + " " + "所属工程" + " " + upProject);
        // 在导航视图中，将thisNode设为焦点
        setSelectNode(thisNode);
    }

    /**
     * 根据传入的node，将其在导航视图中设为焦点
     * 
     * @param node 想要设为焦点的ResourceLeafNode对象
     */
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

    /**
     * 根据传入的参数，弹出需要的对话框
     * 
     * @param style
     *            对话框的风格
     * @param title
     *            对话框的标题
     * @param message
     *            对话框的内容
     * @return 用户在对话框中所点击的按钮的值
     */
    @Override
    public int showMessage(int style, String title, String message)
    {
        MessageBox box = new MessageBox(getSite().getShell(), style);
        box.setText(title);
        box.setMessage(message);
        return box.open();
    }

    /**
     * 实现了保存的操作
     */
    @Override
    public void save()
    {
        if (serverNameText1.getText().trim().isEmpty()
                || serverDescText.getText().trim().isEmpty())
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "警告", "必填项不能为空");
            return;
        }
        if ((serverSpeclibPathText.getText().isEmpty() && serverSpeclibNameText
                .getText().isEmpty())
                || (!serverSpeclibPathText.getText().isEmpty() && !serverSpeclibNameText
                        .getText().isEmpty()))
        {
            List<String> dataList = new ArrayList<String>();
            dataList.add(serverNameText1.getText());
            dataList.add(serverDescText.getText());
            String serverSpeclib = "";
            if (!serverSpeclibPathText.getText().isEmpty())
            {
                serverSpeclib = "[" + serverSpeclibPathText.getText() + "]"
                        + "[" + serverSpeclibNameText.getText() + "]";
            }
            dataList.add(serverSpeclib);
            dataList.add(serverSpecIncludePathText.getText());
            dataList.add(callBackSourceText.getText());
            dataList.add(othFunSourceText.getText());
            dataList.add(serverSpecMarcoText.getText());
            dataList.add(serverSpecObjText.getText());
            dataList.add(serverIdText1.getText());
            EditorsServerServiceImpl editorsServerServiceImpl = new EditorsServerServiceImpl();
            try
            {
                editorsServerServiceImpl.updateServerById(dataList, ps);
                map = editorsServerServiceImpl.queryServerByIdOrName(
                        serverIdText1.getText(), 0, ps);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            setDirty(false);
        } else
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "警告",
                    "个性依赖库路径和个性依赖库名称必须全部输入或全部为空");
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

    // 以下为实现ISearch接口中的方法
    @Override
    public String getTargetId()
    {
        return serverIdText1.getText();
    }

    @Override
    public String getTargetName()
    {
        return serverNameText1.getText();
    }

    @Override
    public void setTargetMap(Map<String, String> map)
    {
        this.map = (HashMap<String, String>) map;
    }

    @Override
    public PreferenceStore getPs()
    {
        return ps;
    }

    // @Override
    // public void setSaveButtonEnable(boolean b)
    // {
    // saveButton.setEnabled(b);
    // }

    @Override
    public void setUnLockButtonText(String text)
    {
        unLockButton.setText(text);
    }

    @Override
    public void setThisNode(ResourceLeafNode node)
    {
        thisNode = node;
    }

    @Override
    public void setInputNode(ResourceLeafNode node)
    {
        input.setNode(node);
    }

    @Override
    public Search getSearch()
    {
        return search;
    }

    @Override
    public void setEditorPartName(String name)
    {
        this.setPartName(name);
    }

    @Override
    public ResourceLeafNode getThisNode()
    {
        return thisNode;
    }

    @Override
    public String getUpProject()
    {
        return serverUpProjectText1.getText();
    }
}
