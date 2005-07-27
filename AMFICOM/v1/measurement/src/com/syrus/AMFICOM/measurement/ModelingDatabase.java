/*
 * $Id: ModelingDatabase.java,v 1.51 2005/07/27 18:20:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.51 $, $Date: 2005/07/27 18:20:26 $
 * @author $Author: arseniy $
 * @module module_name
 */

public final class ModelingDatabase extends StorableObjectDatabase<Modeling> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MODELING_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ ModelingWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ ModelingWrapper.COLUMN_ARGUMENT_SET_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Modeling storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getArgumentSet().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		return startParameterNumber;
	}

	@Override
  protected String getUpdateSingleSQLValuesTmpl(final Modeling storableObject) throws IllegalDataException {
  	final String values = DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMonitoredElementId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getArgumentSet().getId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE;
		return values;
	}

	@Override
  protected Modeling updateEntityFromResultSet(final Modeling storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
  	final Modeling modeling = (storableObject == null)
				? new Modeling(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
					: storableObject;
		ModelingType modelingType;
		ParameterSet argumentSet;
		try {
			final Identifier modelingTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID);
			modelingType = (ModelingType) StorableObjectPool.getStorableObject(modelingTypeId, true);
			final Identifier argumentSetId = DatabaseIdentifier.getIdentifier(resultSet, ModelingWrapper.COLUMN_ARGUMENT_SET_ID);
			argumentSet = (ParameterSet) StorableObjectPool.getStorableObject(argumentSetId, true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

    modeling.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				modelingType,
				DatabaseIdentifier.getIdentifier(resultSet, ModelingWrapper.COLUMN_MONITORED_ELEMENT_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				argumentSet);
		return modeling;
	}

}
