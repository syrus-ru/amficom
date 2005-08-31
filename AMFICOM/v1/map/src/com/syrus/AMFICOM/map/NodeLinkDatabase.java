/*
 * $Id: NodeLinkDatabase.java,v 1.35 2005/08/31 05:50:36 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;

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

/**
 * @version $Revision: 1.35 $, $Date: 2005/08/31 05:50:36 $
 * @author $Author: bass $
 * @module map
 */
public final class NodeLinkDatabase extends StorableObjectDatabase<NodeLink> {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return NODELINK_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final NodeLink storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getPhysicalLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getStartNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEndNode().getId());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLength());
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final NodeLink storableObject) throws IllegalDataException {
		final String values = DatabaseIdentifier.toSQLString(storableObject.getPhysicalLink().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getStartNode().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getEndNode().getId()) + COMMA
			+ storableObject.getLength();
		return values;
	}
	
	@Override
	protected NodeLink updateEntityFromResultSet(final NodeLink storableObject, final ResultSet resultSet)
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
					: storableObject;

		PhysicalLink physicalLink;
		AbstractNode startNode;
		AbstractNode endNode;

		try {
			physicalLink = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
			startNode = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					NodeLinkWrapper.COLUMN_START_NODE_ID), true);
			endNode = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
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
				physicalLink,
				startNode,
				endNode,
				resultSet.getDouble(NodeLinkWrapper.COLUMN_LENGTH));
		return nodeLink;
	}

}
