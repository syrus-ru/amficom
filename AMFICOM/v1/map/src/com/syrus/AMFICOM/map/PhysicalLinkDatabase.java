/*
 * $Id: PhysicalLinkDatabase.java,v 1.27 2005/06/17 12:40:40 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.27 $, $Date: 2005/06/17 12:40:40 $
 * @author $Author: bass $
 * @module map_v1
 */
public final class PhysicalLinkDatabase extends CharacterizableDatabase {
	private static final int LEFT_RIGHT = 0x01;
    private static final int TOP_BOTTOM = 0x02;

	private static String columns;
	
	private static String updateMultipleSQLValues;
	
	private PhysicalLink fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PhysicalLink)
			return (PhysicalLink) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		PhysicalLink physicalLink = this.fromStorableObject(storableObject);
		this.retrieveEntity(physicalLink);
	}	
	
	protected short getEntityCode() {		
		return ObjectEntities.PHYSICALLINK_CODE;
	}	
	
	protected String getColumnsTmpl() {
		if (columns == null){
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
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){
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
	
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		PhysicalLink physicalLink = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLink.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLink.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, physicalLink.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLink.getCity(), MarkDatabase.SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLink.getStreet(), MarkDatabase.SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, physicalLink.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		preparedStatement.setInt(++startParameterNumber, physicalLink.getDimensionX());
		preparedStatement.setInt(++startParameterNumber, physicalLink.getDimensionY());			
		preparedStatement.setInt(++startParameterNumber, (physicalLink.isTopToBottom() ? TOP_BOTTOM : 0) | (physicalLink.isLeftToRight() ? LEFT_RIGHT : 0) );
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, physicalLink.getStartNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, physicalLink.getEndNode().getId());
		return startParameterNumber;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		PhysicalLink physicalLink = fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(physicalLink.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(physicalLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(physicalLink.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getCity(), MarkDatabase.SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getStreet(), MarkDatabase.SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN) + COMMA
			+ physicalLink.getDimensionX() + COMMA
			+ physicalLink.getDimensionY() + COMMA
			+ ((physicalLink.isTopToBottom() ? TOP_BOTTOM : 0) | (physicalLink.isLeftToRight() ? LEFT_RIGHT : 0)) + COMMA
			+ DatabaseIdentifier.toSQLString(physicalLink.getStartNode().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(physicalLink.getEndNode().getId());
		return values;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		PhysicalLink physicalLink = (storableObject == null) ? new PhysicalLink(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null, null, null, 0, 0, false, false)
				: fromStorableObject(storableObject);

		PhysicalLinkType type;
		AbstractNode startNode;
		AbstractNode endNode;
		try {
			type = (PhysicalLinkType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					PhysicalLinkWrapper.COLUMN_PHYSICAL_LINK_TYPE_ID), true);
			startNode = (AbstractNode) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					PhysicalLinkWrapper.COLUMN_START_NODE_ID), true);
			endNode = (AbstractNode) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					PhysicalLinkWrapper.COLUMN_END_NODE_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		int topLeft = resultSet.getInt(PhysicalLinkWrapper.COLUMN_TOPLEFT);
		physicalLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
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
				startNode,
				endNode);
		return physicalLink;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		PhysicalLink physicalLink = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  physicalLink.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
