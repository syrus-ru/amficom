/*
 * $Id: DatabaseIdentifier.java,v 1.1 2004/11/16 09:23:14 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.Identifier;

/**
 * DB Identifier wrapper.
 * Main purpose is hide Identifier implementation and DB representation of it.
 * @version $Revision: 1.1 $, $Date: 2004/11/16 09:23:14 $
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
		preparedStatement.setString(parameterIndex, id.getCode());
	}
	
	public static Identifier getIdentifier(ResultSet resultSet, String columnName) throws SQLException{
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
		return new Identifier(resultSet.getString(columnName));
	}
}
