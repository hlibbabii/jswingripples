/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.Mark;

import java.util.Set;

import static org.incha.core.jswingripples.eig.Mark.CHANGED;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICChangePropagation extends JRipplesICModule {

    private static final String CLASS_ELEMENT_ANNOTATION = "este atributo propaga cambios porque hay otros nodos impactados";
    private static final String CLASS_ANNOTATION = "“hay que cambiar esta clase, este metodo, porque es impactada por el concepto";
    private static final String CHANGE_PROPAGATION_ANNOTATION = "hay que cambiar esta clase, este metodo, porque";

    public JRipplesModuleICChangePropagation(final JSwingRipplesEIG eig) {
        super(eig);
    }

    @Override
    protected Set<Mark> getRulesForNullOrBlankMark() {
        return getStrictRulesForNullOrBlank();
    }

    @Override
    protected Mark getSpecificMark() {
        return CHANGED;
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
                                     Mark rule) {
        CommonEIGRules.assignAnnotationToNodeAndNeighbor(eig, nodeFrom, nodeTo, CHANGE_PROPAGATION_ANNOTATION + rule);
    }

}
	
