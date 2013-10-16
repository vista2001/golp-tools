package dev.wizards.newProject;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewProjectWizardPage2 extends WizardPage{
	private ISelection selection;
	private Text prjCommIncludePathText;
	private Text prjCommLibPathText;
	private Text prjCommAopLibText;
	
	public Text getPrjCommIncludePathText() {
		return prjCommIncludePathText;
	}

	public Text getPrjCommLibPathText() {
		return prjCommLibPathText;
	}

	public Text getPrjCommAopLibText() {
		return prjCommAopLibText;
	}

	private List<Object> list = new ArrayList<Object>();

	public NewProjectWizardPage2(ISelection selection) {
		super("wizardPage");
		setTitle("新建工程");
		setDescription("这个向导将指导你完成GOLP工程的创建");
		this.selection = selection;
	}

	@Override
	/**创建向导页面的控件*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		Label prjCommIncludePathLabel = new Label(container, SWT.NONE);
		prjCommIncludePathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		prjCommIncludePathLabel.setText("公共头文件路径");
		
		prjCommIncludePathText = new Text(container, SWT.BORDER);
		prjCommIncludePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button prjCommIncludePathBtn = new Button(container, SWT.NONE);
		prjCommIncludePathBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd=new DirectoryDialog(e.display.getActiveShell());
				prjCommIncludePathText.setText(dd.open());
			}
		});
		prjCommIncludePathBtn.setText("...");
		
		Label prjCommLibPathLabel = new Label(container, SWT.NONE);
		prjCommLibPathLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		prjCommLibPathLabel.setText("公共库路径");
		
		prjCommLibPathText = new Text(container, SWT.BORDER);
		prjCommLibPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button prjCommLibPathBtn = new Button(container, SWT.NONE);
		prjCommLibPathBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd=new DirectoryDialog(e.display.getActiveShell());
				prjCommLibPathText.setText(dd.open());
			}
		});
		prjCommLibPathBtn.setText("...");
		
		Label prjCommAopLibLabel = new Label(container, SWT.NONE);
		prjCommAopLibLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		prjCommAopLibLabel.setText("公共原子交易库路径");
		
		prjCommAopLibText = new Text(container, SWT.BORDER);
		prjCommAopLibText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button prjCommAopLibBtn = new Button(container, SWT.NONE);
		prjCommAopLibBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd=new DirectoryDialog(e.display.getActiveShell());
				prjCommAopLibText.setText(dd.open());
			}
		});
		prjCommAopLibBtn.setText("...");
		list.add(prjCommAopLibText);
		list.add(prjCommIncludePathText);
		list.add(prjCommLibPathText);
	}
	//更新状态

	// 检测页面输入是否完成
	@Override
	public boolean isPageComplete() {
		return isFieldComplete(list);
	}

	// 检测本页输入信息是否为空
	private boolean isFieldComplete(List<Object> l) {
		for (Object object : l) {
			if (object instanceof Text) {
				if (((Text) object).getText() == null
						|| ((Text) object).getText().isEmpty())
					return false;
			}
			if (object instanceof Combo) {
				if (((Combo) object).getText() == null
						|| ((Combo) object).getText().isEmpty())
					return false;
			}
		}
		return true;
	}
}
