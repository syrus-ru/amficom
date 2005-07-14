/*-
 * $Id: CableChannelingItemDatabase.java,v 1.10 2005/07/14 16:08:08 bass Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/07/14 16:08:08 $
 * @module scheme_v1
 */
public final class CableChannelingItemDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private CableChannelingItem fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof CableChannelingItem)
			return (CableChannelingItem) storableObject;
		throw new IllegalDataException("CableChannelingItemDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObjects
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	@Override
	public void insert(Set<? extends StorableObject> storableObjects)
			throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	@Override
	public void insert(StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		CableChannelingItem cableChannelingItem = fromStorableObject(storableObject);
		super.insertEntity(cableChannelingItem);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	@Override
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		CableChannelingItem cableChannelingItem = fromStorableObject(storableObject);
		super.retrieveEntity(cableChannelingItem);
	}

	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 */
	@Override
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException {
		CableChannelingItem cableChannelingItem = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + cableChannelingItem.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = CableChannelingItemWrapper.COLUMN_START_SPARE + COMMA
					+ CableChannelingItemWrapper.COLUMN_END_SPARE + COMMA
					+ CableChannelingItemWrapper.COLUMN_ROW_X + COMMA
					+ CableChannelingItemWrapper.COLUMN_PLACE_Y + COMMA
					+ CableChannelingItemWrapper.COLUMN_SEQUENTIAL_NUMBER + COMMA
					+ CableChannelingItemWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
					+ CableChannelingItemWrapper.COLUMN_START_SITE_NODE_ID + COMMA
					+ CableChannelingItemWrapper.COLUMN_END_SITE_NODE_ID + COMMA
					+ CableChannelingItemWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return CABLECHANNELINGITEM_CODE;
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

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		CableChannelingItem cableChannelingItem = fromStorableObject(storableObject);
		String sql = APOSTROPHE + cableChannelingItem.getRowX() + APOSTROPHE + COMMA
				+ APOSTROPHE + cableChannelingItem.getPlaceY() + APOSTROPHE + COMMA
				+ APOSTROPHE + cableChannelingItem.getSequentialNumber() + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getPhysicalLink().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getStartSiteNode().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getEndSiteNode().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableChannelingItem.getParentSchemeCableLink().getId());
		return sql;
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		CableChannelingItem cableChannelingItem = fromStorableObject(storableObject);
		preparedStatement.setDouble(++startParameterNumber, cableChannelingItem.getStartSpare());
		preparedStatement.setDouble(++startParameterNumber, cableChannelingItem.getEndSpare());
		preparedStatement.setInt(++startParameterNumber, cableChannelingItem.getRowX());
		preparedStatement.setInt(++startParameterNumber, cableChannelingItem.getPlaceY());
		preparedStatement.setInt(++startParameterNumber, cableChannelingItem.getSequentialNumber());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getPhysicalLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getStartSiteNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getEndSiteNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableChannelingItem.getParentSchemeCableLink().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		CableChannelingItem cableChannelingItem;
		if (storableObject == null) {
			Date created = new Date();
			cableChannelingItem = new CableChannelingItem(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, 0d, 0d, 0, 0, 0, null, null, null, null);
		} else {
			cableChannelingItem = fromStorableObject(storableObject);
		}
		cableChannelingItem.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				resultSet.getDouble(CableChannelingItemWrapper.COLUMN_START_SPARE),
				resultSet.getDouble(CableChannelingItemWrapper.COLUMN_END_SPARE),
				resultSet.getInt(CableChannelingItemWrapper.COLUMN_ROW_X),
				resultSet.getInt(CableChannelingItemWrapper.COLUMN_PLACE_Y),
				resultSet.getInt(CableChannelingItemWrapper.COLUMN_SEQUENTIAL_NUMBER),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_PHYSICAL_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_START_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_END_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, CableChannelingItemWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID));
		return cableChannelingItem;
	}
}
