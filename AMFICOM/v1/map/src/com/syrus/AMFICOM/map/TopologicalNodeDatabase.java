/*
 * $Id: TopologicalNodeDatabase.java,v 1.38 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.38 $, $Date: 2005/12/02 11:24:13 $
 * @author $Author: bass $
 * @module map
 */
public final class TopologicalNodeDatabase extends StorableObjectDatabase<TopologicalNode> {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return TOPOLOGICALNODE_CODE;
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
	protected int setEntityForPreparedStatementTmpl(final TopologicalNode storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLocation().getX());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLocation().getY());
		preparedStatement.setInt(++startParameterNumber, storableObject.isActive() ? 1 : 0);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final TopologicalNode storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ storableObject.getLocation().getX() + COMMA
			+ storableObject.getLocation().getY() + COMMA
			+ (storableObject.isActive() ? 1 : 0);
		return values;
	}

	@Override
	protected TopologicalNode updateEntityFromResultSet(final TopologicalNode storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final TopologicalNode topologicalNode = (storableObject == null)
				? new TopologicalNode(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						0.0,
						0.0,
						false)
					: storableObject;

		topologicalNode.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getDouble(TopologicalNodeWrapper.COLUMN_LONGITUDE),
				resultSet.getDouble(TopologicalNodeWrapper.COLUMN_LATIUDE),
				resultSet.getInt(TopologicalNodeWrapper.COLUMN_ACTIVE) == 1);
		return topologicalNode;
	}
}
