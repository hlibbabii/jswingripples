package org.incha.core.jswingripples.rules;

import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.history.History;
import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static org.incha.ui.jripples.EIGStatusMarks.Mark.BLANK;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.CHANGED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.IMPACTED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.LOCATED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.NEXT_VISIT;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED;
import static org.incha.ui.jripples.EIGStatusMarks.Mark.VISITED_CONTINUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonEIGRules.class)
public class JRipplesModuleICChangePropagationTest extends JRipplesModuleICTest {

    @Override
    protected JRipplesICModule getModuleUnderTest() {
        return new JRipplesModuleICChangePropagation(eig);
    }

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void testGetAvailableRulesForMark() throws Exception {
        assertEquals(null, cp.getAvailableRulesForMark(null));
        assertEquals(null, cp.getAvailableRulesForMark(BLANK));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                CHANGED, VISITED_CONTINUE, VISITED
        )), cp.getAvailableRulesForMark(NEXT_VISIT));

        assertEquals(null, cp.getAvailableRulesForMark(LOCATED));
        assertEquals(null, cp.getAvailableRulesForMark(IMPACTED));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                CHANGED
        )), cp.getAvailableRulesForMark(CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                CHANGED, VISITED_CONTINUE
        )), cp.getAvailableRulesForMark(VISITED_CONTINUE));
    }

    @Test
    public void testApplyRuleAtNode() {
        super.testApplyRuleAtNode();
    }

    @Test
    public void testApplyRuleAtNodeWithExceptionThrown() throws Exception {
        super.testApplyRuleAtNodeWithExceptionThrown();
    }

    @Test
    public void testInitializeStage() throws Exception {
        /* given */
        History mockedHistory = mock(History.class);

        JSwingRipplesEIG mockedEig = mock(JSwingRipplesEIG.class, RETURNS_DEEP_STUBS);
        when(mockedEig.getHistory()).thenReturn(mockedHistory);
        JSwingRipplesEIGNode[] nodes = {
                createEIGClassNodeMock(BLANK, mockedEig),
                createEIGClassNodeMock(VISITED, mockedEig),
                createEIGClassNodeMock(VISITED_CONTINUE, mockedEig),
                createEIGClassNodeMock(NEXT_VISIT, mockedEig),
                createEIGClassNodeMock(LOCATED, mockedEig),
                createEIGClassNodeMock(IMPACTED, mockedEig),
                createEIGClassNodeMock(CHANGED, mockedEig),
                createEIGMethodNodeMock(BLANK, mockedEig),
                createEIGMethodNodeMock(VISITED, mockedEig),
                createEIGMethodNodeMock(VISITED_CONTINUE, mockedEig),
                createEIGMethodNodeMock(NEXT_VISIT, mockedEig),
                createEIGMethodNodeMock(LOCATED, mockedEig),
                createEIGMethodNodeMock(IMPACTED, mockedEig),
                createEIGMethodNodeMock(CHANGED, mockedEig),
        };
        when(mockedEig.getAllNodes()).thenReturn(nodes);
        JRipplesModuleRunner mockedModuleRunner = mock(JRipplesModuleRunner.class);

        PowerMockito.mockStatic(CommonEIGRules.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(mockedEig);

        /* when */
        cp.initializeStage(mockedModuleRunner);

        /* then */

        assertNodesEquals(new EIGStatusMarks.Mark[]{
                BLANK,  // 0
                BLANK,  // 1
                BLANK,  // 2
                BLANK,  // 3
                NEXT_VISIT,  // 4
                NEXT_VISIT,  // 5
                CHANGED,  // 6
                BLANK,  // 7
                BLANK,  // 8
                BLANK,  // 9
                BLANK,  // 10
                NEXT_VISIT, // 11
                NEXT_VISIT, // 12
                NEXT_VISIT, // 13
        }, nodes);

        PowerMockito.verifyStatic(CommonEIGRules.class, times(5));
        ArgumentCaptor<JSwingRipplesEIGNode> nodeCaptor = ArgumentCaptor.forClass(JSwingRipplesEIGNode.class);
        CommonEIGRules.applyRuleToNode(
                eq(mockedEig),
                nodeCaptor.capture(), //12-14, 4, 6
                eq(CHANGED),
                eq(0)
        );
        List<JSwingRipplesEIGNode> capturedNodes = nodeCaptor.getAllValues();
        assertEquals("11", capturedNodes.get(0).getFullName());
        assertEquals("12", capturedNodes.get(1).getFullName());
        assertEquals("13", capturedNodes.get(2).getFullName());
        assertEquals("4", capturedNodes.get(3).getFullName());
        assertEquals("5", capturedNodes.get(4).getFullName());

        verify(mockedModuleRunner).moduleFinished();
        verify(mockedHistory).clear();
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithVisitedContinueRule() {
        testApplyRuleAtNodeWithTwoNodes(VISITED_CONTINUE,
                assignMarkAndAnnotationToNodeAndNeighborVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithVisitedRule() {
        testApplyRuleAtNodeWithTwoNodes(VISITED,
                applyRuleToNodeAndAnnotationToNodeAndNeighbourVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithLocatedRule() {
        testApplyRuleAtNodeWithTwoNodes(LOCATED,
                nothingIsCalledVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithImpactedRule_ShouldDoNothing() {
        testApplyRuleAtNodeWithTwoNodes(IMPACTED,
                nothingIsCalledVerification);
    }

    @Test
    public void testApplyRuleAtNodeWithTwoNodes_WithChangedRule() {
        testApplyRuleAtNodeWithTwoNodes(CHANGED,
                assignMarkAndAnnotationToNodeAndNeighborVerification);
    }
}