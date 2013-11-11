package dev.editors.dataItem;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import dev.db.service.EditorDataitemServiceImpl;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
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
public class DataItemEditor extends EditorPart implements ISearch {
	public DataItemEditor() {
	}
	
	public static final String ID="dev.editor.DataItem.DataItem";	//�༭����ı�ʶ
	public DataItemInput input;									 	//Input�����
	private Text upProject1;										//��ѯ�������������ı���
	private Text searchText;										//�����ı���
	private Text upProject;											//��������������ı���
	private Text dataItemId;										//�������ʶ�ı���
	private Text FMLID;												//FML����ı���
	private Text dataItemDesc;										//������˵���ı���
	private Text dataItemLevel;										//��������ı���
	private Text dataItemName;										//�����������ı���
	private Text dataItemLen;										//���������ͳ����ı���
	private Text dataItemAop;										//������AOP�ı���
	private Combo dataItemType;										//���������������˵�
	private Button saveBtn;											//�޸İ�ť
	private Button clearBtn;										//������ť
	private Button dataitemNameButton;								//��ѡ��ť���������ƣ�
	private Button dataitemIDButton;								//��ѡ��ť�����ݱ�ʶ��
	private PreferenceStore ps;										//���ݿ�������Ϣ
	private EditorDataitemServiceImpl impl;							//���ݿ���������
	private Map< String, String> map;								//�洢��ѯ�����ݵ�Map
	private Map<String, String> restoreMap;
	private String[] dataItemTypeItem={"1-short","2-long","3-float","4-double","5-char","6-String"};
	private Button restoreBtn;
	private boolean bDirty;
	private Search search;
	//���������������˵�ѡ���ַ�������
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		try {
			saveData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		this.setSite(site);								//����Site
		this.setInput(input);							//����Input
		this.setPartName("������"+input.getName());		//���ñ༭������
		this.input=(DataItemInput)input;				//��Input��ʼ��
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return bDirty;
	}

	public void setDirty(boolean b){
		bDirty=b;
		saveBtn.setEnabled(b);
		if(b)
			firePropertyChange(PROP_DIRTY);
		else
			firePropertyChange(PROP_INPUT);
	} 
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
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
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		//�����༭����Ŀؼ�
		Group dataItemSearchGroup = new Group(composite, SWT.NONE);
		dataItemSearchGroup.setText("�������ѯ");
		dataItemSearchGroup.setLayout(new GridLayout(7, false));
		dataItemSearchGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(dataItemSearchGroup);
/*		Label upProjectLabel1 = new Label(dataItemSearchGroup, SWT.NONE);
		upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel1.setText("��������");
		
		upProject1 = new Text(dataItemSearchGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		dataitemNameButton = new Button(dataItemSearchGroup, SWT.RADIO);
		GridData gd_dataitemNameButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dataitemNameButton.widthHint = 80;
		dataitemNameButton.setLayoutData(gd_dataitemNameButton);
		dataitemNameButton.setText("����������");
		
		dataitemIDButton = new Button(dataItemSearchGroup, SWT.RADIO);
		dataitemIDButton.setSelection(true);
		GridData gd_dataitemIDButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dataitemIDButton.widthHint = 80;
		dataitemIDButton.setLayoutData(gd_dataitemIDButton);
		dataitemIDButton.setText("�������ʶ");
		
		searchText = new Text(dataItemSearchGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);
		searchText.addVerifyListener(new VerifyListener() {
			//�����ı�����������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=searchText.getText().length()<=32;
			}
		});
		
		Button searchBtn = new Button(dataItemSearchGroup, SWT.NONE);
		GridData gd_searchBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_searchBtn.widthHint = 80;
		searchBtn.setLayoutData(gd_searchBtn);
		searchBtn.setText("��ѯ");
		//���ò�ѯ��ť����Ϊ
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	DataItem.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					DataItem dataEditor = (DataItem)tmp.getEditor(false);
					if(dataEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(dataitemIDButton.getSelection())
						{
							if(dataEditor.dataItemId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dataEditor);
								return;
							}
						}
						else
						{
							if(dataEditor.dataItemName.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dataEditor);
								return;
							}
						}
					}
				}
				try {
				//�ж��Ǹ��ݱ�ʶ��ѯ���Ǹ������Ʋ�ѯ
				if(dataitemNameButton.getSelection())
					//����ѯ�Ľ������һ��Map��
						map=impl.queryDataitemByIdOrName("", searchText.getText(), ps);
				else
						map=impl.queryDataitemByIdOrName(searchText.getText(), "", ps);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				restoreMap=map;
				//����ѯ������ʱ������������༭����Ӧ���ı�����
				if(!map.isEmpty()){
					dataItemId.setText(map.get("dataitemid"));
					dataItemName.setText(map.get("dataname"));
					dataItemDesc.setText(map.get("datadesc"));
					
			//		 �ж�aoplevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
			//		����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
					
					if(map.get("datalvl").equals("1")){
						dataItemLevel.setText("GOLP");
						saveBtn.setVisible(false);
						clearBtn.setVisible(false);
						restoreBtn.setVisible(false);
						dataItemName.setEnabled(false);
						dataItemLen.setEnabled(false);
						dataItemType.setEnabled(false);
						dataItemDesc.setEnabled(false);
						dataItemAop.setEnabled(false);
					}
					else
					{
						dataItemLevel.setText("APP");
						saveBtn.setVisible(true);
						clearBtn.setVisible(true);
						restoreBtn.setVisible(true);
					}
				
					dataItemType.setText(dataItemTypeItem[Integer.parseInt(map.get("datatype"))-1]);
					dataItemLen.setText(map.get("datalen"));
					dataItemAop.setText(map.get("dataaop"));
					FMLID.setText(map.get("fmlid"));
					setPartName("������"+dataItemId.getText()+"��������"+upProject.getText());//�޸ı༭������
					getSite().getShell().setText("GOLP tool "+"������"+dataItemId.getText()+"��������"+upProject.getText());//�޸Ĺ��߱���
					input.setName(dataItemId.getText());//�ı�༭����ʶ
				}
				//��ѯ�������ݣ��򵯳�����Ի���
				else{
					MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
					box.setText("����");
					box.setMessage("�Ҳ�����ѯ�ļ�¼");
					box.open();
				}	
			}
		});
		*/
		Group dataItemGroup = new Group(composite, SWT.NONE);
		dataItemGroup.setLayout(new GridLayout(8, false));
		GridData gd_dataItemGroup = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_dataItemGroup.heightHint = 351;
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
		
		upProject = new Text(dataItemGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(dataItemGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label dataItemLevelLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLevelLabel.setText("�������");
		
		dataItemLevel = new Text(dataItemGroup, SWT.BORDER);
		dataItemLevel.setEnabled(false);
		GridData gd_dataItemLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLevel.widthHint = 70;
		dataItemLevel.setLayoutData(gd_dataItemLevel);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemIdLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemIdLabel.setText("�������ʶ");
		
		dataItemId = new Text(dataItemGroup, SWT.BORDER);
		dataItemId.setEnabled(false);
		GridData gd_dataItemId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemId.widthHint = 70;
		dataItemId.setLayoutData(gd_dataItemId);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemNameLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemNameLabel.setText("����������");
		
		dataItemName = new Text(dataItemGroup, SWT.BORDER);
		dataItemName.setEnabled(false);
		GridData gd_dataItemName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemName.widthHint = 70;
		dataItemName.setLayoutData(gd_dataItemName);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		dataItemName.addVerifyListener(new VerifyListener() {
			//�����ı�����������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=dataItemName.getText().length()<=32;
			}
		});
		dataItemName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label dataItemTypeLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemTypeLabel.setText("����������");
		
		dataItemType = new Combo(dataItemGroup, SWT.READ_ONLY);
		dataItemType.setEnabled(false);
		GridData gd_dataItemType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemType.widthHint = 53;
		dataItemType.setLayoutData(gd_dataItemType);
		dataItemType.setItems(dataItemTypeItem);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		//���������˵���Ϊ
		dataItemType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				//���˵����ǡ�5-char�����ߡ�6-String��ʱ��������ȱ�Ϊ�ɱ༭
				if(((Combo)e.getSource()).getText().equals("5-char")||((Combo)e.getSource()).getText().equals("6-String"))
					dataItemLen.setEnabled(true);
				//���򣬲��ɱ༭����������Ϊ0
				else{
					dataItemLen.setEnabled(false);
					dataItemLen.setText("0");
				}
			}
		});
		dataItemType.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label dataItemLenLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLenLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLenLabel.setText("�������");
		
		dataItemLen = new Text(dataItemGroup, SWT.BORDER);
		dataItemLen.setEnabled(false);
		GridData gd_dataItemLen = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLen.widthHint = 70;
		dataItemLen.setLayoutData(gd_dataItemLen);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		dataItemLen.addVerifyListener(new VerifyListener() {
			//�����ı������������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=e.text.length()<=5&&e.text.matches("^[0-9]+$");
			}
		});
		dataItemLen.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label FMLIDLabel = new Label(dataItemGroup, SWT.NONE);
		FMLIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		FMLIDLabel.setText("FML��ʶ");
		
		FMLID = new Text(dataItemGroup, SWT.BORDER);
		FMLID.setEnabled(false);
		GridData gd_FMLID = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_FMLID.widthHint = 70;
		FMLID.setLayoutData(gd_FMLID);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemAopLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemAopLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemAopLabel.setText("�������麯��");
		
		dataItemAop = new Text(dataItemGroup, SWT.BORDER);
		dataItemAop.setEnabled(false);
		GridData gd_dataItemAop = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemAop.widthHint = 70;
		dataItemAop.setLayoutData(gd_dataItemAop);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemDescLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemDescLabel.setText("������˵��");
		
		dataItemDesc = new Text(dataItemGroup, SWT.BORDER | SWT.V_SCROLL);
		dataItemDesc.setEnabled(false);
		dataItemDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		dataItemDesc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3));
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		clearBtn = new Button(dataItemGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("����");
		/*
		 * Ϊ����������ť������Ϊ�������°�ťʱ������ťΪ�������������Ϊ��������������
		 * �༭�Ŀؼ�����Ϊ���ã������޸ġ���ť����Ϊ���ã������Ϊ�����������Ա༭��
		 * �ؼ�����Ϊ�����ã����޸İ�ť����Ϊ�����á�
		 */
		clearBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="�༭"){
					nButton.setText("����");
					dataItemName.setEnabled(true);
					dataItemDesc.setEnabled(true);
					dataItemType.setEnabled(true);
					if(dataItemType.getText().equals(dataItemTypeItem[4])||dataItemType.getText().equals(dataItemTypeItem[5]))
					{
						dataItemLen.setEnabled(true);
					}
					saveBtn.setEnabled(true);
				}
				else{
					nButton.setText("�༭");
					dataItemName.setEnabled(false);
					dataItemDesc.setEnabled(false);
					dataItemType.setEnabled(false);
					dataItemLen.setEnabled(false);
					saveBtn.setEnabled(false);
				}
				
			}
		});
		new Label(dataItemGroup, SWT.NONE);
		
		saveBtn = new Button(dataItemGroup, SWT.NONE);
		GridData gd_savaBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_savaBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_savaBtn);
		saveBtn.setText("�޸�");
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				try {
					saveData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		restoreBtn = new Button(dataItemGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreBtn.setLayoutData(gd_restoreBtn);
		restoreBtn.setText("�ָ�");
		//���ûָ���ť��Ϊ
		restoreBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				dataItemName.setText(restoreMap.get("name"));
				dataItemDesc.setText(restoreMap.get("datadesc"));
				dataItemType.setText(dataItemTypeItem[new Integer(restoreMap.get("datatype"))-1]);
				dataItemLen.setText(restoreMap.get("datalen"));
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
		// TODO Auto-generated method stub
		try {
			datainit(input.getName());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
		getSite().getShell().setText("GOLP tool "+"������"+input.getName()+"��������"+upProject.getText());//���ù��ߵ�����
		setPartName("������"+input.getName()+"��������"+upProject.getText());
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
		ResourceLeafNode rln = ((DataItemInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6) + '/'+ prjId + ".properties";
		System.out.println("dbfiles==="+dbfiles);
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�����ݿ���в�ѯ������Map
		impl=new EditorDataitemServiceImpl();
		map=impl.queryDataitemByIdOrName(name, "", ps);
		restoreMap=map;
		//���θ��ؼ���ֵ
		dataItemId.setText(map.get("ID"));
		dataItemName.setText(map.get("NAME"));
		dataItemDesc.setText(map.get("datadesc"));
		//��aoplevelΪ��������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
		if(map.get("datalvl").equals("0")){
			dataItemLevel.setText("APP");
		}
		else
		{
			dataItemLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		dataItemType.setText(dataItemTypeItem[new Integer(map.get("datatype"))-1]);
		dataItemLen.setText(map.get("datalen"));
		dataItemAop.setText(map.get("dataaop"));
		FMLID.setText(map.get("fmlid"));
		upProject.setText(input.getSource().getRootProject().getId());
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
	private void saveData() throws SQLException{
		if(dataItemName.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("����");
			box.setMessage("���������Ʋ���Ϊ��");
			box.open();
		}
		else{
		List<String> datalist=new ArrayList<String>();
		datalist.add(dataItemName.getText());
		datalist.add(dataItemDesc.getText());
		datalist.add(dataItemType.getText().substring(0, 1));
		datalist.add(dataItemLen.getText());
		datalist.add(dataItemAop.getText());
		try {
			impl.updateDataitemById(dataItemId.getText(), datalist, ps);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		restoreMap=impl.queryDataitemByIdOrName(dataItemId.getText(), "", ps);
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
		// TODO Auto-generated method stub
		return dataItemId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return dataItemName.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		// TODO Auto-generated method stub
		this.map=map;
		this.restoreMap=map;
	}

	@Override
	public PreferenceStore getPs() {
		// TODO Auto-generated method stub
		return ps;
	}

	@Override
	public void setUnLockButtonText(String text) {
		// TODO Auto-generated method stub
		clearBtn.setText(text);
	}

	@Override
	public void setThisNode(ResourceLeafNode node) {
		// TODO Auto-generated method stub
		input.setSource(node);
	}

	@Override
	public ResourceLeafNode getThisNode() {
		// TODO Auto-generated method stub
		return input.getSource();
	}

	@Override
	public void setInputNode(ResourceLeafNode node) {
		// TODO Auto-generated method stub
		input.setSource(node);
	}

	@Override
	public int showMessage(int style, String title, String message) {
		// TODO Auto-generated method stub
		MessageBox box=new MessageBox(getSite().getShell(), style);
		box.setText(title);
		box.setMessage(message);
		box.open();
		return 1;
	}

	@Override
	public Search getSearch() {
		// TODO Auto-generated method stub
		return search;
	}

	@Override
	public void setControlsText() {
		// TODO Auto-generated method stub
		dataItemId.setText(map.get("ID"));
		dataItemName.setText(map.get("NAME"));
		dataItemDesc.setText(map.get("datadesc"));
		/*
		 �ж�aoplevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
		����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
		*/
		if(map.get("datalvl").equals("1")){
			dataItemLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
			dataItemName.setEnabled(false);
			dataItemLen.setEnabled(false);
			dataItemType.setEnabled(false);
			dataItemDesc.setEnabled(false);
			dataItemAop.setEnabled(false);
		}
		else
		{
			dataItemLevel.setText("APP");
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreBtn.setVisible(true);
		}
	
		dataItemType.setText(dataItemTypeItem[Integer.parseInt(map.get("datatype"))-1]);
		dataItemLen.setText(map.get("datalen"));
		dataItemAop.setText(map.get("dataaop"));
		FMLID.setText(map.get("fmlid"));
		input.setName(dataItemId.getText());
		setDirty(false);
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName("ԭ�ӽ���"+dataItemId.getText()+"��������"+upProject.getText());
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(!b){
			clearBtn.setText("�༭");
			dataItemName.setEnabled(false);
			dataItemDesc.setEnabled(false);
			dataItemType.setEnabled(false);
			dataItemLen.setEnabled(false);
			saveBtn.setEnabled(false);
		}
		else{
			clearBtn.setText("����");
			dataItemName.setEnabled(true);
			dataItemDesc.setEnabled(true);
			dataItemType.setEnabled(true);
			dataItemLen.setEnabled(true);
			saveBtn.setEnabled(true);
		}
	}

	@Override
	public void mySave() {
		// TODO Auto-generated method stub
		try {
			saveData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
