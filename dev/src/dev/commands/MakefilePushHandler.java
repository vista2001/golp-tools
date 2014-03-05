/* 文件名：       MakefilePushHandler.java
 * 描述：           该文件定义了类Makefile，该类实现了生成makefile按钮的Handler。
 * 创建人：       zxh
 * 创建时间：   2013.11.27
 */

package dev.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.generate.tps.TpsMaker;
import dev.model.base.ResourceLeafNode;
import dev.util.DevLogger;
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
			DevLogger.printError(e);
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		} finally {
			if (count == obj.size()) {
				DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.OK, "成功","生成makefile操作成功\n生成以下服务程序的makefile:\n"
						+ information);
			} else {
				DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.OK, "失败","生成第" + count
						+ "makefile操作出现问题，请检查数据是否符合规定\n成功生成：\n" + information
						+ "\n失败" + (obj.size() - count) + "个");
			}
		}

		return obj;
	}
}
