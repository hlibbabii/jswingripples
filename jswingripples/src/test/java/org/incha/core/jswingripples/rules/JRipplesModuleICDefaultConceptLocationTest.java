package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.JRipplesICModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.incha.core.jswingripples.eig.Mark.BLANK;
import static org.incha.core.jswingripples.eig.Mark.CHANGED;
import static org.incha.core.jswingripples.eig.Mark.IMPACTED;
import static org.incha.core.jswingripples.eig.Mark.LOCATED;
import static org.incha.core.jswingripples.eig.Mark.NEXT_VISIT;
import static org.incha.core.jswingripples.eig.Mark.VISITED;
import static org.incha.core.jswingripples.eig.Mark.VISITED_CONTINUE;
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
        assertEquals(null, cp.getAvailableRulesForMark(BLANK));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                LOCATED, VISITED_CONTINUE, VISITED
        )), cp.getAvailableRulesForMark(NEXT_VISIT));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                LOCATED
        )), cp.getAvailableRulesForMark(LOCATED));
        assertEquals(null, cp.getAvailableRulesForMark(IMPACTED));
        assertEquals(null, cp.getAvailableRulesForMark(CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                LOCATED, VISITED_CONTINUE
        )), cp.getAvailableRulesForMark(VISITED_CONTINUE));
    }

    @Test
    public void testApplyRuleAtNodeWithVisitedContinueRule() {
        testApplyRuleAtNodeWithGranularity(VISITED_CONTINUE,
                applyRuleToNodeVerification.withGranularity(1));
    }

    @Test
    public void testApplyRuleAtNodeWithVisitedRule() {
        testApplyRuleAtNodeWithGranularity(VISITED,
                applyRuleToNodeVerification.withGranularity(0));
    }

    @Test
    public void testApplyRuleAtNodeWithLocatedRule() {
        testApplyRuleAtNodeWithGranularity(LOCATED,
                assignMarkAndNodeToNodeAndParentsVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithImpactedRuleShouldDoNothing() {
        testApplyRuleAtNodeWithGranularity(IMPACTED,
                nothingIsCalledVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithVisitedContinueRule() {
        testApplyRuleAtNodeWithTwoNodes(VISITED_CONTINUE,
                assignMarkToNodeAndNeighborAndNeverAnnotationVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithVisitedRule() {
        testApplyRuleAtNodeWithTwoNodes(VISITED,
                applyRuleToNodeVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithLocatedRule() {
        testApplyRuleAtNodeWithTwoNodes(LOCATED,
                assignMarkAndNodeToNodeAndParentsVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithImpactedRule_ShouldDoNothing() {
        testApplyRuleAtNodeWithTwoNodes(IMPACTED,
                nothingIsCalledVerification);
    }

}