/* 文件名：       MakefilePushHandler.java
 * 描述：           该文件定义了类Makefile，该类实现了生成makefile按钮的Handler。
 * 创建人：       zxh
 * 创建时间：   2013.11.27
 */

package dev.commands;

import java.awt.Composite;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.generate.entryFunction.EntryFunction;
import dev.generate.tps.TpsMaker;
import dev.model.base.ResourceLeafNode;
import dev.model.base.TreeNode;
import dev.model.resource.ServerNodes;
import dev.views.NavView;
import dev.wizards.newAop.NewAopWizard;
import freemarker.template.TemplateException;

/**
 * makefile生成按钮的实现类
 * <p>
 * 生成所选服务程序的交易入口函数，tps.mk,tps.LST
 * 
 * @see ex
 * @author zxh
 * 
 */
public class MakefilePushHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		List<ResourceLeafNode> obj = null;
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		// 获得选中的节点
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		obj = ((IStructuredSelection) selection).toList();
		int count = 0;
		String information = "";
		try {
			for (count = 0; count < obj.size(); count++) {

				// 新建生成tps.mk和tps.LST类的实例并初始化，生成tps.mk和tps.LST
				TpsMaker tps = new TpsMaker(obj.get(count));
				tps.generate();
				information += obj.get(count).id + "\n";
			}
		} catch (TemplateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			MessageBox box = new MessageBox(Display.getCurrent()
					.getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
			if (count == obj.size()) {
				box.setText("成功");
				information = "生成makefile操作成功\n生成以下服务程序的makefile:\n"
						+ information;
			} else {
				box.setText("失败");
				information = "生成第" + count
						+ "makefile操作出现问题，请检查数据是否符合规定\n成功生成：\n" + information
						+ "\n失败" + (obj.size() - count) + "个";
			}
			box.setMessage(information);
			box.open();
			;
		}

		return obj;
	}
}
