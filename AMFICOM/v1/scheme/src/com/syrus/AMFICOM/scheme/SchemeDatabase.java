/*-
 * $Id: SchemeDatabase.java,v 1.19 2005/07/28 10:04:33 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.19 $, $Date: 2005/07/28 10:04:33 $
 * @module scheme
 */
public final class SchemeDatabase extends StorableObjectDatabase<Scheme> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeWrapper.COLUMN_LABEL + COMMA
					+ SchemeWrapper.COLUMN_WIDTH + COMMA
					+ SchemeWrapper.COLUMN_HEIGHT + COMMA
					+ SchemeWrapper.COLUMN_DOMAIN_ID + COMMA
					+ SchemeWrapper.COLUMN_MAP_ID + COMMA
					+ SchemeWrapper.COLUMN_SYMBOL_ID + COMMA
					+ SchemeWrapper.COLUMN_UGO_CELL_ID + COMMA
					+ SchemeWrapper.COLUMN_SCHEME_CELL_ID + COMMA
					+ SchemeWrapper.COLUMN_KIND + COMMA
					+ SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEME_CODE;
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
			Scheme storableObject)
			throws IllegalDataException {
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLabel(), SchemeWrapper.SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getWidth() + COMMA
				+ storableObject.getHeight() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMapId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSchemeCellId()) + COMMA
				+ storableObject.getKind().value() + COMMA
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
			Scheme storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLabel(), SchemeWrapper.SIZE_LABEL_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getWidth());
		preparedStatement.setInt(++startParameterNumber, storableObject.getHeight());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMapId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getUgoCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSchemeCellId());
		preparedStatement.setInt(++startParameterNumber, storableObject.getKind().value());
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
	protected Scheme updateEntityFromResultSet(
			Scheme storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		Date created = new Date();
		Scheme scheme = storableObject == null
				? new Scheme(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						created,
						created,
						null,
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						0,
						0,
						null,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		scheme.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SchemeWrapper.COLUMN_LABEL)),
				resultSet.getInt(SchemeWrapper.COLUMN_WIDTH),
				resultSet.getInt(SchemeWrapper.COLUMN_HEIGHT),
				IdlKind.from_int(resultSet.getInt(SchemeWrapper.COLUMN_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_DOMAIN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_MAP_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return scheme;
	}
}
