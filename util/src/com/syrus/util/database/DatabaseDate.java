/*
 * $Id: DatabaseDate.java,v 1.20 2005/10/31 12:29:58 bass Exp $
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
 * @version $Revision: 1.20 $, $Date: 2005/10/31 12:29:58 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class DatabaseDate {

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd HHmmss");

	private DatabaseDate() {
		assert false;
	}

	public static Date fromQuerySubString(final ResultSet resultSet, final String column) throws SQLException {
		Date date = null;
		try {
			final String dateStr = resultSet.getString(column);
			if (dateStr != null) {
				date = SDF.parse(dateStr);
			}
		} catch (ParseException pe) {
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
