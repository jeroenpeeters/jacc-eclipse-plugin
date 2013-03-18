package nl.jeroenpeeters.eclipse.plugin.jacc;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

public class ConvertToJaccNatureAction implements IObjectActionDelegate {

	private ISelection selection;

	@Override
	public void run(IAction action) {

		final IProject project = getProject(action);
		final JaccNature jaccNature = new JaccNature();
		jaccNature.setProject(project);
		try {
			jaccNature.configure();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		try {
			if(!hasJaccNaure(project)){
				action.setEnabled(false);
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	private boolean hasJaccNaure(final IProject project) throws CoreException {
		final IProjectDescription description = project.getDescription();
		final String[] natures = description.getNatureIds();
		for (int i = 0; i < natures.length; ++i) {
			if (JaccNature.NATURE_ID.equals(natures[i])) {
				return true;
			}
		}
		return false;
	}

	
	private IProject getProject(IAction action) {
		if (selection instanceof IStructuredSelection) {
			for (Iterator it = ((IStructuredSelection) selection).iterator(); it
					.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element)
							.getAdapter(IProject.class);
				}
				return project;
			}
		}
		throw new IllegalArgumentException(
				"Unable to retrieve IProjec from provide IAction instance");
	}

}
