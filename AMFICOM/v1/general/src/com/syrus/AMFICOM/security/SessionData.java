/*-
 * $Id: SessionData.java,v 1.1 2006/04/26 12:30:11 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.security;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/04/26 12:30:11 $
 * @module general
 */
interface SessionData {
	SessionKey getSessionKey();
}
