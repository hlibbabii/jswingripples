package org.incha.ui.table.column.renderer;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.Mark;
import org.incha.ui.classview.AbstractHierarchicalView;
import org.incha.ui.classview.AbstractJavaElementIcon;
import org.incha.ui.classview.AbstractMemberIcon;
import org.incha.ui.classview.FieldIcon;
import org.incha.ui.classview.MethodIcon;
import org.incha.ui.classview.RootTypeIcon;

import javax.swing.*;
import java.awt.*;

import static org.incha.core.jswingripples.eig.Mark.BLANK;

public class ShortNameColumnRenderer extends ColumnRenderer<JSwingRipplesEIGNode> {

    private JTable table;

    public ShortNameColumnRenderer(JTable table) {
        this.table = table;
    }

    @Override
    public Component getComponent(JLabel label, Object value) {

        JSwingRipplesEIGNode node = (JSwingRipplesEIGNode) value;
        final AbstractHierarchicalView view = (AbstractHierarchicalView) table;
        final AbstractJavaElementIcon icon = getIcon(node);
        if (icon != null) { // initializer can have not any icon
            icon.setAccessType(getAccessType(node));
            icon.setHierarchyOffset(view.getHierarchyOffset(node));
            icon.setHasChildren(view.hasChildren(node));
            icon.setExpanded(view.isExpanded(node));
        }

        if (node.getNodeIMember() instanceof IInitializer) {
            label.setText("{...}");
        } else {
            label.setText(node.getShortName());
        }
        label.setIcon(icon);
        return label;
    }


    private RootTypeIcon setRootTypeIcon(JSwingRipplesEIGNode node) {
        RootTypeIcon rootTypeIcon = new RootTypeIcon();
        if (node.getNodeIMember() instanceof IType) {
            //expanded state
            final IType type = (IType) node.getNodeIMember();

            //access type
            final int flags;
            try {
                flags = type.getFlags();
            } catch (JavaModelException e) {
                throw new RuntimeException(e);
            }

            //type (interface/enum/class)
            if (Flags.isEnum(flags)) {
                rootTypeIcon.setType(RootTypeIcon.ENUM);
            } else if (Flags.isInterface(flags)) {
                rootTypeIcon.setType(RootTypeIcon.INTERFACE);
            } else {
                rootTypeIcon.setType(RootTypeIcon.CLASS);
            }

            final Mark mark = node.getMark() != null ?
                    node.getMark() : BLANK;
            rootTypeIcon.setMarkImage(mark.getImageDescriptorForMark());
        }
        return rootTypeIcon;
    }

    /**
     * Get the icon corresponding the the node's type.
     *
     * @param node the EIG node.
     * @return the corresponding icon.
     */
    private AbstractJavaElementIcon getIcon(final JSwingRipplesEIGNode node) {
        final IMember member = node.getNodeIMember();
        if (member instanceof IType) {
            return setRootTypeIcon(node);
        } else if (member instanceof IField) {
            return new FieldIcon();
        } else if (member instanceof IInitializer || member instanceof IMethod) {
            return new MethodIcon();
        }

        return null;
    }


    /**
     * @param member
     * @return
     * @throws JavaModelException
     */
    private int getAccessType(final JSwingRipplesEIGNode member) {
        final int flags;
        try {
            flags = member.getNodeIMember().getFlags();
        } catch (JavaModelException e) {
            throw new RuntimeException(e);
        }
        if (Flags.isPrivate(flags)) {
            return AbstractMemberIcon.PRIVATE;
        }
        if (Flags.isPackageDefault(flags)) {
            return AbstractMemberIcon.DEFAULT;
        }
        if (Flags.isProtected(flags)) {
            return AbstractMemberIcon.PROTECTED;
        }
        if (Flags.isPublic(flags)) {
            return AbstractMemberIcon.PUBLIC;
        }

        return 0;
    }
}
