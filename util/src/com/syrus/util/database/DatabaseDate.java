package com.syrus.util.database;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.syrus.util.Log;

public class DatabaseDate {
	
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd HHmmss");
	
	private DatabaseDate() {
		// private constuctor
	}

	public static Date fromQuerySubString(ResultSet resultset, String column) throws SQLException {		
		Date date = null;
		try{
		String dateStr = resultset.getString(column);
		if(dateStr != null)
			date = SDF.parse(dateStr);
		else Log.errorMessage("DatabaseDate.fromQuerySubString | date in column '" + column +"' is NULL");
		}catch(ParseException pe){
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
