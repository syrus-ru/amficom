/*
 * $Id: MarkDatabase.java,v 1.28 2005/07/24 17:38:43 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.28 $, $Date: 2005/07/24 17:38:43 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class MarkDatabase extends StorableObjectDatabase {
	 public static final int SIZE_CITY_COLUMN = 128;
    public static final int SIZE_STREET_COLUMN = 128;
    public static final int SIZE_BUILDING_COLUMN = 128;

	private static String columns;
	
	private static String updateMultipleSQLValues;

	private Mark fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Mark)
			return (Mark) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MARK_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null){
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
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ QUESTION
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	@Override
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final Mark mark = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mark.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mark.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, mark.getLocation().getX());
		preparedStatement.setDouble(++startParameterNumber, mark.getLocation().getY());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mark.getPhysicalLink().getId());
		preparedStatement.setDouble(++startParameterNumber, mark.getDistance());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mark.getCity(), SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mark.getStreet(), SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mark.getBuilding(), SIZE_BUILDING_COLUMN);
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		final Mark mark = fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(mark.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(mark.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ mark.getLocation().getX() + COMMA
			+ mark.getLocation().getY() + COMMA
			+ DatabaseIdentifier.toSQLString(mark.getPhysicalLink().getId()) + COMMA
			+ mark.getDistance() + COMMA
			+ DatabaseString.toQuerySubString(mark.getCity(), SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(mark.getStreet(), SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(mark.getBuilding(), SIZE_BUILDING_COLUMN);
		return values;
	}
	
	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		final Mark mark = (storableObject == null) ?
				new Mark(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, 0.0, 0.0, null, 0.0, null, null, null) :
					fromStorableObject(storableObject);
				
		PhysicalLink physicalLink;
		try {
			physicalLink = (PhysicalLink) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, MarkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		mark.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
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
