/*-
 * $Id: PathElementDatabase.java,v 1.20 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.PathElementWrapper.COLUMN_END_ABSTRACT_SCHEME_PORT_ID;
import static com.syrus.AMFICOM.scheme.PathElementWrapper.COLUMN_KIND;
import static com.syrus.AMFICOM.scheme.PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID;
import static com.syrus.AMFICOM.scheme.PathElementWrapper.COLUMN_SCHEME_CABLE_THREAD_ID;
import static com.syrus.AMFICOM.scheme.PathElementWrapper.COLUMN_SCHEME_LINK_ID;
import static com.syrus.AMFICOM.scheme.PathElementWrapper.COLUMN_SEQUENTIAL_NUMBER;
import static com.syrus.AMFICOM.scheme.PathElementWrapper.COLUMN_START_ABSTRACT_SCHEME_PORT_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.20 $, $Date: 2006/07/02 22:36:13 $
 * @module scheme
 */
public final class PathElementDatabase extends StorableObjectDatabase<PathElement> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_PARENT_SCHEME_PATH_ID + COMMA
					+ COLUMN_SEQUENTIAL_NUMBER + COMMA
					+ COLUMN_KIND + COMMA
					+ COLUMN_START_ABSTRACT_SCHEME_PORT_ID + COMMA
					+ COLUMN_END_ABSTRACT_SCHEME_PORT_ID + COMMA
					+ COLUMN_SCHEME_CABLE_THREAD_ID + COMMA
					+ COLUMN_SCHEME_LINK_ID;					
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
	 * @param pathElement
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final PathElement pathElement) {
		return DatabaseIdentifier.toSQLString(pathElement.getParentSchemePathId()) + COMMA
				+ pathElement.getSequentialNumber() + COMMA
				+ pathElement.getKind().value() + COMMA
				+ DatabaseIdentifier.toSQLString(pathElement.getStartAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(pathElement.getEndAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(pathElement.getSchemeCableThreadId()) + COMMA
				+ DatabaseIdentifier.toSQLString(pathElement.getSchemeLinkId());
	}

	/**
	 * @param pathElement
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final PathElement pathElement,
			final PreparedStatement preparedStatement,
			final int startParameterNumber)
	throws SQLException {
		int startParameterNumber1 = startParameterNumber;
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, pathElement.getParentSchemePathId());
		preparedStatement.setInt(++startParameterNumber1, pathElement.getSequentialNumber());
		preparedStatement.setInt(++startParameterNumber1, pathElement.getKind().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, pathElement.getStartAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, pathElement.getEndAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, pathElement.getSchemeCableThreadId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, pathElement.getSchemeLinkId());
		return startParameterNumber1;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected PathElement updateEntityFromResultSet(
			final PathElement storableObject,
			final ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final PathElement pathElement = (storableObject == null)
				? new PathElement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						0,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		pathElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_PATH_ID),
				resultSet.getInt(COLUMN_SEQUENTIAL_NUMBER),
				IdlKind.from_int(resultSet.getInt(COLUMN_KIND)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_START_ABSTRACT_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_END_ABSTRACT_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SCHEME_CABLE_THREAD_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SCHEME_LINK_ID));
		return pathElement;
	}
}
