package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.table.column.renderer.ColumnRenderer;
import org.incha.ui.table.column.renderer.PropagationSourceColumnRenderer;

public class PropagationSourceColumn extends Column<JSwingRipplesEIGNode> {
    private static final String NAME = "Propagation source";

    @Override
    public JSwingRipplesEIGNode getValue(JSwingRipplesEIGNode node) {
        return node.getChangePropagationSource();
    }

    @Override
    public Class<JSwingRipplesEIGNode> getColumnClass() {
        return JSwingRipplesEIGNode.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ColumnRenderer getColumnRenderer() {
        return new PropagationSourceColumnRenderer();
    }
}
