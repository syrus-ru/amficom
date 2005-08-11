/*-
 * $Id: MCMBean.java,v 1.3 2005/08/11 13:05:20 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.MCMBeanWrapper.*;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.JGraphText;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/11 13:05:20 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MCMBean extends Bean {

	private MCM mcm;
	
	@Override
	protected void setId(Identifier id) {
		super.setId(id);
		try {
			this.mcm = StorableObjectPool.getStorableObject(this.id, true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final String getDescription() {
		return this.mcm.getDescription();
	}

	@Override
	public final String getName() {
		return this.mcm.getName();
	}
	
	public final void setDescription(String description) {
		String description2 = this.mcm.getDescription();
		if (description2 != description &&
				(description2 != null && !description2.equals(description) ||
				!description.equals(description2))) {
			this.mcm.setDescription(description);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, description2, description));
		}	
	}
	
	@Override
	public final void setName(String name) {
		String name2 = this.mcm.getName();
		if (name2 != name &&
				(name2 != null && !name2.equals(name) ||
				!name.equals(name2))) {
			this.mcm.setName(name);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, name2, name));
			JGraphText.entityDispatcher.firePropertyChange(
				new PropertyChangeEvent(this, ObjectEntities.MCM, null, this));

		}		
	}
	
	public final String getHostname() {
		return this.mcm.getHostName();
	}

	
	public final void setHostname(String hostname) {
		String hostname2 = this.mcm.getHostName();
		if (hostname2 != hostname &&
				(hostname2 != null && !hostname2.equals(hostname) ||
				!hostname.equals(hostname2))) {
			this.mcm.setHostName(hostname);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_HOSTNAME, hostname2, hostname));
		}	
	}
	
	public final Identifier getServerId() {
		return this.mcm.getServerId();
	}
	
	public final void setServerId(Identifier serverId) {
		Identifier serverId2 = this.mcm.getServerId();
		if (serverId2 != serverId &&
				(serverId2 != null && !serverId2.equals(serverId) ||
				!serverId.equals(serverId2))) {
			this.mcm.setServerId(serverId);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_SERVER_ID, serverId2, serverId));
		}		
	}	

	
	public final Identifier getUserId() {
		return this.mcm.getUserId();
	}
	
	public final void setUserId(Identifier userId) {
		Identifier userId2 = this.mcm.getUserId();
		if (userId2 != userId &&
				(userId2 != null && !userId2.equals(userId) ||
				!userId.equals(userId2))) {
			this.mcm.setUserId(userId);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_USER_ID, userId2, userId));
		}		
	}
}
