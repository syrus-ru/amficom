/*-
 * $Id: DatabaseIdentifier.java,v 1.17.4.1 2006/03/21 15:48:17 bass Exp $
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
 * @version $Revision: 1.17.4.1 $, $Date: 2006/03/21 15:48:17 $
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
}
