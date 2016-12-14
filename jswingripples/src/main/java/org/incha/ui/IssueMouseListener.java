package org.incha.ui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by constanzafierro on 13-12-16.
 */
public class IssueMouseListener extends MouseAdapter {
    private final JTextArea issueText;

    public IssueMouseListener(JTextArea issueText){
        super();
        this.issueText = issueText;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
    }
    private void showPopup(MouseEvent e) {
        if (issueText.getSelectedText() != null) { // See if something was selected
            if (e.isPopupTrigger()) {
                colorsPopupMenu().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private JPopupMenu colorsPopupMenu() {
        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem color = new JMenuItem("Red");
        color.addActionListener(new ColorListener(new Color(255, 51, 0)));
        popMenu.add(color);
        color = new JMenuItem("Blue");
        color.addActionListener(new ColorListener(new Color(51, 153, 255)));
        popMenu.add(color);
        color = new JMenuItem("Green");
        color.addActionListener(new ColorListener(new Color(51, 204, 51)));
        popMenu.add(color);
        color = new JMenuItem("Remove all colors");
        color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Highlighter highlighter = issueText.getHighlighter();
                highlighter.removeAllHighlights();
            }
        });
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
