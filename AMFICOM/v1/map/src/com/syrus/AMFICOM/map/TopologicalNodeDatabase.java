/*
 * $Id: TopologicalNodeDatabase.java,v 1.31 2005/07/24 17:38:43 arseniy Exp $
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.31 $, $Date: 2005/07/24 17:38:43 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class TopologicalNodeDatabase extends StorableObjectDatabase {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	private TopologicalNode fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TopologicalNode)
			return (TopologicalNode) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		this.retrieveEntity(topologicalNode);		
		this.retrievePhysicalLink(topologicalNode);
	}	

	private void retrievePhysicalLink(TopologicalNode node) throws RetrieveObjectException, ObjectNotFoundException{
		String nodeIdStr = DatabaseIdentifier.toSQLString(node.getId());
		String sql = SQL_SELECT + NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + SQL_FROM
				+ ObjectEntities.NODELINK + SQL_WHERE
				+ NodeLinkWrapper.COLUMN_START_NODE_ID + EQUALS + nodeIdStr + SQL_OR
				+ NodeLinkWrapper.COLUMN_END_NODE_ID + EQUALS + nodeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrievePhysicalLink | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){				
				try {
					node.setPhysicalLink((PhysicalLink) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							StorableObjectWrapper.COLUMN_ID), true));
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
			throw new ObjectNotFoundException("No physical link for node " + nodeIdStr);
		} catch (SQLException sqle) {
			String mesg = this.getEntityName() + "Database.retrievePhysicalLink | Cannot retrieve physical link for node " + nodeIdStr + " -- " + sqle.getMessage();
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

	private void retrievePhysicalLinks(final Set<TopologicalNode> topologicalNodes) throws RetrieveObjectException, IllegalDataException {
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
			for (Iterator it = topologicalNodes.iterator(); it.hasNext(); i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else
					if (object instanceof Identifiable)
						id = ((Identifiable) object).getId();
					else
						throw new IllegalDataException(this.getEntityName()
								+ "Database.retrievePhysicalLinks | Object "
								+ object.getClass().getName()
								+ " isn't Identifier or Identifiable");

				if (id != null) {
					startNodeBuffer.append(DatabaseIdentifier.toSQLString(id));
					endNodeBuffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()) {
						if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0)) {
							startNodeBuffer.append(COMMA);
							endNodeBuffer.append(COMMA);
						} else {
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
		final String sql = SQL_SELECT
				+ NodeLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_END_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
				+ SQL_FROM + ObjectEntities.NODELINK
				+ SQL_WHERE + startNodeIdStrs + SQL_OR + endNodeIdStrs;
		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrievePhysicalLinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			final Map<TopologicalNode, PhysicalLink> nodePhysicalLinkMap = new HashMap<TopologicalNode, PhysicalLink>();
			while (resultSet.next()) {
				final Identifier startNodeId = DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_START_NODE_ID);
				final Identifier endNodeId = DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_END_NODE_ID);
				TopologicalNode node = null;
				for (final TopologicalNode nodeToCompare : topologicalNodes) {
					if (nodeToCompare.getId().equals(startNodeId) || nodeToCompare.getId().equals(endNodeId)) {
						node = nodeToCompare;
						break;
					}
				}

				if (node == null) {
					final String mesg = this.getEntityName() + "Database.retrievePhysicalLinks | Cannot found correspond node ";
					throw new RetrieveObjectException(mesg);
				}

				PhysicalLink physicalLink = nodePhysicalLinkMap.get(node);
				if (physicalLink != null)
					continue;
				try {
					physicalLink = (PhysicalLink) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				nodePhysicalLinkMap.put(node, physicalLink);
			}

			for (final TopologicalNode topologicalNode : nodePhysicalLinkMap.keySet()) {
				/**
				 * topologicalNode refer to item of input list topologicalNodes that why
				 * modifing item of map we modify item of list
				 */
				final PhysicalLink physicalLink = nodePhysicalLinkMap.get(topologicalNode);
				topologicalNode.setPhysicalLink(physicalLink);
			}

		} catch (SQLException sqle) {
			String mesg = this.getEntityName()
					+ "Database.retrievePhysicalLinks | Cannot retrieve parameters for result -- "
					+ sqle.getMessage();
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

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.TOPOLOGICALNODE_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ TopologicalNodeWrapper.COLUMN_LONGITUDE + COMMA
				+ TopologicalNodeWrapper.COLUMN_LATIUDE + COMMA
				+ TopologicalNodeWrapper.COLUMN_ACTIVE;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final TopologicalNode topologicalNode = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, topologicalNode.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, topologicalNode.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, topologicalNode.getLocation().getX());
		preparedStatement.setDouble(++startParameterNumber, topologicalNode.getLocation().getY());
		preparedStatement.setInt(++startParameterNumber, topologicalNode.isActive() ? 1 : 0);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		final TopologicalNode topologicalNode = fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(topologicalNode.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(topologicalNode.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ topologicalNode.getLocation().getX() + COMMA
			+ topologicalNode.getLocation().getY() + COMMA
			+ (topologicalNode.isActive() ? 1 : 0);
		return values;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, SQLException {
		final TopologicalNode topologicalNode = (storableObject == null) ?
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

	@Override
	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set collection = super.retrieveByCondition(conditionQuery);
		this.retrievePhysicalLinks(collection);
		return collection;
	}
}
