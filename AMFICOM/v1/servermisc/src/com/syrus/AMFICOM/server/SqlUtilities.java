/*
 * $Id: SqlUtilities.java,v 1.1 2004/10/18 15:16:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.server;

import java.sql.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/10/18 15:16:58 $
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
}
