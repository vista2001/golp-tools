package dev.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.dialogs.PropertyDialogAction;

import dev.model.resource.ProjectNode;
import dev.views.NavView;
/**
 * 该类定义了在导航视图中，鼠标右键点击工程节点时，在弹出菜单所显示的“属性”按钮，当用户点击“属性”按钮后，会弹出工程属性对话框
 */
public class PrjPropertyAction extends Action implements ISelectionListener,
		IWorkbenchAction
{

	private IWorkbenchWindow window;
	public final static String ID = "dev.actions.prjPropertyAction";
	private IStructuredSelection selection;

	public PrjPropertyAction(IWorkbenchWindow window)
	{
		super("prjPropertyAction");
		setId(ID);
		setText("属性");
		// setToolTipText("My Action");
		// setImageDescriptor(MyRCPPlugin.getImageDescriptor("icons/sample.gif"));
		this.window = window;
		// 注册选择服务监听器
		window.getSelectionService().addSelectionListener(this);
		//setEnabled(false);
	}
	
	public void run()
	{
		PropertyDialogAction action = new PropertyDialogAction(window
				.getShell(), ((NavView)window.getActivePage().findView(NavView.ID)).getTreeViewer());
		action.run();
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//	    IWorkspaceRoot root = workspace.getRoot();
//	    IProject project = root.getProject("a");
//		String projectAbsPath=project.getLocationURI().toString();
//		System.out.println(projectAbsPath);
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection incoming)
	{
		// TODO 自动生成的方法存根
		if (incoming instanceof IStructuredSelection)
		{
			// 获得事件发生的源所携带的对象
			
			selection = (IStructuredSelection) incoming;
			if(selection.getFirstElement() instanceof ProjectNode)
			{
				this.setEnabled(true);
			}
			else
			{
				this.setEnabled(false);			
			}
		}

	}
	
	@Override
	public void dispose()
	{
		// TODO 自动生成的方法存根
		window.getSelectionService().removeSelectionListener(this);
	}

}
