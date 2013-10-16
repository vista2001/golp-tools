package dev.wizards.newAop;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;

public class NewAopWizardPage1 extends WizardPage {

	private ISelection selection;
	
	private Text inputData;
	private Text outPutData;
	private Text aopPreConditionText;
	private Text aopPostConditionText;
	private Combo aopErrRecoverCombo;
	private Combo upDllCombo;
	private Combo upProjectCombo;
	private Combo aopLvlCombo;
	
	public ISelection getSelection() {
		return selection;
	}
	
	public Text getInputData() {
		return inputData;
	}

	public Text getOutPutData() {
		return outPutData;
	}

	public Text getAopPreConditionText() {
		return aopPreConditionText;
	}

	public Text getAopPostConditionText() {
		return aopPostConditionText;
	}

	public Combo getAopErrRecoverCombo() {
		return aopErrRecoverCombo;
	}

	public Combo getUpDllCombo() {
		return upDllCombo;
	}

	public Combo getUpProjectCombo() {
		return upProjectCombo;
	}

	public Combo getAopLvlCombo() {
		return aopLvlCombo;
	}

	public NewAopWizardPage1(ISelection selection) {
		super("NewAopWizardPage1");
		setTitle("新建原子交易向导");
		setDescription("这个向导将指导你完成GOLP原子交易的创建");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label upProjectLabel = new Label(container, SWT.NONE);
		upProjectLabel.setText("*所属工程：");
		upProjectCombo = new Combo(container, SWT.READ_ONLY);
		upProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		upProjectCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		new Label(container, SWT.NONE);
		
		Label upDllComboLabel = new Label(container, SWT.NONE);
		upDllComboLabel.setText("*所属动态库：");
		upDllCombo = new Combo(container, SWT.READ_ONLY);
		upDllCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		upDllCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		new Label(container, SWT.NONE);

		Label aopErrRecoverLabel = new Label(container, SWT.NONE);
		aopErrRecoverLabel.setText("*AOP恢复机制:");
		aopErrRecoverCombo = new Combo(container, SWT.READ_ONLY);
		aopErrRecoverCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aopErrRecoverCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		aopErrRecoverCombo.setItems(new String[]{"第一种机制","第二种机制"});
		
		new Label(container, SWT.NONE);
		
		Label aopLvlLabel = new Label(container, SWT.NONE);
		aopLvlLabel.setText("*AOP级别：");
		aopLvlCombo = new Combo(container, SWT.READ_ONLY);
		aopLvlCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aopLvlCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		aopLvlCombo.setItems(new String[]{"","GOLP","App"});
		/*aopLvlCombo.add("");
		aopLvlCombo.add("GOLP");
		aopLvlCombo.add("App");*/
		new Label(container, SWT.NONE);
		
		Label inputDataLabel = new Label(container, SWT.NONE);
		inputDataLabel.setText("*输入数据项");
		inputData = new Text(container, SWT.BORDER|SWT.READ_ONLY);
		inputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		inputData.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		Button inputDataBtn = new Button(container, SWT.NONE);
		inputDataBtn.setText("...");
		inputDataBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryDataItemDialog queryAop=new QueryDataItemDialog(e.display.getActiveShell(),e.getSource());
				queryAop.addInformDialogListener(new InformDialogListener() {
					
					@Override
					public void handleEvent(InformDialogEvent dm) {
						java.util.List<String> l=((QueryDataItemDialog)dm.getdm()).listForReturn;
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						inputData.setText(s);
					}
				});
				queryAop.open();
			}
		});
		
		Label outputDataLabel = new Label(container, SWT.NONE);
		outputDataLabel.setText("*输出数据项");
		outPutData = new Text(container, SWT.BORDER|SWT.READ_ONLY);
		outPutData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		outPutData.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		Button outputDataBtn = new Button(container, SWT.NONE);
		outputDataBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryDataItemDialog queryAop=new QueryDataItemDialog(e.display.getActiveShell(),e.getSource());
				queryAop.addInformDialogListener(new InformDialogListener() {
					
					@Override
					public void handleEvent(InformDialogEvent dm) {
						java.util.List<String> l=((QueryDataItemDialog)dm.getdm()).listForReturn;
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						outPutData.setText(s);
					}
				});
				queryAop.open();
			}
		});
		outputDataBtn.setText("...");
		
		Label aopPreConditionLabel = new Label(container, SWT.NONE);
		aopPreConditionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		aopPreConditionLabel.setText("前置条件");
		aopPreConditionText = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = 58;
		aopPreConditionText.setLayoutData(gd_text);
		new Label(container, SWT.NONE);
		
		Label aopPostConditionLabel = new Label(container, SWT.NONE);
		aopPostConditionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		aopPostConditionLabel.setText("后置条件");
		aopPostConditionText = new Text(container, SWT.BORDER | SWT.MULTI);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_1.heightHint = 68;
		aopPostConditionText.setLayoutData(gd_text_1);
		new Label(container, SWT.NONE);
		
		//控件创建后初始化数据
		initData();
	}

	public boolean validInput() {
		if (getAopLvlCombo().getText().length() == 0
				|| getAopErrRecoverCombo().getText().length() == 0
				|| getUpProjectCombo().getText().length() == 0
				|| getInputData().getText().length() == 0
				|| getOutPutData().getText().length() == 0
				|| getUpDllCombo().getText().length() == 0)
			return false;
		return true;
	}

	/*@Override
	public boolean canFlipToNextPage() {
		//return validInput();
		return false;
	}*/

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
	private void dialogChanged() {
		setPageComplete(true);
	}

	private void initData() {
		//初始化所属工程内容
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject[] projects = prjRoot.getProjects();
		for (IProject iProject : projects) {
			upProjectCombo.add(iProject.getName());
		}
		//初始化所属动态库内容
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn();
		ResultSet rs = null;
		try {
			rs = dbConnectImpl.retrive("select * from dll order by dllId");
			while (rs.next()) {
				upDllCombo.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnectImpl.closeConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
