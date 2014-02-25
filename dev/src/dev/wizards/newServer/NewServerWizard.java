/* 文件名：       NewServerWizard.java
 * 修改人：       rxy
 * 修改时间：   2013.11.27
 * 修改内容：    1.将该源文件中出现的所有“SERVERSPECAOPDLLS”替换为
 *         “SERVERSPECINCLUDE”；
 *         2.将服务程序个性依赖宏定义、服务程序额外依赖目标文件加入到此向导中 ；
 *         3.用DebugOut.println方法替换System.out.println方法。；
 *         4.在写数据库的方法中，除去UPPROJECT这个字段。
 */

package dev.wizards.newServer;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.pojo.TServer;
import dev.db.service.EditorsServerServiceImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.model.resource.ServerNodes;
import dev.util.DebugOut;
import dev.views.NavView;

public class NewServerWizard extends Wizard implements INewWizard
{
    private ISelection selection;
    private IWorkbench workbench;

    private NewServerWizardPage0 page0;
    private NewServerWizardPage1 page1;
    private NewServerWizardPage2 page2;
    private NewServerWizardPage3 page3;
    private NewServerWizardPage4 page4;
    private NewServerWizardPage5 page5;
    private NewServerWizardPage6 page6;
    private NewServerWizardPage7 page7;

    private String upProject = "";
    private TServer server;

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        this.selection = selection;
        this.workbench = workbench;
        server = new TServer();
    }

    @Override
    public void addPages()
    {
        page0 = new NewServerWizardPage0(selection);
        page1 = new NewServerWizardPage1(selection);
        page2 = new NewServerWizardPage2(selection);
        page3 = new NewServerWizardPage3(selection);
        page4 = new NewServerWizardPage4(selection);
        page5 = new NewServerWizardPage5(selection);
        page6 = new NewServerWizardPage6(selection);
        page7 = new NewServerWizardPage7(selection);

        addPage(page0);
        addPage(page1);
        addPage(page2);
        addPage(page3);
        addPage(page6);
        addPage(page7);
        addPage(page4);
        addPage(page5);

    }

    @Override
    public boolean performFinish()
    {
        getData();
        try
        {
            doFinish(server, upProject);
            updateNavView();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
        return true;
    }

    // 获取该新建向导中所设置的数据
    private void getData()
    {
        upProject = page0.getUpProjectCombo().getText();
        String serverLevel = page0.getServerLevelCombo().getText();
        server.setServerLevel2(serverLevel);
        String svrId = page0.getSvrIdText().getText();
        server.setServerId(svrId);
        String svrName = page0.getSvrNameText().getText();
        server.setServerName(svrName);
        String svrDesc = page0.getSvrDescText().getText();
        server.setServerDesc(svrDesc);
        String serverSpecLibPath = "";
        String serverSpecLibName = "";
        String serverSpecLib = "";
        if (page1.getServerSpeclibPathList().getItems().length > 0)
        {
            for (String s : page1.getServerSpeclibPathList().getItems())
            {
                if (serverSpecLibPath.length() == 0)
                    serverSpecLibPath = serverSpecLibPath + s;
                else
                    serverSpecLibPath = serverSpecLibPath + "|" + s;
            }
            for (String s : page2.getServerSpeclibNameList().getItems())
            {
                if (serverSpecLibName.length() == 0)
                    serverSpecLibName = serverSpecLibName + s;
                else
                    serverSpecLibName = serverSpecLibName + "|" + s;
            }
            serverSpecLib = "[" + serverSpecLibPath + "]" + "["
                    + serverSpecLibName + "]";
        }
        server.setServerSpecLib(serverSpecLib);
        String serverSpecIncludePath = "";
        if (page3.getServerSpecIncludePathList().getItems().length > 0)
        {
            for (String s : page3.getServerSpecIncludePathList().getItems())
            {
                if (serverSpecIncludePath.length() == 0)
                    serverSpecIncludePath = serverSpecIncludePath + s;
                else
                    serverSpecIncludePath = serverSpecIncludePath + "|" + s;
            }

        }
        server.setServerSpecInclude(serverSpecIncludePath);
        String callbackSource = "";
        if (page4.getCallBackSourceList().getItems().length > 0)
        {
            for (String s : page4.getCallBackSourceList().getItems())
            {
                if (callbackSource.length() == 0)
                    callbackSource = callbackSource + s;
                else
                    callbackSource = callbackSource + "|" + s;
            }

        }
        server.setCallbackSource(callbackSource);
        String othFunSource = "";
        if (page5.getOthFunSourceList().getItems().length > 0)
        {
            for (String s : page5.getOthFunSourceList().getItems())
            {
                if (othFunSource.length() == 0)
                    othFunSource = othFunSource + s;
                else
                    othFunSource = othFunSource + "|" + s;
            }

        }
        server.setOthFunSource(othFunSource);
        String serverSpecMarco = "";
        if (page6.getServerSpecMarcoList().getItems().length > 0)
        {
            for (String s : page6.getServerSpecMarcoList().getItems())
            {
                if (serverSpecMarco.length() == 0)
                    serverSpecMarco = serverSpecMarco + s;
                else
                    serverSpecMarco = serverSpecMarco + "|" + s;
            }

        }
        server.setServerSpecMarco(serverSpecMarco);
        String serverSpecObj = "";
        if (page7.getServerSpecObjList().getItems().length > 0)
        {
            for (String s : page7.getServerSpecObjList().getItems())
            {
                if (serverSpecObj.length() == 0)
                    serverSpecObj = serverSpecObj + s;
                else
                    serverSpecObj = serverSpecObj + "|" + s;
            }

        }
        server.setServerSpecObj(serverSpecObj);
    }

    // 将从新建向导中得到的数据写入数据库
    private void doFinish(TServer server, String prjId) throws SQLException
    {
        EditorsServerServiceImpl editorsServerServiceImpl = new EditorsServerServiceImpl();
        editorsServerServiceImpl.insertServer(server, prjId);
    }

    // 用从新建向导中获得的数据更新导航视图
    private void updateNavView()
    {
        IViewPart view = this.workbench.getActiveWorkbenchWindow()
                .getActivePage().findView(NavView.ID);
        if (view != null)
        {
            NavView v = (NavView) view;
            TreeViewer tv = v.getTreeViewer();
            RootNode root = (RootNode) tv.getInput();

            int index;
            for (index = 0; index < root.getChildren().size(); index++)
            {
                if (root.getChildren().get(index).getName().equals(upProject))
                    break;
            }
            ProjectNode projectNode = (ProjectNode) root.getChildren().get(
                    index);
            DebugOut.println(projectNode.getName());

            List<TreeNode> list = projectNode.getChildren();
            int i;
            for (i = 0; i < list.size(); i++)
            {
                if (list.get(i).getName().equals("服务程序"))
                    break;
            }
            DebugOut.println(list.get(i).getName());
            ResourceLeafNode resourceLeafNode = new ResourceLeafNode(
                    server.getServerName(), server.getServerId(), list.get(i));
            ((ServerNodes) list.get(i)).add(resourceLeafNode);
            tv.refresh();
        }
    }

    @Override
    public boolean canFinish()
    {
        if (page0.canFlipToNextPage())
        {
            if (page1.getServerSpeclibPathList().getItemCount() > 0)
            {
                if (page2.canFlipToNextPage())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }

        }
        return false;
    }

}
