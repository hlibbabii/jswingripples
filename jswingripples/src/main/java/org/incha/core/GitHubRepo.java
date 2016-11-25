package org.incha.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;

public class GitHubRepo {
	
	private static final String REPOSITORY = ModelSerializer.REPOSITORY;
	
	/**
	 * The current repository
	 */
	private String repository;
    /**
     * Owner project.
     */
    private final JavaProject project;
    /**
     * Property change support.
     */
    private final PropertyChangeSupport pcs;
	
	
	public GitHubRepo(JavaProject javaProject) {
		this.project = javaProject;
		pcs = new PropertyChangeSupport(javaProject);
	}

	public void replaceRepository(String newRepo){
		String old = repository;
		repository = newRepo;
		firePropertyChange(REPOSITORY, old, repository);
	}
	
	public String getCurrentRepository(){
		return repository == null ? "" : repository;
	}
	
    /**
     * @param property property name.
     * @param oldValue old property value.
     * @param newValue new property value.
     */
    protected void firePropertyChange(
            final String property, final String oldRepo, final String newRepo) {
        pcs.firePropertyChange(property, oldRepo, newRepo);
    }

	public void addPropertyChangeListener(PropertyChangeListener gitHubRepoListener) {
		pcs.addPropertyChangeListener(gitHubRepoListener);		
	}

	public void removePropertyChangeListener(PropertyChangeListener gitHubRepoListener) {
		pcs.removePropertyChangeListener(gitHubRepoListener);
		
	}

}
