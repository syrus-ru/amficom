/*
 * $Id: ConsoleErrorHandler.java,v 1.3 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.logging;

import java.sql.SQLException;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/03/04 08:05:49 $
 * @module util
 */
public final class ConsoleErrorHandler extends ErrorHandler {
	private static ConsoleErrorHandler instance = null;

	private ConsoleErrorHandler() {
		// empty
	}

	public static ErrorHandler getInstance() {
		if (instance == null)
			synchronized (ConsoleErrorHandler.class) {
				if (instance == null)
					instance = new ConsoleErrorHandler();
			}
		return instance;
	}

	public void error(Exception e) {
		System.err.println("ERROR: Caught a " + fqnToShortName(e.getClass()) + " (stack trace follows):"); //$NON-NLS-1$ //$NON-NLS-2$
		System.err.println("\tReason: " + e.getLocalizedMessage()); //$NON-NLS-1$
		e.printStackTrace();
	}

	public void error(final SQLException sqle) {
		System.err.println("ERROR: Caught a " + fqnToShortName(sqle.getClass()) + " chain (stack traces follow):"); //$NON-NLS-1$ //$NON-NLS-2$
		for (SQLException chainElement = sqle; chainElement != null; chainElement = chainElement.getNextException()) {
			System.err.println("\tReason: " + chainElement.getLocalizedMessage()); //$NON-NLS-1$
			System.err.println("\tSQLState: " + chainElement.getSQLState()); //$NON-NLS-1$
			System.err.println("\tVendor Code: " + chainElement.getErrorCode()); //$NON-NLS-1$
			chainElement.printStackTrace();
		}
	}
}
