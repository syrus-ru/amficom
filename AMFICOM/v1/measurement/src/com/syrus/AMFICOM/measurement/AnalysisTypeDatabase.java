/*
 * $Id: AnalysisTypeDatabase.java,v 1.84 2005/05/13 21:17:13 arseniy Exp $
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
 * @version $Revision: 1.84 $, $Date: 2005/05/13 21:17:13 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class AnalysisTypeDatabase extends ActionTypeDatabase {	

	private static String columns;
	private static String updateMultipleSQLValues;

	private AnalysisType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		throw new IllegalDataException("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.ANALYSISTYPE_ENTITY;
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
		AnalysisType analysisType = this.fromStorableObject(storableObject);		
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException{
		AnalysisType analysisType = storableObject == null ? 
				new AnalysisType(DatabaseIdentifier.getIdentifier(
			resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null, null) : 
					this.fromStorableObject(storableObject);
		analysisType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
								   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
								   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
								   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
								   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
								   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
								   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return analysisType;
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		super.retrieveEntity(analysisType);
		this.retrieveParameterTypesByOneQuery(Collections.singleton(analysisType));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(analysisType));
	}

	private void retrieveParameterTypesByOneQuery(java.util.Set analysisTypes) throws RetrieveObjectException {
		if ((analysisTypes == null) || (analysisTypes.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE + COMMA
				+ AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID
				+ SQL_FROM + ObjectEntities.ANATYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
    sql.append(idsEnumerationString(analysisTypes, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map inParameterTypeIdsMap = new HashMap();
			Map criteriaParameterTypeIdsMap = new HashMap();
			Map etalonParameterTypeIdsMap = new HashMap();
			Map outParameterTypeIdsMap = new HashMap();
			String parameterMode;
			Identifier parameterTypeId;
			Identifier analysisTypeId;
			java.util.Set inParameterTypeIds;
			java.util.Set criteriaParameterTypeIds;
			java.util.Set etalonParameterTypeIds;
			java.util.Set outParameterTypeIds;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				analysisTypeId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID);

				if (parameterMode.equals(AnalysisTypeWrapper.MODE_IN)) {
					inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(analysisTypeId);
					if (inParameterTypeIds == null) {
						inParameterTypeIds = new HashSet();
						inParameterTypeIdsMap.put(analysisTypeId, inParameterTypeIds);
					}
					inParameterTypeIds.add(parameterTypeId);
				}
				else
					if (parameterMode.equals(AnalysisTypeWrapper.MODE_CRITERION)) {
						criteriaParameterTypeIds = (java.util.Set) criteriaParameterTypeIdsMap.get(analysisTypeId);
						if (criteriaParameterTypeIds == null) {
							criteriaParameterTypeIds = new HashSet();
							criteriaParameterTypeIdsMap.put(analysisTypeId, criteriaParameterTypeIds);
						}
						criteriaParameterTypeIds.add(parameterTypeId);
					}
					else
						if (parameterMode.equals(AnalysisTypeWrapper.MODE_ETALON)) {
							etalonParameterTypeIds = (java.util.Set) etalonParameterTypeIdsMap.get(analysisTypeId);
							if (etalonParameterTypeIds == null) {
								etalonParameterTypeIds = new HashSet();
								etalonParameterTypeIdsMap.put(analysisTypeId, etalonParameterTypeIds);
							}
							etalonParameterTypeIds.add(parameterTypeId);
						}
						else
							if (parameterMode.equals(AnalysisTypeWrapper.MODE_OUT)) {
								outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(analysisTypeId);
								if (outParameterTypeIds == null) {
									outParameterTypeIds = new HashSet();
									outParameterTypeIdsMap.put(analysisTypeId, outParameterTypeIds);
								}
								outParameterTypeIds.add(parameterTypeId);
							}
							else
								Log.errorMessage("AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | ERROR: Unknown parameter mode '"
										+ parameterMode + "' for parameterTypeId '" + parameterTypeId + "' of analysis type '" + analysisTypeId + "'");
			}

			AnalysisType analysisType;
			for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
				analysisType = (AnalysisType) it.next();
				analysisTypeId = analysisType.getId();
				inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(analysisTypeId);
				criteriaParameterTypeIds = (java.util.Set) criteriaParameterTypeIdsMap.get(analysisTypeId);
				etalonParameterTypeIds = (java.util.Set) etalonParameterTypeIdsMap.get(analysisTypeId);
				outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(analysisTypeId);

				analysisType.setParameterTypeIds(inParameterTypeIds, criteriaParameterTypeIds, etalonParameterTypeIds, outParameterTypeIds);
			}

		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Cannot retrieve parameter type ids for analysis types -- "
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
	}

	private void retrieveMeasurementTypeIdsByOneQuery(java.util.Set analysisTypes) throws RetrieveObjectException {
		if ((analysisTypes == null) || (analysisTypes.isEmpty()))
			return;

		Map measurementTypeIdsMap = super.retrieveLinkedEntityIds(analysisTypes,
				ObjectEntities.MNTTYPANATYPEVATYP_ENTITY,
				AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

		for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
			final AnalysisType analysisType = (AnalysisType) it.next();
			final Identifier analysisTypeId = analysisType.getId();
			final java.util.Set measurementTypeIds = (java.util.Set) measurementTypeIdsMap.get(analysisTypeId);

			analysisType.setMeasurementTypeIds0(measurementTypeIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + analysisType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		this.insertEntity(analysisType);
		try {
			this.updateParameterTypes(Collections.singleton(storableObject));
			this.updateMeasurementTypeIds(Collections.singleton(storableObject));
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		try {
			this.updateParameterTypes(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
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
			this.updateMeasurementTypeIds(Collections.singleton(storableObject));
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
			this.updateMeasurementTypeIds(storableObjects);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	private void updateParameterTypes(java.util.Set analysisTypes) throws UpdateObjectException {
		if (analysisTypes == null || analysisTypes.isEmpty())
			return;

		Map parameterTypeIdsMap = new HashMap(analysisTypes.size());
		for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
			final AnalysisType analysisType = (AnalysisType) it.next();
			final Map parTypeIdsModeMap = new HashMap();
			parTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_IN, analysisType.getInParameterTypeIds());
			parTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_CRITERION, analysisType.getCriteriaParameterTypeIds());
			parTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_ETALON, analysisType.getEtalonParameterTypeIds());
			parTypeIdsModeMap.put(AnalysisTypeWrapper.MODE_OUT, analysisType.getOutParameterTypeIds());
			parameterTypeIdsMap.put(analysisType.getId(), parTypeIdsModeMap);
		}

		Map dbParameterTypeIdsMap = null;
		try {
			dbParameterTypeIdsMap = super.retrieveDBParameterTypeIdsMap(analysisTypes,
					ObjectEntities.ANATYPPARTYPLINK_ENTITY,
					AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID);
		}
		catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		super.updateParameterTypes(parameterTypeIdsMap,
				dbParameterTypeIdsMap,
				ObjectEntities.ANATYPPARTYPLINK_ENTITY,
				AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID);
	}

	private void updateMeasurementTypeIds(java.util.Set analysisTypes) throws IllegalDataException, UpdateObjectException {
		if (analysisTypes == null || analysisTypes.isEmpty())
			return;

		Map mtIdsMap = new HashMap();
		AnalysisType analysisType;
		java.util.Set mtIds;
		for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
			analysisType = this.fromStorableObject((StorableObject) it.next());
			mtIds = analysisType.getMeasurementTypeIds();
			mtIdsMap.put(analysisType.getId(), mtIds);
		}

		this.updateLinkedEntityIds(mtIdsMap,
				ObjectEntities.MNTTYPANATYPEVATYP_ENTITY,
				AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
	}

	public void delete(Identifier id) {
		assert (id.getMajor() != ObjectEntities.ANALYSISTYPE_ENTITY_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		String analysisTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANATYPPARTYPLINK_ENTITY
					+ SQL_WHERE + AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID + EQUALS + analysisTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANALYSISTYPE_ENTITY
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + analysisTypeIdStr);
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
		this.retrieveMeasurementTypeIdsByOneQuery(collection);
		return collection;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysisType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysisType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

}
