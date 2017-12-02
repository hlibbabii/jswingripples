/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

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
    /**
     * @param eig the eig.
     */
    public JRipplesModuleICImpactAnalysis(final JSwingRipplesEIG eig) {
        super(eig);
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
    public void ApplyRuleAtNode(final String rule,  final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
        if ((rule.compareTo(EIGStatusMarks.IMPACTED) == 0) || (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0)) {
            CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo,rule,EIGStatusMarks.NEXT_VISIT);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
        }
	}

}
