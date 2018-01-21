package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class ProbabilityColumn extends Column<String> {

    private static final String NAME = "Probability";

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
