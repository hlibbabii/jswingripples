package org.incha.ui.table.column.renderer;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.dependency.ClassDependencyView;

import javax.swing.*;
import java.awt.*;

public class DependencyNoteColumnRenderer extends ColumnRenderer<JSwingRipplesEIGNode> {

    private ClassDependencyView classDependencyView;

    public DependencyNoteColumnRenderer(ClassDependencyView classDependencyView) {
        this.classDependencyView = classDependencyView;
    }

    @Override
    public Component getComponent(JLabel label, Object value) {
        JSwingRipplesEIGNode neighborNode = (JSwingRipplesEIGNode) value;
        JSwingRipplesEIGNode node = classDependencyView.getNode();
        final JSwingRipplesEIG eig = node.getEig();
        JSwingRipplesEIGEdge edge=null;
        JSwingRipplesEIGEdge edge1=null;

        int callingDirection = classDependencyView.getCallingDirection();
        if (callingDirection==1) edge=eig.getEdge(node, neighborNode);
        else if (callingDirection==0) edge=eig.getEdge(neighborNode,node);
        else if (callingDirection==2) {
            edge=eig.getEdge(neighborNode,node);
            edge1=eig.getEdge(node,neighborNode);
        }
        if (edge==null && edge1==null) {
            label.setText("Transitive dependency");
        } else if (edge!=null && "Custom".equals(edge.getMark())) {
            label.setText("Custom dependency");
        } else if (edge1!=null && "Custom".equals(edge1.getMark())) {
            label.setText("Custom dependency");
        }
        return label;
    }
}
