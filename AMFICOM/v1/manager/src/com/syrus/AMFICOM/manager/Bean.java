/*-
* $Id: Bean.java,v 1.1 2005/07/29 12:12:33 bob Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/29 12:12:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class Bean extends AbstractBean {

	protected List<PropertyChangeListener>						propertyChangeListeners;

	protected void firePropertyChangeEvent(PropertyChangeEvent event) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.isEmpty()) {
			for (PropertyChangeListener listener : this.propertyChangeListeners) {
				listener.propertyChange(event);
			}
		}
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new LinkedList<PropertyChangeListener>();
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
}

