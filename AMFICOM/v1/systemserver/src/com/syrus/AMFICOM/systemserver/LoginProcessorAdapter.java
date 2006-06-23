/*-
 * $Id: LoginProcessorAdapter.java,v 1.1 2006/06/23 13:20:14 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.systemserver;

import com.syrus.AMFICOM.security.UserLogin;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: cvsadmin $
 * @version $Revision: 1.1 $, $Date: 2006/06/23 13:20:14 $
 * @module systemserver
 */
abstract class LoginProcessorAdapter implements LoginProcessorListener {
	/**
	 * @param userLogin
	 * @see LoginProcessorListener#userLoggedIn(UserLogin)
	 */
	public void userLoggedIn(final UserLogin userLogin) {
		// empty: should be overridden by descendants.
	}

	/**
	 * @param userLogin
	 * @see LoginProcessorListener#userLoggedOut(UserLogin)
	 */
	public void userLoggedOut(final UserLogin userLogin) {
		// empty: should be overridden by descendants.
	}
}
