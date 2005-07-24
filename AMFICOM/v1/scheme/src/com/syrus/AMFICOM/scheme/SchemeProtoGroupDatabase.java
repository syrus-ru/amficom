/*-
 * $Id: SchemeProtoGroupDatabase.java,v 1.13 2005/07/24 17:39:16 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;

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
 * @version $Revision: 1.13 $, $Date: 2005/07/24 17:39:16 $
 * @module scheme_v1
 */
public final class SchemeProtoGroupDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeProtoGroup fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SchemeProtoGroup)
			return (SchemeProtoGroup) storableObject;
		throw new IllegalDataException("SchemeProtoGroupDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeProtoGroupWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeProtoGroupWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID;
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
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject)
			throws IllegalDataException {
		SchemeProtoGroup spg = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(spg.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
		+ APOSTROPHE + DatabaseString.toQuerySubString(spg.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
		+ DatabaseIdentifier.toSQLString(spg.getSymbolId()) + COMMA
		+ DatabaseIdentifier.toSQLString(spg.getParentSchemeProtoGroupId());
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
			PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		SchemeProtoGroup spg = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spg.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, spg.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spg.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, spg.getParentSchemeProtoGroupId());
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
		SchemeProtoGroup spg;
		if (storableObject == null) {
			Date created = new Date();
			spg = new SchemeProtoGroup(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null);
		} else {
			spg = fromStorableObject(storableObject);
		}
		spg.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoGroupWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeProtoGroupWrapper.COLUMN_PARENT_SCHEME_PROTO_GROUP_ID));
		return spg;
	}
}
