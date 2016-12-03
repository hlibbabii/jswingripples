package org.incha.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This abstract class provides the minimal implementation to keep track of a java project 
 * property that you can define. 
 * 
 * A property should be represented by a String.
 * Ex: <code>private static final String REPOSITORY = ModelSerializer.REPOSITORY;</code>
 * 
 * Notice that the property string represents a property that already exists for the project
 * model.
 * You should use a suitable object to store the value(s) of the property.
 * Ex: <code>private String repository;</code>
 * 
 * @author zeval
 *
 */
public abstract class AbstractSettingsManager implements ItemSettingsManager {
	
	/**
     * Property change support.
     */
    protected final PropertyChangeSupport pcs;
    /**
     * Owner project.
     */
    protected final JavaProject project;
    
    public AbstractSettingsManager(JavaProject project){
    	this.project = project;
    	pcs = new PropertyChangeSupport(this.project);
    }
    
    @Override
	public
    void firePropertyChange(final String property, final Object oldValue, final Object newValue){
    	pcs.firePropertyChange(property, oldValue, newValue);
    }

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener){
		pcs.addPropertyChangeListener(listener);	
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener){
		pcs.removePropertyChangeListener(listener);
	}

}
