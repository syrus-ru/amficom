/*
 * $Id: MeasurementTypeDatabase.java,v 1.91 2005/05/13 21:17:13 arseniy Exp $
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
 * @version $Revision: 1.91 $, $Date: 2005/05/13 21:17:13 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementTypeDatabase extends ActionTypeDatabase  {

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

		final Map parameterTypeIdsMap = super.retrieveParameterTypesByOneQuery(measurementTypes,
				ObjectEntities.MNTTYPPARTYPLINK_ENTITY,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
		Map parameterTypeIdsModeMap;
		for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
			final MeasurementType measurementType = (MeasurementType) it.next();
			parameterTypeIdsModeMap = (Map) parameterTypeIdsMap.get(measurementType.getId());
			if (parameterTypeIdsModeMap != null) {
				final java.util.Set inParameterTypeIds = (java.util.Set) parameterTypeIdsModeMap.get(MeasurementTypeWrapper.MODE_IN);
				final java.util.Set outParameterTypeIds = (java.util.Set) parameterTypeIdsModeMap.get(MeasurementTypeWrapper.MODE_OUT);
				measurementType.setParameterTypeIds(inParameterTypeIds, outParameterTypeIds);
			}
		}
	}

	private void retrieveMeasurementPortTypesByOneQuery(java.util.Set measurementTypes) throws RetrieveObjectException {
		if ((measurementTypes == null) || (measurementTypes.isEmpty()))
			return;

		Map measurementPortTypeIdsMap = super.retrieveLinkedEntityIds(measurementTypes,
				ObjectEntities.MNTTYPEMEASPORTTYPELINK_ENTITY,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);

		for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
			final MeasurementType measurementType = (MeasurementType) it.next();
			final Identifier measurementTypeId = measurementType.getId();
			final java.util.Set measurementPortTypeIds = (java.util.Set) measurementPortTypeIdsMap.get(measurementTypeId);

			measurementType.setMeasurementPortTypeIds0(measurementPortTypeIds);
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
		try {
			this.updateParameterTypes(Collections.singleton(storableObject));
			this.updateMeasurementPortTypeIds(Collections.singleton(storableObject));
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		try {
			this.updateParameterTypes(storableObjects);
			this.updateMeasurementPortTypeIds(storableObjects);
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		this.updateParameterTypes(Collections.singleton(storableObject));
		try {
			this.updateMeasurementPortTypeIds(Collections.singleton(storableObject));
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	public void update(java.util.Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		this.updateParameterTypes(storableObjects);
		try {
			this.updateMeasurementPortTypeIds(storableObjects);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	private void updateParameterTypes(java.util.Set measurementTypes) throws UpdateObjectException {
		if (measurementTypes == null || measurementTypes.isEmpty())
			return;

		Map parameterTypeIdsMap = new HashMap(measurementTypes.size());
		for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
			final MeasurementType measurementType = (MeasurementType) it.next();
			final Map parTypeIdsModeMap = new HashMap();
			parTypeIdsModeMap.put(MeasurementTypeWrapper.MODE_IN, measurementType.getInParameterTypeIds());
			parTypeIdsModeMap.put(MeasurementTypeWrapper.MODE_OUT, measurementType.getOutParameterTypeIds());
			parameterTypeIdsMap.put(measurementType.getId(), parTypeIdsModeMap);
		}

		Map dbParameterTypeIdsMap = null;
		try {
			dbParameterTypeIdsMap = super.retrieveDBParameterTypeIdsMap(measurementTypes,
					ObjectEntities.MNTTYPPARTYPLINK_ENTITY,
					MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
		}
		catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		super.updateParameterTypes(parameterTypeIdsMap,
				dbParameterTypeIdsMap,
				ObjectEntities.MNTTYPPARTYPLINK_ENTITY,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
	}

	private void updateMeasurementPortTypeIds(java.util.Set measurementTypes) throws UpdateObjectException, IllegalDataException {
		if (measurementTypes == null || measurementTypes.isEmpty())
			return;

		Map mpIdsMap = new HashMap();
		MeasurementType measurementType;
		java.util.Set mpIds;
		for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
			measurementType = this.fromStorableObject((StorableObject) it.next());
			mpIds = measurementType.getMeasurementPortTypeIds();
			mpIdsMap.put(measurementType.getId(), mpIds);
		}

		this.updateLinkedEntityIds(mpIdsMap,
				ObjectEntities.MNTTYPEMEASPORTTYPELINK_ENTITY,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
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
