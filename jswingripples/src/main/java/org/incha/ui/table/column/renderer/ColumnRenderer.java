package org.incha.ui.table.column.renderer;

import javax.swing.*;
import java.awt.*;

public abstract class ColumnRenderer<T> {

    public ColumnRenderer() {
    }

    public Component getComponent(JLabel label, Object value) {return label;}
}
