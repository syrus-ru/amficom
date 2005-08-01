/*-
 * $Id: MCMBean.java,v 1.1 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBean extends Bean {

	private String			hostname;

	private Identifier		serverId;

	private Identifier		userId;
	
	public final String getHostname() {
		return this.hostname;
	}

	
	public final void setHostname(String hostname) {
		if (this.hostname != hostname &&
				(this.hostname != null && !this.hostname.equals(hostname) ||
				!hostname.equals(this.hostname))) {
			String oldValue = this.hostname;
			this.hostname = hostname;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, MCMBeanWrapper.KEY_HOSTNAME, oldValue, hostname));
		}
	}
	
	public final Identifier getServerId() {
		return this.serverId;
	}
	
	public final void setServerId(Identifier serverId) {
		if (this.serverId != serverId &&
				(this.serverId != null && !this.serverId.equals(serverId) ||
				!serverId.equals(this.serverId))) {
			Identifier oldValue = this.serverId;
			this.serverId = serverId;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, MCMBeanWrapper.KEY_SERVER_ID, oldValue, serverId));
		}	
	}	

	
	public final Identifier getUserId() {
		return this.userId;
	}
	
	public final void setUserId(Identifier userId) {
		if (this.userId != userId &&
				(this.userId != null && !this.userId.equals(userId) ||
				!userId.equals(this.userId))) {
			Identifier oldValue = this.userId;
			this.userId = userId;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, MCMBeanWrapper.KEY_USER_ID, oldValue, userId));
		}	
	}
}
