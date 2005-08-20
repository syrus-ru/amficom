/*
 * $Id: MeasurementPortDatabase.java,v 1.1 2005/08/20 19:25:23 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
 * @version $Revision: 1.1 $, $Date: 2005/08/20 19:25:23 $
 * @author $Author: arseniy $
 * @module config
 */
public final class MeasurementPortDatabase extends StorableObjectDatabase<MeasurementPort> {
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
	protected String getUpdateSingleSQLValuesTmpl(final MeasurementPort storableObject) throws IllegalDataException {
		final Identifier typeId = storableObject.getType().getId();
		final Identifier kisId = storableObject.getKISId();
		final Identifier portId = storableObject.getPortId();
		final String sql = DatabaseIdentifier.toSQLString(typeId) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE	+ COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(kisId)	+ COMMA
			+ DatabaseIdentifier.toSQLString(portId);
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final MeasurementPort storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Identifier typeId = storableObject.getType().getId();
		final Identifier kisId = storableObject.getKISId();
		final Identifier portId = storableObject.getPortId();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, typeId);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, kisId);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, portId);
		return startParameterNumber;
	}

	@Override
	protected MeasurementPort updateEntityFromResultSet(final MeasurementPort storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementPort measurementPort = storableObject == null
				? new MeasurementPort(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
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
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				measurementPortType,
				(name != null) ? name : "",
				(description != null) ? description : "",
				DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_KIS_ID),
				DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_PORT_ID));
		return measurementPort;
	}

}
