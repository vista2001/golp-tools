package dev.wizards.newServer;

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
import org.eclipse.swt.widgets.List;
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

public class NewServerWizardPage3 extends WizardPage
{
	private ISelection selection;
	private Text serverSpecIncludePathText;
	private List serverSpecIncludePathList;
	private Button delButton;
	private Button upButton;
	private Button downButton;

	public ISelection getSelection()
	{
		return selection;
	}

	public List getServerSpecIncludePathList()
	{
		return serverSpecIncludePathList;
	}

	public NewServerWizardPage3(ISelection selection)
	{
		super("NewServerWizardPage3");
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

		Label serverSpecIncludePathLabel = new Label(container, SWT.NONE);
		serverSpecIncludePathLabel.setText("��������������ͷ�ļ�·����");

		serverSpecIncludePathText = new Text(container, SWT.BORDER);
		serverSpecIncludePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button addButton = new Button(container, SWT.NONE);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("���");
		addButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (!serverSpecIncludePathText.getText().isEmpty())
				{
					serverSpecIncludePathList.add(serverSpecIncludePathText.getText());
					serverSpecIncludePathText.setText("");
				}
			}
		});
		new Label(container, SWT.NONE);

		serverSpecIncludePathList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		serverSpecIncludePathList.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 3));
		serverSpecIncludePathList.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (serverSpecIncludePathList.getSelectionIndices().length > 0)
				{
					delButton.setEnabled(true);
					if (serverSpecIncludePathList.getSelectionIndices().length == 1)
					{
						upButton.setEnabled(true);
						downButton.setEnabled(true);
					}
					else
					{
						upButton.setEnabled(false);
						downButton.setEnabled(false);
					}
				}
			}
		});

		delButton = new Button(container, SWT.NONE);
		delButton.setEnabled(false);
		GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_delButton.widthHint = 60;
		delButton.setLayoutData(gd_delButton);
		delButton.setText("�Ƴ�");
		new Label(container, SWT.NONE);

		delButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int[] indices = serverSpecIncludePathList.getSelectionIndices();
				serverSpecIncludePathList.remove(indices);
				delButton.setEnabled(false);
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}
		});

		upButton = new Button(container, SWT.NONE);
		upButton.setEnabled(false);
		GridData gd_upButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				1);
		gd_upButton.widthHint = 60;
		upButton.setLayoutData(gd_upButton);
		upButton.setText("����");
		new Label(container, SWT.NONE);
		upButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int index = serverSpecIncludePathList.getSelectionIndex();
				if(index > 0)
				{
					String tmp = serverSpecIncludePathList.getItem(index);
					serverSpecIncludePathList.setItem(index, serverSpecIncludePathList.getItem(index-1));
					serverSpecIncludePathList.setItem(index-1, tmp);
					serverSpecIncludePathList.setSelection(index-1);
				}
			}
		});

		downButton = new Button(container, SWT.NONE);
		downButton.setEnabled(false);
		GridData gd_downButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_downButton.widthHint = 60;
		downButton.setLayoutData(gd_downButton);
		downButton.setText("����");
		downButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int index = serverSpecIncludePathList.getSelectionIndex();
				if(index < serverSpecIncludePathList.getItemCount()-1)
				{
					String tmp = serverSpecIncludePathList.getItem(index);
					serverSpecIncludePathList.setItem(index, serverSpecIncludePathList.getItem(index+1));
					serverSpecIncludePathList.setItem(index+1, tmp);
					serverSpecIncludePathList.setSelection(index+1);
				}
			}
		});

	}

	// �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����

	@Override
	public boolean canFlipToNextPage()
	{

		// System.out.println(getServerSpeclibText().getText().length());

		return true;

	}

}

