package dev.views;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;

public class QueryView extends ViewPart {
	public static final String ID="dev.views.queryView";
	private Text id;
	private Text name;

	public QueryView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Label prjId = new Label(composite, SWT.NONE);
		prjId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		prjId.setText("工程名称");
		
		Combo combo_1 = new Combo(composite, SWT.NONE);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		DbConnectImpl dbConnImpl=DbConnFactory.dbConnCreator();
		dbConnImpl.openConn();
		try {
			ResultSet rs=dbConnImpl.retrive("select * from project");
			ResultSet count=dbConnImpl.retrive("select count(*) from project");
			String[] name=null;
			if(count.next()){
				int length=count.getInt(1);
				name=new String[length];
			}
			for(int i=0;rs.next();i++){
				name[i]=rs.getString(2);
			}
			combo_1.setItems(name);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}finally{
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		Label resourceType = new Label(composite, SWT.NONE);
		resourceType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		resourceType.setText("资源类型");
		
		Combo combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String[] itemes={"数据项","动态库","返回码","服务","交易","原子交易","流程图"};
		combo.setItems(itemes);
		
		
		Label resourceId = new Label(composite, SWT.NONE);
		resourceId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		resourceId.setText("资源标识");
		
		id = new Text(composite, SWT.BORDER);
		id.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label resourceName = new Label(composite, SWT.NONE);
		resourceName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		resourceName.setText("资源名称");
		
		name = new Text(composite, SWT.BORDER);
		name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button queryBtn = new Button(composite, SWT.NONE);
		queryBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		queryBtn.setText("查询");
		new Label(composite, SWT.NONE);
		queryBtn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("start query");
				System.out.println(e.getSource());
				DbConnectImpl dbConnImpl=DbConnFactory.dbConnCreator();
				try {
					dbConnImpl.openConn();
					dbConnImpl.setPrepareSql("select * from aop where id=?");
					dbConnImpl.setPreparedString(1, "A_0001");
					ResultSet rs=dbConnImpl.retrivePrepared();
					while(rs.next()){
						System.out.println(rs.getString(1));
						MessageDialog.openInformation(
								e.display.getActiveShell(),
								e.display.getActiveShell().getText(),
								"查询结果是：A_001");
						
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
					try {
						dbConnImpl.closeConn();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
