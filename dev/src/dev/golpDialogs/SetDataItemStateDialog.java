/* 文件名：       OutputDataItemDialog.java
 * 描述：           该文件定义了类OutputDataItemDialog，该类实现了在新建交易向导中，
 *         让用户选择输出数据项的功能。输出数据项之间用竖线（|）分隔,输出数据项的格式形如：DataItemID@a@b，
 *         若必须，则a为1，否则a为0，若该数据从请求数据中复制，则b为1，否则b为0。
 * 创建人：       rxy
 * 创建时间：   2013.12.4
 * 修改人：       rxy
 * 修改时间：   2013.12.23
 * 修改内容：   增加文本框，用于显示数据项的详细信息。 
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
 * SetDataItemStateDialog类，该类实现了在设定数据项发布状态的handler中，让用户选择数据项状态的功能。
 * 
 * @author ly
 */
public class SetDataItemStateDialog extends TitleAreaDialog {
	/**
	 * 用来显示所有数据项的列表（org.eclipse.swt.widgets.List类型），支持多选。
	 */
	private List allDataItemList;

	/**
	 * 用来显示输出数据项的列表（org.eclipse.swt.widgets.List类型），支持多选。
	 */
	private List outputDataItemList;

	/**
	 * 添加按钮，点击该按钮，会将所有数据项列表中选中的项添加到输出数据项列表。
	 */
	private Button addButton;
	/**
	 * 删除按钮，点击该按钮，会将所有数据项列表中选中的项添加到输入数据项列表。
	 */
	private Button delButton;

	/**
	 * 所属工程Id
	 */
	private String prjId;

	/**
	 * 该数组用于存储每一个数据项的详细信息
	 */
	// private String[] dataItemInfo;
	private Map<Integer, String> dataItemInfoMap = new HashMap<Integer, String>();

	/**
	 * 用于显示数据项详细信息的文本框
	 */
	private Text dataItemInfoText;

	/**
	 * 构造函数。
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
		setTitle("输出数据项");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		Label allDataItemLabel = new Label(container, SWT.NONE);
		allDataItemLabel.setText("所有数据项：");

		new Label(container, SWT.NONE);

		Label outputDataItemLabel = new Label(container, SWT.NONE);
		outputDataItemLabel.setText("输出数据项：");

		allDataItemList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		allDataItemList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 8));
		allDataItemList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// 只有当allDataItemList中有条目被选中时，添加按钮（addButton）才可用。
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
				// 只有当allDataItemList中有条目被选中时，添加按钮（addButton）才可用。
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
		addButton.setText("添加>>");
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
		delButton.setText("删除<<");
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
		dataItemInfoLabel.setText("数据项详细信息：");
		new Label(container, SWT.NONE);

		dataItemInfoText = new Text(container, SWT.BORDER | SWT.READ_ONLY
				| SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		dataItemInfoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		new Label(container, SWT.NONE);

		initData(prjId);
		this.setMessage("请选择已发布的数据项。" + System.getProperty("line.separator")
				+ "注意：重复的数据项将不会被添加！");
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
	 * 从数据库中读取所有数据项的记录，并按数据项的ID进行排序，将排序后的数据项ID显示在allDataItemList中。
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
