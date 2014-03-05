/* 文件名：       DebugOut.java
 * 描述：           该文件定义了类DebugOut，该类提供了方法println来替代System.out.println，
 *         并提供了可以控制是否打印输出的开关（常量DEBUG）。当DEBUG为true时打印输出，为false
 *         则不打印。
 * 创建人：       rxy
 * 创建时间：   2013.12.11
 */

package dev.util;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import dev.Activator;
import dev.golpDialogs.ExceptionDetailsDialog;

/**
 * 类DebugOut，提供了带有控制开关的方法println，用来替代System.out.println方法。
 * 
 * @author rxy
 */
public class DevLogger {
	private static final boolean DEBUG = true;

	public static void printDebugMsg(Object str) {
		if (DEBUG) {
			System.out.println("DEBUG:" + str);
		}
	}

	public static void printError(Exception e) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		new ExceptionDetailsDialog(shell, "ErrorMSG", null, // image (defaults
															// to error image)
				e.getMessage() + ExceptionDetailsDialog.class.getName()
						+ " dialog.", e, Activator.getDefault().getBundle())
				.open();
	}

	public static void printLog() {

	}

/*	public static int showMessage(ExecutionEvent event, int style, String title,
			String message) {
		MessageBox box = new MessageBox(HandlerUtil.getActiveShell(event),
				style);
		box.setText(title);
		box.setMessage(message);
		return box.open();
	}*/
	
	public static int showMessage(int style, String title, String message) {
		MessageBox box = new MessageBox(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), style);
		box.setMessage(message);
		box.setText(title);
		return box.open();
	}
	public static void main(String[] args) {

	}
}
