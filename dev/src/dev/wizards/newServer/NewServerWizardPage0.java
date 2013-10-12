package dev.wizards.newServer;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class NewServerWizardPage0 extends WizardPage{
	private Text svrIdText;
	private Text svrNameText;
	private Text svrDescText;
	private ISelection selection;

	public NewServerWizardPage0(ISelection selection) {
		super("NewServerWizardPage0");
		setTitle("新建服务程序向导");
		setDescription("这个向导将指导你完成GOLP服务程序的创建");
		this.selection = selection;
	}

	public Text getSvrIdText() {
		return svrIdText;
	}

	public Text getSvrNameText() {
		return svrNameText;
	}

	public Text getSvrDescText() {
		return svrDescText;
	}

	@Override
	/**创建向导页面的控件*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
//		new Label(container, SWT.NONE);
//		new Label(container, SWT.NONE);
		
		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjIdLabel.setText("*服务程序标识符：");
		
		svrIdText = new Text(container, SWT.BORDER);
		svrIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjNameLabel.setText("*服务程序名称：");
		
		svrNameText = new Text(container, SWT.BORDER);
		svrNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjDescLabel.setText("*描述：");
		
		svrDescText = new Text(container, SWT.BORDER);
		svrDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		svrIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		svrNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		svrDescText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
	}

	// 输入项校验
	private void dialogChanged() {
		if (getSvrDescText().getText().length() == 0||getSvrIdText().getText().length()==0||getSvrNameText().getText().length()==0) {
			updateStatus("File container must be specified");
			return;
		}
		updateStatus(null);
	}

	// 更新状态
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	public boolean canFlipToNextPage() {
		if(getSvrDescText().getText().length()==0||getSvrIdText().getText().length()==0||getSvrNameText().getText().length()==0)
			return false;
		return true;
	}

}
