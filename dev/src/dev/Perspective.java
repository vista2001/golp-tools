/* �ļ�����       Perspective.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.6
 * �޸����ݣ�    1.�޸���ͼ�У�IFolderLayout��left��ռ�ı���Ϊ0.25f��
 *         2.ԭ���ƻ���createInitialLayout�����У���
 *         ����̨��ͼ��org.eclipse.ui.console.ConsoleView����
 *         ���Ա���ͼ��org.eclipse.ui.views.PropertySheet����
 *         �����ͼ��org.eclipse.ui.views.ContentOutline��
 *         ��ӵ����ߵ�Ĭ��͸��ͼ�У�����������������perspectiveExtensions
 *         ��չ��ķ�ʽ����Ϊ�ڸ÷�ʽ�£����Է���Ŀ�����ͼ�Ƿ����أ��Ƿ���С���ȡ�
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
