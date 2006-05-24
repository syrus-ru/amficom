/*-
 * $Id: Logger.java,v 1.8 2006/05/24 10:43:52 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.logging.Level;

/**
 * @author Tashoyan Arseniy Feliksovich
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2006/05/24 10:43:52 $
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

	/**
	 * @param reverseIntValue AMFICOM-standard debug level, ranging from 1
	 *        to 10. Higher <code>reverseIntValue</code>s mean more verbose
	 *        output. This behaviour is opposite to that of
	 *        {@link Level Level}<code>.</code>{@link Level#intValue() intValue()},
	 *        that's the reason why parameter is named
	 *        <code>reverseIntValue</code>.
	 * @see #setLevel(Level)
	 * @see java.util.logging.Logger#setLevel(Level)
	 */
	void setLevel(final int reverseIntValue);
}
