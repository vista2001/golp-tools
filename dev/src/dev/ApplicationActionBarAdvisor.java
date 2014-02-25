/* 文件名：       ApplicationActionBarAdvisor.java
 * 修改人：       rxy
 * 修改时间：   2013.12.9
 * 修改内容：    为测试用，增加了工具栏上的“关于”按钮。
 */

package dev;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction aboutAction;
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(coolBar.getStyle());
//        toolbar.add(aboutAction);
        coolBar.add(toolbar);
    }
    
    
}
