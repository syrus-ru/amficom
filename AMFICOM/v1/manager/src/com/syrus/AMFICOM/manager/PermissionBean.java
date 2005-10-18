/*-
 * $Id: PermissionBean.java,v 1.2 2005/10/18 15:10:38 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/10/18 15:10:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class PermissionBean extends Bean implements UserItem {

	private static final String UI_CLASS_ID = "PermissionAttributesBeanUI";
	
	private PermissionAttributes permissionAttributes;
	private HashMap<PermissionCodename, Boolean>	rolePermissionMap;	
	
	@Override
	public String getUIClassID() {
		return UI_CLASS_ID;
	}
	
	@Override
	protected void setId(Identifier id) throws ApplicationException {
		super.setId(id);
		this.permissionAttributes = StorableObjectPool.getStorableObject(this.id, true);
	}

	@Override
	public final String getName() {
		return this.permissionAttributes.getModule().getDescription();
	}	

	@Override
	public void setName(String name) {
	}
	
	public final Identifier getUserId() {
		return this.permissionAttributes.getParentId();
	}
	
	public final void setUserId(Identifier userId) {
		Identifier userId2 = this.permissionAttributes.getParentId();
		if (userId2 != userId &&
				(userId2 != null && !userId2.equals(userId) ||
				!userId.equals(userId2))) {
			this.permissionAttributes.setParentId(userId);
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, KEY_USER_ID, userId2, userId));
		}		
	}
	
	@Override
	public void applyTargetPort(MPort oldPort, MPort newPort) {
		// TODO Auto-generated method stub
		
	}
	
	public void setDomainId(final Identifier oldDomainId,
	                        final Identifier newDomainId) {
		assert Log.debugMessage("PermissionBean.setDomainId | oldDomainId:" 
				+ oldDomainId
				+ ", newDomainId:"
				+ newDomainId, 
			Log.DEBUGLEVEL09);
		this.permissionAttributes.setDomainId(newDomainId);
	}
	
	@Override
	public void dispose() throws ApplicationException {
		Log.debugMessage("PermissionBean.dispose | " + this.id, Log.DEBUGLEVEL09);
		StorableObjectPool.delete(this.id);
		super.disposeLayoutItem();
	}

	
	public final Map<PermissionCodename, Boolean> getRolePermissions() 
	throws ApplicationException{
		if (this.rolePermissionMap == null) {
			this.rolePermissionMap = new HashMap<PermissionCodename, Boolean>();
			
			final SystemUser systemUser = 
				StorableObjectPool.getStorableObject(this.permissionAttributes.getParentId(), true);
			
			final Set<Identifier> roleIds = systemUser.getRoleIds();
			
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
		
		return this.rolePermissionMap;
	}
	
	public final PermissionAttributes getPermissionAttributes() {
		return this.permissionAttributes;
	}
}
