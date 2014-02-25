/* 文件名：       Perspective.java
 * 修改人：       rxy
 * 修改时间：   2013.12.6
 * 修改内容：    1.修改视图夹（IFolderLayout）left所占的比例为0.25f。
 *         2.原本计划在createInitialLayout方法中，将
 *         控制台视图（org.eclipse.ui.console.ConsoleView）、
 *         属性表单视图（org.eclipse.ui.views.PropertySheet）、
 *         大纲视图（org.eclipse.ui.views.ContentOutline）
 *         添加到工具的默认透视图中，但后来采用了配置perspectiveExtensions
 *         扩展点的方式，因为在该方式下，可以方便的控制视图是否隐藏，是否最小化等。
 */

package dev;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		//addFastViews(layout);
		String editorArea=layout.getEditorArea();
		IFolderLayout left=layout.createFolder("left", IPageLayout.LEFT, 0.25f, editorArea);
		left.addView("dev.views.NavView");
//		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, editorArea);
//		bottom.addPlaceholder("org.eclipse.ui.console.ConsoleView");
//		bottom.addView("org.eclipse.ui.console.ConsoleView");
//		bottom.addView("org.eclipse.ui.views.PropertySheet");

//		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.7f, editorArea);
//		right.addView("org.eclipse.ui.views.ContentOutline");
		

		
//		IPlaceholderFolderLayout right = layout.createPlaceholderFolder("right", IPageLayout.RIGHT, 0.7f, editorArea);
//		right.addPlaceholder("org.eclipse.ui.views.ContentOutline");
		//layout.addView("org.eclipse.ui.console.ConsoleView", IPageLayout.BOTTOM, 0.67f, IPageLayout.ID_EDITOR_AREA);
		//layout.addView("org.eclipse.pde.runtime.LogView", IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_EDITOR_AREA);
		/*ConsoleFactory cf = new ConsoleFactory();
		  cf.openConsole();*/
		//layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.67f, IPageLayout.ID_EDITOR_AREA);
	}
	private void addFastViews(IPageLayout layout) {
		//layout.addFastView("org.eclipse.ui.console.ConsoleView");
	}
	
}
