package dev.wizards.newServer;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class NewServerWizardPage2 extends WizardPage
{
	private ISelection selection;
	private List callBackSourceList;
	private Button delButton;

	public List getCallBackSourceList()
	{
		return callBackSourceList;
	}

	public NewServerWizardPage2(ISelection selection)
	{
		super("NewServerWizardPage2");
		setTitle("新建服务程序向导");
		setDescription("这个向导将指导你完成GOLP服务程序的创建");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		Label callBackSourceLabel = new Label(container, SWT.NONE);
		callBackSourceLabel.setText("请选择CallBackSource：");
		
		callBackSourceList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		callBackSourceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 2));

		Button addButton = new Button(container, SWT.NONE);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("添加");
		addButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN
						| SWT.MULTI);
				dialog.setFilterPath("C:\\");
				dialog.open();
				String[] files = dialog.getFileNames();
				for (String s : files)
				{
						if(dialog.getFilterPath().endsWith(File.separator))
						{
							callBackSourceList.add(dialog.getFilterPath() + s);
						}
						//callBackSourceList.add(dialog.getFilterPath() + s);
					else
						callBackSourceList.add(dialog.getFilterPath() + File.separator + s);
				}
			}
		});
		new Label(container, SWT.NONE);

		delButton = new Button(container, SWT.NONE);
		delButton.setEnabled(false);
		delButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int[] indices = callBackSourceList.getSelectionIndices();
				callBackSourceList.remove(indices);
				delButton.setEnabled(false);
			}
		});
		GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_delButton.widthHint = 60;
		delButton.setLayoutData(gd_delButton);
		delButton.setText("移除");
		
		callBackSourceList.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if(callBackSourceList.getSelectionIndices().length > 0)
				{
					delButton.setEnabled(true);
				}
			}
		});
	}

	@Override
	public boolean canFlipToNextPage()
	{

		return true;
	}

}
