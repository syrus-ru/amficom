/*
 * $Id: TopologicalNodeDatabase.java,v 1.20 2005/03/10 15:39:16 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.20 $, $Date: 2005/03/10 15:39:16 $
 * @author $Author: bob $
 * @module map_v1
 */
public class TopologicalNodeDatabase extends CharacterizableDatabase {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	private TopologicalNode fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TopologicalNode)
			return (TopologicalNode) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		this.retrieveEntity(topologicalNode);		
		this.retrievePhysicalLink(topologicalNode);
	}	
	
	private void retrievePhysicalLink(TopologicalNode node) throws RetrieveObjectException, ObjectNotFoundException{
		String nodeIdStr = DatabaseIdentifier.toSQLString(node.getId()); 
		String sql = SQL_SELECT + NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + SQL_FROM
				+ ObjectEntities.NODE_LINK_ENTITY + SQL_WHERE 
				+ NodeLinkWrapper.COLUMN_START_NODE_ID + EQUALS + nodeIdStr + SQL_OR
				+ NodeLinkWrapper.COLUMN_END_NODE_ID + EQUALS + nodeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.retrievePhysicalLink | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){				
				try {
					node.setPhysicalLink((PhysicalLink)MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), true));
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
			throw new ObjectNotFoundException("No physical link for node " + nodeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retrievePhysicalLink | Cannot retrieve physical link for node " + nodeIdStr + " -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	private void retrievePhysicalLinks(Collection topologicalNodes) throws RetrieveObjectException, IllegalDataException{
		if (topologicalNodes == null || topologicalNodes.isEmpty())
			return;
		String startNodeIdStrs;
		String endNodeIdStrs;
		{
			StringBuffer startNodeBuffer = new StringBuffer(NodeLinkWrapper.COLUMN_START_NODE_ID);
			startNodeBuffer.append(SQL_IN);
			startNodeBuffer.append(OPEN_BRACKET);
			
			StringBuffer endNodeBuffer = new StringBuffer(NodeLinkWrapper.COLUMN_END_NODE_ID);
			endNodeBuffer.append(SQL_IN);
			endNodeBuffer.append(OPEN_BRACKET);
			
			int i = 1;
			for (Iterator it = topologicalNodes.iterator(); it.hasNext();i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identifiable)
					id = ((Identifiable)object).getId();
				else throw new IllegalDataException(this.getEnityName() + "Database.retrievePhysicalLinks | Object " +
													object.getClass().getName()
													+ " isn't Identifier or Identifiable");

				if (id != null){
					startNodeBuffer.append(DatabaseIdentifier.toSQLString(id));
					endNodeBuffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()) {
						if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0)){
							startNodeBuffer.append(COMMA);
							endNodeBuffer.append(COMMA);
						}
						else {
							startNodeBuffer.append(CLOSE_BRACKET);
							startNodeBuffer.append(SQL_OR);
							startNodeBuffer.append(NodeLinkWrapper.COLUMN_START_NODE_ID);				
							startNodeBuffer.append(SQL_IN);
							startNodeBuffer.append(OPEN_BRACKET);
							
							endNodeBuffer.append(CLOSE_BRACKET);
							endNodeBuffer.append(SQL_OR);
							endNodeBuffer.append(NodeLinkWrapper.COLUMN_END_NODE_ID);				
							endNodeBuffer.append(SQL_IN);
							endNodeBuffer.append(OPEN_BRACKET);
						}
					}
				}
			}
			startNodeBuffer.append(CLOSE_BRACKET);
			endNodeBuffer.append(CLOSE_BRACKET);
			
			startNodeIdStrs = startNodeBuffer.toString();
			endNodeIdStrs = endNodeBuffer.toString();
		}
		String sql = SQL_SELECT 
				+ NodeLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_END_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA 
				+ SQL_FROM + ObjectEntities.NODE_LINK_ENTITY + SQL_WHERE 
				+ startNodeIdStrs + SQL_OR + endNodeIdStrs;
		 Statement statement = null;
	        ResultSet resultSet = null;
	        Connection connection = DatabaseConnection.getConnection();
	        try {
	            statement = connection.createStatement();
	            Log.debugMessage(this.getEnityName() + "Database.retrievePhysicalLinks | Trying: " + sql, Log.DEBUGLEVEL09);
	            resultSet = statement.executeQuery(sql.toString());
	            Map nodePhysicalLinkMap = new HashMap();
	            while (resultSet.next()) {
	                Identifier startNodeId = DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_START_NODE_ID);
	                Identifier endNodeId = DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_END_NODE_ID);
	                TopologicalNode node = null;
	                for (Iterator it = topologicalNodes.iterator(); it.hasNext();) {
	                	TopologicalNode nodeToCompare = (TopologicalNode) it.next();
	                    if (nodeToCompare.getId().equals(startNodeId) || nodeToCompare.getId().equals(endNodeId)){
	                    	node = nodeToCompare;
	                        break;
	                    }	                    
	                }
	                
	                if (node == null){
	                    String mesg = this.getEnityName() + "Database.retrievePhysicalLinks | Cannot found correspond node " ;
	                    throw new RetrieveObjectException(mesg);
	                }
	                    
	                PhysicalLink physicalLink = (PhysicalLink)nodePhysicalLinkMap.get(node);
	                if (physicalLink != null)
	                	continue;
	                try {                    
	                	physicalLink = (PhysicalLink) MapStorableObjectPool
	                            .getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
	                } catch (ApplicationException ae) {
	                    throw new RetrieveObjectException(ae);
	                }
	                nodePhysicalLinkMap.put(node, physicalLink);         
	            }
	            
	            for (Iterator iter = nodePhysicalLinkMap.keySet().iterator(); iter.hasNext();) {
	            	/** 
	            	 * topologicalNode refer to item of input list topologicalNodes
	            	 *  that why modifing item of map we modify item of list   
	            	 */
	            	TopologicalNode topologicalNode = (TopologicalNode) iter.next();
	                PhysicalLink physicalLink = (PhysicalLink)nodePhysicalLinkMap.get(topologicalNode);
	                topologicalNode.setPhysicalLink(physicalLink);
	            }
	            
	        } catch (SQLException sqle) {
	            String mesg = this.getEnityName() + "Database.retrievePhysicalLinks | Cannot retrieve parameters for result -- " + sqle.getMessage();
	            throw new RetrieveObjectException(mesg, sqle);
	        } finally {
	            try {
	                if (statement != null)
	                    statement.close();
	                if (resultSet != null)
	                    resultSet.close();
	                statement = null;
	                resultSet = null;
	            } catch (SQLException sqle1) {
	                Log.errorException(sqle1);
	            } finally {
	                DatabaseConnection.releaseConnection(connection);
	            }
	        }   
	}
	
	protected String getEnityName() {		
		return ObjectEntities.TOPOLOGICAL_NODE_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ TopologicalNodeWrapper.COLUMN_LONGITUDE + COMMA
				+ TopologicalNodeWrapper.COLUMN_LATIUDE + COMMA 
				+ TopologicalNodeWrapper.COLUMN_ACTIVE;
		}
		return super.getColumns(mode) + columns;
	}	
	
	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA 
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		TopologicalNode topologicalNode = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseString.setString(preparedStatement, ++i, topologicalNode.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, topologicalNode.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++i, topologicalNode.getLocation().getX());
		preparedStatement.setDouble(++i, topologicalNode.getLocation().getY());
		preparedStatement.setInt(++i, topologicalNode.isActive() ? 1 : 0);
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		TopologicalNode topologicalNode = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(topologicalNode.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(topologicalNode.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ topologicalNode.getLocation().getX() + COMMA
			+ topologicalNode.getLocation().getY() + COMMA
			+ (topologicalNode.isActive() ? 1 : 0);
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		TopologicalNode topologicalNode = (storableObject == null) ? 
				new TopologicalNode(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, 0.0, 0.0, false) : 
					fromStorableObject(storableObject);
				
		topologicalNode.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
							   resultSet.getDouble(TopologicalNodeWrapper.COLUMN_LONGITUDE),
							   resultSet.getDouble(TopologicalNodeWrapper.COLUMN_LATIUDE),
							   resultSet.getInt(TopologicalNodeWrapper.COLUMN_ACTIVE) == 1);		
		return topologicalNode;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		super.insertEntity(topologicalNode);
	}
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				return;
		}
	}
	
	
	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				return;
		}

	}	
	
	protected Collection retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Collection collection = super.retrieveByCondition(conditionQuery);
		this.retrievePhysicalLinks(collection);
		return collection;
	}
}

