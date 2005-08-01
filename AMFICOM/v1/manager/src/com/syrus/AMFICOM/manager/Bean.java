/*-
* $Id: Bean.java,v 1.2 2005/08/01 11:32:03 bob Exp $
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

import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;

import static com.syrus.AMFICOM.manager.DomainBeanWrapper.*;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class Bean extends AbstractBean {

	protected WrapperedPropertyTable	table;
	
	protected String			description;

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
	
	public final String getDescription() {
		return this.description;
	}

	
	public final void setDescription(String description) {
		if (this.description != description &&
				(this.description != null && !this.description.equals(description) ||
				!description.equals(this.description))) {
			String oldValue = this.description;
			this.description = description;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, oldValue, description));
		}	
	}
	
	public final void setName(String name) {
		if (this.name != name &&
				(this.name != null && !this.name.equals(name) ||
				!name.equals(this.name))) {
			String oldValue = this.name;
			this.name = name;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, oldValue, name));
		}	
	}
	
	@Override
	public JPanel getPropertyPanel() {
		WrapperedPropertyTableModel model = 
			(WrapperedPropertyTableModel) this.table.getModel();
		model.setObject(this);
		return super.getPropertyPanel();
	}

}

