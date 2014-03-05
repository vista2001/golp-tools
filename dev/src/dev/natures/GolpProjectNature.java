package dev.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class GolpProjectNature implements IProjectNature {
	public static final String ID = "dev.natures.golpProjectNature";
	private IProject project;

	@Override
	public void configure() throws CoreException {

	}

	@Override
	public void deconfigure() throws CoreException {

	}

	@Override
	public IProject getProject() {

		return this.project;
	}

	@Override
	public void setProject(IProject project) {

		this.project = project;
	}

}
