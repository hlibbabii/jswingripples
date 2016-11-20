package org.incha.ui.classview;

import java.util.Comparator;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class MarkComparator implements Comparator<JSwingRipplesEIGNode> {

	@Override
	public int compare(JSwingRipplesEIGNode o1, JSwingRipplesEIGNode o2) {
		return o1.getMark().compareTo(o2.getMark());
	}

}
