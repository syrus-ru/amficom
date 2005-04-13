/*
 * $Id: ModelingTypeDatabase.java,v 1.31 2005/04/13 10:03:39 arseniy Exp $
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
 * @version $Revision: 1.31 $, $Date: 2005/04/13 10:03:39 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class ModelingTypeDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultipleSQLValues;

	private ModelingType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ModelingType)
			return (ModelingType) storableObject;
		throw new IllegalDataException("ModelingTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MODELINGTYPE_ENTITY;
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

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, modelingType.getCodename(), SIZE_CODENAME_COLUMN); 
		DatabaseString.setString(preparedStatement, ++startParameterNumber, modelingType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(modelingType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(modelingType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;		
		return values;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		ModelingType modelingType = (storableObject == null) ?
				new ModelingType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
												 null,
												 0L,
												 null,
												 null,
												 null,
												 null) : 
					this.fromStorableObject(storableObject);
		modelingType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return modelingType;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		this.retrieveEntity(modelingType);
		this.retrieveParameterTypesByOneQuery(Collections.singleton(modelingType));
	}

	private void retrieveParameterTypesByOneQuery(java.util.Set modelingTypes) throws RetrieveObjectException {
		if ((modelingTypes == null) || (modelingTypes.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE + COMMA
				+ ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID
				+ SQL_FROM + ObjectEntities.MODTYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
    sql.append(idsEnumerationString(modelingTypes, ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();

		try {
			statement = connection.createStatement();
			Log.debugMessage("ModelingTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map inParameterTypeIdsMap = new HashMap();
			Map outParameterTypeIdsMap = new HashMap();
			String parameterMode;
			Identifier parameterTypeId;
			Identifier modelingTypeId;
			java.util.Set inParameterTypeIds;
			java.util.Set outParameterTypeIds;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				modelingTypeId = DatabaseIdentifier.getIdentifier(resultSet, ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID);

				if (parameterMode.equals(ModelingTypeWrapper.MODE_IN)) {
					inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(modelingTypeId);
					if (inParameterTypeIds == null) {
						inParameterTypeIds = new HashSet();
						inParameterTypeIdsMap.put(modelingTypeId, inParameterTypeIds);
					}
					inParameterTypeIds.add(parameterTypeId);
				}
				else
					if (parameterMode.equals(ModelingTypeWrapper.MODE_OUT)) {
						outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(modelingTypeId);
						if (outParameterTypeIds == null) {
							outParameterTypeIds = new HashSet();
							outParameterTypeIdsMap.put(modelingTypeId, outParameterTypeIds);
						}
						outParameterTypeIds.add(parameterTypeId);
					}
					else
						Log.errorMessage("ModelingTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode '"
								+ parameterMode + "' for parameterTypeId '" + parameterTypeId
								+ "' of modeling type '" + modelingTypeId + "'");
			}

			ModelingType modelingType;
			for (Iterator it = modelingTypes.iterator(); it.hasNext();) {
				modelingType = (ModelingType) it.next();
				modelingTypeId = modelingType.getId();
				inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(modelingTypeId);
				outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(modelingTypeId);

				modelingType.setParameterTypes(GeneralStorableObjectPool.getStorableObjects(inParameterTypeIds, true),
						GeneralStorableObjectPool.getStorableObjects(outParameterTypeIds, true));
			}

		}
		catch (SQLException sqle) {
			String mesg = "ModelingTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for modeling types -- " + sqle.getMessage();
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  modelingType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		ModelingType modelingType = this.fromStorableObject(storableObject);
		this.insertEntity(modelingType);
		this.insertParameterTypes(modelingType);
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			ModelingType modelingType = this.fromStorableObject((StorableObject)it.next());
			insertParameterTypes(modelingType);
		}
	}

	private PreparedStatement insertParameterTypesPreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			String sql = SQL_INSERT_INTO
				+ ObjectEntities.MODTYPPARTYPLINK_ENTITY + OPEN_BRACKET
				+ ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID + COMMA
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

	private void updatePrepareStatementValues(PreparedStatement preparedStatement, ModelingType modelingType) throws SQLException {
		java.util.Set inParTyps = modelingType.getInParameterTypes();
		java.util.Set outParTyps = modelingType.getOutParameterTypes();
		Identifier modelingTypeId = modelingType.getId();
		Identifier parameterTypeId = null;
		String parameterMode = null;

		for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, modelingTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = ModelingTypeWrapper.MODE_IN;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("ModelingTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for modeling type " + modelingTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, modelingTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
			parameterMode = ModelingTypeWrapper.MODE_OUT;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("ModelingTypeDatabase.insertParameterTypes | Inserting parameter type '" + parameterTypeId + "' of parameter mode '" + parameterMode + "' for modeling type '" + modelingTypeId + "'", Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
	}

	private void insertParameterTypes(ModelingType modelingType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier modelingTypeId = modelingType.getId();
		try {
			preparedStatement = this.insertParameterTypesPreparedStatement();
			this.updatePrepareStatementValues(preparedStatement, modelingType);
		}
		catch (SQLException sqle) {
			String mesg = "ModelingTypeDatabase.insertParameterTypes | Cannot insert parameter type for modeling type '" + modelingTypeId + "' -- " + sqle.getMessage();
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
		assert (id.getMajor() != ObjectEntities.MODELINGTYPE_ENTITY_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		String modelingTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MODTYPPARTYPLINK_ENTITY
					+ SQL_WHERE + ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID + EQUALS + modelingTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MODELINGTYPE_ENTITY
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + modelingTypeIdStr);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	protected java.util.Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		java.util.Set collection = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypesByOneQuery(collection);
		return collection;
	}

}
