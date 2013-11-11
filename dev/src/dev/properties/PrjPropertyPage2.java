package dev.properties;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import dev.Activator;
import dev.model.base.TreeNode;
import dev.util.FileUtil;
import dev.util.PropertiesUtil;
import dev.views.NavView;
/**
 * 该类定义了工程的第二个属性页，包括工程对用的数据库的全部信息
 */
public class PrjPropertyPage2 extends PropertyPage implements
		IWorkbenchPropertyPage
{

	private Text dbAddressText;
	private Text dbPortText;
	private Text dbInstanceText;
	private Text dbUserText;
	private Text dbPwdText;
	private Combo dbTypeCombo;
	
	private PreferenceStore ps;
	private String propertyPath = "";
	//private LinkedHashMap<String,String> map;
	
	public PrjPropertyPage2()
	{
		// TODO 自动生成的构造函数存根
		setMessage("工程数据库属性");
		//setDescription("2222");
		initPropertyPath();
		initPreferenceStore();
	}
	/**
	 * 该方法用于获取工程对应的properties文件的路径
	 */
	private void initPropertyPath()
	{
		TreeViewer treeViewer = ((NavView)Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(NavView.ID)).getTreeViewer();
		StructuredSelection select = (StructuredSelection)treeViewer.getSelection();
		String prjId = ((TreeNode)select.getFirstElement()).getName();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject project = root.getProject(prjId);
	    propertyPath = project.getLocationURI().toString().substring(6) + '/' + prjId +".properties";
	}
	/**
	 * 该方法用于加载工程对应的properties文件到ps
	 */
	public void initPreferenceStore()
	{
		ps = new PreferenceStore(propertyPath);
		ps.setDefault("dbAddress","未从文件读取到");
		ps.setDefault("dbPort", "未从文件读取到");
		ps.setDefault("dbInstance", "未从文件读取到");
		ps.setDefault("dbUser", "未从文件读取到");
		ps.setDefault("dbPwd", "未从文件读取到");
		ps.setDefault("dbType", "");
		try
		 {
			 ps.load();
		 } 
		 catch (IOException e)
		 {
				// TODO 自动生成的 catch 块
			 e.printStackTrace();
		 }
	}

	@Override
	protected Control createContents(Composite parent)
	{
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label dbTypeLabel = new Label(container, SWT.NONE);
		dbTypeLabel.setText("*数据库类型:");

		dbTypeCombo = new Combo(container, SWT.READ_ONLY);
		dbTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbTypeCombo.setItems(new String[] { "oracle" , "DB2"});
		dbTypeCombo.setText(ps.getString("dbType"));

		Label dbAddressLabel = new Label(container, SWT.NONE);
		dbAddressLabel.setText("*数据库地址:");

		dbAddressText = new Text(container, SWT.BORDER);
		dbAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbAddressText.setText(ps.getString("dbAddress"));

		Label dbPortLabel = new Label(container, SWT.NONE);
		dbPortLabel.setText("*数据库端口:");

		dbPortText = new Text(container, SWT.BORDER);
		dbPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbPortText.setText(ps.getString("dbPort"));

		Label dbInstanceLabel = new Label(container, SWT.NONE);
		dbInstanceLabel.setText("*数据库实例:");

		dbInstanceText = new Text(container, SWT.BORDER);
		dbInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbInstanceText.setText(ps.getString("dbInstance"));

		Label dbUserLabel = new Label(container, SWT.NONE);
		dbUserLabel.setText("*数据库用户:");

		dbUserText = new Text(container, SWT.BORDER);
		dbUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbUserText.setText(ps.getString("dbUser"));

		Label dbPwdLabel = new Label(container, SWT.NONE);
		dbPwdLabel.setText("*数据库口令:");

		dbPwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		dbPwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbPwdText.setText(ps.getString("dbPwd"));
		
		try
		{
			ps.save();
		} catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		return container;
	}
	
	@Override
	public boolean performOk()
	{
		// TODO 自动生成的方法存根
		if(dbTypeCombo.getText().isEmpty()
				||dbAddressText.getText().isEmpty()
				||dbPortText.getText().isEmpty()
				||dbInstanceText.getText().isEmpty()
				||dbUserText.getText().isEmpty()
				||dbPwdText.getText().isEmpty())
		{
			setErrorMessage("请将信息输入完整");
			return false;
		}
		setErrorMessage(null);
		
		try
		 {
			 ps.load();
		 } 
		 catch (IOException e)
		 {
				// TODO 自动生成的 catch 块
			 e.printStackTrace();
		 }
		
		ps.setValue("dbType", dbTypeCombo.getText().trim());
		ps.setValue("dbAddress", dbAddressText.getText().trim());
		ps.setValue("dbPort", dbPortText.getText().trim());
		ps.setValue("dbInstance", dbInstanceText.getText().trim());
		ps.setValue("dbUser", dbUserText.getText().trim());
		ps.setValue("dbPwd", dbPwdText.getText().trim());
		
		try
		{
			ps.save();
		} 
		catch (IOException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		System.out.println("OK");
		return true;
		
	}
	@Override
	protected void performDefaults()
	{
		// TODO 自动生成的方法存根
		dbTypeCombo.setText("oracle");
		dbAddressText.setText("http://127.0.0.1");
		dbPortText.setText("8080");
		dbInstanceText.setText("XE");
		dbUserText.setText("GOLP");
		dbPwdText.setText("GOLP");
		
	}

}
