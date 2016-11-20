package org.incha.ui.classview;

import java.util.Comparator;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

public class HitsComparator implements Comparator<JSwingRipplesEIGNode> {
	
	private ClassTreeRenderer renderer;;
	
	public HitsComparator(ClassTreeRenderer renderer){		
		super();
		this.renderer = renderer;		
	}

	@Override
	public int compare(JSwingRipplesEIGNode o1, JSwingRipplesEIGNode o2) {
		Integer i1, i2;
		try{
			i1 = new Integer(renderer.searchResults(o1));
		}catch(Exception e){
			i1 = new Integer(0);
		}
		try{
			i2 = new Integer(renderer.searchResults(o2));
		}catch(Exception e){
			i2 = new Integer(0);
		}
		return i1.compareTo(i2);
	}


}
