/*-
 * $Id: PermissionBean.java,v 1.4 2005/11/08 13:44:09 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import static com.syrus.AMFICOM.manager.MCMBeanWrapper.KEY_USER_ID;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributesWrapper;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/11/08 13:44:09 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PermissionBean extends Bean implements UserItem {

	private PermissionAttributes permissionAttributes;
	private HashMap<PermissionCodename, Boolean>	rolePermissionMap;	
	
	@Override
	protected void setIdentifier(Identifier id) throws ApplicationException {
		super.setIdentifier(id);
		this.permissionAttributes = StorableObjectPool.getStorableObject(this.identifier, true);
	}

	@Override
	public final String getName() {
		return this.permissionAttributes.getModule().getDescription();
	}	

	@Override
	public void setName(String name) {
	}
	
	public final Identifier getParentId() {
		return this.permissionAttributes.getParentId();
	}
	
	public final void setParentId(final Identifier parentId) {
		Identifier userId2 = this.permissionAttributes.getParentId();
		if (userId2 != parentId &&
				(userId2 != null && !userId2.equals(parentId) ||
				!parentId.equals(userId2))) {
			this.permissionAttributes.setParentId(parentId);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_USER_ID, userId2, parentId));
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
		this.permissionAttributes.setDomainId(newDomainId);
	}
	
	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage(this.identifier.getIdentifierString(), Log.DEBUGLEVEL09);
		StorableObjectPool.delete(this.identifier);
		super.disposeLayoutItem();
	}

	public final boolean getRolePermission(final PermissionCodename permissionCodename) 
	throws ApplicationException {
		final Boolean b = this.getRolePermissions().get(permissionCodename);
		return b == null ? false : b.booleanValue();
	}
	
	public void updateRolePermissions() throws ApplicationException {
		if (this.rolePermissionMap == null) {
			this.rolePermissionMap = new HashMap<PermissionCodename, Boolean>();
		} else {
			this.rolePermissionMap.clear();
		}
		final SystemUser systemUser = 
			StorableObjectPool.getStorableObject(this.permissionAttributes.getParentId(), true);
		
		final Set<Identifier> roleIds = systemUser.getRoleIds();
		if (!roleIds.isEmpty()) {
			final Module module = this.permissionAttributes.getModule();
			final CompoundCondition compoundCondition = 
				new CompoundCondition(
					new LinkedIdsCondition(roleIds, ObjectEntities.PERMATTR_CODE), 
					CompoundConditionSort.AND,
					new TypicalCondition(module, 
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.PERMATTR_CODE,
						PermissionAttributesWrapper.COLUMN_MODULE));
			
			final Set<PermissionAttributes> rolePermissions =
				StorableObjectPool.getStorableObjectsByCondition(compoundCondition, 
					true);
			
			final List<String> keys = PermissionBeanWrapper.getInstance(module).getKeys();
			
			for (final String string : keys) {
				final PermissionCodename codename = PermissionCodename.valueOf(string);
				boolean permitted = false;
				
				for (final PermissionAttributes attributes : rolePermissions) {
					permitted |= attributes.isPermissionEnable(codename);
					if (permitted) {
						break;
					}
				}
				this.rolePermissionMap.put(codename, Boolean.valueOf(permitted));
			}	
		}
		assert Log.debugMessage(this.hashCode() , Log.DEBUGLEVEL09);
		this.firePropertyChangeEvent(new PropertyChangeEvent(this, ObjectEntities.PERMATTR, null, null));
	}
	
	private final Map<PermissionCodename, Boolean> getRolePermissions() 
	throws ApplicationException{
		if (this.rolePermissionMap == null) {
			this.updateRolePermissions();
		}			
		
		return this.rolePermissionMap;
	}
	
	public final PermissionAttributes getPermissionAttributes() {
		return this.permissionAttributes;
	}
	
	@Override
	public String getCodename() {
		return this.permissionAttributes.getModule().getCodename() + ObjectEntities.SYSTEMUSER;
	}
}
