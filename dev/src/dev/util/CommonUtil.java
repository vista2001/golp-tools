/* �ļ�����       CommonUtil.java
 * ������           ���ļ���������CommonUtil�������ṩ��һЩ���õĹ��߷�����
 * �����ˣ�       rxy
 * ����ʱ�䣺   2014.1.2
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
 * ��CommonUtil�ж�����һЩ���õĹ��߷�����
 * 
 * @author rxy
 */
public class CommonUtil
{

    /**
     * ��ָ������ID��Ӧ�ı����ļ�*.properties���ص�һ��PreferenceStore�����У������������
     * 
     * @param projectId
     *            ָ�����̵�ID
     * @return ������*.properties�ļ���PreferenceStore����
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
