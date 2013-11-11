package dev;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import dev.console.ConsoleFactory;
import dev.editors.server.UnCloseableEditorPresentationFactory;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
        configurer.setInitialSize(new Point(1024, 768));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowFastViewBars(true);
        configurer.setTitle("GOLP TOOL"); //$NON-NLS-1$
        ConsoleFactory cf = new ConsoleFactory();
		cf.openConsole();
        
		configurer.setPresentationFactory(new UnCloseableEditorPresentationFactory());
		
    }
    @Override  
    public void postWindowCreate() {  
        super.postWindowCreate();  
        Shell shell = getWindowConfigurer().getWindow().getShell();  
        Rectangle screenSize = Display.getDefault().getClientArea();  
        Rectangle frameSize = shell.getBounds();  
        shell.setLocation((screenSize.width - frameSize.width) / 2,  
                    (screenSize.height - frameSize.height) / 2);  
    } 
}
