package org.incha.core.jswingripples.rules;

import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

public class JRipplesModuleICChangePropagationTest {
    @Test
    public void testGetAvailableRulesForMark() throws Exception {
        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(null);

        assertEquals(null, cp.GetAvailableRulesForMark(null));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.BLANK));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.LOCATED));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.GetAvailableRulesForMark("unknown_mark"));
    }

}