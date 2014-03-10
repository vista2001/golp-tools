package dev.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.db.DbConnectImpl;
import dev.golpDialogs.SetDataItemStateDialog;
import dev.golpDialogs.TestDialog;
import dev.model.base.ResourceLeafNode;
import dev.model.base.ResourceNode;
import dev.model.resource.ProjectNode;

public class SetDataItemStateHandler extends AbstractHandler {
	String prjId;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object obj = structuredSelection.getFirstElement();

		if (obj instanceof ProjectNode) {
			prjId = ((ProjectNode) obj).getId();
		} else if (obj instanceof ResourceNode) {
			prjId = ((ResourceNode) obj).getRootProject().getId();
		} else if (obj instanceof ResourceLeafNode) {
			prjId = ((ResourceLeafNode) obj).getRootProject().getId();
		}

		SetDataItemStateDialog setDataItemStateDialog = new SetDataItemStateDialog(
				HandlerUtil.getActiveShell(event), prjId);
		setDataItemStateDialog.open();

		//test testdialog
		
		/*TestDialog d=new TestDialog(HandlerUtil.getActiveShell(event), new LabelProvider());
		DbConnectImpl dbConnectImpl=new DbConnectImpl();
		String[] tmp=null;
		try {
			dbConnectImpl.openConn();
			ResultSet rs=dbConnectImpl.retrive("select DATAITEMID,DATANAME from dataitem order by DATAITEMID");
			List<String> contents=new ArrayList<String>();
			while(rs.next()){
				contents.add(rs.getInt(1)+"-"+rs.getString(2));
			}
			tmp=new String[contents.size()];
			contents.toArray(tmp);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		d.setElements(tmp);
		d.open();*/
		
		return null;
	}

}
