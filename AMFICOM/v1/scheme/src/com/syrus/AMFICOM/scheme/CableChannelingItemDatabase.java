/*-
 * $Id: CableChannelingItemDatabase.java,v 1.22 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_END_SITE_NODE_ID;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_END_SPARE;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_PHYSICAL_LINK_ID;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_PIPE_BLOCK_ID;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_PLACE_Y;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_ROW_X;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_SEQUENTIAL_NUMBER;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_START_SITE_NODE_ID;
import static com.syrus.AMFICOM.scheme.CableChannelingItemWrapper.COLUMN_START_SPARE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2006/07/02 22:36:13 $
 * @module scheme
 */
public final class CableChannelingItemDatabase extends StorableObjectDatabase<CableChannelingItem> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_START_SPARE + COMMA
					+ COLUMN_END_SPARE + COMMA
					+ COLUMN_ROW_X + COMMA
					+ COLUMN_PLACE_Y + COMMA
					+ COLUMN_SEQUENTIAL_NUMBER + COMMA
					+ COLUMN_PHYSICAL_LINK_ID + COMMA
					+ COLUMN_PIPE_BLOCK_ID + COMMA
					+ COLUMN_START_SITE_NODE_ID + COMMA
					+ COLUMN_END_SITE_NODE_ID + COMMA
					+ COLUMN_PARENT_SCHEME_CABLE_LINK_ID;
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
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param cableChannelingItem
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final CableChannelingItem cableChannelingItem) {
		return cableChannelingItem.getRowX() + COMMA
				+ cableChannelingItem.getPlaceY() + COMMA
				+ cableChannelingItem.getSequentialNumber() + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getPhysicalLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getPipeBlockId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getStartSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getEndSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getParentSchemeCableLinkId());
	}

	/**
	 * @param cableChannelingItem
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final CableChannelingItem cableChannelingItem,
			final PreparedStatement preparedStatement,
			final int startParameterNumber)
	throws SQLException {
		int startParameterNumber1 = startParameterNumber;
		preparedStatement.setDouble(++startParameterNumber1, cableChannelingItem.getStartSpare());
		preparedStatement.setDouble(++startParameterNumber1, cableChannelingItem.getEndSpare());
		preparedStatement.setInt(++startParameterNumber1, cableChannelingItem.getRowX());
		preparedStatement.setInt(++startParameterNumber1, cableChannelingItem.getPlaceY());
		preparedStatement.setInt(++startParameterNumber1, cableChannelingItem.getSequentialNumber());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, cableChannelingItem.getPhysicalLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, cableChannelingItem.getPipeBlockId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, cableChannelingItem.getStartSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, cableChannelingItem.getEndSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, cableChannelingItem.getParentSchemeCableLinkId());
		return startParameterNumber1;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected CableChannelingItem updateEntityFromResultSet(
			final CableChannelingItem storableObject,
			final ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final CableChannelingItem cableChannelingItem = (storableObject == null)
				? new CableChannelingItem(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						0d,
						0d,
						0,
						0,
						0,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		cableChannelingItem.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				resultSet.getDouble(COLUMN_START_SPARE),
				resultSet.getDouble(COLUMN_END_SPARE),
				resultSet.getInt(COLUMN_ROW_X),
				resultSet.getInt(COLUMN_PLACE_Y),
				resultSet.getInt(COLUMN_SEQUENTIAL_NUMBER),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PHYSICAL_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PIPE_BLOCK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_START_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_END_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_CABLE_LINK_ID));
		return cableChannelingItem;
	}
}
