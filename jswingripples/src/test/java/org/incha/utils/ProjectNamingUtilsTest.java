package org.incha.utils;


import org.junit.Test;

import static org.incha.utils.ProjectNamingUtils.getGitProjectNameFromUrl;
import static org.junit.Assert.assertEquals;

/**
 * Created by hlib on 21.11.17.
 */
public class ProjectNamingUtilsTest {

    @Test
    public void testGetGitProjectNameWithDotGit() throws Exception {
        assertEquals("project", getGitProjectNameFromUrl("http://path/project.git"));
    }

    @Test
    public void testGetGitProjectNameWithoutDotGit() throws Exception {
        assertEquals("project", getGitProjectNameFromUrl("http://path/project"));
    }

    @Test
    public void testGetGitProjectNameFromUrlWithoutLeadingPath() throws Exception {
        assertEquals("project", getGitProjectNameFromUrl("project.git"));
    }

}