/* �ļ�����       MakefilePushTest.java
 * ������           ���ļ��������� MakefilePushTest������ʵ���������Ĳ˵����жϹ���
 * �����ˣ�       zxh
 * ����ʱ�䣺   2013.12.4
 */
package dev.commands;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import dev.model.base.ResourceLeafNode;
import dev.views.NavView;

/**
 * makefile���ɰ�ť�������Ĳ˵����Ƿ�ɼ��Ĳ�����
 * <p>
 * ÿһ���ڵ�����ͼ����Ҽ����������Ĳ˵���Ҫͨ���������ж�makefile���ɰ�ť�Ƿ�ɼ�
 * 
 * @author zxh
 * @see FmlIdHandler
 */
public class MakefilePushTest extends PropertyTester {
	// ������ڵĿյĹ������캯��

	public static IStructuredSelection s;

	public MakefilePushTest() {

	}

	/**
	 * �����жϰ�ť�������Ĳ˵����Ƿ����<br>
	 * ֻ���ڷ��������ӽڵ�ų���makefile���ɰ�ť
	 * 
	 * @param receiver
	 *            ���յĲ������ԣ���Ӧpropertytesters��չ���е�properties
	 * @param property
	 *            ���յĲ������Ե����� ,�����������ж���properties�е��ĸ�����
	 * @param args
	 *            ���Ӳ���
	 * @param expectedValue
	 *            �ڴ��������Գ��ֵ�ֵ
	 * @return ����Ƿ��������ӽڵ㣬����true�����򷵻�false
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		IStructuredSelection temselection;
		ISelection selection;
		// ��õ�����ͼ��ѡ�еĿؼ�
		Object obj = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		selection = ((NavView) obj).getTreeViewer().getSelection();
		if (selection instanceof IStructuredSelection) {
			temselection = (IStructuredSelection) selection;
			// ѡ�е��ǲ���Ҷ�ӽڵ�
			for (Object one : temselection.toArray()) {
				if (one instanceof ResourceLeafNode) {
					ResourceLeafNode node = (ResourceLeafNode) one;
					// �ж�ѡ�нڵ�ĸ��ڵ��ǲ��Ƿ������
					if (!node.parent.name.equals("�������")) {
						return false;
					}
				} else
					return false;
			}
			return true;
		}

		return false;
	}

}
