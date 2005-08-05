/*
 * $Id: AnalysisTypeDatabase.java,v 1.101 2005/08/05 08:29:21 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.101 $, $Date: 2005/08/05 08:29:21 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class AnalysisTypeDatabase extends ActionTypeDatabase<AnalysisType> {	
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
	protected String getUpdateSingleSQLValuesTmpl(final AnalysisType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected AnalysisType updateEntityFromResultSet(final AnalysisType storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final AnalysisType analysisType = (storableObject == null)
				? new AnalysisType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						null,
						null)
					: storableObject;
		analysisType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return analysisType;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final AnalysisType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	public void retrieve(final AnalysisType storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		super.retrieveEntity(storableObject);
		super.retrieveParameterTypeIdsByOneQuery(Collections.singleton(storableObject));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(storableObject));
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
	public void insert(final Set<AnalysisType> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			super.updateParameterTypeIds(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set<AnalysisType> storableObjects) throws UpdateObjectException {
		super.updateEntities(storableObjects);
		super.updateParameterTypeIds(storableObjects);
		this.updateMeasurementTypeIds(storableObjects);
	}

	private void updateMeasurementTypeIds(final Set<AnalysisType> analysisTypes) throws UpdateObjectException {
		if (analysisTypes == null || analysisTypes.isEmpty()) {
			return;
		}

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
	public void delete(final Identifier id) {
		assert (id.getMajor() != ObjectEntities.ANALYSIS_TYPE_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		final String analysisTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
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
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	@Override
	protected Set<AnalysisType> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<AnalysisType> objects = super.retrieveByCondition(conditionQuery);
		super.retrieveParameterTypeIdsByOneQuery(objects);
		this.retrieveMeasurementTypeIdsByOneQuery(objects);
		return objects;
	}

}
