/*
 * $Id: MeasurementTypeDatabase.java,v 1.90 2005/05/11 08:13:13 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
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
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.90 $, $Date: 2005/05/11 08:13:13 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementTypeDatabase extends StorableObjectDatabase  {

	private static String columns;
	private static String updateMultipleSQLValues;

	private MeasurementType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementType)
			return (MeasurementType)storableObject;
		throw new IllegalDataException("MeasurementTypeDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}	

	protected String getEnityName() {
		return ObjectEntities.MEASUREMENTTYPE_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA 
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
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(measurementType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementType measurementType = (storableObject == null) ? 
				new MeasurementType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
														null,
														0L,
														null,
														null,
														null,
														null,
														null) : 
				this.fromStorableObject(storableObject);
		measurementType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									  DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									  DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									  resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									  DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
									  DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return measurementType;
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementType);
		this.retrieveParameterTypesByOneQuery(Collections.singleton(measurementType));
		this.retrieveMeasurementPortTypesByOneQuery(Collections.singleton(measurementType));
	}

	private void retrieveParameterTypesByOneQuery(java.util.Set measurementTypes) throws RetrieveObjectException {
		if ((measurementTypes == null) || (measurementTypes.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE + COMMA
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID
				+ SQL_FROM + ObjectEntities.MNTTYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
		sql.append(idsEnumerationString(measurementTypes, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID, true));

    Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map inParameterTypeIdsMap = new HashMap();
			Map outParameterTypeIdsMap = new HashMap();
			String parameterMode;
			Identifier parameterTypeId;
			Identifier measurementTypeId;
			java.util.Set inParameterTypeIds;
			java.util.Set outParameterTypeIds;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				measurementTypeId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

				if (parameterMode.equals(MeasurementTypeWrapper.MODE_IN)) {
					inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(measurementTypeId);
					if (inParameterTypeIds == null) {
						inParameterTypeIds = new HashSet();
						inParameterTypeIdsMap.put(measurementTypeId, inParameterTypeIds);
					}
					inParameterTypeIds.add(parameterTypeId);
				}
				else
					if (parameterMode.equals(MeasurementTypeWrapper.MODE_OUT)) {
						outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(measurementTypeId);
						if (outParameterTypeIds == null) {
							outParameterTypeIds = new HashSet();
							outParameterTypeIdsMap.put(measurementTypeId, outParameterTypeIds);
						}
						outParameterTypeIds.add(parameterTypeId);
					}
					else
						Log.errorMessage("MeasurementTypeDatabase.retrieveParameterTypesByOneQuery | ERROR: Unknown parameter mode '"
								+ parameterMode + "' for parameterTypeId '" + parameterTypeId
								+ "' of measurement type '" + measurementTypeId + "'");

			}

			MeasurementType measurementType;
			for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
				measurementType = (MeasurementType) it.next();
				measurementTypeId = measurementType.getId();
				inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(measurementTypeId);
				outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(measurementTypeId);

				final java.util.Set inParameterTypes = (inParameterTypeIds != null && !inParameterTypeIds.isEmpty())
						? GeneralStorableObjectPool.getStorableObjects(inParameterTypeIds, true) : Collections.EMPTY_SET;
				final java.util.Set outParameterTypes = (outParameterTypeIds != null && !outParameterTypeIds.isEmpty())
						? GeneralStorableObjectPool.getStorableObjects(outParameterTypeIds, true) : Collections.EMPTY_SET;
				measurementType.setParameterTypes(inParameterTypes, outParameterTypes);
			}

		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveParameterTypesByOneQuery | Cannot retrieve parameter type ids for measurement types -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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
	}

	private void retrieveMeasurementPortTypesByOneQuery(java.util.Set measurementTypes) throws RetrieveObjectException {
		if ((measurementTypes == null) || (measurementTypes.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID + COMMA
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID
				+ SQL_FROM + ObjectEntities.MNTTYPEMEASPORTTYPELINK_ENTITY
				+ SQL_WHERE);
		sql.append(idsEnumerationString(measurementTypes, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID, true));

    Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveMeasurementPortTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map measurementPortTypeIdsMap = new HashMap();
			Identifier measurementPortTypeId;
			Identifier measurementTypeId;
			java.util.Set measurementPortTypeIds;
			while (resultSet.next()) {
				measurementPortTypeId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
				measurementTypeId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

				measurementPortTypeIds = (java.util.Set) measurementPortTypeIdsMap.get(measurementTypeId);
				if (measurementPortTypeIds == null) {
					measurementPortTypeIds = new HashSet();
					measurementPortTypeIdsMap.put(measurementTypeId, measurementPortTypeIds);
				}
				measurementPortTypeIds.add(measurementPortTypeId);
			}

			MeasurementType measurementType;
			for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
				measurementType = (MeasurementType) it.next();
				measurementTypeId = measurementType.getId();
				measurementPortTypeIds = (java.util.Set) measurementPortTypeIdsMap.get(measurementTypeId);

				final java.util.Set measurementPortTypes = (measurementPortTypeIds != null && !measurementPortTypeIds.isEmpty())
						? ConfigurationStorableObjectPool.getStorableObjects(measurementPortTypeIds, true) : Collections.EMPTY_SET;
				measurementType.setMeasurementPortTypes0(measurementPortTypes);
			}

		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveMeasurementPortTypesByOneQuery | Cannot retrieve parameters for result -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + measurementType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.insertEntity(measurementType);
		this.insertParameterTypes(measurementType);
		this.insertMeasurementPortTypes(measurementType);
		
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			MeasurementType measurementType = this.fromStorableObject((StorableObject)it.next());
			this.insertParameterTypes(measurementType);
			this.insertMeasurementPortTypes(measurementType);
		}
	}

	private PreparedStatement insertParameterTypesPreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			String sql = SQL_INSERT_INTO
				+ ObjectEntities.MNTTYPPARTYPLINK_ENTITY + OPEN_BRACKET
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		}
		finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}

	private PreparedStatement insertMeasurementPortTypePreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			String sql = SQL_INSERT_INTO
				+ ObjectEntities.MNTTYPEMEASPORTTYPELINK_ENTITY + OPEN_BRACKET
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		}
		finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}

	private void updatePrepareStatementValues(PreparedStatement preparedStatement, MeasurementType measurementType)
			throws SQLException {
		java.util.Set inParTyps = measurementType.getInParameterTypes();
		java.util.Set outParTyps = measurementType.getOutParameterTypes();
		Identifier measurementTypeId = measurementType.getId();
		Identifier parameterTypeId = null;
		String parameterMode = null;

		for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, measurementTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = MeasurementTypeWrapper.MODE_IN;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("MeasurementTypeDatabase.updatePrepareStatementValues | Inserting parameter type " + parameterTypeId
					+ " of parameter mode '" + parameterMode + "' for measurement type " + measurementTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, measurementTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
			parameterMode = MeasurementTypeWrapper.MODE_OUT;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("MeasurementTypeDatabase.updatePrepareStatementValues | Inserting parameter type '" + parameterTypeId
					+ "' of parameter mode '" + parameterMode + "' for measurement type '" + measurementTypeId + "'", Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
	}

	private void updateMeasurementPortTypePrepareStatementValues(PreparedStatement preparedStatement,
			MeasurementType measurementType) throws SQLException {
		java.util.Set measurementPortTypes = measurementType.getMeasurementPortTypes();
		Identifier measurementTypeId = measurementType.getId();
		Identifier measurementPortTypeId = null;
		
		for (Iterator iterator = measurementPortTypes.iterator(); iterator.hasNext();) {			
			measurementPortTypeId = ((MeasurementPortType) iterator.next()).getId();			
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, measurementTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, measurementPortTypeId);
			Log.debugMessage("MeasurementTypeDatabase.updateMeasurementPortTypePrepareStatementValues | Inserting measurement port type "
					+ measurementPortTypeId + "' for measurement type " + measurementTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
	}

	private void insertParameterTypes(MeasurementType measurementType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier measurementTypeId = measurementType.getId();
		try {
			preparedStatement = this.insertParameterTypesPreparedStatement();
			this.updatePrepareStatementValues(preparedStatement, measurementType);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertParameterTypes | Cannot insert parameter type for measurement type '"
					+ measurementTypeId + "' -- " + sqle.getMessage();
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
		}
	}
	
	private void insertMeasurementPortTypes(MeasurementType measurementType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier measurementTypeId = measurementType.getId();
		try {
			preparedStatement = this.insertMeasurementPortTypePreparedStatement();
			this.updateMeasurementPortTypePrepareStatementValues(preparedStatement, measurementType);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertMeasurementPortTypes | Cannot insert measurement port type for measurement type '"
					+ measurementTypeId + "' -- " + sqle.getMessage();
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
		}
	}

	public void delete(Identifier id) {
		assert (id.getMajor() != ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		String measurementTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MNTTYPEMEASPORTTYPELINK_ENTITY
					+ SQL_WHERE + MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MNTTYPPARTYPLINK_ENTITY
					+ SQL_WHERE + MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MEASUREMENTTYPE_ENTITY 
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + measurementTypeIdStr);

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

	protected java.util.Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		java.util.Set set = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypesByOneQuery(set);
		this.retrieveMeasurementPortTypesByOneQuery(set);
		return set;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber)
			throws IllegalDataException, SQLException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
			DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementType.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
		}

}
