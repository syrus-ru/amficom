/*-
 * $Id: SchemeOptimizeInfoSwitchDatabase.java,v 1.13 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2006/07/02 22:36:13 $
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
	 * @param schemeOptimizeInfoSwitch
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeOptimizeInfoSwitch.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ schemeOptimizeInfoSwitch.getPriceUsd() + COMMA
				+ schemeOptimizeInfoSwitch.getNoOfPorts() + COMMA
				+ DatabaseIdentifier.toSQLString(schemeOptimizeInfoSwitch.getParentSchemeOptimizeInfoId());
	}

	/**
	 * @param schemeOptimizeInfoSwitch
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, java.sql.PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeOptimizeInfoSwitch.getName(), SIZE_NAME_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemeOptimizeInfoSwitch.getPriceUsd());
		preparedStatement.setByte(++startParameterNumber, schemeOptimizeInfoSwitch.getNoOfPorts());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeOptimizeInfoSwitch.getParentSchemeOptimizeInfoId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, java.sql.ResultSet)
	 */
	@Override
	protected SchemeOptimizeInfoSwitch updateEntityFromResultSet(
			final SchemeOptimizeInfoSwitch storableObject,
			final ResultSet resultSet)
	throws SQLException {
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
