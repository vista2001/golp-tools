/* �ļ�����       retcodeEditor.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.29
 * �޸����ݣ�   1.��dllDesc�����Ը���ΪSWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI��
 *         2.�޸�init�����е�setPartName�����Ӷ��������̵���ʾ�������ͽ���˴ӵ����д�һ���༭���󣬵���ñ༭��
 *         �Ĺرհ�ťʱ���༭����δ�رգ������ڱ༭���ı�������ʾ�˸�������ݣ������������ԭ������init������setFocus
 *         �����ڵ���setPartNameʱ������һ�£�
 *         3.�޸��˲��ֲ��֣�
 *         4.���Ǹ����е�dispose������ʹ�������б༭�����ر�ʱ���ָ����ߵı��⡰GOLP TOOL����
 *         5.�ڽ��м���APP/GOLP�����ж�ʱ������ֱ��ʹ��0��1�����֣���Ϊʹ��dev.util.Constants��ĳ�����
 *         6.��UI��ͳһʹ��0-APP��1-GOLP���ֱ�ʾ��
 *         7.��DebugOut.println�����滻System.out.println������
 *         8.���ӱ༭���Ĺ������ܣ�
 *         9.ͳһʹ��File.separator��
 *         10.���ӶԶ�̬��·���Ĵ���
 *         11.���ӷ���Զ��Ŀ¼�Ĺ��ܡ�
 * �޸��ˣ�       zxh
 * �޸�ʱ�䣺   2013.12.2
 * �޸����ݣ�   1.�޸ı�������
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
 * AopDll��༭����
 * <p>
 * �����̳���EditorPart�࣬��AopDllEditorInput��һ�����AopDll�༭���Ĺ���<br>
 * �ڱ༭����ʼ����ʱ��ͨ��AopDllEditorInput�ഫ���������Init�����жԱ༭��
 * ���г�ʼ����Ȼ����createPartControl��������ɶԱ༭���Ŀؼ��Ĺ��죬������
 * �ÿؼ�����Ҫ�ǡ���ѯ�������������޸ġ���ť������Ϊ���ı�����������ƣ���createPartControl
 * ������������dataInit������ɴ����ݿ��л�����ݲ������ı����У���ɶ����� �༭���ĳ�ʼ����
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

    public static final String ID = "dev.editor.AopDll.AopDllEditor"; // �༭����ı�ʶ
    private Text upProjectText; // ��������������ı���
    private Text dllLevelText; // ԭ�ӽ��׿⼶���ı���
    private Text dllIdText; // ԭ�ӽ��׿��ʶ�ı���
    private Text dllTypeText; // ԭ�ӽ��׿������ı���
    private Text dllNameText; // ԭ�ӽ��׿������ı���
    private Text dllDescText; // ԭ�ӽ��׿�˵���ı���
    private Button saveButton; // �޸İ�ť
    private Button unlockButton; // ������ť
    private Button restoreButton; // �ָ���ť
    private AopDllEditorInput input; // Input����
    private EditorAopDllServiceImpl impl; // ���ݿ���������
    private PreferenceStore ps; // ���ݿ�������Ϣ
    private Map<String, String> map; // �洢��ѯ�����ݵ�Map
    private Map<String, String> restoreMap; // �ָ���ť�����ݵ�Map
    private boolean bDirty = false; // �Ƿ��޸ĵı�ʶ
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
     * ���ڶԱ༭�����г�ʼ��<br>
     * ����site,input����Ա༭�����г�ʼ�����򿪱༭����ʱ�����ȵ��õķ���
     * 
     * @param site
     *            �༭����site
     * @param input
     *            �༭�����׵�input
     * @return û�з���ֵ
     * */
    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException
    {
        this.setSite(site); // ����Site
        this.setInput(input); // ����Input
        this.input = (AopDllEditorInput) input; // ��Input��ʼ��
        this.setPartName("��̬��" + " " + this.input.getName() + " " + "��������"
                + " " + this.input.getSource().getRootProject().getId()); // ���ñ༭������
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
     * �ڱ༭���ϴ������ֿռ䣬���ò��֣��Լ��Կռ���г�ʼ��<br>
     * ����Ҫ�Ŀؼ�������parent����пؼ�����Ϊ������������ﱻ�趨����������ڵ�����Init�󱳵���
     * 
     * @param parent
     *            ���пؼ���parent.
     * @return û�з���ֵ
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
        // �����༭����Ŀؼ�
        Group searchDllGroup = new Group(composite, SWT.NONE);
        searchDllGroup.setText("��̬���ѯ");
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
        dllGroup.setText("ԭ�ӽ��׿�");
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
        upProjectLabel.setText("��������");

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
        dllLevelLabel.setText("��̬�⼶��");

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
        dllIdLabel.setText("��̬���ʶ");

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
        dllTypeLabel.setText("��̬������");

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
        dllNameLabel.setText("*��̬������");

        dllNameText = new Text(dllGroup, SWT.BORDER);
        dllNameText.setEnabled(false);
        GridData gd_dllName = new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1);
        gd_dllName.widthHint = 70;
        dllNameText.setLayoutData(gd_dllName);
//        dllNameText.addVerifyListener(new VerifyListener()
//        {
//            // �����ı������������
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
        dllPathLabel.setText("*��̬��·��");
        
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
        dllPathButton.setText("ѡ��");
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
        dllDescLabel.setText("*����");

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
        // // �����ı������������
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
         * Ϊ����������ť������Ϊ�������°�ťʱ������ťΪ�������������Ϊ��������������
         * �༭�Ŀؼ�����Ϊ���ã������޸ġ���ť����Ϊ���ã������Ϊ�����������Ա༭�� �ؼ�����Ϊ�����ã����޸İ�ť����Ϊ�����á�
         */
        new Label(dllGroup, SWT.NONE);
        new Label(dllGroup, SWT.NONE);
        /*
         * Ϊ���޸ġ���ť������Ϊ�������޸İ�ť������̬�������ı���Ϊ�գ��򵯳�����Ի���
         * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorAopDllServiceImpl���
         * updateAopDllById������List��������滻��ʶΪAopId �����ݡ�
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
        unlockButton.setText("�༭");
        unlockButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                Button nButton = ((Button) e.getSource());
                if (nButton.getText() == "�༭")
                {
                    nButton.setText("����");
                    dllNameText.setEnabled(true);
                    dllDescText.setEnabled(true);
                    dllPathText.setEnabled(true);
                    dllPathButton.setEnabled(true);
                } else
                {
                    nButton.setText("�༭");
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
        saveButton.setText("����");
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
        restoreButton.setText("�ָ�");
        // ���ûָ���ť��Ϊ
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
     * ���༭��ѡ�е�ʱ�����<br>
     * ���༭���Ĵ��ڱ�ѡ��ʱ���Զ��������������ִ�з����е����
     * 
     * @return û�з���ֵ
     * */
    @Override
    public void setFocus()
    {
        getSite().getShell().setText(
                "GOLP TOOL" + " " + "��̬��" + " " + input.getName() + " " + "��������"
                        + " " + upProjectText.getText());// ���ù��ߵ�����
        setPartName("��̬��" + " " + input.getName() + " " + "��������" + " " + upProjectText.getText());
        setSelectNode(input.getSource());
    }

    /**
     * �Ա༭���Ŀؼ����г�ʼ��<br>
     * �����ҵ����ݿ��������Ϣ��Ȼ�����name��������Ϣ����EditorAopServiceImpl��
     * ��queryAopByIdOrName�����õ���ʼ�������ݷ���Map����һ������ؼ��С�
     * 
     * @param name
     *            Ҫ��ѯ�����ݵı�ʶ
     * @return û�з���ֵ
     * */
    private void datainit(String name) throws SQLException
    {
        // ������ݿ��������Ϣ
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
        // �����ݿ���в�ѯ������Map
        impl = new EditorAopDllServiceImpl();
        map = impl.queryAopDllByIdOrName(name, "", ps);
        restoreMap = map;
        // ���θ��ؼ���ֵ
        dllIdText.setText(map.get("ID"));
        dllDescText.setText(map.get("dlldesc"));
        dllNameText.setText(map.get("NAME"));
        dllTypeText.setText(map.get("dlltype"));
        dllPathText.setText(map.get("dllpath"));
        // ��aoplevelΪConstants.APP������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
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
     * �������ݵķ��� ���ڱ��水ť��doSave�����ı�������������޸İ�ť����ԭ�ӽ��������ı���Ϊ�գ��򵯳�����Ի���
     * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorDllServiceImpl���updateDllById������
     * List��������滻��ʶΪDllId�����ݡ�
     * 
     * @throws SQLException
     */
    private void saveDate() throws SQLException
    {
        if (dllNameText.getText().trim().isEmpty()
           || dllDescText.getText().trim().isEmpty()
           || dllPathText.getText().isEmpty())
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
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
         * �ж�dlllevel�Ƿ�ΪConstants.APP����������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
         * ����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ�
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
        setPartName("��̬��" + dllIdText.getText() + "��������" + upProjectText.getText());
    }

    @Override
    public void setEnable(boolean b)
    {
        if (!b)
        {
        	unlockButton.setText("�༭");
            dllNameText.setEnabled(false);
            dllDescText.setEnabled(false);
            dllPathText.setEnabled(false);
            dllPathButton.setEnabled(false);
            // setDirty(false);
        } 
        else
        {
        	unlockButton.setText("����");
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
        // �ñ༭�����رպ󣬻ָ����ߵı���
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
