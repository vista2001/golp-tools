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
 * ���ඨ���˹��̵ĵڶ�������ҳ���������̶��õ����ݿ��ȫ����Ϣ
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
		// TODO �Զ����ɵĹ��캯�����
		setMessage("�������ݿ�����");
		//setDescription("2222");
		initPropertyPath();
		initPreferenceStore();
	}
	/**
	 * �÷������ڻ�ȡ���̶�Ӧ��properties�ļ���·��
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
	 * �÷������ڼ��ع��̶�Ӧ��properties�ļ���ps
	 */
	public void initPreferenceStore()
	{
		ps = new PreferenceStore(propertyPath);
		ps.setDefault("dbAddress","δ���ļ���ȡ��");
		ps.setDefault("dbPort", "δ���ļ���ȡ��");
		ps.setDefault("dbInstance", "δ���ļ���ȡ��");
		ps.setDefault("dbUser", "δ���ļ���ȡ��");
		ps.setDefault("dbPwd", "δ���ļ���ȡ��");
		ps.setDefault("dbType", "");
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
		
		Label dbTypeLabel = new Label(container, SWT.NONE);
		dbTypeLabel.setText("*���ݿ�����:");

		dbTypeCombo = new Combo(container, SWT.READ_ONLY);
		dbTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbTypeCombo.setItems(new String[] { "oracle" , "DB2"});
		dbTypeCombo.setText(ps.getString("dbType"));

		Label dbAddressLabel = new Label(container, SWT.NONE);
		dbAddressLabel.setText("*���ݿ��ַ:");

		dbAddressText = new Text(container, SWT.BORDER);
		dbAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbAddressText.setText(ps.getString("dbAddress"));

		Label dbPortLabel = new Label(container, SWT.NONE);
		dbPortLabel.setText("*���ݿ�˿�:");

		dbPortText = new Text(container, SWT.BORDER);
		dbPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbPortText.setText(ps.getString("dbPort"));

		Label dbInstanceLabel = new Label(container, SWT.NONE);
		dbInstanceLabel.setText("*���ݿ�ʵ��:");

		dbInstanceText = new Text(container, SWT.BORDER);
		dbInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbInstanceText.setText(ps.getString("dbInstance"));

		Label dbUserLabel = new Label(container, SWT.NONE);
		dbUserLabel.setText("*���ݿ��û�:");

		dbUserText = new Text(container, SWT.BORDER);
		dbUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbUserText.setText(ps.getString("dbUser"));

		Label dbPwdLabel = new Label(container, SWT.NONE);
		dbPwdLabel.setText("*���ݿ����:");

		dbPwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		dbPwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		dbPwdText.setText(ps.getString("dbPwd"));
		
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
		if(dbTypeCombo.getText().isEmpty()
				||dbAddressText.getText().isEmpty()
				||dbPortText.getText().isEmpty()
				||dbInstanceText.getText().isEmpty()
				||dbUserText.getText().isEmpty()
				||dbPwdText.getText().isEmpty())
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
		dbTypeCombo.setText("oracle");
		dbAddressText.setText("http://127.0.0.1");
		dbPortText.setText("8080");
		dbInstanceText.setText("XE");
		dbUserText.setText("GOLP");
		dbPwdText.setText("GOLP");
		
	}

}
