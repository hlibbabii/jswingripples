package org.incha.ui.stats;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.JavaProject;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.classview.ClassTreeView;
import org.incha.ui.texteditor.TextEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class HierarchicalView extends ClassTreeView {
    private static final long serialVersionUID = -725916023414871313L;

    /**
     * Default constructor.
     */
    public HierarchicalView(final JavaProject project, final List<JSwingRipplesEIGNode> nodes) {
        super(project);
        setData(nodes);
    	
        addMouseListener(new MouseAdapter() {

            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(final MouseEvent e) {
                Point point = e.getPoint();
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopupMenu(point);
                } else if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    JSwingRipplesEIGNode selectedItem = getSelectedItem(point);
                    if (!isColumnEditable(point)) {
                        IMember selectedNodeIMember = selectedItem.getNodeIMember();
                        ISourceRange selectedNodeSourceRange;
                        try {
                            selectedNodeSourceRange = selectedNodeIMember.getSourceRange();
                        } catch (JavaModelException e1) {
                            throw new RuntimeException(e1);
                        }
                        final ICompilationUnit unit = selectedNodeIMember.getCompilationUnit();
                        String fileToOpen = unit.getPath().toString();
                        try {
                            TextEditor textEditor = TextEditor.getInstance();
                            textEditor.bringToFront();
                            textEditor.openFile(fileToOpen, selectedNodeSourceRange);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                
            }
            
        });
    }

    private void showPopupMenu(Point point) {
        final JSwingRipplesEIGNode node = getSelectedItem(point);
        if (node != null) {
            ICActionsManager.getInstance().showMenuForNode(node, point.x, point.y, this);
        }
    }
}
