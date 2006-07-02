/*-
 * $Id: SchemeElementDatabase.java,v 1.23 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_EQUIPMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_KIND;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_KIS_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_LABEL;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_PARENT_SCHEME_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_PROTO_EQUIPMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_SCHEME_CELL_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_SITE_NODE_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_SYMBOL_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.COLUMN_UGO_CELL_ID;
import static com.syrus.AMFICOM.scheme.SchemeElementWrapper.SIZE_LABEL_COLUMN;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.23 $, $Date: 2006/07/02 22:36:13 $
 * @module scheme
 */
public final class SchemeElementDatabase extends StorableObjectDatabase<SchemeElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_KIND + COMMA 
					+ COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_LABEL + COMMA
					+ COLUMN_PROTO_EQUIPMENT_ID + COMMA
					+ COLUMN_EQUIPMENT_ID + COMMA
					+ COLUMN_KIS_ID + COMMA
					+ COLUMN_SITE_NODE_ID + COMMA
					+ COLUMN_SYMBOL_ID + COMMA
					+ COLUMN_UGO_CELL_ID + COMMA
					+ COLUMN_SCHEME_CELL_ID + COMMA
					+ COLUMN_PARENT_SCHEME_ID + COMMA
					+ COLUMN_PARENT_SCHEME_ELEMENT_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEELEMENT_CODE;
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
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param schemeElement
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeElement schemeElement) {
		return 	schemeElement.getKind() + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeElement.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeElement.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeElement.getLabel(), SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getProtoEquipmentId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getEquipmentId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getKisId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSchemeCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getParentSchemeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getParentSchemeElementId());
	}

	/**
	 * @param schemeElement
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeElement schemeElement,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		preparedStatement.setInt(++startParameterNumber, schemeElement.getKind().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getLabel(), SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getProtoEquipmentId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getEquipmentId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getKisId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getUgoCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getSchemeCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getParentSchemeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getParentSchemeElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeElement updateEntityFromResultSet(
			SchemeElement storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		SchemeElement schemeElement = (storableObject == null)
				? schemeElement = new SchemeElement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
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
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		schemeElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				IdlSchemeElementKind.from_int(resultSet.getInt(COLUMN_KIND)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_LABEL)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PROTO_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_KIS_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return schemeElement;
	}
}
