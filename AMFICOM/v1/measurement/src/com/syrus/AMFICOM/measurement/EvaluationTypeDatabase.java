/*
 * $Id: EvaluationTypeDatabase.java,v 1.98 2005/08/19 14:19:04 arseniy Exp $
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
 * @version $Revision: 1.98 $, $Date: 2005/08/19 14:19:04 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class EvaluationTypeDatabase extends ActionTypeDatabase<EvaluationType> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	String getParameterTypeLinkTableName() {
		return ObjectEntities.EVATYPPARTYPLINK;
	}

	@Override
	String getActionTypeColumnName() {
		return EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID;
	}

	@Override
	protected short getEntityCode() {
		return ObjectEntities.EVALUATION_TYPE_CODE;
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
	protected int setEntityForPreparedStatementTmpl(final EvaluationType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final EvaluationType storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;		
		return values;
	}

	@Override
	protected EvaluationType updateEntityFromResultSet(final EvaluationType storableObject, final ResultSet resultSet)
		throws IllegalDataException, SQLException {
		final EvaluationType evaluationType = (storableObject == null)
				? new EvaluationType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
		evaluationType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return evaluationType;
	}

	@Override
	public void retrieve(final EvaluationType storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		super.retrieveEntity(storableObject);
		super.retrieveParameterTypesByOneQuery(Collections.singleton(storableObject));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(storableObject));
	}

	private void retrieveMeasurementTypeIdsByOneQuery(final Set<EvaluationType> evaluationTypes) throws RetrieveObjectException {
		if ((evaluationTypes == null) || (evaluationTypes.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> measurementTypeIdsMap = super.retrieveLinkedEntityIds(evaluationTypes,
				ObjectEntities.MNTTYPANATYPEVATYP,
				EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

		for (final EvaluationType evaluationType : evaluationTypes) {
			final Identifier evaluationTypeId = evaluationType.getId();
			final Set<Identifier> measurementTypeIds = measurementTypeIdsMap.get(evaluationTypeId);

			evaluationType.setMeasurementTypeIds0(measurementTypeIds);
		}
	}

	@Override
	public void insert(final Set<EvaluationType> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			super.updateParameterTypes(storableObjects);
			this.updateMeasurementTypeIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set<EvaluationType> storableObjects) throws UpdateObjectException {
		super.updateEntities(storableObjects);
		super.updateParameterTypes(storableObjects);
		this.updateMeasurementTypeIds(storableObjects);
	}

	private void updateMeasurementTypeIds(final Set<EvaluationType> evaluationTypes)
			throws UpdateObjectException {
		if (evaluationTypes == null || evaluationTypes.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> mtIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final EvaluationType evaluationType : evaluationTypes) {
			final Set<Identifier> mtIds = evaluationType.getMeasurementTypeIds();
			mtIdsMap.put(evaluationType.getId(), mtIds);
		}

		super.updateLinkedEntityIds(mtIdsMap,
				ObjectEntities.MNTTYPANATYPEVATYP,
				EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
	}

	@Override
	public void delete(final Identifier id) {
		assert (id.getMajor() != ObjectEntities.EVALUATION_TYPE_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		final String evaluationTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVATYPPARTYPLINK
					+ SQL_WHERE + EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVALUATION_TYPE
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + evaluationTypeIdStr);
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
	protected Set<EvaluationType> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<EvaluationType> objects = super.retrieveByCondition(conditionQuery);
		super.retrieveParameterTypesByOneQuery(objects);
		this.retrieveMeasurementTypeIdsByOneQuery(objects);
		return objects;
	}

}
