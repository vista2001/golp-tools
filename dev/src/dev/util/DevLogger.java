/* �ļ�����       DebugOut.java
 * ������           ���ļ���������DebugOut�������ṩ�˷���println�����System.out.println��
 *         ���ṩ�˿��Կ����Ƿ��ӡ����Ŀ��أ�����DEBUG������DEBUGΪtrueʱ��ӡ�����Ϊfalse
 *         �򲻴�ӡ��
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.11
 */

package dev.util;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import dev.Activator;
import dev.golpDialogs.ExceptionDetailsDialog;

/**
 * ��DebugOut���ṩ�˴��п��ƿ��صķ���println���������System.out.println������
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
