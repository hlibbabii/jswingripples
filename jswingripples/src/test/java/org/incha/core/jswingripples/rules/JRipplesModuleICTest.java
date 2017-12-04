package org.incha.core.jswingripples.rules;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.incha.core.jswingripples.JRipplesICModule;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;
import org.incha.ui.jripples.EIGStatusMarks;
import org.powermock.api.mockito.PowerMockito;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;

public abstract class JRipplesModuleICTest {
    protected AtomicInteger counter;
    protected JSwingRipplesEIG eig;
    protected JSwingRipplesEIGNode node;
    protected JSwingRipplesEIGNode node2;
    protected JRipplesICModule cp;

    protected Verification assignMarkAndAnnotationToNodeAndNeighborVerification;
    protected Verification assignMarkToNodeAndNeighborAndNeverAnnotationVerification;
    protected Verification applyRuleToNodeAndAnnotationToNodeAndNeighbourVerification;
    protected Verification assignMarkAndNodeToNodeAndParentsVerification;
    protected Verification nothingIsCalledVerification;
    protected Verification applyRuleToNodeVerification;

    public static abstract class Verification {
        public abstract void verify(String rule);
        public Verification withGranularity(int granularity) {
            throw new UnsupportedOperationException();
        }
    }

    protected abstract JRipplesICModule getModuleUnderTest();

    public void setup() {
        counter = new AtomicInteger(0);

        eig = mock(JSwingRipplesEIG.class);
        node = mock(JSwingRipplesEIGNode.class);
        node2 = mock(JSwingRipplesEIGNode.class);
        cp = getModuleUnderTest();

        initVerifications();
    }

    private void initVerifications() {
        assignMarkAndAnnotationToNodeAndNeighborVerification = new Verification() {
            @Override
            public void verify(String rule) {
                PowerMockito.verifyStatic(CommonEIGRules.class);
                CommonEIGRules.assignMarkToNodeAndNeighbor(eig, node, node2,
                        rule, EIGStatusMarks.NEXT_VISIT);
                PowerMockito.verifyStatic(CommonEIGRules.class);
                CommonEIGRules.assignAnnotationToNodeAndNeighbor(
                        eq(eig), eq(node), eq(node2),
                        anyString());
            }
        };

        assignMarkToNodeAndNeighborAndNeverAnnotationVerification = new Verification() {
            @Override
            public void verify(String rule) {
                PowerMockito.verifyStatic(CommonEIGRules.class);
                CommonEIGRules.assignMarkToNodeAndNeighbor(eig, node, node2,
                        rule, EIGStatusMarks.NEXT_VISIT);
                PowerMockito.verifyStatic(CommonEIGRules.class, never());
                CommonEIGRules.assignAnnotationToNodeAndNeighbor(
                        eq(eig), eq(node), eq(node2),
                        anyString());
            }
        };

        applyRuleToNodeAndAnnotationToNodeAndNeighbourVerification = new Verification() {
            @Override
            public void verify(String rule) {
                PowerMockito.verifyStatic(CommonEIGRules.class);
                CommonEIGRules.applyRuleToNode(eig, node, rule, 0);
                PowerMockito.verifyStatic(CommonEIGRules.class, never());
                CommonEIGRules.assignAnnotationToNodeAndNeighbor(
                        eq(eig), eq(node), eq(node2),
                        anyString());
            }
        };

        assignMarkAndNodeToNodeAndParentsVerification = new Verification() {
            @Override
            public void verify(String rule) {
                PowerMockito.verifyStatic(CommonEIGRules.class);
                CommonEIGRules.assignMarkToNodeAndParents(eig, node, rule);

                PowerMockito.verifyStatic(CommonEIGRules.class);
                CommonEIGRules.assignAnnotationToNodeAndParents(eq(eig), eq(node), anyString());
            }
        };

        nothingIsCalledVerification = new Verification() {
            @Override
            public void verify(String rule) {
                PowerMockito.verifyStatic(CommonEIGRules.class, never());
            }
        };

        applyRuleToNodeVerification = new Verification() {
            private int granularity = 0;

            @Override
            public void verify(String rule) {
                PowerMockito.verifyStatic(CommonEIGRules.class);
                CommonEIGRules.applyRuleToNode(eig, node, rule, granularity);
            }

            @Override
            public Verification withGranularity(int granularity) {
                this.granularity = granularity;
                return this;
            }
        };
    }


    protected JSwingRipplesEIGNode createEIGClassNodeMock(String mark, JSwingRipplesEIG eig) {
        return createEIGNodeMock(mark, eig, mock(IType.class));
    }

    protected JSwingRipplesEIGNode createEIGMethodNodeMock(String mark, JSwingRipplesEIG eig) {
        return createEIGNodeMock(mark, eig, mock(IMethod.class));
    }

    private JSwingRipplesEIGNode createEIGNodeMock(String mark,
                                                   JSwingRipplesEIG eig, IMember iMember) {

        JSwingRipplesEIGNode jSwingRipplesEIGNode = spy(new JSwingRipplesEIGNode(eig, iMember));
        doReturn(Integer.toString(counter.getAndAdd(1))).when(jSwingRipplesEIGNode).getFullName();
        jSwingRipplesEIGNode.setMark(mark);

        return jSwingRipplesEIGNode;
    }

    protected void assertNodesEquals(String[] marks, JSwingRipplesEIGNode[] nodes) {
        assertEquals(marks.length, nodes.length);
        for (int i = 0; i < marks.length; i++) {
            assertEquals(marks[i], nodes[i].getMark());
        }
    }

    protected void testApplyRuleAtNodeWithTwoNodes(String rule, Verification verification) {
        /* given */
        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(rule, node, node2);

        /* then*/
        verification.verify(rule);
    }

    protected void testApplyRuleAtNodeWithGranularity(String rule, Verification verification) {
        /* given */
        PowerMockito.mockStatic(CommonEIGRules.class);

        /* when */
        cp.applyRuleAtNode(rule, node, 1);

        /* then*/
        verification.verify(rule);
    }
}
