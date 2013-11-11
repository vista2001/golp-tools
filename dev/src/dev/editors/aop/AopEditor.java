package dev.editors.aop;

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

import dev.db.service.EditorAopServiceImpl;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
/**
 * Aop��༭����
 * <p>�����̳���EditorPart�࣬��AopEditorInput��һ�����Aop�༭���Ĺ���<br>
 * �ڱ༭����ʼ����ʱ��ͨ��AopEditorInput�ഫ���������Init�����жԱ༭��
 * ���г�ʼ����Ȼ����createPartControl��������ɶԱ༭���Ŀؼ��Ĺ��죬������
 * �ÿؼ�����Ҫ�ǡ���ѯ�������������޸ġ���ť������Ϊ���ı�����������ƣ���createPartControl
 * ������������dataInit������ɴ����ݿ��л�����ݲ������ı����У���ɶ�����
 * �༭���ĳ�ʼ����
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
public class AopEditor extends EditorPart implements ISearch {
	public static final String ID="dev.editor.Aop.AopEditor";//�༭����ı�ʶ
	//private Text upProject1;								 //��ѯ�������������ı���
	//private Text searchText;								 //�����ı���
	private Text upProject;									 //��������������ı���
	private Text AopLevel;                                   //ԭ�ӽ��׼����ı���
	private Text upDll;                                      //������̬���ı���
	private Text AopId;                                      //ԭ�ӽ��ױ�ʶ�ı���
	private Text AopName;                                    //ԭ�ӽ��������ı���
	private Text AopExts;                                    //��չ���б��ı���
	private Text AopDesc;                                    //ԭ�ӽ���˵���ı���
	private Button saveBtn;                                  //�޸İ�ť
	private Button clearBtn;                                 //������ť
	//private Button aopNameButton;                            //��ѡ��ť�����ƣ�
	//private Button aopIDButton;                              //��ѡ��ť����ʶ��
	private Button restoreButton;							 //�ָ���ť
	private PreferenceStore ps;                              //���ݿ�������Ϣ
	private EditorAopServiceImpl impl;                       //���ݿ���������
	private Map<String, String> map;                         //�洢��ѯ�����ݵ�Map
	private Map<String, String> restoreMap;					 //���ڻָ������ݵ�Map
	String[] ErrRecoverItem={"1-first","2-second","3-third"};//�����˵�ѡ���ַ�������
	private Combo AopErrRecover;                             //�����˵�
	private List<Text> list=new ArrayList<Text>();           //�ɱ༭�ؼ��б�
	private AopEditorInput input;                            //Input�����
	private Text inputData;                                  //�����������ı���
	private Text outputData;                                 //����������ı���
	private Text preCondition;                               //ǰ�������ı���
	private Text postCondition;                              //���������ı���
	private boolean bDirty=false;							 //�Ƿ��޸��ж�
	private Search search;
	public AopEditor() {
	}

	/**
	 * �����ڹرձ༭��ʱ����༭��������<br>
	 * ��isDirtyΪtrueʱ���رձ༭������ʾ�Ƿ񱣴��޸ģ����ѡ��"��"�����ִ��saveData������
	 * @return û�з���ֵ
	 */
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
		this.setPartName("ԭ�ӽ���"+input.getName()); 	//���ñ༭������
		this.input=(AopEditorInput)input;				//��Input��ʼ��		
	}
	/**
	 * /**
	 * ��Ҫ�����Ŀؼ�����һ��List��
	 * @param list
	 * @return û�з���ֵ
	 */
	private void addText(List<Text> list){
		list.add(AopName);
		list.add(AopDesc);
		list.add(preCondition);
		list.add(postCondition);
	}

	/**
	 * �ж��Ƿ�༭�����޸�<br>
	 * �������ֵ��true���رձ༭��ʱ����ʾ�Ƿ񱣴档
	 * @return ����༭�����޸ģ����ء�true�������򷵻ء�false��
	 */
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return bDirty;
	}
	/**
	 * ���ñ༭����״̬<br>
	 * ��isDirty(),���޸ġ���ť�޸ĳ�b��״̬��
	 * @param b Ҫ�޸ĳɵ�״̬
	 * @return û�з���ֵ
	 */
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
		Group searchAopGroup = new Group(composite, SWT.NONE);
		searchAopGroup.setText("ԭ�ӽ��ײ�ѯ");
		searchAopGroup.setLayout(new GridLayout(7, false));
		searchAopGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(searchAopGroup);
/*		Label upProjectLabel1 = new Label(searchGroup, SWT.NONE);
		upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel1.setText("��������");
		
		upProject1 = new Text(searchGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		aopNameButton = new Button(searchGroup, SWT.RADIO);
		aopNameButton.setText("ԭ�ӽ�������");
		
		aopIDButton = new Button(searchGroup, SWT.RADIO);
		aopIDButton.setSelection(true);
		aopIDButton.setText("ԭ�ӽ��ױ�ʶ");
		
		searchText = new Text(searchGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);
		
		Button searchBtn = new Button(searchGroup, SWT.NONE);
		GridData gd_searchBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_searchBtn.widthHint = 80;
		searchBtn.setLayoutData(gd_searchBtn);
		searchBtn.setText("��ѯ");
		//���ò�ѯ��ť����Ϊ
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				//�жϽڵ�ı༭���Ƿ��Ѿ����򿪣�����Ѿ��򿪣����ת�����㵽�Ѿ��򿪵ı༭��
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	AopEditor.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					AopEditor aopEditor = (AopEditor)tmp.getEditor(false);
					if(aopEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(aopIDButton.getSelection())
						{
							if(aopEditor.AopId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(aopEditor);
								return;
							}
						}
						else
						{
							if(aopEditor.AopName.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(aopEditor);
								return;
							}
						}
					}
				}

				try {
					//�ж��Ǹ��ݱ�ʶ��ѯ���Ǹ������Ʋ�ѯ
					if(aopIDButton.getSelection())
						//����ѯ�Ľ������һ��Map��
						map=impl.queryAopByIdOrName(searchText.getText(), "", ps);
					else
						map=impl.queryAopByIdOrName("", searchText.getText(), ps);
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				restoreMap=map;
				//����ѯ������ʱ������������༭����Ӧ���ı�����
				if(!map.isEmpty()){
					AopId.setText(map.get("aopid"));
					AopName.setText(map.get("aopname"));
					AopDesc.setText(map.get("aopdesc"));
					AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover"))-1]);
					AopExts.setText(map.get("aopexts"));
					
			//		 �ж�aoplevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
				//	����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
					
					if(map.get("aoplevel").equals("0")){
						AopLevel.setText("AOP");
						saveBtn.setVisible(true);
						clearBtn.setVisible(true);
						restoreButton.setVisible(true);
					}
					else{
						AopLevel.setText("GOLP");
						saveBtn.setVisible(false);
						clearBtn.setVisible(false);
						restoreButton.setVisible(false);
					}
					upDll.setText(map.get("updll"));
					preCondition.setText(map.get("precondition"));
					postCondition.setText(map.get("postcondition"));
					setPartName("ԭ�ӽ���"+AopId.getText()+"��������"+upProject.getText());//�޸ı༭������
					getSite().getShell().setText("GOLP tool "+"ԭ�ӽ���"+AopId.getText()+"��������"+upProject.getText());//�޸Ĺ��߱���
					input.setName(AopId.getText());//�ı�༭����ʶ
					//�ҵ�������༭����Ӧ�Ľڵ㣬Ȼ�����еĽڵ�����Ϊ����
					List<TreeNode> list=input.getSource().getParent().getChildren();
					for(TreeNode treenode:list){
						if(treenode.getName().equals(AopName.getText()))
							break;
					input.setNode((ResourceLeafNode)treenode);
					setSelectNode((ResourceLeafNode)treenode);
					setDirty(false);
					}
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
		//�����ı������������
		searchText.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=searchText.getText().length()<=32;
			}
		});*/
		
		Group AOPGroup = new Group(composite, SWT.NONE);
		AOPGroup.setLayout(new GridLayout(8, false));
		GridData gd_AOPGroup = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_AOPGroup.heightHint = 414;
		gd_AOPGroup.widthHint = 562;
		AOPGroup.setLayoutData(gd_AOPGroup);
		AOPGroup.setText("ԭ�ӽ��ױ�");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(AOPGroup, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(AOPGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("��������");
		
		upProject = new Text(AOPGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(AOPGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label AopLevelLabel = new Label(AOPGroup, SWT.NONE);
		AopLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopLevelLabel.setText("ԭ�ӽ��׼���");
		
		AopLevel = new Text(AOPGroup, SWT.BORDER);
		AopLevel.setEnabled(false);
		GridData gd_AopLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopLevel.widthHint = 70;
		AopLevel.setLayoutData(gd_AopLevel);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopErrRecoverLabel = new Label(AOPGroup, SWT.DROP_DOWN);
		AopErrRecoverLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopErrRecoverLabel.setText("����ָ�����");
		
		
		AopErrRecover = new Combo(AOPGroup, SWT.READ_ONLY);
		AopErrRecover.setEnabled(false);
		GridData gd_AopErrRecover = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopErrRecover.widthHint = 53;
		AopErrRecover.setLayoutData(gd_AopErrRecover);
		AopErrRecover.setItems(ErrRecoverItem);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label upDllLabel = new Label(AOPGroup, SWT.NONE);
		upDllLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upDllLabel.setText("������̬��");
		
		upDll = new Text(AOPGroup, SWT.BORDER);
		upDll.setEnabled(false);
		GridData gd_upDll = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upDll.widthHint = 70;
		upDll.setLayoutData(gd_upDll);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopIdLabel = new Label(AOPGroup, SWT.NONE);
		AopIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopIdLabel.setText("ԭ�ӽ��ױ�ʶ");
		
		AopId = new Text(AOPGroup, SWT.BORDER);
		AopId.setEnabled(false);
		GridData gd_AopId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopId.widthHint = 70;
		AopId.setLayoutData(gd_AopId);
		AopId.addVerifyListener(new VerifyListener() {
			//�����ı������������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopId.getText().length()<=32;
			}
		});
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopNameLabel = new Label(AOPGroup, SWT.NONE);
		AopNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopNameLabel.setText("ԭ�ӽ�������");
		
		AopName = new Text(AOPGroup, SWT.BORDER);
		AopName.setEnabled(false);
		GridData gd_AopName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopName.widthHint = 70;
		AopName.setLayoutData(gd_AopName);
		AopName.addVerifyListener(new VerifyListener() {
			//�����ı������������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopName.getText().length()<=32;
			}
		});
		//���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
		AopName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopExtsLabel = new Label(AOPGroup, SWT.NONE);
		AopExtsLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		AopExtsLabel.setText("��չ���б�");
		
		AopExts = new Text(AOPGroup, SWT.BORDER);
		AopExts.setEnabled(false);
		GridData gd_AopExts = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1);
		gd_AopExts.heightHint = 18;
		AopExts.setLayoutData(gd_AopExts);
		
		Button btnNewButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 80;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("����");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label inputDataLabel = new Label(AOPGroup, SWT.NONE);
		inputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		inputDataLabel.setText("����������");
		
		inputData = new Text(AOPGroup, SWT.BORDER);
		inputData.setEnabled(false);
		inputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Button prePositionButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_prePositionButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_prePositionButton.widthHint = 80;
		prePositionButton.setLayoutData(gd_prePositionButton);
		prePositionButton.setText("...");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label outputDataLabel = new Label(AOPGroup, SWT.NONE);
		outputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		outputDataLabel.setText("���������");
		
		outputData = new Text(AOPGroup, SWT.BORDER);
		outputData.setEnabled(false);
		outputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Button postPositionButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_postPositionButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_postPositionButton.widthHint = 80;
		postPositionButton.setLayoutData(gd_postPositionButton);
		postPositionButton.setText("...");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label prePositionLabel = new Label(AOPGroup, SWT.NONE);
		prePositionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		prePositionLabel.setText("ǰ������");
		
		preCondition = new Text(AOPGroup, SWT.BORDER);
		preCondition.setEnabled(false);
		preCondition.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 2));
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		preCondition.addVerifyListener(new VerifyListener() {
			//�����ı�����������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopDesc.getText().length()<=128;
			}
		});
		//���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
		preCondition.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
					saveBtn.setEnabled(true);
				}
			}
		});
		
		Label postPositionLabel = new Label(AOPGroup, SWT.NONE);
		postPositionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		postPositionLabel.setText("��������");
		
		postCondition = new Text(AOPGroup, SWT.BORDER);
		postCondition.setEnabled(false);
		postCondition.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 2));
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		postCondition.addVerifyListener(new VerifyListener() {
			//�����ı�����������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopDesc.getText().length()<=128;
			}
		});
		//���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
		postCondition.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label AopDescLabel = new Label(AOPGroup, SWT.NONE);
		AopDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		AopDescLabel.setText("ԭ�ӽ���˵��");
		
		AopDesc = new Text(AOPGroup, SWT.BORDER);
		AopDesc.setEnabled(false);
		AopDesc.addVerifyListener(new VerifyListener() {
			//�����ı�����������
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopDesc.getText().length()<=128;
			}
		});
		//���ı����޸�ʱ�����ñ༭����״̬Ϊ���޸�
		AopDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		GridData gd_AopDesc = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3);
		gd_AopDesc.heightHint = 24;
		AopDesc.setLayoutData(gd_AopDesc);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		clearBtn = new Button(AOPGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("�༭");
		/*
		 * Ϊ���༭����ť������Ϊ�������°�ťʱ������ťΪ���༭�������Ϊ��������������
		 * �༭�Ŀؼ�����Ϊ���ã������Ϊ���༭���������Ա༭�Ŀؼ�����Ϊ�����ã�����
		 * �İ�ť����Ϊ�����á�
		 */
		clearBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="�༭"){
					nButton.setText("����");
					AopErrRecover.setEnabled(true);
					unlock(list);
				}
				else{
					nButton.setText("�༭");
					AopErrRecover.setEnabled(false);
					lock(list);
				}
				
			}
		});
		new Label(AOPGroup, SWT.NONE);
		
		saveBtn = new Button(AOPGroup, SWT.NONE);
		saveBtn.setEnabled(false);
		GridData gd_saveBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_saveBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_saveBtn);
		saveBtn.setText("�޸�");
		/*
		 * Ϊ�޸İ�ť������Ϊ�������°�ťʱ��ִ��saveData����
		 * 
		 * */
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)
			{
				try {
					saveData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		restoreButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_restoreButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreButton.widthHint = 80;
		restoreButton.setLayoutData(gd_restoreButton);
		restoreButton.setText("�ָ�");
		//���ûָ���ť��Ϊ
		restoreButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)
			{
				AopName.setText(restoreMap.get("name"));
				AopDesc.setText(restoreMap.get("aopdesc"));
				AopExts.setText(restoreMap.get("aopexts"));
				preCondition.setText(restoreMap.get("precondition"));
				postCondition.setText(restoreMap.get("postcondition"));
				setDirty(false);
			}
		});
		new Label(AOPGroup, SWT.NONE);
		// TODO Auto-generated method stub
		addText(list);
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
		getSite().getShell().setText("GOLP tool "+"ԭ�ӽ���"+input.getName()+"��������"+upProject.getText());//���ù��ߵı���
		setSelectNode(input.getSource());
		setPartName("ԭ�ӽ���"+input.getName()+"��������"+upProject.getText());
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
		ResourceLeafNode rln = ((AopEditorInput) input).getSource();
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
		impl=new EditorAopServiceImpl();
		//�����ݿ���в�ѯ������Map
		map=impl.queryAopByIdOrName(name, "", ps);
		restoreMap=map;
		//���θ��ؼ���ֵ
		AopId.setText(map.get("id"));
		AopName.setText(map.get("name"));
		AopDesc.setText(map.get("aopdesc"));
		AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover"))-1]);
		AopExts.setText(map.get("aopexts"));
		//��aoplevelΪ��������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
		if(map.get("aoplevel").equals("0"))
			AopLevel.setText("AOP");
		else{
			AopLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreButton.setVisible(false);
		}
		upDll.setText(map.get("updll"));
		preCondition.setText(map.get("precondition"));
		postCondition.setText(map.get("postcondition"));
		upProject.setText(input.getSource().getRootProject().getId());
		setDirty(false);
		saveBtn.setEnabled(isDirty());
	}
	/**
	 * ���ؼ�����Ϊ������<br>
	 * ��for eachѭ����List�еĿռ�������Ϊ�����á�
	 * @param list  Ҫ���õĿռ���б�
	 * @return û�з���ֵ
	 * */
	 private void lock(List<Text> list){
		 for(Text i : list)
		 {
			 i.setEnabled(false);
		 }
	 }
	 /**
		 * ���ؼ�����Ϊ����<br>
		 * ��for eachѭ����List�еĿռ�������Ϊ���á�
		 * @param list  Ҫ���õĿռ���б�
		 * @return û�з���ֵ
		 * */
	 private void unlock(List<Text> list)
	 {
		 for(Text i : list)
			 i.setEnabled(true);
	 }

	/**
	 * �������ݵķ���
	 * ���ڱ��水ť��doSave�����ı�������������޸İ�ť����ԭ�ӽ��������ı���Ϊ�գ��򵯳�����Ի���
	 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorAopServiceImpl���updateAopById������
	 * List��������滻��ʶΪAopId�����ݡ�
	 * @throws SQLException
	 */
	private void saveData() throws SQLException{
		if(AopName.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("����");
			box.setMessage("���������Ʋ���Ϊ��");
			box.open();
		}
		else{
			List<String> datalist=new ArrayList<String>();
			datalist.add(AopName.getText());
			datalist.add(AopDesc.getText());
			datalist.add(AopErrRecover.getText().substring(0, 1));
			datalist.add(preCondition.getText());
			datalist.add(postCondition.getText());
			try {
				impl.updateAopById(AopId.getText(), datalist, ps);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setDirty(false);
			}
		restoreMap=impl.queryAopByIdOrName(AopId.getText(), "", ps);
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
	public String getTargetId() {
		// TODO Auto-generated method stub
		return AopId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return AopName.getText();
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
		AopId.setText(map.get("id"));
		AopName.setText(map.get("name"));
		AopDesc.setText(map.get("aopdesc"));
		AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover"))-1]);
		AopExts.setText(map.get("aopexts"));
		/*
		 �ж�aoplevel�Ƿ�Ϊ0��Ϊ��������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
		����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ� 
		*/
		if(map.get("aoplevel").equals("0")){
			AopLevel.setText("AOP");
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreButton.setVisible(true);
		}
		else{
			AopLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreButton.setVisible(false);
		}
		upDll.setText(map.get("updll"));
		preCondition.setText(map.get("precondition"));
		postCondition.setText(map.get("postcondition"));
		input.setName(AopId.getText());
		setDirty(false);
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName("ԭ�ӽ���"+AopId.getText()+"��������"+upProject.getText());
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(!b){
			clearBtn.setText("����");
			AopErrRecover.setEnabled(true);
			unlock(list);
		}
		else{
			clearBtn.setText("�༭");
			AopErrRecover.setEnabled(false);
			lock(list);
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
