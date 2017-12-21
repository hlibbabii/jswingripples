package org.incha.ui.dependency;

import org.incha.core.JavaProject;
import org.incha.ui.classview.AbstractHierarchicalView;
import org.incha.ui.classview.ClassTreeDataModel;
import org.incha.ui.table.column.ClassShortNameColumn;

public class AllClassesView extends AbstractHierarchicalView {
    private static final long serialVersionUID = 8693122992017345093L;

    /**
     * @param project the project.
     */
    public AllClassesView(final JavaProject project) {
        super(project);
        //hide table header
        setTableHeader(null);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createModel()
     */
    @Override
    protected ClassTreeDataModel createModel() {
        return new ClassTreeDataModel(new ClassShortNameColumn(this) {
            @Override
            public String getName() {
                return "";
            }
        });
    }
}
