/*
 * $Id: MeasurementPortTypeDatabase.java,v 1.3 2005/08/25 20:13:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.MNTPORTTYPMNTTYPLINK;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_CODE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.DatabaseStorableObjectCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/25 20:13:56 $
 * @author $Author: arseniy $
 * @module measurement
 */
public final class MeasurementPortTypeDatabase extends StorableObjectDatabase<MeasurementPortType> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
    	updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
    	}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final MeasurementPortType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final MeasurementPortType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected MeasurementPortType updateEntityFromResultSet(final MeasurementPortType storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final MeasurementPortType measurementPortType = (storableObject == null)
				? new MeasurementPortType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
				: storableObject;
		measurementPortType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)));
		return measurementPortType;
	}

	@Override
	public void retrieve(final MeasurementPortType storableObject)
			throws ObjectNotFoundException,
				RetrieveObjectException,
				IllegalDataException {
		this.retrieveEntity(storableObject);
		this.retrieveMeasurementTypesByOneQuery(Collections.singleton(storableObject));
	}

	private void retrieveMeasurementTypesByOneQuery(final Set<MeasurementPortType> measurementPortTypes) throws RetrieveObjectException {
		if (measurementPortTypes == null || measurementPortTypes.isEmpty()) {
			return;
		}

		final Map<Identifier, EnumSet<MeasurementType>> dbMeasurementTypesMap = this.retrieveDBMeasurementTypes(measurementPortTypes);
		for (final MeasurementPortType measurementPortType : measurementPortTypes) {
			final Identifier measurementPortTypeId = measurementPortType.getId();
			final EnumSet<MeasurementType> measurementTypes = dbMeasurementTypesMap.get(measurementPortTypeId);

			measurementPortType.setMeasurementTypes0(measurementTypes);
		}
	}

	private Map<Identifier, EnumSet<MeasurementType>> retrieveDBMeasurementTypes(final Set<MeasurementPortType> measurementPortTypes) throws RetrieveObjectException {
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID + COMMA
				+ LINK_COLUMN_MEASUREMENT_TYPE_CODE
				+ SQL_FROM + MNTPORTTYPMNTTYPLINK
				+ SQL_WHERE);
		sql.append(idsEnumerationString(measurementPortTypes, LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID, true));

		final Map<Identifier, EnumSet<MeasurementType>> dbMeasurementTypesMap = new HashMap<Identifier, EnumSet<MeasurementType>>();

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveMeasurementTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			while (resultSet.next()) {
				final Identifier measurementPortTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
				EnumSet<MeasurementType> measurementTypes = dbMeasurementTypesMap.get(measurementPortTypeId);
				if (measurementTypes == null) {
					measurementTypes = EnumSet.noneOf(MeasurementType.class);
					dbMeasurementTypesMap.put(measurementPortTypeId, measurementTypes);
				}
				measurementTypes.add(MeasurementType.fromInt(resultSet.getInt(LINK_COLUMN_MEASUREMENT_TYPE_CODE)));
			}

			return dbMeasurementTypesMap;
		} catch (SQLException sqle) {
			final String mesg = this.getEntityName()
					+ "Database.retrieveMeasurementTypesByOneQuery | Cannot retrieve measurement types for measurement port type -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}

	}

	@Override
	public void insert(final Set<MeasurementPortType> storableObjects) throws CreateObjectException, IllegalDataException {
		super.insertEntities(storableObjects);

		final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap = new HashMap<Identifier, EnumSet<MeasurementType>>();
		for (final MeasurementPortType measurementPortType : storableObjects) {
			measurementTypesMap.put(measurementPortType.getId(), measurementPortType.getMeasurementTypes());
		}
		this.insertMeasurementTypes(measurementTypesMap);
	}

	@Override
	public void update(final Set<MeasurementPortType> storableObjects) throws UpdateObjectException {
		super.updateEntities(storableObjects);
		this.updateMeasurementTypes(storableObjects);
	}

	private void updateMeasurementTypes(final Set<MeasurementPortType> measurementPortTypes) throws UpdateObjectException {
		if (measurementPortTypes == null || measurementPortTypes.isEmpty()) {
			return;
		}

		Map<Identifier, EnumSet<MeasurementType>> dbMeasurementTypesMap = null;
		try {
			dbMeasurementTypesMap = this.retrieveDBMeasurementTypes(measurementPortTypes);
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap = new HashMap<Identifier, EnumSet<MeasurementType>>();
		for (final MeasurementPortType measurementPortType : measurementPortTypes) {
			measurementTypesMap.put(measurementPortType.getId(), measurementPortType.getMeasurementTypes());
		}

		this.updateMeasurementTypes(measurementTypesMap, dbMeasurementTypesMap);
	}

	private void updateMeasurementTypes(final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap,
			final Map<Identifier, EnumSet<MeasurementType>> dbMeasurementTypesMap) throws UpdateObjectException {
		final Map<Identifier, EnumSet<MeasurementType>> insertMeasurementTypesMap = new HashMap<Identifier, EnumSet<MeasurementType>>();
		final Map<Identifier, EnumSet<MeasurementType>> deleteMeasurementTypesMap = new HashMap<Identifier, EnumSet<MeasurementType>>();
		for (final Identifier measurementPortTypeId : measurementTypesMap.keySet()) {
			final EnumSet<MeasurementType> measurementTypes = measurementTypesMap.get(measurementPortTypeId);
			final EnumSet<MeasurementType> dbMeasurementTypes = dbMeasurementTypesMap.get(measurementPortTypeId);
			if (dbMeasurementTypes != null) {
				for (final MeasurementType measurementType : measurementTypes) {
					if (!dbMeasurementTypes.contains(measurementType)) {
						EnumSet<MeasurementType> altMeasurementTypes = insertMeasurementTypesMap.get(measurementPortTypeId);
						if (altMeasurementTypes == null) {
							altMeasurementTypes = EnumSet.noneOf(MeasurementType.class);
							insertMeasurementTypesMap.put(measurementPortTypeId, altMeasurementTypes);
						}
						altMeasurementTypes.add(measurementType);
					}
				}
				for (final MeasurementType measurementType : dbMeasurementTypes) {
					if (!measurementTypes.contains(measurementType)) {
						EnumSet<MeasurementType> altMeasurementTypes = deleteMeasurementTypesMap.get(measurementPortTypeId);
						if (altMeasurementTypes == null) {
							altMeasurementTypes = EnumSet.noneOf(MeasurementType.class);
							deleteMeasurementTypesMap.put(measurementPortTypeId, altMeasurementTypes);
						}
						altMeasurementTypes.add(measurementType);
					}
				}
			} else {
				EnumSet<MeasurementType> altMeasurementTypes = insertMeasurementTypesMap.get(measurementPortTypeId);
				if (altMeasurementTypes == null) {
					altMeasurementTypes = EnumSet.noneOf(MeasurementType.class);
					insertMeasurementTypesMap.put(measurementPortTypeId, altMeasurementTypes);
				}
				altMeasurementTypes.addAll(measurementTypes);
			}
		}

		this.deleteMeasurementTypes(deleteMeasurementTypesMap);
		try {
			this.insertMeasurementTypes(insertMeasurementTypesMap);
		} catch (CreateObjectException ce) {
			throw new UpdateObjectException(ce);
		}
	}

	private void deleteMeasurementTypes(final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap) {
		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + MNTPORTTYPMNTTYPLINK
				+ SQL_WHERE
				+ DatabaseStorableObjectCondition.FALSE_CONDITION);
		for (final Identifier measurementPortTypeId : measurementTypesMap.keySet()) {
			final EnumSet<MeasurementType> measurementTypes = measurementTypesMap.get(measurementPortTypeId);
			sql.append(SQL_OR);
			sql.append(OPEN_BRACKET);
			sql.append(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID + EQUALS + DatabaseIdentifier.toSQLString(measurementPortTypeId)
					+ SQL_AND + LINK_COLUMN_MEASUREMENT_TYPE_CODE + SQL_IN + OPEN_BRACKET);
			for (final Iterator<MeasurementType> it = measurementTypes.iterator(); it.hasNext();) {
				final MeasurementType measurementType = it.next();
				sql.append(measurementType.getCode());
				if (it.hasNext()) {
					sql.append(COMMA);
				}
			}
			sql.append(CLOSE_BRACKET);
			sql.append(CLOSE_BRACKET);
		}

		Connection connection = null;
		Statement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortTypeDatabase.deleteMeasurementTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorException(sqle);
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

	private void insertMeasurementTypes(final Map<Identifier, EnumSet<MeasurementType>> measurementTypesMap)
			throws CreateObjectException {
		final String sql = SQL_INSERT_INTO + MNTPORTTYPMNTTYPLINK + OPEN_BRACKET
				+ LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID + COMMA
				+ LINK_COLUMN_MEASUREMENT_TYPE_CODE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (final Identifier measurementPortTypeId : measurementTypesMap.keySet()) {
				final EnumSet<MeasurementType> measurementTypes = measurementTypesMap.get(measurementPortTypeId);
				for (final MeasurementType measurementType : measurementTypes) {
					DatabaseIdentifier.setIdentifier(preparedStatement, 1, measurementPortTypeId);
					preparedStatement.setInt(2, measurementType.getCode());
					Log.debugMessage(this.getEntityName() + "Database.insertMeasurementTypes | Inserting measurement type '"
							+ measurementType + "' for measurement port type '" + measurementPortTypeId + "'", Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
			}
			connection.commit();
		} catch (SQLException sqle) {
			throw new CreateObjectException("Cannot insert measurement type ids -- " + sqle.getMessage(), sqle);
		} finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
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
	protected Set<MeasurementPortType> retrieveByCondition(final String conditionQuery)
			throws RetrieveObjectException,
				IllegalDataException {
		final Set<MeasurementPortType> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveMeasurementTypesByOneQuery(objects);
		return objects;
	}

}
