/*
 * Created on Oct 20, 2005
 *
 */
package org.incha.core.jswingripples;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

import java.util.Arrays;
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

	public abstract void InitializeStage(JRipplesModuleRunner moduleRunner);
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
	/**
	 * Applies the selected propagation rule at the selected node.
	 * @param rule
	 *  rule to apply
	 * @param node
	 *  node to apply the rule at
	 * @param granularity
	 * granularity at which the rule is applied
	 */
	public abstract void ApplyRuleAtNode(String rule, JSwingRipplesEIGNode node, int granularity);
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
}
