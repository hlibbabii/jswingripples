package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonEIGRules.class)
public class JRipplesModuleICDefaultConceptLocationTest {
    @Test
    public void getAvailableRulesForMark() throws Exception {
        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(null);

        assertEquals(null, cp.GetAvailableRulesForMark(null));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.BLANK));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.LOCATED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.LOCATED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.LOCATED));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.IMPACTED));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.LOCATED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.GetAvailableRulesForMark("unknown_mark"));
    }

    @Test
    public void testApplyRuleAtNodeWithVisitedContinueRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.ApplyRuleAtNode(EIGStatusMarks.VISITED_CONTINUE, node, 0);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(eig, node, EIGStatusMarks.VISITED_CONTINUE, 0);

    }

    @Test
    public void testApplyRuleAtNodeWithVisitedRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.ApplyRuleAtNode(EIGStatusMarks.VISITED, node, 0);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(eig, node, EIGStatusMarks.VISITED, 0);
    }

    @Test
    public void testApplyRuleAtNodeWithLocatedRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.ApplyRuleAtNode(EIGStatusMarks.LOCATED, node, 0);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndParents(eig, node, EIGStatusMarks.LOCATED);

        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignAnottationToNodeAndParents(eq(eig), eq(node), anyString());
    }

}