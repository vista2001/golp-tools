package dev.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import dev.Activator;

public class BasicPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * Create the preference page.
	 */
	public BasicPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		/** 设置首选项页面描述信息 */
		setDescription("基本设置");
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		{
			// Create the field editors
			StringFieldEditor workSpacePath = new StringFieldEditor(PreferenceConstants.WORKSPACE_PATH, "工作区路径", 50, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
			StringFieldEditor codeModelPath = new StringFieldEditor(PreferenceConstants.CODEMODEL_PATH, "代码模板路径", 50, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
			StringFieldEditor makeFileModelPath = new StringFieldEditor(PreferenceConstants.MAKEFILEMODEL_PATH, "MakeFile模板路径", 50, StringFieldEditor.VALIDATE_ON_KEY_STROKE, getFieldEditorParent());
			addField(workSpacePath);
			addField(codeModelPath);
			addField(makeFileModelPath);
		}
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
