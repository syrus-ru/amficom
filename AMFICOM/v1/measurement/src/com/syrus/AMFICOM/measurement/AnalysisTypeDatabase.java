/*
 * $Id: AnalysisTypeDatabase.java,v 1.93 2005/06/24 13:54:35 arseniy Exp $
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
import java.util.Map;
import java.util.Set;

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
 * @version $Revision: 1.93 $, $Date: 2005/06/24 13:54:35 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class AnalysisTypeDatabase extends ActionTypeDatabase {	

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	String getParameterTypeLinkTableName() {
		return ObjectEntities.ANATYPPARTYPLINK;
	}

	@Override
	String getActionTypeColumnName() {
		return AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID;
	}

	private AnalysisType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		throw new IllegalDataException("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {
		return ObjectEntities.ANALYSIS_TYPE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);		
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws IllegalDataException, SQLException{
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

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysisType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysisType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		super.retrieveEntity(analysisType);
		super.retrieveParameterTypeIdsByOneQuery(Collections.singleton(analysisType));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(analysisType));
	}

	private void retrieveMeasurementTypeIdsByOneQuery(final Set<AnalysisType> analysisTypes) throws RetrieveObjectException {
		if ((analysisTypes == null) || (analysisTypes.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> measurementTypeIdsMap = super.retrieveLinkedEntityIds(analysisTypes,
				ObjectEntities.MNTTYPANATYPEVATYP,
				AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

		for (final AnalysisType analysisType : analysisTypes) {
			final Identifier analysisTypeId = analysisType.getId();
			final Set<Identifier> measurementTypeIds = measurementTypeIdsMap.get(analysisTypeId);

			analysisType.setMeasurementTypeIds0(measurementTypeIds);
		}
	}

	@Override
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + analysisType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		final AnalysisType analysisType = this.fromStorableObject(storableObject);
		super.insertEntity(analysisType);
		try {
			super.updateParameterTypeIds(Collections.singleton(storableObject));
			this.updateMeasurementTypeIds(Collections.singleton(this.fromStorableObject(storableObject)));
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			super.updateParameterTypeIds(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final StorableObject storableObject, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		super.updateParameterTypeIds(Collections.singleton(storableObject));
		try {
			this.updateMeasurementTypeIds(Collections.singleton(this.fromStorableObject(storableObject)));
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	@Override
	public void update(final Set storableObjects, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		super.updateParameterTypeIds(storableObjects);
		try {
			this.updateMeasurementTypeIds(storableObjects);
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	private void updateMeasurementTypeIds(Set<AnalysisType> analysisTypes) throws IllegalDataException, UpdateObjectException {
		if (analysisTypes == null || analysisTypes.isEmpty())
			return;

		final Map<Identifier, Set<Identifier>> mtIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final AnalysisType analysisType : analysisTypes) {
			final Set<Identifier> mtIds = analysisType.getMeasurementTypeIds();
			mtIdsMap.put(analysisType.getId(), mtIds);
		}
		super.updateLinkedEntityIds(mtIdsMap,
				ObjectEntities.MNTTYPANATYPEVATYP,
				AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
	}

	@Override
	public void delete(Identifier id) {
		assert (id.getMajor() != ObjectEntities.ANALYSIS_TYPE_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		String analysisTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANATYPPARTYPLINK
					+ SQL_WHERE + AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID + EQUALS + analysisTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANALYSIS_TYPE
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + analysisTypeIdStr);
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	@Override
	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Set objects = super.retrieveByCondition(conditionQuery);
		super.retrieveParameterTypeIdsByOneQuery(objects);
		this.retrieveMeasurementTypeIdsByOneQuery(objects);
		return objects;
	}

}
