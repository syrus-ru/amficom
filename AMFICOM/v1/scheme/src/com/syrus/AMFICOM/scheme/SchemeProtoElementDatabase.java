/*-
 * $Id: SchemeProtoElementDatabase.java,v 1.22 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.COLUMN_LABEL;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.COLUMN_PROTO_EQUIPMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.COLUMN_SCHEME_CELL_ID;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.COLUMN_SYMBOL_ID;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.COLUMN_UGO_CELL_ID;
import static com.syrus.AMFICOM.scheme.SchemeProtoElementWrapper.SIZE_LABEL_COLUMN;

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
 * @version $Revision: 1.22 $, $Date: 2006/07/02 22:36:13 $
 * @module scheme
 */
public final class SchemeProtoElementDatabase extends StorableObjectDatabase<SchemeProtoElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_LABEL + COMMA
					+ COLUMN_PROTO_EQUIPMENT_ID + COMMA
					+ COLUMN_SYMBOL_ID + COMMA
					+ COLUMN_UGO_CELL_ID + COMMA
					+ COLUMN_SCHEME_CELL_ID + COMMA
					+ COLUMN_PARENT_SCHEME_PROTO_GROUP_ID + COMMA
					+ COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEPROTOELEMENT_CODE;
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
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param schemeProtoElement
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(SchemeProtoElement schemeProtoElement) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeProtoElement.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeProtoElement.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeProtoElement.getLabel(), SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoElement.getProtoEquipmentId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoElement.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoElement.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoElement.getSchemeCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoElement.getParentSchemeProtoGroupId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeProtoElement.getParentSchemeProtoElementId());
	}

	/**
	 * @param schemeProtoElement
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeProtoElement schemeProtoElement,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeProtoElement.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeProtoElement.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeProtoElement.getLabel(), SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoElement.getProtoEquipmentId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoElement.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoElement.getUgoCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoElement.getSchemeCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoElement.getParentSchemeProtoGroupId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeProtoElement.getParentSchemeProtoElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeProtoElement updateEntityFromResultSet(SchemeProtoElement storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemeProtoElement spe = (storableObject == null)
				? new SchemeProtoElement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
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
						null,
						null,
						null)
				: storableObject;
		spe.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_LABEL)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PROTO_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_PROTO_GROUP_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID));
		return spe;
	}
}
