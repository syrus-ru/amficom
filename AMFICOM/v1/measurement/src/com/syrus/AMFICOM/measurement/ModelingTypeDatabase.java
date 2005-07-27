/*
 * $Id: ModelingTypeDatabase.java,v 1.48 2005/07/27 18:20:26 arseniy Exp $
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
 * @version $Revision: 1.48 $, $Date: 2005/07/27 18:20:26 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class ModelingTypeDatabase extends ActionTypeDatabase<ModelingType> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	String getParameterTypeLinkTableName() {
		return ObjectEntities.MODTYPPARTYPLINK;
	}

	@Override
	String getActionTypeColumnName() {
		return ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID;
	}

	@Override
	protected short getEntityCode() {
		return ObjectEntities.MODELING_TYPE_CODE;
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
	protected int setEntityForPreparedStatementTmpl(final ModelingType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final ModelingType storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;		
		return values;
	}

	@Override
	protected ModelingType updateEntityFromResultSet(final ModelingType storableObject, final ResultSet resultSet)
		throws IllegalDataException, SQLException {
		final ModelingType modelingType = (storableObject == null)
				? new ModelingType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
					: storableObject;
		modelingType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return modelingType;
	}

	@Override
	public void retrieve(final ModelingType storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		this.retrieveEntity(storableObject);
		super.retrieveParameterTypeIdsByOneQuery(Collections.singleton(storableObject));
	}

	@Override
	public void insert(final Set<ModelingType> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			super.updateParameterTypeIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set<ModelingType> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		super.updateParameterTypeIds(storableObjects);
	}

	@Override
	public void delete(final Identifier id) {
		assert (id.getMajor() != ObjectEntities.MODELING_TYPE_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		final String modelingTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MODTYPPARTYPLINK
					+ SQL_WHERE + ModelingTypeWrapper.LINK_COLUMN_MODELING_TYPE_ID + EQUALS + modelingTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MODELING_TYPE
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + modelingTypeIdStr);
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
	protected Set<ModelingType> retrieveByCondition(final String conditionQuery)
			throws RetrieveObjectException, IllegalDataException {
		final Set<ModelingType> objects = super.retrieveByCondition(conditionQuery);
		super.retrieveParameterTypeIdsByOneQuery(objects);
		return objects;
	}

}
