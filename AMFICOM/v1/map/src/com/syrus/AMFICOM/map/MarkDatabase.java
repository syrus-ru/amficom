/*
 * $Id: MarkDatabase.java,v 1.34 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.MARK_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.34 $, $Date: 2005/12/02 11:24:13 $
 * @author $Author: bass $
 * @module map
 */
public final class MarkDatabase extends StorableObjectDatabase<Mark> {
	 public static final int SIZE_CITY_COLUMN = 128;
    public static final int SIZE_STREET_COLUMN = 128;
    public static final int SIZE_BUILDING_COLUMN = 128;

	private static String columns;
	
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return MARK_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MarkWrapper.COLUMN_LONGITUDE + COMMA
				+ MarkWrapper.COLUMN_LATIUDE + COMMA
				+ MarkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
				+ MarkWrapper.COLUMN_DISTANCE + COMMA
				+ MarkWrapper.COLUMN_CITY  + COMMA
				+ MarkWrapper.COLUMN_STREET + COMMA
				+ MarkWrapper.COLUMN_BUILDING;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final Mark storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLocation().getX());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLocation().getY());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getPhysicalLink().getId());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getDistance());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCity(), SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getStreet(), SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getBuilding(), SIZE_BUILDING_COLUMN);
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Mark storableObject) {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ storableObject.getLocation().getX() + COMMA
			+ storableObject.getLocation().getY() + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getPhysicalLink().getId()) + COMMA
			+ storableObject.getDistance() + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getCity(), SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getStreet(), SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getBuilding(), SIZE_BUILDING_COLUMN);
		return values;
	}
	
	@Override
	protected Mark updateEntityFromResultSet(final Mark storableObject, final ResultSet resultSet)
			throws RetrieveObjectException,
				SQLException {
		final Mark mark = (storableObject == null) ? new Mark(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID),
				null,
				StorableObjectVersion.ILLEGAL_VERSION,
				null,
				null,
				0.0,
				0.0,
				null,
				0.0,
				null,
				null,
				null) : storableObject;
				
		PhysicalLink physicalLink;
		try {
			physicalLink = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, MarkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
		} catch (ApplicationException ae) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		mark.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getDouble(MarkWrapper.COLUMN_LONGITUDE),
				resultSet.getDouble(MarkWrapper.COLUMN_LATIUDE),
				physicalLink,
				resultSet.getDouble(MarkWrapper.COLUMN_DISTANCE),
				DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_CITY)),
				DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_STREET)),
				DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_BUILDING)));
		return mark;
	}

}
