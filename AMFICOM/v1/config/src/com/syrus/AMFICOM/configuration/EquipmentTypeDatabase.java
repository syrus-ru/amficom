/*
 * $Id: EquipmentTypeDatabase.java,v 1.1 2004/07/28 12:49:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1 $, $Date: 2004/07/28 12:49:46 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class EquipmentTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private EquipmentType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EquipmentType)
			return (EquipmentType)storableObject;
		throw new IllegalDataException("EquipmentTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		this.retrieveEquipmentType(equipmentType);
	}

	private void retrieveEquipmentType(EquipmentType equipmentType) throws ObjectNotFoundException, RetrieveObjectException {
		String etIdStr = equipmentType.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.EQUIPMENTTYPE_ENTITY
			+ SQL_WHERE	+ COLUMN_ID + EQUALS + etIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentTypeDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				equipmentType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																		DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																		/**
																			* @todo when change DB Identifier model ,change getString() to getLong()
																			*/
																		new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																		/**
																			* @todo when change DB Identifier model ,change getString() to getLong()
																			*/
																		new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																		resultSet.getString(COLUMN_CODENAME),
																		resultSet.getString(COLUMN_DESCRIPTION));
			else
				throw new ObjectNotFoundException("No such equipment type: " + etIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentTypeDatabase.retrieve | Cannot retrieve Equipment type " + etIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		try {
			this.insertEquipmentType(equipmentType);
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

	private void insertEquipmentType(EquipmentType equipmentType) throws CreateObjectException {
		String etIdStr = equipmentType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EQUIPMENTTYPE_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ etIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(equipmentType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(equipmentType.getModified()) + COMMA
			+ equipmentType.getCreatorId().toSQLString() + COMMA
			+ equipmentType.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + equipmentType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + equipmentType.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentTypeDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentTypeDatabase.insert | Cannot insert equipment type " + etIdStr;
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
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}

	public static List retrieveAll() throws RetrieveObjectException {
		List equipmentTypes = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.EQUIPMENTTYPE_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentTypeDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				equipmentTypes.add(new EquipmentType(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentTypeDatabase.retrieveAll | Cannot retrieve equipment type";
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
		return equipmentTypes;
	}
}
