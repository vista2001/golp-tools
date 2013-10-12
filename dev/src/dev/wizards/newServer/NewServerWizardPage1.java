package dev.wizards.newServer;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.views.NavView;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

public class NewServerWizardPage1 extends WizardPage
{
	private ISelection selection;
	private Text serverSpeclibText;
	private Text serverSpecAopDllsText;
	private Combo upProjectCombo;

	public ISelection getSelection()
	{
		return selection;
	}

	public Text getServerSpeclibText()
	{
		return serverSpeclibText;
	}

	public Text getServerSpecAopDllsText()
	{
		return serverSpecAopDllsText;
	}

	public Combo getUpProjectCombo()
	{
		return upProjectCombo;
	}

	public NewServerWizardPage1(ISelection selection)
	{
		super("NewServerWizardPage0");
		setTitle("�½����������");
		setDescription("����򵼽�ָ�������GOLP�������Ĵ���");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO �Զ����ɵķ������
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label serverSpeclibLabel = new Label(container, SWT.NONE);
		serverSpeclibLabel.setText("*�����lib��������");

		serverSpeclibText = new Text(container, SWT.BORDER);
		serverSpeclibText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		serverSpeclibText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button serverSpeclibButton = new Button(container, SWT.NONE);
		GridData gd_serverSpeclibButton = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_serverSpeclibButton.widthHint = 60;
		serverSpeclibButton.setLayoutData(gd_serverSpeclibButton);
		serverSpeclibButton.setText("ѡ��");
		serverSpeclibButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				System.out.println(serverSpeclibText.getText().length());
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				dialog.setMessage("��ѡ������lib��������Ŀ¼");
				dialog.setText("ѡ��Ŀ¼");
				dialog.setFilterPath("C:\\");
				serverSpeclibText.setText(dialog.open());
			}
		});

		Label serverSpecAopDllsLabel = new Label(container, SWT.NONE);
		serverSpecAopDllsLabel
				.setText("*���Ի���ԭ�ӽ��׿�������");

		serverSpecAopDllsText = new Text(container, SWT.BORDER);
		serverSpecAopDllsText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		serverSpecAopDllsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button serverSpecAopDllsButton = new Button(container, SWT.NONE);
		GridData gd_serverSpecAopDllsButton = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_serverSpecAopDllsButton.widthHint = 60;
		serverSpecAopDllsButton.setLayoutData(gd_serverSpecAopDllsButton);
		serverSpecAopDllsButton.setText("ѡ��");

		serverSpecAopDllsButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				dialog.setMessage("��ѡ����Ի���ԭ�ӽ��׿�������Ŀ¼");
				dialog.setText("ѡ��Ŀ¼");
				dialog.setFilterPath("C:\\");
				serverSpecAopDllsText.setText(dialog.open());
			}
		});

		Label upProjectLabel = new Label(container, SWT.NONE);
		upProjectLabel.setText("*�ϼ����̣�");

		upProjectCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		upProjectCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		upProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		new Label(container, SWT.NONE);
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
				upProjectCombo.add(treeNode.getName());
			}
		}
	}
	//�˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
	private void dialogChanged()
	{
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage()
	{

		// System.out.println(getServerSpeclibText().getText().length());
		if (getServerSpeclibText().getText().length() == 0
				|| getServerSpecAopDllsText().getText().length() == 0
				|| getUpProjectCombo().getText().length() == 0)
			return false;
		return true;

	}

}
