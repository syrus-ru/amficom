/*-
 * $Id: RolePermissionBean.java,v 1.2 2005/12/14 15:08:30 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.beans;

import static com.syrus.AMFICOM.manager.beans.MCMBeanWrapper.KEY_USER_ID;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/12/14 15:08:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class RolePermissionBean extends Bean implements RoleItem {

	private PermissionAttributes permissionAttributes;

	@Override
	public boolean isDeletable() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}
	
	@Override
	protected void setIdentifier(final Identifier id) 
	throws ApplicationException {
		super.setIdentifier(id);
		this.permissionAttributes = 
			StorableObjectPool.getStorableObject(this.identifier, true);
	}

	@Override
	public final String getName() {
		return this.permissionAttributes.getModule().getDescription();
	}	

	@Override
	public void setName(String name) {
	}
	
	public final Identifier getRoleId() {
		return this.permissionAttributes.getParentId();
	}
	
	public final void setRoleId(Identifier roleId) {
		Identifier roleId2 = this.permissionAttributes.getParentId();
		if (roleId2 != roleId &&
				(roleId2 != null && !roleId2.equals(roleId) ||
				!roleId.equals(roleId2))) {
			this.permissionAttributes.setParentId(roleId);
			this.firePropertyChangeEvent(
				new PropertyChangeEvent(this, KEY_USER_ID, roleId2, roleId));
		}		
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage(this.identifier.getIdentifierString(), Log.DEBUGLEVEL09);
		StorableObjectPool.delete(this.identifier);
		super.disposeLayoutItem();
	}
	
	public final PermissionAttributes getPermissionAttributes() {
		return this.permissionAttributes;
	}
	
	@Override
	public String getCodename() {
		return this.permissionAttributes.getModule().getCodename();
	}
}
