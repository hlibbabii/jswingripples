package org.incha.ui.classview;

import java.util.Comparator;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class ProbabilityComparator implements Comparator<JSwingRipplesEIGNode> {

	@Override
	public int compare(JSwingRipplesEIGNode o1, JSwingRipplesEIGNode o2) {
		Float f1 = new Float(o1.getProbability());
		Float f2 = new Float(o2.getProbability());
		return f1.compareTo(f2);
	}

}
