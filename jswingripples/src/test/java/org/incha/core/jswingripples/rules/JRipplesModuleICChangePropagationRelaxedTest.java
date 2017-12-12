package org.incha.core.jswingripples.rules;

import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.incha.ui.jripples.EIGStatusMarks.Mark.BLANK;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.CHANGED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.IMPACTED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.LOCATED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.NEXT_VISIT;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED_CONTINUE;
import static org.junit.Assert.assertEquals;

public class JRipplesModuleICChangePropagationRelaxedTest {

    @Test
    public void testGetAvailableRulesForMark() throws Exception {
        JRipplesModuleICChangePropagationRelaxed cp
                = new JRipplesModuleICChangePropagationRelaxed(null);
        LinkedHashSet<EIGStatusMarks.Mark> setOf3 = new LinkedHashSet<>(Arrays.asList(
                CHANGED, VISITED_CONTINUE, VISITED
        ));

        assertEquals(setOf3, cp.getAvailableRulesForMark(null));
        assertEquals(setOf3, cp.getAvailableRulesForMark(BLANK));
        assertEquals(setOf3, cp.getAvailableRulesForMark(NEXT_VISIT));

        assertEquals(null, cp.getAvailableRulesForMark(LOCATED));
        assertEquals(null, cp.getAvailableRulesForMark(IMPACTED));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                CHANGED
        )), cp.getAvailableRulesForMark(CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                CHANGED, VISITED_CONTINUE
        )), cp.getAvailableRulesForMark(VISITED_CONTINUE));
    }

}