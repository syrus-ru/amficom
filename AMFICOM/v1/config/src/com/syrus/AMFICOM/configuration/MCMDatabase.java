/*
 * $Id: MCMDatabase.java,v 1.6 2004/07/27 16:03:30 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.configuration.corba.EquipmentSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $ $, $ $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class MCMDatabase extends StorableObjectDatabase {

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_SERVER_ID = "server_id";
	public static final String COLUMN_LOCATION = "location";
	public static final String COLUMN_HOSTNAME = "hostname";
	
	private MCM fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		this.retrieveMCM(mcm);
		this.retrieveMCMKis(mcm);
	}

	private void retrieveMCM(MCM mcm) throws ObjectNotFoundException, RetrieveObjectException {
		String mIdStr = mcm.getId().toSQLString();
		String sql = SQL_SELECT
				+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
				+ COLUMN_CREATOR_ID + COMMA
				+ COLUMN_MODIFIER_ID + COMMA 
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA 
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_USER_ID + COMMA
				+ COLUMN_SERVER_ID + COMMA
				+ COLUMN_LOCATION + COMMA
				+ COLUMN_HOSTNAME
				+ SQL_FROM + ObjectEntities.MCM_ENTITY
				+ SQL_WHERE + COLUMN_ID + EQUALS + mIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				mcm.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
													DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
													/**
													 * @todo when change DB Identifier model ,change getString() to
													 *       getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
													/**
													 * @todo when change DB Identifier model ,change getString() to
													 *       getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
													/**
													 * @todo when change DB Identifier model ,change getString() to
													 *       getLong()
													 */
													 new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),
													 resultSet.getString(COLUMN_NAME),
													 resultSet.getString(COLUMN_DESCRIPTION),
													 /**
													 * @todo when change DB Identifier model ,change getString() to
													 *       getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_USER_ID)),
													 /**
													 * @todo when change DB Identifier model ,change getString() to
													 *       getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_SERVER_ID)),
													 resultSet.getString(COLUMN_LOCATION),
													 resultSet.getString(COLUMN_HOSTNAME));
			}
			else
				throw new ObjectNotFoundException("No such mcm: " + mIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieve | Cannot retrieve mcm " + mIdStr;
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
			}
		}
	}
	
	private void retrieveMCMKis(MCM mcm) throws ObjectNotFoundException, RetrieveObjectException {
		String mIdStr = mcm.getId().toSQLString();
		String sql = SQL_SELECT 
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.EQUIPMENT_ENTITY
				+ SQL_WHERE + EquipmentDatabase.COLUMN_SORT + EQUALS + Integer.toString(EquipmentSort._EQUIPMENT_SORT_KIS)
				+ SQL_AND + KISDatabase.COLUMN_MCM_ID + EQUALS + mIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			List characteristicIds = new ArrayList();
			if (resultSet.next()) {				
				characteristicIds.add(resultSet.getString(COLUMN_ID));
			}
			else
				throw new ObjectNotFoundException("No such mcm: " + mIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieve | Cannot retrieve mcm " + mIdStr;
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
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		try {
			this.insertMCM(mcm);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertMCM(MCM mcm) throws CreateObjectException {
		String cIdStr = mcm.getId().toSQLString();		
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.CHARACTERISTIC_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_USER_ID + COMMA
			+ COLUMN_SERVER_ID + COMMA
			+ COLUMN_LOCATION + COMMA
			+ COLUMN_HOSTNAME
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ cIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(mcm.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(mcm.getModified()) + COMMA
			+ mcm.getCreatorId().toSQLString() + COMMA
			+ mcm.getModifierId().toSQLString() + COMMA
			+ mcm.getDomainId().toSQLString() + COMMA
			+ APOSTOPHE + mcm.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + mcm.getDescription() + APOSTOPHE + COMMA
			+ mcm.getUserId().toSQLString() + COMMA
			+ mcm.getServerId().toSQLString() + COMMA
			+ APOSTOPHE + mcm.getLocation() + APOSTOPHE + COMMA
			+ APOSTOPHE + mcm.getHostName() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.insert | Cannot insert mcm " + cIdStr;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws IllegalDataException, UpdateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}
