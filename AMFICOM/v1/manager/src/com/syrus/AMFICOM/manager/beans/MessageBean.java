/*-
* $Id: MessageBean.java,v 1.1 2005/11/17 09:00:32 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.beans;

import com.syrus.AMFICOM.administration.Role;
import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/17 09:00:32 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MessageBean extends Bean {
	
	private DeliveryAttributes deliveryAttributes;   	
	
	@Override
	protected void setIdentifier(Identifier id) throws ApplicationException {
		super.setIdentifier(id);
		this.deliveryAttributes = StorableObjectPool.getStorableObject(this.identifier, true);
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.DELIVERYATTRIBUTES;
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.manager.AbstractBean#applyTargetPort(com.syrus.AMFICOM.manager.MPort, com.syrus.AMFICOM.manager.MPort)
	 */
	@Override
	public void applyTargetPort(MPort oldPort,
								MPort newPort) throws ApplicationException {
		assert Log.debugMessage("was:" + oldPort + ", now: "
			+ newPort, Log.DEBUGLEVEL03);
		
	}
	
	@Override
	public String getName() {
		return this.deliveryAttributes.getSeverity().getLocalizedName();
	}
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.manager.AbstractBean#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage(this.identifier, Log.DEBUGLEVEL10);
		StorableObjectPool.delete(this.identifier);
		super.disposeLayoutItem();
	}
	
	public final Severity getSeverity() {
		return this.deliveryAttributes.getSeverity();
	}
	
	public void addRole(final Role role) {
		assert Log.debugMessage(role, Log.DEBUGLEVEL03);
		this.deliveryAttributes.addRole(role);
	}
	
	public void removeRole(final Role role) {
		assert Log.debugMessage(role, Log.DEBUGLEVEL03);
		this.deliveryAttributes.removeRole(role);
	}
}
