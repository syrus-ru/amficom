/*
 * $Id: SqlUtilities.java,v 1.2 2004/10/19 08:12:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.server;

import java.math.BigDecimal;
import java.sql.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/10/19 08:12:16 $
 * @module servermisc_v1
 */
public final class SqlUtilities implements SqlConstants {
	private SqlUtilities() {
	}

	public static String getNonNullString(final ResultSet resultSet, final String columnName) throws SQLException {
		String returnValue = resultSet.getString(columnName);
		return ((returnValue == null) ? "" : returnValue); 
	}

	public static Timestamp getNonNullTimestamp(final ResultSet resultSet, final String columnName) throws SQLException {
		Timestamp returnValue = resultSet.getTimestamp(columnName);
		return ((returnValue == null) ? new Timestamp(System.currentTimeMillis()) : returnValue);
	}

	public static long getNonNullTimestampAsMillis(final ResultSet resultSet, final String columnName) throws SQLException {
		Timestamp returnValue = resultSet.getTimestamp(columnName);
		return ((returnValue == null) ? System.currentTimeMillis() : returnValue.getTime());
	}

	public static Date getNonNullDate(final ResultSet resultSet, final String columnName) throws SQLException {
		Date returnValue = resultSet.getDate(columnName);
		return ((returnValue == null) ? new Date(System.currentTimeMillis()) : returnValue);
	}

	public static long getNonNullDateAsMillis(final ResultSet resultSet, final String columnName) throws SQLException {
		Date returnValue = resultSet.getDate(columnName);
		return ((returnValue == null) ? System.currentTimeMillis() : returnValue.getTime());
	}

	public static int getNonNullInt(final ResultSet resultSet, final String columnName) throws SQLException {
		BigDecimal bigDecimal = resultSet.getBigDecimal(columnName);
		boolean bigDecimalNull = (bigDecimal == null);
		if (DEBUG && bigDecimalNull)
			System.err.println("WARNING: inserting SQL NULL (column " + columnName + ") into a primitive int type...");
		return (bigDecimalNull ? 0 : bigDecimal.intValue());
	}

	public static long getNonNullLong(final ResultSet resultSet, final String columnName) throws SQLException {
		BigDecimal bigDecimal = resultSet.getBigDecimal(columnName);
		boolean bigDecimalNull = (bigDecimal == null);
		if (DEBUG && bigDecimalNull)
			System.err.println("WARNING: inserting SQL NULL (column " + columnName + ") into a primitive long type...");
		return (bigDecimalNull ? 0L : bigDecimal.longValue());
	}

	public static float getNonNullFloat(final ResultSet resultSet, final String columnName) throws SQLException {
		BigDecimal bigDecimal = resultSet.getBigDecimal(columnName);
		boolean bigDecimalNull = (bigDecimal == null);
		if (DEBUG && bigDecimalNull)
			System.err.println("WARNING: inserting SQL NULL (column " + columnName + ") into a primitive float type...");
		return (bigDecimalNull ? 0f : bigDecimal.floatValue());
	}

	public static double getNonNullDouble(final ResultSet resultSet, final String columnName) throws SQLException {
		BigDecimal bigDecimal = resultSet.getBigDecimal(columnName);
		boolean bigDecimalNull = (bigDecimal == null);
		if (DEBUG && bigDecimalNull)
			System.err.println("WARNING: inserting SQL NULL (column " + columnName + ") into a primitive double type...");
		return (bigDecimalNull ? 0d : bigDecimal.doubleValue());
	}
}
