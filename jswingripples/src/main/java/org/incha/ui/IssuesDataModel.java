
package org.incha.ui;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jrobledo
 */
public class IssuesDataModel extends DefaultTableModel{
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
