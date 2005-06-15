/*-
 * $Id: PathElementDatabase.java,v 1.5 2005/06/15 13:17:17 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

import java.sql.*;
import java.util.Date;
import java.util.Set;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/15 13:17:17 $
 * @module scheme_v1
 */
public final class PathElementDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private PathElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PathElement)
			return (PathElement) storableObject;
		throw new IllegalDataException("PathElementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
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
		PathElement pe = fromStorableObject(storableObject);
		super.insertEntity(pe);
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
		PathElement pe = fromStorableObject(storableObject);
		super.retrieveEntity(pe);
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
		PathElement pe = fromStorableObject(storableObject);
		switch (retrieveKind) {
		default:
			Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
					+ " '" + pe.getId() + "'; argument: " + arg);
			return null;
		}
	}
	
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID + COMMA
					+ PathElementWrapper.COLUMN_SEQUENTIAL_NUMBER + COMMA
					+ PathElementWrapper.COLUMN_KIND + COMMA
					+ PathElementWrapper.COLUMN_START_ABSTRACT_SCHEME_PORT_ID + COMMA
					+ PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID + COMMA
					+ PathElementWrapper.COLUMN_SCHEME_CABLE_THREAD_ID + COMMA
					+ PathElementWrapper.COLUMN_SCHEME_LINK_ID;					
		}
		return columns;
	}

	protected short getEntityCode() {
		return ObjectEntities.PATH_ELEMENT_ENTITY_CODE;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
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
		PathElement pe = fromStorableObject(storableObject);
		String sql = DatabaseIdentifier.toSQLString(pe.getParentSchemePath().getId()) + COMMA
				+ APOSTOPHE + pe.getSequentialNumber() + APOSTOPHE + COMMA
				+ APOSTOPHE + pe.getKind().value() + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(pe.getStartAbstractSchemePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(pe.getEndAbstractSchemePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(pe.getSchemeCableLink().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(pe.getSchemeLink().getId());
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
		PathElement pe = fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, pe.getParentSchemePath().getId());
		preparedStatement.setInt(++startParameterNumber, pe.getSequentialNumber());
		preparedStatement.setInt(++startParameterNumber, pe.getKind().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, pe.getStartAbstractSchemePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, pe.getEndAbstractSchemePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, pe.getSchemeCableLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, pe.getSchemeLink().getId());
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
		PathElement pathElement;
		if (storableObject == null) {
			Date created = new Date();
			pathElement = new PathElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, 0, null, null, null, null, null);
		} else {
			pathElement = fromStorableObject(storableObject);
		}
		pathElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID),
				resultSet.getInt(PathElementWrapper.COLUMN_SEQUENTIAL_NUMBER),
				Kind.from_int(resultSet.getInt(PathElementWrapper.COLUMN_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_START_ABSTRACT_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_SCHEME_CABLE_THREAD_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_SCHEME_LINK_ID));
		return pathElement;
	}
}
