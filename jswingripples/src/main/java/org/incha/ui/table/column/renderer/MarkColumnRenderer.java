package org.incha.ui.table.column.renderer;

import org.incha.core.jswingripples.eig.Mark;
import org.incha.ui.table.column.ParentAwareMark;

import javax.swing.*;
import java.awt.*;

public class MarkColumnRenderer extends ColumnRenderer {

    @Override
    public Component getComponent(JLabel label, Object value) {
        final ParentAwareMark parentAwareMark = (ParentAwareMark) value;
        Mark mark = parentAwareMark.getMark();
        if (mark != Mark.BLANK) {
            final Color color = mark.getColorForMark();
            label.setBackground(color);
            label.setText(mark.getValue());
        } else {
            label.setText("");
        }
        return label;
    }
}
