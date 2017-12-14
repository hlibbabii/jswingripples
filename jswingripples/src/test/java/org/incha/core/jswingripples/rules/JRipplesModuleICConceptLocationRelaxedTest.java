package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.eig.Mark;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.incha.core.jswingripples.eig.Mark.BLANK;
import static org.incha.core.jswingripples.eig.Mark.CHANGED;
import static org.incha.core.jswingripples.eig.Mark.IMPACTED;
import static org.incha.core.jswingripples.eig.Mark.LOCATED;
import static org.incha.core.jswingripples.eig.Mark.NEXT_VISIT;
import static org.incha.core.jswingripples.eig.Mark.VISITED;
import static org.incha.core.jswingripples.eig.Mark.VISITED_CONTINUE;
import static org.junit.Assert.assertEquals;

public class JRipplesModuleICConceptLocationRelaxedTest {
    @Test
    public void getAvailableRulesForMark() throws Exception {
        JRipplesModuleICConceptLocationRelaxed cp
                = new JRipplesModuleICConceptLocationRelaxed(null);
        LinkedHashSet<Mark> setOf3 = new LinkedHashSet<>(Arrays.asList(
                LOCATED, VISITED_CONTINUE, VISITED
        ));

        assertEquals(setOf3, cp.getAvailableRulesForMark(null));
        assertEquals(setOf3, cp.getAvailableRulesForMark(BLANK));
        assertEquals(setOf3, cp.getAvailableRulesForMark(NEXT_VISIT));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                LOCATED
        )), cp.getAvailableRulesForMark(LOCATED));
        assertEquals(null, cp.getAvailableRulesForMark(IMPACTED));
        assertEquals(null, cp.getAvailableRulesForMark(CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                LOCATED, VISITED_CONTINUE
        )), cp.getAvailableRulesForMark(VISITED_CONTINUE));
    }

}