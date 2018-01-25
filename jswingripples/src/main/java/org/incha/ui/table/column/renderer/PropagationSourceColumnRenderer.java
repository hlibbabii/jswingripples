package org.incha.ui.table.column.renderer;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

import javax.swing.*;
import java.awt.*;

public class PropagationSourceColumnRenderer extends ColumnRenderer {
    @Override
    public Component getComponent(JLabel label, Object value) {
        JSwingRipplesEIGNode node = (JSwingRipplesEIGNode) value;
        if (node != null) {
            label.setText(node.getShortName());
        }
        return label;
    }
}
