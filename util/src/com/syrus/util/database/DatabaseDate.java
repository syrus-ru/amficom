package com.syrus.util.database;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DatabaseDate {
	private DatabaseDate() {
	}

	public static Date fromQuerySubString(ResultSet resultset, String column) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		ParsePosition pp = new ParsePosition(0);
		Date date = null;
		String dateStr = resultset.getString(column);
		if(dateStr != null)
			date = sdf.parse(dateStr, pp);
		return date;
	}

	public static String toQuerySubString(String column) {
		String subString = new String("TO_CHAR(" + column + ", 'YYYYMMDD HH24MISS') " + column);
		return subString;
	}

	public static String toUpdateSubString(Date date) {
		if(date != null) {
			String subString = new String("TO_DATE('");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
			subString = subString.concat(sdf.format(date));
			subString = subString.concat("', 'YYYYMMDD HH24MISS')");
			return subString;
		}
		return null;
	}
}
