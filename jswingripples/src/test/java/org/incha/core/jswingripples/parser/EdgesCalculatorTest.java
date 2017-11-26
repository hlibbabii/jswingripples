package org.incha.core.jswingripples.parser;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.incha.TestUtils;
import org.incha.compiler.dom.JavaDomBuilder;
import org.incha.core.JavaProject;
import org.incha.core.jswingripples.parser.samples.AnyDependency;
import org.incha.core.jswingripples.parser.samples.AnyInterface;
import org.incha.core.jswingripples.parser.samples.DeclarationInConstructorDependency;
import org.incha.core.jswingripples.parser.samples.DeclarationInMethodDependency;
import org.incha.core.jswingripples.parser.samples.InitializationInConstructorDependency;
import org.incha.core.jswingripples.parser.samples.InitializationInMethodDependency;
import org.incha.core.jswingripples.parser.samples.InitializerDependency;
import org.incha.core.jswingripples.parser.samples.StaticInitializerDependency;
import org.incha.core.jswingripples.parser.samples.SuperType;
import org.incha.core.jswingripples.parser.samples.TypeWithArrayType;
import org.incha.core.jswingripples.parser.samples.TypeWithInheritance;
import org.incha.core.jswingripples.parser.samples.TypeWithLocalVariableDependencies;
import org.incha.core.jswingripples.parser.samples.TypeWithMethodInfovation;
import org.incha.ui.util.NullMonitor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class EdgesCalculatorTest {
    private JavaDomBuilder builder;
    private JavaProject javaProject;

    @Before
    public void setUp() throws Exception {
        javaProject = new JavaProject("jinit");
        builder = new JavaDomBuilder(javaProject.getName());
    }

    private ICompilationUnit loadCompilationUnits(final Class<?> clazz)
            throws IOException {
        final NullMonitor monitor = new NullMonitor();
        return builder.build(TestUtils.findSourceForClass(clazz), monitor);
    }

    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testSuperType() throws IOException, JavaModelException {
        final ICompilationUnit superTypeUnit = loadCompilationUnits(SuperType.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithInheritance.class);

        final Set<Edge> edges = findEdges(mainType, superTypeUnit);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(SuperType.class.getSimpleName(), edge.getTo().getElementName());
        assertEquals(TypeWithInheritance.class.getSimpleName(), edge.getFrom().getElementName());
    }
    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testSuperInterface() throws IOException, JavaModelException {
        final ICompilationUnit superInterface = loadCompilationUnits(AnyInterface.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithInheritance.class);

        final Set<Edge> edges = findEdges(mainType, superInterface);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(AnyInterface.class.getSimpleName(), edge.getTo().getElementName());
        assertEquals(TypeWithInheritance.class.getSimpleName(), edge.getFrom().getElementName());
    }
    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testInitalizerDependency() throws IOException, JavaModelException {
        final ICompilationUnit dependency = loadCompilationUnits(InitializerDependency.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithLocalVariableDependencies.class);

        final Set<Edge> edges = findEdges(mainType, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(InitializerDependency.class.getSimpleName(), edge.getTo().getElementName());
        //initializer has not name
        assertEquals("", edge.getFrom().getElementName());
    }
    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testStaticInitalizerDependency() throws IOException, JavaModelException {
        final ICompilationUnit dependency = loadCompilationUnits(StaticInitializerDependency.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithLocalVariableDependencies.class);

        final Set<Edge> edges = findEdges(mainType, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(StaticInitializerDependency.class.getSimpleName(), edge.getTo().getElementName());
        //initializer has not name
        assertEquals("", edge.getFrom().getElementName());
    }
    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testInitializationInConstructor() throws IOException, JavaModelException {
        final ICompilationUnit dependency = loadCompilationUnits(InitializationInConstructorDependency.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithLocalVariableDependencies.class);

        final Set<Edge> edges = findEdges(mainType, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(InitializationInConstructorDependency.class.getSimpleName(),
                edge.getTo().getElementName());
        //constructor name is same as simple name of class.
        assertEquals(TypeWithLocalVariableDependencies.class.getSimpleName(),
                edge.getFrom().getElementName());
    }

    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testDeclarationInConstructor() throws IOException, JavaModelException {
        final ICompilationUnit dependency = loadCompilationUnits(DeclarationInConstructorDependency.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithLocalVariableDependencies.class);

        final Set<Edge> edges = findEdges(mainType, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(DeclarationInConstructorDependency.class.getSimpleName(),
                edge.getTo().getElementName());
        //constructor name is same as simple name of class.
        assertEquals(TypeWithLocalVariableDependencies.class.getSimpleName(),
                edge.getFrom().getElementName());
    }

    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testInitializationInMethod() throws IOException, JavaModelException {
        final ICompilationUnit dependency = loadCompilationUnits(InitializationInMethodDependency.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithLocalVariableDependencies.class);

        final Set<Edge> edges = findEdges(mainType, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(InitializationInMethodDependency.class.getSimpleName(),
                edge.getTo().getElementName());
        //constructor name is same as simple name of class.
        assertEquals("methodWithInitializationDependency", edge.getFrom().getElementName());
    }

    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testDeclarationInMethodDependency() throws IOException, JavaModelException {
        final ICompilationUnit dependency = loadCompilationUnits(DeclarationInMethodDependency.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithLocalVariableDependencies.class);

        final Set<Edge> edges = findEdges(mainType, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(DeclarationInMethodDependency.class.getSimpleName(),
                edge.getTo().getElementName());
        //constructor name is same as simple name of class.
        assertEquals("methodWithDeclarationDependency", edge.getFrom().getElementName());
    }
    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testArrayTypeDependency() throws IOException, JavaModelException {
        final ICompilationUnit dependency = loadCompilationUnits(AnyDependency.class);
        final ICompilationUnit mainType = loadCompilationUnits(TypeWithArrayType.class);

        final Set<Edge> edges = findEdges(mainType, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        assertEquals(AnyDependency.class.getSimpleName(), edge.getTo().getElementName());
        //constructor name is same as simple name of class.
        assertEquals("get", edge.getFrom().getElementName());
    }
    /**
     * @throws IOException
     * @throws JavaModelException
     */
    @Test
    public void testMethodCallDependency() throws IOException, JavaModelException {
//        TypeWithMethodInfovation
        final ICompilationUnit dependency = loadCompilationUnits(TypeWithLocalVariableDependencies.class);
        final ICompilationUnit mainUnit = loadCompilationUnits(TypeWithMethodInfovation.class);

        final Set<Edge> edges = findEdges(mainUnit, dependency);
        assertEquals(1, edges.size());
        final Edge edge = edges.iterator().next();

        //TODO implement method to method binding
        assertEquals(TypeWithLocalVariableDependencies.class.getSimpleName(),
                edge.getTo().getElementName());
        //constructor name is same as simple name of class.
        assertEquals("invokeMethod", edge.getFrom().getElementName());
    }

    /**
     * @param unitToProcess the child unit.
     * @param otherUnitsOfProject other compilation units of project.
     * @return
     * @throws JavaModelException
     */
    private Set<Edge> findEdges(final ICompilationUnit unitToProcess,
            final ICompilationUnit... otherUnitsOfProject) throws JavaModelException {
        return EdgesCalculator.getEdges(javaProject, unitToProcess, otherUnitsOfProject);
    }
}
