package dev.wizards.newDataItem;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.views.NavView;

public class NewDataItemWizardPage1 extends WizardPage
{

	private ISelection selection;
	private Combo dataItemLvLCombo;
	private Combo dataItemTypeCombo;
	private Text dataItemlenText;
	private Text dataItemAOPText;
	private Combo dataItemUpProjectCombo;

	public Combo getDataItemLvLCombo()
	{
		return dataItemLvLCombo;
	}

	public Combo getDataItemTypeCombo()
	{
		return dataItemTypeCombo;
	}

	public Text getDataItemlenText()
	{
		return dataItemlenText;
	}

	public Text getDataItemAOPText()
	{
		return dataItemAOPText;
	}

	public Combo getDataItemUpProjectCombo()
	{
		return dataItemUpProjectCombo;
	}

	public NewDataItemWizardPage1(ISelection selection)
	{
		super("NewDataItemWizardPage0");
		setTitle("�½���������");
		setDescription("����򵼽�ָ�������GOLP������Ĵ���");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO �Զ����ɵķ������
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dataItemLvLLabel = new Label(container, SWT.NONE);
		dataItemLvLLabel.setText("*�������");

		dataItemLvLCombo = new Combo(container, SWT.READ_ONLY);
		dataItemLvLCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dataItemLvLCombo.add("GOLP");
		dataItemLvLCombo.add("APP");

		Label dataItemTypeLabel = new Label(container, SWT.NONE);
		dataItemTypeLabel.setText("*��������:");

		dataItemTypeCombo = new Combo(container, SWT.READ_ONLY);
		dataItemTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		dataItemTypeCombo.add("0-int");
		dataItemTypeCombo.add("1-long");
		dataItemTypeCombo.add("2-double");
		dataItemTypeCombo.add("3-char");
		dataItemTypeCombo.add("4-char[]");
		dataItemTypeCombo.add("5-String");

		Label dataItemlenLabel = new Label(container, SWT.NONE);
		dataItemlenLabel.setText("*������󳤶ȣ�");

		dataItemlenText = new Text(container, SWT.BORDER);
		dataItemlenText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dataItemAOPLabel = new Label(container, SWT.NONE);
		dataItemAOPLabel.setText("����Լ����麯����");

		dataItemAOPText = new Text(container, SWT.BORDER);
		dataItemAOPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		
		Label dataItemUpProjectLabel = new Label(container, SWT.NONE);
		dataItemUpProjectLabel.setText("*�ϼ�����:");
		
		dataItemUpProjectCombo = new Combo(container, SWT.READ_ONLY);
		dataItemUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
				dataItemUpProjectCombo.add(treeNode.getName());
			}
		}

		dataItemLvLCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});

		dataItemTypeCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});

		dataItemlenText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dataItemUpProjectCombo.addModifyListener(new ModifyListener()
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
		if (getDataItemLvLCombo().getText().length() == 0
				|| getDataItemTypeCombo().getText().length() == 0
				|| getDataItemlenText().getText().length() == 0
				|| getDataItemUpProjectCombo().getText().length() == 0)
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
