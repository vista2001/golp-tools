package dev.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import dev.Activator;


public class BasicPreferencePage1 extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public BasicPreferencePage1() {
		super(GRID);
		/** 设置首选项保存对象 */
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		/** 设置首选项页面描述信息 */
		setDescription("基本设置");
	}

	public void createFieldEditors() {
		/** 添加一个单选按钮组字段 */
		addField(new RadioGroupFieldEditor(
				PreferenceConstants.WORKSPACE_PATH, "默认的搜索类型", 2,
				new String[][] { { "客户", "Customer" }, { "联系人", "Contact" } },
				getFieldEditorParent(), true));
	}
	


	public void init(IWorkbench workbench) {
	}

}
