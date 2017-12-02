/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;

import java.util.Set;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICChangePropagation extends JRipplesICModule {
    private static final Log log = LogFactory.getLog(JRipplesModuleICChangePropagation.class);

    private static final String CLASS_ELEMENT_ANNOTATION = "este atributo propaga cambios porque hay otros nodos impactados";
    private static final String CLASS_ANNOTATION = "“hay que cambiar esta clase, este metodo, porque es impactada por el concepto";


    /**
     * @param eig eig.
     */
    public JRipplesModuleICChangePropagation(final JSwingRipplesEIG eig) {
        super(eig);
    }

    @Override
    protected Set<String> getRulesForNullOrBlankMark() {
        return getStrictRulesForNullOrBlank();
    }

    @Override
    protected String getSpecificMark() {
        return EIGStatusMarks.CHANGED;
    }

    @Override
    protected void setClassElementAnnotation(JSwingRipplesEIGNode node) {
        node.setAnottation(JRipplesModuleICChangePropagation.CLASS_ELEMENT_ANNOTATION);
    }

    @Override
    protected void setClassAnnotation(JSwingRipplesEIGNode node) {
        node.setAnottation(JRipplesModuleICChangePropagation.CLASS_ANNOTATION);

    }

	@Override
    public void ApplyRuleAtNode(final String rule,
            final JSwingRipplesEIGNode nodeFrom, final JSwingRipplesEIGNode nodeTo) {
        if ((rule.compareTo(EIGStatusMarks.CHANGED) == 0) || (rule.compareTo(EIGStatusMarks.VISITED_CONTINUE) == 0)) {
            CommonEIGRules.assignMarkToNodeAndNeighbor(eig, nodeFrom, nodeTo,rule,EIGStatusMarks.NEXT_VISIT);
            CommonEIGRules.assignAnottationToNodeAndNeighbor(eig,nodeFrom,nodeTo,rule,"hay que cambiar esta clase, este metodo, porque" + rule);

        } else if (rule.compareTo(EIGStatusMarks.VISITED) == 0) {

            CommonEIGRules.applyRuleToNode(eig, nodeFrom,rule,0);
        }
	}

}
	
