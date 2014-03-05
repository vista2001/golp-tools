/* �ļ�����       OutputDataItemDialog.java
 * ������           ���ļ���������OutputDataItemDialog������ʵ�������½��������У�
 *         ���û�ѡ�����������Ĺ��ܡ����������֮�������ߣ�|���ָ�,���������ĸ�ʽ���磺DataItemID@a@b��
 *         �����룬��aΪ1������aΪ0���������ݴ����������и��ƣ���bΪ1������bΪ0��
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.4
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.23
 * �޸����ݣ�   �����ı���������ʾ���������ϸ��Ϣ�� 
 */

package dev.golpDialogs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
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

import dev.db.pojo.TDataItem;
import dev.db.service.CommonDialogServiceImpl;
import dev.util.DevLogger;

/**
 * SetDataItemStateDialog�࣬����ʵ�������趨�������״̬��handler�У����û�ѡ��������״̬�Ĺ��ܡ�
 * 
 * @author ly
 */
public class SetDataItemStateDialog extends TitleAreaDialog {
	/**
	 * ������ʾ������������б�org.eclipse.swt.widgets.List���ͣ���֧�ֶ�ѡ��
	 */
	private List allDataItemList;

	/**
	 * ������ʾ�����������б�org.eclipse.swt.widgets.List���ͣ���֧�ֶ�ѡ��
	 */
	private List outputDataItemList;

	/**
	 * ��Ӱ�ť������ð�ť���Ὣ�����������б���ѡ�е�����ӵ�����������б�
	 */
	private Button addButton;
	/**
	 * ɾ����ť������ð�ť���Ὣ�����������б���ѡ�е�����ӵ������������б�
	 */
	private Button delButton;

	/**
	 * ��������Id
	 */
	private String prjId;

	/**
	 * ���������ڴ洢ÿһ�����������ϸ��Ϣ
	 */
	// private String[] dataItemInfo;
	private Map<Integer, String> dataItemInfoMap = new HashMap<Integer, String>();

	/**
	 * ������ʾ��������ϸ��Ϣ���ı���
	 */
	private Text dataItemInfoText;

	/**
	 * ���캯����
	 * 
	 * @param parentShell
	 * @param obj
	 */

	public SetDataItemStateDialog(Shell parentShell, String prjId) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.PRIMARY_MODAL);
		setHelpAvailable(false);
		this.prjId = prjId;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("���������");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		Label allDataItemLabel = new Label(container, SWT.NONE);
		allDataItemLabel.setText("���������");

		new Label(container, SWT.NONE);

		Label outputDataItemLabel = new Label(container, SWT.NONE);
		outputDataItemLabel.setText("��������");

		allDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		allDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 8));
		allDataItemList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// ֻ�е�allDataItemList������Ŀ��ѡ��ʱ����Ӱ�ť��addButton���ſ��á�
				if (allDataItemList.getSelectionCount() > 0) {
					addButton.setEnabled(true);
					if (allDataItemList.getSelectionCount() == 1) {
						String[] objs = allDataItemList.getSelection();
						dataItemInfoText.setText(dataItemInfoMap.get(Integer
								.parseInt(objs[0])));
					} else {
						dataItemInfoText.setText("");
					}
					if (outputDataItemList.getSelectionCount() > 0) {
						outputDataItemList.deselect((outputDataItemList
								.getSelectionIndices()));
					}
				}
			}
		});
		new Label(container, SWT.NONE);

		outputDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		outputDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 10));
		outputDataItemList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ֻ�е�allDataItemList������Ŀ��ѡ��ʱ����Ӱ�ť��addButton���ſ��á�
				if (outputDataItemList.getSelectionCount() > 0) {
					delButton.setEnabled(true);
					if (outputDataItemList.getSelectionCount() == 1) {
						String[] objs = outputDataItemList.getSelection();
						dataItemInfoText.setText(dataItemInfoMap.get(Integer
								.parseInt(objs[0])));
					} else {
						dataItemInfoText.setText("");
					}
					if (allDataItemList.getSelectionCount() > 0) {
						allDataItemList.deselect((allDataItemList
								.getSelectionIndices()));
					}

				}
			}
		});
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		addButton = new Button(container, SWT.NONE);
		addButton.setEnabled(false);
		GridData gd_addButton = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("���>>");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (allDataItemList.getSelectionCount() > 0) {

					for (String s : allDataItemList.getSelection()) {
						allDataItemList.remove(s);
						java.util.List<Integer> temp = new ArrayList<Integer>();
						for (String string : outputDataItemList.getItems()) {
							temp.add(Integer.parseInt(string));
						}
						temp.add(Integer.parseInt(s));
						Integer[] tempIntegers = new Integer[temp.size()];
						Arrays.sort(temp.toArray(tempIntegers));
						outputDataItemList.removeAll();
						for (int a : tempIntegers) {
							outputDataItemList.add(a + "");
						}

					}

				}
			}
		});

		delButton = new Button(container, SWT.NONE);
		delButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		delButton.setText("ɾ��<<");
		delButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (outputDataItemList.getSelectionCount() > 0) {

					for (String s : outputDataItemList.getSelection()) {
						outputDataItemList.remove(s);
						java.util.List<Integer> temp = new ArrayList<Integer>();
						for (String string : allDataItemList.getItems()) {
							temp.add(Integer.parseInt(string));
						}
						temp.add(Integer.parseInt(s));
						Integer[] tempIntegers = new Integer[temp.size()];
						Arrays.sort(temp.toArray(tempIntegers));
						allDataItemList.removeAll();
						for (int a : tempIntegers) {
							allDataItemList.add(a + "");
						}
					}

				}
			}
		});
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label dataItemInfoLabel = new Label(container, SWT.NONE);
		dataItemInfoLabel.setText("��������ϸ��Ϣ��");
		new Label(container, SWT.NONE);

		dataItemInfoText = new Text(container, SWT.BORDER | SWT.READ_ONLY
				| SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		dataItemInfoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		new Label(container, SWT.NONE);

		initData(prjId);
		this.setMessage("��ѡ���ѷ����������" + System.getProperty("line.separator")
				+ "ע�⣺�ظ�����������ᱻ��ӣ�");
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
		try {
			commonDialogServiceImpl.updateDataItemState(prjId,
					outputDataItemList.getItems(), allDataItemList.getItems());
			// throw new RuntimeException("Fake Exception");
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);

		}
		super.okPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(620, 550);
	}

	/**
	 * �����ݿ��ж�ȡ����������ļ�¼�������������ID�������򣬽�������������ID��ʾ��allDataItemList�С�
	 */
	private void initData(String prjId) {
		CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
		try {
			java.util.List<TDataItem> dataItems = commonDialogServiceImpl
					.dataItemDialogQuery(prjId);
			if (dataItems.isEmpty() == false) {
				for (TDataItem dataItem : dataItems) {
					if (dataItem.getIsPublished().equals("0")) {
						allDataItemList.add(dataItem.getDataItemId() + "");
						dataItemInfoMap.put(dataItem.getDataItemId(),
								dataItem.toString());
					} else {
						outputDataItemList.add(dataItem.getDataItemId() + "");
						dataItemInfoMap.put(dataItem.getDataItemId(),
								dataItem.toString());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
	}

}
