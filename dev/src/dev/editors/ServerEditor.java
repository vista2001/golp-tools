package dev.editors;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;

public class ServerEditor extends EditorPart {

	public static final String ID="dev.editors.serverEditor";
	private boolean dirty=false;
	private Text AopIdText;
	private Text AopNameText;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public ServerEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.horizontalSpacing = 0;
		gl_parent.verticalSpacing = 0;
		parent.setLayout(gl_parent);
		
		Group group = new Group(parent, SWT.NONE);
		group.setText("原子交易查询");
		GridData gd=new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalSpan=3;
		group.setLayoutData(gd);
		
		GridLayout gl_group = new GridLayout(5, false);
		gl_group.horizontalSpacing = 0;
		group.setLayout(gl_group);
		Label AopIdLabel = new Label(group, SWT.NONE);
		AopIdLabel.setText("原子交易标识");
		AopIdText = new Text(group, SWT.BORDER);
		Label AopNameLabel = new Label(group, SWT.NONE);
		AopNameLabel.setText("原子交易名称");
		AopNameText = new Text(group, SWT.BORDER);
		
		Button quaryAopBtn = new Button(group, SWT.NONE);
		quaryAopBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		quaryAopBtn.setText("查询");
		
		Group group_1 = new Group(parent, SWT.NONE);
		group_1.setLayout(new GridLayout(6, true));
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		group_1.setText("原子交易");
		
		Label lblNewLabel = new Label(group_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("New Label");
		
		text = new Text(group_1, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(group_1, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("New Label");
		
		text_1 = new Text(group_1, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_2 = new Label(group_1, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("New Label");
		
		text_2 = new Text(group_1, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_4 = new Label(group_1, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("New Label");
		
		text_4 = new Text(group_1, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_5 = new Label(group_1, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("New Label");
		
		text_5 = new Text(group_1, SWT.BORDER);
		text_5.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_3 = new Label(group_1, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("New Label");
		
		text_3 = new Text(group_1, SWT.BORDER);
		text_3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
