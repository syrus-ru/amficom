/*-
 * $Id: RTUBean.java,v 1.12 2005/11/07 15:24:19 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_HOSTNAME;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_MCM_ID;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_NAME;
import static com.syrus.AMFICOM.manager.RTUBeanWrapper.KEY_PORT;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RTUBean extends Bean implements DomainNetworkItem {
	
	private KIS kis;
	
	@Override
	protected void setIdentifier(Identifier storableObject) throws ApplicationException {
		super.setIdentifier(storableObject);
		this.kis = StorableObjectPool.getStorableObject(this.identifier, true);
	}
	
	@Override
	public final String getName() {
		return this.kis.getName();
	}
	
	@Override
	public final void setName(String name) {
		String name2 = this.kis.getName();
		if (name2 != name &&
				(name2 != null && !name2.equals(name) ||
				!name.equals(name2))) {
			this.kis.setName(name);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, name2, name));
		}		
	}
	
	public final String getDescription() {
		return this.kis.getDescription();
	}
	
	public final void setDescription(String description) {
		String description2 = this.kis.getDescription();
		if (description2 != description &&
				(description2 != null && !description2.equals(description) ||
				!description.equals(description2))) {
			this.kis.setDescription(description);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, description2, description));
		}	
	}
	
	public final String getHostname() {
		return this.kis.getHostName();
	}

	
	public final void setHostname(String hostname) {
		String hostname2 = this.kis.getHostName();
		if (hostname2 != hostname &&
				(hostname2 != null && !hostname2.equals(hostname) ||
				!hostname.equals(hostname2))) {
			this.kis.setHostName(hostname);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_HOSTNAME, hostname2, hostname));
		}	
	}

	
	public final short getPort() {
		return this.kis.getTCPPort();
	}

	
	public final void setPort(short port) {
		short port2 = this.kis.getTCPPort();
		if (port2 != port) {
			this.kis.setTCPPort(port);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_PORT, Short.valueOf(port2), Short.valueOf(port)));
		}		

	}
	
	public final Identifier getMcmId() {
		return this.kis.getMCMId();
	}
	
	public final void setMcmId(Identifier mcmId) {
		Identifier mcmId2 = this.kis.getMCMId();
		if (mcmId2 != mcmId &&
				(mcmId2 != null && !mcmId2.equals(mcmId) ||
				!mcmId.equals(mcmId2))) {
			this.kis.setMCMId(mcmId);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_MCM_ID, mcmId2, mcmId));
		}	
	}

	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		// TODO Auto-generated method stub
		
	}
	
	public void setDomainId(final Identifier oldDomainId,
	                        final Identifier newDomainId) {
		assert Log.debugMessage("oldDomainId:" 
			+ oldDomainId
			+ ", newDomainId:"
			+ newDomainId, 
		Log.DEBUGLEVEL09);		
		this.kis.setDomainId(newDomainId);
	}
	
	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage(this.identifier.getIdentifierString(), Log.DEBUGLEVEL10);
		StorableObjectPool.delete(this.identifier);		
		super.disposeLayoutItem();
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.KIS;
	}
}
