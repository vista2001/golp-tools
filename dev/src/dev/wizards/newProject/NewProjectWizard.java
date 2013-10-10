package dev.wizards.newProject;

import java.io.File;
import java.sql.SQLException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.Activator;
import dev.db.DbConnectImpl;
import dev.model.base.RootNode;
import dev.model.resource.ProjectNode;
import dev.views.NavView;

public class NewProjectWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private NewProjectWizardPage0 page0;
	private NewProjectWizardPage1 page1;
	private NewProjectWizardPage2 page2;
	private IWorkbench workbench;
	
	public NewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}

	public void addPages() {
		page0 = new NewProjectWizardPage0(selection);
		page1 = new NewProjectWizardPage1(selection);
		page2 = new NewProjectWizardPage2(selection);
		
		addPage(page0);
		//addPage(page1);
		//addPage(page2);
	}

	@Override
	public boolean performFinish() {
		createDirectorys();
		 String prjId = page0.getPrjIdText().getText();
		 String prjName = page0.getPrjNameText().getText();
		 String prjDesc = page0.getPrjDescText().getText();
					doFinish(prjId, prjName, prjDesc);
					System.out.println(Activator.getDefault()
							.getStateLocation().append("src"));
					//从配置文件读取默认路径
					StringBuffer dirPath=new StringBuffer();
					dirPath.append("d:/");
					dirPath.append("golptests");
					File s=new File(dirPath.toString());
					s.mkdir();
					File a=Activator.getDefault().getStateLocation().append("src1").toFile();
					System.out.println(a.exists());
					a.mkdir();
					IViewPart view = this.workbench.getActiveWorkbenchWindow().getActivePage().findView(NavView.ID);
					 if (view != null) {
				          NavView v=(NavView)view;
				          TreeViewer tv=v.getTreeViewer();
				          RootNode root=(RootNode)tv.getInput();
				          if(root==null){
				        	root=new RootNode("root", "root", null);  
				        	ProjectNode prj=new ProjectNode(prjId, prjName, root);
				        	root.add(prj);
				        	tv.setInput(root);
				          }else{
				        	  ProjectNode prj=new ProjectNode(prjId, prjName, root);
				        	  root.add(prj);
				          }
				          System.out.println(root.name);
				          v.getTreeViewer().refresh();
				        }
					 prj(prjId);
		return true;
	}

	private void doFinish(String prjId, String prjName, String prjDesc
			) {
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		try {
			dbConnImpl.executeExceptQuery("insert into project values('aa','aa','aa',null,null,null)");
		} catch (SQLException e) {
	// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
private void createDirectorys(){
	
}
	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "ccp", IStatus.OK, message,
				null);
		throw new CoreException(status);
	}
	
	private void prj(String prjId){
/*		ISelection selection;
		IWorkbench workbench;*/
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject project = root.getProject(prjId);
	    IProject[] prj1=root.getProjects();
	    for (IProject iProject : prj1) {
			System.out.println(iProject.getName());
			System.out.println(iProject.getFullPath());
			System.out.println(iProject.getLocation());
		}
	    System.out.println("project is exist:"+project.exists());
	    if (!project.exists())
	        try
	        {
	          project.create(null);
	          project.open(null);

	          IProjectDescription projectDesc = project.getDescription();
	          String[] oldIds = projectDesc.getNatureIds();
	          String[] newIds = new String[oldIds.length + 1];
	          System.arraycopy(oldIds, 0, newIds, 0, oldIds.length);
	          newIds[oldIds.length] = "dev.natures.golpProjectNature";
	          projectDesc.setNatureIds(newIds);
	          project.setDescription(projectDesc, null);

	          /*IFile serverFile = project.getFile(new Path(".server"));
	          InputStream serverStream = sc.object2Stream();
	          if (serverFile.exists())
	            serverFile.setContents(serverStream, true, true, null);
	          else {
	            serverFile.create(serverStream, true, null);
	          }
	          serverStream.close();*/

/*	          IViewPart view = this.workbench.getActiveWorkbenchWindow().getActivePage().findView("com.tongdatech.sef.ui.views.ItemExplorer");

	          if (view != null) {
	            ((ItemExplorer)view).getTreeViewer().setInput(new com.tongdatech.sef.ui.views.navigator.item.TreeRoot("root", "root", null));
	          } else {
	            view = this.workbench.getActiveWorkbenchWindow().getActivePage().findView("com.tongdatech.sef.ui.views.ResourceExplorer");
	            if (view != null) {
	              ((ResourceExplorer)view).getTreeViewer().setInput(new com.tongdatech.sef.ui.views.navigator.resource.TreeRoot("root", "root", null));
	            }
	          }*/
	        }
	        catch (Exception e)
	        {
	          //LogUtil.getInstance().logError("创建工程失败!", e);
	        	e.printStackTrace();
	        }
	      else
	        try {
	          project.open(null);
	        } catch (CoreException e) {
	          //LogUtil.getInstance().logError("创建工程失败!", e);
	        	e.printStackTrace();
	        }
	}

	@Override
	public boolean canFinish() {
		System.out.println(page0.canFlipToNextPage());
		/*if(page0.canFlipToNextPage())
			return true;*/
		return this.getContainer().getCurrentPage().canFlipToNextPage();
		//return false;
	}
}
