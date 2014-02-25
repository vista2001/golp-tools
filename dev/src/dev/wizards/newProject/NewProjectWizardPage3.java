/* 文件名：       NewProjectWizardPage3.java
 * 修改人：       rxy
 * 修改时间：   2013.12.14
 * 修改内容：   1.修改构造函数中，调用super方法时的参数为本类的类名；
 *         2.修改该向导页的内容，现要求用户输入AppHome和GolpHome两项。 
 */

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

public class NewProjectWizardPage3 extends WizardPage{
	private ISelection selection;
	private Text appHomeText;
	private Text golpHomeText;
	
	public Text getAppHomeText() {
		return appHomeText;
	}

	public Text getGolpHomeText() {
		return golpHomeText;
	}

	private List<Object> list = new ArrayList<Object>();

	public NewProjectWizardPage3(ISelection selection) {
		super("NewProjectWizardPage3");
		setTitle("新建工程");
		setDescription("这个向导将指导你完成GOLP工程的创建");
		this.selection = selection;
	}

	@Override
	/**创建向导页面的控件*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label appHomeLabel = new Label(container, SWT.NONE);
		appHomeLabel.setText("AppHome的路径：");
		
		appHomeText = new Text(container, SWT.BORDER);
		appHomeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label golpHomeLabel = new Label(container, SWT.NONE);
		golpHomeLabel.setText("GolpHome的路径：");
		
		golpHomeText = new Text(container, SWT.BORDER);
		golpHomeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		list.add(appHomeText);
		list.add(golpHomeText);
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
