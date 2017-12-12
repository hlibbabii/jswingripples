/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

import java.util.Set;

import static org.incha.ui.jripples.EIGStatusMarks.Mark.BLANK;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.LOCATED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.NEXT_VISIT;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED_CONTINUE;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICDefaultConceptLocation extends JRipplesICModule {

    public JRipplesModuleICDefaultConceptLocation(final JSwingRipplesEIG eig) {
        super(eig);
    }

    @Override
    protected Set<EIGStatusMarks.Mark> getRulesForNullOrBlankMark() {
        return getStrictRulesForNullOrBlank();
    }

    @Override
    protected EIGStatusMarks.Mark getSpecificMark() {
        return LOCATED;
    }

    @Override
    public void initializeStage(JRipplesModuleRunner moduleRunner) {
        final JSwingRipplesEIGNode[] nodes = eig.getAllNodes();
        if (nodes != null) {
            for (JSwingRipplesEIGNode node : nodes) {
                node.setMark(BLANK);
            }

            if (eig.getMainClass() != null) {
                final JSwingRipplesEIGNode mainType = getMainClassNode(nodes);
                if (mainType != null) {
                    mainType.setMark(NEXT_VISIT);
                }
            }
        }
        moduleRunner.moduleFinished();
        eig.getHistory().clear();
    }


    /*
     * (non-Javadoc)
     *
     * @see org.severe.jripples.modules.interfaces.JRipplesICModule#applyRuleAtNode(java.lang.String,
     *      java.lang.String)
     */
	@Override
    public void applyRuleAtNode(final EIGStatusMarks.Mark rule, final JSwingRipplesEIGNode node, final int granularity) {
        if (VISITED_CONTINUE == rule) {
            CommonEIGRules.applyRuleToNode(eig, node, rule, granularity);
        } else {
            apply(rule, node);
        }
	}

    @Override
    public void applyRuleAtNode(final EIGStatusMarks.Mark rule, final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
        if (VISITED_CONTINUE == rule) {
            CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo, rule, NEXT_VISIT);
        } else {
            apply(rule, nodeFrom);
        }
    }

    private void apply(EIGStatusMarks.Mark rule, JSwingRipplesEIGNode node) {
        if (LOCATED == rule) {
            CommonEIGRules.assignMarkToNodeAndParents(eig, node, rule);
            CommonEIGRules.assignAnnotationToNodeAndParents(eig,node,"hay que cambiar esta clase, este método por que el concepto fue localizado");
        } else if (VISITED == rule) {
            CommonEIGRules.applyRuleToNode(eig, node,rule,0);
        }
    }

    private JSwingRipplesEIGNode getMainClassNode(final JSwingRipplesEIGNode[] nodes) {
        for (JSwingRipplesEIGNode node : nodes) {
            final IMember member = node.getNodeIMember();
            if (member instanceof IType && ((IType) member).getFullyQualifiedName().equals(eig.getMainClass())) {
                return node;
            }
        }
        return null;
    }
}
