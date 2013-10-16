package dev.properties;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

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

public class PrjPropertyPage1 extends PropertyPage implements
		IWorkbenchPropertyPage
{
	private Text prjIdText;
	private Text prjNameText;
	private Text prjDescText;
	private PreferenceStore ps;
	private String propertyPath = "";
	
	public PrjPropertyPage1()
	{
		
		setMessage("修改");
		setDescription("lalala");
		initPropertyPath();
		initPreferenceStore();
	}
	
	private void initPropertyPath()
	{
		TreeViewer treeViewer = ((NavView)Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(NavView.ID)).getTreeViewer();
		StructuredSelection select = (StructuredSelection)treeViewer.getSelection();
		String prjId = ((TreeNode)select.getFirstElement()).getName();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject project = root.getProject(prjId);
	    propertyPath = project.getLocationURI().toString().substring(6) + '/' + prjId +".properties";
	    //System.out.println(propertyPath);
	}
	
	public void initPreferenceStore()
	{
		ps = new PreferenceStore(propertyPath);
		ps.setDefault("prjId","未从文件读取到");
		ps.setDefault("prjName", "未从文件读取到");
		ps.setDefault("prjDesc", "未从文件读取到");
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
		
		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setText("*工程标识符：");
		
		prjIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		prjIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjIdText.setText(ps.getString("prjId"));
	
		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setText("*工程名称：");
		
		prjNameText = new Text(container, SWT.BORDER);
		prjNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjNameText.setText(ps.getString("prjName"));

		
		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setText("*描述：");
		
		prjDescText = new Text(container, SWT.BORDER);
		prjDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		prjDescText.setText(ps.getString("prjDesc"));
		
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
		if(prjNameText.getText().isEmpty()
				||prjDescText.getText().isEmpty())
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
		
		ps.setValue("prjId", prjIdText.getText().trim());
		ps.setValue("prjName", prjNameText.getText().trim());
		ps.setValue("prjDesc", prjDescText.getText().trim());
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
		//prjIdText.setText("工程名不允许修改");
		prjNameText.setText("默认工程名称");
		prjDescText.setText("默认工程描述");
	}

}
