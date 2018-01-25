package org.incha.ui.table.column.renderer;

import javax.swing.*;
import java.awt.*;

public class CommentColumnRenderer extends ColumnRenderer {

    @Override
    public Component getComponent(JLabel label, Object value) {
        if(value!=null){
            label.setText(value.toString());
        }
        return label;
    }
}
