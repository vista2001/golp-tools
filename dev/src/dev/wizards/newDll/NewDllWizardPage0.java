package dev.wizards.newDll;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.util.RegExpCheck;
import dev.views.NavView;
/**
 * ���ඨ�����½���������򵼵ĵ�1ҳ
 */
public class NewDllWizardPage0 extends WizardPage
{
	private Combo dllUpProjectCombo;
	private Combo dllLevelCombo;
	private Text dllIdText;
	private Text dllNameText;
	private Text dllDescText;
	private Combo dllTypeCombo;
	private ISelection selection;

	public NewDllWizardPage0(ISelection selection)
	{
		super("NewDllWizardPage0");
		setTitle("�½���̬����");
		setDescription("����򵼽�ָ�������GOLP��̬��Ĵ���");
		this.selection = selection;
	}

	public Combo getDllUpProjectCombo()
	{
		return dllUpProjectCombo;
	}

	public Combo getDllLevelCombo()
	{
		return dllLevelCombo;
	}

	public Text getDllIdText()
	{
		return dllIdText;
	}

	public Text getDllNameText()
	{
		return dllNameText;
	}

	public Text getDllDescText()
	{
		return dllDescText;
	}
	
	public Combo getDllTypeCombo()
	{
		return dllTypeCombo;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO �Զ����ɵķ������
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout gl_container = new GridLayout(2, false);
		container.setLayout(gl_container);
		
		Label dllUpProjectLabel = new Label(container, SWT.NONE);
		dllUpProjectLabel.setText("*�������̣�");
		
		dllUpProjectCombo = new Combo(container, SWT.READ_ONLY);
		dllUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		IViewPart viewPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		if (viewPart != null)
		{
			NavView v = (NavView) viewPart;
			TreeViewer treeViewer = v.getTreeViewer();
			RootNode root = (RootNode) treeViewer.getInput();
			List<TreeNode> projectNodes = root.getChildren();
			for (TreeNode treeNode : projectNodes)
			{
				dllUpProjectCombo.add(treeNode.getName());
			}
		}
		
		Label dllLevelLabel = new Label(container, SWT.NONE);
		dllLevelLabel.setText("*�������");
		
		dllLevelCombo = new Combo(container, SWT.READ_ONLY);
		dllLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dllLevelCombo.add("0-APP");
		
		Label dllTypeLabel = new Label(container, SWT.NONE);
		dllTypeLabel.setText("*��̬�����ͣ�");
		
		dllTypeCombo = new Combo(container, SWT.READ_ONLY);
		dllTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dllTypeCombo.add("appAopDll");
		
		Label dllIdLabel = new Label(container, SWT.NONE);
		dllIdLabel.setText("*��̬���ʶ����");

		dllIdText = new Text(container, SWT.BORDER);
		dllIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		
		Label dllNameLabel = new Label(container, SWT.NONE);
		dllNameLabel.setText("*��̬�����ƣ�");

		dllNameText = new Text(container, SWT.BORDER);
		dllNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dllDescLabel = new Label(container, SWT.NONE);
		dllDescLabel.setText("*������");

		dllDescText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		dllDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		dllUpProjectCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dllLevelCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dllTypeCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dllIdText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if(RegExpCheck.isDllId(dllIdText.getText()))
				{
					setErrorMessage(null);
					
				}
				else
				{
					setErrorMessage("��̬���ʶ�����Ե���������ĸ��ͷ����������������");
				}
				dialogChanged();
			}
		});
		
		dllNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dllDescText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
	}

	// �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
	private void dialogChanged()
	{
		setPageComplete(true);
	}

	public boolean validInput()
	{
		if (	   getDllUpProjectCombo().getText().length() == 0
				|| getDllLevelCombo().getText().length() == 0
				|| !(RegExpCheck.isDllId(dllIdText.getText()))
				|| getDllNameText().getText().length() == 0
				|| getDllDescText().getText().length() == 0
				|| getDllTypeCombo().getText().length() == 0)
			return false;
		return true;
	}
	
	@Override
	public boolean canFlipToNextPage()
	{
		// TODO �Զ����ɵķ������
		return false;
	}

}
