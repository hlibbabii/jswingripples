package org.incha.ui.dependency;

import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.classview.ClassTreeDataModel;
import org.incha.ui.classview.ClassTreeView;
import org.incha.ui.classview.HeaderRenderer;
import org.incha.ui.table.column.ClassFullNameColumn;
import org.incha.ui.table.column.ClassShortNameColumn;
import org.incha.ui.table.column.DependencyNotesColumn;
import org.incha.ui.table.column.MarkColumn;

import javax.swing.table.TableCellRenderer;
import java.util.Collection;
import java.util.Set;

@SuppressWarnings("serial")
public class ClassDependencyView extends ClassTreeView {
    private JSwingRipplesEIGNode node;
    private final int callingDirection;
    /**
     * @param project
     */
    public ClassDependencyView(final JavaProject project, final int callingDirection) {
        super(project);
        this.callingDirection = callingDirection;
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.ClassTreeView#createModel()
     */
    @Override
    protected ClassTreeDataModel createModel() {
        return new ClassTreeDataModel(
                new ClassShortNameColumn(this),
                new MarkColumn(),
                new DependencyNotesColumn(this),
                new ClassFullNameColumn()
            ).withFixedValueColumns(1, 3);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#createHeaderRenderer()
     */
    @Override
    protected TableCellRenderer createHeaderRenderer() {
        return new HeaderRenderer(this);
    }

    /* (non-Javadoc)
     * @see org.incha.ui.classview.AbstractHierarchicalView#setData(java.util.Collection)
     */
    @Override
    public void setData(final Collection<JSwingRipplesEIGNode> members) {
        throw new IllegalStateException("Illegal method access, please use setDependencies() instead");
    }
    /**
     * @param node
     * @param set
     */
    public void setDependencies(final JSwingRipplesEIGNode node,
            final Set<JSwingRipplesEIGNode> set) {
        this.node = node;
        getClassHierarchyModel().setFixedValue(node);
        super.setData(set);
    }

    public JSwingRipplesEIGNode getNode() {
        return node;
    }

    public int getCallingDirection() {
        return callingDirection;
    }
}
