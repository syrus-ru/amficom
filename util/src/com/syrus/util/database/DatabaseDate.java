/*
 * $Id: DatabaseDate.java,v 1.11 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.syrus.util.Log;

public class DatabaseDate {

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd HHmmss"); //$NON-NLS-1$

	private DatabaseDate() {
		assert false;
	}

	public static Date fromQuerySubString(ResultSet resultset, String column) throws SQLException {		
		Date date = null;
		try{
		String dateStr = resultset.getString(column);
		if(dateStr != null)
			date = SDF.parse(dateStr);
		else Log.errorMessage("DatabaseDate.fromQuerySubString | date in column '" + column +"' is NULL"); //$NON-NLS-1$ //$NON-NLS-2$
		}catch(ParseException pe){
			Log.errorMessage("DatabaseDate.fromQuerySubString | parse exception '" + pe.getMessage() + '\''); //$NON-NLS-1$
		}
		return date;
	}

	public static String toQuerySubString(String column) {
		String subString = "TO_CHAR(" + column + ", 'YYYYMMDD HH24MISS') " + column; //$NON-NLS-1$ //$NON-NLS-2$
		return subString;
	}

	public static String toUpdateSubString(Date date) {
		if(date != null) {
			String subString = "TO_DATE('" + SDF.format(date) + "', 'YYYYMMDD HH24MISS')"; //$NON-NLS-1$ //$NON-NLS-2$
			return subString;
		}
		return null;
	}
}
