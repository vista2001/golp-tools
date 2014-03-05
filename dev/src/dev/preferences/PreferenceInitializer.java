package dev.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import dev.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public PreferenceInitializer() {

	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.WORKSPACE_PATH, "c:\\");
		store.setDefault(PreferenceConstants.MAKEFILEMODEL_PATH, "c:\\");
		store.setDefault(PreferenceConstants.CODEMODEL_PATH, "c:\\");
	}

}
