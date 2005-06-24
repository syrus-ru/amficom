/*
 * $Id: EvaluationTypeDatabase.java,v 1.88 2005/06/24 13:54:35 arseniy Exp $
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
 * @version $Revision: 1.88 $, $Date: 2005/06/24 13:54:35 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class EvaluationTypeDatabase extends ActionTypeDatabase {

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

	private EvaluationType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType) storableObject;
		throw new IllegalDataException("EvaluationTypeDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
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
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final EvaluationType evaluationType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, evaluationType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, evaluationType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final EvaluationType evaluationType = this.fromStorableObject(storableObject);
		final String values = APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;		
		return values;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
		throws IllegalDataException, SQLException {
		final EvaluationType evaluationType = (storableObject == null) ?
				new EvaluationType(DatabaseIdentifier.getIdentifier(
			resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null, null) :
				this.fromStorableObject(storableObject);
		evaluationType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return evaluationType;
	}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEntity(evaluationType);
		super.retrieveParameterTypeIdsByOneQuery(Collections.singleton(evaluationType));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(evaluationType));
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
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + evaluationType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		super.insertEntity(evaluationType);
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

	private void updateMeasurementTypeIds(final Set<EvaluationType> evaluationTypes)
			throws IllegalDataException, UpdateObjectException {
		if (evaluationTypes == null || evaluationTypes.isEmpty())
			return;

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
		final Connection connection = DatabaseConnection.getConnection();
		try {
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
	protected Set retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set objects = super.retrieveByCondition(conditionQuery);
		super.retrieveParameterTypeIdsByOneQuery(objects);
		this.retrieveMeasurementTypeIdsByOneQuery(objects);
		return objects;
	}

}
