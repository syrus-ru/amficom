/*-
 * $Id: LoginProcessorListener.java,v 1.1 2006/04/26 17:09:17 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.security.UserLogin;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/04/26 17:09:17 $
 * @module leserver
 */
interface LoginProcessorListener {
	void userLoggedIn(final UserLogin userLogin);

	void userLoggedOut(final UserLogin userLogin);
}
