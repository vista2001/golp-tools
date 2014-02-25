package dev.commands;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.generate.entryFunction.EntryFunction;
import dev.model.base.ResourceLeafNode;
import freemarker.template.TemplateException;

public class EntryFunctionPushHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException{
		List<ResourceLeafNode> obj = null;
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		// 获得选中的节点
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		obj = ((IStructuredSelection) selection).toList();
		int count=0;
		String information="";
		try {
			for (count = 0; count < obj.size(); count++) {

				// 新建生成交易入口函数类的实例并初始化，生成交易入口函数
				EntryFunction entry = new EntryFunction(obj.get(count));
				entry.generate();

				information+=obj.get(count).id+"\n";
			}
		} catch (TemplateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			MessageBox box=new MessageBox(Display.getCurrent()
			.getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
			if(count==obj.size()){
				box.setText("成功");
				information="生成入口函数操作成功"+information;
			}
			else{
				box.setText("失败");
				information="生成第"+count+"入口函数操作出现问题\n该交易没有绑定流程图";
			}
			box.setMessage(information);
			box.open(); ;
		} 

		return obj;
	}

}
