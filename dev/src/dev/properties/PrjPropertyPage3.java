/* 文件名：       PrjPropertyPage3.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   1.用DebugOut.println方法替换System.out.println方法；
 *         2.统一使用File.separator；
 *         3.当前只处理AppHome和GolpHome。
 */


package dev.properties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import dev.db.service.ProjectDaoServiceImpl;
import dev.model.base.TreeNode;
import dev.util.CommonUtil;
import dev.util.DebugOut;
import dev.views.NavView;
/**
 * 该类定义了工程的第三个属性页，包括个性化头文件路径、个性化库路径、个性化原子交易库路径、
 * AppHome的路径和GolpHome的路径
 */
public class PrjPropertyPage3 extends PropertyPage implements
		IWorkbenchPropertyPage
{
	
	private PreferenceStore ps;
	private Text appHomeText;
	private Text golpHomeText;
	private Map<String, String> map;
	
	public PrjPropertyPage3()
	{
		setMessage("工程路径属性");
		initPs();
		try
        {
		    ProjectDaoServiceImpl projectDaoServiceImpl = new ProjectDaoServiceImpl();
		    map = projectDaoServiceImpl.queryProject(ps);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
	}
	/**
	 * 该方法用于获取工程对应的properties文件的路径
	 */
	private void initPs()
	{
		TreeViewer treeViewer = ((NavView)Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(NavView.ID)).getTreeViewer();
		StructuredSelection select = (StructuredSelection)treeViewer.getSelection();
		String prjId = ((TreeNode)select.getFirstElement()).getName();
		ps = CommonUtil.initPs(prjId);
	}


	@Override
	protected Control createContents(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));
		
		Label appHomeLabel = new Label(container, SWT.NONE);
		appHomeLabel.setText("*AppHome的路径：");
		
		appHomeText = new Text(container, SWT.BORDER);
		appHomeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		appHomeText.setText(map.get("APPHOME"));
		
		Label golpHomeLabel = new Label(container, SWT.NONE);
		golpHomeLabel.setText("*GolpHome的路径：");
		
		golpHomeText = new Text(container, SWT.BORDER);
		golpHomeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		golpHomeText.setText(map.get("GOLPHOME"));
		
		return container;
	}
	
	@Override
	public boolean performOk()
	{
		if( appHomeText.getText().isEmpty()
		 || golpHomeText.getText().isEmpty())
		{
			setErrorMessage("请将信息输入完整");
			return false;
		}
		setErrorMessage(null);
		
		List<String> datalist = new ArrayList<String>();
        datalist.add(appHomeText.getText());
        datalist.add(golpHomeText.getText());
        try
        {
            ProjectDaoServiceImpl projectDaoServiceImpl = new ProjectDaoServiceImpl();
            projectDaoServiceImpl.updateProject2(datalist, ps);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
		DebugOut.println("OK");
		return true;
		
	}
	@Override
	protected void performDefaults()
	{
		appHomeText.setText("默认路径");
		golpHomeText.setText("默认路径");
	}

}
