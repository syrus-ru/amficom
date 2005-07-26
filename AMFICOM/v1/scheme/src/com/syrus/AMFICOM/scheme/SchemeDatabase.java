/*-
 * $Id: SchemeDatabase.java,v 1.18 2005/07/26 12:52:23 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.18 $, $Date: 2005/07/26 12:52:23 $
 * @module scheme
 */
public final class SchemeDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private Scheme fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Scheme)
			return (Scheme) storableObject;			
		throw new IllegalDataException("Scheme.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

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
			StorableObject storableObject)
			throws IllegalDataException {
		Scheme scheme = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(scheme.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(scheme.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(scheme.getLabel(), SchemeWrapper.SIZE_LABEL_COLUMN) + APOSTROPHE + COMMA
				+ scheme.getWidth() + COMMA
				+ scheme.getHeight() + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getMapId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getSymbolId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getUgoCellId()) + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getSchemeCellId()) + COMMA
				+ scheme.getKind().value() + COMMA
				+ DatabaseIdentifier.toSQLString(scheme.getParentSchemeElementId());
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
		Scheme scheme = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, scheme.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, scheme.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, scheme.getLabel(), SchemeWrapper.SIZE_LABEL_COLUMN);
		preparedStatement.setInt(++startParameterNumber, scheme.getWidth());
		preparedStatement.setInt(++startParameterNumber, scheme.getHeight());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getMapId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getSymbolId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getUgoCellId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getSchemeCellId());
		preparedStatement.setInt(++startParameterNumber, scheme.getKind().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, scheme.getParentSchemeElementId());
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
		Scheme scheme;
		if (storableObject == null) {
			Date created = new Date();
			scheme = new Scheme(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
					null);
		} else {
			scheme = fromStorableObject(storableObject);
		}
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
