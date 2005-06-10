/*-
 * $Id: DatabaseIdentifier.java,v 1.9 2005/06/10 16:12:57 bass Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DB Identifier wrapper.
 * Main purpose is to hide Identifier implementation and DB representation of it.
 *
 * @version $Revision: 1.9 $, $Date: 2005/06/10 16:12:57 $
 * @author $Author: bass $
 * @module general_v1
 */
public class DatabaseIdentifier {
	private DatabaseIdentifier() {
		assert false;
	}

	public static void setIdentifier(final PreparedStatement preparedStatement, final int parameterIndex, final Identifier id)
			throws SQLException {
		/**
		 * @todo When changing DB Identifier model, change #setString() to
		 *       #setLong()
		 */
		preparedStatement.setString(parameterIndex, !(id == null || id.isVoid()) ? id.getIdentifierString() : "");
	}

	public static Identifier getIdentifier(final ResultSet resultSet, final String columnName) throws SQLException {
		/**
		 * @todo When changing DB Identifier model, change #getString() to
		 *       #getLong()
		 */
		final String idCode = resultSet.getString(columnName);
		return (idCode != null) && (idCode.indexOf(Identifier.SEPARATOR) > 0)
			? new Identifier(idCode)
			: null;
		/**
		 * @todo Should we return null or the void identifier singleton? 
		 */
	}

	public static String toSQLString(final Identifier id) {
		return StorableObjectDatabase.APOSTOPHE
			+ (id != null ? id.getIdentifierString() : "")
			+ StorableObjectDatabase.APOSTOPHE;
	}
}
