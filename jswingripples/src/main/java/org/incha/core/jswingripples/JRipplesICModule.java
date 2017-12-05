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
import java.util.Collections;
import java.util.HashMap;
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
		put(specificMark, Collections.singletonList(
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
	public Set<String> getAvailableRulesForMark(final String mark) {

		if (mark == null || EIGStatusMarks.BLANK.equals(mark)) {
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

	public void initializeStage(JRipplesModuleRunner moduleRunner) {
		final JSwingRipplesEIGNode[] nodes = eig.getAllNodes();
		final Set<JSwingRipplesEIGNode> impactedMemberNodes = new LinkedHashSet<>();
		final Set<JSwingRipplesEIGNode> impactedTopNodes = new LinkedHashSet<>();
		if (nodes != null) {
			List<String> importantMarksList = Arrays.asList(
					EIGStatusMarks.LOCATED,
					EIGStatusMarks.IMPACTED,
					EIGStatusMarks.CHANGED
			);
			for (JSwingRipplesEIGNode node : nodes) {
				if (!importantMarksList.contains(node.getMark()))
					node.setMark(EIGStatusMarks.BLANK);
				else if (!node.isTop()) {
					impactedMemberNodes.add(node);
					setClassAnnotation(node);
				} else {
					impactedTopNodes.add(node);
					setClassElementAnnotation(node);
				}
			}
			//          Process members first
			for (final JSwingRipplesEIGNode impacted_node : impactedMemberNodes) {
				impacted_node.setMark(EIGStatusMarks.NEXT_VISIT);
				CommonEIGRules.applyRuleToNode(eig, impacted_node, getSpecificMark(), 0);
			}

			//          Process top nodes if any
			for (final JSwingRipplesEIGNode impacted_node : impactedTopNodes) {
				if (!getSpecificMark().equals(impacted_node.getMark())) {
					impacted_node.setMark(EIGStatusMarks.NEXT_VISIT);
					CommonEIGRules.applyRuleToNode(eig, impacted_node, getSpecificMark(), 0);
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

	protected void assignAnnotations(JSwingRipplesEIGNode nodeFrom, JSwingRipplesEIGNode nodeTo,
									 String rule) {
		//Do nothing
	}

	public void applyRuleAtNode(final String rule, final JSwingRipplesEIGNode node, final int granularity) {
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
	public void applyRuleAtNode(final String rule, final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
		if (getSpecificMark().equals(rule) || EIGStatusMarks.VISITED_CONTINUE.equals(rule)) {
			CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo,rule,EIGStatusMarks.NEXT_VISIT);
			assignAnnotations(nodeFrom, nodeTo, rule);
		} else if (EIGStatusMarks.VISITED.equals(rule)) {
			CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
		}
	}

    @Override
    public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
        initializeStage(moduleRunner);
    }
}
