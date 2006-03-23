/*
 * $Id: DatabaseDate.java,v 1.20.4.2 2006/03/23 16:38:41 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.20.4.2 $, $Date: 2006/03/23 16:38:41 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class DatabaseDate {

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd HHmmss");

	private DatabaseDate() {
		assert false;
	}

	/**
	 * <p>WARNING: prior to migration to
	 * {@link java.sql.ResultSet#getTimestamp(String)} database schemas
	 * should be updated in order to reference TIMESTAMP, not DATE. This
	 * is not an easy task and requires a tool for schema alteration
	 * to be written.</p>
	 * 
	 * <p>Currently, blind migration would result in an
	 * {@code IllegalArgumentException} at {@link java.sql.Timestamp#valueOf(String)}.</p>
	 * 
	 * @deprecated Use {@link java.sql.ResultSet#getTimestamp(String)} instead.
	 */
	@Deprecated
	public static Date fromQuerySubString(final ResultSet resultSet, final String column) throws SQLException {
		Date date = null;
		try {
			final String dateStr = resultSet.getString(column);
			if (dateStr != null) {
				date = SDF.parse(dateStr);
			}
		} catch (final ParseException pe) {
			Log.errorMessage("parse exception '" + pe.getMessage() + '\'');
		}
		return date;
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
