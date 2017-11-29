package org.incha.core.jswingripples.rules;

import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;

public class JRipplesModuleICChangePropagationRelaxedTest {

    @Test
    public void testGetAvailableRulesForMark() throws Exception {
        JRipplesModuleICChangePropagationRelaxed cp
                = new JRipplesModuleICChangePropagationRelaxed(null);
        LinkedHashSet<String> setOf3 = new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        ));

        assertEquals(setOf3, cp.GetAvailableRulesForMark(null));
        assertEquals(setOf3, cp.GetAvailableRulesForMark(EIGStatusMarks.BLANK));
        assertEquals(setOf3, cp.GetAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.GetAvailableRulesForMark("unknown_mark"));
    }

}