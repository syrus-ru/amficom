/*-
 * $Id: SchemeOptimizeInfoSwitchDatabase.java,v 1.12 2005/12/02 11:24:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoSwitchWrapper.COLUMN_NO_OF_PORTS;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoSwitchWrapper.COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoSwitchWrapper.COLUMN_PRICE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/12/02 11:24:17 $
 * @module scheme
 */
public final class SchemeOptimizeInfoSwitchDatabase extends StorableObjectDatabase<SchemeOptimizeInfoSwitch> {
	@SuppressWarnings("hiding")
	public static final int SIZE_NAME_COLUMN = 128;

	private static String columns;

	private static String updateMultipleSQLValues;

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getColumnsTmpl()
	 */
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_PRICE + COMMA
					+ COLUMN_NO_OF_PORTS + COMMA
					+ COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID;
		}
		return columns;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getEntityCode()
	 */
	@Override
	protected short getEntityCode() {
		return SCHEMEOPTIMIZEINFOSWITCH_CODE;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateMultipleSQLValuesTmpl()
	 */
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
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final SchemeOptimizeInfoSwitch storableObject)
	throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getPriceUsd() + COMMA
				+ storableObject.getNoOfPorts() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeOptimizeInfoId());
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, java.sql.PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeOptimizeInfoSwitch storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber)
	throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getPriceUsd());
		preparedStatement.setByte(++startParameterNumber, storableObject.getNoOfPorts());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeOptimizeInfoId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, java.sql.ResultSet)
	 */
	@Override
	protected SchemeOptimizeInfoSwitch updateEntityFromResultSet(
			final SchemeOptimizeInfoSwitch storableObject,
			final ResultSet resultSet)
	throws IllegalDataException, SQLException {
		final Date created = new Date();
		final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch = (storableObject == null)
				? new SchemeOptimizeInfoSwitch(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						0,
						(byte) 0,
						null)
				: storableObject;
		schemeOptimizeInfoSwitch.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				resultSet.getInt(COLUMN_PRICE),
				resultSet.getByte(COLUMN_NO_OF_PORTS),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID));
		return schemeOptimizeInfoSwitch;
	}
}
