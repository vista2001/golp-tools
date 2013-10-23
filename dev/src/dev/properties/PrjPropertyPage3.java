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
	private Text prjIncludesPathText;
	private Text prjLibsPathText;
	private Text prjAopLibsText;
	
	private PreferenceStore ps;
	private String propertyPath = "";
	private Text appHomePathText;
	private Text golpHomePathText;
	
	public PrjPropertyPage3()
	{
		
		// TODO �Զ����ɵĹ��캯�����
		setMessage("����·������");
		//setDescription("3333");
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
		ps.setDefault("prjIncludesPath","δ���ļ���ȡ��");
		ps.setDefault("prjLibsPath", "δ���ļ���ȡ��");
		ps.setDefault("prjAopLibs", "δ���ļ���ȡ��");
		ps.setDefault("appHomePath", "δ���ļ���ȡ��");
		ps.setDefault("golpHomePath", "δ���ļ���ȡ��");
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
		container.setLayout(new GridLayout(2, false));
		
		Label prjIncludesPathLabel = new Label(container, SWT.NONE);
		prjIncludesPathLabel.setText("*���Ի�ͷ�ļ�·����");
		
		prjIncludesPathText = new Text(container, SWT.BORDER);
		prjIncludesPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjIncludesPathText.setText(ps.getString("prjIncludesPath"));
		
		Label prjLibsPathLabel = new Label(container, SWT.NONE);
		prjLibsPathLabel.setText("*���Ի���·����");
		
		prjLibsPathText = new Text(container, SWT.BORDER);
		prjLibsPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjLibsPathText.setText(ps.getString("prjLibsPath"));
		
		Label prjAopLibsLabel = new Label(container, SWT.NONE);
		prjAopLibsLabel.setText("*���Ի�ԭ�ӽ��׿�·����");
		
		prjAopLibsText = new Text(container, SWT.BORDER);
		prjAopLibsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		prjAopLibsText.setText(ps.getString("prjAopLibs"));
		
		Label appHomePathLabel = new Label(container, SWT.NONE);
		appHomePathLabel.setText("*AppHome��·����");
		
		appHomePathText = new Text(container, SWT.BORDER);
		appHomePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		appHomePathText.setText(ps.getString("appHomePath"));
		
		Label golpHomePathLabel = new Label(container, SWT.NONE);
		golpHomePathLabel.setText("*GolpHome��·����");
		
		golpHomePathText = new Text(container, SWT.BORDER);
		golpHomePathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		golpHomePathText.setText(ps.getString("golpHomePath"));
		
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
		if(prjIncludesPathText.getText().isEmpty()
				||prjLibsPathText.getText().isEmpty()
				||prjAopLibsText.getText().isEmpty()
				||appHomePathText.getText().isEmpty()
				||golpHomePathText.getText().isEmpty())
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
				
		ps.setValue("prjIncludesPath", prjIncludesPathText.getText().trim());
		ps.setValue("prjLibsPath", prjLibsPathText.getText().trim());
		ps.setValue("prjAopLibs", prjAopLibsText.getText().trim());
		ps.setValue("appHomePath", appHomePathText.getText().trim());
		ps.setValue("golpHomePath", golpHomePathText.getText().trim());
		
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
		prjIncludesPathText.setText("Ĭ��·��");
		prjLibsPathText.setText("Ĭ��·��");
		prjAopLibsText.setText("Ĭ��·��");
		appHomePathText.setText("Ĭ��·��");
		golpHomePathText.setText("Ĭ��·��");
	}

}
