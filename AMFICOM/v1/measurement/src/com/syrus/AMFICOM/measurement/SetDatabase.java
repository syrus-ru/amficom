/*
 * $Id: SetDatabase.java,v 1.78 2005/03/10 15:20:56 arseniy Exp $
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.78 $, $Date: 2005/03/10 15:20:56 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class SetDatabase extends StorableObjectDatabase {

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultipleSQLValues;    

	protected String getEnityName() {
		return '"' + ObjectEntities.SET_ENTITY + '"';
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = COMMA
				+ SetWrapper.COLUMN_SORT  + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}	

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Set set = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ Integer.toString(set.getSort().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(set.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}	

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		Set set = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		preparedStatement.setInt(++i, set.getSort().value());
		DatabaseString.setString(preparedStatement, ++i, set.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return i;
	}

	private Set fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Set)
			return (Set)storableObject;
		throw new IllegalDataException("SetDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Set set = this.fromStorableObject(storableObject);
		this.retrieveEntity(set);
		this.retrieveSetParameters(set);
		this.retrieveSetMELinksByOneQuery(Collections.singletonList(set));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Set set = (storableObject == null) ?
				new Set(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, 0, null, null, null) :			
				this.fromStorableObject(storableObject);
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		set.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
						  DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
						  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
						  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
						  resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
						  resultSet.getInt(SetWrapper.COLUMN_SORT),
						  (description != null) ? description : "");
		return set;
	}

	private void retrieveSetParameters(Set set) throws RetrieveObjectException {
		Collection parameters = new HashSet();

		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID + COMMA			
			+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
			+ SetWrapper.LINK_COLUMN_PARAMETER_VALUE
			+ SQL_FROM
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ SQL_WHERE
			+ SetWrapper.LINK_COLUMN_SET_ID +EQUALS
			+ setIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSetParameters | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			SetParameter parameter;
			ParameterType parameterType;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
											 parameterType,
											 ByteArrayDatabase.toByteArray(resultSet.getBlob(SetWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				parameters.add(parameter);
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetParameters | Cannot retrieve parameters for set '" + setIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		set.setParameters0((SetParameter[]) parameters.toArray(new SetParameter[parameters.size()]));
	}

	private void retrieveSetParametersByOneQuery(Collection sets) throws RetrieveObjectException {
        if ((sets == null) || (sets.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ SetWrapper.LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ SetWrapper.LINK_COLUMN_SET_ID
				+ SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
				+ SQL_WHERE);
		try {
			sql.append(idsEnumerationString(sets, SetWrapper.LINK_COLUMN_SET_ID, true));
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Map setParametersMap = new HashMap();
		Identifier setId;
		Collection setParameters;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSetParametersByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			ParameterType parameterType;
			SetParameter parameter;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
														parameterType,
														ByteArrayDatabase.toByteArray(resultSet.getBlob(SetWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				setId = DatabaseIdentifier.getIdentifier(resultSet, SetWrapper.LINK_COLUMN_SET_ID);
				setParameters = (Collection) setParametersMap.get(setId);
				if (setParameters == null) {
					setParameters = new HashSet();
					setParametersMap.put(setId, setParameters);
				}
				setParameters.add(parameter);
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetParametersByOneQuery | Cannot retrieve parameters for set -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		Set set;
		for (Iterator it = sets.iterator(); it.hasNext();) {
			set = (Set) it.next();
			setId = set.getId();
			setParameters = (Collection) setParametersMap.get(setId);

			if (setParameters != null)
				set.setParameters0((SetParameter[]) setParameters.toArray(new SetParameter[setParameters.size()]));
			else
				set.setParameters0(new SetParameter[0]);
		}

	}

	private void retrieveSetMELinksByOneQuery(Collection sets) throws RetrieveObjectException {
		if ((sets == null) || (sets.isEmpty()))
			return;

		Map meIdsMap = null;
		try {
			meIdsMap = this.retrieveLinkedEntityIds(sets,
					ObjectEntities.SETMELINK_ENTITY,
					SetWrapper.LINK_COLUMN_SET_ID,
					SetWrapper.LINK_COLUMN_ME_ID);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Set set;
		Identifier setId;
		Collection meIds;
		for (Iterator it = sets.iterator(); it.hasNext();) {
			set = (Set) it.next();
			setId = set.getId();
			meIds = (Collection) meIdsMap.get(setId);

			set.setMonitoredElementIds0(meIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Set set = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  set.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Set set = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(set);
			this.insertSetParameters(set);
			this.insertSetMELinks(set);
		}
		catch (CreateObjectException coe) {
			this.delete(set);
			throw coe;
		}
	}
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Set set = this.fromStorableObject((StorableObject) it.next());
			this.insertSetParameters(set);
			this.insertSetMELinks(set);			
		}

	}

	private void insertSetParameters(Set set) throws CreateObjectException {
		Identifier setId = set.getId();
		SetParameter[] setParameters = set.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ StorableObjectWrapper.COLUMN_ID  + COMMA
			+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
			+ SetWrapper.LINK_COLUMN_SET_ID + COMMA
			+ SetWrapper.LINK_COLUMN_PARAMETER_VALUE + CLOSE_BRACKET
			+ SQL_VALUES 
			+ OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ SQL_FUNCTION_EMPTY_BLOB + CLOSE_BRACKET;
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

				Log.debugMessage("SetDatabase.insertSetParameters | Inserting parameter " + parameterTypeId.toString()
						+ " for set '" + setId + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				ByteArrayDatabase.saveAsBlob(setParameters[i].getValue(),
											 connection,
											 ObjectEntities.SETPARAMETER_ENTITY,
											 SetWrapper.LINK_COLUMN_PARAMETER_VALUE,
											 StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetParameters | Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterTypeId.toString() + "' for set '" + setId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void insertSetMELinks(Set set) throws CreateObjectException {
		Identifier setId = set.getId();
		Collection meIds = set.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ SetWrapper.LINK_COLUMN_SET_ID + COMMA 
			+ SetWrapper.LINK_COLUMN_ME_ID 
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION 
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier meId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				meId = ((Identifier)iterator.next());
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, setId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, meId);
				Log.debugMessage("SetDatabase.insertSetMELinks | Inserting link for set " + setId + " and monitored element " + meId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetMELinks | Cannot insert link for monitored element '" + meId + "' and set '" + setId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}  finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

/*
	private void createMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ SetWrapper.LINK_COLUMN_SET_ID + COMMA
			+ SetWrapper.LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET			
			+ setIdStr + COMMA
			+ meIdStr
			+ CLOSE_BRACKET;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set.createMEAttachment | Cannot attach set '" + setIdStr + "' to monitored element '" + meIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void deleteMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_DELETE_FROM 
					+ ObjectEntities.SETMELINK_ENTITY
					+ SQL_WHERE 
					+ SetWrapper.LINK_COLUMN_SET_ID + EQUALS
					+ setIdStr
					+ SQL_AND
					+ SetWrapper.LINK_COLUMN_ME_ID + EQUALS
					+ meIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.deleteMEAttachment | Cannot detach set '" + setIdStr + "' from monitored element '" + meIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
*/
/*
	private void setModified(Set set) throws UpdateObjectException {
		String setIdStr = DatabaseIdentifier.toSQLString(set.getId());
		String sql = SQL_UPDATE
					+ ObjectEntities.SET_ENTITY
					+ SQL_SET
					+ StorableObjectWrapper.COLUMN_MODIFIED + EQUALS
					+ DatabaseDate.toUpdateSubString(set.getModified()) + COMMA
					+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(set.getModifierId())
					+ SQL_WHERE + EQUALS + setIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.setModified | Cannot set modified for set '" + setIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
*/
	public void delete(Identifier id) throws IllegalDataException {
		if (id.getMajor() != ObjectEntities.SET_ENTITY_CODE)
			throw new IllegalDataException("SetDatabase.delete | Cannot delete object of code "
					+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'");

		String setIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.SETMELINK_ENTITY
					+ SQL_WHERE + SetWrapper.LINK_COLUMN_SET_ID + EQUALS + setIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.SETPARAMETER_ENTITY
					+ SQL_WHERE + SetWrapper.LINK_COLUMN_SET_ID + EQUALS + setIdStr);									
			statement.executeUpdate(SQL_DELETE_FROM
					+ this.getEnityName()
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + setIdStr);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	protected Collection retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Collection collection = super.retrieveByCondition(conditionQuery);
		this.retrieveSetParametersByOneQuery(collection);
		this.retrieveSetMELinksByOneQuery(collection);
		return collection;
	}

}
