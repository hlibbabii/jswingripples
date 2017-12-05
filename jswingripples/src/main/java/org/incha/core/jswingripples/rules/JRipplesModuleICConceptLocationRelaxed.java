/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;

import java.util.Set;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICConceptLocationRelaxed extends JRipplesModuleICDefaultConceptLocation{

    public JRipplesModuleICConceptLocationRelaxed(final JSwingRipplesEIG eig) {
        super(eig);
    }

	@Override
	protected Set<String> getRulesForNullOrBlankMark() {
		return getRelaxedRulesForNullOrBlankMark();
	}
}
