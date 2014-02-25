/* �ļ�����       NewProjectWizardPage3.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.14
 * �޸����ݣ�   1.�޸Ĺ��캯���У�����super����ʱ�Ĳ���Ϊ�����������
 *         2.�޸ĸ���ҳ�����ݣ���Ҫ���û�����AppHome��GolpHome��� 
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
		setTitle("�½�����");
		setDescription("����򵼽�ָ�������GOLP���̵Ĵ���");
		this.selection = selection;
	}

	@Override
	/**������ҳ��Ŀؼ�*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label appHomeLabel = new Label(container, SWT.NONE);
		appHomeLabel.setText("AppHome��·����");
		
		appHomeText = new Text(container, SWT.BORDER);
		appHomeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label golpHomeLabel = new Label(container, SWT.NONE);
		golpHomeLabel.setText("GolpHome��·����");
		
		golpHomeText = new Text(container, SWT.BORDER);
		golpHomeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		list.add(appHomeText);
		list.add(golpHomeText);
	}
	//����״̬

	// ���ҳ�������Ƿ����
	@Override
	public boolean isPageComplete() {
		return isFieldComplete(list);
	}

	// ��Ȿҳ������Ϣ�Ƿ�Ϊ��
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
