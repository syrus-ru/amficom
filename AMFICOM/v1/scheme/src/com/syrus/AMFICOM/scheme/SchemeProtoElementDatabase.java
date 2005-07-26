/*-
 * $Id: SchemeProtoElementDatabase.java,v 1.15 2005/07/26 12:52:23 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.15 $, $Date: 2005/07/26 12:52:23 $
 * @module scheme
 */
public final class SchemeProtoElementDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	private SchemeProtoElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SchemeProtoElement)
			return (SchemeProtoElement) storableObject;			
		throw new IllegalDataException("SchemeProtoElement.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

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
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject)
			throws IllegalDataException {
		SchemeProtoElement spe = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(spe.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(spe.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(spe.getLabel(), SchemeProtoElementWrapper.SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getEquipmentTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getSchemeCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getParentSchemeProtoGroupId()) + COMMA
				+ DatabaseIdentifier.toSQLString(spe.getParentSchemeProtoElementId());
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
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement, int startParameterNumber) throws IllegalDataException,
			SQLException {
		SchemeProtoElement spe = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spe.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spe.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spe.getLabel(), SchemeProtoElementWrapper.SIZE_LABEL_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getEquipmentTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getUgoCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getSchemeCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getParentSchemeProtoGroupId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spe.getParentSchemeProtoElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SchemeProtoElement spe;
		if (storableObject == null) {
			Date created = new Date();
			spe = new SchemeProtoElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
					null);
		} else {
			spe = fromStorableObject(storableObject);
		}
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
