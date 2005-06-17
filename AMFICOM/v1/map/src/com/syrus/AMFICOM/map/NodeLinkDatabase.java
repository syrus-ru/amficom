/*
 * $Id: NodeLinkDatabase.java,v 1.26 2005/06/17 12:40:40 bass Exp $
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
 * @version $Revision: 1.26 $, $Date: 2005/06/17 12:40:40 $
 * @author $Author: bass $
 * @module map_v1
 */
public final class NodeLinkDatabase extends CharacterizableDatabase {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	private NodeLink fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TopologicalNode)
			return (NodeLink) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		NodeLink nodeLink = this.fromStorableObject(storableObject);
		this.retrieveEntity(nodeLink);
	}	
	
	protected short getEntityCode() {		
		return ObjectEntities.NODELINK_CODE;
	}	
	
	protected String getColumnsTmpl() {
		if (columns == null){
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
				+ NodeLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_END_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_LENGTH;
		}
		return columns;
	}	
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		NodeLink nodeLink = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, nodeLink.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, nodeLink.getPhysicalLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, nodeLink.getStartNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, nodeLink.getEndNode().getId());
		preparedStatement.setDouble(++startParameterNumber, nodeLink.getLength());
		return startParameterNumber;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		NodeLink nodeLink = fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(nodeLink.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getPhysicalLink().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getStartNode().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getEndNode().getId()) + COMMA
			+ nodeLink.getLength();
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		NodeLink nodeLink = (storableObject == null) ?
				new NodeLink(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, 0.0) :
					fromStorableObject(storableObject);

		PhysicalLink physicalLink;
		AbstractNode startNode;
		AbstractNode endNode;

		try {
			physicalLink = (PhysicalLink) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
			startNode = (AbstractNode) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					NodeLinkWrapper.COLUMN_START_NODE_ID), true);
			endNode = (AbstractNode) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					NodeLinkWrapper.COLUMN_END_NODE_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}

		nodeLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				physicalLink,
				startNode,
				endNode,
				resultSet.getDouble(NodeLinkWrapper.COLUMN_LENGTH));
		return nodeLink;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		NodeLink nodeLink = this.fromStorableObject(storableObject);
		switch (retrieveKind) {			
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  nodeLink.getId() + "'; argument: " + arg);
				return null;
		}
	}	

}
