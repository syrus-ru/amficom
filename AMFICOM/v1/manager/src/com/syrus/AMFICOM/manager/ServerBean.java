/*-
 * $Id: ServerBean.java,v 1.8 2005/08/24 14:05:47 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.ServerBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.ServerBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.ServerBeanWrapper.KEY_NAME;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/24 14:05:47 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBean extends Bean implements DomainNetworkItem {

	private Server server;
	
	@Override
	protected void setId(Identifier id) {
		super.setId(id);
		try {
			this.server = StorableObjectPool.getStorableObject(this.id, true);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final String getDescription() {
		return this.server.getDescription();
	}
	
	public final void setDescription(String description) {
		String description2 = this.server.getDescription();
		if (description2 != description &&
				(description2 != null && !description2.equals(description) ||
				!description.equals(description2))) {
			this.server.setDescription(description);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, description2, description));
		}	
	}
	
	@Override
	public final String getName() {
		return this.server.getName();
	}
	
	@Override
	public final void setName(String name) {
		String name2 = this.server.getName();
		if (name2 != name &&
				(name2 != null && !name2.equals(name) ||
				!name.equals(name2))) {
			this.server.setName(name);
			this.graphText.getDispatcher().firePropertyChange(
				new PropertyChangeEvent(this, ObjectEntities.SERVER, null, this));
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, name2, name));
		}		
	}
	
	public final String getHostname() {
		return this.server.getHostName();
	}

	
	public final void setHostname(String hostname) {
		String hostname2 = this.server.getHostName();
		if (hostname2 != hostname &&
				(hostname2 != null && !hostname2.equals(hostname) ||
				!hostname.equals(hostname2))) {
			this.server.setHostName(hostname);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_HOSTNAME, hostname2, hostname));
		}	
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		// TODO Auto-generated method stub
		
	}
	
	public void setDomainId(final Identifier oldDomainId,
	                        final Identifier newDomainId) {
		this.server.setDomainId(newDomainId);
	}

}
