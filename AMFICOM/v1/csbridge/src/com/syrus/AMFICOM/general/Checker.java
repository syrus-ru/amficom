/*-
* $Id: Checker.java,v 1.5 2005/10/20 11:51:29 bob Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/10/20 11:51:29 $
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
		final Domain domain = 
			StorableObjectPool.getStorableObject(LoginManager.getDomainId(), true);
		
		final Identifier userId = LoginManager.getUserId();		
		final Module module = codename.getModule();
		
		final PermissionAttributes permissionAttributes = 
			domain.getPermissionAttributes(userId, module);
		
		if (permissionAttributes != null) {
			if (permissionAttributes.isDenied(codename)) {
				return false;
			}
			
			if (permissionAttributes.isPermissionEnable(codename)) {
				return true;
			}
		}
		
		final SystemUser systemUser = 
			StorableObjectPool.getStorableObject(userId, true);
		
		final Set<Identifier> roleIds = systemUser.getRoleIds();
		
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
		
		for (final PermissionAttributes attributes : rolePermissions) {
			permitted |= attributes.isPermissionEnable(codename);
			if (permitted) {
				break;
			}
		}
		
		return permitted;
	}
	
}

