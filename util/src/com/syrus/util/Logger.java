/*-
 * $Id: Logger.java,v 1.6 2005/11/10 11:30:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.logging.Level;

/**
 * @version $Revision: 1.6 $, $Date: 2005/11/10 11:30:43 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public interface Logger {
	void debugMessage(final String message, final Level debugLevel);

	void debugException(final Throwable t, final Level debugLevel);

	void errorMessage(final String message);

	void errorException(final Throwable t);

	boolean isLoggable(final Level level);
}
