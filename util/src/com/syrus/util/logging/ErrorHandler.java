/*
 * $Id: ErrorHandler.java,v 1.1 2004/09/25 18:02:37 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.logging;

import java.sql.SQLException;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/09/25 18:02:37 $
 * @module util
 */
public abstract class ErrorHandler {
	/**
	 * @todo Rewrite.
	 */
	public static ErrorHandler getInstance() {
		return ConsoleErrorHandler.getInstance();
	}

	public abstract void error(final SQLException sqle);
}
