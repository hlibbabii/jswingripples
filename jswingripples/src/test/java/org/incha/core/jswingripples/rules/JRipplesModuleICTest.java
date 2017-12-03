package org.incha.core.jswingripples.rules;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.incha.core.jswingripples.eig.JSwingRipplesEIG;
import org.incha.core.jswingripples.eig.JSwingRipplesEIGNode;

import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class JRipplesModuleICTest {
    protected AtomicInteger counter;


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
}
