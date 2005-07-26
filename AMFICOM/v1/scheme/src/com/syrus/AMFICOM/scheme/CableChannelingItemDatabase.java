/*-
 * $Id: CableChannelingItemDatabase.java,v 1.16 2005/07/26 12:52:23 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.16 $, $Date: 2005/07/26 12:52:23 $
 * @module scheme
 */
public final class CableChannelingItemDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private CableChannelingItem fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof CableChannelingItem)
			return (CableChannelingItem) storableObject;
		throw new IllegalDataException("CableChannelingItemDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

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
			StorableObject storableObject)
			throws IllegalDataException {
		CableChannelingItem cableChannelingItem = fromStorableObject(storableObject);
		String sql = cableChannelingItem.getRowX() + COMMA
				+ cableChannelingItem.getPlaceY() + COMMA
				+ cableChannelingItem.getSequentialNumber() + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getPhysicalLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getStartSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getEndSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getParentSchemeCableLinkId());
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
		CableChannelingItem cableChannelingItem = fromStorableObject(storableObject);
		preparedStatement.setDouble(++startParameterNumber, cableChannelingItem.getStartSpare());
		preparedStatement.setDouble(++startParameterNumber, cableChannelingItem.getEndSpare());
		preparedStatement.setInt(++startParameterNumber, cableChannelingItem.getRowX());
		preparedStatement.setInt(++startParameterNumber, cableChannelingItem.getPlaceY());
		preparedStatement.setInt(++startParameterNumber, cableChannelingItem.getSequentialNumber());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getPhysicalLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getStartSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getEndSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getParentSchemeCableLinkId());
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
		CableChannelingItem cableChannelingItem;
		if (storableObject == null) {
			Date created = new Date();
			cableChannelingItem = new CableChannelingItem(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
					null);
		} else {
			cableChannelingItem = fromStorableObject(storableObject);
		}
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
