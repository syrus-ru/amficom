/*-
 * $Id: SchemeProtoGroupDatabase.java,v 1.20 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID;
import static com.syrus.AMFICOM.scheme.SchemeProtoGroupWrapper.COLUMN_SYMBOL_ID;

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
 * @version $Revision: 1.20 $, $Date: 2006/07/02 22:36:13 $
 * @module scheme
 */
public final class SchemeProtoGroupDatabase extends StorableObjectDatabase<SchemeProtoGroup> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_SYMBOL_ID + COMMA
					+ COLUMN_PARENT_SCHEME_PROTO_GROUP_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEPROTOGROUP_CODE;
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
	 * @param schemeProtoGroup
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final SchemeProtoGroup schemeProtoGroup) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeProtoGroup.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeProtoGroup.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoGroup.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoGroup.getParentSchemeProtoGroupId());
	}

	/**
	 * @param schemeProtoGroup
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeProtoGroup schemeProtoGroup,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeProtoGroup.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeProtoGroup.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoGroup.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoGroup.getParentSchemeProtoGroupId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeProtoGroup updateEntityFromResultSet(
			final SchemeProtoGroup storableObject,
			final ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemeProtoGroup spg = (storableObject == null)
				? new SchemeProtoGroup(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
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
		spg.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_PROTO_GROUP_ID));
		return spg;
	}
}
