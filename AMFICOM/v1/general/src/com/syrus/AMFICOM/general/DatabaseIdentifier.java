/*-
 * $Id: DatabaseIdentifier.java,v 1.12 2005/06/17 19:51:11 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.util.Log;


/**
 * DB Identifier wrapper.
 * Main purpose is to hide Identifier implementation and DB representation of it.
 *
 * @version $Revision: 1.12 $, $Date: 2005/06/17 19:51:11 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class DatabaseIdentifier {
	private static final String NULL_SQL_STRING = "NULL";

	private DatabaseIdentifier() {
		assert false;
	}

	public static void setIdentifier(final PreparedStatement preparedStatement, final int parameterIndex, final Identifier id)
			throws SQLException {
		assert id != null : ErrorMessages.NON_NULL_EXPECTED;
		if (id.isVoid()) {
			preparedStatement.setObject(parameterIndex, null);
		} else {
			preparedStatement.setLong(parameterIndex, id.getIdentifierCode());
		}
	}

	public static Identifier getIdentifier(final ResultSet resultSet, final String columnName) throws SQLException {
		if (resultSet.getObject(columnName) == null) {
			Log.debugMessage("DatabaseIdentifier.getIdentifier() | Returning void identifier", Log.SEVERE);
			return Identifier.VOID_IDENTIFIER;
		}

		final long identifierCode = resultSet.getLong(columnName);

		assert identifierCode != 0 : ErrorMessages.NON_ZERO_EXPECTED;

		return new Identifier(identifierCode);
	}

	public static String toSQLString(final Identifier id) {
		assert id != null : ErrorMessages.NON_NULL_EXPECTED;
		return id.isVoid() ? NULL_SQL_STRING : Long.toString(id.getIdentifierCode());
	}
}
