package org.incha.core;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;


public class ModelSerializerTest {

    @Test
    public void testSerializeModel() throws TransformerException, SAXException, IOException {
        final JavaProjectsModel origin = new JavaProjectsModel();
        final JavaProject p = new JavaProject("JUnit1");
        p.getBuildPath().addSource(new File("a/b/c/d/1"));
        p.getBuildPath().addSource(new File("a/b/c/d/2"));
        origin.addProject(p);

        origin.addProject(new JavaProject("JUnit2"));

        //save model
        final ModelSerializer ser = new ModelSerializer();
        final StringWriter sw = new StringWriter();
        ser.save(origin, sw);
        System.out.println(sw);

        //parse XML to model
        final JavaProjectsModel model = ser.parse(new StringReader(sw.toString()));

        assertEquals(origin.getProjects().size(), model.getProjects().size());

        //compare first projects
        final JavaProject prg1 = origin.getProjects().get(0);
        final JavaProject prg2 = model.getProjects().get(0);

        assertEquals(prg1.getName(), prg2.getName());
        assertEquals(prg1.getBuildPath().getSources().size(), prg2.getBuildPath().getSources().size());
        assertEquals(prg1.getBuildPath().getSources().get(1), prg2.getBuildPath().getSources().get(1));
    }
}
