/*-
* $Id: Checker.java,v 1.7 2005/10/24 09:09:34 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributesWrapper;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;


/**
 * 
 * Permission checker for a user logged in 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/10/24 09:09:34 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module csbridge
 */
public abstract class Checker {

	private Checker() {
		assert false;
	}

	/**
	 * @param codename permission codename
	 * @return true if permission for login user is enable
	 * @throws ApplicationException 
	 */
	public static boolean isPermitted(final PermissionCodename codename) 
	throws ApplicationException {
		final Identifier domainId = LoginManager.getDomainId();
		final Identifier userId = LoginManager.getUserId();		

		if (domainId == null || userId == null || domainId.isVoid() || userId.isVoid()) {
			return false;
		}
		
		final Domain domain = 
			StorableObjectPool.getStorableObject(domainId, true);
		
		final Module module = codename.getModule();
		
		// get user permission in logged in domain		
		final PermissionAttributes permissionAttributes = 
			domain.getPermissionAttributes(userId, module);
		
		// if it's permission is not null - check it
		if (permissionAttributes != null) {
			// if permission denied - return false without any additional checks
			if (permissionAttributes.isDenied(codename)) {
				return false;
			}
			
			// otherwise, check if force permitted by self permissions
			if (permissionAttributes.isPermissionEnable(codename)) {
				return true;
			}
		}
		
		// if codename is not occur in self user permissions, 
		// check permissions extended by roles 
		final SystemUser systemUser = 
			StorableObjectPool.getStorableObject(userId, true);
		
		final Set<Identifier> roleIds = systemUser.getRoleIds();
		
		// return false if no role for user
		if (roleIds.isEmpty()) {
			return false;
		}
		
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
		
		boolean permitted = false;
		
		// check logical summ for all role permissions
		for (final PermissionAttributes attributes : rolePermissions) {
			permitted |= attributes.isPermissionEnable(codename);
			if (permitted) {
				break;
			}
		}
		
		return permitted;
	}
	
}

