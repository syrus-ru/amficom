/*-
 * $Id: ServerBean.java,v 1.1 2005/08/01 11:32:03 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.beans.PropertyChangeEvent;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/01 11:32:03 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBean extends Bean {

	private String			hostname;

	public final String getHostname() {
		return this.hostname;
	}

	
	public final void setHostname(String hostname) {
		if (this.hostname != hostname &&
				(this.hostname != null && !this.hostname.equals(hostname) ||
				!hostname.equals(this.hostname))) {
			String oldValue = this.hostname;
			this.hostname = hostname;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, ServerBeanWrapper.KEY_HOSTNAME, oldValue, hostname));
		}
	}
}
