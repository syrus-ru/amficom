/*-
 * $Id: Dispatcher.java,v 1.16 2005/10/30 15:20:24 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * 
 * @version $Revision: 1.16 $, $Date: 2005/10/30 15:20:24 $
 * @author $Author: bass $
 * @author Kholshin Stanislav
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class Dispatcher {

	private Map<String, List<PropertyChangeListener>> events; // список событий

	public Dispatcher() {
		this.events = new HashMap<String, List<PropertyChangeListener>>();
	}

	// регистрация связывает подписчика с определенным событием
	public synchronized void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		assert Log.debugMessage("propertyName:" + propertyName
				+ ", listener: " + listener.getClass().getName(), Log.DEBUGLEVEL10);
		List<PropertyChangeListener> listeners = this.events.get(propertyName);
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<PropertyChangeListener>();
			this.events.put(propertyName, listeners);
		}

		if (!listeners.contains(listener)) {
			listeners.add(listener);
		} else {
			assert Log.debugMessage("already added listener: " + listener.getClass().getName(),
					Level.WARNING);
		}
	}

	// унрегистрация убирает связь подписчика с определенным событием
	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		final List<PropertyChangeListener> listeners = this.events.get(propertyName);
		assert Log.debugMessage("propertyName:" + propertyName
				+ ", listener: " + listener.getClass().getName(), Log.DEBUGLEVEL10);
		if (listeners != null) {
			if (!listeners.remove(listener)) {
				assert Log.debugMessage("there is no added listener: "
						+ listener.getClass().getName(), Level.WARNING);
			}
		}
	}

	public void firePropertyChange(final PropertyChangeEvent event) {
		this.firePropertyChange(event, false);
	}

	@SuppressWarnings("unused")
	private void printListeners() {
		assert Log.debugMessage("this.hashCode(): " + this.hashCode(), Level.FINEST);
		for (Iterator iterator = this.events.keySet().iterator(); iterator.hasNext();) {
			final String propertyName = (String) iterator.next();
			List<PropertyChangeListener> listeners = this.events.get(propertyName);
			assert Log.debugMessage("propertyName: " + propertyName, Level.FINEST);
			for (PropertyChangeListener changeListener : listeners) {
				assert Log.debugMessage("changeListener : " + changeListener.getClass().getName(), Level.FINEST);
			}
		}
	}

	public void firePropertyChange(final PropertyChangeEvent event, final boolean canSendToSelf) {
		final String propertyName = event.getPropertyName();

		final List<PropertyChangeListener> listeners = this.events.get(propertyName);

		if (listeners != null && !listeners.isEmpty()) {
			final Object source = event.getSource();
			for (final PropertyChangeListener listener : listeners) {
				/*
				 * yeah, really compare references, skip sending message to source
				 */
				if (!canSendToSelf && listener == source) {
					assert Log.debugMessage("propertyName: " + propertyName
							+ ", listener == source (" + source.getClass().getName() + ")", Log.DEBUGLEVEL10);
					continue;
				}

				assert Log.debugMessage("propertyName: " + propertyName
						+ ", listener: " + listener.getClass().getName(), Log.DEBUGLEVEL10);
				listener.propertyChange(event);
			}
		} else {
			assert Log.debugMessage("listener for '" + propertyName
					+ "' is " + (listeners == null ? "'null'" : "empty"), Log.DEBUGLEVEL10);
		}
	}
}
