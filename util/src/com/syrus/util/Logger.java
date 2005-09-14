/*-
 * $Id: Logger.java,v 1.5 2005/09/14 18:28:26 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.logging.Level;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/14 18:28:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public interface Logger {
	void debugMessage(final String message, final Level debugLevel);

	void debugException(final Throwable t, final Level debugLevel);

	void errorMessage(final String message);

	void errorException(final Throwable t);
}
