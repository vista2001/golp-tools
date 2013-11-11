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
 * 该类定义了新建服务程序向导的第1页
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
		setTitle("新建动态库向导");
		setDescription("这个向导将指导你完成GOLP动态库的创建");
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
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout gl_container = new GridLayout(2, false);
		container.setLayout(gl_container);
		
		Label dllUpProjectLabel = new Label(container, SWT.NONE);
		dllUpProjectLabel.setText("*所属工程：");
		
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
		dllLevelLabel.setText("*数据项级别：");
		
		dllLevelCombo = new Combo(container, SWT.READ_ONLY);
		dllLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dllLevelCombo.add("0-APP");
		
		Label dllTypeLabel = new Label(container, SWT.NONE);
		dllTypeLabel.setText("*动态库类型：");
		
		dllTypeCombo = new Combo(container, SWT.READ_ONLY);
		dllTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dllTypeCombo.add("appAopDll");
		
		Label dllIdLabel = new Label(container, SWT.NONE);
		dllIdLabel.setText("*动态库标识符：");

		dllIdText = new Text(container, SWT.BORDER);
		dllIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		
		Label dllNameLabel = new Label(container, SWT.NONE);
		dllNameLabel.setText("*动态库名称：");

		dllNameText = new Text(container, SWT.BORDER);
		dllNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dllDescLabel = new Label(container, SWT.NONE);
		dllDescLabel.setText("*描述：");

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
					setErrorMessage("动态库标识符需以单个或多个字母开头，后跟零个或多个数字");
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

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
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
		// TODO 自动生成的方法存根
		return false;
	}

}
