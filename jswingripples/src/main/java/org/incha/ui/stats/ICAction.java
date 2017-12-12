package org.incha.ui.stats;

import org.eclipse.jdt.core.IJavaElement;
import org.incha.compiler.dom.JavaDomUtils;
import org.incha.core.ModuleConfiguration;
import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.history.CompoundUndoAction;
import org.incha.core.jswingripples.eig.history.UndoAction;
import org.incha.ui.jripples.EIGStatusMarks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static org.incha.ui.jripples.EIGStatusMarks.Mark.BLANK;

class ICAction extends AbstractAction {
    private static final long serialVersionUID = -300064190936944986L;

    private EIGStatusMarks.Mark rule;
    private boolean displayAsRule;
    private final JSwingRipplesEIGNode fromNode;
    private final JSwingRipplesEIGNode toNode;
    private int granularity = 0;
    private final Container context;

    public ICAction(final JSwingRipplesEIGNode fromNode, final EIGStatusMarks.Mark rule,
            final int granularity, final boolean displayAsRule, final Container context) {
        this(fromNode, null, rule, displayAsRule, context);
        this.granularity = granularity;
    }
    public ICAction(final JSwingRipplesEIGNode fromNode, final JSwingRipplesEIGNode toNode,
                    final EIGStatusMarks.Mark rule, final boolean displayAsRule, final Container context) {
        this.rule = rule;
        this.displayAsRule=displayAsRule;
        this.fromNode=fromNode;
        this.toNode=toNode;
        this.context = context;
        setText();
    }

    private void setText() {
        String ruleToDisplay = BLANK.getValue();
        if (displayAsRule) {
            ruleToDisplay = rule.getValue();
        } else {
            if ((fromNode != null) && (toNode == null)) {
                if (fromNode.getNodeIMember() instanceof IJavaElement) {
                    ruleToDisplay = this.fromNode.getNodeIMember()
                            .getElementName();
                }
            } else if ((fromNode != null) && (toNode != null)) {
                ruleToDisplay = JavaDomUtils.getTopDeclaringType(this.toNode.getNodeIMember())
                        .getElementName() + "." + this.toNode.getNodeIMember().getElementName();
            } else {
                ruleToDisplay = rule.getValue();
            }
        }
        this.setText(ruleToDisplay);
    }

    /**
     * @param text action text.
     */
    private void setText(final String text) {
        putValue(Action.NAME, text);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (fromNode != null) {
            final ModuleConfiguration cfg = fromNode.getEig().getJavaProject().getModuleConfiguration();
            final JRipplesICModule module = cfg.createIncrementalChangeModule(fromNode.getEig());

            final UndoActionsBuilder undoCreater = new UndoActionsBuilder();
            fromNode.getEig().addJRipplesEIGListener(undoCreater);

            try {
                if (toNode == null) {
                    module.applyRuleAtNode(rule, fromNode, granularity);
                } else if (toNode != null) {
                    module.applyRuleAtNode(rule, fromNode, toNode);
                }
                if (context != null) {
                    context.repaint();
                }
            } finally {
                fromNode.getEig().removeJRipplesEIGListener(undoCreater);
            }

            //add changes to undo history
            final CompoundUndoAction undo = new CompoundUndoAction();
                final List<UndoAction> a = undoCreater.getActions();
                if (a.size() > 0) {
                final UndoAction[] actions = a.toArray(
                        new UndoAction[a.size()]);
                for (int i = actions.length - 1; i >= 0; i--) {
                    undo.add(actions[i]);
                }

                fromNode.getEig().getHistory().add(undo);
            }
        }
    }
}
