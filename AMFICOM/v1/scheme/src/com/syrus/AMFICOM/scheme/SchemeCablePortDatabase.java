/*-
 * $Id: SchemeCablePortDatabase.java,v 1.22 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeCablePortWrapper.COLUMN_CABLE_PORT_ID;
import static com.syrus.AMFICOM.scheme.SchemeCablePortWrapper.COLUMN_CABLE_PORT_TYPE_ID;
import static com.syrus.AMFICOM.scheme.SchemeCablePortWrapper.COLUMN_DIRECTION_TYPE;
import static com.syrus.AMFICOM.scheme.SchemeCablePortWrapper.COLUMN_MEASUREMENT_PORT_ID;
import static com.syrus.AMFICOM.scheme.SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID;

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
public final class SchemeCablePortDatabase extends StorableObjectDatabase<SchemeCablePort> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_DIRECTION_TYPE + COMMA
					+ COLUMN_CABLE_PORT_TYPE_ID + COMMA
					+ COLUMN_CABLE_PORT_ID + COMMA
					+ COLUMN_MEASUREMENT_PORT_ID + COMMA
					+ COLUMN_PARENT_DEVICE_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMECABLEPORT_CODE;
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
	 * @param schemeCablePort
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeCablePort schemeCablePort) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeCablePort.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeCablePort.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ schemeCablePort.getDirectionType().value() + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getPortTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getMeasurementPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getParentSchemeDeviceId());
	}

	/**
	 * @param schemeCablePort
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeCablePort schemeCablePort,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCablePort.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCablePort.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemeCablePort.getDirectionType().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getPortTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getMeasurementPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getParentSchemeDeviceId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeCablePort updateEntityFromResultSet(
			SchemeCablePort storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemeCablePort schemeCablePort = (storableObject == null)
				? new SchemeCablePort(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
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
		schemeCablePort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				IdlDirectionType.from_int(resultSet.getInt(COLUMN_DIRECTION_TYPE)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CABLE_PORT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CABLE_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_DEVICE_ID));
		return schemeCablePort;
	}
}
