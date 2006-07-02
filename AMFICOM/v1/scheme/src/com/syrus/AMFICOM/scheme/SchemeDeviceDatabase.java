/*-
 * $Id: SchemeDeviceDatabase.java,v 1.18 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2006/07/02 22:36:13 $
 * @module scheme
 */
public final class SchemeDeviceDatabase extends StorableObjectDatabase<SchemeDevice> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID + COMMA
					+ COLUMN_PARENT_SCHEME_ELEMENT_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEDEVICE_CODE;
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

	/**
	 * @param schemeDevice
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeDevice schemeDevice) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeDevice.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeDevice.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeDevice.getParentSchemeProtoElementId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeDevice.getParentSchemeElementId());
	}

	/**
	 * @param schemeDevice
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeDevice schemeDevice,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeDevice.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeDevice.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeDevice.getParentSchemeProtoElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeDevice.getParentSchemeElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeDevice updateEntityFromResultSet(
			SchemeDevice storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemeDevice schemeDevice = (storableObject == null)
				? new SchemeDevice(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
				: storableObject;
		schemeDevice.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return schemeDevice;
	}
}
