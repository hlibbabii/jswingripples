package org.incha.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.incha.utils.CollectionUtils;

/**
 * Stores the Sources of the project, with ArrayList for the folders
 *
 */
public class BuildPath extends AbstractSettingsManager{

    /**
     * The source files property name.
     */
    public static final String SOURCES = ModelSerializer.SOURCES;

    /**
     * The list of java source folders.
     */
    private final List<File> sources = new ArrayList<File>();

    /**
     * @param project owner java project.
     */
    public BuildPath(final JavaProject project) {
        super(project);
    }

    /**
     * @return the sourceFolders
     */
    public List<File> getSources() {
        return new ArrayList<File>(sources);
    }
    /**
     * @param file the source folder.
     */
    public void addSource(final File file) {
        final File equals = CollectionUtils.getEquals(sources, file);
        if (equals == null) {
            final List<File> old = new ArrayList<File>(sources);
            sources.add(file);
            firePropertyChange(SOURCES, old, sources);
        }
    }
    /**
     * @param file the source folder.
     */
    public void deleteSource(final File file) {
        final File equals = CollectionUtils.getEquals(sources, file);
        if (equals != null) {
            final List<File> old = new ArrayList<File>(sources);
            sources.remove(file);
            firePropertyChange(SOURCES, old, sources);
        }
    }

    /**
     * @return the project
     */
    public JavaProject getProject() {
        return project;
    }
    
    /**
     * return the first path related to the sources
     * @return the first path
     */
    public String getFirstPath(){
        if (!sources.isEmpty()){
            return sources.get(0).getPath();
        }
        return "";
    }

}
