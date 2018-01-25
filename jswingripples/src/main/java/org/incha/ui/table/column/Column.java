package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.table.column.renderer.ColumnRenderer;

import javax.swing.*;
import java.awt.*;

public abstract class Column<T> {

    public boolean isEditable() {
        return false;
    }

    public abstract T getValue(JSwingRipplesEIGNode node);
    public void setValue(JSwingRipplesEIGNode node, T value) {
        throw new UnsupportedOperationException();
    }
    public abstract Class<?> getColumnClass();
    public abstract String getName();

    public ColumnRenderer getColumnRenderer() {
        return new ColumnRenderer() {
            @Override
            public Component getComponent(JLabel label, Object value) {
                return label;
            }
        };
    }
}
