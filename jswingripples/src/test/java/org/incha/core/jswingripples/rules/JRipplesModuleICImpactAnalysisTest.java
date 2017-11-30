package org.incha.core.jswingripples.rules;

import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

public class JRipplesModuleICImpactAnalysisTest {
    @Test
    public void getAvailableRulesForMark() throws Exception {
        JRipplesModuleICImpactAnalysis cp
                = new JRipplesModuleICImpactAnalysis(null);

        assertEquals(null, cp.GetAvailableRulesForMark(null));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.BLANK));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.LOCATED));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.IMPACTED));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.GetAvailableRulesForMark("unknown_mark"));
    }

}