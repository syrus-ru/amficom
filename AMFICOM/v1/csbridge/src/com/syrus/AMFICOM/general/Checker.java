/*-
* $Id: Checker.java,v 1.1 2005/08/12 06:30:53 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.PermissionAttributes;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodenames;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/12 06:30:53 $
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
	public boolean isPermited(PermissionCodenames codename) 
	throws ApplicationException {
		Domain domain = 
			StorableObjectPool.getStorableObject(LoginManager.getDomainId(), true);
		
		PermissionAttributes permissionAttributes = 
			domain.getPermissionAttributes(LoginManager.getUserId());
		
		return permissionAttributes.isPermissionEnable(codename);
	}
	
}

