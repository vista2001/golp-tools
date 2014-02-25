/* �ļ�����       LoadProjectWizard.java
 * ������           ���ļ���������LoadProjectWizard���������ڴ��û����������ݿ��ж�ȡ���̼�¼��
 *         �����ù������뵱ǰ��������
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.26
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
 * �������ڴ��û����������ݿ��ж�ȡ���̼�¼�������ù������뵱ǰ��������
 * 
 * @author rxy
 */
public class LoadProjectWizard extends Wizard implements INewWizard
{
    private ISelection selection;

    /**
     * ��������������ҳ��ΪLoadProjectWizardPage0��Ķ���
     */
    private LoadProjectWizardPage0 page0;

    private IWorkbench workbench;
    private String projectAbsPath = "";

    /**
     * ���ݿ��ַ��
     */
    private String dbAddress;

    /**
     * ���ݿ�˿ڡ�
     */
    private String dbPort;

    /**
     * ���ݿ�ʵ����
     */
    private String dbInstance;

    /**
     * ���ݿ��û�����
     */
    private String dbUser;

    /**
     * ���ݿ����롣
     */
    private String dbPwd;

    /**
     * ���̵�ID��
     */
    private String prjId;

    /**
     * ��������ַ��
     */
    private String remoteAddress;

    /**
     * �������û���
     */
    private String remoteUser;

    /**
     * ���������
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

        // TODO ��ǰ������У�ֻ�ܱ�֤���û���ȷ���������ݿ�������Ϣ������ǰ���ݿ���û��project����иñ�
        // �ñ����޼�¼���Ż���û�������ȷ��ʾ���������û������Ĵ�������ݿ�������Ϣ����ǰ��δ����

        // ���ڸ��������ݿ��У�δ�ҵ����̼�¼
        if (prjId == null)
        {
            showMessage(SWT.ICON_INFORMATION | SWT.YES, "��ʾ", "���̼�¼������");
            return false;
        }

        // ���ҵ��Ĺ����ڵ�ǰ���������Ѵ���
        if (isProjectExist() == true)
        {
            showMessage(SWT.ICON_INFORMATION | SWT.YES, "��ʾ",
                    "�����ݿ��еĹ����ڵ�ǰ���������Ѵ���");
            return false;
        }

        // �������������˴���˵�����ҵ���һ�����̼�¼���Ҹù����ڵ�ǰ�������в����ڡ���ʱѯ���û��Ƿ�����ù��̡�
        int result = showMessage(SWT.ICON_INFORMATION | SWT.YES | SWT.NO, "��ʾ",
                "�ҵ����¹��̣�" + System.getProperty("line.separator") + prjId
                        + System.getProperty("line.separator") + "�Ƿ����룿");

        // ���û�ѡ������
        if (result == SWT.YES)
        {
            // �������̹������ļ�
            createProject(prjId);

            // �������̹���Ŀ¼
            createDirectorys();

            // ֪ͨ������ͼ��༭����
            informParts(prjId);

            // �������������ļ�
            createProperties();

            // ��ʾ�û�����ɹ�
            showMessage(SWT.ICON_INFORMATION | SWT.YES, "��ʾ", "����ɹ�");

            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * �÷��������ж����ҵ��Ĺ����Ƿ��Ѵ����ڵ�ǰ�Ĺ������С�
     * 
     * @return ���Ѵ��ڣ�����true�����򷵻�false��
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
     * ��ȡ���½���������������ݡ�
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
     * �÷���ʹ���û��ڵ�ǰҳ������������ݿ�������Ϣ�������ݿ⣬�������ݿ����й��̼�¼��
     * ���ҵ��Ĺ��̵�ID������Ա����prjId�����򣬽�prjId��Ϊnull��
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
                // LogUtil.getInstance().logError("��������ʧ��!", e);
                // LogUtil.getInstance().logError("��������ʧ��!", e);
                // LogUtil.getInstance().logError("��������ʧ��!", e);
                // LogUtil.getInstance().logError("��������ʧ��!", e);
                e.printStackTrace();
            }
        else
            try
            {
                project.open(null);
            }
            catch (CoreException e)
            {
                // LogUtil.getInstance().logError("��������ʧ��!", e);
                e.printStackTrace();
            }
    }

    /** ������ع���Ŀ¼ */
    private void createDirectorys()
    {
        // ��ȡ·��
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

    /** ֪ͨ������ͼ��༭�� */
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
