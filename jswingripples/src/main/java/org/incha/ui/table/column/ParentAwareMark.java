package org.incha.ui.table.column;

import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.Mark;
import org.incha.core.jswingripples.rules.CommonEIGRules;

public class ParentAwareMark implements Comparable<ParentAwareMark> {
    private final Mark mark;
    private final Mark uppestParentMark;

    private ParentAwareMark(Mark mark, Mark uppestParentMark) {
        this.mark = mark;
        this.uppestParentMark = uppestParentMark;
    }

    public static ParentAwareMark create(JSwingRipplesEIGNode node) {
        return new ParentAwareMark(node.getMark(),
                CommonEIGRules.findUppestParent(node.getEig(), node).getMark());
    }

    public Mark getMark() {
        return mark;
    }

    private Mark getUppestParentMark() {
        return uppestParentMark;
    }

    @Override
    public int compareTo(ParentAwareMark o) {
        return this.uppestParentMark.compareTo(o.getUppestParentMark());
    }
}
