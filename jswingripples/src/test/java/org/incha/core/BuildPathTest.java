package org.incha.core;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BuildPathTest implements PropertyChangeListener {
    /**
     * Currently handled property change event.
     */
    private PropertyChangeEvent propertyChangeEvent;
    /**
     * Build path to test.
     */
    private BuildPath buildPath;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        buildPath = new JavaProject("JUnit").getBuildPath();
        buildPath.addPropertyChangeListener(this);
    }

    @Test
    public void testAddSource() {
        final File f = new File("/a/b/c/d");

        buildPath.addSource(f);

        //check source added.
        assertEquals(1, buildPath.getSources().size());
        //check property changed event has sent
        assertNotNull(propertyChangeEvent);
        //check the event name is correct
        assertEquals(BuildPath.SOURCES, propertyChangeEvent.getPropertyName());
        //check old value is correct
        assertEquals(0, ((List<?>) propertyChangeEvent.getOldValue()).size());
        //check new value is correct
        assertEquals(1, ((List<?>) propertyChangeEvent.getNewValue()).size());

        //clear property change event
        propertyChangeEvent = null;
        //check can't add duplicate
        buildPath.addSource(f);

        assertEquals(1, buildPath.getSources().size());
        assertNull(propertyChangeEvent);
    }

    @Test
    public void testDeleteSource() {
        final File f1 = new File("/a/b/c/d");
        final File f2 = new File("/a/b/c/e");

        buildPath.addSource(f1);
        buildPath.addSource(f2);

        //run test
        buildPath.deleteSource(f2);

        //check source added.
        assertEquals(1, buildPath.getSources().size());
        //check property changed event has sent
        assertNotNull(propertyChangeEvent);
        //check the event name is correct
        assertEquals(BuildPath.SOURCES, propertyChangeEvent.getPropertyName());
        //check old value is correct
        assertEquals(2, ((List<?>) propertyChangeEvent.getOldValue()).size());
        //check new value is correct
        assertEquals(1, ((List<?>) propertyChangeEvent.getNewValue()).size());

        //clear property change event
        propertyChangeEvent = null;
        //check can't add duplicate
        buildPath.deleteSource(f2);

        assertEquals(1, buildPath.getSources().size());
        assertNull(propertyChangeEvent);
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        propertyChangeEvent = evt;
    }

    @After
    public void tearDown() throws Exception {
        propertyChangeEvent = null;
        buildPath.removePropertyChangeListener(this);
    }
}
