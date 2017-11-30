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

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICDefaultConceptLocation extends JRipplesICModule {
	private final JSwingRipplesEIG eig;

	/**
     * @param eig the eig.
     */
    public JRipplesModuleICDefaultConceptLocation(final JSwingRipplesEIG eig) {
        super();
        this.eig = eig;
    }

    @Override
    protected Set<String> getRulesForNullOrBlankMark() {
        return getStrictRulesForNullOrBlank();
    }

    @Override
    protected String getSpecificMark() {
        return EIGStatusMarks.LOCATED;
    }

    @Override
    public void InitializeStage(JRipplesModuleRunner moduleRunner) {
        final JSwingRipplesEIGNode[] nodes = eig.getAllNodes();
        if (nodes != null) {
            for (int i = 0; i < nodes.length; i++) {
                nodes[i].setMark(EIGStatusMarks.BLANK);
            }

            if (eig.getMainClass() != null) {
                final JSwingRipplesEIGNode mainType = getMainClassNode(nodes);
                if (mainType != null) {
                    mainType.setMark(EIGStatusMarks.NEXT_VISIT);
                }
            }
        }
        moduleRunner.moduleFinished();
        eig.getHistory().clear();
    }


    /*
     * (non-Javadoc)
     *
     * @see org.severe.jripples.modules.interfaces.JRipplesICModule#ApplyRuleAtNode(java.lang.String,
     *      java.lang.String)
     */
	@Override
    public void ApplyRuleAtNode(final String rule, final JSwingRipplesEIGNode node, final int granularity) {
        if (rule.compareTo(EIGStatusMarks.LOCATED) == 0) {
            CommonEIGRules.assignMarkToNodeAndParents(eig, node,EIGStatusMarks.LOCATED);
            CommonEIGRules.assignAnottationToNodeAndParents(eig, node,"hay que cambiar esta clase, este metodo por que el concepto ha sido localizado");
        } else if (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0) {
            CommonEIGRules.applyRuleToNode(eig, node,rule,granularity);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, node,rule,0);
        }

	}

	@Override
    public void ApplyRuleAtNode(final String rule, final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
        if (rule.compareTo(EIGStatusMarks.LOCATED) == 0) {
            CommonEIGRules.assignMarkToNodeAndParents(eig, nodeFrom,EIGStatusMarks.LOCATED);
            CommonEIGRules.assignAnottationToNodeAndParents(eig,nodeFrom,"hay que cambiar esta clase, este método por que el concepto fue localizado");

        } else if (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0) {
            CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo, rule,EIGStatusMarks.NEXT_VISIT);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
        }
	}

    @Override
    public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
        InitializeStage(moduleRunner);
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
