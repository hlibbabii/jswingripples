package org.incha.ui.table.column;

import org.eclipse.jdt.core.IType;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.table.column.renderer.ColumnRenderer;
import org.incha.ui.table.column.renderer.SearchHitsColumnRenderer;

public class SearchHitsColumn extends Column<String> {
    private static final String NAME = "Search Hits";

    @Override
    public String getValue(JSwingRipplesEIGNode node) {
        return node.getNodeIMember() instanceof IType ?
            node.getShortName():
            "";
    }

    @Override
    public Class<?> getColumnClass() {
        return String.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ColumnRenderer<?> getColumnRenderer() {
        return new SearchHitsColumnRenderer();
    }
}
