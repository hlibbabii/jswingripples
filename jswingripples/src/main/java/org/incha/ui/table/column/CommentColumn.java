package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.table.column.renderer.ColumnRenderer;
import org.incha.ui.table.column.renderer.CommentColumnRenderer;

public class CommentColumn extends Column<String> {

    private static final String NAME = "Comment";

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public String getValue(JSwingRipplesEIGNode node) {
        return node.getAnnottation() != null ?
                node.getAnnottation() : "";
    }

    @Override
    public void setValue(JSwingRipplesEIGNode node, String value) {
        node.onAnnotationEditedManually(value);
    }

    @Override
    public Class<String> getColumnClass() {
        return String.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ColumnRenderer getColumnRenderer() {
        return new CommentColumnRenderer();
    }
}
