/*
 * Created on Oct 20, 2005
 *
 */
package org.incha.core.jswingripples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.rules.CommonEIGRules;
import org.incha.core.jswingripples.rules.JRipplesModuleICChangePropagation;
import org.incha.ui.jripples.EIGStatusMarks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface of JRipples modules that provide and execute Incremental Change (IC) propagation rules for JRipplesEIG nodes.
 * @author Maksym Petrenko
 *
 */
public abstract class JRipplesICModule extends JRipplesModule {

	private static final Log log = LogFactory.getLog(JRipplesModuleICChangePropagation.class);


	protected final JSwingRipplesEIG eig;

	public JRipplesICModule(JSwingRipplesEIG eig) {
		super();
		this.eig = eig;
	}

	private final Map<String, List<String>> rulesForMarks
			= new HashMap<String, List<String>>(){{
		String specificMark = getSpecificMark();

		put(EIGStatusMarks.NEXT_VISIT, Arrays.asList(
				specificMark,
				EIGStatusMarks.VISITED_CONTINUE,
				EIGStatusMarks.VISITED
		));
		put(specificMark, Arrays.asList(
				specificMark
		));
		put(EIGStatusMarks.VISITED_CONTINUE, Arrays.asList(
				specificMark,
				EIGStatusMarks.VISITED_CONTINUE
		));
	}};

	protected abstract Set<String> getRulesForNullOrBlankMark();
	/**
	 * Returns a set of marks (names of propagation rules), available for a node with the supplied current mark. This is called to determine which propagation rules can still be applied to a particular node and display this rules in GUI.
	 * @param mark
	 *  current mark of a node
	 * @return
	 * 	a set of marks (of type String)
	 */
	public Set<String> GetAvailableRulesForMark(final String mark) {

		if (mark == null || mark.compareTo(EIGStatusMarks.BLANK) == 0) {
			return getRulesForNullOrBlankMark();
		} else {
			List<String> rules = rulesForMarks.get(mark);
			return rules != null ? new LinkedHashSet<>(rules): null;
		}
	}

	protected final Set<String> getStrictRulesForNullOrBlank() {
		return null;
	}

	protected final Set<String> getRelaxedRulesForNullOrBlankMark() {
		return (new LinkedHashSet<>(Arrays.asList(
				getSpecificMark(),
				EIGStatusMarks.VISITED_CONTINUE,
				EIGStatusMarks.VISITED))
		);
	}

	protected abstract String getSpecificMark();

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
					setClassAnnotation(nodes[i]);
				} else{
					impactedTopNodes.add(nodes[i]);
					setClassElementAnnotation(nodes[i]);
				}
			}
			//          Process members first
			for (final Iterator<JSwingRipplesEIGNode> iter = impactedMemberNodes.iterator(); iter.hasNext();) {
				final JSwingRipplesEIGNode impacted_node = iter.next();
				impacted_node.setMark(EIGStatusMarks.NEXT_VISIT);
				CommonEIGRules.applyRuleToNode(eig, impacted_node, getSpecificMark(),0);
			}

			//          Process top nodes if any
			for (final Iterator<JSwingRipplesEIGNode> iter = impactedTopNodes.iterator(); iter.hasNext();) {
				final JSwingRipplesEIGNode impacted_node = iter.next();

				if ( (impacted_node.getMark().compareTo(getSpecificMark()) != 0)) {
					impacted_node.setMark(EIGStatusMarks.NEXT_VISIT);
					CommonEIGRules.applyRuleToNode(eig, impacted_node, getSpecificMark(),0);
				}
			}

		}
		eig.getHistory().clear();
		moduleRunner.moduleFinished();
	}

    protected void setClassElementAnnotation(JSwingRipplesEIGNode node) {
		// Do nothing
    }

    protected void setClassAnnotation(JSwingRipplesEIGNode node) {
		// Do nothing
    }

	public void ApplyRuleAtNode(final String rule, final JSwingRipplesEIGNode node, final int granularity) {
		try {
			CommonEIGRules.applyRuleToNode(eig, node,rule,granularity);
		} catch (final Exception e) {
			log.error(e);
		}
	}
    /**
	 * Applies the selected propagation rule at the selected node using the particular dependency instead of the whole dependency graph.
	 * @param rule
	 * rule to apply
	 * @param nodeFrom
	 * node to apply the rule at
	 * @param nodeTo
	 * node, to which the rule propagates

	 */
	public abstract void ApplyRuleAtNode(String rule, JSwingRipplesEIGNode nodeFrom, JSwingRipplesEIGNode nodeTo);

    @Override
    public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
        InitializeStage(moduleRunner);
    }
}
