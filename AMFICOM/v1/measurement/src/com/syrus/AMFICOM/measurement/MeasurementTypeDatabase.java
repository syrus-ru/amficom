/*
 * $Id: MeasurementTypeDatabase.java,v 1.109 2005/08/08 11:31:46 arseniy Exp $
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
 * @version $Revision: 1.109 $, $Date: 2005/08/08 11:31:46 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class MeasurementTypeDatabase extends ActionTypeDatabase<MeasurementType>  {
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
	protected String getUpdateSingleSQLValuesTmpl(final MeasurementType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected MeasurementType updateEntityFromResultSet(final MeasurementType storableObject, final ResultSet resultSet)
		throws IllegalDataException, SQLException {
		final MeasurementType measurementType = (storableObject == null)
				? new MeasurementType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null)
					: storableObject;
		measurementType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return measurementType;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final MeasurementType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber)
			throws IllegalDataException, SQLException {
			DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
		}

	@Override
	public void retrieve(final MeasurementType storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		this.retrieveEntity(storableObject);
		super.retrieveParameterTypeIdsByOneQuery(Collections.singleton(storableObject));
		this.retrieveMeasurementPortTypeIdsByOneQuery(Collections.singleton(storableObject));
	}

	private void retrieveMeasurementPortTypeIdsByOneQuery(final Set<MeasurementType> measurementTypes) throws RetrieveObjectException {
		if ((measurementTypes == null) || (measurementTypes.isEmpty())) {
			return;
		}

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
	public void insert(final Set<MeasurementType> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			super.updateParameterTypeIds(storableObjects);
			this.updateMeasurementPortTypeIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set<MeasurementType> storableObjects) throws UpdateObjectException {
		super.updateEntities(storableObjects);
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
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
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
	protected Set<MeasurementType> retrieveByCondition(final String conditionQuery)
			throws RetrieveObjectException, IllegalDataException {
		final Set<MeasurementType> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypeIdsByOneQuery(objects);
		this.retrieveMeasurementPortTypeIdsByOneQuery(objects);
		return objects;
	}

}
