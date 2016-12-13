/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.incha.ui;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author jrobledo
 */
public class IssuesView extends JScrollPane{
    public void addTableView(Object[][] a, String[] b){
        JTable t = new JTable(a,b);
        IssuesDataModel dataModel = new IssuesDataModel();
        dataModel.setDataVector(a, b);
        t.setModel(dataModel);
        add(t);
        setViewportView(t);
        TableColumn column;
        for (int i = 0; i < t.getColumnCount(); i++) {
            column = t.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(10); //sport column is bigger
            } else {
                column.setPreferredWidth(500);
            }
        }    
    }
}
