/*-
 * $Id: DatabaseIdentifier.java,v 1.13 2005/06/20 08:56:55 bass Exp $
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
import static com.syrus.util.Log.SEVERE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.util.Log;

/**
 * DB Identifier wrapper.
 * Main purpose is to hide Identifier implementation and DB representation of it.
 *
 * @version $Revision: 1.13 $, $Date: 2005/06/20 08:56:55 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class DatabaseIdentifier {
	private DatabaseIdentifier() {
		assert false;
	}

	public static void setIdentifier(final PreparedStatement preparedStatement, final int parameterIndex, final Identifier id)
			throws SQLException {
		assert id != null : NON_NULL_EXPECTED;
		if (id.isVoid()) {
			preparedStatement.setObject(parameterIndex, null);
		} else {
			preparedStatement.setLong(parameterIndex, id.getIdentifierCode());
		}
	}

	public static Identifier getIdentifier(final ResultSet resultSet, final String columnName) throws SQLException {
		if (resultSet.getObject(columnName) == null) {
			Log.debugMessage("DatabaseIdentifier.getIdentifier() | Returning void identifier", SEVERE);
			return VOID_IDENTIFIER;
		}

		final long identifierCode = resultSet.getLong(columnName);

		assert identifierCode != 0 : NON_ZERO_EXPECTED;

		return new Identifier(identifierCode);
	}

	public static String toSQLString(final Identifier id) {
		assert id != null : NON_NULL_EXPECTED;
		return id.isVoid() ? SQL_NULL_TRIMMED : Long.toString(id.getIdentifierCode());
	}
}
