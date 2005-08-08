/*-
 * $Id: TraceResource.java,v 1.3 2005/08/08 11:59:53 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.awt.Color;
import java.beans.*;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:59:53 $
 * @module analysis
 */

public class TraceResource {
	
	private String	id;
	private String	title;
	private Color		color;
	private boolean	isShown;
	private boolean isAlarm = false;
	
	private List						propertyChangeListeners;

	public TraceResource(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		if (this.title == null || !this.title.equals(title)) {
			String oldValue = this.title;
			this.title = title;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, TraceResourceWrapper.KEY_TITLE, oldValue, title));
		}
	}

	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		if (this.color == null || !this.color.equals(color)) {
			Color oldValue = this.color;
			this.color = color;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, TraceResourceWrapper.KEY_COLOR, oldValue, color));
		}
	}
	
	public boolean isShown() {
		return this.isShown;
	}

	public void setShown(boolean isShown) {
		if (this.isShown != isShown) {
			boolean oldValue = this.isShown;
			this.isShown = isShown;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, TraceResourceWrapper.KEY_IS_SHOWN, new Boolean(oldValue), new Boolean(isShown)));
		}
	}
	
	private void firePropertyChangeEvent(PropertyChangeEvent event) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.isEmpty()) {
			for (Iterator iterator = this.propertyChangeListeners.iterator(); iterator.hasNext();) {
				PropertyChangeListener listener = (PropertyChangeListener) iterator.next();
				listener.propertyChange(event);
			}
		}
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new LinkedList();
		}
		if (!this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.add(propertyChangeListener);
		}
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.remove(propertyChangeListener);
		}
	}

	public boolean isAlarm() {
		return isAlarm;
	}

	public void setAlarm(boolean isAlarm) {
		this.isAlarm = isAlarm;
	}
}

