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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import dev.Activator;
import dev.model.base.TreeNode;
import dev.util.FileUtil;
import dev.util.PropertiesUtil;
import dev.views.NavView;

public class PrjPropertyPage3 extends PropertyPage implements
		IWorkbenchPropertyPage
{
	private Text prjCommIncludePathText;
	private Text prjCommLibPathText;
	private Text prjCommAopLibText;
	
	private PreferenceStore ps;
	private String propertyPath = "";
	
	public PrjPropertyPage3()
	{
		
		// TODO �Զ����ɵĹ��캯�����
		setMessage("�޸�3");
		setDescription("3333");
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
	}
	
	public void initPreferenceStore()
	{
		ps = new PreferenceStore(propertyPath);
		ps.setDefault("prjCommIncludePath","δ���ļ���ȡ��");
		ps.setDefault("prjCommLibPath", "δ���ļ���ȡ��");
		ps.setDefault("prjCommAopLib", "δ���ļ���ȡ��");
		try
		 {
			 ps.load();
		 } 
		 catch (IOException e)
		 {
				// TODO �Զ����ɵ� catch ��
			 e.printStackTrace();
		 }
	}

	@Override
	protected Control createContents(Composite parent)
	{
		// TODO �Զ����ɵķ������
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3, false));
		
		Label prjCommIncludePathLabel = new Label(container, SWT.NONE);
		prjCommIncludePathLabel.setText("*����ͷ�ļ�·����");
		
		prjCommIncludePathText = new Text(container, SWT.BORDER);
		prjCommIncludePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjCommIncludePathText.setText(ps.getString("prjCommIncludePath"));
		
		Button prjCommIncludePathBtn = new Button(container, SWT.NONE);
		prjCommIncludePathBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd=new DirectoryDialog(e.display.getActiveShell());
				prjCommIncludePathText.setText(dd.open());
			}
		});
		prjCommIncludePathBtn.setText("...");
		
		Label prjCommLibPathLabel = new Label(container, SWT.NONE);
		prjCommLibPathLabel.setText("*������·����");
		
		prjCommLibPathText = new Text(container, SWT.BORDER);
		prjCommLibPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjCommLibPathText.setText(ps.getString("prjCommLibPath"));
		
		Button prjCommLibPathBtn = new Button(container, SWT.NONE);
		prjCommLibPathBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd=new DirectoryDialog(e.display.getActiveShell());
				prjCommLibPathText.setText(dd.open());
			}
		});
		prjCommLibPathBtn.setText("...");
		
		Label prjCommAopLibLabel = new Label(container, SWT.NONE);
		prjCommAopLibLabel.setText("*����ԭ�ӽ��׿�·����");
		
		prjCommAopLibText = new Text(container, SWT.BORDER);
		prjCommAopLibText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjCommAopLibText.setText(ps.getString("prjCommAopLib"));
		
		Button prjCommAopLibBtn = new Button(container, SWT.NONE);
		prjCommAopLibBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog dd=new DirectoryDialog(e.display.getActiveShell());
				prjCommAopLibText.setText(dd.open());
			}
		});
		prjCommAopLibBtn.setText("...");
		
		try
		{
			ps.save();
		} catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		return container;
	}
	
	@Override
	public boolean performOk()
	{
		// TODO �Զ����ɵķ������
		if(prjCommIncludePathText.getText().isEmpty()
				||prjCommLibPathText.getText().isEmpty()
				||prjCommAopLibText.getText().isEmpty())
		{
			setErrorMessage("�뽫��Ϣ��������");
			return false;
		}
		setErrorMessage(null);
		
		try
		 {
			 ps.load();
		 } 
		 catch (IOException e)
		 {
				// TODO �Զ����ɵ� catch ��
			 e.printStackTrace();
		 }
				
		ps.setValue("prjCommIncludePath", prjCommIncludePathText.getText().trim());
		ps.setValue("prjCommLibPath", prjCommLibPathText.getText().trim());
		ps.setValue("prjCommAopLib", prjCommAopLibText.getText().trim());
		
		try
		{
			ps.save();
		} 
		catch (IOException e)
		{
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		System.out.println("OK");
		return true;
		
	}
	@Override
	protected void performDefaults()
	{
		// TODO �Զ����ɵķ������
		prjCommIncludePathText.setText("Ĭ��·��");
		prjCommLibPathText.setText("Ĭ��·��");
		prjCommAopLibText.setText("Ĭ��·��");
	}

}
