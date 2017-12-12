package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

public class MarkSetAction implements UndoAction {
    private final JSwingRipplesEIGNode node;
    private final EIGStatusMarks.Mark oldMark;
    private final EIGStatusMarks.Mark newMark;

    /**
     * @param node
     * @param oldMark
     * @param newMark
     */
    public MarkSetAction(final JSwingRipplesEIGNode node, final EIGStatusMarks.Mark oldMark,
            final EIGStatusMarks.Mark newMark) {
        super();
        this.node = node;
        this.oldMark = oldMark;
        this.newMark = newMark;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.BackAction#undo()
     */
    @Override
    public UndoAction undo() {
        node.setMark(oldMark);
        return new MarkSetAction(node, newMark, oldMark);
    }
}
