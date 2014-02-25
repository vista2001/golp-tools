/* �ļ�����       ServerEditor.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.27
 * �޸����ݣ�    1.����Դ�ļ��г��ֵ����С�SERVERSPECAOPDLLS���滻Ϊ
 *         ��SERVERSPECINCLUDE����
 *         2.�����������������궨�塢��������������Ŀ���ļ����뵽�˱༭���� ��
 *         3.��serverDescText�����Ը���ΪSWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI��
 *         4.���Ӵ˱༭���Ĺ������ܣ�
 *         5.�ڽ��м���APP/GOLP�����ж�ʱ������ֱ��ʹ��0��1�����֣���Ϊʹ��dev.util.Constants��ĳ�����
 *         6.��DebugOut.println�����滻System.out.println������ 
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
 * ���ඨ���˷����������Ӧ�ı༭��
 */
public class ServerEditor extends EditorPart implements ISearch, IGetUpProject
{
    public static final String ID = "dev.editors.server.serverEditor";
    /**
     * ��ps����װ�ع��̶�Ӧ��properties�ļ�
     */
    private PreferenceStore ps;
    private ServerEditorInput input;
    /**
     * ��thisNode���������ServerEditorInput��ȡ����ResourceLeafNode
     */
    private ResourceLeafNode thisNode;
    private boolean dirty = false;
    private String serverId;
    private String upProject;
    private Search search;
    /**
     * ��map�������ղ�ѯ���ʼ���༭��ʱ�����ݿ�õ��ķ�������Ӧ����Ϣ
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
     * ��controls�������õ�ǰ�༭��ҳ���У������ܡ�����/�༭����ť���ƵĿؼ�
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
     * ���༭���е����ݱ��޸ĺ��ֶ����ø÷���������������dirty��־λ��״̬�������桱��ť�Ƿ���ò�����firePropertyChange()����
     * 
     * @param dirty
     *            ���ݸ�ֵ�����о�������
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
        // ��ʼ��ps
        // initps();
        ps = CommonUtil.initPs(thisNode.getParent().getParent().getId());
        // ��ʼ���ñ༭��������ʾʱ����Ӧ�ṩ������
        initData();
        this.setPartName("�������" + " " + input.getName() + " " + "��������" + " "
                + upProject);
    }

    // /**
    // * �÷����ȼ�����÷�������������̵������ļ�·����Ȼ���ø�·����ʼ��ps,������ps
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
     * �÷������༭��ҳ���������ܡ�����/�༭����ť���ƵĿؼ��������controls��
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
     * ����ҳ���������ܡ�����/�༭����ť���ƵĿؼ��Ŀ�����
     * 
     * @param b
     *            ����ֵΪtrue,����Ӧ�İ�ť����,false���෴
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
     * ��ʼ���ñ༭��������ʾʱ����Ӧ�ṩ������
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
        serverSearchGroup.setText("��������ѯ");
        serverSearchGroup.setLayout(new GridLayout(7, false));
        serverSearchGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 1, 1));

        search = new Search();
        search.setUpProject(upProject);
        search.setEditorId(ID);
        search.setEditor(this);
        search.createPartControl(serverSearchGroup);

        Group serverEditGroup = new Group(composite, SWT.NONE);
        serverEditGroup.setText("�������");
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
        serverUpProjectLabel1.setText("��������");

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
        serverLevelLabel.setText("������򼶱�");

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
        serverIdLabel1.setText("��������ʶ");

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
        serverNameLabel1.setText("*�����������");

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
        serverSpeclibPathLabel.setText("����������·��");

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
        serverSpeclibPathButton.setText("ѡ��");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpeclibNameLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpeclibNameLabel.setLayoutData(new GridData(SWT.RIGHT,
                SWT.CENTER, false, false, 1, 1));
        serverSpeclibNameLabel.setText("��������������");

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
        serverSpeclibNameButton.setText("ѡ��");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpecIncludePathLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpecIncludePathLabel.setLayoutData(new GridData(SWT.RIGHT,
                SWT.CENTER, false, false, 1, 1));
        serverSpecIncludePathLabel.setText("��������ͷ�ļ�·��");

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
        serverSpecIncludePathButton.setText("ѡ��");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpecMarcoLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpecMarcoLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverSpecMarcoLabel.setText("���������궨��");

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
        serverSpecMarcoButton.setText("ѡ��");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverSpecObjLabel = new Label(serverEditGroup, SWT.NONE);
        serverSpecObjLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverSpecObjLabel.setText("��������Ŀ���ļ�");

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
        serverSpecObjButton.setText("ѡ��");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label callBackSourceLabel = new Label(serverEditGroup, SWT.NONE);
        callBackSourceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        callBackSourceLabel.setText("�ص���Դ����");

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
        callBackSourceButton.setText("ѡ��");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label othFunSourceLabel = new Label(serverEditGroup, SWT.NONE);
        othFunSourceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        othFunSourceLabel.setText("������������");

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
        othFunSourceButton.setText("ѡ��");
        new Label(serverEditGroup, SWT.NONE);
        new Label(serverEditGroup, SWT.NONE);

        Label serverDescLabel = new Label(serverEditGroup, SWT.NONE);
        serverDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
                false, false, 1, 1));
        serverDescLabel.setText("*����");

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
        unLockButton.setText("�༭");

        unLockButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {

                if (unLockButton.getText().equals("�༭"))
                {
                    setEnable(true);
                    unLockButton.setText("����");
                } else
                {
                    setEnable(false);
                    unLockButton.setText("�༭");
                }
            }
        });

        saveButton = new Button(serverEditGroup, SWT.NONE);
        saveButton.setEnabled(false);
        GridData gd_saveButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_saveButton.widthHint = 80;
        saveButton.setLayoutData(gd_saveButton);
        saveButton.setText("����");

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
        recoverButton.setText("�ָ�");

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
     * �ó�Ա����map�е�ֵ�����ҳ��Ŀؼ�
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
                "GOLP TOOL" + " " + "�������" + " " + getServerIdText1().getText()
                        + " " + "��������" + " " + upProject);
        // �ڵ�����ͼ�У���thisNode��Ϊ����
        setSelectNode(thisNode);
    }

    /**
     * ���ݴ����node�������ڵ�����ͼ����Ϊ����
     * 
     * @param node ��Ҫ��Ϊ�����ResourceLeafNode����
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
     * ���ݴ���Ĳ�����������Ҫ�ĶԻ���
     * 
     * @param style
     *            �Ի���ķ��
     * @param title
     *            �Ի���ı���
     * @param message
     *            �Ի��������
     * @return �û��ڶԻ�����������İ�ť��ֵ
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
     * ʵ���˱���Ĳ���
     */
    @Override
    public void save()
    {
        if (serverNameText1.getText().trim().isEmpty()
                || serverDescText.getText().trim().isEmpty())
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
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
            showMessage(SWT.ICON_WARNING | SWT.YES, "����",
                    "����������·���͸������������Ʊ���ȫ�������ȫ��Ϊ��");
        }
    }

    @Override
    public void dispose()
    {
        // �ñ༭�����رպ󣬻ָ����ߵı���
        if (getEditorSite().getPage().getActiveEditor() == null)
        {
            getSite().getShell().setText("GOLP TOOL");
        }
    }

    // ����Ϊʵ��ISearch�ӿ��еķ���
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
