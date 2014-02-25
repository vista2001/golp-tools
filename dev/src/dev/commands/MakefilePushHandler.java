/* �ļ�����       MakefilePushHandler.java
 * ������           ���ļ���������Makefile������ʵ��������makefile��ť��Handler��
 * �����ˣ�       zxh
 * ����ʱ�䣺   2013.11.27
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.generate.tps.TpsMaker;
import dev.model.base.ResourceLeafNode;
import freemarker.template.TemplateException;

/**
 * makefile���ɰ�ť��ʵ����
 * <p>
 * ������ѡ�������Ľ�����ں�����tps.mk,tps.LST
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
		// ���ѡ�еĽڵ�
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		obj = ((IStructuredSelection) selection).toList();
		int count=0;
		String information="";
		try {
			for (count = 0; count < obj.size(); count++) {

				// �½�����tps.mk��tps.LST���ʵ������ʼ��������tps.mk��tps.LST
				TpsMaker tps = new TpsMaker(obj.get(count));
				tps.generate();
				information+=obj.get(count).id+"\n";
			}
		} catch (TemplateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			MessageBox box=new MessageBox(Display.getCurrent()
			.getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
			if(count==obj.size()){
				box.setText("�ɹ�");
				information="����makefile�����ɹ�\n�������·�������makefile:\n"+information;
			}
			else{
				box.setText("ʧ��");
				information="���ɵ�"+count+"makefile������������\n�ɹ����ɣ�\n"
						+information+"\nʧ��"+(obj.size()-count)+"��";
			}
			box.setMessage(information);
			box.open(); ;
		} 

		return obj;
	}
}
