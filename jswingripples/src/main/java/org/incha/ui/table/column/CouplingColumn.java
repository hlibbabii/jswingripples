package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class CouplingColumn extends Column<String> {

    private static final String NAME = "Coupling with Impact Set";

    @Override
    public String getValue(JSwingRipplesEIGNode node) {
        return node.getProbability();
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
