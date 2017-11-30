/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICImpactAnalysis extends JRipplesICModule {
    private static final Log log = LogFactory.getLog(JRipplesModuleICImpactAnalysis.class);
//algorithm
//1. Identify all members or parents at specified granularity
//2. find all neighbors of these members
//3. filter them based on the specified granularity
//4. apply mark to the member and the neighbors
//5. verify bottom-up the consistency of the marks of all involved parties and in between
    private final JSwingRipplesEIG eig;
    /**
     * @param eig the eig.
     */
    public JRipplesModuleICImpactAnalysis(final JSwingRipplesEIG eig) {
        super();
        this.eig = eig;
    }

	@Override
	protected Set<String> getRulesForNullOrBlankMark() {
		return getStrictRulesForNullOrBlank();
	}

	@Override
	protected String getSpecificMark() {
		return EIGStatusMarks.IMPACTED;
	}

	@Override
	public void InitializeStage(JRipplesModuleRunner moduleRunner) {
		final JSwingRipplesEIGNode[] nodes = eig.getAllNodes();
		final Set<JSwingRipplesEIGNode> locatedMemberNodes = new LinkedHashSet<JSwingRipplesEIGNode>();
		final Set<JSwingRipplesEIGNode> locatedTopNodes = new LinkedHashSet<JSwingRipplesEIGNode>();
		if (nodes != null) {
			for (int i = 0; i < nodes.length; i++) {
				if ((nodes[i].getMark()==null) || ( (nodes[i].getMark().compareTo(EIGStatusMarks.LOCATED) != 0) && (nodes[i].getMark().compareTo(EIGStatusMarks.IMPACTED) != 0) && (nodes[i].getMark().compareTo(EIGStatusMarks.CHANGED) != 0)))
					nodes[i].setMark(EIGStatusMarks.BLANK);
				else {
					if (!nodes[i].isTop()) locatedMemberNodes.add(nodes[i]);
					else locatedTopNodes.add(nodes[i]);
				}
			}
			//Process members first
			for (final Iterator<JSwingRipplesEIGNode> iter = locatedMemberNodes.iterator(); iter.hasNext();) {
				final JSwingRipplesEIGNode located_node = iter.next();
				located_node.setMark(EIGStatusMarks.NEXT_VISIT);
				CommonEIGRules.applyRuleToNode(eig, located_node,EIGStatusMarks.IMPACTED,0);
			}

			//Process top nodes if any
			for (final Iterator<JSwingRipplesEIGNode> iter = locatedTopNodes.iterator(); iter.hasNext();) {
				final JSwingRipplesEIGNode located_node = iter.next();

				if ( (located_node.getMark().compareTo(EIGStatusMarks.IMPACTED) != 0)) {
					located_node.setMark(EIGStatusMarks.NEXT_VISIT);
					CommonEIGRules.applyRuleToNode(eig, located_node,EIGStatusMarks.IMPACTED,0);
				}
			}

		}
		eig.getHistory().clear();
		moduleRunner.moduleFinished();
	}

	@Override
    public void ApplyRuleAtNode(final String rule,  final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
        if ((rule.compareTo(EIGStatusMarks.IMPACTED) == 0) || (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0)) {
            CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo,rule,EIGStatusMarks.NEXT_VISIT);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
        }
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.severe.jripples.modules.interfaces.JRipplesICModule#ApplyRuleAtNode(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
    public void ApplyRuleAtNode(final String rule, final JSwingRipplesEIGNode node, final int granularity) {
        try {
            CommonEIGRules.applyRuleToNode(eig, node,rule,granularity);
        } catch (final Exception e) {
            log.error(e);
        }
	}

	@Override
	public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
		InitializeStage(moduleRunner);
	}
}
