/*-
 * $Id: SchemeElementDatabase.java,v 1.11 2005/07/24 17:39:15 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/07/24 17:39:15 $
 * @module scheme_v1
 */
public final class SchemeElementDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	private SchemeElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SchemeElement)
			return (SchemeElement) storableObject;			
		throw new IllegalDataException("SchemeElement.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

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
			StorableObject storableObject)
			throws IllegalDataException {
		SchemeElement schemeElement = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(schemeElement.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeElement.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeElement.getLabel(), SchemeElementWrapper.SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getEquipmentTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getEquipmentId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getKisId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getSchemeCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getParentSchemeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeElement.getParentSchemeElementId());
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
		SchemeElement schemeElement = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeElement.getLabel(), SchemeElementWrapper.SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeElement.getEquipmentTypeId());
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
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SchemeElement schemeElement;
		if (storableObject == null) {
			Date created = new Date();
			schemeElement = new SchemeElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null, null, null, null, null, null, null, null, null);
		} else {
			schemeElement = fromStorableObject(storableObject);
		}
		schemeElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
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
