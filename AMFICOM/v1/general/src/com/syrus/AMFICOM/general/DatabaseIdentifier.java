/*
 * $Id: DatabaseIdentifier.java,v 1.3 2004/11/17 08:22:45 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DB Identifier wrapper.
 * Main purpose is hide Identifier implementation and DB representation of it.
 * @version $Revision: 1.3 $, $Date: 2004/11/17 08:22:45 $
 * @author $Author: bob $
 * @module general_v1
 */
public class DatabaseIdentifier {

	private DatabaseIdentifier() {
		// empty private constructor
	}

	public static void setIdentifier(PreparedStatement preparedStatement, int parameterIndex, Identifier id)
			throws SQLException {
		/**
		 * @todo when change DB Identifier model ,change setString() to
		 *       setLong()
		 */
		preparedStatement.setString(parameterIndex, (id != null) ? id.getIdentifierString() : "");
	}
	
	public static Identifier getIdentifier(ResultSet resultSet, String columnName) throws SQLException{
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
		String idCode = resultSet.getString(columnName);
		return (idCode != null && idCode.indexOf(Identifier.SEPARATOR) > 0) ? new Identifier(idCode) : null;
	}
	
	public static String toSQLString(Identifier id) {
		return "'" + ((id != null) ? id.getIdentifierString() : "") + "'";
	}
	
	/**
	 * @deprecated use {@link #toSQLString(null)}
	 * @return
	 */
	public static String getNullSQLString() {
		return "''";
	}
}
