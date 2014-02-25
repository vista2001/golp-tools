/* �ļ�����       TradeEditor.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.29
 * �޸����ݣ�   1.��AopDesc�����Ը���ΪSWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI��
 *         2.��String����ErrRecoverItem�������޸�Ϊ"0-��һ��","1-�ڶ���"��
 *         3.�Ը��ļ������г������´���ĵط������޸ģ�
 *         AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover")-1)]);
 *         ȥ����ߵļ�1���Դ�������½�ԭ�ӽ���ʱ��ѡ���һ�ָֻ����ƣ������ñ༭����ʱ��ʾ����Խ������⣻
 *         4.�޸�init�����е�setPartName�����Ӷ��������̵���ʾ�������ͽ���˴ӵ����д�һ���༭���󣬵���ñ༭��
 *         �Ĺرհ�ťʱ���༭����δ�رգ������ڱ༭���ı�������ʾ�˸�������ݣ������������ԭ������init������setFocus
 *         �����ڵ���setPartNameʱ������һ�£�
 *         5.�޸��˲��ֲ��֣�
 *         6.���Ǹ����е�dispose������ʹ�������б༭�����ر�ʱ���ָ����ߵı��⡰GOLP TOOL����
 *         7.���Ӷ�AopRetVal��ԭ�ӽ��׷���ֵ���Ĵ���
 *         8.�ڽ��м���APP/GOLP�����ж�ʱ������ֱ��ʹ��0��1�����֣���Ϊʹ��dev.util.Constants��ĳ�����
 *         9.��UI��ͳһʹ��0-APP��1-GOLP���ֱ�ʾ��
 *         10.��DebugOut.println�����滻System.out.println������
 *         11.���ӹ������ܣ�
 *         12.ͳһʹ��File.separator��
 * �޸��ˣ�       zxh
 * �޸�ʱ�䣺   2013.12.2
 * �޸����ݣ�   1.�޸ı���������
 * 		   2.������������������ӶԻ���
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
 * Aop��༭����
 * <p>
 * �����̳���EditorPart�࣬��AopEditorInput��һ�����Aop�༭���Ĺ���<br>
 * �ڱ༭����ʼ����ʱ��ͨ��AopEditorInput�ഫ���������Init�����жԱ༭��
 * ���г�ʼ����Ȼ����createPartControl��������ɶԱ༭���Ŀؼ��Ĺ��죬������
 * �ÿؼ�����Ҫ�ǡ���ѯ�������������޸ġ���ť������Ϊ���ı�����������ƣ���createPartControl
 * ������������dataInit������ɴ����ݿ��л�����ݲ������ı����У���ɶ����� �༭���ĳ�ʼ����
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
    public static final String ID = "dev.editor.aop.AopEditor";// �༭����ı�ʶ
    private Text upProjectText; // ��������������ı���
    private Text aopLevelText; // ԭ�ӽ��׼����ı���
    private Text upDllText; // ������̬���ı���
    private Text aopIdText; // ԭ�ӽ��ױ�ʶ�ı���
    private Text aopNameText; // ԭ�ӽ��������ı���
    private Text aopExtsText; // ��չ���б��ı���
    private Text aopDescText; // ԭ�ӽ���˵���ı���
    private Button saveButton; // �޸İ�ť
    private Button unlockButton; // ������ť
    private Button restoreButton; // �ָ���ť
    private Button inputDataButton;// ����������ѡ��ť
    private Button outputDataButton;// ���������ѡ��ť
    private Button aopExtsButton;// ԭ�ӽ�����չ���б����鰴ť
    private PreferenceStore ps; // ���ݿ�������Ϣ
    private EditorAopServiceImpl impl; // ���ݿ���������
    private Map<String, String> map; // �洢��ѯ�����ݵ�Map
    private Map<String, String> restoreMap; // ���ڻָ������ݵ�Map
    String[] ErrRecoverItem = { "0-��һ��", "1-�ڶ���" }; // �����˵�ѡ���ַ�������
    private Combo aopErrRecoverCombo; // �����˵�
    private List<Control> list = new ArrayList<Control>(); // �ɱ༭�ؼ��б�
    private AopEditorInput input; // Input�����
    private Text inputDataText; // �����������ı���
    private Text outputDataText; // ����������ı���
    private Text preConditionText; // ǰ�������ı���
    private Text postConditionText; // ���������ı���
    private boolean bDirty = false; // �Ƿ��޸��ж�
    private Search search;
    private Text aopRetValText;

    public AopEditor()
    {
    }

    /**
     * �����ڹرձ༭��ʱ����༭��������<br>
     * ��isDirtyΪtrueʱ���رձ༭������ʾ�Ƿ񱣴��޸ģ����ѡ��"��"�����ִ��saveData������
     * 
     * @return û�з���ֵ
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
        this.input = (AopEditorInput) input; // ��Input��ʼ��
        this.setPartName("ԭ�ӽ���" + " " + this.input.getName() + " " + "��������"
                + " " + this.input.getSource().getRootProject().getId()); // ���ñ༭������
    }

    /**
     * /** ��Ҫ�����Ŀؼ�����һ��List��
     * 
     * @param list
     * @return û�з���ֵ
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
        // TODO ԭ�ӽ�����չ�㹦�ܴ�ʵ��
        // list.add(aopExtsButton);
        list.add(inputDataText);
        list.add(outputDataText);
    }

    /**
     * �ж��Ƿ�༭�����޸�<br>
     * �������ֵ��true���رձ༭��ʱ����ʾ�Ƿ񱣴档
     * 
     * @return ����༭�����޸ģ����ء�true�������򷵻ء�false��
     */
    @Override
    public boolean isDirty()
    {
        return bDirty;
    }

    /**
     * ���ñ༭����״̬<br>
     * ��isDirty(),���޸ġ���ť�޸ĳ�b��״̬��
     * 
     * @param b
     *            Ҫ�޸ĳɵ�״̬
     * @return û�з���ֵ
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
        Group searchAopGroup = new Group(composite, SWT.NONE);
        searchAopGroup.setText("ԭ�ӽ��ײ�ѯ");
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
        aopGroup.setText("ԭ�ӽ��ױ�");

        Label lblNewLabel_1 = new Label(aopGroup, SWT.NONE);
        GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 2, 1);
        gd_lblNewLabel_1.widthHint = 80;
        lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);

        Label upProjectLabel = new Label(aopGroup, SWT.NONE);
        upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        upProjectLabel.setText("��������");

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
        aopLevelTextLabel.setText("ԭ�ӽ��׼���");

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
        aopErrRecoverLabel.setText("*����ָ�����");

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
        upDllLabel.setText("������̬��");

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
        aopIdLabel.setText("ԭ�ӽ��ױ�ʶ");

        aopIdText = new Text(aopGroup, SWT.BORDER);
        aopIdText.setEnabled(false);
        GridData gd_AopId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
                1);
        gd_AopId.widthHint = 70;
        aopIdText.setLayoutData(gd_AopId);
        aopIdText.addVerifyListener(new VerifyListener()
        {
            // �����ı������������
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
        aopNameLabel.setText("*ԭ�ӽ�������");

        aopNameText = new Text(aopGroup, SWT.BORDER);
        aopNameText.setEnabled(false);
        GridData gd_AopName = new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1);
        gd_AopName.widthHint = 70;
        aopNameText.setLayoutData(gd_AopName);
        // aopNameText.addVerifyListener(new VerifyListener() {
        // // �����ı������������
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopNameText.getText().length() <= 32;
        // }
        // });
        // ���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
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
        aopExtsLabel.setText("��չ���б�");

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
        aopExtsButton.setText("����");
        new Label(aopGroup, SWT.NONE);
        new Label(aopGroup, SWT.NONE);

        Label inputDataLabel = new Label(aopGroup, SWT.NONE);
        inputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1));
        inputDataLabel.setText("*����������");

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
        outputDataLabel.setText("*���������");

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
        aopRetValLabel.setText("*ԭ�ӽ��׷���ֵ");
        aopRetValLabel.setToolTipText("����ֵ������������ֵ֮�������߷ָ���Ĭ��ֵ���ڵ�һ��");

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
        prePositionLabel.setText("ǰ������");

        preConditionText = new Text(aopGroup, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        preConditionText.setEnabled(false);
        preConditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 4, 3));
        // preConditionText.addVerifyListener(new VerifyListener() {
        // // �����ı�����������
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopDescText.getText().length() <= 128;
        // }
        // });
        // ���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
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
        postPositionLabel.setText("��������");

        postConditionText = new Text(aopGroup, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        postConditionText.setEnabled(false);
        postConditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 4, 3));
        // postConditionText.addVerifyListener(new VerifyListener() {
        // // �����ı�����������
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopDescText.getText().length() <= 128;
        // }
        // });
        // ���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
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
        aopDescLabel.setText("*����");

        aopDescText = new Text(aopGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
                | SWT.MULTI);
        aopDescText.setEnabled(false);
        // aopDescText.addVerifyListener(new VerifyListener() {
        // // �����ı�����������
        // @Override
        // public void verifyText(VerifyEvent e) {
        // if (e.character != 8)
        // e.doit = aopDescText.getText().length() < 128;
        // }
        // });
        // ���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
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
        unlockButton.setText("�༭");
        unlockButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                Button nButton = ((Button) e.getSource());
                if (nButton.getText() == "�༭")
                {
                    nButton.setText("����");
                    aopErrRecoverCombo.setEnabled(true);
                    unlock(list);
                } else
                {
                    nButton.setText("�༭");
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
        saveButton.setText("����");
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
        restoreButton.setText("�ָ�");
        // ���ûָ���ť��Ϊ
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
     * ���༭��ѡ�е�ʱ�����<br>
     * ���༭���Ĵ��ڱ�ѡ��ʱ���Զ��������������ִ�з����е����
     * 
     * @return û�з���ֵ
     * */
    @Override
    public void setFocus()
    {
        getSite().getShell().setText(
                "GOLP TOOL" + " " + "ԭ�ӽ���" + " " + input.getName() + " "
                        + "��������" + " " + upProjectText.getText());// ���ù��ߵı���
        setSelectNode(input.getSource());
        setPartName("ԭ�ӽ���" + " " + input.getName() + " " + "��������" + " "
                + upProjectText.getText());
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
        // �����ݿ���в�ѯ������Map
        map = impl.queryAopByIdOrName(name, "", ps);
        restoreMap = map;
        // ���θ��ؼ���ֵ
        aopIdText.setText(map.get("ID"));
        aopNameText.setText(map.get("NAME"));
        aopDescText.setText(map.get("aopdesc"));
        aopErrRecoverCombo.setText(ErrRecoverItem[Integer.parseInt(map
                .get("aoperrrecover"))]);
        aopExtsText.setText(map.get("aopexts"));
        // ��AopLevelTextΪConstants.APP������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
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
     * ���ؼ�����Ϊ������<br>
     * ��for eachѭ����List�еĿռ�������Ϊ�����á�
     * 
     * @param list
     *            Ҫ���õĿռ���б�
     * @return û�з���ֵ
     * */
    private void lock(List<Control> list)
    {
        for (Control i : list)
        {
            i.setEnabled(false);
        }
    }

    /**
     * ���ؼ�����Ϊ����<br>
     * ��for eachѭ����List�еĿռ�������Ϊ���á�
     * 
     * @param list
     *            Ҫ���õĿռ���б�
     * @return û�з���ֵ
     * */
    private void unlock(List<Control> list)
    {
        for (Control i : list)
            i.setEnabled(true);
    }

    /**
     * �������ݵķ��� ���ڱ��水ť��doSave�����ı�������������޸İ�ť����ԭ�ӽ��������ı���Ϊ�գ��򵯳�����Ի���
     * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorAopServiceImpl���updateAopById������
     * List��������滻��ʶΪAopId�����ݡ�
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
            showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
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
        // �ñ༭�����رպ󣬻ָ����ߵı���
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
         * �ж�AopLevelText�Ƿ�ΪConstants.APP����������Ϊ"APP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
         * ����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ�
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
        setPartName("ԭ�ӽ���" + aopIdText.getText() + "��������"
                + upProjectText.getText());
    }

    @Override
    public void setEnable(boolean b)
    {
        if (!b)
        {
            unlockButton.setText("�༭");
            aopErrRecoverCombo.setEnabled(false);
            lock(list);
        } else
        {
            unlockButton.setText("����");
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
