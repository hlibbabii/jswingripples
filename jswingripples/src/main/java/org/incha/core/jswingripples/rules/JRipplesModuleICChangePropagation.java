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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICChangePropagation extends JRipplesICModule {
    private static final Log log = LogFactory.getLog(JRipplesModuleICChangePropagation.class);
    private final JSwingRipplesEIG eig;

    private static final Map<String, List<String>> rulesForMarks
            = new HashMap<String, List<String>>(){{
        put(EIGStatusMarks.NEXT_VISIT, Arrays.asList(
                EIGStatusMarks.CHANGED,
                EIGStatusMarks.VISITED_CONTINUE,
                EIGStatusMarks.VISITED
        ));
        put(EIGStatusMarks.CHANGED, Arrays.asList(
                EIGStatusMarks.CHANGED
        ));
        put(EIGStatusMarks.VISITED_CONTINUE, Arrays.asList(
                EIGStatusMarks.CHANGED,
                EIGStatusMarks.VISITED_CONTINUE
        ));
    }};


    /**
     * @param eig eig.
     */
    public JRipplesModuleICChangePropagation(final JSwingRipplesEIG eig) {
        super();
        this.eig = eig;
    }

    private boolean isNullOrBlank(String mark) {
        return mark == null || mark.compareTo(EIGStatusMarks.BLANK) == 0;
    }

    protected Set<String> getRulesForNullOrBlankMark() {
        return null;
    }
	/*
	 * (non-Javadoc)
	 *
	 * @see org.severe.jripples.modules.interfaces.JRipplesICModule#GetAvailableRulesForMark(java.lang.String)
	 */
	@Override
    public Set<String> GetAvailableRulesForMark(final String mark) {

		if (isNullOrBlank(mark)) {
			return getRulesForNullOrBlankMark();
		} else {
            List<String> rules = rulesForMarks.get(mark);
            return rules != null ? new LinkedHashSet<>(rules): null;
        }
	}

    @Override
    public void InitializeStage(JRipplesModuleRunner moduleRunner) {
        final JSwingRipplesEIGNode[] nodes = eig.getAllNodes();
        final Set<JSwingRipplesEIGNode> impactedMemberNodes = new LinkedHashSet<JSwingRipplesEIGNode>();
        final Set<JSwingRipplesEIGNode> impactedTopNodes = new LinkedHashSet<JSwingRipplesEIGNode>();
        if (nodes != null) {
            for (int i = 0; i < nodes.length; i++) {
                if ((nodes[i].getMark().compareTo(EIGStatusMarks.LOCATED) != 0) && (nodes[i].getMark().compareTo(EIGStatusMarks.IMPACTED) != 0) && (nodes[i].getMark().compareTo(EIGStatusMarks.CHANGED) != 0))
                    nodes[i].setMark(EIGStatusMarks.BLANK);
                else
                if (!nodes[i].isTop()){
                	impactedMemberNodes.add(nodes[i]);
                	nodes[i].setAnottation("“hay que cambiar esta clase, este metodo, porque es impactada por el concepto");
                }
                else{
                	impactedTopNodes.add(nodes[i]);
                	nodes[i].setAnottation("este atributo propaga cambios porque hay otros nodos impactados");
                }
            }
            //          Process members first
            for (final Iterator<JSwingRipplesEIGNode> iter = impactedMemberNodes.iterator(); iter.hasNext();) {
                final JSwingRipplesEIGNode impacted_node = iter.next();
                impacted_node.setMark(EIGStatusMarks.NEXT_VISIT);
                CommonEIGRules.applyRuleToNode(eig, impacted_node,EIGStatusMarks.CHANGED,0);
            }

            //          Process top nodes if any
            for (final Iterator<JSwingRipplesEIGNode> iter = impactedTopNodes.iterator(); iter.hasNext();) {
                final JSwingRipplesEIGNode impacted_node = iter.next();

                if ( (impacted_node.getMark().compareTo(EIGStatusMarks.CHANGED) != 0)) {
                    impacted_node.setMark(EIGStatusMarks.NEXT_VISIT);
                    CommonEIGRules.applyRuleToNode(eig, impacted_node,EIGStatusMarks.CHANGED,0);
                }
            }

        }
        eig.getHistory().clear();
        moduleRunner.moduleFinished();
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
    public void ApplyRuleAtNode(final String rule,
            final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
        if ((rule.compareTo(EIGStatusMarks.CHANGED) == 0) || (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0)) {
            CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo,rule,EIGStatusMarks.NEXT_VISIT);
            CommonEIGRules.assignAnottationToNodeAndNeighbor(eig,nodeFrom,nodeTo,rule,"hay que cambiar esta clase, este metodo, porque" + rule);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
        }
	}

    @Override
    public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
        InitializeStage(moduleRunner);
    }
}
	
