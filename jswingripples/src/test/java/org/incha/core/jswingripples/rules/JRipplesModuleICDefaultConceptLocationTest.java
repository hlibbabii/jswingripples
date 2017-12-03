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
import static org.mockito.Mockito.never;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonEIGRules.class)
public class JRipplesModuleICDefaultConceptLocationTest {
    @Test
    public void getAvailableRulesForMark() throws Exception {
        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(null);

        assertEquals(null, cp.getAvailableRulesForMark(null));
        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.BLANK));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.LOCATED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        )), cp.getAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.LOCATED
        )), cp.getAvailableRulesForMark(EIGStatusMarks.LOCATED));
        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.IMPACTED));
        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.LOCATED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.getAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.getAvailableRulesForMark("unknown_mark"));
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
        cp.applyRuleAtNode(EIGStatusMarks.VISITED_CONTINUE, node, 1);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(eig, node, EIGStatusMarks.VISITED_CONTINUE, 1);

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
        cp.applyRuleAtNode(EIGStatusMarks.VISITED, node, 1);

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
        cp.applyRuleAtNode(EIGStatusMarks.LOCATED, node, 1);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndParents(eig, node, EIGStatusMarks.LOCATED);

        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignAnottationToNodeAndParents(eq(eig), eq(node), anyString());
    }

    @Test
    public void testApplyRuleAtNodeWithImpactedRuleShouldDoNothing() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.IMPACTED, node, 1);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class, never());
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedContinueRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.VISITED_CONTINUE, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndNeighbor(eig, node, node2, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.NEXT_VISIT);

    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.VISITED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(eig, node, EIGStatusMarks.VISITED, 0);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithLocatedRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.LOCATED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndParents(eig, node, EIGStatusMarks.LOCATED);

        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignAnottationToNodeAndParents(eq(eig), eq(node), anyString());
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithImpactedRuleShouldDoNothing() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICDefaultConceptLocation cp
                = new JRipplesModuleICDefaultConceptLocation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.IMPACTED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class, never());
    }

}