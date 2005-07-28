/*-
 * $Id: SchemeProtoElementDatabase.java,v 1.16 2005/07/28 10:04:34 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;

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
 * @version $Revision: 1.16 $, $Date: 2005/07/28 10:04:34 $
 * @module scheme
 */
public final class SchemeProtoElementDatabase extends StorableObjectDatabase<SchemeProtoElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeProtoElementWrapper.COLUMN_LABEL + COMMA
					+ SchemeProtoElementWrapper.COLUMN_EQUIPMENT_TYPE_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_UGO_CELL_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_SCHEME_CELL_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID + COMMA
					+ SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;
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
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(SchemeProtoElement storableObject)
			throws IllegalDataException {
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLabel(), SchemeProtoElementWrapper.SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getEquipmentTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSchemeCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeProtoGroupId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeProtoElementId());
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
	protected int setEntityForPreparedStatementTmpl(SchemeProtoElement storableObject,
			PreparedStatement preparedStatement, int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLabel(), SchemeProtoElementWrapper.SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEquipmentTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getUgoCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSchemeCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeProtoGroupId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeProtoElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected SchemeProtoElement updateEntityFromResultSet(SchemeProtoElement storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		Date created = new Date();
		SchemeProtoElement spe = storableObject == null
				? new SchemeProtoElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
						null)
				: storableObject;
		spe.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SchemeProtoElementWrapper.COLUMN_LABEL)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_EQUIPMENT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoElementWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID));
		return spe;
	}
}
