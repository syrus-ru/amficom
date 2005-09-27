/*-
* $Id: Checker.java,v 1.3 2005/09/27 14:06:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;


/**
 * @version $Revision: 1.3 $, $Date: 2005/09/27 14:06:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module csbridge
 */
public abstract class Checker {

	private Checker() {
		// singleton
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
		
		final PermissionAttributes permissionAttributes = 
			domain.getPermissionAttributes(LoginManager.getUserId(), codename.getModule());
		
		return permissionAttributes.isPermissionEnable(codename);
	}
	
}

