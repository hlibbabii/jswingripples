package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.Mark;

public class MarkSetAction implements UndoAction {
    private final JSwingRipplesEIGNode node;
    private final Mark oldMark;
    private final Mark newMark;

    /**
     * @param node
     * @param oldMark
     * @param newMark
     */
    public MarkSetAction(final JSwingRipplesEIGNode node, final Mark oldMark,
            final Mark newMark) {
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
