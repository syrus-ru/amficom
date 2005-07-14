/*
 * $Id: MeasurementTypeDatabase.java,v 1.103 2005/07/14 19:02:39 arseniy Exp $
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
 * @version $Revision: 1.103 $, $Date: 2005/07/14 19:02:39 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class MeasurementTypeDatabase extends ActionTypeDatabase  {

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	String getParameterTypeLinkTableName() {
		return ObjectEntities.MNTTYPPARTYPLINK;
	}

	@Override
	String getActionTypeColumnName() {
		return MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID;
	}

	private MeasurementType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementType)
			return (MeasurementType)storableObject;
		throw new IllegalDataException("MeasurementTypeDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}	

	@Override
	protected short getEntityCode() {
		return ObjectEntities.MEASUREMENT_TYPE_CODE;
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
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final MeasurementType measurementType = this.fromStorableObject(storableObject);
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(measurementType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(measurementType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
		throws IllegalDataException, SQLException {
		final MeasurementType measurementType = (storableObject == null) ?
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

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber)
			throws IllegalDataException, SQLException {
		final MeasurementType measurementType = this.fromStorableObject(storableObject);
			DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementType.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
		}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementType);
		super.retrieveParameterTypeIdsByOneQuery(Collections.singleton(measurementType));
		this.retrieveMeasurementPortTypeIdsByOneQuery(Collections.singleton(measurementType));
	}

	private void retrieveMeasurementPortTypeIdsByOneQuery(final Set<MeasurementType> measurementTypes) throws RetrieveObjectException {
		if ((measurementTypes == null) || (measurementTypes.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> measurementPortTypeIdsMap = super.retrieveLinkedEntityIds(measurementTypes,
				ObjectEntities.MNTTYPEMEASPORTTYPELINK,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);

		for (final MeasurementType measurementType :  measurementTypes) {
			final Identifier measurementTypeId = measurementType.getId();
			final Set<Identifier> measurementPortTypeIds = measurementPortTypeIdsMap.get(measurementTypeId);

			measurementType.setMeasurementPortTypeIds0(measurementPortTypeIds);
		}
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + measurementType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		final MeasurementType measurementType = this.fromStorableObject(storableObject);
		super.insertEntity(measurementType);
		try {
			super.updateParameterTypeIds(Collections.singleton(measurementType));
			this.updateMeasurementPortTypeIds(Collections.singleton(measurementType));
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			super.updateParameterTypeIds(storableObjects);
			this.updateMeasurementPortTypeIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final StorableObject storableObject, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			final MeasurementType measurementType = this.fromStorableObject(storableObject);
			super.updateParameterTypeIds(Collections.singleton(measurementType));
			this.updateMeasurementPortTypeIds(Collections.singleton(measurementType));
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	@Override
	public void update(final Set storableObjects, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		super.updateParameterTypeIds(storableObjects);
		this.updateMeasurementPortTypeIds(storableObjects);
	}

	private void updateMeasurementPortTypeIds(final Set<MeasurementType> measurementTypes)
			throws UpdateObjectException {
		if (measurementTypes == null || measurementTypes.isEmpty())
			return;

		final Map<Identifier, Set<Identifier>> mpIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final MeasurementType measurementType : measurementTypes) {
			final Set<Identifier> mpIds = measurementType.getMeasurementPortTypeIds();
			mpIdsMap.put(measurementType.getId(), mpIds);
		}

		super.updateLinkedEntityIds(mpIdsMap,
				ObjectEntities.MNTTYPEMEASPORTTYPELINK,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID,
				MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
	}

	@Override
	public void delete(final Identifier id) {
		assert (id.getMajor() != ObjectEntities.MEASUREMENT_TYPE_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		final String measurementTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MNTTYPEMEASPORTTYPELINK
					+ SQL_WHERE + MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MNTTYPPARTYPLINK
					+ SQL_WHERE + MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MEASUREMENT_TYPE
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + measurementTypeIdStr);

			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			} catch(SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	@Override
	protected Set retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set objects = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypeIdsByOneQuery(objects);
		this.retrieveMeasurementPortTypeIdsByOneQuery(objects);
		return objects;
	}

}
