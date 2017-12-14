/*
 * Created on Dec 4, 2005
 *
 */
package org.incha.core.jswingripples.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.Mark;

import java.util.Set;

import static org.incha.core.jswingripples.eig.Mark.IMPACTED;

/**
 * @author Maksym Petrenko
 *
 */
public class JRipplesModuleICImpactAnalysis extends JRipplesICModule {
    private static final Log log = LogFactory.getLog(JRipplesModuleICImpactAnalysis.class);

    public JRipplesModuleICImpactAnalysis(final JSwingRipplesEIG eig) {
        super(eig);
    }

	@Override
	protected Set<Mark> getRulesForNullOrBlankMark() {
		return getStrictRulesForNullOrBlank();
	}

	@Override
	protected Mark getSpecificMark() {
		return IMPACTED;
	}
}
