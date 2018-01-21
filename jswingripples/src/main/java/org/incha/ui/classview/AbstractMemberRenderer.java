package org.incha.ui.classview;

import org.incha.ui.table.column.renderer.ColumnRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AbstractMemberRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 5646472411943179112L;

    private ColumnRenderer columnRenderer;

    public AbstractMemberRenderer(ColumnRenderer columnRenderer) {
        this.columnRenderer = columnRenderer;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable t, final Object value,
            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        setBackground(null);
        final JLabel label = (JLabel) super.getTableCellRendererComponent(
                t, value, isSelected, hasFocus, row, column);
        label.setIcon(null);
        label.setHorizontalTextPosition(RIGHT);

        return columnRenderer.getComponent(label, value);
    }
}
