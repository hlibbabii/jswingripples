package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.table.column.renderer.ColumnRenderer;
import org.incha.ui.table.column.renderer.ShortNameColumnRenderer;

import javax.swing.*;

public class ClassShortNameColumn extends Column<JSwingRipplesEIGNode> {

    private final static String NAME = "Class";

    private JTable table;

    public ClassShortNameColumn(JTable table) {
        this.table = table;
    }

    @Override
    public JSwingRipplesEIGNode getValue(JSwingRipplesEIGNode node) {
        return node;
    }

    @Override
    public Class<?> getColumnClass() {
        return JSwingRipplesEIGNode.class;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ColumnRenderer<?> getColumnRenderer() {
        return new ShortNameColumnRenderer(table);
    }
}
