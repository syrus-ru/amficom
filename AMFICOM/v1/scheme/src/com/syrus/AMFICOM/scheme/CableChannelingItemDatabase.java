/*-
 * $Id: CableChannelingItemDatabase.java,v 1.17 2005/07/28 10:04:34 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;

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

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.17 $, $Date: 2005/07/28 10:04:34 $
 * @module scheme
 */
public final class CableChannelingItemDatabase extends StorableObjectDatabase<CableChannelingItem> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = CableChannelingItemWrapper.COLUMN_START_SPARE + COMMA
					+ CableChannelingItemWrapper.COLUMN_END_SPARE + COMMA
					+ CableChannelingItemWrapper.COLUMN_ROW_X + COMMA
					+ CableChannelingItemWrapper.COLUMN_PLACE_Y + COMMA
					+ CableChannelingItemWrapper.COLUMN_SEQUENTIAL_NUMBER + COMMA
					+ CableChannelingItemWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
					+ CableChannelingItemWrapper.COLUMN_START_SITE_NODE_ID + COMMA
					+ CableChannelingItemWrapper.COLUMN_END_SITE_NODE_ID + COMMA
					+ CableChannelingItemWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return CABLECHANNELINGITEM_CODE;
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
	protected String getUpdateSingleSQLValuesTmpl(
			CableChannelingItem storableObject)
			throws IllegalDataException {
		String sql = storableObject.getRowX() + COMMA
				+ storableObject.getPlaceY() + COMMA
				+ storableObject.getSequentialNumber() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getPhysicalLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getStartSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getEndSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeCableLinkId());
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
			CableChannelingItem storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		preparedStatement.setDouble(++startParameterNumber, storableObject.getStartSpare());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getEndSpare());
		preparedStatement.setInt(++startParameterNumber, storableObject.getRowX());
		preparedStatement.setInt(++startParameterNumber, storableObject.getPlaceY());
		preparedStatement.setInt(++startParameterNumber, storableObject.getSequentialNumber());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getPhysicalLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getStartSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEndSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeCableLinkId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected CableChannelingItem updateEntityFromResultSet(
			CableChannelingItem storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		Date created = new Date();
		CableChannelingItem cableChannelingItem = storableObject == null
				? new CableChannelingItem(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						created,
						created,
						null,
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						0d,
						0d,
						0,
						0,
						0,
						null,
						null,
						null,
						null)
				: storableObject;
		cableChannelingItem.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				resultSet.getDouble(CableChannelingItemWrapper.COLUMN_START_SPARE),
				resultSet.getDouble(CableChannelingItemWrapper.COLUMN_END_SPARE),
				resultSet.getInt(CableChannelingItemWrapper.COLUMN_ROW_X),
				resultSet.getInt(CableChannelingItemWrapper.COLUMN_PLACE_Y),
				resultSet.getInt(CableChannelingItemWrapper.COLUMN_SEQUENTIAL_NUMBER),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_PHYSICAL_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_START_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_END_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID));
		return cableChannelingItem;
	}
}
