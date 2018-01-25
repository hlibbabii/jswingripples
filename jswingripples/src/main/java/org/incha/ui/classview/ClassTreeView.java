package org.incha.ui.classview;

import org.incha.core.JavaProject;
import org.incha.core.search.Searcher;

import javax.swing.table.TableCellRenderer;

public abstract class ClassTreeView extends AbstractHierarchicalView {
    private static final long serialVersionUID = -725916023414871313L;

    /**
     * Default constructor.
     */
    public ClassTreeView(final JavaProject project) {
        super(project);
        Searcher.getInstance().setClassTreeView(this);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createHeaderRenderer()
     */
    @Override
    protected TableCellRenderer createHeaderRenderer() {
        return new HeaderRenderer(this);
    }
}
