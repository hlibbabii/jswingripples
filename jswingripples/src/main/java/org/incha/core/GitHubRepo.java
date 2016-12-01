package org.incha.core;



import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GitHubRepo {
	/**
	 * String repository
	 */
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
	
	/**
	 * Default constructor.
	 * @param javaProject
	 */
	public GitHubRepo(JavaProject javaProject) {
		this.project = javaProject;
		pcs = new PropertyChangeSupport(javaProject);
	}
	/**
	 * Replaces the current repository.
	 * If you have a url, format it using GitHubSettings.formatURLRepo(url) before using this method.
	 * @param newRepo
	 */
	public void replaceRepository(String newRepo){
		String old = repository;
		repository = newRepo;
		firePropertyChange(REPOSITORY, old, repository);
	}
	/**
	 * Gets the current repository. Returns null if there is no.
	 * @return
	 */
	public String getCurrentRepository(){
		return repository == null ? "" : repository;
	}
	
	/**
	 * To notify if repository changes.
	 * @param property
	 * @param oldRepo
	 * @param newRepo
	 */
    protected void firePropertyChange(
            final String property, final String oldRepo, final String newRepo) {
        pcs.firePropertyChange(property, oldRepo, newRepo);
    }
    
    /**
     * Wrapper of void java.beans.PropertyChangeSupport.addPropertyChangeListener(PropertyChangeListener listener)
     * @param gitHubRepoListener
     */
	public void addPropertyChangeListener(PropertyChangeListener gitHubRepoListener) {
		pcs.addPropertyChangeListener(gitHubRepoListener);		
	}
	/**
	 * Wrapper of void org.incha.core.GitHubRepo.removePropertyChangeListener(PropertyChangeListener gitHubRepoListener)
	 * @param gitHubRepoListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener gitHubRepoListener) {
		pcs.removePropertyChangeListener(gitHubRepoListener);
		
	}

}
