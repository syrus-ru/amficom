/*-
* $Id: Bean.java,v 1.5 2005/10/11 15:34:53 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;

import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;

/**
 * @version $Revision: 1.5 $, $Date: 2005/10/11 15:34:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public abstract class Bean extends AbstractBean {

	protected JTable	table;
	
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

	
	@Override
	public JPanel getPropertyPanel() {
		if (this.table instanceof WrapperedPropertyTable) {
			WrapperedPropertyTableModel model = 
				(WrapperedPropertyTableModel) this.table.getModel();
			model.setObject(this);
		}
		return super.getPropertyPanel();
	}

}

