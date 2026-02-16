package org.eclipse.chemclipse.ux.extension.xxd.ui.parts;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IAggregateWorkingSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISaveablesSource;
import org.eclipse.ui.ISecondarySaveableSource;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.Saveable;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.internal.DefaultSaveable;
import org.eclipse.ui.internal.navigator.NavigatorPlugin;
import org.eclipse.ui.internal.navigator.filters.UserFilter;
import org.eclipse.ui.internal.views.helpers.EmptyWorkspaceHelper;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.views.WorkbenchViewerSetup;

@SuppressWarnings("restriction")
public final class DataNavigator extends CommonNavigator implements ISecondarySaveableSource {

	public static final String VIEW_ID = IPageLayout.ID_PROJECT_EXPLORER;

	public static final int WORKING_SETS = 0;
	public static final int PROJECTS = 1;

	private static final String MEMENTO_REGEXP_FILTER_ELEMENT = "regexpFilter"; //$NON-NLS-1$
	private static final String MEMENTO_REGEXP_FILTER_REGEXP_ATTRIBUTE = "regexp"; //$NON-NLS-1$
	private static final String MEMENTO_REGEXP_FILTER_ENABLED_ATTRIBUTE = "enabled"; //$NON-NLS-1$

	private int rootMode;

	private String workingSetLabel;

	private List<UserFilter> userFilters;
	private EmptyWorkspaceHelper emptyWorkspaceHelper;

	/**
	 * TODO: Replace this with a preference / product property.
	 * For now: use user.home as a "root folder".
	 */
	private File getRootFolder() {

		return new File(System.getProperty("user.home"));
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {

		super.init(site, memento);
		userFilters = new ArrayList<>();
		if(memento != null) {
			IMemento[] filters = memento.getChildren(MEMENTO_REGEXP_FILTER_ELEMENT);
			for(IMemento filterMemento : filters) {
				String regexp = filterMemento.getString(MEMENTO_REGEXP_FILTER_REGEXP_ATTRIBUTE);
				Boolean enabled = filterMemento.getBoolean(MEMENTO_REGEXP_FILTER_ENABLED_ATTRIBUTE);
				userFilters.add(new UserFilter(regexp, enabled));
			}
		}
	}

	@Override
	public void saveState(IMemento aMemento) {

		Object data = getCommonViewer().getData(NavigatorPlugin.RESOURCE_REGEXP_FILTER_DATA);
		if(data instanceof Collection) {
			Collection<?> dataAsFilters = (Collection<?>)data;
			for(Object object : dataAsFilters) {
				if(!(object instanceof UserFilter filter)) {
					continue;
				}
				IMemento memento = aMemento.createChild(MEMENTO_REGEXP_FILTER_ELEMENT);
				memento.putString(MEMENTO_REGEXP_FILTER_REGEXP_ATTRIBUTE, filter.getRegexp());
				memento.putBoolean(MEMENTO_REGEXP_FILTER_ENABLED_ATTRIBUTE, filter.isEnabled());
			}
		}
		super.saveState(aMemento);
	}

	@Override
	public void createPartControl(Composite aParent) {

		emptyWorkspaceHelper = new EmptyWorkspaceHelper();
		Composite displayAreas = emptyWorkspaceHelper.getComposite(aParent);

		super.createPartControl(displayAreas);

		CommonViewer viewer = getCommonViewer();
		viewer.setData(NavigatorPlugin.RESOURCE_REGEXP_FILTER_DATA, this.userFilters);

		// IMPORTANT: replace Project Explorer model with filesystem model
		viewer.setContentProvider(new FileTreeContentProvider());
		viewer.setLabelProvider(new FileLabelProvider());

		File root = getRootFolder();
		viewer.setInput(root);
		setContentDescription(root.getAbsolutePath());

		if(this.userFilters.stream().anyMatch(UserFilter::isEnabled)) {
			viewer.refresh();
		}
	}

	@Override
	protected ActionGroup createCommonActionGroup() {

		return super.createCommonActionGroup();
	}

	@Override
	public void updateTitle() {

		super.updateTitle();
		Object input = getCommonViewer().getInput();

		if(input == null || input instanceof IAggregateWorkingSet) {
			setContentDescription(""); //$NON-NLS-1$
			return;
		}

		if(input instanceof File f) {
			setContentDescription(f.getAbsolutePath());
			return;
		}

		if(!(input instanceof org.eclipse.core.resources.IResource)) {
			String label = ((ILabelProvider)getCommonViewer().getLabelProvider()).getText(input);
			setContentDescription(label != null ? label : String.valueOf(input));
			return;
		}
	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {

		// TODO
		super.handleDoubleClick(anEvent);
	}

	@Override
	protected CommonViewer createCommonViewer(Composite aParent) {

		CommonViewer viewer = super.createCommonViewer(aParent);
		emptyWorkspaceHelper.setNonEmptyControl(viewer.getControl());
		WorkbenchViewerSetup.setupViewer(viewer);
		return viewer;
	}

	@Override
	public Saveable[] getSaveables() {

		if(!hasSaveablesProvider()) {
			IEditorPart saveablePart = getActiveEditor();
			return saveablePart != null ? saveablePart instanceof ISaveablesSource i ? i.getSaveables() : new Saveable[]{new DefaultSaveable(saveablePart)} : new Saveable[]{};
		}
		return super.getSaveables();
	}

	@Override
	public Saveable[] getActiveSaveables() {

		if(!hasSaveablesProvider()) {
			IEditorPart saveablePart = getActiveEditor();
			return saveablePart != null ? saveablePart instanceof ISaveablesSource i ? i.getActiveSaveables() : new Saveable[]{new DefaultSaveable(saveablePart)} : new Saveable[]{};
		}
		return super.getActiveSaveables();
	}

	private IEditorPart getActiveEditor() {

		var page = getSite().getPage();
		return page != null ? page.getActiveEditor() : null;
	}

	@Override
	public boolean isDirtyStateSupported() {

		return hasSaveablesProvider();
	}

	@Override
	public void setRootMode(int mode) {

		rootMode = mode;
	}

	@Override
	public int getRootMode() {

		return rootMode;
	}

	@Override
	public void setWorkingSetLabel(String label) {

		workingSetLabel = label;
	}

	@Override
	public String getWorkingSetLabel() {

		return workingSetLabel;
	}

	private static final class FileTreeContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {

			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {

			if(!(parentElement instanceof File f) || !f.isDirectory()) {
				return new Object[0];
			}
			File[] children = f.listFiles();
			if(children == null) {
				return new Object[0];
			}

			// Sort: directories first, then files; alphabetically
			Arrays.sort(children, Comparator.comparing((File x) -> !x.isDirectory()).thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER));

			return children;
		}

		@Override
		public Object getParent(Object element) {

			return (element instanceof File f) ? f.getParentFile() : null;
		}

		@Override
		public boolean hasChildren(Object element) {

			return (element instanceof File f) && f.isDirectory() && f.listFiles() != null && f.listFiles().length > 0;
		}

		@Override
		public void dispose() {

			// nothing
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			// nothing
		}
	}

	private static final class FileLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {

			if(element instanceof File f) {
				String name = f.getName();
				return (name == null || name.isBlank()) ? f.getAbsolutePath() : name;
			}
			return super.getText(element);
		}
	}
}