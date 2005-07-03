/*-
 * $Id: SchemeElementDatabase.java,v 1.6 2005/06/17 11:01:18 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

import java.sql.*;
import java.util.Date;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/06/17 11:01:18 $
 * @module scheme_v1
 */
public final class SchemeElementDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	private SchemeElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SchemeElement)
			return (SchemeElement) storableObject;			
		throw new IllegalDataException("SchemeElement.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 */
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException {
		SchemeElement schemeElement = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemeElement.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeElementWrapper.COLUMN_LABEL + COMMA
					+ SchemeElementWrapper.COLUMN_EQUIPMENT_TYPE_ID + COMMA
					+ SchemeElementWrapper.COLUMN_EQUIPMENT_ID + COMMA
					+ SchemeElementWrapper.COLUMN_KIS_ID + COMMA
					+ SchemeElementWrapper.COLUMN_SITE_NODE_ID + COMMA
					+ SchemeElementWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeElementWrapper.COLUMN_UGO_CELL_ID + COMMA
					+ SchemeElementWrapper.COLUMN_SCHEME_CELL_ID + COMMA
					+ SchemeElementWrapper.COLUMN_PARENT_SCHEME_ID + COMMA
					+ SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
		}
		return columns;
	}

	protected short getEntityCode() {
		return ObjectEntities.SCHEMEELEMENT_CODE;
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
		SchemeElement schemeElement = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeElement.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeElement.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeElement.getLabel(), SchemeElementWrapper.SIZE_LABEL_COLUMN) + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getEquipmentType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getEquipment().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getKis().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSiteNode().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSymbol().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getUgoCell().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSchemeCell().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getParentScheme().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getParentSchemeElement().getId());
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
		SchemeElement schemeElement = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getLabel(), SchemeElementWrapper.SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getEquipmentType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getEquipment().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getKis().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getSiteNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getSymbol().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getUgoCell().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getSchemeCell().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getParentScheme().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getParentSchemeElement().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SchemeElement schemeElement;
		if (storableObject == null) {
			Date created = new Date();
			schemeElement = new SchemeElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null, null, null, null, null, null, null, null, null);
		} else {
			schemeElement = fromStorableObject(storableObject);
		}
		schemeElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SchemeElementWrapper.COLUMN_LABEL)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_EQUIPMENT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_KIS_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_PARENT_SCHEME_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return schemeElement;
	}
}
