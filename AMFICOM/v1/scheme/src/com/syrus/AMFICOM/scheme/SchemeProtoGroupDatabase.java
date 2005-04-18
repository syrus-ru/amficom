/*-
 * $Id: SchemeProtoGroupDatabase.java,v 1.2 2005/04/18 15:26:06 max Exp $
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
import java.util.Set;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 15:26:06 $
 * @module scheme_v1
 */
public final class SchemeProtoGroupDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeProtoGroup fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SchemeProtoGroup)
			return (SchemeProtoGroup) storableObject;
		throw new IllegalDataException("SchemeProtoGroupDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
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
		SchemeProtoGroup spg = fromStorableObject(storableObject);
		super.insertEntity(spg);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		SchemeProtoGroup spg = fromStorableObject(storableObject);
		super.retrieveEntity(spg);
	}

	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	public Object retrieveObject(StorableObject storableObject,	int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		SchemeProtoGroup spg = fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + spg.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = SchemeProtoElementWrapper.COLUMN_NAME + COMMA
					+ SchemeProtoElementWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeProtoElementWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID;
		}
		return columns;
	}

	protected String getEnityName() {
		return ObjectEntities.SCHEME_PROTO_GROUP_ENTITY;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
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
		SchemeProtoGroup spg = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(spg.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
		+ APOSTOPHE + DatabaseString.toQuerySubString(spg.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
		+ DatabaseIdentifier.toSQLString(spg.getSymbol().getId()) + COMMA
		+ DatabaseIdentifier.toSQLString(spg.getParentSchemeProtoGroup().getId());
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
			PreparedStatement preparedStatement, int startParameterNumber) 
			throws IllegalDataException, SQLException {
		SchemeProtoGroup spg = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spg.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spg.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spg.getSymbol().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spg.getParentSchemeProtoGroup().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 */
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		SchemeProtoGroup spg;
		if (storableObject == null) {
			Date created = new Date(); 
			spg = new SchemeProtoGroup(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null);
		} else {
			spg = fromStorableObject(storableObject);
		}
		spg.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoGroupWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoGroupWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID));
		return spg;
	}
}
