/*-
 * $Id: PathElementDatabase.java,v 1.9 2005/07/14 16:08:08 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.DataPackage.Kind;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/07/14 16:08:08 $
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
	@Override
	public void insert(final Set<? extends StorableObject> storableObjects)
			throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	@Override
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
	@Override
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
	@Override
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
	
	@Override
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

	@Override
	protected short getEntityCode() {
		return PATHELEMENT_CODE;
	}

	@Override
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
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		PathElement pe = fromStorableObject(storableObject);
		String sql = DatabaseIdentifier.toSQLString(pe.getParentSchemePath().getId()) + COMMA
				+ APOSTROPHE + pe.getSequentialNumber() + APOSTROPHE + COMMA
				+ APOSTROPHE + pe.getKind().value() + APOSTROPHE + COMMA
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
	@Override
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
	@Override
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
