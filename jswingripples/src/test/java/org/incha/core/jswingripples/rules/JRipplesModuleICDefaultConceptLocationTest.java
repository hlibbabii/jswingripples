package org.incha.core.jswingripples.rules;

import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Before;
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
import static org.mockito.Mockito.never;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonEIGRules.class)
public class JRipplesModuleICDefaultConceptLocationTest extends JRipplesModuleICTest {

    private JRipplesModuleICDefaultConceptLocation cp;

    @Before
    public void setup() {
        super.setup();

        cp = new JRipplesModuleICDefaultConceptLocation(eig);
    }

    @Test
    public void getAvailableRulesForMark() throws Exception {
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
        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.LOCATED, node, 1);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndParents(eig, node, EIGStatusMarks.LOCATED);

        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignAnnotationToNodeAndParents(eq(eig), eq(node), anyString());
    }

    @Test
    public void testApplyRuleAtNodeWithImpactedRuleShouldDoNothing() {
        /* given */
        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.IMPACTED, node, 1);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class, never());
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedContinueRule() {
        /* given */
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
        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.LOCATED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndParents(eig, node, EIGStatusMarks.LOCATED);

        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignAnnotationToNodeAndParents(eq(eig), eq(node), anyString());
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithImpactedRuleShouldDoNothing() {
        /* given */
        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.IMPACTED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class, never());
    }

}