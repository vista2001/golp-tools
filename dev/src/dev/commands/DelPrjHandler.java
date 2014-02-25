package dev.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import dev.editors.IGetUpProject;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.views.NavView;

public class DelPrjHandler extends AbstractHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();

        NavView navView = (NavView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                NavView.ID);
        TreeViewer tv = navView.getTreeViewer();
        StructuredSelection s = (StructuredSelection) tv.getSelection();
        Object[] selections = s.toArray();
        // Object o = s.getFirstElement();
        for (Object o : selections)
        {
            if (o instanceof ProjectNode)
            {
                String delId = ((ProjectNode) o).getId();
                IProject project = root.getProject(delId);
                System.out.println(((ProjectNode) o).getId());
                System.out.println(project.getName());
                if (project.exists())
                {
                    try
                    {
                        RootNode rootNode = (RootNode) tv.getInput();
                        List<TreeNode> l = rootNode.getChildren();
                        boolean flag = false;
                        int point = 0;
                        for (int i = 0; i < l.size(); i++)
                        {
                            if (l.get(i).getId().equals(delId))
                            {
                                flag = true;
                                point = i;
                                break;
                            }
                        }
                        if (flag)
                        {
                            l.remove(point);
                            tv.refresh();
                        }

                        IEditorReference[] editorReference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
                        for (IEditorReference editor : editorReference)
                        {
                            IEditorPart editorPart = editor.getEditor(false);
                            if (editorPart != null)
                            {
                                if (editorPart instanceof IGetUpProject)
                                {
                                    if (((IGetUpProject) editorPart).getUpProject().equals(
                                            delId))
                                    {

                                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(
                                                editorPart, false);

                                    }
                                }
                            }

                        }
                        // 放到删除节点之后？？？？
                        project.delete(true, null);

                    }
                    catch (CoreException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }
        return null;
    }
}
