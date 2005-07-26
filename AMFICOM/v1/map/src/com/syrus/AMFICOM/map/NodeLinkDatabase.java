/*
 * $Id: NodeLinkDatabase.java,v 1.30 2005/07/26 11:41:05 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.30 $, $Date: 2005/07/26 11:41:05 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class NodeLinkDatabase extends StorableObjectDatabase {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	private NodeLink fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TopologicalNode)
			return (NodeLink) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	@Override
	protected short getEntityCode() {		
		return ObjectEntities.NODELINK_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
				+ NodeLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_END_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_LENGTH;
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
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final NodeLink nodeLink = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, nodeLink.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, nodeLink.getPhysicalLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, nodeLink.getStartNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, nodeLink.getEndNode().getId());
		preparedStatement.setDouble(++startParameterNumber, nodeLink.getLength());
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final NodeLink nodeLink = fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(nodeLink.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getPhysicalLink().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getStartNode().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getEndNode().getId()) + COMMA
			+ nodeLink.getLength();
		return values;
	}
	
	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final NodeLink nodeLink = (storableObject == null)
				? new NodeLink(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						0.0)
					: fromStorableObject(storableObject);

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
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				physicalLink,
				startNode,
				endNode,
				resultSet.getDouble(NodeLinkWrapper.COLUMN_LENGTH));
		return nodeLink;
	}

}
