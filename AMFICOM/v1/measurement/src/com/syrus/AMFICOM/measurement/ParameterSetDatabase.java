/*
 * $Id: ParameterSetDatabase.java,v 1.26 2006/03/27 10:10:07 bass Exp $
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TableNames;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.26 $, $Date: 2006/03/27 10:10:07 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class ParameterSetDatabase extends StorableObjectDatabase<ParameterSet> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ObjectEntities.PARAMETERSET_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = ParameterSetWrapper.COLUMN_SORT  + COMMA
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
	protected String getUpdateSingleSQLValuesTmpl(final ParameterSet storableObject) throws IllegalDataException {
		final String values = Integer.toString(storableObject.getSort().value()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final ParameterSet storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, storableObject.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected ParameterSet updateEntityFromResultSet(final ParameterSet storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final ParameterSet set = (storableObject == null)
				? new ParameterSet(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						0,
						null,
						null,
						null)
					: storableObject;

		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		set.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				resultSet.getInt(ParameterSetWrapper.COLUMN_SORT),
				(description != null) ? description : "");
		return set;
	}

	private void retrieveSetParametersByOneQuery(final Set<ParameterSet> sets) throws RetrieveObjectException {
		if ((sets == null) || (sets.isEmpty())) {
			return;
		}

		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_CODE + COMMA
				+ ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ ParameterSetWrapper.LINK_COLUMN_SET_ID
				+ SQL_FROM + ObjectEntities.PARAMETER + SQL_WHERE);
		sql.append(idsEnumerationString(sets, ParameterSetWrapper.LINK_COLUMN_SET_ID, true));

		final Map<Identifier, Set<Parameter>> setParametersMap = new HashMap<Identifier, Set<Parameter>>();

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			while (resultSet.next()) {
				final ParameterType parameterType = ParameterType.valueOf(resultSet.getInt(StorableObjectWrapper.COLUMN_TYPE_CODE));
				final Parameter parameter = new Parameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						parameterType,
						ByteArrayDatabase.toByteArray(resultSet.getBlob(ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				final Identifier setId = DatabaseIdentifier.getIdentifier(resultSet, ParameterSetWrapper.LINK_COLUMN_SET_ID);
				Set<Parameter> setParameters = setParametersMap.get(setId);
				if (setParameters == null) {
					setParameters = new HashSet<Parameter>();
					setParametersMap.put(setId, setParameters);
				}
				setParameters.add(parameter);
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve parameters for set -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
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
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}

		for (final ParameterSet set : sets) {
			final Identifier setId = set.getId();
			final Set<Parameter> setParameters = setParametersMap.get(setId);

			if (setParameters != null) {
				set.setParameters0(setParameters.toArray(new Parameter[setParameters.size()]));
			} else {
				set.setParameters0(new Parameter[0]);
			}
		}

	}

	private void retrieveSetMELinksByOneQuery(final Set<ParameterSet> sets) throws RetrieveObjectException {
		if ((sets == null) || (sets.isEmpty())) {
			return;
		}

		final Map<Identifier, Set<Identifier>> meIdsMap = this.retrieveLinkedEntityIds(sets,
				TableNames.PARAMETERSETMELINK,
				ParameterSetWrapper.LINK_COLUMN_SET_ID,
				ParameterSetWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);

		for (final ParameterSet set : sets) {
			final Identifier setId = set.getId();
			final Set<Identifier> meIds = meIdsMap.get(setId);

			set.setMonitoredElementIds0(meIds);
		}
	}

	@Override
	protected void insert(final Set<ParameterSet> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		for (final ParameterSet parameterSet : storableObjects) {
			this.insertSetParameters(parameterSet);
		}
		try {
			this.updateSetMELinks(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	private void insertSetParameters(final ParameterSet set) throws CreateObjectException {
		final Identifier setId = set.getId();		
		final Parameter[] setParameters = set.getParameters();
		Log.debugMessage("setParameters count:" + setParameters.length, Log.DEBUGLEVEL09);
		final String sql = SQL_INSERT_INTO + ObjectEntities.PARAMETER + OPEN_BRACKET
				+ StorableObjectWrapper.COLUMN_ID  + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_CODE + COMMA
				+ ParameterSetWrapper.LINK_COLUMN_SET_ID + COMMA
				+ ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB
				+ CLOSE_BRACKET;
		Log.debugMessage("Trying:" + sql, Log.DEBUGLEVEL09);
		PreparedStatement preparedStatement = null;
		Identifier parameterId = null;
		ParameterType parameterType = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (final Parameter parameter : setParameters) {
				parameterId = parameter.getId();
				parameterType = parameter.getType();

				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				preparedStatement.setInt(2, parameterType.ordinal());
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, setId);

				Log.debugMessage("Inserting parameter " + parameterType.getDescription() + " for set '" + setId + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				ByteArrayDatabase.saveAsBlob(parameter.getValue(),
						connection,
						ObjectEntities.PARAMETER,
						ParameterSetWrapper.LINK_COLUMN_PARAMETER_VALUE,
						StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		} catch (SQLException sqle) {
			final String mesg = "Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterType.getDescription() + "' for set '" + setId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
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
				Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	protected void update(final Set<ParameterSet> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateSetMELinks(storableObjects);
	}

	private void updateSetMELinks(final Set<ParameterSet> sets) throws UpdateObjectException {
		if (sets == null || sets.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> meIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final ParameterSet set : sets) {
			final Set<Identifier> meIds = set.getMonitoredElementIds();
			meIdsMap.put(set.getId(), meIds);
		}

		super.updateLinkedEntityIds(meIdsMap,
				TableNames.PARAMETERSETMELINK,
				ParameterSetWrapper.LINK_COLUMN_SET_ID,
				ParameterSetWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	@Override
	protected Set<ParameterSet> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<ParameterSet> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveSetParametersByOneQuery(objects);
		this.retrieveSetMELinksByOneQuery(objects);
		return objects;
	}

}
