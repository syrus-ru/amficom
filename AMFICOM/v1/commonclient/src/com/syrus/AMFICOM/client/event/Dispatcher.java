
package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.util.Log;

/**
 * 
 * @version $Revision: 1.3 $, $Date: 2005/05/27 12:47:56 $
 * @author $Author: bob $
 * @author Kholshin Stanislav
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class Dispatcher {

	private Map	events; // список событий

	public Dispatcher() {
		this.events = new HashMap();
	}

	// регистрация связывает подписчика с определенным событием
	public synchronized void addPropertyChangeListener(	String propertyName,
														PropertyChangeListener listener) {
		Log.debugMessage("Dispatcher.addPropertyChangeListener | propertyName:" + propertyName + ", listener: "
			+ listener.getClass().getName(), Log.DEBUGLEVEL10);
		List listeners = (List) this.events.get(propertyName);
		if (listeners == null) {
			listeners = new LinkedList();
			this.events.put(propertyName, listeners);
		}
		
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		} else {
			Log.debugMessage("Dispatcher.addPropertyChangeListener | already added listener: " + listener.getClass().getName(), Log.WARNING);
		}
	}

	// унрегистрация убирает связь подписчика с определенным событием
	public synchronized void removePropertyChangeListener(	String propertyName,
															PropertyChangeListener listener) {
		List listeners = (List) this.events.get(propertyName);
		Log.debugMessage("Dispatcher.removePropertyChangeListener | propertyName:" + propertyName + ", listener: "
			+ listener.getClass().getName(), Log.DEBUGLEVEL10);
		if (listeners != null) {
			if (!listeners.remove(listener)) {
				Log.debugMessage("Dispatcher.removePropertyChangeListener | there is no added listener: " + listener.getClass().getName() , Log.WARNING);
			}
		}		
	}

	public synchronized void firePropertyChange(PropertyChangeEvent event) {
		this.firePropertyChange(event, false);
	}
	                                            
	public synchronized void firePropertyChange(PropertyChangeEvent event, boolean canSendToSelf) {
		String propertyName = event.getPropertyName();

		List listeners = (List) this.events.get(propertyName);

		if (listeners != null && !listeners.isEmpty()) {
			Object source = event.getSource();
			for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
				PropertyChangeListener listener = (PropertyChangeListener) iterator.next();

				/*
				 * yeah, really compare references, skip sending message to
				 * source
				 */
				if (!canSendToSelf && listener == source) {
					Log.debugMessage("Dispatcher.firePropertyChange | propertyName: " + propertyName
							+ ", listener == source (" + source.getClass().getName() + ")", Log.DEBUGLEVEL10);
					continue;
				}

				Log.debugMessage("Dispatcher.firePropertyChange | propertyName: " + propertyName + ", listener: "
						+ listener.getClass().getName(), Log.DEBUGLEVEL10);
				listener.propertyChange(event);

			}
		}
	}
}
