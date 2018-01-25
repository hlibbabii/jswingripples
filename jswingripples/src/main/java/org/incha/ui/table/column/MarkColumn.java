package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.Mark;
import org.incha.ui.table.column.renderer.ColumnRenderer;
import org.incha.ui.table.column.renderer.MarkColumnRenderer;

public class MarkColumn extends Column<ParentAwareMark> {

    private final static String NAME = "Mark";

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public ParentAwareMark getValue(JSwingRipplesEIGNode node) {
        return ParentAwareMark.create(node);
    }

    @Override
    public Class<Mark> getColumnClass() {
        return Mark.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ColumnRenderer getColumnRenderer() {
        return new MarkColumnRenderer();
    }

    @Override
    public boolean isSortable() {
        return true;
    }
}
