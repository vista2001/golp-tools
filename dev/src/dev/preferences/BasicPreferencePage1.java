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
		/** ������ѡ������ */
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		/** ������ѡ��ҳ��������Ϣ */
		setDescription("��������");
	}

	public void createFieldEditors() {
		/** ���һ����ѡ��ť���ֶ� */
		addField(new RadioGroupFieldEditor(
				PreferenceConstants.WORKSPACE_PATH, "Ĭ�ϵ���������", 2,
				new String[][] { { "�ͻ�", "Customer" }, { "��ϵ��", "Contact" } },
				getFieldEditorParent(), true));
	}
	


	public void init(IWorkbench workbench) {
	}

}
