package org.incha.core;

import java.beans.PropertyChangeListener;

/**
 * This interface represents a item which manages some configuration for a project
 * @author zeval
 *
 */
public interface ItemSettingsManager {
	
    /**
     * Wrapper of <code>void java.beans.PropertyChangeSupport.firePropertyChange(String propertyName, Object oldValue, Object newValue)</code>
     * @param property property name.
     * @param oldValue old property value.
     * @param newValue new property value.
     */
    void firePropertyChange(final String property, final Object oldValue, final Object newValue);

	/**
	 * Wrapper of <code>void java.beans.PropertyChangeSupport.addPropertyChangeListener(PropertyChangeListener listener)</code>
	 * @param listener
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Wrapper of <code>void java.beans.PropertyChangeSupport.removePropertyChangeListener(PropertyChangeListener listener)</code>
	 * @param listener
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

}