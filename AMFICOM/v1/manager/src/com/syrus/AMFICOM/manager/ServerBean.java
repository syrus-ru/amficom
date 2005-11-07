/*-
 * $Id: ServerBean.java,v 1.12 2005/11/07 15:24:19 bob Exp $
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
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.UI.ManagerModel;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ServerBean extends Bean implements DomainNetworkItem {

	private Server server;

	@Override
	protected void setIdentifier(Identifier id) throws ApplicationException {
		super.setIdentifier(id);
		this.server = StorableObjectPool.getStorableObject(this.identifier, true);
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
			final ManagerModel managerModel = (ManagerModel)this.graphText.getModel();
			final Dispatcher dispatcher = managerModel.getDispatcher();
			dispatcher.firePropertyChange(
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

	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage("ServerBean.dispose | " + this.identifier, Log.DEBUGLEVEL10);
		StorableObjectPool.delete(this.identifier);
		super.disposeLayoutItem();
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.SERVER;
	}
}
