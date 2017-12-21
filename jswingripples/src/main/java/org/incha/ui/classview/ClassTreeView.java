package org.incha.ui.classview;

import org.incha.core.JavaProject;
import org.incha.core.search.Searcher;
import org.incha.ui.table.column.ClassFullNameColumn;
import org.incha.ui.table.column.ClassShortNameColumn;
import org.incha.ui.table.column.CommentColumn;
import org.incha.ui.table.column.MarkColumn;
import org.incha.ui.table.column.ProbabilityColumn;
import org.incha.ui.table.column.SearchHitsColumn;

import javax.swing.table.TableCellRenderer;

public class ClassTreeView extends AbstractHierarchicalView {
    private static final long serialVersionUID = -725916023414871313L;

    /**
     * Default constructor.
     */
    public ClassTreeView(final JavaProject project) {
        super(project);
        Searcher.getInstance().setClassTreeView(this);
    }
    /* (non-Javadoc)
     * @see org.incha.ui.AbstractHierarchicalView#createModel()
     */
    @Override
    protected ClassTreeDataModel createModel() {
        return new ClassTreeDataModel(
                new ClassShortNameColumn(this),
                new MarkColumn(),
                new ProbabilityColumn(),
                new ClassFullNameColumn(),
                new SearchHitsColumn(),
                new CommentColumn()
                );
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createHeaderRenderer()
     */
    @Override
    protected TableCellRenderer createHeaderRenderer() {
        return new ClassTreeHeaderRenderer(this);
    }
}
