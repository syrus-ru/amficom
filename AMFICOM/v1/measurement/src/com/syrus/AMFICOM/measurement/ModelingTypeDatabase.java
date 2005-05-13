/*
 * $Id: ModelingTypeDatabase.java,v 1.33 2005/05/13 21:17:13 arseniy Exp $
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

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.33 $, $Date: 2005/05/13 21:17:13 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class ModelingTypeDatabase extends ActionTypeDatabase {

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

		final Map parameterTypeIdsMap = super.retrieveParameterTypesByOneQuery(modelingTypes,
				ObjectEntities.MODTYPPARTYPLINK_ENTITY,
				ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID);
		Map parameterTypeIdsModeMap;
		for (Iterator it = modelingTypes.iterator(); it.hasNext();) {
			final ModelingType modelingType = (ModelingType) it.next();
			parameterTypeIdsModeMap = (Map) parameterTypeIdsMap.get(modelingType.getId());
			if (parameterTypeIdsModeMap != null) {
				final java.util.Set inParameterTypeIds = (java.util.Set) parameterTypeIdsModeMap.get(ModelingTypeWrapper.MODE_IN);
				final java.util.Set outParameterTypeIds = (java.util.Set) parameterTypeIdsModeMap.get(ModelingTypeWrapper.MODE_OUT);
				modelingType.setParameterTypeIds(inParameterTypeIds, outParameterTypeIds);
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
		try {
			this.updateParameterTypes(Collections.singleton(storableObject));
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		try {
			this.updateParameterTypes(storableObjects);
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		this.updateParameterTypes(Collections.singleton(storableObject));
	}

	public void update(java.util.Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		this.updateParameterTypes(storableObjects);
	}

	private void updateParameterTypes(java.util.Set modelingTypes) throws UpdateObjectException {
		if (modelingTypes == null || modelingTypes.isEmpty())
			return;

		Map parameterTypeIdsMap = new HashMap(modelingTypes.size());
		for (Iterator it = modelingTypes.iterator(); it.hasNext();) {
			final ModelingType modelingType = (ModelingType) it.next();
			final Map parTypeIdsModeMap = new HashMap();
			parTypeIdsModeMap.put(ModelingTypeWrapper.MODE_IN, modelingType.getInParameterTypeIds());
			parTypeIdsModeMap.put(ModelingTypeWrapper.MODE_OUT, modelingType.getOutParameterTypeIds());
			parameterTypeIdsMap.put(modelingType.getId(), parTypeIdsModeMap);
		}

		Map dbParameterTypeIdsMap = null;
		try {
			dbParameterTypeIdsMap = super.retrieveDBParameterTypeIdsMap(modelingTypes,
					ObjectEntities.MODTYPPARTYPLINK_ENTITY,
					ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID);
		}
		catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		super.updateParameterTypes(parameterTypeIdsMap,
				dbParameterTypeIdsMap,
				ObjectEntities.MODTYPPARTYPLINK_ENTITY,
				ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID);
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
