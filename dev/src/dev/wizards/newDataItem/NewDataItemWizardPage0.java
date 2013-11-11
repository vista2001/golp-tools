package dev.wizards.newDataItem;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferenceStore;
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

import dev.db.DbConnectImpl;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.views.NavView;
/**
 * 该类定义了新建数据项向导的 第1页
 */
public class NewDataItemWizardPage0 extends WizardPage
{
	private Combo dataItemUpProjectCombo;
	private Combo dataItemLvLCombo;
	private Text dataItemIdText;
	private Text dataItemNameText;
	private Text dataItemDescText;
	private ISelection selection;

	public NewDataItemWizardPage0(ISelection selection)
	{
		super("NewDataItemWizardPage0");
		setTitle("新建数据项向导");
		setDescription("这个向导将指导你完成GOLP数据项的创建");
		this.selection = selection;
	}

	public Combo getDataItemUpProjectCombo()
	{
		return dataItemUpProjectCombo;
	}

	public Combo getDataItemLvLCombo()
	{
		return dataItemLvLCombo;
	}

	public Text getDataItemIdText()
	{
		return dataItemIdText;
	}

	public Text getDataItemNameText()
	{
		return dataItemNameText;
	}

	public Text getDataItemDescText()
	{
		return dataItemDescText;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label dataItemUpProjectLabel = new Label(container, SWT.NONE);
		dataItemUpProjectLabel.setText("*所属工程：");
		
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
		
		Label dataItemLvLLabel = new Label(container, SWT.NONE);
		dataItemLvLLabel.setText("*数据项级别：");
		
		dataItemLvLCombo = new Combo(container, SWT.READ_ONLY);
		dataItemLvLCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dataItemLvLCombo.add("0-APP");

		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		prjIdLabel.setText("*数据项标识符：");

		dataItemIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		dataItemIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		//读取数据库，查看DATAITEM表是否已有DATALVL为APP的记录，若有，得到这些记录中DATAITEMID的最大值,
		//然后加1，填入dataItemIdText，若无，则从project表中读取dataidstart字段的值，填入dataItemIdText
		ResultSet rs = null;
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String Sql = "select max(DATAITEMID) from DATAITEM where DATALVL=0";
		try
		{
			rs = dbConnImpl.retrive(Sql);
			if (rs.next() && rs.getString(1) != null)
			{
				//System.out.println(rs.getInt(1));
				dataItemIdText.setText(rs.getInt(1) + 1 + "");
			}
			else
			{
				//dataItemIdText.setText("");
				Sql = "select dataidstart from project";
				rs = dbConnImpl.retrive(Sql);
				rs.next();
				dataItemIdText.setText(rs.getInt(1) + "");
			}

		} 
		catch (SQLException e1)
		{
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				dbConnImpl.closeConn();
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//若在上边的数据库中未找到合适的记录，则读取本地 的配置文件来设置dataItemId
//		if(dataItemIdText.getText().equals(""))
//		{
//			IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		    IWorkspaceRoot root = workspace.getRoot();
//		    String s = root.getLocationURI().toString();
//		    String filePath = root.getLocationURI().toString().substring(6, s.lastIndexOf("/")) + "/" + "GOLPCFG/golpcfg.properties";
//			System.out.println(filePath);
//		    PreferenceStore ps = new PreferenceStore(filePath);
//			ps.setDefault("DataIdStart","读取DataIdStart失败");
//			try
//			 {
//				 ps.load();
//				 dataItemIdText.setText(ps.getString("DataIdStart"));
//			 } 
//			 catch (IOException e)
//			 {
//					// TODO 自动生成的 catch 块
//				 e.printStackTrace();
//			 }
//			
//		}

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setText("*数据项名称：");

		dataItemNameText = new Text(container, SWT.BORDER);
		dataItemNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		prjDescLabel.setText("*描述：");

		dataItemDescText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		dataItemDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		dataItemUpProjectCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dataItemLvLCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dataItemIdText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dataItemNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		
		dataItemDescText.addModifyListener(new ModifyListener()
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

	@Override
	public boolean canFlipToNextPage()
	{
		// TODO 自动生成的方法存根
		if (       getDataItemUpProjectCombo().getText().length() == 0    
				|| getDataItemLvLCombo().getText().length() == 0
				|| getDataItemIdText().getText().length() == 0
				|| getDataItemNameText().getText().length() == 0
				|| getDataItemDescText().getText().length() == 0)
			return false;
		return true;
	}
}
