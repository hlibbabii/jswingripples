package org.incha.ui.table.column.renderer;

import org.incha.core.jswingripples.eig.Mark;

import javax.swing.*;
import java.awt.*;

public class MarkColumnRenderer extends ColumnRenderer<Mark> {

    @Override
    public Component getComponent(JLabel label, Object value) {
        final Mark mark = (Mark) value;
        if (mark != null && mark != Mark.BLANK) {
            final Color color = mark.getColorForMark();
            label.setBackground(color);
            label.setText(mark.getValue());
        } else {
            label.setText("");
        }
        return label;
    }
}
