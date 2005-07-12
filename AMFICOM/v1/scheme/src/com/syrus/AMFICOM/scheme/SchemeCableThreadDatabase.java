/*-
 * $Id: SchemeCableThreadDatabase.java,v 1.7 2005/07/12 08:40:55 bass Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/07/12 08:40:55 $
 * @module scheme_v1
 */
public final class SchemeCableThreadDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeCableThread fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeCableThread)
			return (SchemeCableThread) storableObject;
		throw new IllegalDataException("SchemeCableThreadDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
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
		SchemeCableThread schemeCableThread = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemeCableThread.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeCableThreadWrapper.COLUMN_CABLE_THREAD_TYPE_ID + COMMA
					+ SchemeCableThreadWrapper.COLUMN_LINK_ID + COMMA
					+ SchemeCableThreadWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID + COMMA
					+ SchemeCableThreadWrapper.COLUMN_SOURCE_SCHEME_PORT_ID + COMMA
					+ SchemeCableThreadWrapper.COLUMN_TARGET_SCHEME_PORT_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMECABLETHREAD_CODE;
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
		SchemeCableThread schemeCableThread = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeCableThread.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeCableThread.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getCableThreadType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getLink().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getParentSchemeCableLink().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getSourceSchemePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getTargetSchemePort().getId());
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
		SchemeCableThread schemeCableThread = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableThread.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableThread.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getCableThreadType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getParentSchemeCableLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getSourceSchemePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getTargetSchemePort().getId());
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
		SchemeCableThread schemeCableThread;
		if (storableObject == null) {
			Date created = new Date();
			schemeCableThread = new SchemeCableThread(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null, null, null, null);
		} else {
			schemeCableThread = fromStorableObject(storableObject);
		}
		schemeCableThread.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableThreadWrapper.COLUMN_CABLE_THREAD_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableThreadWrapper.COLUMN_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableThreadWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableThreadWrapper.COLUMN_SOURCE_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableThreadWrapper.COLUMN_TARGET_SCHEME_PORT_ID));
		return schemeCableThread;
	}
}
