/* 文件名：       CommonUtil.java
 * 描述：           该文件定义了类CommonUtil，该类提供了一些常用的工具方法。
 * 创建人：       rxy
 * 创建时间：   2014.1.2
 */

package dev.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferenceStore;

/**
 * 类CommonUtil中定义了一些常用的工具方法。
 * 
 * @author rxy
 */
public class CommonUtil
{

    /**
     * 将指定工程ID对应的本地文件*.properties加载到一个PreferenceStore对象中，返回这个对象
     * 
     * @param projectId
     *            指定工程的ID
     * @return 加载了*.properties文件的PreferenceStore对象
     */
    public static PreferenceStore initPs(String projectId)
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IProject project = root.getProject(projectId);
        String propertyPath = project.getLocationURI().toString().substring(6)
                              + File.separator + projectId + ".properties";
        PreferenceStore ps = new PreferenceStore(propertyPath);
        try
        {
            ps.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return ps;
    }

}
