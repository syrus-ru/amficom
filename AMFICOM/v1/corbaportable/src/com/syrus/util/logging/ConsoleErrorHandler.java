/*
 * $Id: ConsoleErrorHandler.java,v 1.1 2005/06/08 14:24:41 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.logging;

import java.sql.SQLException;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/08 14:24:41 $
 * @deprecated
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
		System.err.println("ERROR: Caught a " + fqnToShortName(e.getClass()) + " (stack trace follows):");
		System.err.println("\tReason: " + e.getLocalizedMessage());
		e.printStackTrace();
	}

	public void error(final SQLException sqle) {
		System.err.println("ERROR: Caught a " + fqnToShortName(sqle.getClass()) + " chain (stack traces follow):");
		for (SQLException chainElement = sqle; chainElement != null; chainElement = chainElement.getNextException()) {
			System.err.println("\tReason: " + chainElement.getLocalizedMessage());
			System.err.println("\tSQLState: " + chainElement.getSQLState());
			System.err.println("\tVendor Code: " + chainElement.getErrorCode());
			chainElement.printStackTrace();
		}
	}
}
