package org.incha.ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by constanzafierro on 13-12-16.
 */
public class IssueMouseListener implements MouseListener {
    private final JTextArea issueText;

    public IssueMouseListener(JTextArea issueText){
        super();
        this.issueText = issueText;
    }
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
    }
    private void showPopup(MouseEvent e) {
        if (issueText.getSelectedText() != null) { // See if they selected something
            JPopupMenu colorMenu = colorsPopupMenu();
            if (e.isPopupTrigger()) {
                colorMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private JPopupMenu colorsPopupMenu() {
        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem color = new JMenuItem("Red");
        color.addActionListener(new ColorListener(Color.RED));
        popMenu.add(color);
        color = new JMenuItem("Blue");
        color.addActionListener(new ColorListener(Color.BLUE));
        popMenu.add(color);
        color = new JMenuItem("Green");
        color.addActionListener(new ColorListener(Color.GREEN));
        popMenu.add(color);
        return popMenu;
    }

    private class ColorListener implements ActionListener {
        private Color color;

        private ColorListener(Color color) {
            this.color = color;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Highlighter highlighter = issueText.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(color);
            int start = issueText.getSelectionStart();
            int end = issueText.getSelectionEnd();
            try {
                highlighter.addHighlight(start, end, painter);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
            issueText.setSelectionEnd(start);
        }
    }
}
