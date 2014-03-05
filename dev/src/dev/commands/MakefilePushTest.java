/* 文件名：       MakefilePushTest.java
 * 描述：           该文件定义了类 MakefilePushTest，该类实现了上下文菜单的判断规则。
 * 创建人：       zxh
 * 创建时间：   2013.12.4
 */
package dev.commands;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import dev.model.base.ResourceLeafNode;
import dev.views.NavView;

/**
 * makefile生成按钮在上下文菜单中是否可见的测试类
 * <p>
 * 每一次在导航视图鼠标右键出现上下文菜单都要通过测试类判断makefile生成按钮是否可见
 * 
 * @author zxh
 * @see FmlIdHandler
 */
public class MakefilePushTest extends PropertyTester {
	// 必须存在的空的公共构造函数

	public static IStructuredSelection s;

	public MakefilePushTest() {

	}

	/**
	 * 用于判断按钮在上下文菜单中是否出现<br>
	 * 只有在服务程序的子节点才出现makefile生成按钮
	 * 
	 * @param receiver
	 *            接收的测试属性，对应propertytesters扩展点中的properties
	 * @param property
	 *            接收的测试属性的名称 ,根据名称来判断是properties中的哪个变量
	 * @param args
	 *            附加参数
	 * @param expectedValue
	 *            期待测试属性出现的值
	 * @return 如果是服务程序的子节点，返回true，否则返回false
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		IStructuredSelection temselection;
		ISelection selection;
		// 获得导航视图中选中的控件
		Object obj = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		selection = ((NavView) obj).getTreeViewer().getSelection();
		if (selection instanceof IStructuredSelection) {
			temselection = (IStructuredSelection) selection;
			// 选中的是不是叶子节点
			for (Object one : temselection.toArray()) {
				if (one instanceof ResourceLeafNode) {
					ResourceLeafNode node = (ResourceLeafNode) one;
					// 判断选中节点的父节点是不是服务程序
					if (!node.parent.name.equals("服务程序")) {
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
