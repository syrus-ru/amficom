/*-
 * $Id: SchemeDatabase.java,v 1.4 2005/04/19 17:45:16 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

import java.sql.*;
import java.util.Date;
import java.util.Set;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/04/19 17:45:16 $
 * @module scheme_v1
 */
public final class SchemeDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private Scheme fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Scheme)
			return (Scheme) storableObject;			
		throw new IllegalDataException("Scheme.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObjects
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	public void insert(final Set storableObjects)
			throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	public void insert(StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		Scheme scheme = fromStorableObject(storableObject);
		super.insertEntity(scheme);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		Scheme scheme = fromStorableObject(storableObject);
		super.retrieveEntity(scheme);
	}

	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		Scheme scheme = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + scheme.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeWrapper.COLUMN_LABEL + COMMA
					+ SchemeWrapper.COLUMN_WIDTH + COMMA
					+ SchemeWrapper.COLUMN_HEIGHT + COMMA
					+ SchemeWrapper.COLUMN_DOMAIN_ID + COMMA
					+ SchemeWrapper.COLUMN_MAP_ID + COMMA
					+ SchemeWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeWrapper.COLUMN_UGO_CELL_ID + COMMA
					+ SchemeWrapper.COLUMN_SCHEME_CELL_ID + COMMA
					+ SchemeWrapper.COLUMN_KIND + COMMA
					+ SchemeWrapper.COLUMN_SCHEME_MONITORING_SOLUTION_ID + COMMA
					+ SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
		}
		return columns;
	}

	protected String getEnityName() {
		return ObjectEntities.SCHEME_ENTITY;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		Scheme scheme = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(scheme.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(scheme.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(scheme.getLabel(), SchemeWrapper.SIZE_LABEL_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + scheme.getWidth() + COMMA
				+ APOSTOPHE + scheme.getHeight() + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getDomainId().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getMap().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getSymbol().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getUgoCell().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getSchemeCell().getId()) + COMMA
				+ APOSTOPHE + scheme.getKind().value() + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getCurrentSchemeMonitoringSolution().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getParentSchemeElement().getId());
		return sql;
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	protected int setEntityForPreparedStatementTmpl(
			StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		Scheme scheme = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, scheme.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, scheme.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, scheme.getLabel(), SchemeWrapper.SIZE_LABEL_COLUMN);
		preparedStatement.setInt(++startParameterNumber, scheme.getWidth());
		preparedStatement.setInt(++startParameterNumber, scheme.getHeight());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getDomainId().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getMap().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getSymbol().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getUgoCell().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getSchemeCell().getId());
		preparedStatement.setInt(++startParameterNumber, scheme.getKind().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getCurrentSchemeMonitoringSolution().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getParentSchemeElement().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 */
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException,
			SQLException {
		Scheme scheme;
		if (storableObject == null) {
			Date created = new Date(); 
			scheme = new Scheme(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null, 0, 0, null, null, null, null, null, null, null);
		} else {
			scheme = fromStorableObject(storableObject);
		}
		scheme.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SchemeWrapper.COLUMN_LABEL)),
				resultSet.getInt(SchemeWrapper.COLUMN_WIDTH),
				resultSet.getInt(SchemeWrapper.COLUMN_HEIGHT),
				Kind.from_int(resultSet.getInt(SchemeWrapper.COLUMN_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_DOMAIN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_MAP_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_SCHEME_MONITORING_SOLUTION_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return scheme;
	}
}
