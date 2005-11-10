/*-
 * $Id: Logger.java,v 1.7 2005/11/10 15:47:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.logging.Level;

/**
 * @version $Revision: 1.7 $, $Date: 2005/11/10 15:47:46 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public interface Logger {
	void debugMessage(final String message, final Level debugLevel);

	void debugException(final Throwable t, final Level debugLevel);

	void errorMessage(final String message);

	void errorException(final Throwable t);

	/**
	 * @see java.util.logging.Logger#isLoggable(Level)
	 */
	boolean isLoggable(final Level level);

	/**
	 * @see java.util.logging.Logger#getLevel()
	 */
	Level getLevel();

	/**
	 * @see java.util.logging.Logger#setLevel(Level)
	 */
	void setLevel(final Level newLevel);
}
