/*-
 * $Id: Logger.java,v 1.4 2005/07/11 07:53:55 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.logging.Level;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/07/11 07:53:55 $
 * @module util
 */
public interface Logger {
	void debugMessage(final String message, final Level debugLevel);

	void debugException(final Throwable t, final Level debugLevel);

	void errorMessage(final String message);

	void errorException(final Throwable t);
}
