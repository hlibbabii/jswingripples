package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.JRipplesICModule;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonEIGRules.class)
public class JRipplesModuleICDefaultConceptLocationTest extends JRipplesModuleICTest {

    @Override
    protected JRipplesICModule getModuleUnderTest() {
        return new JRipplesModuleICDefaultConceptLocation(eig);
    }

    @Before
    public void setup() {
        super.setup();
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
        testApplyRuleAtNodeWith4Params(EIGStatusMarks.LOCATED, assignMarkAndNodeToNodeAndParentsVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithImpactedRuleShouldDoNothing() {
        testApplyRuleAtNodeWith4Params(EIGStatusMarks.IMPACTED, nothingIsCalledVerification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedContinueRule() {
        testApplyRuleAtNodeWith4Params(EIGStatusMarks.VISITED_CONTINUE, assignMarkToNodeAndNeighborAndNeverAnnotationVerification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedRule() {
        testApplyRuleAtNodeWith4Params(EIGStatusMarks.VISITED, applyRuleToNodeVArification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithLocatedRule() {
        testApplyRuleAtNodeWith4Params(EIGStatusMarks.LOCATED, assignMarkAndNodeToNodeAndParentsVerification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithImpactedRuleShouldDoNothing() {
        testApplyRuleAtNodeWith4Params(EIGStatusMarks.IMPACTED, nothingIsCalledVerification);
    }

}