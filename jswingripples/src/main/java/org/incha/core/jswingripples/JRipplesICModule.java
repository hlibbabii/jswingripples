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

import static org.incha.ui.jripples.EIGStatusMarks.Mark.BLANK;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.CHANGED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.IMPACTED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.LOCATED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.NEXT_VISIT;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED_CONTINUE;

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

	private final Map<EIGStatusMarks.Mark, List<EIGStatusMarks.Mark>> rulesForMarks
			= new HashMap<EIGStatusMarks.Mark, List<EIGStatusMarks.Mark>>(){{
		EIGStatusMarks.Mark specificMark = getSpecificMark();

		put(NEXT_VISIT, Arrays.asList(
				specificMark,
				VISITED_CONTINUE, VISITED
		));
		put(specificMark, Collections.singletonList(
				specificMark
		));
		put(VISITED_CONTINUE, Arrays.asList(
				specificMark, VISITED_CONTINUE
		));
	}};

	protected abstract Set<EIGStatusMarks.Mark> getRulesForNullOrBlankMark();
	/**
	 * Returns a set of marks (names of propagation rules), available for a node with the supplied current mark. This is called to determine which propagation rules can still be applied to a particular node and display this rules in GUI.
	 * @param mark
	 *  current mark of a node
	 * @return
	 * 	a set of marks (of type String)
	 */
	public Set<EIGStatusMarks.Mark> getAvailableRulesForMark(final EIGStatusMarks.Mark mark) {

		if (mark == null || BLANK == mark) {
			return getRulesForNullOrBlankMark();
		} else {
			List<EIGStatusMarks.Mark> rules = rulesForMarks.get(mark);
			return rules != null ? new LinkedHashSet<>(rules): null;
		}
	}

	protected final Set<EIGStatusMarks.Mark> getStrictRulesForNullOrBlank() {
		return null;
	}

	protected final Set<EIGStatusMarks.Mark> getRelaxedRulesForNullOrBlankMark() {
		return (new LinkedHashSet<>(Arrays.asList(
				getSpecificMark(),
				VISITED_CONTINUE,
				VISITED))
		);
	}

	protected abstract EIGStatusMarks.Mark getSpecificMark();

	public void initializeStage(JRipplesModuleRunner moduleRunner) {
		final JSwingRipplesEIGNode[] nodes = eig.getAllNodes();
		final Set<JSwingRipplesEIGNode> impactedMemberNodes = new LinkedHashSet<>();
		final Set<JSwingRipplesEIGNode> impactedTopNodes = new LinkedHashSet<>();
		if (nodes != null) {
			List<EIGStatusMarks.Mark> importantMarksList = Arrays.asList(
					LOCATED, IMPACTED, CHANGED
			);
			for (JSwingRipplesEIGNode node : nodes) {
				if (!importantMarksList.contains(node.getMark()))
					node.setMark(BLANK);
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
				impacted_node.setMark(NEXT_VISIT);
				CommonEIGRules.applyRuleToNode(eig, impacted_node, getSpecificMark(), 0);
			}

			//          Process top nodes if any
			for (final JSwingRipplesEIGNode impacted_node : impactedTopNodes) {
				if (!getSpecificMark().equals(impacted_node.getMark())) {
					impacted_node.setMark(NEXT_VISIT);
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
									 EIGStatusMarks.Mark rule) {
		//Do nothing
	}

	public void applyRuleAtNode(final EIGStatusMarks.Mark rule, final JSwingRipplesEIGNode node, final int granularity) {
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
	public void applyRuleAtNode(final EIGStatusMarks.Mark rule, final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
		if (getSpecificMark() == rule || VISITED_CONTINUE == rule) {
			CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo,rule, NEXT_VISIT);
			assignAnnotations(nodeFrom, nodeTo, rule);
		} else if (VISITED == rule) {
			CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
		}
	}

    @Override
    public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
        initializeStage(moduleRunner);
    }
}
