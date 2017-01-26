package org.incha.core.jswingripples.eig.history;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class AnnotationSetAction implements UndoAction {
	
	private final JSwingRipplesEIGNode node;
    private final String newAnnotation;
    private final String oldAnnotation;
    
    public AnnotationSetAction(final JSwingRipplesEIGNode node, final String oldAnnotation,
            final String newAnnotation) {
        super();
        this.node = node;
        this.oldAnnotation = oldAnnotation;
        this.newAnnotation = newAnnotation;
    }

	@Override
	public AnnotationSetAction undo() {
		node.setAnottation(oldAnnotation);
		return new AnnotationSetAction(node,newAnnotation,oldAnnotation);
	}

}
