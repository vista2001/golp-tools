/* 文件名：       LoadProjectWizard.java
 * 描述：           该文件定义了类LoadProjectWizard，该类用于从用户给定的数据库中读取工程记录，
 *         并将该工程载入当前工作区。
 * 创建人：       rxy
 * 创建时间：   2013.12.26
 */

package dev.wizard.loadProject;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.service.ProjectDaoServiceImpl;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.util.DebugOut;
import dev.util.PropertiesUtil;
import dev.views.NavView;

/**
 * 该类用于从用户给定的数据库中读取工程记录，并将该工程载入当前工作区。
 * 
 * @author rxy
 */
public class LoadProjectWizard extends Wizard implements INewWizard
{
    private ISelection selection;

    /**
     * 该向导所包含的向导页，为LoadProjectWizardPage0类的对象
     */
    private LoadProjectWizardPage0 page0;

    private IWorkbench workbench;
    private String projectAbsPath = "";

    /**
     * 数据库地址。
     */
    private String dbAddress;

    /**
     * 数据库端口。
     */
    private String dbPort;

    /**
     * 数据库实例。
     */
    private String dbInstance;

    /**
     * 数据库用户名。
     */
    private String dbUser;

    /**
     * 数据库密码。
     */
    private String dbPwd;

    /**
     * 工程的ID。
     */
    private String prjId;

    /**
     * 服务器地址。
     */
    private String remoteAddress;

    /**
     * 服务器用户。
     */
    private String remoteUser;

    /**
     * 服务器口令。
     */
    private String remotePwd;

    public LoadProjectWizard()
    {
        super();
        // setNeedsProgressMonitor(true);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        this.selection = selection;
        this.workbench = workbench;
    }

    @Override
    public void addPages()
    {
        page0 = new LoadProjectWizardPage0(selection);
        addPage(page0);
    }

    @Override
    public boolean performFinish()
    {
        getData();
        getProjectId();

        // TODO 当前的设计中，只能保证在用户正确输入了数据库连接信息后，若当前数据库中没有project表或有该表但
        // 该表中无记录，才会对用户给出明确提示，但对于用户给定的错误的数据库连接信息，当前并未处理。

        // 若在给定的数据库中，未找到工程记录
        if (prjId == null)
        {
            showMessage(SWT.ICON_INFORMATION | SWT.YES, "提示", "工程记录不存在");
            return false;
        }

        // 若找到的工程在当前工作区中已存在
        if (isProjectExist() == true)
        {
            showMessage(SWT.ICON_INFORMATION | SWT.YES, "提示",
                    "该数据库中的工程在当前工作区中已存在");
            return false;
        }

        // 若程序运行至此处，说明已找到了一条工程记录，且该工程在当前工作区中不存在。此时询问用户是否载入该工程。
        int result = showMessage(SWT.ICON_INFORMATION | SWT.YES | SWT.NO, "提示",
                "找到以下工程：" + System.getProperty("line.separator") + prjId
                        + System.getProperty("line.separator") + "是否载入？");

        // 若用户选择载入
        if (result == SWT.YES)
        {
            // 创建工程工作区文件
            createProject(prjId);

            // 创建工程工作目录
            createDirectorys();

            // 通知其它视图或编辑器等
            informParts(prjId);

            // 创建工程属性文件
            createProperties();

            // 提示用户载入成功
            showMessage(SWT.ICON_INFORMATION | SWT.YES, "提示", "载入成功");

            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * 该方法用于判断所找到的工程是否已存在于当前的工作区中。
     * 
     * @return 若已存在，返回true，否则返回false。
     */
    private boolean isProjectExist()
    {
        IViewPart view = this.workbench.getActiveWorkbenchWindow().getActivePage().findView(
                NavView.ID);
        if (view != null)
        {
            NavView v = (NavView) view;
            TreeViewer tv = v.getTreeViewer();
            RootNode root = (RootNode) tv.getInput();
            if (root != null)
            {
                for (TreeNode treeNode : root.getChildren())
                {
                    if (treeNode.getName().equals(prjId))
                    {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    @Override
    public boolean canFinish()
    {
        return page0.isInputValid();
    }

    /**
     * 获取该新建向导中所输入的数据。
     */
    private void getData()
    {
        dbAddress = page0.getDbAddressText().getText();
        dbPort = page0.getDbPortText().getText();
        dbInstance = page0.getDbInstanceText().getText();
        dbUser = page0.getDbUserText().getText();
        dbPwd = page0.getDbPwdText().getText();
        remoteAddress = page0.getRemoteAddressText().getText();
        remoteUser = page0.getRemoteUserText().getText();
        remotePwd = page0.getRemotePwdText().getText();
    }

    /**
     * 该方法使用用户在当前页面中输入的数据库连接信息查找数据库，若该数据库中有工程记录，
     * 则将找到的工程的ID赋给成员变量prjId，否则，将prjId赋为null。
     */
    private void getProjectId()
    {
        ProjectDaoServiceImpl projectDaoServiceImpl = new ProjectDaoServiceImpl();
        try
        {
            prjId = projectDaoServiceImpl.queryProjectId(dbAddress, dbPort,
                    dbInstance, dbUser, dbPwd);
        }
        catch (SQLException e)
        {
            prjId = null;
            e.printStackTrace();
        }
    }

    private void createProject(String prjId)
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IProject project = root.getProject(prjId);
        IProject[] prj1 = root.getProjects();
        /*
         * for (IProject iProject : prj1) {
         * DebugOut.println(iProject.getName());
         * DebugOut.println(iProject.getFullPath());
         * DebugOut.println(iProject.getLocation()); }
         */
        if (!project.exists())
            try
            {
                project.create(null);
                project.open(null);
                projectAbsPath = project.getLocation().toFile().getCanonicalPath();
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
                // LogUtil.getInstance().logError("创建工程失败!", e);
                // LogUtil.getInstance().logError("创建工程失败!", e);
                // LogUtil.getInstance().logError("创建工程失败!", e);
                // LogUtil.getInstance().logError("创建工程失败!", e);
                e.printStackTrace();
            }
        else
            try
            {
                project.open(null);
            }
            catch (CoreException e)
            {
                // LogUtil.getInstance().logError("创建工程失败!", e);
                e.printStackTrace();
            }
    }

    /** 创建相关工程目录 */
    private void createDirectorys()
    {
        // 读取路径
        String targetPath = projectAbsPath;
        String sourcePath = (new File(projectAbsPath).getParentFile().getParent())
                            + File.separator + "sampleWorkDir";
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

    /** 通知其他视图或编辑器 */
    private void informParts(String prjId)
    {
        IViewPart view = this.workbench.getActiveWorkbenchWindow().getActivePage().findView(
                NavView.ID);
        if (view != null)
        {
            NavView v = (NavView) view;
            TreeViewer tv = v.getTreeViewer();
            RootNode root = (RootNode) tv.getInput();
            if (root == null)
            {
                root = new RootNode("root", "root", null);
                // ProjectNode prj = new ProjectNode(prjId, prjName, root);
                ProjectNode prj = new ProjectNode(prjId, prjId, root);
                root.add(prj);
                tv.setInput(root);
            }
            else
            {
                // ProjectNode prj = new ProjectNode(prjId, prjName, root);
                ProjectNode prj = new ProjectNode(prjId, prjId, root);
                root.add(prj);
            }
            v.getTreeViewer().refresh();
        }
    }

    private void createProperties()
    {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("prjId", prjId);
        map.put("dbAddress", dbAddress);
        map.put("dbInstance", dbInstance);
        map.put("dbPort", dbPort);
        map.put("dbUser", dbUser);
        map.put("dbPwd", dbPwd);
        map.put("remoteAddress", remoteAddress);
        map.put("remoteUser", remoteUser);
        map.put("remotePwd", remotePwd);
        String properties = projectAbsPath + File.separator + prjId
                            + ".properties";
        DebugOut.println("properties path is :" + properties);
        PropertiesUtil.rewriteProperties1(properties, map);

    }

    private int showMessage(int style, String title, String message)
    {
        MessageBox box = new MessageBox(workbench.getActiveWorkbenchWindow().getShell(),
                                        style);
        box.setText(title);
        box.setMessage(message);
        return box.open();
    }
}
