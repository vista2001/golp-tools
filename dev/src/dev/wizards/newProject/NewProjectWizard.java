/* 文件名：       NewProjectWizard.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   1.用DebugOut.println方法替换System.out.println方法；
 *         2.在informParts方法中，将以下代码（共2处）：
 *         ProjectNode prj = new ProjectNode(prjId, prjName, root);
 *         替换为：
 *         ProjectNode prj = new ProjectNode(prjId, prjId, root);
 */

package dev.wizards.newProject;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

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

import dev.db.pojo.TProject;
import dev.db.service.ProjectDaoServiceImpl;
import dev.model.base.RootNode;
import dev.model.resource.ProjectNode;
import dev.util.DebugOut;
import dev.util.PropertiesUtil;
import dev.views.NavView;

public class NewProjectWizard extends Wizard  implements INewWizard{
	private ISelection selection;
	private NewProjectWizardPage0 page0;
	private NewProjectWizardPage1 page1;
	private NewProjectWizardPage2 page2;
	private NewProjectWizardPage3 page3;
	private IWorkbench workbench;
	private String projectAbsPath="";
	private TProject project;
	
	public NewProjectWizard() {
		super();
		project = new TProject();
		//setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}
	
	@Override
	public void addPages() {
		page0 = new NewProjectWizardPage0(selection);
		page1 = new NewProjectWizardPage1(selection);
		page2 = new NewProjectWizardPage2(selection);
		page3 = new NewProjectWizardPage3(selection);
		addPage(page0);
		addPage(page1);
		addPage(page2);
		addPage(page3);
	}

	@Override
	public boolean performFinish() {
		
	    getData();
		
		//创建工程工作区文件
		createProject(project.getPrjId());
		//创建工程工作目录
		createDirectorys();
		
		//创建工程属性文件
		createProperties();
		
		
        try
        {
            //创建数据库记录，此处之所以把创建数据库记录放在最后，是因为写数据库时，需要本地属性文件中的信息。
            createDbRecord(project);
            
            //通知其它视图或编辑器等
            informParts(project.getPrjId(), project.getPrjName(), project.getPrjDesc());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return true;
	}
	
	private void getData()
	{
	    String prjId = page0.getPrjIdText().getText();
	    project.setPrjId(prjId);
        String prjName = page0.getPrjNameText().getText();
        project.setPrjName(prjName);
        String prjDesc = page0.getPrjDescText().getText();
        project.setPrjDesc(prjDesc);
        String appHome = page3.getAppHomeText().getText();
        project.setAppHome(appHome);
        String golpHome = page3.getGolpHomeText().getText();
        project.setGolpHome(golpHome);
	}

	@Override
	public boolean canFinish() {
		boolean p0 = page0.canFlipToNextPage();
		boolean p1 = page1.canFlipToNextPage();
		boolean p2 = page2.isPageComplete();
		if (p0 && p1 && p2) {
			return true;
		}
		return false;
	}

	private void createDbRecord(TProject project) throws SQLException 
	{
	    ProjectDaoServiceImpl projectDaoServiceImpl = new ProjectDaoServiceImpl();
	    projectDaoServiceImpl.insertProject(project);
	}

	private void createProject(String prjId){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject project = root.getProject(prjId);
	    IProject[] prj1=root.getProjects();
/*	    for (IProject iProject : prj1) {
			DebugOut.println(iProject.getName());
			DebugOut.println(iProject.getFullPath());
			DebugOut.println(iProject.getLocation());
		}*/
	    if (!project.exists())
	        try
	        {
	          project.create(null);
	          project.open(null);
	          projectAbsPath=project.getLocation().toFile().getCanonicalPath();
	          System.out.println(project.getLocationURI().toString());
	          IProjectDescription projectDesc = project.getDescription();
	          String[] oldIds = projectDesc.getNatureIds();
	          String[] newIds = new String[oldIds.length + 1];
	          System.arraycopy(oldIds, 0, newIds, 0, oldIds.length);
	          newIds[oldIds.length] = "dev.natures.golpProjectNature";
	          projectDesc.setNatureIds(newIds);
	          project.setDescription(projectDesc, null);
	
	        }
	        catch (Exception e)
	        {
	          //LogUtil.getInstance().logError("创建工程失败!", e);
	        	//LogUtil.getInstance().logError("创建工程失败!", e);
	        	//LogUtil.getInstance().logError("创建工程失败!", e);
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

    /** 创建相关工程目录 */
    private void createDirectorys()
    {
        // 读取路径
        String targetPath = projectAbsPath;
        String sourcePath = (new File(projectAbsPath).getParentFile().getParent())
                            + File.separator + "temp";
        System.out.println(targetPath);
        System.out.println(sourcePath);
        Process p = null;
        try
        {
            p = Runtime.getRuntime().exec(
                    "cmd /c xcopy " + sourcePath + " " + targetPath + "/e");
            p.waitFor();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

	/** 通知其他视图或编辑器*/
	private void informParts(String prjId, String prjName, String prjDesc) {
	IViewPart view = this.workbench.getActiveWorkbenchWindow().getActivePage().findView(NavView.ID);
	if (view != null) {
		NavView v = (NavView) view;
		TreeViewer tv = v.getTreeViewer();
		RootNode root = (RootNode) tv.getInput();
		if (root == null) {
			root = new RootNode("root", "root", null);
//			ProjectNode prj = new ProjectNode(prjId, prjName, root);
			ProjectNode prj = new ProjectNode(prjId, prjId, root);
			root.add(prj);
			tv.setInput(root);
		} else {
//			ProjectNode prj = new ProjectNode(prjId, prjName, root);
			ProjectNode prj = new ProjectNode(prjId, prjId, root);
			root.add(prj);
			}
			v.getTreeViewer().refresh();
		}
	}
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "ccp", IStatus.OK, message,
				null);
		throw new CoreException(status);
	}
	
	private void createProperties(){
		LinkedHashMap<String,String> map=new LinkedHashMap<String, String>();
/*		map.put("dbAddress", page1.getDbAddressText().getText());
		map.put("dbInstance", page1.getDbInstanceText().getText());
		map.put("dbPort", page1.getDbPortText().getText());
		map.put("dbUser", page1.getDbUserText().getText());
		map.put("dbPwd", page1.getDbPwdText().getText());*/
		map.put("prjId", page0.getPrjIdText().getText());
//		map.put("prjName", page0.getPrjNameText().getText());
//		map.put("prjDesc", page0.getPrjDescText().getText());
//		map.put("dbType", page1.getDbTypeCombo().getText());
		map.put("dbAddress", page1.getDbAddressText().getText());
		map.put("dbInstance", page1.getDbInstanceText().getText());
		map.put("dbPort", page1.getDbPortText().getText());
		map.put("dbUser", page1.getDbUserText().getText());
		map.put("dbPwd", page1.getDbPwdText().getText());
		map.put("remoteAddress", page2.getRemoteAddressText().getText());
		map.put("remoteUser", page2.getRemoteUserText().getText());
		map.put("remotePwd", page2.getRemotePwdText().getText());
//		map.put("appHomePath", page3.getAppHomePathText().getText());
//		map.put("golpHomePath", page3.getGolpHomePathText().getText());
		String properties=projectAbsPath+File.separator+page0.getPrjIdText().getText()+".properties";
		DebugOut.println("properties path is :"+properties);
		PropertiesUtil.rewriteProperties1(properties, map);
		
	}
}
