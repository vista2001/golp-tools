/* 文件名：       ServerSpeclibNameDialog.java
 * 修改人：       rxy
 * 修改时间：   2013.11.29
 * 修改内容：    1.修改对话框布局；
 *         2.用DebugOut.println方法替换System.out.println方法；
 *         3.为输入文本框增加监听器，使其可以响应键盘回车事件。
 */

package dev.editors.server;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;

public class ServerSpeclibNameDialog extends TitleAreaDialog
{
	List serverSpeclibNameList;
	java.util.List<String> listForReturn = null;
	java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();
	private Text serverSpeclibNameText;
	private Button delButton;
	private Button upButton;
	private Button downButton;
	private java.util.List<String> already = new ArrayList<String>();

	public java.util.List<String> getListForReturn() {
		return listForReturn;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ServerSpeclibNameDialog(Shell parentShell, Object obj, String string) {
		super(parentShell);
		String[] tmp = string.split("\\|");
		for(String s : tmp)
		{
			already.add(s);
		}
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		new Label(area, SWT.NONE);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.setMessage("请选择服务程序个性依赖库名称");
		
		serverSpeclibNameText = new Text(container, SWT.BORDER);
		serverSpeclibNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button addButton = new Button(container, SWT.NONE);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("添加");
		serverSpeclibNameList = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		serverSpeclibNameList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		for(String s : already)
		{
			if(s != "")
			{
				serverSpeclibNameList.add(s);
			}
		}
		
		delButton = new Button(container, SWT.NONE);
		GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_delButton.widthHint = 60;
		delButton.setLayoutData(gd_delButton);
		delButton.setText("移除");
		
		upButton = new Button(container, SWT.NONE);
		GridData gd_upButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_upButton.widthHint = 60;
		upButton.setLayoutData(gd_upButton);
		upButton.setText("上移");
		
		downButton = new Button(container, SWT.NONE);
		GridData gd_downButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_downButton.widthHint = 60;
		downButton.setLayoutData(gd_downButton);
		downButton.setText("下移");

		serverSpeclibNameText.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if((serverSpeclibNameText.getText().trim().isEmpty() == false)
                    && (e.keyCode == SWT.CR))
                {
                    serverSpeclibNameList.add(serverSpeclibNameText.getText());
                    serverSpeclibNameText.setText("");
                }
            }
        });
		
		serverSpeclibNameList.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (serverSpeclibNameList.getSelectionIndices().length > 0)
				{
					delButton.setEnabled(true);
					if (serverSpeclibNameList.getSelectionIndices().length == 1)
					{
						upButton.setEnabled(true);
						downButton.setEnabled(true);
					}
					else
					{
						upButton.setEnabled(false);
						downButton.setEnabled(false);
					}
				}
			}
		});
		
		addButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (!serverSpeclibNameText.getText().isEmpty())
				{
					
					serverSpeclibNameList.add(serverSpeclibNameText.getText());
					serverSpeclibNameText.setText("");
				}
			}
		});
		
		delButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int[] indices = serverSpeclibNameList.getSelectionIndices();
				serverSpeclibNameList.remove(indices);
				delButton.setEnabled(false);
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}
		});
		
		upButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int index = serverSpeclibNameList.getSelectionIndex();
				if(index > 0)
				{
					String tmp = serverSpeclibNameList.getItem(index);
					serverSpeclibNameList.setItem(index, serverSpeclibNameList.getItem(index-1));
					serverSpeclibNameList.setItem(index-1, tmp);
					serverSpeclibNameList.setSelection(index-1);
				}
			}
		});
		
		downButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int index = serverSpeclibNameList.getSelectionIndex();
				if(index < serverSpeclibNameList.getItemCount()-1)
				{
					String tmp = serverSpeclibNameList.getItem(index);
					serverSpeclibNameList.setItem(index, serverSpeclibNameList.getItem(index+1));
					serverSpeclibNameList.setItem(index+1, tmp);
					serverSpeclibNameList.setSelection(index+1);
				}
			}
		});

		return area;

	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		//DebugOut.println(listInDialog.isDisposed());
		String[] serverSpeclibNames = serverSpeclibNameList.getItems();
		listForReturn = new ArrayList<String>();
		for (int i = 0; i < serverSpeclibNames.length; i++) {
			listForReturn.add(serverSpeclibNames[i]);
		}
		notifyInformDialogEvent();
		super.okPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 400);
	}
	


	public void addInformDialogListener(InformDialogListener dl) {
		listeners.add(dl);
		//DebugOut.println("demo add");
	}

	public void notifyInformDialogEvent() {
		for (InformDialogListener idl : listeners) {
			idl.handleEvent(new InformDialogEvent(this));
			//DebugOut.println(idl.toString() + " is informed");
		}
	}
}
