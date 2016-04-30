package ts.eclipse.ide.internal.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

import ts.eclipse.ide.core.TypeScriptCorePlugin;
import ts.eclipse.ide.core.resources.IIDETypeScriptProject;
import ts.eclipse.ide.core.resources.ITypeScriptElementChangedListener;
import ts.eclipse.ide.core.resources.buildpath.ITypeScriptBuildPath;
import ts.eclipse.ide.core.resources.buildpath.ITypeScriptRootContainer;
import ts.eclipse.ide.core.utils.TypeScriptResourceUtil;
import ts.eclipse.ide.ui.TypeScriptUIImageResource;

/**
 * Display an overlay icon on the bottom top for
 * {@link ITypeScriptRootContainer}.
 */
public class BuildpathIndicatorLabelDecorator implements ILightweightLabelDecorator {

	private class DecoratorElementChangeListener implements ITypeScriptElementChangedListener {

		@Override
		public void buildPathChanged(IIDETypeScriptProject tsProject, ITypeScriptBuildPath newBuildPath,
				ITypeScriptBuildPath oldBuildPath) {
			List<IResource> changed = new ArrayList<IResource>();
			addResource(newBuildPath, changed);
			addResource(oldBuildPath, changed);
			fireChange(changed.toArray(new IResource[changed.size()]));
		}

		private void addResource(ITypeScriptBuildPath buildPath, List<IResource> resources) {
			if (buildPath == null) {
				return;
			}
			ITypeScriptRootContainer[] containers = buildPath.getRootContainers();
			for (int i = 0; i < containers.length; i++) {
				resources.add(containers[i].getContainer());
			}
		}

	}

	private ListenerList fListeners;
	private ITypeScriptElementChangedListener fChangeListener;

	@Override
	public void addListener(ILabelProviderListener listener) {
		if (fChangeListener == null) {
			fChangeListener = new DecoratorElementChangeListener();
			TypeScriptCorePlugin.getDefault().addTypeScriptElementChangedListener(fChangeListener);
		}

		if (fListeners == null) {
			fListeners = new ListenerList();
		}

		fListeners.add(listener);
	}

	@Override
	public void dispose() {

		if (fChangeListener != null) {
			TypeScriptCorePlugin.getDefault().removeTypeScriptElementChangedListener(fChangeListener);
			fChangeListener = null;
		}

		if (fListeners != null) {
			Object[] listeners = fListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				fListeners.remove(listeners[i]);
			}
			fListeners = null;
		}
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		if (fListeners == null)
			return;

		fListeners.remove(listener);

		if (fListeners.isEmpty() && fChangeListener != null) {
			TypeScriptCorePlugin.getDefault().removeTypeScriptElementChangedListener(fChangeListener);
			fChangeListener = null;
		}
	}

	private void fireChange(IResource[] elements) {
		if (fListeners != null && !fListeners.isEmpty()) {
			LabelProviderChangedEvent event = new LabelProviderChangedEvent(this, elements);
			Object[] listeners = fListeners.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				((ILabelProviderListener) listeners[i]).labelProviderChanged(event);
			}
		}
	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		ImageDescriptor overlay = getOverlay(element);
		if (overlay != null) {
			decoration.addOverlay(overlay, IDecoration.TOP_RIGHT);
		}
	}

	private ImageDescriptor getOverlay(Object element) {
		if (element instanceof IResource) {
			IResource resource = (IResource) element;
			IProject project = resource.getProject();
			if (project != null && TypeScriptResourceUtil.isTypeScriptProject(project)) {
				try {
					IIDETypeScriptProject tsProject = TypeScriptResourceUtil.getTypeScriptProject(project);
					if (tsProject.getTypeScriptBuildPath().isRootContainer(resource)) {
						return TypeScriptUIImageResource.getImageDescriptor(TypeScriptUIImageResource.DESC_OVR_LIBRARY);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}