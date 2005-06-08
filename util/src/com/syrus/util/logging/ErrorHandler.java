/*
 * $Id: ErrorHandler.java,v 1.3 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.logging;

import java.sql.SQLException;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
public abstract class ErrorHandler {
	/**
	 * @todo Rewrite.
	 */
	public static ErrorHandler getInstance() {
		return ConsoleErrorHandler.getInstance();
	}

	/**
	 * Truncates package name from the fully qualified class name, thus
	 * returning short class name.
	 *
	 * @param fqn fully qualified class name.
	 * @return short class name.
	 */
	static String fqnToShortName(final String fqn) {
		return fqn.substring(fqn.lastIndexOf('.') + 1);
	}

	/**
	 * @param clazz the class whose short name is to be obtained.
	 * @return short class name.
	 * @see #fqnToShortName(String)
	 */
	static String fqnToShortName(final Class clazz) {
		return fqnToShortName(clazz.getName());
	}

	public abstract void error(final Exception e);

	public abstract void error(final SQLException sqle);
}
