/*
 * $Id: ParameterSetDatabase.java,v 1.3 2005/06/17 12:38:55 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/17 12:38:55 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class ParameterSetDatabase extends StorableObjectDatabase {

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultipleSQLValues;

	protected short getEntityCode() {
		return ObjectEntities.PARAMETERSET_CODE;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = ParameterSetWrapper.COLUMN_SORT  + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}	

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		ParameterSet set = this.fromStorableObject(storableObject);
		String values = Integer.toString(set.getSort().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(set.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}	

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		ParameterSet set = this.fromStorableObject(storableObject);
		preparedStatement.setInt(++startParameterNumber, set.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, set.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	private ParameterSet fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ParameterSet)
			return (ParameterSet)storableObject;
		throw new IllegalDataException("ParameterSetDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		ParameterSet set = (storableObject == null) ?
				new ParameterSet(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, 0, null, null, null) :			
				this.fromStorableObject(storableObject);
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		set.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
						  DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
						  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
						  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
						  resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
						  resultSet.getInt(ParameterSetWrapper.COLUMN_SORT),
						  (description != null) ? description : "");
		return set;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ParameterSet set = this.fromStorableObject(storableObject);
		this.retrieveEntity(set);
		this.retrieveSetParametersByOneQuery(Collections.singleton(set));
		this.retrieveSetMELinksByOneQuery(Collections.singleton(set));
	}

	private void retrieveSetParametersByOneQuery(java.util.Set sets) throws RetrieveObjectException {
        if ((sets == null) || (sets.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ ParameterSetWrapper.LINK_COLUMN_SET_ID
				+ SQL_FROM + ObjectEntities.PARAMETER
				+ SQL_WHERE);
		sql.append(idsEnumerationString(sets, ParameterSetWrapper.LINK_COLUMN_SET_ID, true));

		Map setParametersMap = new HashMap();
		Identifier setId;
		java.util.Set setParameters;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterSetDatabase.retrieveSetParametersByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			ParameterType parameterType;
			Parameter parameter;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new Parameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
														parameterType,
														ByteArrayDatabase.toByteArray(resultSet.getBlob(ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				setId = DatabaseIdentifier.getIdentifier(resultSet, ParameterSetWrapper.LINK_COLUMN_SET_ID);
				setParameters = (java.util.Set) setParametersMap.get(setId);
				if (setParameters == null) {
					setParameters = new HashSet();
					setParametersMap.put(setId, setParameters);
				}
				setParameters.add(parameter);
			}
		} catch (SQLException sqle) {
			String mesg = "ParameterSetDatabase.retrieveSetParametersByOneQuery | Cannot retrieve parameters for set -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		ParameterSet set;
		for (Iterator it = sets.iterator(); it.hasNext();) {
			set = (ParameterSet) it.next();
			setId = set.getId();
			setParameters = (java.util.Set) setParametersMap.get(setId);

			if (setParameters != null)
				set.setParameters0((Parameter[]) setParameters.toArray(new Parameter[setParameters.size()]));
			else
				set.setParameters0(new Parameter[0]);
		}

	}

	private void retrieveSetMELinksByOneQuery(java.util.Set sets) throws RetrieveObjectException {
		if ((sets == null) || (sets.isEmpty()))
			return;

		Map meIdsMap = null;
		meIdsMap = this.retrieveLinkedEntityIds(sets,
				ObjectEntities.SETMELINK,
				ParameterSetWrapper.LINK_COLUMN_SET_ID,
				ParameterSetWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);

		ParameterSet set;
		Identifier setId;
		java.util.Set meIds;
		for (Iterator it = sets.iterator(); it.hasNext();) {
			set = (ParameterSet) it.next();
			setId = set.getId();
			meIds = (java.util.Set) meIdsMap.get(setId);

			set.setMonitoredElementIds0(meIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		ParameterSet set = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  set.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Log.debugMessage("ParameterSetDatabase.insert | 1 ", Log.DEBUGLEVEL01);
		ParameterSet set = this.fromStorableObject(storableObject);
		try {
			Log.debugMessage("ParameterSetDatabase.insert | before insertEntity ", Log.DEBUGLEVEL01);
			super.insertEntity(set);
			Log.debugMessage("ParameterSetDatabase.insert | before insertSetParameters ", Log.DEBUGLEVEL01);
			this.insertSetParameters(set);
			Log.debugMessage("ParameterSetDatabase.insert | after insertSetParameters ", Log.DEBUGLEVEL01);
			this.updateSetMELinks(Collections.singleton(set));
		} catch (CreateObjectException coe) {
			this.delete(set);
			throw coe;
		} catch (UpdateObjectException uoe) {
			this.delete(set);
			throw new CreateObjectException(uoe);
		}
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		Log.debugMessage("ParameterSetDatabase.insert | many ", Log.DEBUGLEVEL01);
		super.insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			ParameterSet set = this.fromStorableObject((StorableObject) it.next());
			this.insertSetParameters(set);
		}
		try {
			this.updateSetMELinks(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	private void insertSetParameters(ParameterSet set) throws CreateObjectException {
		Log.debugMessage("ParameterSetDatabase.insertSetParameters | ", Log.DEBUGLEVEL01);
		Identifier setId = set.getId();		
		Parameter[] setParameters = set.getParameters();
		Log.debugMessage("ParameterSetDatabase.insertSetParameters | setParameters count:" + setParameters.length, Log.DEBUGLEVEL01);
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.PARAMETER
			+ OPEN_BRACKET
			+ StorableObjectWrapper.COLUMN_ID  + COMMA
			+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
			+ ParameterSetWrapper.LINK_COLUMN_SET_ID + COMMA
			+ ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE + CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ SQL_FUNCTION_EMPTY_BLOB + CLOSE_BRACKET;
		Log.debugMessage("ParameterSetDatabase.insertSetParameters | try:" + sql, Log.DEBUGLEVEL01);
		PreparedStatement preparedStatement = null;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < setParameters.length; i++) {
				parameterId = setParameters[i].getId();
				parameterTypeId = setParameters[i].getType().getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, setId);

				Log.debugMessage("ParameterSetDatabase.insertSetParameters | Inserting parameter " + parameterTypeId.toString()
						+ " for set '" + setId + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				ByteArrayDatabase.saveAsBlob(setParameters[i].getValue(),
											 connection,
											 ObjectEntities.PARAMETER,
											 ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE,
											 StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		} catch (SQLException sqle) {
			String mesg = "ParameterSetDatabase.insertSetParameters | Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterTypeId.toString() + "' for set '" + setId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			this.updateSetMELinks(Collections.singleton(storableObject));
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	public void update(java.util.Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		try {
			this.updateSetMELinks(storableObjects);
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	private void updateSetMELinks(java.util.Set sets) throws IllegalDataException, UpdateObjectException {
		if (sets == null || sets.isEmpty())
			return;

		Map meIdsMap = new HashMap();
		ParameterSet set;
		java.util.Set meIds;
		for (Iterator it = sets.iterator(); it.hasNext();) {
			set = this.fromStorableObject((StorableObject) it.next());
			meIds = set.getMonitoredElementIds();
			meIdsMap.put(set.getId(), meIds);
		}

		super.updateLinkedEntityIds(meIdsMap,
				ObjectEntities.SETMELINK,
				ParameterSetWrapper.LINK_COLUMN_SET_ID,
				ParameterSetWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	public void delete(Identifier id) {
		assert (id.getMajor() == ObjectEntities.PARAMETERSET_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		String setIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.SETMELINK
					+ SQL_WHERE + ParameterSetWrapper.LINK_COLUMN_SET_ID + EQUALS + setIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.PARAMETER
					+ SQL_WHERE + ParameterSetWrapper.LINK_COLUMN_SET_ID + EQUALS + setIdStr);									
			statement.executeUpdate(SQL_DELETE_FROM
					+ this.getEntityName()
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + setIdStr);
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			} catch(SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	protected java.util.Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		java.util.Set collection = super.retrieveByCondition(conditionQuery);
		this.retrieveSetParametersByOneQuery(collection);
		this.retrieveSetMELinksByOneQuery(collection);
		return collection;
	}

}
