/*-
 * $Id: CableChannelingItemDatabase.java,v 1.21 2005/12/02 11:24:16 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.21 $, $Date: 2005/12/02 11:24:16 $
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
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final CableChannelingItem storableObject)
			throws IllegalDataException {
		return storableObject.getRowX() + COMMA
				+ storableObject.getPlaceY() + COMMA
				+ storableObject.getSequentialNumber() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getPhysicalLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getPipeBlockId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getStartSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getEndSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeCableLinkId());
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
			final CableChannelingItem storableObject,
			final PreparedStatement preparedStatement,
			final int startParameterNumber)
	throws IllegalDataException, SQLException {
		int startParameterNumber1 = startParameterNumber;
		preparedStatement.setDouble(++startParameterNumber1, storableObject.getStartSpare());
		preparedStatement.setDouble(++startParameterNumber1, storableObject.getEndSpare());
		preparedStatement.setInt(++startParameterNumber1, storableObject.getRowX());
		preparedStatement.setInt(++startParameterNumber1, storableObject.getPlaceY());
		preparedStatement.setInt(++startParameterNumber1, storableObject.getSequentialNumber());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, storableObject.getPhysicalLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, storableObject.getPipeBlockId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, storableObject.getStartSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, storableObject.getEndSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber1, storableObject.getParentSchemeCableLinkId());
		return startParameterNumber1;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected CableChannelingItem updateEntityFromResultSet(
			final CableChannelingItem storableObject,
			final ResultSet resultSet)
	throws IllegalDataException, SQLException {
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
