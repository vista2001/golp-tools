/* 文件名：       NewDllWizardPage0.java
 * 修改人：       rxy
 * 修改时间：   2013.12.10
 * 修改内容：   1.修改部分UI；
 *         2.增加动态库路径；
 *         3.增加访问远程目录的功能。
 */

package dev.wizards.newDll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.db.pojo.TAopDll;
import dev.db.service.CommonDialogServiceImpl;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.remote.RemoteDialog;
import dev.util.CommonUtil;
import dev.views.NavView;
/**
 * 该类定义了新建服务程序向导的第1页
 */
public class NewDllWizardPage0 extends WizardPage
{
	private Combo aopDllUpProjectCombo;
	private Combo aopDllLevelCombo;
	private Text aopDllIdText;
	private Text aopDllPathText;
	private Text aopDllDescText;
	private Combo aopDllTypeCombo;
	private ISelection selection;
	private Text aopDllNameText;
	private Button aopDllPathButton;

	public NewDllWizardPage0(ISelection selection)
	{
		super("NewDllWizardPage0");
		setTitle("新建动态库向导");
		setDescription("这个向导将指导你完成GOLP动态库的创建");
		this.selection = selection;
	}

	public Combo getAopDllUpProjectCombo()
	{
		return aopDllUpProjectCombo;
	}

	public Combo getAopDllLevelCombo()
	{
		return aopDllLevelCombo;
	}

	public Text getAopDllIdText()
	{
		return aopDllIdText;
	}

	public Text getAopDllPathText()
	{
		return aopDllPathText;
	}

	public Text getAopDllDescText()
	{
		return aopDllDescText;
	}
	
	public Combo getAopDllTypeCombo()
	{
		return aopDllTypeCombo;
	}

	public Text getAopDllNameText()
    {
        return aopDllNameText;
    }

    @Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout gl_container = new GridLayout(3, false);
		container.setLayout(gl_container);
		
		Label aopDllUpProjectLabel = new Label(container, SWT.NONE);
		aopDllUpProjectLabel.setText("*所属工程：");
		
		aopDllUpProjectCombo = new Combo(container, SWT.READ_ONLY);
		aopDllUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
				aopDllUpProjectCombo.add(treeNode.getName());
			}
		}
		new Label(container, SWT.NONE);
		
		Label aopDllLevelLabel = new Label(container, SWT.NONE);
		aopDllLevelLabel.setText("*动态库级别：");
		
		aopDllLevelCombo = new Combo(container, SWT.READ_ONLY);
		aopDllLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aopDllLevelCombo.add("0-APP");
		new Label(container, SWT.NONE);
		
		Label aopDllTypeLabel = new Label(container, SWT.NONE);
		aopDllTypeLabel.setText("*动态库类型：");
		
		aopDllTypeCombo = new Combo(container, SWT.READ_ONLY);
		aopDllTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aopDllTypeCombo.add("appAopDll");
		new Label(container, SWT.NONE);
		
		Label aopDllIdLabel = new Label(container, SWT.NONE);
		aopDllIdLabel.setText("*动态库标识符：");

		aopDllIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		aopDllIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		new Label(container, SWT.NONE);
		
		Label aopDllNameLabel = new Label(container, SWT.NONE);
		aopDllNameLabel.setText("*动态库名称：");
		
		aopDllNameText = new Text(container, SWT.BORDER);
        aopDllNameText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
		aopDllNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label aopDllPathLabel = new Label(container, SWT.NONE);
		aopDllPathLabel.setText("*动态库路径：");

		aopDllPathText = new Text(container, SWT.BORDER);
		aopDllPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		
		aopDllPathButton = new Button(container, SWT.NONE);
		aopDllPathButton.setEnabled(false);
        aopDllPathButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                ArrayList<String> paths = new ArrayList<String>();
                RemoteDialog remoteDialog = new RemoteDialog(getShell(),
                        aopDllUpProjectCombo.getText(), null, RemoteDialog.REMOTEDIALOG_FILE,
                        RemoteDialog.REMOTEDIALOG_SINGLE, paths);
                remoteDialog.open();
                if(paths.size() == 1)
                {
                    aopDllPathText.setText(paths.get(0));
                }
            }
        });
		aopDllPathButton.setText("...");

		Label aopDllDescLabel = new Label(container, SWT.NONE);
		aopDllDescLabel.setText("*描述：");

		aopDllDescText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_aopDllDescText = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		gd_aopDllDescText.heightHint = 60;
		aopDllDescText.setLayoutData(gd_aopDllDescText);
		new Label(container, SWT.NONE);

		aopDllUpProjectCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
				if (aopDllUpProjectCombo.getText().isEmpty() == false)
                {
				    setDllIdText(CommonUtil.initPs(aopDllUpProjectCombo.getText()));
				    aopDllPathButton.setEnabled(true);
                }
			}
		});
		
		aopDllLevelCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		aopDllTypeCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
//		dllIdText.addModifyListener(new ModifyListener()
//		{
//			public void modifyText(ModifyEvent e)
//			{
//				if(RegExpCheck.isDllId(dllIdText.getText()))
//				{
//					setErrorMessage(null);
//					
//				}
//				else
//				{
//					setErrorMessage("动态库标识符需以单个或多个字母开头，后跟零个或多个数字");
//				}
//				dialogChanged();
//			}
//		});
		
		aopDllPathText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		aopDllDescText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
	}
	
	/**
	 * 该方法用于设置所新建的动态库的ID，首先从所传递的PreferenceStore对象
	 * 获取数据库连接，然后从AOPDLL表中查找当前AOPDLLID的最大值，此值加1即为
	 * 所新建的动态库的ID，若AOPDLL表为空，则查找project的AOPDLLIDSTART
	 * 字段，此值即为所新建的动态库的ID。
	 * @param ps
	 */
	private void setDllIdText(PreferenceStore ps)
    {
	    CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
        try
        {
            TAopDll aopDll = commonDialogServiceImpl.getNewAopDllId(aopDllUpProjectCombo.getText());
            aopDllIdText.setText(aopDll.getAopDllId() + "");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
	private void dialogChanged()
	{
		setPageComplete(true);
	}

	public boolean validInput()
	{
		if (	   aopDllUpProjectCombo.getText().isEmpty()
				|| aopDllLevelCombo.getText().isEmpty()
				|| aopDllIdText.getText().isEmpty()
				|| aopDllNameText.getText().isEmpty()
				|| aopDllPathText.getText().isEmpty()
				|| aopDllDescText.getText().isEmpty()
				|| aopDllTypeCombo.getText().isEmpty())
			return false;
		return true;
	}
	
	@Override
	public boolean canFlipToNextPage()
	{
		return false;
	}

}
