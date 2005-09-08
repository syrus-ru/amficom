/*-
 * $Id: Dispatcher.java,v 1.14 2005/09/08 14:25:57 bob Exp $
 *
 * Copyright � 2005 Syrus Systems.
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
 * @version $Revision: 1.14 $, $Date: 2005/09/08 14:25:57 $
 * @author $Author: bob $
 * @author Kholshin Stanislav
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class Dispatcher {

	private Map<String, List<PropertyChangeListener>> events; // ������ �������

	public Dispatcher() {
		this.events = new HashMap<String, List<PropertyChangeListener>>();
	}

	// ����������� ��������� ���������� � ������������ ��������
	public synchronized void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		Log.debugMessage("Dispatcher.addPropertyChangeListener | propertyName:" + propertyName
				+ ", listener: " + listener.getClass().getName(), Log.DEBUGLEVEL10);
		List<PropertyChangeListener> listeners = this.events.get(propertyName);
		if (listeners == null) {
			listeners = new CopyOnWriteArrayList<PropertyChangeListener>();
			this.events.put(propertyName, listeners);
		}

		if (!listeners.contains(listener)) {
			listeners.add(listener);
		} else {
			Log.debugMessage("Dispatcher.addPropertyChangeListener | already added listener: " + listener.getClass().getName(),
					Level.WARNING);
		}
	}

	// ������������� ������� ����� ���������� � ������������ ��������
	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		final List<PropertyChangeListener> listeners = this.events.get(propertyName);
		Log.debugMessage("Dispatcher.removePropertyChangeListener | propertyName:" + propertyName
				+ ", listener: " + listener.getClass().getName(), Log.DEBUGLEVEL10);
		if (listeners != null) {
			if (!listeners.remove(listener)) {
				Log.debugMessage("Dispatcher.removePropertyChangeListener | there is no added listener: "
						+ listener.getClass().getName(), Level.WARNING);
			}
		}
	}

	public void firePropertyChange(final PropertyChangeEvent event) {
		this.firePropertyChange(event, false);
	}

	@SuppressWarnings("unused")
	private void printListeners() {
		Log.debugMessage("Dispatcher.printListeners | this.hashCode(): " + this.hashCode(), Level.FINEST);
		for (Iterator iterator = this.events.keySet().iterator(); iterator.hasNext();) {
			final String propertyName = (String) iterator.next();
			List<PropertyChangeListener> listeners = this.events.get(propertyName);
			Log.debugMessage("Dispatcher.printListeners | propertyName: " + propertyName, Level.FINEST);
			for (PropertyChangeListener changeListener : listeners) {
				Log.debugMessage("Dispatcher.printListeners | changeListener : " + changeListener.getClass().getName(), Level.FINEST);
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
					Log.debugMessage("Dispatcher.firePropertyChange | propertyName: " + propertyName
							+ ", listener == source (" + source.getClass().getName() + ")", Log.DEBUGLEVEL10);
					continue;
				}

				Log.debugMessage("Dispatcher.firePropertyChange | propertyName: " + propertyName
						+ ", listener: " + listener.getClass().getName(), Log.DEBUGLEVEL10);
				listener.propertyChange(event);
			}
		} else {
			Log.debugMessage("Dispatcher.firePropertyChange | listener for '" + propertyName
					+ "' is " + (listeners == null ? "'null'" : "empty"), Log.DEBUGLEVEL10);
		}
	}
}
