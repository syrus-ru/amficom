/*
 * $Id: DatabaseIdentifier.java,v 1.5 2004/12/27 12:59:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierDefaultFactory;
import com.syrus.AMFICOM.general.corba.IdentifierValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DB Identifier wrapper.
 * Main purpose is to hide Identifier implementation and DB representation of it.
 *
 * @version $Revision: 1.5 $, $Date: 2004/12/27 12:59:56 $
 * @author $Author: bass $
 * @module general_v1
 */
public class DatabaseIdentifier {
	private static final IdentifierValueFactory IDENTIFIER_VALUE_FACTORY
		= new IdentifierDefaultFactory();

	private DatabaseIdentifier() {
		// empty private constructor
	}

	public static void setIdentifier(final PreparedStatement preparedStatement, final int parameterIndex, final Identifier id)
			throws SQLException {
		/**
		 * @todo When changing DB Identifier model, change #setString() to
		 *       #setLong()
		 */
		preparedStatement.setString(parameterIndex, id != null ? id.getIdentifierString() : "");
	}

	public static void setVtIdentifier(final PreparedStatement preparedStatement, final int parameterIndex, final com.syrus.AMFICOM.general.corba.Identifier id)
			throws SQLException {
		/**
		 * @todo When changing DB Identifier model, change #setString() to
		 *       #setLong()
		 */
		preparedStatement.setString(parameterIndex, id != null ? id.identifierString() : "");
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
	}
	
	public static com.syrus.AMFICOM.general.corba.Identifier getVtIdentifier(final ResultSet resultSet, final String columnName) throws SQLException {
		/**
		 * @todo When changing DB Identifier model, change #getString() to
		 *       #getLong()
		 */
		final String idCode = resultSet.getString(columnName);
		return (idCode != null) && (idCode.indexOf(com.syrus.AMFICOM.general.corba.Identifier.SEPARATOR) > 0)
			? IDENTIFIER_VALUE_FACTORY.newInstanceFromString(idCode)
			: null;
	}

	/**
	 * @todo If you're changing this in order to make use of numeric identifiers,
	 * you must also change {@link #toSQLString(com.syrus.AMFICOM.general.corba.Identifier)} 
	 */
	public static String toSQLString(final Identifier id) {
		return StorableObjectDatabase.APOSTOPHE
			+ (id != null ? id.getIdentifierString() : "")
			+ StorableObjectDatabase.APOSTOPHE;
	}

	/**
	 * @todo If you're changing this in order to make use of numeric identifiers,
	 * you must also change {@link #toSQLString(Identifier)}
	 */
	public static String toSQLString(final com.syrus.AMFICOM.general.corba.Identifier id) {
		return StorableObjectDatabase.APOSTOPHE
			+ (id != null ? id.identifierString() : "")
			+ StorableObjectDatabase.APOSTOPHE;
	}

	/**
	 * @deprecated Use {@link #toSQLString(Identifier)} with
	 *             <code>null</code> as a parameter.
	 */
	public static String getNullSQLString() {
		return "''";
	}
}
