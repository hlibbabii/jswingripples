
package org.incha.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author jrobledo
 */
public class IssuesView extends JScrollPane{
    private Object[][] issuesList;
    public void addTableView(Object[][] a, String[] b){
        issuesList = a;
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
                column.setPreferredWidth(10);
            } else {
                column.setPreferredWidth(500);
            }
        }

        t.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable)e.getSource();
                    editIssue(target.getSelectedRow(), target.getSelectedColumn());
                }
            }
        });


    }

    private void editIssue(int row, int column) {
        if (column==0) return; // not interested in the issue number
        // frame to edit issue
        JFrame principalFrame = new JFrame("Edit Issue");
        principalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        principalFrame.getContentPane().setLayout(new BorderLayout(0, 3));
        // things inside this frame
        JPanel editionPanel = createEditionPanel(row, column);
        principalFrame.getContentPane().add(editionPanel, BorderLayout.CENTER);
        //set frame location
        principalFrame.pack();
        principalFrame.setLocationRelativeTo(JSwingRipplesApplication.getInstance());
        //show frame
        principalFrame.setVisible(true);
    }

    private JPanel createEditionPanel(int row, int column) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10,20,10,20));
        // instruction message
        final JLabel instruction = new JLabel("Select a word to change its color",JLabel.LEFT);
        instruction.setBorder(new EmptyBorder(0,0,10,0));
        panel.add(instruction, BorderLayout.NORTH);
        // area with the issue to be edited
        final JTextArea issueText = new JTextArea((String)issuesList[row][column]);
        issueText.setEditable(false);
        issueText.setBackground(Color.WHITE);
        Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        issueText.setBorder(new CompoundBorder(etched, new EmptyBorder(10,20,10,20)));
        issueText.addMouseListener(new IssueMouseListener(issueText));
        panel.add(issueText, BorderLayout.CENTER);
        panel.setPreferredSize(panel.getPreferredSize());
        return panel;
    }
}
