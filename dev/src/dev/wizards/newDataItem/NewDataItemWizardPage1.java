/* 文件名：       NewDataItemWizardPage1.java
 * 修改人：       rxy
 * 修改时间：   2013.12.14
 * 修改内容：   修改构造函数中，调用super方法时的参数为本类的类名。 
 */

package dev.wizards.newDataItem;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import dev.util.RegExpCheck;

/**
 * 该类定义了新建数据项向导的 第2页
 */
public class NewDataItemWizardPage1 extends WizardPage {

	private ISelection selection;
	private Combo dataItemTypeCombo;
	private Text dataItemlenText;
	private Text dataItemAOPText;

	// private Combo dataItemLenFixedCombo;

	public Combo getDataItemTypeCombo() {
		return dataItemTypeCombo;
	}

	// public Combo getDataItemLenFixedCombo()
	// {
	// return dataItemLenFixedCombo;
	// }

	public Text getDataItemlenText() {
		return dataItemlenText;
	}

	public Text getDataItemAOPText() {
		return dataItemAOPText;
	}

	public NewDataItemWizardPage1(ISelection selection) {
		super("NewDataItemWizardPage1");
		setTitle("新建数据项向导");
		setDescription("这个向导将指导你完成GOLP数据项的创建");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dataItemTypeLabel = new Label(container, SWT.NONE);
		dataItemTypeLabel.setText("*数据类型:");

		dataItemTypeCombo = new Combo(container, SWT.READ_ONLY);
		dataItemTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		// dataItemTypeCombo.add("0-int");
		dataItemTypeCombo.add("1-long");
		dataItemTypeCombo.add("2-double");
		dataItemTypeCombo.add("3-char");
		// dataItemTypeCombo.add("4-char[]");
		// dataItemTypeCombo.add("5-String");

		// Label dataItemLenFixedLabel = new Label(container, SWT.NONE);
		// dataItemLenFixedLabel.setText("*数据是否定长：");

		// dataItemLenFixedCombo = new Combo(container, SWT.READ_ONLY);
		// dataItemLenFixedCombo.setLayoutData(new GridData(SWT.FILL,
		// SWT.CENTER, true, false, 1, 1));
		// dataItemLenFixedCombo.add("0-是");
		// dataItemLenFixedCombo.add("1-否");

		Label dataItemlenLabel = new Label(container, SWT.NONE);
		dataItemlenLabel.setText("*数据项长度：");

		dataItemlenText = new Text(container, SWT.BORDER);
		dataItemlenText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dataItemlenText.setEnabled(false);

		Label dataItemAOPLabel = new Label(container, SWT.NONE);
		dataItemAOPLabel.setText("数据约束检查函数：");

		dataItemAOPText = new Text(container, SWT.BORDER);
		dataItemAOPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		dataItemTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (dataItemTypeCombo.getText().equals("4-char[]")
						|| dataItemTypeCombo.getText().equals("5-String")) {
					dataItemlenText.setEnabled(true);
					dataItemlenText.setText("");
				} else {
					dataItemlenText.setEnabled(false);
					dataItemlenText.setText("-1");
					setErrorMessage(null);

				}
				dialogChanged();
			}
		});

		// dataItemLenFixedCombo.addModifyListener(new ModifyListener()
		// {
		// public void modifyText(ModifyEvent e)
		// {
		// dialogChanged();
		// }
		// });

		dataItemlenText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (dataItemlenText.isEnabled()) {
					if (RegExpCheck.isPositiveInteger(dataItemlenText.getText())) {
						setErrorMessage(null);
					} else {
						setErrorMessage("数据项长度为正整数");
					}
					dialogChanged();
				}

			}
		});

	}

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
	private void dialogChanged() {
		setPageComplete(true);
	}

	public boolean validInput() {
		if (dataItemlenText.isEnabled()) {
			if (getDataItemTypeCombo().getText().length() == 0
					|| !(RegExpCheck.isPositiveInteger(getDataItemlenText()
							.getText())))
				return false;
			return true;
		} else {
			if (getDataItemTypeCombo().getText().length() == 0)
				return false;
			return true;
		}

	}

	@Override
	public boolean canFlipToNextPage() {

		return false;
	}

}
