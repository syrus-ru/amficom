/*
 * $Id: PhysicalLinkDatabase.java,v 1.38 2005/10/03 16:16:58 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.38 $, $Date: 2005/10/03 16:16:58 $
 * @author $Author: krupenn $
 * @module map
 */
public final class PhysicalLinkDatabase extends StorableObjectDatabase<PhysicalLink> {
	private static final int LEFT_RIGHT = 0x01;
    private static final int TOP_BOTTOM = 0x02;

	private static String columns;
	
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {		
		return PHYSICALLINK_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ PhysicalLinkWrapper.COLUMN_PHYSICAL_LINK_TYPE_ID + COMMA
				+ PhysicalLinkWrapper.COLUMN_CITY + COMMA
				+ PhysicalLinkWrapper.COLUMN_STREET + COMMA
				+ PhysicalLinkWrapper.COLUMN_BUILDING + COMMA
				+ PhysicalLinkWrapper.COLUMN_DIMENSION_X + COMMA
				+ PhysicalLinkWrapper.COLUMN_DIMENSION_Y + COMMA
				+ PhysicalLinkWrapper.COLUMN_TOPLEFT + COMMA
				+ PhysicalLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ PhysicalLinkWrapper.COLUMN_END_NODE_ID;
		}
		return columns;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final PhysicalLink storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName0(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCity(), MarkDatabase.SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getStreet(), MarkDatabase.SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getDimensionX());
		preparedStatement.setInt(++startParameterNumber, storableObject.getDimensionY());			
		preparedStatement.setInt(++startParameterNumber, (storableObject.isTopToBottom() ? TOP_BOTTOM : 0) | (storableObject.isLeftToRight() ? LEFT_RIGHT : 0) );
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getStartNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEndNodeId());
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final PhysicalLink storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName0(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getCity(), MarkDatabase.SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getStreet(), MarkDatabase.SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN) + COMMA
			+ storableObject.getDimensionX() + COMMA
			+ storableObject.getDimensionY() + COMMA
			+ ((storableObject.isTopToBottom() ? TOP_BOTTOM : 0) | (storableObject.isLeftToRight() ? LEFT_RIGHT : 0)) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getStartNodeId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getEndNodeId());
		return values;
	}

	@Override
	protected PhysicalLink updateEntityFromResultSet(final PhysicalLink storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final PhysicalLink physicalLink = (storableObject == null) ? new PhysicalLink(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID),
				null,
				StorableObjectVersion.ILLEGAL_VERSION,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				0,
				0,
				false,
				false,
				false) : storableObject;

		PhysicalLinkType type;
		try {
			type = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					PhysicalLinkWrapper.COLUMN_PHYSICAL_LINK_TYPE_ID), true);
		} catch (ApplicationException ae) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		final int topLeft = resultSet.getInt(PhysicalLinkWrapper.COLUMN_TOPLEFT);
		physicalLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				type,
				DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_CITY)),
				DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_STREET)),
				DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_BUILDING)),
				resultSet.getInt(PhysicalLinkWrapper.COLUMN_DIMENSION_X),
				resultSet.getInt(PhysicalLinkWrapper.COLUMN_DIMENSION_Y),
				(topLeft & LEFT_RIGHT) == 1,
				(topLeft & TOP_BOTTOM) == 1,
				DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkWrapper.COLUMN_START_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkWrapper.COLUMN_END_NODE_ID));
		return physicalLink;
	}

}
