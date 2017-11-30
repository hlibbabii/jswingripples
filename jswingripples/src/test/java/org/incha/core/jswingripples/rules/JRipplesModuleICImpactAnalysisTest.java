package org.incha.core.jswingripples.rules;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CommonEIGRules.class)
public class JRipplesModuleICImpactAnalysisTest {

    private AtomicInteger counter;

    @Before
    public void setup() {
        counter = new AtomicInteger(0);
    }

    @Test
    public void getAvailableRulesForMark() throws Exception {
        JRipplesModuleICImpactAnalysis cp
                = new JRipplesModuleICImpactAnalysis(null);

        assertEquals(null, cp.GetAvailableRulesForMark(null));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.BLANK));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED, EIGStatusMarks.VISITED_CONTINUE, EIGStatusMarks.VISITED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.NEXT_VISIT));

        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.LOCATED));
        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.IMPACTED));
        assertEquals(null, cp.GetAvailableRulesForMark(EIGStatusMarks.CHANGED));

        assertEquals(new LinkedHashSet<>(Arrays.asList(
                EIGStatusMarks.IMPACTED, EIGStatusMarks.VISITED_CONTINUE
        )), cp.GetAvailableRulesForMark(EIGStatusMarks.VISITED_CONTINUE));

        assertEquals(null, cp.GetAvailableRulesForMark("unknown_mark"));
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

        JRipplesModuleICImpactAnalysis cp
                = new JRipplesModuleICImpactAnalysis(mockedEig);

        /* when */
        cp.InitializeStage(mockedModuleRunner);

        /* then */

        assertNodesEquals(new String[]{
                EIGStatusMarks.BLANK,  // 0
                EIGStatusMarks.BLANK,  // 1
                EIGStatusMarks.BLANK,  // 2
                EIGStatusMarks.BLANK,  // 3
                EIGStatusMarks.NEXT_VISIT,  // 4
                EIGStatusMarks.IMPACTED,  // 5
                EIGStatusMarks.NEXT_VISIT,  // 6
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
                Matchers.matches("Impacted"),
                eq(0)
        );
        List<JSwingRipplesEIGNode> capturedNodes = nodeCaptor.getAllValues();
        assertEquals("12", capturedNodes.get(0).getFullName());
        assertEquals("13", capturedNodes.get(1).getFullName());
        assertEquals("14", capturedNodes.get(2).getFullName());
        assertEquals("4", capturedNodes.get(3).getFullName());
        assertEquals("6", capturedNodes.get(4).getFullName());

        verify(mockedModuleRunner).moduleFinished();
        verify(mockedHistory).clear();
    }

    private void assertNodesEquals(String[] marks, JSwingRipplesEIGNode[] nodes) {
        assertEquals(marks.length, nodes.length);
        for (int i = 0; i < marks.length; i++) {
            assertEquals(marks[i], nodes[i].getMark());
        }
    }

    private JSwingRipplesEIGNode createEIGClassNodeMock(String mark, JSwingRipplesEIG eig) {
        return createEIGNodeMock(mark, eig, mock(IType.class));
    }

    private JSwingRipplesEIGNode createEIGMethodNodeMock(String mark, JSwingRipplesEIG eig) {
        return createEIGNodeMock(mark, eig, mock(IMethod.class));
    }

    private JSwingRipplesEIGNode createEIGNodeMock(String mark,
                                                   JSwingRipplesEIG eig, IMember iMember) {

        JSwingRipplesEIGNode jSwingRipplesEIGNode = spy(new JSwingRipplesEIGNode(eig, iMember));
        doReturn(Integer.toString(counter.getAndAdd(1))).when(jSwingRipplesEIGNode).getFullName();
        jSwingRipplesEIGNode.setMark(mark);

        return jSwingRipplesEIGNode;
    }
}