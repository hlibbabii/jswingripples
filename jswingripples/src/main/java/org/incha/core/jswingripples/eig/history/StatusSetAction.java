package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.NodeEventData;

public class StatusSetAction implements UndoAction {
    private final JSwingRipplesEIGNode node;
    private final NodeEventData oldStatus;
    private final NodeEventData newStatus;

    /**
     * @param node
     * @param oldStatus
     * @param newStatus
     */
    public StatusSetAction(final JSwingRipplesEIGNode node,
                           final NodeEventData oldStatus,
                           final NodeEventData newStatus) {
        super();
        this.node = node;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    /* (non-Javadoc)
     * @see org.incha.core.jswingripples.eig.history.BackAction#undo()
     */
    @Override
    public UndoAction undo() {
        node.changeStatus(oldStatus.getStatus());
        return new StatusSetAction(node, newStatus, oldStatus);
    }
}
