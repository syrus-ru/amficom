/*-
 * $Id: SchemeProtoElementDatabase.java,v 1.6 2005/06/15 13:17:17 bass Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/06/15 13:17:17 $
 * @module scheme_v1
 */
public final class SchemeProtoElementDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	private SchemeProtoElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SchemeProtoElement)
			return (SchemeProtoElement) storableObject;			
		throw new IllegalDataException("SchemeProtoElement.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 */
	public Object retrieveObject(StorableObject storableObject,	int retrieveKind, Object arg)
			throws IllegalDataException {
		SchemeProtoElement spe = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + spe.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeProtoElementWrapper.COLUMN_LABEL + COMMA
					+ SchemeProtoElementWrapper.COLUMN_EQUIPMENT_TYPE_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_UGO_CELL_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_SCHEME_CELL_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;
		}
		return columns;
	}

	protected short getEntityCode() {
		return ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE;
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
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject)
			throws IllegalDataException {
		SchemeProtoElement spe = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(spe.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(spe.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(spe.getLabel(), SchemeProtoElementWrapper.SIZE_LABEL_COLUMN) + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getEquipmentType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getSymbol().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getUgoCell().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getSchemeCell().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getParentSchemeProtoGroup().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getParentSchemeProtoElement().getId());
		return sql;
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement, int startParameterNumber) throws IllegalDataException,
			SQLException {
		SchemeProtoElement spe = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spe.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spe.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spe.getLabel(), SchemeProtoElementWrapper.SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getEquipmentType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getSymbol().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getUgoCell().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getSchemeCell().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getParentSchemeProtoGroup().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getParentSchemeProtoElement().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SchemeProtoElement spe;
		if (storableObject == null) {
			Date created = new Date();
			spe = new SchemeProtoElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null, null, null, null, null, null);
		} else {
			spe = fromStorableObject(storableObject);
		}
		spe.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SchemeProtoElementWrapper.COLUMN_LABEL)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_EQUIPMENT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID));
		return spe;
	}
}
