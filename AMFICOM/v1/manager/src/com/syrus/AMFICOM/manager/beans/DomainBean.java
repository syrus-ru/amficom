/*-
 * $Id: DomainBean.java,v 1.1 2005/11/17 09:00:32 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import static com.syrus.AMFICOM.manager.beans.DomainBeanWrapper.KEY_DESCRIPTION;
import static com.syrus.AMFICOM.manager.beans.DomainBeanWrapper.KEY_NAME;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBean extends Bean {
	
	private Domain domain;

	@Override
	protected void setIdentifier(Identifier storableObject) throws ApplicationException {
		super.setIdentifier(storableObject);
		this.domain = StorableObjectPool.getStorableObject(this.identifier, true);
	}
	
	public final String getDescription() {
		return this.domain.getDescription();
	}

	@Override
	public final String getName() {
		return this.domain.getName();
	}
	
	public final void setDescription(String description) {
		String description2 = this.domain.getDescription();
		if (description2 != description &&
				(description2 != null && !description2.equals(description) ||
				!description.equals(description2))) {
			this.domain.setDescription(description);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_DESCRIPTION, description2, description));
		}	
	}
	
	@Override
	public final void setName(final String name) {
		String name2 = this.domain.getName().intern();
		if (name2 != name.intern() ) {
			assert Log.debugMessage("DomainBean.setName | was:" + name2 + ", now:" + name, Log.DEBUGLEVEL09);
			this.domain.setName(name);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_NAME, name2, name));
		}
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		Identifier parentId = Identifier.VOID_IDENTIFIER;
		if (newPort != null) {
			parentId = ((DomainBean) newPort.getUserObject()).getIdentifier();
		}		
		assert Log.debugMessage(this.domain.getId() 
				+ ", set parent " 
				+ parentId,
			Log.DEBUGLEVEL10); 
		this.domain.setDomainId(parentId);
	}	

	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage("DomainBean.dispose | " + this.identifier, Log.DEBUGLEVEL10);
		StorableObjectPool.delete(this.identifier);		
		super.disposeLayoutItem();
	}
	
	@Override
	public final String getCodename() {
		return ObjectEntities.DOMAIN;
	}
	
	public final Domain getDomain() {
		return this.domain;
	}
}
