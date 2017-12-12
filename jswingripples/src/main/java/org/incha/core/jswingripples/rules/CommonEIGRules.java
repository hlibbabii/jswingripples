package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.incha.ui.jripples.EIGStatusMarks.Mark.BLANK;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.CHANGED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.IMPACTED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.LOCATED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.NEXT_VISIT;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED_CONTINUE;

public class CommonEIGRules {

	//---------------Edge Level-------------------------------------------------
	public static void assignMarkToNodeAndNeighbor(final JSwingRipplesEIG eig,
												   final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo, final EIGStatusMarks.Mark markForNode, final EIGStatusMarks.Mark markForNeighbors) {
		assignMarkToNodeAndParents(eig, nodeFrom,markForNode);
		assignMarkToNodeAndParents(eig, nodeTo, markForNeighbors);
	}

	//=============================================Rules as of ICPC 08=================================
	public static void applyRuleToNode(final JSwingRipplesEIG eig,
									   final JSwingRipplesEIGNode node,
									   final EIGStatusMarks.Mark newMark, final int granularity) {
		//algorithm
		//1. Identify all members or parents at specified granularity
		//2. find all neighbors of these members
		//3. filter them based on the specified granularity
		//4. apply mark to the member and the neighbors
		//5. verify bottom-up the consistency of the marks of all involved parties and in between
		final Set<JSwingRipplesEIGNode> targets= new HashSet<>();
		if (granularity==0) {
			targets.add(node);
		} else if (granularity<0) {
			targets.add(findParent(eig, node,granularity));
		} else {
			findMembers(eig, node,targets,granularity,0);
		}

		for (final JSwingRipplesEIGNode n:targets) {
			assignMarkToNodeAndParents(eig, n,newMark);

		}
		if (VISITED == newMark) {
			return;
		}

		final Set<JSwingRipplesEIGNode> membersOfTargets= new HashSet<>(targets);
		final Set<JSwingRipplesEIGNode> membersOfTargetsAux= new HashSet<>(targets);
		while (membersOfTargetsAux.size()>0) {
			final JSwingRipplesEIGNode n=membersOfTargetsAux.iterator().next();
			membersOfTargetsAux.remove(n);
			membersOfTargetsAux.addAll(Arrays.asList(eig.getNodeMembers(n)));
			membersOfTargets.addAll(Arrays.asList(eig.getNodeMembers(n)));
		}


		final HashSet<JSwingRipplesEIGNode> neighbors=eig.edgesToNeigbors(
		        membersOfTargets,
		        JSwingRipplesEIG.DIRECTION_CONSIDERED_BOTH_CALLING_AND_CALLED,
		        JSwingRipplesEIG.NESTING_CONSIDERED_BOTH_TOP_AND_MEMBER_NODES);
		filterNeighbors(eig, targets.iterator().next(), neighbors);
		for (final JSwingRipplesEIGNode n:neighbors) {
			assignMarkToNodeAndParents(eig, n, NEXT_VISIT);
		}
	}

	public static void assignMarkToNodeAndParents(final JSwingRipplesEIG eig, JSwingRipplesEIGNode node,
												  final EIGStatusMarks.Mark mark) {

		while (node!=null) {
			if (getImportance(mark)>getImportance(node.getMark())) {
				node.setMark(mark);
			}
			if (node.isTop()) {
				return;
			}
			//comment below will lead JRipples halt, but why?
			node=eig.findParentNodeForMemberNode(node);
			if (node==null) {
				return;
			}
		}
	}



	private static int getImportance(final EIGStatusMarks.Mark mark) {
			if (mark==null) return 0;
			if (mark == BLANK) return 1;
			if (mark == VISITED) return 3;
			if (mark == NEXT_VISIT) return 2;
			if (mark == VISITED_CONTINUE) return 4;
			if (mark == LOCATED) return 5;
			if (mark == IMPACTED) return 6;
			if (mark == CHANGED) return 7;


			return 0;

	}

	private static void filterNeighbors(final JSwingRipplesEIG eig,
	        final JSwingRipplesEIGNode focusNode,final Set<JSwingRipplesEIGNode> neighbors) {
		final Set<JSwingRipplesEIGNode> filteredNeighbors= new HashSet<>();
		final int granularity=checkNestingLevel(eig, eig.findTopNodeForIMember(focusNode.getNodeIMember()),focusNode);

		for (final JSwingRipplesEIGNode neighbor:neighbors) {
			if (checkNestingLevel(eig, eig.findTopNodeForIMember(neighbor.getNodeIMember()),neighbor)==granularity)
				filteredNeighbors.add(neighbor);
		}
		neighbors.clear();
		neighbors.addAll(filteredNeighbors);

	}
	private static int checkNestingLevel(final JSwingRipplesEIG eig,
	        final JSwingRipplesEIGNode currentNode, final JSwingRipplesEIGNode neededNode) {
		if (currentNode.equals(neededNode)) {
			return 0;
		}
		for (final JSwingRipplesEIGNode member:eig.getNodeMembers(currentNode)) {
			final int k=checkNestingLevel(eig, member, neededNode);
			if (k!=-1) {
				return k+1;
			}
		}
		return -1;
	}

	private static JSwingRipplesEIGNode findParent(final JSwingRipplesEIG eig, final JSwingRipplesEIGNode n,final int neededLevel) {
		int level=0;
		JSwingRipplesEIGNode node=n;
		while (level!=neededLevel) {
			node=eig.findParentNodeForMemberNode(node);
			level--;
		}
		return node;
	}



	private static void findMembers(final JSwingRipplesEIG eig, final JSwingRipplesEIGNode node, final Set <JSwingRipplesEIGNode> members, final int neededLevel, final int currentLevel) {
		if (neededLevel==currentLevel) {
			members.add(node);
			return;
		}
		for (final JSwingRipplesEIGNode member: eig.getNodeMembers(node)) {
			findMembers(eig, member, members,neededLevel, currentLevel+1);
		}

	}

	public static void assignAnnotationToNodeAndNeighbor(final JSwingRipplesEIG eig, final JSwingRipplesEIGNode nodeFrom,
														 final JSwingRipplesEIGNode nodeTo, String text) {
		 {
			assignAnnotationToNodeAndParents(eig, nodeFrom, text);
			assignAnnotationToNodeAndParents(eig, nodeTo, text);
		
		 }
	}

	public static void assignAnnotationToNodeAndParents(final JSwingRipplesEIG eig, JSwingRipplesEIGNode node,
														final String text) {
		while (node!=null) {
			node.setAnottation(text);
			if (node.isTop()) {
				return;
			}
			//comment below will lead JRipples halt, but why?
			node=eig.findParentNodeForMemberNode(node);
			if (node==null) {
				return;
			}
		}
		
	}
}	