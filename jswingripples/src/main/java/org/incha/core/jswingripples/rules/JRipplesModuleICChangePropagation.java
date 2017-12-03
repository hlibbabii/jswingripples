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
    private static final String CHANGE_PROPAGATION_ANNOTATION = "hay que cambiar esta clase, este metodo, porque";


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
    protected void assignAnnotations(JSwingRipplesEIGNode nodeFrom, JSwingRipplesEIGNode nodeTo,
                                     String rule) {
        CommonEIGRules.assignAnottationToNodeAndNeighbor(eig,nodeFrom,nodeTo,rule, CHANGE_PROPAGATION_ANNOTATION + rule);
    }

}
	
