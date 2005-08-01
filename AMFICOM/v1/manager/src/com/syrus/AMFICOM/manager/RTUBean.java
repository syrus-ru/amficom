/*-
 * $Id: RTUBean.java,v 1.2 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RTUBean extends Bean {

	private String			hostname;

	private int				port;

	private Identifier		mcmId;
	
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
