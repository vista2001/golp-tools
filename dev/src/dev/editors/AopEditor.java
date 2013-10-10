package dev.editors;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;

public class AopEditor extends EditorPart {

	public static final String ID="dev.editors.aopEditor";
	private boolean dirty=false;
	private Text AopIdText;
	private Text AopNameText;
	private Text textAOPId;
	private Text textAOPName;
	private Text textAOPDesc;
	private Text textPostCondition;
	private Text textAOPSrcDir;
	private Text textPrecondition;
	private Text textInputDatas;
	private Text textOutputDatas;
	private Text textAOPExts;
	private Text textAOPRetCodes;
	private Text textAOPErrRecover;
	private Text text_5;
	private IEditorInput input;
	private List a;
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public AopEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());
		this.input=input;
		System.out.println("input name="+input.getName());
		new DbConnFactory();
		DbConnectImpl dbConnImpl=DbConnFactory.dbConnCreator();
		dbConnImpl.openConn();
		try {
			ResultSet rs=dbConnImpl.retrive("select * from server where serverid='"+input.getName()+"'");
			System.out.println(rs);
			a=new ArrayList<>();
			for(int i=1;rs.next();i++){
				a.add(rs.getString(i));
				//System.out.println("hello:"+a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.horizontalSpacing = 0;
		gl_parent.verticalSpacing = 0;
		parent.setLayout(gl_parent);
		
		Group group = new Group(parent, SWT.NONE);
		group.setText("原子交易查询");
		GridData gd=new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalSpan=3;
		group.setLayoutData(gd);
		
		GridLayout gl_group = new GridLayout(5, false);
		gl_group.horizontalSpacing = 0;
		group.setLayout(gl_group);
		Label AopIdLabel = new Label(group, SWT.NONE);
		AopIdLabel.setText("原子交易标识");
		AopIdText = new Text(group, SWT.BORDER);
		Label AopNameLabel = new Label(group, SWT.NONE);
		AopNameLabel.setText("原子交易名称");
		AopNameText = new Text(group, SWT.BORDER);
		
		Button quaryAopBtn = new Button(group, SWT.NONE);
		quaryAopBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		quaryAopBtn.setText("查询");
		
		Group group_1 = new Group(parent, SWT.NONE);
		group_1.setLayout(new GridLayout(6, true));
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		group_1.setText("原子交易");
		
		Label labelAopId = new Label(group_1, SWT.NONE);
		labelAopId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelAopId.setText("原子交易标识");
		
		textAOPId = new Text(group_1, SWT.BORDER);
		textAOPId.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label labelAopName = new Label(group_1, SWT.NONE);
		labelAopName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelAopName.setText("原子交易名称");
		
		textAOPName = new Text(group_1, SWT.BORDER);
		textAOPName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		//textAOPName.setText((String)a.get(0));
		textAOPName.setSize(70, 18);
		
		Label labelAopDesc = new Label(group_1, SWT.NONE);
		labelAopDesc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelAopDesc.setText("原子交易描述");
		
		textAOPDesc = new Text(group_1, SWT.BORDER);
		textAOPDesc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label labelAOPSrcDir = new Label(group_1, SWT.NONE);
		labelAOPSrcDir.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelAOPSrcDir.setText("所属动态库");
		
		textAOPSrcDir = new Text(group_1, SWT.BORDER);
		textAOPSrcDir.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label labelPrecondition = new Label(group_1, SWT.NONE);
		labelPrecondition.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelPrecondition.setText("源程序目录");
		
		textPrecondition = new Text(group_1, SWT.BORDER);
		textPrecondition.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label labelPostCondition = new Label(group_1, SWT.NONE);
		labelPostCondition.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		labelPostCondition.setText("前置条件");
		
		textPostCondition = new Text(group_1, SWT.BORDER);
		textPostCondition.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(group_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("后置条件");
		
		textInputDatas = new Text(group_1, SWT.BORDER);
		textInputDatas.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(group_1, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("输入数据项");
		
		textOutputDatas = new Text(group_1, SWT.BORDER);
		textOutputDatas.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(group_1, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("输出数据项");
		
		textAOPExts = new Text(group_1, SWT.BORDER);
		textAOPExts.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = new Label(group_1, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("AOP扩展点");
		
		textAOPRetCodes = new Text(group_1, SWT.BORDER);
		textAOPRetCodes.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_4 = new Label(group_1, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("AOP响应码");
		
		textAOPErrRecover = new Text(group_1, SWT.BORDER);
		textAOPErrRecover.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_5 = new Label(group_1, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("交易恢复机制");
		
		text_5 = new Text(group_1, SWT.BORDER);
		text_5.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		
		Button saveBtn = new Button(group_1, SWT.NONE);
		saveBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		saveBtn.setText("修改");
		saveBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				DbConnectImpl dbConnImpl=DbConnFactory.dbConnCreator();
				dbConnImpl.openConn();
				try {
					dbConnImpl.setPrepareSql("update  aop set (id=?,name=?,desc=?,null,null,null,null,null,null,null,null)");
					dbConnImpl.setPreparedString(1, textAOPId.getText().toString());
					dbConnImpl.setPreparedString(2, textAOPName.getText().toString());
					dbConnImpl.setPreparedString(3, textAOPDesc.getText().toString());
					dbConnImpl.executeExceptPreparedQuery();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
					try {
						dbConnImpl.closeConn();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				System.out.println("hello");
				System.out.println(textAOPId.getText().toString());
				System.out.println(textAOPName.getText().toString());
			}
			
		});
		datainit(input.getName());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	private void datainit(String name){
		DbConnectImpl dbConnImpl=DbConnFactory.dbConnCreator();
		dbConnImpl.openConn();
		try {
			System.out.println(name);
			ResultSet rs=dbConnImpl.retrive("select * from aop where id="+"'"+name+"'");
			if(rs.next()){
				this.textAOPId.setText(rs.getString(1));
				this.textAOPName.setText(rs.getString(2));
				this.textAOPDesc.setText(rs.getString(3));
				this.textAOPSrcDir.setText(rs.getString(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
