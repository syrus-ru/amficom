/*
 * $Id: DatabaseString.java,v 1.9 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @version $Revision: 1.9 $, $Date: 2005/03/04 08:05:49 $
 * @author $Author: bass $
 * @module util
 */
public class DatabaseString {

	private DatabaseString() {
		assert false;
	}

	/**
	 * @param string string to be made SQL query-ready, <code>null</code>s
	 *               are also allowed.
	 * @return original string with apostrophes (&apos;) escaped (i.&nbsp;e.
	 *         an SQL query-ready string).
	 * @since j2sdk 1.4
	 */
	public static String toQuerySubString(final String string) {
		/*
		 * Avoid code copypasting! If switching implementation here,
		 * then do the same below to avoid stack overflowing.
		 */
//*/
		return (string != null) ? string.replaceAll("(')", "$1$1") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
/*/
		return (string != null)
				? toQuerySubString(string, string.length())
				: "";
//*/
	}

	/**
	 * Does the same as {@link #toQuerySubString(String)}, but processes
	 * at most <code>length</code> chars (with <code>beginIndex</code> of
	 * <code>0</code> and <code>endIndex</code> of <code>length - 1</code>).
	 *
	 * @param string string to be made SQL query-ready, <code>null</code>s
	 *               are also allowed.
	 * @param length maximum length to this string
	 * @return original string with apostrophes (&apos;) escaped (i.&nbsp;e.
	 *         an SQL query-ready string).
	 * @since j2sdk 1.4
	 */
	public static String toQuerySubString(final String string, final int length) {
		/*
		 * Avoid code copypasting! If switching implementation here,
		 * then do the same above to avoid stack overflowing.
		 */
/*/
		return (string != null) ? (
				(string.length() > length) ? string.substring(0, length) : string
						).replaceAll("(')", "$1$1") : "";
/*/
		return (string != null)
				? toQuerySubString((string.length() > length)
						? string.substring(0, length)
						: string)
				: ""; //$NON-NLS-1$
//*/
	}

	/**
	 * 
	 * @param preparedStatement
	 * @param parameterIndex
	 * @param string string to query, null is also available
	 * @param length
	 * @throws SQLException
	 */
	public static void setString(PreparedStatement preparedStatement, int parameterIndex, String string, int length)
		throws SQLException {
		preparedStatement.setString(parameterIndex, 
			(string != null) ? ((string.length() > length) ? string.substring(0, length) : string) : ""); //$NON-NLS-1$
	}

	/**
	 * @param string 
	 * @return sql query without escape chars 
 	 * @since j2sdk 1.4
	 */
	public static String fromQuerySubString(final String string){
		// we mustn't unescape from substring because of this work done in sql driver
/*/
		return (string != null) ? string.replaceAll("(')\\1", "$1") : null;
/*/
		return string;
//*/
	}
}
