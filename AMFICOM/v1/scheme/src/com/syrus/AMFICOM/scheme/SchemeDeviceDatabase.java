/*-
 * $Id: SchemeDeviceDatabase.java,v 1.13 2005/07/26 12:52:23 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2005/07/26 12:52:23 $
 * @module scheme
 */
public final class SchemeDeviceDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeDevice fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeDevice)
			return (SchemeDevice) storableObject;
		throw new IllegalDataException("SchemeDeviceDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID + COMMA
					+ SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
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
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		SchemeDevice schemeDevice = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(schemeDevice.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeDevice.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeDevice.getParentSchemeProtoElementId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeDevice.getParentSchemeElementId());
		return sql;
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
			StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		SchemeDevice schemeDevice = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeDevice.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeDevice.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeDevice.getParentSchemeProtoElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeDevice.getParentSchemeElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SchemeDevice schemeDevice;
		if (storableObject == null) {
			Date created = new Date();
			schemeDevice = new SchemeDevice(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created,
					created,
					null,
					null,
					StorableObjectVersion.ILLEGAL_VERSION,
					null,
					null,
					null,
					null);
		} else {
			schemeDevice = fromStorableObject(storableObject);
		}
		schemeDevice.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return schemeDevice;
	}
}
