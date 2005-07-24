/*
 * $Id: MeasurementPortDatabase.java,v 1.58 2005/07/24 17:38:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.58 $, $Date: 2005/07/24 17:38:08 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public final class MeasurementPortDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MEASUREMENTPORT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MeasurementPortWrapper.COLUMN_KIS_ID + COMMA
				+ MeasurementPortWrapper.COLUMN_PORT_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		final Identifier typeId = measurementPort.getType().getId();
		final Identifier kisId = measurementPort.getKISId();
		final Identifier portId = measurementPort.getPortId();
		final String sql = DatabaseIdentifier.toSQLString(typeId) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(measurementPort.getName(), SIZE_NAME_COLUMN) + APOSTROPHE	+ COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(measurementPort.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(kisId)	+ COMMA
			+ DatabaseIdentifier.toSQLString(portId);
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		final Identifier typeId = measurementPort.getType().getId();
		final Identifier kisId = measurementPort.getKISId();
		final Identifier portId = measurementPort.getPortId();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, typeId);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementPort.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, measurementPort.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, kisId);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, portId);
		return startParameterNumber;
	}

  private MeasurementPort fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPort)
			return (MeasurementPort)storableObject;
		throw new IllegalDataException("MeasurementPortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementPort measurementPort = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (measurementPort == null) {
			measurementPort = new MeasurementPort(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null,
					null);
		}
		MeasurementPortType measurementPortType;
		try {
			final Identifier measurementPortTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID);
			measurementPortType = (measurementPortTypeId != null)
					? (MeasurementPortType) StorableObjectPool.getStorableObject(measurementPortTypeId, true) : null;
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		final String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));

		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		measurementPort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				measurementPortType,
				(name != null) ? name : "",
				(description != null) ? description : "",
				DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_KIS_ID),
				DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_PORT_ID));
		return measurementPort;
	}

}
