package dev.editors.aopDll;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
import dev.db.service.EditorAopDllServiceImpl;
/**
 * AopDll��༭����
 * <p>�����̳���EditorPart�࣬��AopDllEditorInput��һ�����AopDll�༭���Ĺ���<br>
 * �ڱ༭����ʼ����ʱ��ͨ��AopDllEditorInput�ഫ���������Init�����жԱ༭��
 * ���г�ʼ����Ȼ����createPartControl��������ɶԱ༭���Ŀؼ��Ĺ��죬������
 * �ÿؼ�����Ҫ�ǡ���ѯ�������������޸ġ���ť������Ϊ���ı�����������ƣ���createPartControl
 * ������������dataInit������ɴ����ݿ��л�����ݲ������ı����У���ɶ�����
 * �༭���ĳ�ʼ����
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class AopDllEditor extends EditorPart implements ISearch{
	public AopDllEditor() {
	}

	public static final String ID="dev.editor.AopDll.AopDllEditor"; //�༭����ı�ʶ
	//private Text upProject1;										//��ѯ�������������ı���
	//private Text searchText;										//�����ı���
	private Text upProject;											//��������������ı���
	private Text dllLevel;											//ԭ�ӽ��׿⼶���ı���
	private Text dllId;												//ԭ�ӽ��׿��ʶ�ı���	
	private Text dllType;											//ԭ�ӽ��׿������ı���	
	private Text dllName;											//ԭ�ӽ��׿������ı���
	private Text dllDesc;											//ԭ�ӽ��׿�˵���ı���
	private Button saveBtn;											//�޸İ�ť
	private Button clearBtn;										//������ť
	private Button restoreBtn;										//�ָ���ť
	//private Button dllIDButton;										//��ѡ��ť�����ݱ�ʶ��
	//private Button dllNameButton;									//��ѡ��ť���������ƣ�
	private AopDllEditorInput input;								//Input����
	private EditorAopDllServiceImpl impl;							//���ݿ���������
	private PreferenceStore ps;										//���ݿ�������Ϣ
	private Map<String, String> map;								//�洢��ѯ�����ݵ�Map
	private Map<String, String> restoreMap;							//�ָ���ť�����ݵ�Map
	private boolean bDirty=false;									//�Ƿ��޸ĵı�ʶ
	private Search search;
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		try {
			saveDate();
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
		this.setSite(site);							//����Site
		this.setInput(input);						//����Input
		this.setPartName("��̬��"+input.getName());	//���ñ༭������
		this.input=(AopDllEditorInput)input;		//��Input��ʼ��
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
		Group searchDllGroup = new Group(composite, SWT.NONE);
		searchDllGroup.setText("��̬���ѯ");
		searchDllGroup.setLayout(new GridLayout(7, false));
		searchDllGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(searchDllGroup);

/*		
		Label upProjectLabel1 = new Label(searchDllGroup, SWT.NONE);
		upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel1.setText("��������");
		
		upProject1 = new Text(searchDllGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		dllNameButton = new Button(searchDllGroup, SWT.RADIO);
		dllNameButton.setText("��̬������");
		
		dllIDButton = new Button(searchDllGroup, SWT.RADIO);
		dllIDButton.setSelection(true);
		dllIDButton.setText("��̬���ʶ");
		
		searchText = new Text(searchDllGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);
		
		Button searchBtn = new Button(searchDllGroup, SWT.NONE);
		GridData gd_searchBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_searchBtn.widthHint = 80;
		searchBtn.setLayoutData(gd_searchBtn);
		searchBtn.setText("��ѯ");
		//���ò�ѯ��ť����Ϊ
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	AopDllEditor.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					AopDllEditor dllEditor = (AopDllEditor)tmp.getEditor(false);
					if(dllEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(dllIDButton.getSelection())
						{
							if(dllEditor.dllId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dllEditor);
								return;
							}
						}
						else
						{
							if(dllEditor.dllName.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dllEditor);
								return;
							}
						}
					}
				}
					//�ж��Ǹ��ݱ�ʶ��ѯ���Ǹ������Ʋ�ѯ
					try {
						//����ѯ�Ľ������һ��Map��
						if(dllNameButton.getSelection())
							map=impl.queryAopDllByIdOrName("", searchText.getText(), ps);
						else
							map=impl.queryAopDllByIdOrName(searchText.getText(),"", ps);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					//����ѯ������ʱ������������༭����Ӧ���ı�����
					if(!map.isEmpty()){
						dllId.setText(map.get("dllid"));
						dllDesc.setText(map.get("dlldesc"));
						dllName.setText(map.get("dllname"));
						dllType.setText(map.get("dlltype"));
						
					//	 �ж�aoplevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
					//	����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
						
						if(map.get("dlllevel").equals("0")){
							dllLevel.setText("APP");
							saveBtn.setVisible(true);
							clearBtn.setVisible(true);
							restoreBtn.setVisible(true);
						}
						else{
							dllLevel.setText("GOLP");
							saveBtn.setVisible(false);
							clearBtn.setVisible(false);
							restoreBtn.setVisible(false);
						}
						setPartName("��̬��"+searchText.getText()+"��������"+upProject.getText());				  //�޸ı༭������
						getSite().getShell().setText("GOLP tool "+"��̬��"+dllId+"��������"+upProject.getText());//�޸Ĺ��߱���
						input.setName(dllId.getText());							  //�ı�༭����ʶ
						setDirty(false);
						restoreMap=map;
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
		Group dllGroup = new Group(composite, SWT.NONE);
		GridData gd_dllGroup = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_dllGroup.heightHint = 377;
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
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(dllGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("��������");
		
		upProject = new Text(dllGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(dllGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label dllLevelLabel = new Label(dllGroup, SWT.NONE);
		dllLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllLevelLabel.setText("�������");
		
		dllLevel = new Text(dllGroup, SWT.BORDER);
		dllLevel.setEnabled(false);
		GridData gd_dllLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllLevel.widthHint = 70;
		dllLevel.setLayoutData(gd_dllLevel);
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
		dllIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllIdLabel.setText("��̬���ʶ");
		
		dllId = new Text(dllGroup, SWT.BORDER);
		dllId.setEnabled(false);
		GridData gd_dllId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllId.widthHint = 70;
		dllId.setLayoutData(gd_dllId);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		Label dllTypeLabel = new Label(dllGroup, SWT.NONE);
		dllTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllTypeLabel.setText("��̬������");
		
		dllType = new Text(dllGroup, SWT.BORDER);
		dllType.setEnabled(false);
		GridData gd_dllType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllType.widthHint = 70;
		dllType.setLayoutData(gd_dllType);
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
		dllNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllNameLabel.setText("��̬������");
		
		dllName = new Text(dllGroup, SWT.BORDER);
		dllName.setEnabled(false);
		GridData gd_dllName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllName.widthHint = 70;
		dllName.setLayoutData(gd_dllName);
		dllName.addVerifyListener(new VerifyListener() {
			//�����ı������������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=dllName.getText().length()<=32;
			}
		});
		dllName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
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
		
		Label dllDescLabel = new Label(dllGroup, SWT.NONE);
		dllDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		dllDescLabel.setText("��̬��˵��");
		
		dllDesc = new Text(dllGroup, SWT.BORDER | SWT.V_SCROLL);
		dllDesc.setEnabled(false);
		GridData gd_dllDesc = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3);
		gd_dllDesc.widthHint = 81;
		gd_dllDesc.heightHint = 29;
		dllDesc.setLayoutData(gd_dllDesc);
		dllDesc.addVerifyListener(new VerifyListener() {
			//�����ı������������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=dllName.getText().length()<=128;
			}
		});
		dllDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
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
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		clearBtn = new Button(dllGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("�༭");
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
					dllName.setEnabled(true);
					dllDesc.setEnabled(true);
				}
				else{
					nButton.setText("�༭");
					dllName.setEnabled(false);
					dllDesc.setEnabled(false);
				}
				
			}
		});
		new Label(dllGroup, SWT.NONE);
		
		saveBtn = new Button(dllGroup, SWT.NONE);
		GridData gd_saveBtn = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_saveBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_saveBtn);
		saveBtn.setText("�޸�");
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				try {
					saveDate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		/*
		 * Ϊ���޸ġ���ť������Ϊ�������޸İ�ť������̬�������ı���Ϊ�գ��򵯳�����Ի���
		 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorAopDllServiceImpl���
		 * updateAopDllById������List��������滻��ʶΪAopId
		 * �����ݡ�
		 * */
		
		restoreBtn = new Button(dllGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreBtn.setLayoutData(gd_restoreBtn);
		restoreBtn.setText("�ָ�");
		//���ûָ���ť��Ϊ
		restoreBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				dllName.setText(restoreMap.get("name"));
				dllDesc.setText(restoreMap.get("dlldesc"));
				setDirty(false);
			}
		});
		new Label(dllGroup, SWT.NONE);
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
		getSite().getShell().setText("GOLP tool "+"��̬��"+input.getName()+"��������"+upProject.getText());//���ù��ߵ�����
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
		ResourceLeafNode rln = ((AopDllEditorInput) input).getSource();
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
		impl=new EditorAopDllServiceImpl();
		map=impl.queryAopDllByIdOrName(name, "", ps);
		restoreMap=map;
		//���θ��ؼ���ֵ
		dllId.setText(map.get("id"));
		dllDesc.setText(map.get("dlldesc"));
		dllName.setText(map.get("name"));
		dllType.setText(map.get("dlltype"));
		//��aoplevelΪ��������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
		if(map.get("dlllevel").equals("0"))
			dllLevel.setText("APP");
		else{
			dllLevel.setText("GOLP");
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
	 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorDllServiceImpl���updateDllById������
	 * List��������滻��ʶΪDllId�����ݡ�
	 * @throws SQLException
	 */
	private void saveDate() throws SQLException
	{
		if(dllName.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("����");
			box.setMessage("���������Ʋ���Ϊ��");
			box.open();
		}
		else{
			List<String> datalist=new ArrayList<String>();
			datalist.add(dllName.getText());
			datalist.add(dllDesc.getText());
			try {
				impl.updateAopDllById(dllId.getText(), datalist, ps);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		restoreMap=impl.queryAopDllByIdOrName(dllId.getText(), "", ps);
		setDirty(false);
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
		return dllId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return dllName.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		// TODO Auto-generated method stub
		this.map=map;
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
		dllId.setText(map.get("id"));
		dllDesc.setText(map.get("dlldesc"));
		dllName.setText(map.get("name"));
		dllType.setText(map.get("dlltype"));
		/*
		 �ж�dlllevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
		����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
		*/
		if(map.get("dlllevel").equals("0")){
			dllLevel.setText("APP");
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreBtn.setVisible(true);
		}
		else{
			dllLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		input.setName(dllId.getText());
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName("��̬��"+dllId.getText()+"��������"+upProject.getText());
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(b){
			clearBtn.setText("����");
			dllName.setEnabled(true);
			dllDesc.setEnabled(true);
		}
		else{
			clearBtn.setText("�༭");
			dllName.setEnabled(false);
			dllDesc.setEnabled(false);
		}
		setDirty(false);
	}

	@Override
	public void mySave() {
		// TODO Auto-generated method stub
		try {
			saveDate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
