package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
        testApplyRuleAtNodeWithGranularity(EIGStatusMarks.VISITED_CONTINUE,
                applyRuleToNodeVArification.withGranularity(1));
    }

    @Test
    public void testApplyRuleAtNodeWithVisitedRule() {
        testApplyRuleAtNodeWithGranularity(EIGStatusMarks.VISITED,
                applyRuleToNodeVArification.withGranularity(0));
    }

    @Test
    public void testApplyRuleAtNodeWithLocatedRule() {
        testApplyRuleAtNodeWithGranularity(EIGStatusMarks.LOCATED, assignMarkAndNodeToNodeAndParentsVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithImpactedRuleShouldDoNothing() {
        testApplyRuleAtNodeWithGranularity(EIGStatusMarks.IMPACTED, nothingIsCalledVerification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedContinueRule() {
        testApplyRuleAtNodeWithTwoNodes(EIGStatusMarks.VISITED_CONTINUE, assignMarkToNodeAndNeighborAndNeverAnnotationVerification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedRule() {
        testApplyRuleAtNodeWithTwoNodes(EIGStatusMarks.VISITED, applyRuleToNodeVArification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithLocatedRule() {
        testApplyRuleAtNodeWithTwoNodes(EIGStatusMarks.LOCATED, assignMarkAndNodeToNodeAndParentsVerification);
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithImpactedRuleShouldDoNothing() {
        testApplyRuleAtNodeWithTwoNodes(EIGStatusMarks.IMPACTED, nothingIsCalledVerification);
    }

}