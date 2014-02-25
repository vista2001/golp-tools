/* �ļ�����       TradeEditor.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.29
 * �޸����ݣ�   1.��tradeDescText�����Ը���ΪSWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI��
 *         2.���Ӷ�Դ�ļ�·����TradeSrcPath���Ĵ������ǵ�Ӧ���ݽ������Ͷԡ�Դ�ļ�·������صĿؼ����п��������ã�
 *         ���Բ�δ�ѡ�Դ�ļ�·������صĿؼ��Ž��б�controlsForLock�У�������enableControls������disableControls����
 *         �У������˶���Щ�ؼ����е������õĴ��룬ͬʱ�޸���checkDataisBlank�����������жϣ�����������Ϊ�ֹ����룬��Դ�ļ�·��Ϊ�գ���У��ʧ�ܣ�
 *         3.��inputDataText��outputDataText�����б�controlsForLock�У�ʹ�á��༭/��������ť���Զ����ǽ��п��ƣ�ͬʱ���ǵ��û����ܻḴ��
 *         ����/�����������԰��������ؼ��ĵ�������Ϊֻ����
 *         4.��������������������ѡ���Ƿ����Ĺ��ܣ��������������������ѡ���Ƿ�������������Դ�Ĺ��ܡ�
 *         5.���Ӵ˱༭�������Ĺ��ܡ�
 *         6.�ڽ��м���APP/GOLP�����ж�ʱ������ֱ��ʹ��0��1�����֣���Ϊʹ��dev.util.Constants��ĳ�����
 *         7.��DebugOut.println�����滻System.out.println������ 
 */

package dev.editors.trade;

import java.io.IOException;
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import dev.db.service.EditorsDaoServiceImpl;
import dev.editors.IGetUpProject;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.golpDialogs.InputDataItemDialog;
import dev.golpDialogs.OutputDataItemDialog;
import dev.golpDialogs.QueryServerDialog;
import dev.golpDialogs.QueryTradeDialog;
import dev.golpDialogs.TradeSrcPathDialog;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;
import dev.model.base.ResourceLeafNode;
import dev.util.Constants;
import dev.util.DebugOut;
import dev.util.PropertiesUtil;
import dev.views.NavView;

public class TradeEditor extends EditorPart implements ISearch, IGetUpProject
{

    public static final String ID = "dev.editors.Trade.TradeEditor"; //$NON-NLS-1$
    // ��ѯ����
    // private Text upProjectText;
    private Text searchText;
    // �༭����
    // ��������
    private Text upProjectText1;
    // ���׼���
    private Text tradeLevelText;
    // ���ױ�ʾ��
    private Text tradeIdText1;
    // ��������
    private Text tradeNameText1;
    // �����������
    private Text upServerText;
    // ����ģʽ
    private Combo tradeModelCombo;
    // ���׷���ģʽ
    private Combo tradeServerModelCombo;
    // ����������
    private Text inputDataText;
    // ���������
    private Text outputDataText;
    // ǰ������
    private Text preconditionText;
    // ��������
    private Text postconditionText;
    // ���õĽ���
    private Text callServiceText;
    // ��������
    private Text tradeDescText;
    // ѡ�������������İ�ť
    private Button upServerBtn;
    // ѡ������������İ�ť
    private Button inputSelBtn;
    // ѡ�����������İ�ť
    private Button outputSelBtn;
    // ѡ����õķ���İ�ť
    private Button callServiceBtn;
    // ������ʾ��Դ�ļ�·�����ı�ǩ��������Ϊ��Ա����
    // ����Ϊ��Ҫ���ݽ���ģʽ��ѡ�����ݣ��ж����Ƿ����
    private Label tradeSrcPathLabel;
    // Դ�ļ�·��
    private Text tradeSrcPathText;
    // ѡ��Դ�ļ�·���İ�ť
    private Button tradeSrcPathBtn;
    // ������ť
    private Button unlockButton;
    // ���水ť
    private Button saveButton;
    // private Button tradeQueryBtn;
    // �ָ���ť
    private Button recoverBtn;
    // �༭����Ⱦ��ʶ
    private boolean dirty = false;
    // ��ѯ����
    private Search search;
    // �༭��������
    private IEditorInput input;
    // ��Ҫ�����Ŀؼ�����
    private List<Control> controlsForLock = new ArrayList<Control>();
    // ��������Ŀؼ�����
    private List<Control> controlsForInput = new ArrayList<Control>();
    // ���ڻָ���ť��ԭʼ����
    private Map<String, String> recoverMap = new HashMap<String, String>();
    private Map<String, String> contentMap = new HashMap<String, String>();
    // ���ڲ�ѯʱ�ṩ���ݿ�����ӵ�ַ
    private PreferenceStore ps = null;
    // �༭����Ӧ�����е����ڵ�
    private ResourceLeafNode resourceLeafNode;

    public TradeEditor()
    {
    }

    /**
     * Create contents of the editor part.
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent)
    {
        createControls(parent);
        // ��ʼ�����ݺ�״̬
        initDataAndState(input);
        setPartName("����" + " " + input.getName() + " " + "��������" + " "
                + upProjectText1.getText());
    }

    /**
     * ����dirty��״̬
     * 
     * @param dirty
     */
    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
        saveButton.setEnabled(dirty);
        recoverBtn.setEnabled(dirty);
    }

    @Override
    public void setFocus()
    {
        // Set the focus
        getSite().getShell().setText(
                "GOLP TOOL" + " " + "����" + " " + input.getName() + " " + "��������"
                        + " " + upProjectText1.getText());
        setSelectionNode(((TradeEditorInput) input).getSource());
    }

    @Override
    public void doSave(IProgressMonitor monitor)
    {
        // Do the Save operation
        saveMethod();
    }

    @Override
    public void doSaveAs()
    {
        // Do the Save As operation

    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException
    {
        this.setSite(site);
        this.setInput(input);
        // this.setPartName(input.getName());
        this.input = input;
        this.resourceLeafNode = ((TradeEditorInput) input).getSource();
        initps();
    }

    private void initps()
    {
        try
        {
            ps = PropertiesUtil.getPropertiesByRln(resourceLeafNode);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * �ж��༭���Ƿ���Ⱦ
     */
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
     * ��ʼ���ؼ����ݺ�״̬ ���Ȼ��趨ҳ�浱ǰ���̵�id�� Ȼ�������ݿ��ѯҳ������ݣ� �����ݷ��õ��ؼ��ڣ� �趨�ؼ�
     * 
     * �Ľ���״̬ Ϊ�ɱ༭�Ŀؼ�����modify����
     * 
     * @param input
     *            ��ǰ�༭����input
     */
    private void initDataAndState(IEditorInput input)
    {
        // ������������
        String prjid = getProjectData(input);
        // upProjectText.setText(prjid);
        upProjectText1.setText(prjid);
        contentMap = getTradeData(input);
        // ���ÿؼ�����
        setTradeData(contentMap);
        // ��ÿ��Խ����Ŀؼ�
        getControls();
        // ���ý�����ť״̬
        if (tradeLevelText.getText().equals("0-APP"))
        {
            unlockButton.setEnabled(true);
        }
        // Ϊ�ɱ༭�Ŀؼ�����modify����
        addModify();
    }

    /**
     * ��ù���id
     * 
     * @param input
     *            ��ǰ�༭����input������������ڻ�õ�ǰ���̵�������Ϣ���Թ����ݿ���ʳ���ʹ��
     * @return ����һ��String���͵Ķ�������������ͨ����ѯ���ݿ�õ��Ĺ���id
     */
    private String getProjectData(IEditorInput input)
    {
        String prjid = "";

        EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
        try
        {

            prjid = editorsDaoServiceImpl.queryProjectId(ps);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return prjid;
    }

    /**
     * ��ý��ױ༭ҳ�������
     * 
     * @param input
     *            ��ǰ�༭����input������������ڻ�õ�ǰ���̵����ݿ�������Ϣ���Թ����ݿ���ʳ���ʹ��
     * @return ����һ��map�����Ϊ����map������Ϊ�գ�������null
     */
    private Map<String, String> getTradeData(IEditorInput input)
    {
        Map<String, String> map = new HashMap<String, String>();
        EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();

        try
        {
            map = editorsDaoServiceImpl.queryTradeByIdOrName(input.getName(),
                    "", ps);
            recoverMap = map;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * ���ý���ҳ�����ݣ������map�ǲ�ѯ���ݿⷵ�صõ��Ľ��
     * 
     * @param map
     *            ����һ�����ҳ�����ݵ�map�������Ϊ�վ����η������ݵ�ҳ��ؼ��У���������û���ʾ
     */
    private void setTradeData(Map<String, String> map)
    {
        if (!map.isEmpty())
        {
            // ���ò�ѯ�������
            // searchText.setText("");
            // ���ñ༭�������
            tradeLevelText
                    .setText(map.get("tradeLevelText").equals(Constants.APP) ? "0-APP"
                            : "1-GOLP");
            tradeIdText1.setText(map.get("ID"));
            tradeNameText1.setText(map.get("NAME"));
            upServerText.setText(map.get("upServerCombo"));
            tradeModelCombo.setItems(new String[] { "0-����ͼ", "1-�ֹ�����" });
            tradeModelCombo
                    .select(Integer.parseInt(map.get("tradeModelCombo")));

            tradeServerModelCombo.setItems(new String[] { "0-2 Wayͬ��ģʽ", "1-N Way�Ựģʽ" });
            tradeServerModelCombo.select(Integer.parseInt(map
                    .get("tradeServerModelCombo")));

            inputDataText.setText(map.get("inputDataText"));
            outputDataText.setText(map.get("outputDataText"));
            tradeSrcPathText.setText(map.get("tradeSrcPathText"));
            preconditionText.setText(map.get("preconditionText"));
            postconditionText.setText(map.get("postconditionText"));
            callServiceText.setText(map.get("callServiceText"));
            tradeDescText.setText(map.get("tradeDescText"));
        } else
        {
            showMessage("δ�ҵ���Ӧ�ļ�¼", SWT.ICON_WARNING);
            // ���ò�ѯ�������
            searchText.setText("");
            return;
        }

    }

    /**
     * ���Ҫ�����Ŀؼ��б�
     */
    private void getControls()
    {
        // ��ÿɽ����Ŀؼ�
        controlsForLock.add(tradeNameText1);
        controlsForLock.add(upServerText);
        controlsForLock.add(upServerBtn);
        controlsForLock.add(tradeModelCombo);
        controlsForLock.add(tradeServerModelCombo);
        // controls.add(tradeExtFlagText);
        controlsForLock.add(inputSelBtn);
        controlsForLock.add(inputDataText);
        controlsForLock.add(outputSelBtn);
        controlsForLock.add(outputDataText);
        controlsForLock.add(callServiceBtn);
        controlsForLock.add(callServiceText);
        controlsForLock.add(preconditionText);
        controlsForLock.add(postconditionText);
        controlsForLock.add(tradeDescText);

        // ��ñ�����ؼ�
        controlsForInput.add(tradeNameText1);
        controlsForInput.add(tradeDescText);
        controlsForInput.add(upServerText);
        controlsForInput.add(tradeModelCombo);
        controlsForInput.add(tradeServerModelCombo);
        controlsForInput.add(inputDataText);
        controlsForInput.add(outputDataText);
        /*
         * controlsForInput.add(callServiceText);
         * controlsForInput.add(tradeDescText);
         * controlsForInput.add(preconditionText);
         * controlsForInput.add(postconditionText);
         */
    }

    /**
     * ���ɱ༭�Ķ�������ModifyListener,������ֵ�ı��ʱ����༭���趨dirty����
     */
    private void addModify()
    {
        List<Control> controlsCanModify = new ArrayList<Control>(
                controlsForInput);
        controlsCanModify.add(callServiceText);
        controlsCanModify.add(tradeDescText);
        controlsCanModify.add(tradeSrcPathText);
        controlsCanModify.add(preconditionText);
        controlsCanModify.add(postconditionText);
        for (Control o : controlsCanModify)
        {
            if (o instanceof Text)
            {

                ((Text) o).addModifyListener(new ModifyListener()
                {

                    @Override
                    public void modifyText(ModifyEvent e)
                    {
                        if (!isDirty())
                        {
                            setDirty(true);
                        }
                        firePropertyChange(IEditorPart.PROP_DIRTY);
                    }
                });
            }
            if (o instanceof Combo)
            {
                ((Combo) o).addModifyListener(new ModifyListener()
                {

                    @Override
                    public void modifyText(ModifyEvent e)
                    {
                        if (!isDirty())
                        {
                            setDirty(true);
                        }
                        firePropertyChange(IEditorPart.PROP_DIRTY);
                    }
                });
            }
        }
    }

    /**
     * �����ؼ�
     */
    private void enableControls()
    {
        for (Control o : controlsForLock)
        {
            o.setEnabled(true);
        }

        // ���µ�if-else���飬���ݽ������ͣ��ԡ�Դ�ļ�·������صĿؼ����п��������á�
        // ����������Ϊ����ͼ��ʽ
        if (tradeModelCombo.getSelectionIndex() == 0)
        {
            tradeSrcPathLabel.setEnabled(false);
            // tradeSrcPathText.setText("");
            tradeSrcPathText.setEnabled(false);
            tradeSrcPathBtn.setEnabled(false);
        }
        // ����������Ϊ�ֹ����뷽ʽ
        else
        {
            tradeSrcPathLabel.setEnabled(true);
            tradeSrcPathText.setEnabled(true);
            tradeSrcPathBtn.setEnabled(true);
        }

    }

    /**
     * �����ؼ�
     */
    private void disableControls()
    {
        for (Control o : controlsForLock)
        {
            o.setEnabled(false);
        }
        // ��Ϊ��controlsForLock�У���������tradeSrcPathText
        // ��tradeSrcPathBtn�����Դ˴������Ǳ�Ϊ�����á�
        tradeSrcPathText.setEnabled(false);
        tradeSrcPathBtn.setEnabled(false);

//        saveButton.setEnabled(false);
    }

    /**
     * У��ؼ�����
     * 
     * @return ����һ������ֵ��trueΪУ��ͨ����falseΪУ��ʧ��
     */
    private boolean checkDataisBlank()
    {
        // ������Ŀؼ��Ƿ���������
        for (Control control : controlsForInput)
        {
            if (control instanceof Text)
            {
                if (((Text) control).getText().trim().equals(""))
                {
                    showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
                    return false;
                }
            }
            if (control instanceof Combo)
            {
                if (((Combo) control).getText().trim().equals(""))
                {
                    showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
                    return false;
                }
            }
        }
        // ����������Ϊ�ֹ����룬��Դ�ļ�·��Ϊ�գ���У��ʧ�ܡ�
        if (tradeModelCombo.getSelectionIndex() == 1)
        {
            if (tradeSrcPathText.getText().trim().isEmpty())
            {
                showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
                return false;
            }
        }

        return true;
    }

    /**
     * ��ʾ��ʾ��Ϣ
     * 
     * @param msg
     * @param style
     */
    private void showMessage(String msg, int style)
    {
        MessageBox box = new MessageBox(getSite().getShell(), style);
        box.setMessage(msg);
        box.open();
    }

    /**
     * �������ؼ�
     * 
     * @param parent
     */
    private void createControls(Composite parent)
    {

        ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        Composite container = new Composite(scrolledComposite, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        Group searchDllGroup = new Group(container, SWT.NONE);
        searchDllGroup.setLayout(new GridLayout(7, false));
        searchDllGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false, 1, 1));
        searchDllGroup.setText("���ײ�ѯ");

        search = new Search();
        search.setUpProject(resourceLeafNode.getRootProject().getId());
        search.setEditorId(ID);
        search.setEditor(this);

        search.createPartControl(searchDllGroup);

        Group group_1 = new Group(container, SWT.NONE);
        group_1.setLayout(new GridLayout(8, false));
        group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        group_1.setText("����");

        Label label = new Label(group_1, SWT.NONE);
        GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2,
                1);
        gd_label.widthHint = 70;
        label.setLayoutData(gd_label);

        Label upProjectLabel1 = new Label(group_1, SWT.NONE);
        upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        upProjectLabel1.setText("��������");
        upProjectText1 = new Text(group_1, SWT.BORDER);
        upProjectText1.setEnabled(false);
        GridData gd_upProjectText1 = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_upProjectText1.widthHint = 70;
        upProjectText1.setLayoutData(gd_upProjectText1);

        Label label_1 = new Label(group_1, SWT.NONE);
        GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false,
                2, 1);
        gd_label_1.widthHint = 70;
        label_1.setLayoutData(gd_label_1);

        Label tradeLevelLabel = new Label(group_1, SWT.NONE);
        tradeLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeLevelLabel.setText("���׼���");
        tradeLevelText = new Text(group_1, SWT.BORDER);
        tradeLevelText.setEnabled(false);
        GridData gd_tradeLevelText = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_tradeLevelText.widthHint = 70;
        tradeLevelText.setLayoutData(gd_tradeLevelText);

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        Label tradeIdLabel1 = new Label(group_1, SWT.NONE);
        tradeIdLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeIdLabel1.setText("���ױ�ʶ��");
        tradeIdText1 = new Text(group_1, SWT.BORDER);
        tradeIdText1.setEnabled(false);
        GridData gd_tradeIdText1 = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_tradeIdText1.widthHint = 70;
        tradeIdText1.setLayoutData(gd_tradeIdText1);

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        Label tradeNameLabel1 = new Label(group_1, SWT.NONE);
        tradeNameLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeNameLabel1.setText("*��������");

        tradeNameText1 = new Text(group_1, SWT.BORDER);
        tradeNameText1.setEnabled(false);
        GridData gd_tradeNameText1 = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_tradeNameText1.widthHint = 70;
        tradeNameText1.setLayoutData(gd_tradeNameText1);

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        Label upServerLabel = new Label(group_1, SWT.NONE);
        upServerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        upServerLabel.setText("*�����������");

        upServerText = new Text(group_1, SWT.READ_ONLY | SWT.BORDER);

        upServerText.setEnabled(false);
        GridData gd_upServerCombo = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_upServerCombo.widthHint = 53;
        upServerText.setLayoutData(gd_upServerCombo);

        upServerBtn = new Button(group_1, SWT.NONE);
        upServerBtn.setEnabled(false);
        upServerBtn.setText("...");
        upServerBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProject = upProjectText1.getText();
                QueryServerDialog queryServerDialog = new QueryServerDialog(
                        e.display.getActiveShell(), e.getSource(),
                        upProject);
                queryServerDialog.addInformDialogListener(new InformDialogListener()
                {

                    @Override
                    public void handleEvent(InformDialogEvent dm)
                    {

                        java.util.List<String> l = ((QueryServerDialog) dm
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
                        upServerText.setText(s);
                    }
                });
                queryServerDialog.open();
            }
        });
        
        new Label(group_1, SWT.NONE);

        Label tradeModelLabel = new Label(group_1, SWT.NONE);
        tradeModelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeModelLabel.setText("*��������");

        tradeModelCombo = new Combo(group_1, SWT.READ_ONLY);
        tradeModelCombo.setEnabled(false);
        tradeModelCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                // ���༭���ڱ༭״̬��ʱ����ʱ���༭����ť���ı�����ˡ������������ŶԽ������ͽ����жϣ��Ӷ��ԡ�Դ�ļ�·������صĿؼ����п��������á�
                // ���༭�����δ�ʱ���༭��Ϊ����״̬����ʱӦ��֤���пؼ����ǲ����õġ�
                if (unlockButton.getText().equals("����"))
                {
                    // ����������Ϊ����ͼ��ʽ
                    if (tradeModelCombo.getSelectionIndex() == 0)
                    {
                        tradeSrcPathLabel.setEnabled(false);
                        tradeSrcPathText.setText("");
                        tradeSrcPathText.setEnabled(false);
                        tradeSrcPathBtn.setEnabled(false);
                    }
                    // ����������Ϊ�ֹ����뷽ʽ
                    else
                    {
                        tradeSrcPathLabel.setEnabled(true);
                        tradeSrcPathText.setEnabled(true);
                        tradeSrcPathBtn.setEnabled(true);
                    }
                }
            }
        });
        GridData gd_tradeModelCombo = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_tradeModelCombo.widthHint = 100;
        tradeModelCombo.setLayoutData(gd_tradeModelCombo);

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        Label tradeServerModelLabel = new Label(group_1, SWT.NONE);
        tradeServerModelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeServerModelLabel.setText("*���׷���ģʽ");

        tradeServerModelCombo = new Combo(group_1, SWT.READ_ONLY);
        tradeServerModelCombo.setEnabled(false);
        GridData gd_tradeServerModelCombo = new GridData(SWT.FILL, SWT.CENTER,
                false, false, 1, 1);
        gd_tradeServerModelCombo.widthHint = 100;
        tradeServerModelCombo.setLayoutData(gd_tradeServerModelCombo);

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        Label inputDataLabel = new Label(group_1, SWT.NONE);
        inputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        inputDataLabel.setText("*��������");

        inputDataText = new Text(group_1, SWT.BORDER | SWT.READ_ONLY);
        inputDataText.setEnabled(false);
        inputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 4, 1));

        inputSelBtn = new Button(group_1, SWT.NONE);
        inputSelBtn.setEnabled(false);
        GridData gd_inputSelBtn = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_inputSelBtn.widthHint = 80;
        inputSelBtn.setLayoutData(gd_inputSelBtn);
        inputSelBtn.setText("...");
        inputSelBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = upProjectText1.getText();
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
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        Label outputDataLabel = new Label(group_1, SWT.NONE);
        outputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        outputDataLabel.setText("*�������");

        outputDataText = new Text(group_1, SWT.BORDER | SWT.READ_ONLY);
        outputDataText.setEnabled(false);
        outputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 4, 1));

        outputSelBtn = new Button(group_1, SWT.NONE);
        outputSelBtn.setEnabled(false);
        GridData gd_outputSelBtn = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_outputSelBtn.widthHint = 80;
        outputSelBtn.setLayoutData(gd_outputSelBtn);
        outputSelBtn.setText("...");
        outputSelBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = upProjectText1.getText();
                OutputDataItemDialog outputDataItemDialog = new OutputDataItemDialog(
                        e.display.getActiveShell(), e.getSource(),
                        outputDataText.getText(), upProjectId);
                outputDataItemDialog
                        .addInformDialogListener(new InformDialogListener()
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
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        Label callServiceLabel = new Label(group_1, SWT.NONE);
        callServiceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        callServiceLabel.setText("���õĽ���");

        callServiceText = new Text(group_1, SWT.BORDER | SWT.READ_ONLY);
        callServiceText.setEditable(false);
        callServiceText.setEnabled(false);
        callServiceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 4, 1));

        callServiceBtn = new Button(group_1, SWT.NONE);
        callServiceBtn.setEnabled(false);
        GridData gd_callServiceBtn = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_callServiceBtn.widthHint = 80;
        callServiceBtn.setLayoutData(gd_callServiceBtn);
        callServiceBtn.setText("...");
        callServiceBtn.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProject = upProjectText1.getText();
                QueryTradeDialog queryTradeDialog = new QueryTradeDialog(
                        e.display.getActiveShell(), e.getSource(),
                        upProject, Integer.parseInt(tradeIdText1.getText()), Constants.ALL);
                queryTradeDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((QueryTradeDialog) dm
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
                                callServiceText.setText(s);
                            }
                        });
                queryTradeDialog.open();
            }

        });
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        tradeSrcPathLabel = new Label(group_1, SWT.NONE);
        tradeSrcPathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeSrcPathLabel.setText("*Դ�ļ�·��");

        tradeSrcPathText = new Text(group_1, SWT.BORDER);
        tradeSrcPathText.setEnabled(false);
        tradeSrcPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                false, false, 4, 1));

        tradeSrcPathBtn = new Button(group_1, SWT.NONE);
        GridData gd_tradeSrcPathBtn = new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1);
        gd_tradeSrcPathBtn.widthHint = 80;
        tradeSrcPathBtn.setLayoutData(gd_tradeSrcPathBtn);
        tradeSrcPathBtn.setEnabled(false);
        tradeSrcPathBtn.setText("...");
        tradeSrcPathBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                TradeSrcPathDialog tradeSrcPathDialog = new TradeSrcPathDialog(
                        e.display.getActiveShell(), e.getSource(),
                        tradeSrcPathText.getText().trim(), upProjectText1.getText());
                tradeSrcPathDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((TradeSrcPathDialog) dm
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
                                tradeSrcPathText.setText(s);
                            }
                        });
                tradeSrcPathDialog.open();
            }
        });

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        Label preconditionLabel = new Label(group_1, SWT.NONE);
        preconditionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        preconditionLabel.setText("ǰ������");

        preconditionText = new Text(group_1, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        preconditionText.setEnabled(false);
        preconditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 5, 3));
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        Label postconditionLabel = new Label(group_1, SWT.NONE);
        postconditionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        postconditionLabel.setText("��������");

        postconditionText = new Text(group_1, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        postconditionText.setEnabled(false);
        postconditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 5, 3));

        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        Label tradeDescLabel = new Label(group_1, SWT.NONE);
        tradeDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeDescLabel.setText("*����");

        tradeDescText = new Text(group_1, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
                | SWT.MULTI);
        tradeDescText.setEnabled(false);
        tradeDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                false, 5, 3));
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);
        new Label(group_1, SWT.NONE);

        unlockButton = new Button(group_1, SWT.NONE);
        unlockButton.setEnabled(false);
        GridData gd_unlockButton = new GridData(SWT.RIGHT, SWT.CENTER, false,
                false, 1, 1);
        gd_unlockButton.widthHint = 80;
        unlockButton.setLayoutData(gd_unlockButton);
        unlockButton.setText("�༭");
        unlockButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseUp(MouseEvent e)
            {
                String name = ((Button) e.getSource()).getText();
                if (name.equals("�༭"))
                {
                    enableControls();
                    ((Button) e.getSource()).setText("����");
                } else
                {
                    disableControls();
                    ((Button) e.getSource()).setText("�༭");
                }
            }

        });

        saveButton = new Button(group_1, SWT.NONE);
        saveButton.setEnabled(false);
        GridData gd_saveButton = new GridData(SWT.CENTER, SWT.CENTER, false,
                false, 2, 1);
        gd_saveButton.widthHint = 80;
        saveButton.setLayoutData(gd_saveButton);
        saveButton.setText("����");
        saveButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseUp(MouseEvent e)
            {
                saveMethod();
            }

        });

        recoverBtn = new Button(group_1, SWT.NONE);
        recoverBtn.setEnabled(false);
        GridData gd_recoverBtn = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_recoverBtn.widthHint = 80;
        recoverBtn.setLayoutData(gd_recoverBtn);
        recoverBtn.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseUp(MouseEvent e)
            {
                setTradeData(recoverMap);
                setDirty(false);
                firePropertyChange(IEditorPart.PROP_DIRTY);
            }

        });
        recoverBtn.setText("�ָ�");
        new Label(group_1, SWT.NONE);

        scrolledComposite.setContent(container);
        scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));
    }

    /**
     * �������ݿ�Ĳ��� ��Ҫ���µ����ݷ��õ�һ��list�д��ݸ����ݿ��������
     * 
     * @return
     */
    private boolean updateDb()
    {
        List<String> dataToUp = new ArrayList<String>();
        /*
         * tradeName=?,upServer=?,tradeModel=?," +
         * "tradeServerModel=?,inputData=?,outputData=?," +
         * "callServices=?,precondition=?,postcondition=?,tradeDesc=?"
         */
        dataToUp.add(tradeNameText1.getText());
        dataToUp.add(upServerText.getText());
        dataToUp.add(tradeModelCombo.getText().substring(0, 1));
        dataToUp.add(tradeServerModelCombo.getText().substring(0, 1));
        dataToUp.add(inputDataText.getText());
        dataToUp.add(outputDataText.getText());
        dataToUp.add(callServiceText.getText());
//        dataToUp.add("");
        dataToUp.add(preconditionText.getText());
        dataToUp.add(postconditionText.getText());
        dataToUp.add(tradeDescText.getText());
        dataToUp.add(tradeSrcPathText.getText().trim());
        EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
        try
        {
            editorsDaoServiceImpl.updateTradeById(tradeIdText1.getText(),
                    dataToUp, ps);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * �趨�༭����Ӧ�����������еĽڵ�Ϊ����
     * 
     * @param rln
     *            ����һResourceLeafNode ����
     */
    private void setSelectionNode(ResourceLeafNode rln)
    {
        NavView view = (NavView) getSite().getWorkbenchWindow().getActivePage()
                .findView(NavView.ID);
        TreeViewer tv = view.getTreeViewer();
        StructuredSelection structuredSelection = new StructuredSelection(rln);
        tv.setSelection(structuredSelection, true);
    }

    /**
     * ���水ť��doSave()���õı��淽�������ȼ��ҳ�����ݣ� �������Ҫ����������ݿ⣬���������ڻָ���recoverMap��
     * �趨ҳ��Ϊδ���ģ��趨���水ť״̬������ ���������Ҫ������û���ʾ
     */
    private void saveMethod()
    {
        // ���Ϸ��� ���Ϸ�������ʾ �Ϸ��ͼ���
        if (checkDataisBlank())
        {
            // �������ݿ�������
            DebugOut.println("can save");
            // ���ø������ݿ�ķ���
            updateDb();
            recoverMap = getTradeData(input);
            // ���ñ༭����Ⱦ����
            setDirty(false);
            firePropertyChange(IEditorPart.PROP_DIRTY);
        } else
        {
            DebugOut.println("can not save");
        }
    }

    // ����Ϊʵ��ISearch�ӿ��еķ���
    @Override
    public String getTargetId()
    {
        return tradeIdText1.getText();
    }

    @Override
    public String getTargetName()
    {
        return tradeNameText1.getText();
    }

    @Override
    public void setTargetMap(Map<String, String> map)
    {
        contentMap = map;
        recoverMap = map;
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
    public void save()
    {
        saveMethod();
    }

    @Override
    public void setThisNode(ResourceLeafNode node)
    {
        resourceLeafNode = node;
    }

    @Override
    public ResourceLeafNode getThisNode()
    {
        return resourceLeafNode;
    }

    @Override
    public void setInputNode(ResourceLeafNode node)
    {
        ((TradeEditorInput) input).setSource(node);
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
        setTradeData(contentMap);
        // if (contentMap.get().equals("0-APP"))
        // {
        // unlockButton.setEnabled(true);
        // }
    }

    @Override
    public void setEditorPartName(String name)
    {
        this.setPartName(name);
    }

    @Override
    public void setEnable(boolean b)
    {
        disableControls();
    }

    @Override
    public void setSelectNode(ResourceLeafNode node)
    {
        setSelectionNode(node);
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
        return upProjectText1.getText();
    }

}
