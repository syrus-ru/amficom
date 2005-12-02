/*-
 * $Id: SchemePortDatabase.java,v 1.21 2005/12/02 11:24:16 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemePortWrapper.COLUMN_DIRECTION_TYPE;
import static com.syrus.AMFICOM.scheme.SchemePortWrapper.COLUMN_MEASUREMENT_PORT_ID;
import static com.syrus.AMFICOM.scheme.SchemePortWrapper.COLUMN_PARENT_DEVICE_ID;
import static com.syrus.AMFICOM.scheme.SchemePortWrapper.COLUMN_PORT_ID;
import static com.syrus.AMFICOM.scheme.SchemePortWrapper.COLUMN_PORT_TYPE_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.21 $, $Date: 2005/12/02 11:24:16 $
 * @module scheme
 */
public final class SchemePortDatabase extends StorableObjectDatabase<SchemePort> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_DIRECTION_TYPE + COMMA
					+ COLUMN_PORT_TYPE_ID + COMMA
					+ COLUMN_PORT_ID + COMMA
					+ COLUMN_MEASUREMENT_PORT_ID + COMMA
					+ COLUMN_PARENT_DEVICE_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEPORT_CODE;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemePort storableObject)
			throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getDirectionType().value() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getPortTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeDeviceId());
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			SchemePort storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getDirectionType().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getPortTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeDeviceId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected SchemePort updateEntityFromResultSet(
			SchemePort storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final Date created = new Date();
		final SchemePort schemePort = (storableObject == null)
				? new SchemePort(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		schemePort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				IdlDirectionType.from_int(resultSet.getInt(COLUMN_DIRECTION_TYPE)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PORT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_DEVICE_ID));
		return schemePort;
	}
}
