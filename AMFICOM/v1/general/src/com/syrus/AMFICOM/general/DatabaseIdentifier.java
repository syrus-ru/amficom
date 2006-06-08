/*-
 * $Id: DatabaseIdentifier.java,v 1.19 2006/06/08 19:04:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_ZERO_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_NULL_TRIMMED;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * DB Identifier wrapper.
 * Main purpose is to hide Identifier implementation and DB representation of it.
 *
 * @version $Revision: 1.19 $, $Date: 2006/06/08 19:04:31 $
 * @author $Author: bass $
 * @module general
 */
public final class DatabaseIdentifier {
	private DatabaseIdentifier() {
		assert false;
	}

	public static void setIdentifier(final PreparedStatement preparedStatement, final int parameterIndex, final Identifier id)
			throws SQLException {
		assert id != null : NON_NULL_EXPECTED;
		if (id.isVoid()) {
			preparedStatement.setNull(parameterIndex, Types.BIGINT);
		} else {
			preparedStatement.setLong(parameterIndex, id.getIdentifierCode());
		}
	}

	/**
	 * A helper method similar to {@link PreparedStatement#setNull(int, int)}.
	 *
	 * @param preparedStatement
	 * @param parameterIndex
	 * @throws SQLException
	 */
	public static void setVoid(final PreparedStatement preparedStatement, final int parameterIndex)
	throws SQLException {
		setIdentifier(preparedStatement, parameterIndex, VOID_IDENTIFIER);
	}

	public static Identifier getIdentifier(final ResultSet resultSet, final String columnName) throws SQLException {
		final long identifierCode = resultSet.getLong(columnName);
		if (resultSet.wasNull()) {
			return VOID_IDENTIFIER;
		}

		assert identifierCode != 0 : NON_ZERO_EXPECTED;

		return Identifier.valueOf(identifierCode);
	}

	public static String toSQLString(final Identifier id) {
		assert id != null : NON_NULL_EXPECTED;
		return id.isVoid() ? SQL_NULL_TRIMMED : Long.toString(id.getIdentifierCode());
	}

	public static String voidToSQLString() {
		return toSQLString(VOID_IDENTIFIER);
	}
}
