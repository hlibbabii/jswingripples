/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.analysis;

import org.incha.core.ModuleConfiguration;
import org.incha.core.jswingripples.JRipplesAnalysisModule;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEdge;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGEvent;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGListener;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNodeEvent;
import org.incha.core.jswingripples.eig.Mark;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.incha.core.jswingripples.eig.Mark.CHANGED;
import static org.incha.core.jswingripples.eig.Mark.IMPACTED;
import static org.incha.core.jswingripples.eig.Mark.NEXT_VISIT;
import static org.incha.core.jswingripples.eig.Mark.VISITED_CONTINUE;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleAnalysisDefaultImpactSetConnections extends JRipplesAnalysisModule
		                                                       implements JSwingRipplesEIGListener {


	private Set<JSwingRipplesEIGNode> impact_set = new HashSet<JSwingRipplesEIGNode>();
    private final JSwingRipplesEIG eig;
	/**
     *
     */
    public JRipplesModuleAnalysisDefaultImpactSetConnections(final JSwingRipplesEIG eig) {
        super();
        this.eig = eig;
    }

	@Override
	public void AnalyzeProjectWithinModuleRunner(JRipplesModuleRunner moduleRunner) {
		if (isConceptLocationModuleUsed()) {
			return;
		}

		boolean dirty=!impact_set.isEmpty();

		impact_set.clear();

		final JSwingRipplesEIGNode[] nodes=eig.getAllNodes();
		if (nodes.length==0) return;

		for (int i=0;i<nodes.length;i++) {
			final JSwingRipplesEIGNode node = nodes[i];
			final Mark mark=node.getMark();
			if (mark == CHANGED || mark == IMPACTED) {
				if (impact_set.add(node))  dirty=true;
				if (impact_set.addAll(Arrays.asList(eig.getNodeMembers(node))))  dirty=true;
			}
		}

		if (dirty)
			calculateCouplingAndUpdateNodes();
		moduleRunner.moduleFinished();
	}
    /**
     * @return
     */
    protected boolean isConceptLocationModuleUsed() {
        return eig.getJavaProject().getModuleConfiguration().getIncrementalChange()
		        == ModuleConfiguration.AnalysisModule.MODULE_CONCEPT_LOCATION;
    }

	@Override
    public void jRipplesEIGChanged(final JSwingRipplesEIGEvent event) {
		if (isConceptLocationModuleUsed())
			return;

		if (!event.hasNodeEvents()) return;

		boolean dirty=false;

		final JSwingRipplesEIGNodeEvent[] nodeEvents=event.getNodeTypedEvents(
				new int[] {JSwingRipplesEIGNodeEvent.NODE_ADDED,JSwingRipplesEIGNodeEvent.NODE_REMOVED,JSwingRipplesEIGNodeEvent.NODE_STATUS_CHANGED});
		if (nodeEvents.length==0) return;

        for (int i=0;i<nodeEvents.length;i++) {
			final JSwingRipplesEIGNode changedNode = nodeEvents[i].getSource();
			switch (nodeEvents[i].getEventType()) {
				case JSwingRipplesEIGNodeEvent.NODE_REMOVED: {
					if (impact_set.remove(changedNode)) dirty=true;
					if (impact_set.removeAll(Arrays.asList(eig.getNodeMembers(changedNode)))) dirty=true;
					break;
				}
				case JSwingRipplesEIGNodeEvent.NODE_ADDED: {
					if (impact_set.add(changedNode))  dirty=true;
					if (impact_set.addAll(Arrays.asList(eig.getNodeMembers(changedNode))))  dirty=true;
					break;
				}
				case JSwingRipplesEIGNodeEvent.NODE_STATUS_CHANGED: {
					final Mark mark=changedNode.getMark();
					if (mark == CHANGED || mark == IMPACTED) {
							if (impact_set.add(changedNode))  dirty=true;
							if (impact_set.addAll(Arrays.asList(eig.getNodeMembers(changedNode))))  dirty=true;
					} else {
						if (impact_set.remove(changedNode))  dirty=true;
						if (impact_set.removeAll(Arrays.asList(eig.getNodeMembers(changedNode))))  dirty=true;
						if (mark == NEXT_VISIT || mark == VISITED_CONTINUE)
							dirty=true;
					}
					break;
				}
			}
		}
		if (dirty)
			calculateCouplingAndUpdateNodes();
	}

	private void calculateCouplingAndUpdateNodes() {


		final JSwingRipplesEIGNode[] nodeArr=eig.getAllNodes();
		final JSwingRipplesEIGEdge[] edgesArr=eig.getAllEdges();

		final HashMap<JSwingRipplesEIGNode,Integer> nodes=new HashMap<JSwingRipplesEIGNode,Integer>();

		for (int i=0;i<nodeArr.length;i++) {
			nodes.put(nodeArr[i], Integer.valueOf(0));
		}

		for (int i=0;i<edgesArr.length;i++) {
			final JSwingRipplesEIGNode nodeFrom=edgesArr[i].getFromNode();
			final JSwingRipplesEIGNode nodeTo=edgesArr[i].getToNode();

			if ((impact_set.contains(nodeFrom))) {
				nodes.put(nodeTo, Integer.valueOf(nodes.get(nodeTo).intValue()+1) );
				if (!nodeTo.isTop()) {
					final JSwingRipplesEIGNode topNode=eig.findTopNodeForMemberNode(nodeTo);
					if (topNode!=null)
					nodes.put(topNode, Integer.valueOf(nodes.get(topNode).intValue()+1));
				}
			}

			if ((impact_set.contains(nodeTo))) {
				nodes.put(nodeFrom, Integer.valueOf(nodes.get(nodeFrom).intValue()+1) );
				if (!nodeFrom.isTop()) {
					final JSwingRipplesEIGNode topNode=eig.findTopNodeForMemberNode(nodeFrom);
					if (topNode!=null)
					nodes.put(topNode, Integer.valueOf(nodes.get(topNode).intValue()+1) );
				}
			}
		}

		for (final Iterator<JSwingRipplesEIGNode> iter=nodes.keySet().iterator();iter.hasNext();){
			final JSwingRipplesEIGNode node=iter.next();
			updateNodeProbability(node, nodes.get(node).toString());
		}
	}


	private void updateNodeProbability(final JSwingRipplesEIGNode node, String newProbability) {
		if (newProbability==null) newProbability="";

		if (node.getMark()==null) {
			newProbability="";
		} else if (!(node.getMark() == NEXT_VISIT
				|| node.getMark() == VISITED_CONTINUE)) {
			newProbability="";
		}

		String oldProbability=node.getProbability();
		if (oldProbability==null) oldProbability="";
		if (oldProbability.compareTo(newProbability)!=0) node.setProbability(newProbability);
	}

	@Override
	public void runModuleWithinRunner(JRipplesModuleRunner moduleRunner) {
		AnalyzeProjectWithinModuleRunner(moduleRunner);
	}
}
