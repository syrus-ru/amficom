/*-
 * $Id: SchemeDatabase.java,v 1.22 2005/12/02 11:24:16 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_HEIGHT;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_KIND;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_LABEL;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_MAP_ID;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_SCHEME_CELL_ID;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_SYMBOL_ID;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_UGO_CELL_ID;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.COLUMN_WIDTH;
import static com.syrus.AMFICOM.scheme.SchemeWrapper.SIZE_LABEL_COLUMN;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/12/02 11:24:16 $
 * @module scheme
 */
public final class SchemeDatabase extends StorableObjectDatabase<Scheme> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_LABEL + COMMA
					+ COLUMN_WIDTH + COMMA
					+ COLUMN_HEIGHT + COMMA
					+ COLUMN_DOMAIN_ID + COMMA
					+ COLUMN_MAP_ID + COMMA
					+ COLUMN_SYMBOL_ID + COMMA
					+ COLUMN_UGO_CELL_ID + COMMA
					+ COLUMN_SCHEME_CELL_ID + COMMA
					+ COLUMN_KIND + COMMA
					+ COLUMN_PARENT_SCHEME_ELEMENT_ID;
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
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLabel(), SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getWidth() + COMMA
				+ storableObject.getHeight() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMapId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSchemeCellId()) + COMMA
				+ storableObject.getKind().value() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeElementId());
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
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLabel(), SIZE_LABEL_COLUMN);
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
		final Date created = new Date();
		final Scheme scheme = (storableObject == null)
				? new Scheme(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
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
		scheme.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_LABEL)),
				resultSet.getInt(COLUMN_WIDTH),
				resultSet.getInt(COLUMN_HEIGHT),
				IdlKind.from_int(resultSet.getInt(COLUMN_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MAP_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SYMBOL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_UGO_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SCHEME_CELL_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return scheme;
	}
}
