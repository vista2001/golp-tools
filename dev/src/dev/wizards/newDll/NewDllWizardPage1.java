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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.views.NavView;

public class NewDllWizardPage1 extends WizardPage
{
	private ISelection selection;
	private Combo dllTypeCombo;
	private Combo dllUpProjectCombo;
	
	
	
	public Combo getDllTypeCombo()
	{
		return dllTypeCombo;
	}

	public Combo getDllUpProjectCombo()
	{
		return dllUpProjectCombo;
	}

	public NewDllWizardPage1(ISelection selection)
	{
		super("NewDllWizardPage1");
		setTitle("新建动态库向导");
		setDescription("这个向导将指导你完成GOLP动态库的创建");
		this.selection = selection;
	}
	
	@Override
	public void createControl(Composite parent)
	{
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label dllTypeLabel = new Label(container, SWT.NONE);
		dllTypeLabel.setText("*动态库类型：");
		
		dllTypeCombo = new Combo(container, SWT.READ_ONLY);
		dllTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dllTypeCombo.add("libGolp");
		dllTypeCombo.add("golpAOP");
		dllTypeCombo.add("golpTrad");
		dllTypeCombo.add("libApp");
		dllTypeCombo.add("appAOP");
		dllTypeCombo.add("CallBacks");
		
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
		
		dllTypeCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dllUpProjectCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});

	}
	
	private void dialogChanged()
	{
		setPageComplete(true);
	}
	
	public boolean validInput()
	{
		if (dllTypeCombo.getText().isEmpty() || dllUpProjectCombo.getText().isEmpty())
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
