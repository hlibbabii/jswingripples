package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class ClassFullNameColumn extends Column<String> {
    private static final String NAME = "Full name";

    @Override
    public String getValue(JSwingRipplesEIGNode node) {
        return node.getFullName();
    }

    @Override
    public Class<String> getColumnClass() {
        return String.class;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
