/*
 * $Id: DatabaseDate.java,v 1.16 2005/09/14 19:05:23 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.16 $, $Date: 2005/09/14 19:05:23 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class DatabaseDate {

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd HHmmss");

	private DatabaseDate() {
		assert false;
	}

	public static Date fromQuerySubString(ResultSet resultset, String column) throws SQLException {
		Date date = null;
		try {
			String dateStr = resultset.getString(column);
			if (dateStr != null)
				date = SDF.parse(dateStr);
		} catch (ParseException pe) {
			Log.errorMessage("DatabaseDate.fromQuerySubString | parse exception '" + pe.getMessage() + '\'');
		}
		return date;
	}

	public static String toQuerySubString(String column) {
		String subString = "TO_CHAR(" + column + ", 'YYYYMMDD HH24MISS') " + column;
		return subString;
	}

	public static String toUpdateSubString(Date date) {
		if(date != null) {
			String subString = "TO_DATE('" + SDF.format(date) + "', 'YYYYMMDD HH24MISS')";
			return subString;
		}
		return null;
	}
}
