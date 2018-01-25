package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.Mark;
import org.incha.core.jswingripples.eig.Status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.incha.core.jswingripples.eig.Mark.NEXT_VISIT;
import static org.incha.core.jswingripples.eig.Mark.VISITED;

public class CommonEIGRules {

	//---------------Edge Level-------------------------------------------------
	public static void assignMarkToNodeAndNeighbor(final JSwingRipplesEIG eig,
                                                   final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo, final Mark markForNode) {
		assignStateToNodeAndParents(eig, nodeFrom,markForNode, null, null);
		assignStateToNodeAndParents(eig, nodeTo, NEXT_VISIT, nodeFrom, null);
	}

	public static void assignStateToNodeAndParents(JSwingRipplesEIG eig, JSwingRipplesEIGNode node,
												   Mark mark, JSwingRipplesEIGNode propagationSource, String annotation) {
		while (node!=null) {
			JSwingRipplesEIGNode newPropagationSource = propagationSource != null ?
					propagationSource :
					node.getChangePropagationSource();

			Mark newMark = (mark != null && mark.getImportance() > node.getMark().getImportance()) ?
					mark :
					node.getMark();

			String newAnnotation = annotation != null ?
					annotation :
					node.getAnnottation();

			node.changeStatus(Status.create(newMark, newPropagationSource, newAnnotation));

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

	//=============================================Rules as of ICPC 08=================================
	public static void applyRuleToNode(final JSwingRipplesEIG eig,
                                       final JSwingRipplesEIGNode node,
                                       final Mark newMark, final int granularity) {
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
			assignStateToNodeAndParents(eig, n,newMark, null, null);

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
			assignStateToNodeAndParents(eig, n, NEXT_VISIT, node, null);
		}
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

	private static JSwingRipplesEIGNode findParent(final JSwingRipplesEIG eig, final JSwingRipplesEIGNode node,final int neededLevel) {
		int level=0;
		JSwingRipplesEIGNode uppestNotNull = node;
		JSwingRipplesEIGNode parent = eig.findParentNodeForMemberNode(uppestNotNull);
		while (level != neededLevel && parent != null) {
			uppestNotNull = parent;
			parent = eig.findParentNodeForMemberNode(uppestNotNull);
			level--;
		}
		return uppestNotNull;
	}

	public static JSwingRipplesEIGNode findUppestParent(final JSwingRipplesEIG eig, final JSwingRipplesEIGNode node) {
		return findParent(eig, node, Integer.MIN_VALUE);
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
            assignStateToNodeAndParents(eig, nodeFrom, null, null, text);
            assignStateToNodeAndParents(eig, nodeTo, null, null, text);
    }
}	