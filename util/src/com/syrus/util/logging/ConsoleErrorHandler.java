/*
 * $Id: ConsoleErrorHandler.java,v 1.1 2004/09/25 18:02:37 bass Exp $
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
public final class ConsoleErrorHandler extends ErrorHandler {
	private static ConsoleErrorHandler instance = null;

	private ConsoleErrorHandler() {
	}

	public static ErrorHandler getInstance() {
		if (instance == null)
			synchronized (ConsoleErrorHandler.class) {
				if (instance == null)
					instance = new ConsoleErrorHandler();
			}
		return instance;
	}

	public void error(final SQLException sqle) {
		System.err.println("ERROR: Caught an SQLException chain (stack traces follow):");
		for (SQLException chainElement = sqle; chainElement != null; chainElement = chainElement.getNextException()) {
			System.err.println("\tReason: " + chainElement.getLocalizedMessage());
			System.err.println("\tSQLState: " + chainElement.getSQLState());
			System.err.println("\tVendor Code: " + chainElement.getErrorCode());
			chainElement.printStackTrace();
		}
	}
}
