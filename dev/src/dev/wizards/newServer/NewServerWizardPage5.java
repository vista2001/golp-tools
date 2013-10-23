package dev.wizards.newServer;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

public class NewServerWizardPage5 extends WizardPage
{
	private ISelection selection;
	private List othFunSrourceList;
	private Button delButton;
	
	public List getOthFunSrourceList()
	{
		return othFunSrourceList;
	}

	public NewServerWizardPage5(ISelection selection)
	{
		super("NewServerWizardPage5");
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
		
		Label  othFunSrourceLabel = new Label(container, SWT.NONE);
		 othFunSrourceLabel.setText("请选择OthFunSrource：");
		 
		 othFunSrourceList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		 othFunSrourceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
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
						othFunSrourceList.add(dialog.getFilterPath() + s);
					}
					
					else
					{
						othFunSrourceList.add(dialog.getFilterPath() + File.separator + s);
					}
					
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
				int[] indices =  othFunSrourceList.getSelectionIndices();
				 othFunSrourceList.remove(indices);
				delButton.setEnabled(false);
			}
		});
		GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_delButton.widthHint = 60;
		delButton.setLayoutData(gd_delButton);
		delButton.setText("移除");
		
		 othFunSrourceList.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if( othFunSrourceList.getSelectionIndices().length > 0)
				{
					delButton.setEnabled(true);
				}
			}
		});

	}
	
	@Override
	public boolean canFlipToNextPage()
	{

		return false;
	}

}
