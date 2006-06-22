/*-
 * $Id: LoginProcessorListener.java,v 1.1 2006/06/22 12:56:05 cvsadmin Exp $
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
 * @version $Revision: 1.1 $, $Date: 2006/06/22 12:56:05 $
 * @module systemserver
 */
interface LoginProcessorListener {
	void userLoggedIn(final UserLogin userLogin);

	void userLoggedOut(final UserLogin userLogin);
}
