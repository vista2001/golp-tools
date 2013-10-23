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
		setText("����");
		// setToolTipText("My Action");
		// setImageDescriptor(MyRCPPlugin.getImageDescriptor("icons/sample.gif"));
		this.window = window;
		// ע��ѡ����������
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
		// TODO �Զ����ɵķ������
		if (incoming instanceof IStructuredSelection)
		{
			// ����¼�������Դ��Я���Ķ���
			
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
		// TODO �Զ����ɵķ������
		window.getSelectionService().removeSelectionListener(this);
	}

}
