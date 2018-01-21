package org.incha.ui.stats;

import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.classview.ClassTreeDataModel;
import org.incha.ui.table.column.ClassFullNameColumn;
import org.incha.ui.table.column.ClassShortNameColumn;
import org.incha.ui.table.column.CommentColumn;
import org.incha.ui.table.column.CouplingColumn;
import org.incha.ui.table.column.MarkColumn;
import org.incha.ui.table.column.SearchHitsColumn;

import java.util.List;

public class HierarchicalViewWithCouplingColumn extends HierarchicalView {
    /**
     * Default constructor.
     *
     * @param project
     * @param nodes
     */
    public HierarchicalViewWithCouplingColumn(JavaProject project, List<JSwingRipplesEIGNode> nodes) {
        super(project, nodes);
    }

    @Override
    protected ClassTreeDataModel createModel() {
        return new ClassTreeDataModel(
                new ClassShortNameColumn(this),
                new MarkColumn(),
                new CouplingColumn(),
                new ClassFullNameColumn(),
                new SearchHitsColumn(),
                new CommentColumn()
        );
    }
}
