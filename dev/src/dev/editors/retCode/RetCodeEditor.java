package dev.editors.retCode;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import dev.db.service.EditorRetcodeServiceImpl;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
/**
 * Retcode��༭����
 * <p>�����̳���EditorPart�࣬��RetcodeEditorInput��һ�����Retcode�༭���Ĺ���<br>
 * �ڱ༭����ʼ����ʱ��ͨ��RetcodeEditorInput�ഫ���������Init�����жԱ༭��
 * ���г�ʼ����Ȼ����createPartControl��������ɶԱ༭���Ŀؼ��Ĺ��죬������
 * �ÿؼ�����Ҫ�ǡ���ѯ�������������޸ġ���ť������Ϊ���ı�����������ƣ���createPartControl
 * ������������dataInit������ɴ����ݿ��л�����ݲ������ı����У���ɶ�����
 * �༭���ĳ�ʼ����
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class RetCodeEditor extends EditorPart implements ISearch{
	public static final String ID="dev.editor.retcode.RetCodeEditor";	////�༭����ı�ʶ
	private Text upProject1;											//��ѯ�������������ı���
	private Text searchText;											//�����ı���
	private Text retCodeId;												//��Ӧ���ʶ�ı���
	private Text retCodeValue;											//��Ӧ��ֵ�ı���
	private Text retCodeDesc;											//��Ӧ��˵���ı���
	private Text upProject;												//��������������ı���
	private Text retCodeLevel;											//��Ӧ�뼶���ı���
	private RetCodeInput input;											//inpiu����
	private Button saveBtn;												//�޸İ�ť
	private Button clearBtn;											//������ť
	private Button restoreBtn;
	private Button retcodeValueButton;									//��ѡ��ť������ֵ��
	private Button retcodeIDButton;										//��ѡ��ť�����ݱ�ʶ��
	private EditorRetcodeServiceImpl impl;								//���ݿ���������
	private Map<String,String> map;										//�洢��ѯ�����ݵ�Map
	private Map<String, String> restoreMap;
	private PreferenceStore ps;											//���ݿ�������Ϣ
	private boolean bDirty=false;
	private Search search;
	public RetCodeEditor() {	
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		saveData();
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
		this.setSite(site);							//����site
		this.setInput(input);						//����input
		this.setPartName("��Ӧ��"+input.getName());	//���ñ༭������
		this.input=(RetCodeInput)input;				//��Input��ʼ��
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
		Group retcodeSearchGroup = new Group(composite, SWT.NONE);
		retcodeSearchGroup.setText("��Ӧ���ѯ");
		retcodeSearchGroup.setLayout(new GridLayout(7, false));
		retcodeSearchGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(retcodeSearchGroup);
/*		Label upProjectLabel1 = new Label(retcodeSearchGroup, SWT.NONE);
		upProjectLabel1.setText("��������");
		
		upProject1 = new Text(retcodeSearchGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		retcodeValueButton = new Button(retcodeSearchGroup, SWT.RADIO);
		GridData gd_retcodeValueButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_retcodeValueButton.widthHint = 80;
		retcodeValueButton.setLayoutData(gd_retcodeValueButton);
		retcodeValueButton.setText("��Ӧ��ֵ");
		
		retcodeIDButton = new Button(retcodeSearchGroup, SWT.RADIO);
		retcodeIDButton.setSelection(true);
		GridData gd_retcodeIDButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_retcodeIDButton.widthHint = 80;
		retcodeIDButton.setLayoutData(gd_retcodeIDButton);
		retcodeIDButton.setText("��Ӧ���ʶ");
		
		searchText = new Text(retcodeSearchGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
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
		
		Button Search = new Button(retcodeSearchGroup, SWT.NONE);
		GridData gd_Search = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_Search.widthHint = 80;
		Search.setLayoutData(gd_Search);
		Search.setText("��ѯ");
		//���ò�ѯ��ť����Ϊ
		Search.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	RetCodeEditor.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					RetCodeEditor retcodeEditor = (RetCodeEditor)tmp.getEditor(false);
					if(retcodeEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(retcodeIDButton.getSelection())
						{
							if(retcodeEditor.retCodeId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(retcodeEditor);
								return;
							}
						}
						else
						{
							if(retcodeEditor.retCodeValue.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(retcodeEditor);
								return;
							}
						}
					}
				}
					//�ж��Ǹ��ݱ�ʶ��ѯ���Ǹ������Ʋ�ѯ
					if(retcodeValueButton.getSelection())
						try {
							//����ѯ�Ľ������һ��Map��
							map=impl.queryRetcodeByIdOrName("", searchText.getText(), ps);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					else
						try {
							map=impl.queryRetcodeByIdOrName(searchText.getText(),"", ps);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					restoreMap=map;
					//����ѯ������ʱ������������༭����Ӧ���ı�����
					if(!map.isEmpty()){
						retCodeId.setText(map.get("retcodeid"));
						retCodeValue.setText(map.get("retcodevalue"));
						retCodeDesc.setText(map.get("retcodedesc"));
						
			//			 �ж�aoplevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
			//			����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
					
						if(map.get("retcodelevel").equals("0")){
							saveBtn.setVisible(true);
							clearBtn.setVisible(true);
							restoreBtn.setVisible(true);
							retCodeLevel.setText("APP");
						}
						else{
							retCodeLevel.setText("GOLP");
							saveBtn.setVisible(false);
							clearBtn.setVisible(false);
							restoreBtn.setVisible(false);
						}
						setPartName("��Ӧ��"+retCodeId.getText()+"��������"+upProject.getText());//�޸ı༭������
						getSite().getShell().setText("GOLP tool "+"��Ӧ��"+input.getName()+"��������"+upProject.getText());//�޸Ĺ��߱���
						input.setName(retCodeId.getText());//�ı�༭����ʶ
						setDirty(false);
					}
					//��ѯ�������ݣ��򵯳�����Ի���
					else{
						MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
						box.setText("����");
						box.setMessage("�Ҳ�����ѯ�ļ�¼");
						box.open();
					}
				}
		});*/
		
		Group retcodeGroup = new Group(composite, SWT.NONE);
		GridData gd_retcodeGroup = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_retcodeGroup.heightHint = 365;
		gd_retcodeGroup.widthHint = 575;
		retcodeGroup.setLayoutData(gd_retcodeGroup);
		retcodeGroup.setText("��Ӧ���");
		retcodeGroup.setLayout(new GridLayout(8, false));
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(retcodeGroup, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(retcodeGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("��������");
		
		upProject = new Text(retcodeGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(retcodeGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label retCodeLevelLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		retCodeLevelLabel.setText("�������");
		
		retCodeLevel = new Text(retcodeGroup, SWT.BORDER);
		retCodeLevel.setEnabled(false);
		GridData gd_retCodeLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_retCodeLevel.widthHint = 70;
		retCodeLevel.setLayoutData(gd_retCodeLevel);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label retCodeIdLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		retCodeIdLabel.setText("��Ӧ���ʶ");
		
		retCodeId = new Text(retcodeGroup, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_retCodeId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_retCodeId.widthHint = 70;
		retCodeId.setLayoutData(gd_retCodeId);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label retCodeValueLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeValueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		retCodeValueLabel.setText("��Ӧ��ֵ");
		
		retCodeValue = new Text(retcodeGroup, SWT.BORDER);
		retCodeValue.setEnabled(false);
		retCodeValue.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		GridData gd_retCodeValue = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_retCodeValue.widthHint = 70;
		retCodeValue.setLayoutData(gd_retCodeValue);
		//�����ı������������
		retCodeValue.addVerifyListener(new VerifyListener() { 
		      public void verifyText(VerifyEvent event) { 
		          event.doit=event.text.length()<=5&&event.text.matches("^[0-9]+$");
		      }
		});
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label retCodeDescLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		retCodeDescLabel.setText("��Ӧ��˵��");
		
		retCodeDesc = new Text(retcodeGroup, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		retCodeDesc.setEnabled(false);
		GridData gd_retCodeDesc = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 3);
		gd_retCodeDesc.heightHint = 83;
		retCodeDesc.setLayoutData(gd_retCodeDesc);
		//�����ı������������
		retCodeDesc.addVerifyListener(new VerifyListener() { 
		      public void verifyText(VerifyEvent event) { 
		    	  if(event.character!=8)
		          event.doit=retCodeDesc.getText().length()<=32;
		      }
		});
		retCodeDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		clearBtn = new Button(retcodeGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("�༭");
		/*
		 * Ϊ����������ť������Ϊ�������°�ťʱ������ťΪ�������������Ϊ��������������
		 * �༭�Ŀؼ�����Ϊ���ã������޸ġ���ť����Ϊ���ã������Ϊ�����������Ա༭��
		 * �ؼ�����Ϊ�����ã����޸İ�ť����Ϊ�����á�
		 */
		clearBtn.addSelectionListener(new  SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="�༭"){
				nButton.setText("����");
				retCodeValue.setEnabled(true);
				retCodeDesc.setEnabled(true);
				saveBtn.setEnabled(true);
			}
				else{
					nButton.setText("�༭");
					retCodeValue.setEnabled(false);
					retCodeDesc.setEnabled(false);
					saveBtn.setEnabled(false);
				}
			}
		});
		new Label(retcodeGroup, SWT.NONE);
		
		saveBtn = new Button(retcodeGroup, SWT.NONE);
		GridData gd_savaBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_savaBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_savaBtn);
		saveBtn.setText("�޸�");
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				saveData();
			}
		});
		/*
		 * Ϊ���޸ġ���ť������Ϊ�������޸İ�ť������Ӧ��ֵ�ı���Ϊ�գ��򵯳�����Ի���
		 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorretCodeServiceImpl���
		 * updateretCodeById������List��������滻��ʶΪAopId
		 * �����ݡ�
		 * */
		
		restoreBtn = new Button(retcodeGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreBtn.setLayoutData(gd_restoreBtn);
		restoreBtn.setText("�ָ�");
		//���ûָ���ť��Ϊ
		restoreBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				retCodeValue.setText(restoreMap.get("name"));
				retCodeDesc.setText(restoreMap.get("retcodedesc"));
				setDirty(false);
			}
		});
		new Label(retcodeGroup, SWT.NONE);
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
		getSite().getShell().setText("GOLP tool "+"��Ӧ��"+input.getName()+"��������"+upProject.getText());
		setPartName("��̬��"+input.getName()+"��������"+upProject.getText());
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
		ResourceLeafNode rln = ((RetCodeInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6) + '/'+ prjId + ".properties";
		System.out.println("dbfiles==="+dbfiles);
		//�����ݿ���в�ѯ������Map
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		impl=new EditorRetcodeServiceImpl();
		map=impl.queryRetcodeByIdOrName(name, "", ps);
		restoreMap=map;
		//���θ��ؼ���ֵ
		retCodeId.setText(map.get("ID"));
		retCodeValue.setText(map.get("NAME"));
		retCodeDesc.setText(map.get("retcodedesc"));
		//��aoplevelΪ��������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
		if(map.get("retcodelevel").equals("0"))
			retCodeLevel.setText("APP");
		else{
			retCodeLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		upProject.setText(input.getSource().getRootProject().getId());
//		upProject1.setText(upProject.getText());
		setDirty(false);
	} 
	/**
	 * �������ݵķ���
	 * ���ڱ��水ť��doSave�����ı�������������޸İ�ť����ԭ�ӽ��������ı���Ϊ�գ��򵯳�����Ի���
	 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorRetcodeServiceImpl���updateRetcodeById������
	 * List��������滻��ʶΪRetcodeId�����ݡ�
	 * @throws SQLException
	 */
	private void saveData(){
		if(retCodeValue.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("����");
			box.setMessage("���������Ʋ���Ϊ��");
			box.open();
		}
		else{
			List<String> datalist=new ArrayList<String>();
			datalist.add(retCodeValue.getText());
			datalist.add(retCodeDesc.getText());
			try {
				impl.updateRetcodeById(input.getName(), datalist, ps);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				restoreMap=impl.queryRetcodeByIdOrName(retCodeId.getText(), "", ps);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setDirty(false);
			try {
				restoreMap=impl.queryRetcodeByIdOrName(retCodeId.getText(), "", ps);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		return retCodeId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return retCodeValue.getText();
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
		retCodeId.setText(map.get("ID"));
		retCodeValue.setText(map.get("NAME"));
		retCodeDesc.setText(map.get("retcodedesc"));
		/*
		 �ж�aoplevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
		����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
		*/
		if(map.get("retcodelevel").equals("0")){
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreBtn.setVisible(true);
			retCodeLevel.setText("APP");
		}
		else{
			retCodeLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		input.setName(retCodeId.getText());
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName(name);
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(!b)
		{
			clearBtn.setText("�༭");
			retCodeValue.setEnabled(false);
			retCodeDesc.setEnabled(false);
		}
		else{
			clearBtn.setText("����");
			retCodeValue.setEnabled(true);
			retCodeDesc.setEnabled(true);
		}
	}

	@Override
	public void mySave() {
		// TODO Auto-generated method stub
		saveData();
	}
}
