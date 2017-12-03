package org.incha.core.jswingripples.rules;

import org.apache.commons.logging.Log;
import org.incha.TestUtils;
import org.incha.core.jswingripples.JRipplesModuleRunner;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.core.jswingripples.eig.history.History;
import org.incha.ui.jripples.EIGStatusMarks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonEIGRules.class)
public class JRipplesModuleICChangePropagationTest extends JRipplesModuleICTest {

    @Before
    public void setup() {
        counter = new AtomicInteger(0);
    }

    @Test
    public void testGetAvailableRulesForMark() throws Exception {
        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(null);

        assertEquals(null, cp.getAvailableRulesForMark(null));
        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.BLANK));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        )), cp.getAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.LOCATED));
        assertEquals(null, cp.getAvailableRulesForMark(EIGStatusMarks.IMPACTED));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED
        )), cp.getAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.CHANGED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.getAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.getAvailableRulesForMark("unknown_mark"));
    }

    @Test
    public void testApplyRuleAtNode() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);
        PowerMockito.doNothing().when(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(
                Matchers.<JSwingRipplesEIG>any(),
                Matchers.<JSwingRipplesEIGNode>any(),
                Matchers.<String>any(),
                Matchers.anyInt()
        );

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.IMPACTED, node, 0);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(eig, node, EIGStatusMarks.IMPACTED, 0);

    }

    @Test
    public void testApplyRuleAtNodeWithExceptionThrown() throws Exception {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        Exception toBeThrown = new RuntimeException();
        Log log = mock(Log.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(eig);
        TestUtils.setFinalFieldOfParent(cp, log);


        PowerMockito.mockStatic(CommonEIGRules.class);
        PowerMockito.doThrow(toBeThrown)
                .when(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(
                Matchers.<JSwingRipplesEIG>any(),
                Matchers.<JSwingRipplesEIGNode>any(),
                Matchers.<String>any(),
                Matchers.anyInt()
        );

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.IMPACTED, node, 0);

        /* then*/
        verify(log).error(toBeThrown);
    }

    @Test
    public void testInitializeStage() throws Exception {
        /* given */
        History mockedHistory = mock(History.class);

        JSwingRipplesEIG mockedEig = mock(JSwingRipplesEIG.class, RETURNS_DEEP_STUBS);
        when(mockedEig.getHistory()).thenReturn(mockedHistory);
        JSwingRipplesEIGNode[] nodes = {
                createEIGClassNodeMock(EIGStatusMarks.BLANK, mockedEig),
                createEIGClassNodeMock(EIGStatusMarks.VISITED, mockedEig),
                createEIGClassNodeMock(EIGStatusMarks.VISITED_CONTINUE, mockedEig),
                createEIGClassNodeMock(EIGStatusMarks.NEXT_VISIT, mockedEig),
                createEIGClassNodeMock(EIGStatusMarks.LOCATED, mockedEig),
                createEIGClassNodeMock(EIGStatusMarks.IMPACTED, mockedEig),
                createEIGClassNodeMock(EIGStatusMarks.CHANGED, mockedEig),
                createEIGClassNodeMock("wrong mark", mockedEig),
                createEIGMethodNodeMock(EIGStatusMarks.BLANK, mockedEig),
                createEIGMethodNodeMock(EIGStatusMarks.VISITED, mockedEig),
                createEIGMethodNodeMock(EIGStatusMarks.VISITED_CONTINUE, mockedEig),
                createEIGMethodNodeMock(EIGStatusMarks.NEXT_VISIT, mockedEig),
                createEIGMethodNodeMock(EIGStatusMarks.LOCATED, mockedEig),
                createEIGMethodNodeMock(EIGStatusMarks.IMPACTED, mockedEig),
                createEIGMethodNodeMock(EIGStatusMarks.CHANGED, mockedEig),
                createEIGMethodNodeMock("wrong mark", mockedEig),
        };
        when(mockedEig.getAllNodes()).thenReturn(nodes);
        JRipplesModuleRunner mockedModuleRunner = mock(JRipplesModuleRunner.class);

        PowerMockito.mockStatic(CommonEIGRules.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(mockedEig);

        /* when */
        cp.initializeStage(mockedModuleRunner);

        /* then */

        assertNodesEquals(new String[]{
                EIGStatusMarks.BLANK,  // 0
                EIGStatusMarks.BLANK,  // 1
                EIGStatusMarks.BLANK,  // 2
                EIGStatusMarks.BLANK,  // 3
                EIGStatusMarks.NEXT_VISIT,  // 4
                EIGStatusMarks.NEXT_VISIT,  // 5
                EIGStatusMarks.CHANGED,  // 6
                EIGStatusMarks.BLANK,  // 7
                EIGStatusMarks.BLANK,  // 8
                EIGStatusMarks.BLANK,  // 9
                EIGStatusMarks.BLANK,  // 10
                EIGStatusMarks.BLANK,  // 11
                EIGStatusMarks.NEXT_VISIT, // 12
                EIGStatusMarks.NEXT_VISIT, // 13
                EIGStatusMarks.NEXT_VISIT, // 14
                EIGStatusMarks.BLANK, // 15
        }, nodes);

        PowerMockito.verifyStatic(CommonEIGRules.class, times(5));
        ArgumentCaptor<JSwingRipplesEIGNode> nodeCaptor = ArgumentCaptor.forClass(JSwingRipplesEIGNode.class);
        CommonEIGRules.applyRuleToNode(
                eq(mockedEig),
                nodeCaptor.capture(), //12-14, 4, 6
                Matchers.matches("Changed"),
                eq(0)
        );
        List<JSwingRipplesEIGNode> capturedNodes = nodeCaptor.getAllValues();
        assertEquals("12", capturedNodes.get(0).getFullName());
        assertEquals("13", capturedNodes.get(1).getFullName());
        assertEquals("14", capturedNodes.get(2).getFullName());
        assertEquals("4", capturedNodes.get(3).getFullName());
        assertEquals("5", capturedNodes.get(4).getFullName());

        verify(mockedModuleRunner).moduleFinished();
        verify(mockedHistory).clear();
    }

    private void assertNodesEquals(String[] marks, JSwingRipplesEIGNode[] nodes) {
        assertEquals(marks.length, nodes.length);
        for (int i = 0; i < marks.length; i++) {
            assertEquals(marks[i], nodes[i].getMark());
        }
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedContinueRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.VISITED_CONTINUE, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndNeighbor(eig, node, node2, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.NEXT_VISIT);
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignAnnotationToNodeAndNeighbor(
                eq(eig), eq(node), eq(node2),
                anyString());

    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithVisitedRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.VISITED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.applyRuleToNode(eig, node, EIGStatusMarks.VISITED, 0);
        PowerMockito.verifyStatic(CommonEIGRules.class, never());
        CommonEIGRules.assignAnnotationToNodeAndNeighbor(
                eq(eig), eq(node), eq(node2),
                anyString());
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithLocatedRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.LOCATED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class, never());
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithImpactedRuleShouldDoNothing() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.IMPACTED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class, never());
    }

    @Test
    public void testApplyRuleAtNodeWith4ParamsWithChangedRule() {
        /* given */
        JSwingRipplesEIG eig = mock(JSwingRipplesEIG.class);
        JSwingRipplesEIGNode node = mock(JSwingRipplesEIGNode.class);
        JSwingRipplesEIGNode node2 = mock(JSwingRipplesEIGNode.class);

        JRipplesModuleICChangePropagation cp
                = new JRipplesModuleICChangePropagation(eig);

        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(EIGStatusMarks.CHANGED, node, node2);

        /* then*/
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignMarkToNodeAndNeighbor(eig, node, node2, EIGStatusMarks.CHANGED, EIGStatusMarks.NEXT_VISIT);
        PowerMockito.verifyStatic(CommonEIGRules.class);
        CommonEIGRules.assignAnnotationToNodeAndNeighbor(
                eq(eig), eq(node), eq(node2),
                anyString());
    }

}