/*-
 * $Id: RTUBean.java,v 1.1 2005/07/29 12:12:33 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;

import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.WrapperedPropertyTable;
import com.syrus.AMFICOM.client.UI.WrapperedPropertyTableModel;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2005/07/29 12:12:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RTUBean extends Bean {

	private String			description;

	private String			hostname;

	private int				port;

	private Identifier		mcmId;

	WrapperedPropertyTable	table;
	
	@Override
	public JPanel getPropertyPanel() {
		WrapperedPropertyTableModel model = 
			(WrapperedPropertyTableModel) this.table.getModel();
		model.setObject(this);
		return super.getPropertyPanel();
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
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, RTUBeanWrapper.KEY_DESCRIPTION, oldValue, description));
		}	
	}

	
	public final String getHostname() {
		return this.hostname;
	}

	
	public final void setHostname(String hostname) {
		if (this.hostname != hostname &&
				(this.hostname != null && !this.hostname.equals(hostname) ||
				!hostname.equals(this.hostname))) {
			String oldValue = this.hostname;
			this.hostname = hostname;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, RTUBeanWrapper.KEY_HOSTNAME, oldValue, hostname));
		}
	}

	
	public final int getPort() {
		return this.port;
	}

	
	public final void setPort(int port) {
		this.port = port;
		if (this.port != port) {
			int oldValue = this.port;
			this.port = port;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, RTUBeanWrapper.KEY_PORT, oldValue, port));
		}	

	}

	
	public final void setName(String name) {
		if (this.name != name &&
				(this.name != null && !this.name.equals(name) ||
				!name.equals(this.name))) {
			String oldValue = this.name;
			this.name = name;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, RTUBeanWrapper.KEY_NAME, oldValue, name));
		}	
	}


	
	public final Identifier getMcmId() {
		return this.mcmId;
	}
	
	public final void setMcmId(Identifier mcmId) {
		System.out.println("RTUBean.setMcmId() | " + mcmId);
		if (this.mcmId != mcmId &&
				(this.mcmId != null && !this.mcmId.equals(mcmId) ||
				!mcmId.equals(this.mcmId))) {
			Identifier oldValue = this.mcmId;
			this.mcmId = mcmId;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, RTUBeanWrapper.KEY_MCM_ID, oldValue, mcmId));
		}	
	}	

}
