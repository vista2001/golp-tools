package dev.wizards.newProject;

import java.io.File;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import dev.Activator;
import dev.db.DbConnectImpl;
import dev.model.base.RootNode;
import dev.model.resource.ProjectNode;
import dev.util.FileUtil;
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
	
	public NewProjectWizard() {
		super();
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
		String prjId = page0.getPrjIdText().getText();
		String prjName = page0.getPrjNameText().getText();
		String prjDesc = page0.getPrjDescText().getText();
		
		//创建数据库记录
		createDbRecord(prjId, prjName, prjDesc);
		//创建工程工作区文件
		createProject(prjId);
		//创建工程工作目录
		createDirectorys();
		//通知其它视图或编辑器等
		informParts(prjId, prjName, prjDesc);
		//创建工程属性文件
		createProperties();
		return true;
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

	private void createDbRecord(String prjId, String prjName, String prjDesc) {
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		try {
			String preSql="insert into project values(?,?,?,null,null,null)";
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedString(1, prjId);
			dbConnImpl.setPreparedString(2, prjName);
			dbConnImpl.setPreparedString(3, prjDesc);
			dbConnImpl.executeExceptPreparedQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void createProject(String prjId){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject project = root.getProject(prjId);
	    IProject[] prj1=root.getProjects();
/*	    for (IProject iProject : prj1) {
			System.out.println(iProject.getName());
			System.out.println(iProject.getFullPath());
			System.out.println(iProject.getLocation());
		}*/
	    if (!project.exists())
	        try
	        {
	          project.create(null);
	          project.open(null);
	          projectAbsPath=project.getLocationURI().toString();
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
	/**创建相关工程目录*/
	private void createDirectorys() {
		//读取路径
		String projectPath=projectAbsPath.substring(6);
		List<String> sorceList=FileUtil.iteratorDirs("e:\\temp",null);
		for (String string : sorceList) {
			String sourceString=string;
			string=projectPath+string.substring(7);
			System.out.println(string);
			System.out.println(sourceString);
			if(string.endsWith(File.separator)){
				FileUtil.createMultiDir(string);
			}else{
				FileUtil.createFile(string);
				if(!FileUtil.isFileBlank(sourceString)){
					List<String> sourceFile=FileUtil.readFileByLines(sourceString);
					for (String string2 : sourceFile) {
						FileUtil.writeFileByLine(string, string2);
					}
				}
			}
			
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
			ProjectNode prj = new ProjectNode(prjId, prjName, root);
			root.add(prj);
			tv.setInput(root);
		} else {
			ProjectNode prj = new ProjectNode(prjId, prjName, root);
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
		map.put("prjName", page0.getPrjNameText().getText());
		map.put("prjDesc", page0.getPrjDescText().getText());
		map.put("dbType", page1.getDbTypeCombo().getText());
		map.put("dbAddress", page1.getDbAddressText().getText());
		map.put("dbInstance", page1.getDbInstanceText().getText());
		map.put("dbPort", page1.getDbPortText().getText());
		map.put("dbUser", page1.getDbUserText().getText());
		map.put("dbPwd", page1.getDbPwdText().getText());
		map.put("prjCommIncludePath", page3.getPrjCommIncludePathText().getText());
		map.put("prjCommLibPath", page3.getPrjCommLibPathText().getText());
		map.put("prjCommAopLib", page3.getPrjCommAopLibText().getText());
		String properties=projectAbsPath.substring(5)+File.separator+page0.getPrjIdText().getText()+".properties";
		System.out.println("properties path is :"+properties);
		PropertiesUtil.rewriteProperties1(properties, map);
		
	}
}
