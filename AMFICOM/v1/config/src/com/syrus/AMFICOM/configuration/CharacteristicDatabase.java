/*
 * $Id: CharacteristicDatabase.java,v 1.6 2004/07/28 12:54:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;

/**
 * @version $Revision: 1.6 $, $Date: 2004/07/28 12:54:18 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class CharacteristicDatabase extends StorableObjectDatabase {
	public static final String COLUMN_TYPE_ID							=	"type_id";
	public static final String COLUMN_NAME								=	"name";
	public static final String COLUMN_DESCRIPTION					=	"description";
	public static final String COLUMN_SORT								=	"sort";
	public static final String COLUMN_VALUE								=	"value";
	public static final String COLUMN_EQUIPMENT_ID				=	"equipment_id";
	public static final String COLUMN_PORT_ID							=	"port_id";
	public static final String COLUMN_CABLE_PORT_ID				=	"cable_port_id";
	public static final String COLUMN_MEASUREMENT_PORT_ID	=	"measurement_port_id";
	public static final String COLUMN_MONITORING_PORT_ID	=	"monitoring_port_id";
	public static final String COLUMN_LINK_ID							=	"link_id";
	public static final String COLUMN_CABLE_LINK_ID				=	"cable_link_id";

	private Characteristic fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characteristic)
			return (Characteristic)storableObject;
		throw new IllegalDataException("Characteristic_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		this.retrieveCharacteristic(characteristic);
	}

	private void retrieveCharacteristic(Characteristic characteristic) throws ObjectNotFoundException, RetrieveObjectException {
		String cIdStr = characteristic.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_VALUE + COMMA
			+ COLUMN_EQUIPMENT_ID + COMMA
			+ COLUMN_PORT_ID + COMMA
			+ COLUMN_CABLE_PORT_ID + COMMA
			+ COLUMN_MEASUREMENT_PORT_ID + COMMA
			+ COLUMN_MONITORING_PORT_ID + COMMA
			+ COLUMN_LINK_ID + COMMA
			+ COLUMN_CABLE_LINK_ID
			+ SQL_FROM + ObjectEntities.CHARACTERISTIC_ENTITY
			+ SQL_WHERE	+ COLUMN_ID + EQUALS + cIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Characteristic_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				int sort = resultSet.getInt(COLUMN_SORT);
				Identifier characterizedId;
				switch (sort) {
					case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
						/**
							* @todo when change DB Identifier model ,change getString() to
							*       getLong()
							*/
						characterizedId = new Identifier(resultSet.getString(COLUMN_EQUIPMENT_ID));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
						/**
							* @todo when change DB Identifier model ,change getString() to
							*       getLong()
							*/
						characterizedId = new Identifier(resultSet.getString(COLUMN_PORT_ID));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_CABLEPORT:
						/**
							* @todo when change DB Identifier model ,change getString() to
							*       getLong()
							*/
						characterizedId = new Identifier(resultSet.getString(COLUMN_CABLE_PORT_ID));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORT:
						/**
							* @todo when change DB Identifier model ,change getString() to
							*       getLong()
							*/
						characterizedId = new Identifier(resultSet.getString(COLUMN_MEASUREMENT_PORT_ID));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_MONITORINGPORT:
						/**
							* @todo when change DB Identifier model ,change getString() to
							*       getLong()
							*/
						characterizedId = new Identifier(resultSet.getString(COLUMN_MONITORING_PORT_ID));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_LINK:
						/**
							* @todo when change DB Identifier model ,change getString() to
							*       getLong()
							*/
						characterizedId = new Identifier(resultSet.getString(COLUMN_LINK_ID));
						break;
					case CharacteristicSort._CHARACTERISTIC_SORT_CABLELINK:
						/**
							* @todo when change DB Identifier model ,change getString() to
							*       getLong()
							*/
						characterizedId = new Identifier(resultSet.getString(COLUMN_CABLE_LINK_ID));
						break;
					default:
						characterizedId = null;
						Log.errorMessage("Unknown sort: " + sort + " for characteristic: " + cIdStr);
				}
				CharacteristicType characteristicType = (CharacteristicType)ConfigurationObjectTypePool.getObjectType(new Identifier(resultSet.getString(COLUMN_TYPE_ID)));
				characteristic.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
																		 characteristicType,
																		 resultSet.getString(COLUMN_NAME),
																		 resultSet.getString(COLUMN_DESCRIPTION),
																		 sort,
																		 resultSet.getString(COLUMN_VALUE),
																		 characterizedId);
			}
			else
				throw new ObjectNotFoundException("No such characteristic: " + cIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "Characteristic_Database.retrieve | Cannot retrieve characteristic " + cIdStr;
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
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		try {
			this.insertCharacteristic(characteristic);
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

	private void insertCharacteristic(Characteristic characteristic) throws CreateObjectException {
		String cIdStr = characteristic.getId().toSQLString();
		int sort = characteristic.getSort().value();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.CHARACTERISTIC_ENTITY);
			buffer.append(OPEN_BRACKET);
			buffer.append(COLUMN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATED);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIED);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_SORT);
			buffer.append(COMMA);
			buffer.append(COLUMN_NAME);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(COMMA);
			buffer.append(COLUMN_VALUE);
			buffer.append(COMMA);
			buffer.append(COLUMN_EQUIPMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_PORT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CABLE_PORT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MEASUREMENT_PORT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MONITORING_PORT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_LINK_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CABLE_LINK_ID);
			buffer.append(COMMA);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);			
			buffer.append(cIdStr);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(characteristic.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(characteristic.getModified()));
			buffer.append(COMMA);
			buffer.append(characteristic.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(characteristic.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(characteristic.getType().getId().toSQLString());
			buffer.append(COMMA);
			buffer.append(Integer.toString(sort));
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(characteristic.getName());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(characteristic.getDescription());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(characteristic.getValue());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			String characterizedIdStr = characteristic.getCharacterizedId().toString();
			switch (sort) {
				case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
					buffer.append(characterizedIdStr);
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(characterizedIdStr);
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_CABLEPORT:
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(characterizedIdStr);
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORT:
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(characterizedIdStr);
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_MONITORINGPORT:
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(characterizedIdStr);
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_LINK:
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(characterizedIdStr);
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_CABLELINK:
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(Identifier.getNullSQLString());
					buffer.append(COMMA);
					buffer.append(characterizedIdStr);
					break;
			}
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Characteristic_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Characteristic_Database.insert | Cannot insert characteristic " + cIdStr;
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
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}
