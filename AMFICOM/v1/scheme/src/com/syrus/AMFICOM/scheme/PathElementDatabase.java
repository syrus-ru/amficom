/*-
 * $Id: PathElementDatabase.java,v 1.16 2005/07/28 10:04:34 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/07/28 10:04:34 $
 * @module scheme
 */
public final class PathElementDatabase extends StorableObjectDatabase<PathElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID + COMMA
					+ PathElementWrapper.COLUMN_SEQUENTIAL_NUMBER + COMMA
					+ PathElementWrapper.COLUMN_KIND + COMMA
					+ PathElementWrapper.COLUMN_START_ABSTRACT_SCHEME_PORT_ID + COMMA
					+ PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID + COMMA
					+ PathElementWrapper.COLUMN_SCHEME_CABLE_THREAD_ID + COMMA
					+ PathElementWrapper.COLUMN_SCHEME_LINK_ID;					
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return PATHELEMENT_CODE;
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
			PathElement storableObject)
			throws IllegalDataException {
		String sql = DatabaseIdentifier.toSQLString(storableObject.getParentSchemePathId()) + COMMA
				+ storableObject.getSequentialNumber() + COMMA
				+ storableObject.getKind().value() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getStartAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getEndAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSchemeCableThreadId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSchemeLinkId());
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
			PathElement storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemePathId());
		preparedStatement.setInt(++startParameterNumber, storableObject.getSequentialNumber());
		preparedStatement.setInt(++startParameterNumber, storableObject.getKind().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getStartAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEndAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSchemeCableThreadId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSchemeLinkId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected PathElement updateEntityFromResultSet(
			PathElement storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		Date created = new Date();
		PathElement pathElement = storableObject == null
				? pathElement = new PathElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						created,
						created,
						null,
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						0,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		pathElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID),
				resultSet.getInt(PathElementWrapper.COLUMN_SEQUENTIAL_NUMBER),
				IdlKind.from_int(resultSet.getInt(PathElementWrapper.COLUMN_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_START_ABSTRACT_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_SCHEME_CABLE_THREAD_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PathElementWrapper.COLUMN_SCHEME_LINK_ID));
		return pathElement;
	}
}
