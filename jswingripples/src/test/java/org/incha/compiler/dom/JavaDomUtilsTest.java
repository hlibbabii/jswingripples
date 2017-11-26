package org.incha.compiler.dom;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.core.simpledom.MockCompilationUnit;
import org.incha.core.simpledom.MockLocalVariable;
import org.incha.core.simpledom.MockMethod;
import org.incha.core.simpledom.MockType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class JavaDomUtilsTest {

    @Test
    public void testGetAllTypes() throws JavaModelException {
        //create compilatino unit
        final MockType type1 = new MockType("Type1");
        final MockType type2 = new MockType("Type2");
        final MockType subType = new MockType("Type2");

        final MockCompilationUnit unit = new MockCompilationUnit(
                "junit", type1, type2, subType);

        type2.add(new MockMethod("myMockMethod"));

        assertEquals(3, JavaDomUtils.getAllTypes(new ICompilationUnit[] {unit}).length);
    }

    @Test
    public void testGetAllNodes() throws JavaModelException {
        //create compilatino unit
        final MockType type1 = new MockType("Type1");
        final MockType type2 = new MockType("Type2");
        final MockType subType = new MockType("Type2");

        final MockCompilationUnit unit = new MockCompilationUnit(
                "junit", type1, type2, subType);

        final MockMethod m = new MockMethod("myMockMethod");
        type2.add(m);
        m.add(new MockLocalVariable("myLocalVariable"));

        assertEquals(5, JavaDomUtils.getAllNodes(unit).size());
    }

    @Test
    public void testGetAllMembers() throws JavaModelException {
        //create compilatino unit
        final MockType type1 = new MockType("Type1");
        final MockType type2 = new MockType("Type2");
        final MockType subType = new MockType("Type2");

        final MockCompilationUnit unit = new MockCompilationUnit(
                "junit", type1, type2, subType);

        final MockMethod m = new MockMethod("myMockMethod");
        type2.add(m);
        m.add(new MockLocalVariable("myLocalVariable"));

        assertEquals(4, JavaDomUtils.getAllMembers(unit).size());
    }
}
