/*
 * $Id: DatabaseDate.java,v 1.20.4.1 2006/03/23 15:05:04 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author $Author: bass $
 * @version $Revision: 1.20.4.1 $, $Date: 2006/03/23 15:05:04 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class DatabaseDate {

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd HHmmss");

	private DatabaseDate() {
		assert false;
	}

	/**
	 * @deprecated Use {@link java.sql.ResultSet#getTimestamp(String)} instead.
	 */
	@Deprecated
	public static Date fromQuerySubString(final ResultSet resultSet, final String column) throws SQLException {
		return resultSet.getTimestamp(column);
	}

	public static String toQuerySubString(final String column) {
		final String subString = "TO_CHAR(" + column + ", 'YYYYMMDD HH24MISS') " + column;
		return subString;
	}

	public static String toUpdateSubString(final Date date) {
		if (date != null) {
			final String subString = "TO_DATE('" + SDF.format(date) + "', 'YYYYMMDD HH24MISS')";
			return subString;
		}
		return null;
	}
}
