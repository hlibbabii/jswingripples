package org.incha.core.jswingripples.rules;

import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

public class JRipplesModuleICImpactAnalysisRelaxedTest {
    @Test
    public void getAvailableRulesForMark() throws Exception {
        JRipplesModuleICImpactAnalysisRelaxed cp
                = new JRipplesModuleICImpactAnalysisRelaxed(null);
        LinkedHashSet<String> setOf3 = new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        ));

        assertEquals(setOf3, cp.getAvailableRulesForMark(null));
        assertEquals(setOf3, cp.getAvailableRulesForMark(EIGStatusMarks.BLANK));
        assertEquals(setOf3, cp.getAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.LOCATED));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED
        )), cp.getAvailableRulesForMark(EIGStatusMarks.IMPACTED));
        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.getAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.getAvailableRulesForMark("unknown_mark"));
    }

}