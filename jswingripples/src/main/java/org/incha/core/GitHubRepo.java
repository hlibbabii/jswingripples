package org.incha.core;

/**
 * Created by zeval on 11/26/16.
 * @author zeval
 */
public class GitHubRepo extends AbstractSettingsManager {
	/**
	 * String repository
	 */
	private static final String REPOSITORY = ModelSerializer.REPOSITORY;
	
	/**
	 * The current repository
	 */
	private String repository;
	
	/**
	 * Default constructor.
	 * @param javaProject
	 */
	public GitHubRepo(JavaProject javaProject) {		
		super(javaProject);
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

}
