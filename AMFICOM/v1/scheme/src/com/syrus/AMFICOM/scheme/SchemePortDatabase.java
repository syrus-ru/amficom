/*-
 * $Id: SchemePortDatabase.java,v 1.22 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2006/07/02 22:36:13 $
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
	 * @param schemePort
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemePort schemePort) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemePort.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemePort.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ schemePort.getDirectionType().value() + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getPortTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getMeasurementPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getParentSchemeDeviceId());
	}

	/**
	 * @param schemePort
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemePort schemePort,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePort.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePort.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemePort.getDirectionType().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getPortTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getMeasurementPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getParentSchemeDeviceId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemePort updateEntityFromResultSet(
			SchemePort storableObject, ResultSet resultSet)
	throws SQLException {
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
