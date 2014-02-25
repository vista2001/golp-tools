/* �ļ�����       TradeEditor.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.29
 * �޸����ݣ�   1.��dataItemDesc�����Ը���ΪSWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI��
 *         2.�޸�init�����е�setPartName�����Ӷ��������̵���ʾ�������ͽ���˴ӵ����д�һ���༭���󣬵���ñ༭��
 *         �Ĺرհ�ťʱ���༭����δ�رգ������ڱ༭���ı�������ʾ�˸�������ݣ������������ԭ������init������setFocus
 *         �����ڵ���setPartNameʱ������һ�£�
 *         3.�޸��˲��ֲ��֣�
 *         4.���Ǹ����е�dispose������ʹ�������б༭�����ر�ʱ���ָ����ߵı��⡰GOLP TOOL����
 *         5.�ڽ��м���APP/GOLP�����ж�ʱ������ֱ��ʹ��0��1�����֣���Ϊʹ��dev.util.Constants��ĳ�����
 *         6.��UI��ͳһʹ��0-APP��1-GOLP���ֱ�ʾ��
 *         7.��DebugOut.println�����滻System.out.println������
 *         8.���ӱ༭���������ܣ�
 *         9.ͳһʹ��File.separator��
 * �޸��ˣ�        zxh
 * �޸�ʱ�䣺    2013.12.2
 * �޸����ݣ�    1.�޸ı�������     
 *      
 */

package dev.editors.dataItem;

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
import org.eclipse.swt.widgets.Combo;
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

import dev.db.service.EditorDataitemServiceImpl;
import dev.editors.IGetUpProject;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.generate.fml.FmlId;
import dev.model.base.ResourceLeafNode;
import dev.util.Constants;
import dev.util.DebugOut;
import dev.util.RegExpCheck;
import dev.views.NavView;
/**
 * Dataitem��༭����
 * <p>�����̳���EditorPart�࣬��DataitemEditorInput��һ�����Dataitem�༭���Ĺ���<br>
 * �ڱ༭����ʼ����ʱ��ͨ��DataitemEditorInput�ഫ���������Init�����жԱ༭��
 * ���г�ʼ����Ȼ����createPartControl��������ɶԱ༭���Ŀؼ��Ĺ��죬������
 * �ÿؼ�����Ҫ�ǡ���ѯ�������������޸ġ���ť������Ϊ���ı�����������ƣ���createPartControl
 * ������������dataInit������ɴ����ݿ��л�����ݲ������ı����У���ɶ�����
 * �༭���ĳ�ʼ����
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class DataItemEditor extends EditorPart implements ISearch, IGetUpProject {
	public DataItemEditor() {
	}
	
	public static final String ID="dev.editor.DataItem.DataItem";	//�༭����ı�ʶ
	public DataItemEditorInput input;									 	//Input�����
	private Text upProjectText;											//��������������ı���
	private Text dataItemIdText;										//�������ʶ�ı���
	private Text FMLIDText;												//FML����ı���
	private Text dataItemDescText;										//������˵���ı���
	private Text dataItemLevelText;										//��������ı���
	private Text dataItemNameText;										//�����������ı���
	private Text dataItemLenText;										//���������ͳ����ı���
	private Text dataItemAopText;										//������AOP�ı���
	private Combo dataItemTypeCombo;								//���������������˵�
	private Button saveButton;											//�޸İ�ť
	private Button unlockButton;										//������ť
	private PreferenceStore ps;										//���ݿ�������Ϣ
	private EditorDataitemServiceImpl impl;							//���ݿ���������
	private Map< String, String> map;								//�洢��ѯ�����ݵ�Map
	private Map<String, String> restoreMap;
	private String[] dataItemTypeItem={"0-int", "1-long", "2-double",
	                                   "3-char", "4-char[]", "5-String"};
	private Button restoreButton;
	private boolean bDirty;
	private Search search;
	//���������������˵�ѡ���ַ�������
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			saveData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
	}

	/**
	 * ���ڶԱ༭�����г�ʼ��<br>
	 * ����site,input����Ա༭�����г�ʼ�����򿪱༭����ʱ�����ȵ��õķ���
	 * @param site    �༭����site
	 * @param input	    �༭�����׵�input
	 * @return û�з���ֵ
	 * */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);								//����Site
		this.setInput(input);							//����Input
		this.input=(DataItemEditorInput)input;              //��Input��ʼ��
        this.setPartName("������" + " " + this.input.getName() + " " + "��������"
                + " " + this.input.getSource().getRootProject().getId()); // ���ñ༭������
	}

	@Override
	public boolean isDirty() {
		return bDirty;
	}

	public void setDirty(boolean b){
		bDirty=b;
		saveButton.setEnabled(b);
		restoreButton.setEnabled(b);
		if(b)
			firePropertyChange(PROP_DIRTY);
		else
			firePropertyChange(PROP_INPUT);
	} 
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * �ڱ༭���ϴ������ֿռ䣬���ò��֣��Լ��Կռ���г�ʼ��<br>
	 * ����Ҫ�Ŀؼ�������parent����пؼ�����Ϊ������������ﱻ�趨����������ڵ�����Init�󱳵���
	 * @param parent    ���пؼ���parent.
	 * @return û�з���ֵ
	 * */
	@Override
	public void createPartControl(Composite parent) {
	    ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
	    
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		//�����༭����Ŀؼ�
		Group dataItemSearchGroup = new Group(composite, SWT.NONE);
		dataItemSearchGroup.setText("�������ѯ");
		dataItemSearchGroup.setLayout(new GridLayout(7, false));
		dataItemSearchGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(dataItemSearchGroup);

		Group dataItemGroup = new Group(composite, SWT.NONE);
		dataItemGroup.setLayout(new GridLayout(8, false));
		GridData gd_dataItemGroup = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_dataItemGroup.widthHint = 584;
		dataItemGroup.setLayoutData(gd_dataItemGroup);
		dataItemGroup.setText("�������");
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(dataItemGroup, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(dataItemGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("��������");
		
		upProjectText = new Text(dataItemGroup, SWT.BORDER);
		upProjectText.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProjectText.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(dataItemGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label dataItemLevelLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLevelLabel.setText("�������");
		
		dataItemLevelText = new Text(dataItemGroup, SWT.BORDER);
		dataItemLevelText.setEnabled(false);
		GridData gd_dataItemLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLevel.widthHint = 70;
		dataItemLevelText.setLayoutData(gd_dataItemLevel);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemIdLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemIdLabel.setText("�������ʶ");
		
		dataItemIdText = new Text(dataItemGroup, SWT.BORDER);
		dataItemIdText.setEnabled(false);
		GridData gd_dataItemId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemId.widthHint = 70;
		dataItemIdText.setLayoutData(gd_dataItemId);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemNameLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemNameLabel.setText("*����������");
		
		dataItemNameText = new Text(dataItemGroup, SWT.BORDER);
		dataItemNameText.setEnabled(false);
		GridData gd_dataItemName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemName.widthHint = 70;
		dataItemNameText.setLayoutData(gd_dataItemName);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
//		dataItemName.addVerifyListener(new VerifyListener() {
//			//�����ı�����������
//			@Override
//			public void verifyText(VerifyEvent e) {
//				if(e.character!=8)
//					e.doit=dataItemName.getText().length()<=32;
//			}
//		});
		dataItemNameText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label dataItemTypeLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemTypeLabel.setText("*����������");
		
		dataItemTypeCombo = new Combo(dataItemGroup, SWT.READ_ONLY);
		dataItemTypeCombo.setEnabled(false);
		GridData gd_dataItemType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemType.widthHint = 53;
		dataItemTypeCombo.setLayoutData(gd_dataItemType);
		dataItemTypeCombo.setItems(dataItemTypeItem);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		//���������˵���Ϊ
//		dataItemTypeCombo.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e){
//				//���˵����ǡ�4-char[]�����ߡ�5-String��ʱ��������ȱ�Ϊ�ɱ༭
//				if ( dataItemTypeCombo.getText().equals("4-char[]")
//				   ||dataItemTypeCombo.getText().equals("5-String"))
//				{
//				    dataItemLenText.setText("");
//				    dataItemLenText.setEnabled(true);
//				}
//				//���򣬲��ɱ༭�����ȱ�-1
//				else{
//				    dataItemLenText.setEnabled(false);
//				    dataItemLenText.setText("-1");
//				}
//			}
//		});
		dataItemTypeCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			    
			    // �ڡ��༭��״̬�£��ü���������Ч��
			    if(unlockButton.getText().equals("����"))
			    {
			      //���˵����ǡ�4-char[]�����ߡ�5-String��ʱ��������ȱ�Ϊ�ɱ༭
	                if ( dataItemTypeCombo.getText().equals("4-char[]")
	                   ||dataItemTypeCombo.getText().equals("5-String"))
	                {
	                    //�����Ͳ���char[]��Stringʱ��dataItemLenText��������-1��
	                    //��ʱ�������ͱ�Ϊchar[]��String����Ӧ��֮ǰ�����ݣ�-1����ա�
	                    if(dataItemLenText.getText().equals("-1"))
	                    {
	                        dataItemLenText.setText("");
	                    }
	                    dataItemLenText.setEnabled(true);
	                }
	                //���򣬲��ɱ༭�����ȱ�-1
	                else
	                {
	                    dataItemLenText.setEnabled(false);
	                    dataItemLenText.setText("-1");
	                }
	                setDirty(true);
			    }
				
			}
		});
		
		Label dataItemLenLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLenLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLenLabel.setText("*�������");
	
		dataItemLenText= new Text(dataItemGroup, SWT.BORDER);
		dataItemLenText.setEnabled(false);
		
		GridData gd_dataItemLen = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLen.widthHint = 70;
		dataItemLenText.setLayoutData(gd_dataItemLen);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
//		dataItemLen.addVerifyListener(new VerifyListener() {
//			//�����ı������������
//			@Override
//			public void verifyText(VerifyEvent e) {
//				if(e.character!=8)
//					e.doit=e.text.length()<=5&&e.text.matches("^[0-9]+$");
//			}
//		});
		dataItemLenText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label FMLIDLabel = new Label(dataItemGroup, SWT.NONE);
		FMLIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		FMLIDLabel.setText("FML��ʶ");
		
		FMLIDText = new Text(dataItemGroup, SWT.BORDER);
		FMLIDText.setEnabled(false);
		GridData gd_FMLID = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_FMLID.widthHint = 70;
		FMLIDText.setLayoutData(gd_FMLID);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemAopLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemAopLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemAopLabel.setText("�������麯��");
		
		dataItemAopText = new Text(dataItemGroup, SWT.BORDER);
		dataItemAopText.setEnabled(false);
		GridData gd_dataItemAop = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemAop.widthHint = 70;
		dataItemAopText.setLayoutData(gd_dataItemAop);
		dataItemAopText.addModifyListener(new ModifyListener()
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
		
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemDescLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemDescLabel.setText("*����");
		
		dataItemDescText = new Text(dataItemGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		dataItemDescText.setEnabled(false);
		dataItemDescText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		dataItemDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3));
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		unlockButton = new Button(dataItemGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		unlockButton.setLayoutData(gd_clearBtn);
		unlockButton.setText("�༭");
		/*
		 * Ϊ����������ť������Ϊ�������°�ťʱ������ťΪ�������������Ϊ��������������
		 * �༭�Ŀؼ�����Ϊ���ã������޸ġ���ť����Ϊ���ã������Ϊ�����������Ա༭��
		 * �ؼ�����Ϊ�����ã����޸İ�ť����Ϊ�����á�
		 */
		unlockButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="�༭"){
					nButton.setText("����");
					dataItemNameText.setEnabled(true);
					dataItemDescText.setEnabled(true);
					dataItemTypeCombo.setEnabled(true);
					if(dataItemTypeCombo.getText().equals(dataItemTypeItem[4])||dataItemTypeCombo.getText().equals(dataItemTypeItem[5]))
					{
						dataItemLenText.setEnabled(true);
					}
					dataItemAopText.setEnabled(true);
				}
				else{
					nButton.setText("�༭");
					dataItemNameText.setEnabled(false);
					dataItemDescText.setEnabled(false);
					dataItemTypeCombo.setEnabled(false);
					dataItemLenText.setEnabled(false);
					dataItemAopText.setEnabled(false);
				}
				
			}
		});
		new Label(dataItemGroup, SWT.NONE);
		
		saveButton = new Button(dataItemGroup, SWT.NONE);
		GridData gd_savaBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_savaBtn.widthHint = 80;
		saveButton.setLayoutData(gd_savaBtn);
		saveButton.setText("����");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				try {
					saveData();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		restoreButton = new Button(dataItemGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreButton.setLayoutData(gd_restoreBtn);
		restoreButton.setText("�ָ�");
		//���ûָ���ť��Ϊ
		restoreButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				dataItemNameText.setText(restoreMap.get("NAME"));
				dataItemDescText.setText(restoreMap.get("datadesc"));
				dataItemTypeCombo.setText(dataItemTypeItem[new Integer(restoreMap.get("datatype"))]);
				dataItemLenText.setText(restoreMap.get("datalen"));
				dataItemAopText.setText(restoreMap.get("dataaop"));
				setDirty(false);
			}
		});
		new Label(dataItemGroup, SWT.NONE);
		/*
		 * Ϊ���޸ġ���ť������Ϊ�������޸İ�ť���������������ı���Ϊ�գ��򵯳�����Ի���
		 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditordataItemServiceImpl���
		 * updatedataItemById������List��������滻��ʶΪAopId
		 * �����ݡ�
		 * */
		scrolledComposite.setContent(composite);
        scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));
		
		try {
			datainit(input.getName());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * ���༭��ѡ�е�ʱ�����<br>
	 * ���༭���Ĵ��ڱ�ѡ��ʱ���Զ��������������ִ�з����е����
	 * @return û�з���ֵ
	 * */
	@Override
	public void setFocus() {
        getSite().getShell().setText(
                "GOLP TOOL" + " " + "������" + " " + input.getName() + " "
                        + "��������" + " " + upProjectText.getText());// ���ù��ߵ�����
		setPartName("������" + " " + input.getName() + " " + "��������" + " " + upProjectText.getText());
		setSelectNode(input.getSource());
	}
	
	/**
	 * �Ա༭���Ŀؼ����г�ʼ��<br>
	 * �����ҵ����ݿ��������Ϣ��Ȼ�����name��������Ϣ����EditorAopServiceImpl��
	 * ��queryAopByIdOrName�����õ���ʼ�������ݷ���Map����һ������ؼ��С�
	 * @param name   Ҫ��ѯ�����ݵı�ʶ
	 * @return û�з���ֵ
	 * */
	private void datainit(String name) throws SQLException{
		//������ݿ��������Ϣ
		ResourceLeafNode rln = ((DataItemEditorInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6) + File.separator+ prjId + ".properties";
		DebugOut.println("dbfiles==="+dbfiles);
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//�����ݿ���в�ѯ������Map
		impl=new EditorDataitemServiceImpl();
		map=impl.queryDataitemByIdOrName(name, "", ps);
		restoreMap=map;
		//���θ��ؼ���ֵ
		dataItemIdText.setText(map.get("ID"));
		dataItemNameText.setText(map.get("NAME"));
		dataItemDescText.setText(map.get("datadesc"));
		//��aoplevelΪ����Constants.APP��������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
		if(map.get("datalvl").equals(Constants.APP)){
			dataItemLevelText.setText("0-APP");
		}
		else
		{
			dataItemLevelText.setText("1-GOLP");
			saveButton.setVisible(false);
			unlockButton.setVisible(false);
			restoreButton.setVisible(false);
		}
		dataItemTypeCombo.setText(dataItemTypeItem[new Integer(map.get("datatype"))]);
		dataItemLenText.setText(map.get("datalen"));
		dataItemAopText.setText(map.get("dataaop"));
		FMLIDText.setText(map.get("fmlid"));
		upProjectText.setText(input.getSource().getRootProject().getId());
//		upProject1.setText(upProject.getText());
		setDirty(false);
	}
	/**
	 * �������ݵķ���
	 * ���ڱ��水ť��doSave�����ı�������������޸İ�ť����ԭ�ӽ��������ı���Ϊ�գ��򵯳�����Ի���
	 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorDataitemServiceImpl���updateDataitemById������
	 * List��������滻��ʶΪdataitemId�����ݡ�
	 * @throws SQLException
	 */
    private void saveData() throws SQLException
    {
        if (dataItemNameText.getText().trim().isEmpty()
            || dataItemDescText.getText().trim().isEmpty())
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
        }
        else if(( dataItemTypeCombo.getText().equals("4-char[]")
                || dataItemTypeCombo.getText().equals("5-String"))
                && (RegExpCheck.isPositiveInteger(dataItemLenText.getText()) == false))
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ������");
        }
        else
        {
            List<String> datalist = new ArrayList<String>();
            datalist.add(dataItemNameText.getText());
            datalist.add(dataItemDescText.getText());
            datalist.add(dataItemTypeCombo.getText().substring(0, 1));
            datalist.add(dataItemLenText.getText());
            datalist.add(dataItemAopText.getText());
            
            // �ֲ�����fmlId��ʼ��0L
            Long fmlId = 0L;
            try
            {
                //ͨ���༭���е����������ƣ�������Id���������ͣ�����fmlId
                fmlId = FmlId.getFmlId(dataItemNameText.getText(), 
                                            Integer.parseInt(dataItemIdText.getText()), 
                                            dataItemTypeCombo.getText().substring(2));
            }
            catch (NumberFormatException | IOException | InterruptedException e)
            {
                e.printStackTrace();
                
                //�����������׳��쳣����fmlId��Ϊ-1L
                fmlId = -1L;
            }
            datalist.add(fmlId + "");
            
            // �ڼ����fmlId�󣬸��±༭���е�fmlId
            FMLIDText.setText(fmlId + "");
            
            try
            {
                impl.updateDataitemById(dataItemIdText.getText(), datalist, ps);
            } catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            restoreMap = impl.queryDataitemByIdOrName(dataItemIdText.getText(),
                    "", ps);
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
	public String getTargetId() {
		return dataItemIdText.getText();
	}

	@Override
	public String getTargetName() {
		return dataItemNameText.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		this.map=map;
		this.restoreMap=map;
	}

	@Override
	public PreferenceStore getPs() {
		return ps;
	}

	@Override
	public void setUnLockButtonText(String text) {
		unlockButton.setText(text);
	}

	@Override
	public void setThisNode(ResourceLeafNode node) {
		input.setSource(node);
	}

	@Override
	public ResourceLeafNode getThisNode() {
		return input.getSource();
	}

	@Override
	public void setInputNode(ResourceLeafNode node) {
		input.setSource(node);
	}

	@Override
	public int showMessage(int style, String title, String message) {
		MessageBox box=new MessageBox(getSite().getShell(), style);
		box.setText(title);
		box.setMessage(message);
		return box.open();
	}

	@Override
	public Search getSearch() {
		return search;
	}

	@Override
	public void setControlsText() {
		dataItemIdText.setText(map.get("ID"));
		dataItemNameText.setText(map.get("NAME"));
		dataItemDescText.setText(map.get("datadesc"));
		/*
		 �ж�aoplevel�Ƿ�ΪConstants.APP����������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
		����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
		*/
		if(map.get("datalvl").equals(Constants.GOLP)){
			dataItemLevelText.setText("1-GOLP");
			saveButton.setVisible(false);
			unlockButton.setVisible(false);
			restoreButton.setVisible(false);
			dataItemNameText.setEnabled(false);
			dataItemLenText.setEnabled(false);
			dataItemTypeCombo.setEnabled(false);
			dataItemDescText.setEnabled(false);
			dataItemAopText.setEnabled(false);
		}
		else
		{
			dataItemLevelText.setText("0-APP");
			saveButton.setVisible(true);
			unlockButton.setVisible(true);
			restoreButton.setVisible(true);
		}
	
		dataItemTypeCombo.setText(dataItemTypeItem[Integer.parseInt(map.get("datatype"))]);
		dataItemLenText.setText(map.get("datalen"));
		dataItemAopText.setText(map.get("dataaop"));
		FMLIDText.setText(map.get("fmlid"));
		input.setName(dataItemIdText.getText());
		setDirty(false);
	}

	@Override
	public void setEditorPartName(String name) {
		setPartName("ԭ�ӽ���"+dataItemIdText.getText()+"��������"+upProjectText.getText());
	}

	@Override
	public void setEnable(boolean b) {
		if(!b){
			unlockButton.setText("�༭");
			dataItemNameText.setEnabled(false);
			dataItemDescText.setEnabled(false);
			dataItemTypeCombo.setEnabled(false);
			dataItemLenText.setEnabled(false);
			dataItemAopText.setEnabled(false);
			saveButton.setEnabled(false);
		}
		else{
			unlockButton.setText("����");
			dataItemNameText.setEnabled(true);
			dataItemDescText.setEnabled(true);
			dataItemTypeCombo.setEnabled(true);
			dataItemLenText.setEnabled(true);
			dataItemAopText.setEnabled(true);
			saveButton.setEnabled(true);
		}
	}

	@Override
	public void save() {
		try {
			saveData();
		} catch (SQLException e) {
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
