/*-
 * $Id: SchemeElementDatabase.java,v 1.14 2005/07/28 10:04:34 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/07/28 10:04:34 $
 * @module scheme
 */
public final class SchemeElementDatabase extends StorableObjectDatabase<SchemeElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeElementWrapper.COLUMN_LABEL + COMMA
					+ SchemeElementWrapper.COLUMN_EQUIPMENT_TYPE_ID + COMMA
					+ SchemeElementWrapper.COLUMN_EQUIPMENT_ID + COMMA
					+ SchemeElementWrapper.COLUMN_KIS_ID + COMMA
					+ SchemeElementWrapper.COLUMN_SITE_NODE_ID + COMMA
					+ SchemeElementWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeElementWrapper.COLUMN_UGO_CELL_ID + COMMA
					+ SchemeElementWrapper.COLUMN_SCHEME_CELL_ID + COMMA
					+ SchemeElementWrapper.COLUMN_PARENT_SCHEME_ID + COMMA
					+ SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
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
			SchemeElement storableObject)
			throws IllegalDataException {
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLabel(), SchemeElementWrapper.SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getEquipmentTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getEquipmentId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getKisId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSchemeCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeElementId());
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
			SchemeElement storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLabel(), SchemeElementWrapper.SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEquipmentTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEquipmentId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getKisId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getUgoCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSchemeCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected SchemeElement updateEntityFromResultSet(
			SchemeElement storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final Date created = new Date();
		SchemeElement schemeElement = storableObject == null
				? schemeElement = new SchemeElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						created,
						created,
						null,
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
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
		schemeElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SchemeElementWrapper.COLUMN_LABEL)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_EQUIPMENT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_KIS_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_PARENT_SCHEME_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return schemeElement;
	}
}
