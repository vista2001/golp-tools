package dev.wizards.newProject;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewProjectWizardPage1 extends WizardPage {
	private ISelection selection;
	private Text dbAddressText;
	private Text dbPortText;
	private Text dbInstanceText;
	private Text dbUserText; 
	private Text dbPwdText;
	private List<Object> list=new ArrayList<Object>();
	public ISelection getSelection() {
		return selection;
	}

	public Text getDbAddressText() {
		return dbAddressText;
	}

	public Text getDbPortText() {
		return dbPortText;
	}

	public Text getDbInstanceText() {
		return dbInstanceText;
	}

	public Text getDbUserText() {
		return dbUserText;
	}

	public Text getDbPwdText() {
		return dbPwdText;
	}

	public NewProjectWizardPage1(ISelection selection) {
		super("wizardPage");
		setTitle("新建工程");
		setDescription("请输入新建工程的数据库连接信息");
		this.selection = selection;
	}

	@Override
	/**创建向导页面的控件*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label dbTypeLabel = new Label(container, SWT.NONE);
		dbTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dbTypeLabel.setText("数据库类型");
		
		Combo dbTypeCombo = new Combo(container, SWT.NONE);
		dbTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbTypeCombo.setItems(new String[]{"oracle"});
		
		Label dbAddressLabel = new Label(container, SWT.NONE);
		dbAddressLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dbAddressLabel.setText("数据库地址");
		
		dbAddressText = new Text(container, SWT.BORDER);
		dbAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label dbPortLabel = new Label(container, SWT.NONE);
		dbPortLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dbPortLabel.setText("数据库端口");
		
		dbPortText = new Text(container, SWT.BORDER);
		dbPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label dbInstanceLabel = new Label(container, SWT.NONE);
		dbInstanceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dbInstanceLabel.setText("数据库实例");
		
		dbInstanceText = new Text(container, SWT.BORDER);
		dbInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label dbUserLabel = new Label(container, SWT.NONE);
		dbUserLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dbUserLabel.setText("数据库用户");
		
		dbUserText = new Text(container, SWT.BORDER);
		dbUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label dbPwdLabel = new Label(container, SWT.NONE);
		dbPwdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dbPwdLabel.setText("数据库口令");
		
		dbPwdText = new Text(container, SWT.BORDER|SWT.PASSWORD);
		dbPwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		list.add(dbTypeCombo);
		list.add(dbAddressText);
		list.add(dbInstanceText);
		list.add(dbPortText);
		list.add(dbUserText);
		list.add(dbPwdText);
	}

	// 更新状态
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}

	@Override
	public boolean isPageComplete() {
		return isFieldComplete(list);
	}

	private boolean isFieldComplete(List<Object> l) {
		for (Object object : l) {
			if (object instanceof Text) {
				if (((Text) object).getText() == null
						|| ((Text) object).getText().length() == 0)
					return false;
			}
		}
		return true;
	}
}
