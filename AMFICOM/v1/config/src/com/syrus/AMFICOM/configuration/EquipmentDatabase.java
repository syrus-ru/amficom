/*
 * $Id: EquipmentDatabase.java,v 1.72 2005/02/24 09:27:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.72 $, $Date: 2005/02/24 09:27:50 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class EquipmentDatabase extends StorableObjectDatabase {
	private static final int SIZE_SUPPLIER_COLUMN   = 128;

	private static final int SIZE_SUPPLIER_CODE_COLUMN = 128;

	private static final int SIZE_HW_SERIAL_COLUMN  = 64;

	private static final int SIZE_HW_VERSION_COLUMN = 64;

	private static final int SIZE_SW_SERIAL_COLUMN  = 64;

	private static final int SIZE_SW_VERSION_COLUMN = 64;

	private static final int SIZE_INVENTOY_NUMBER_COLUMN = 64;


	// table :: EquipmentMELink
	// equipment_id Identifier,
	public static final String LINK_COLUMN_EQUIPMENT_ID  = "equipment_id";
	// monitored_element_id Identifier,
	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;

	private Equipment fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Equipment)
			return (Equipment)storableObject;
		throw new IllegalDataException("EquipmentDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.EQUIPMENT_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ EquipmentWrapper.COLUMN_IMAGE_ID + COMMA
				+ EquipmentWrapper.COLUMN_SUPPLIER + COMMA
				+ EquipmentWrapper.COLUMN_SUPPLIER_CODE + COMMA
				+ EquipmentWrapper.COLUMN_LATITUDE + COMMA
				+ EquipmentWrapper.COLUMN_LONGITUDE + COMMA
				+ EquipmentWrapper.COLUMN_HW_SERIAL + COMMA
				+ EquipmentWrapper.COLUMN_HW_VERSION + COMMA
				+ EquipmentWrapper.COLUMN_SW_SERIAL + COMMA
				+ EquipmentWrapper.COLUMN_SW_VERSION + COMMA
				+ EquipmentWrapper.COLUMN_INVENTORY_NUMBER;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA 
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Equipment equipment = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(equipment.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(equipment.getType().getId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(equipment.getImageId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSupplier(), SIZE_SUPPLIER_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + equipment.getLatitude() + APOSTOPHE + COMMA
			+ APOSTOPHE + equipment.getLongitude() + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getHwSerial(), SIZE_HW_SERIAL_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getHwVersion(), SIZE_HW_VERSION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSwSerial(), SIZE_SW_SERIAL_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSwVersion(), SIZE_SW_VERSION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		Equipment equipment = this.fromStorableObject(storableObject);
		int i;
		i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipment.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipment.getType().getId());
		DatabaseString.setString(preparedStatement, ++i, equipment.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, equipment.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipment.getImageId());
		DatabaseString.setString(preparedStatement, ++i, equipment.getSupplier(), SIZE_SUPPLIER_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, equipment.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
		preparedStatement.setFloat(++i, equipment.getLatitude());
		preparedStatement.setFloat(++i, equipment.getLongitude());
		DatabaseString.setString(preparedStatement, ++i, equipment.getHwSerial(), SIZE_HW_SERIAL_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, equipment.getHwVersion(), SIZE_HW_VERSION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, equipment.getSwSerial(), SIZE_SW_SERIAL_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, equipment.getSwVersion(), SIZE_SW_VERSION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, equipment.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN);
		return i;
	}

	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Equipment equipment = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (equipment == null){
			equipment = new Equipment(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
													null,
													0L,
													null,
													null,
													null,
													null,
													null,
													"",
													null,
													0,
													0,
													null,
													null,
													null,
													null,
													null);			
		}
		String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		EquipmentType equipmentType;
		try {
			equipmentType = (EquipmentType) ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		equipment.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
									equipmentType,
									(name != null) ? name : "",
									(description != null) ? description : "",
									DatabaseIdentifier.getIdentifier(resultSet, EquipmentWrapper.COLUMN_IMAGE_ID),
									DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SUPPLIER)),
									DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SUPPLIER_CODE)),
									resultSet.getFloat(EquipmentWrapper.COLUMN_LONGITUDE),
									resultSet.getFloat(EquipmentWrapper.COLUMN_LATITUDE),
									DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_HW_VERSION)),
									DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_HW_SERIAL)),
									DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SW_VERSION)),
									DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SW_SERIAL)),
									DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_INVENTORY_NUMBER)));

		return equipment;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		Equipment equipment = this.fromStorableObject(storableObject);
		super.retrieveEntity(equipment);
		this.retrieveEquipmentPortIds(equipment);
		this.retrieveEquipmentMEIdsByOneQuery(Collections.singletonList(equipment));
		equipment.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(equipment.getId(), CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT));
	}

	private void retrieveEquipmentPortIds(Equipment equipment) throws RetrieveObjectException {
		List portIds = new ArrayList();
		String eqIdStr = DatabaseIdentifier.toSQLString(equipment.getId());

		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.PORT_ENTITY
			+ SQL_WHERE + PortWrapper.COLUMN_EQUIPMENT_ID + EQUALS + eqIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.retrieveEquipmentPortIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {				
				portIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));				
			}
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.retrieveEquipmentPortIds | Cannot retrieve port ids for equipment " + eqIdStr;
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		equipment.setPortIds0(portIds);
	}

	private void retrieveEquipmentPortIdsByOneQuery(Collection equipments) throws RetrieveObjectException {
		if ((equipments == null) || (equipments.isEmpty()))
			return;

		Map portIdsMap = null;
		try {
			portIdsMap = this.retrieveLinkedEntityIds(equipments, ObjectEntities.PORT_ENTITY, PortWrapper.COLUMN_EQUIPMENT_ID, StorableObjectWrapper.COLUMN_ID);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Equipment equipment;
		Identifier equipmentId;
		Collection portIds;
		for (Iterator it = equipments.iterator(); it.hasNext();) {
			equipment = (Equipment) it.next();
			equipmentId = equipment.getId();
			portIds = (Collection) portIdsMap.get(equipmentId);

			equipment.setPortIds0(portIds);
		}
	}

	private void retrieveEquipmentMEIdsByOneQuery(Collection equipments) throws RetrieveObjectException, IllegalDataException {
		if ((equipments == null) || (equipments.isEmpty()))
			return;

		Map linkedEntityIdsMap = this.retrieveLinkedEntityIds(equipments,
				ObjectEntities.EQUIPMENTMELINK_ENTITY,
				LINK_COLUMN_EQUIPMENT_ID,
				LINK_COLUMN_MONITORED_ELEMENT_ID);
		Equipment equipment;
		List meIds;
		for (Iterator it = equipments.iterator(); it.hasNext();) {
			equipment = (Equipment) it.next();
			meIds = (List) linkedEntityIdsMap.get(equipment.getId());

			equipment.setMonitoredElementIds0(meIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Equipment equipment = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		super.insertEntity(equipment);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			this.updateEquipmentMELinks(Collections.singletonList(equipment));
			characteristicDatabase.updateCharacteristics(equipment);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}		
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());		
		try {
			this.updateEquipmentMELinks(storableObjects);
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	private void updateEquipmentMELinks(Collection equipments) throws IllegalDataException, UpdateObjectException {
		if (equipments == null || equipments.isEmpty())
			return;
		Map monitoredElementIdsMap = new HashMap();
		for (Iterator it = equipments.iterator(); it.hasNext();) {
			Equipment equipment = this.fromStorableObject((StorableObject)it.next());
			List monitoredElementIds = equipment.getMonitoredElementIds();
			monitoredElementIdsMap.put(equipment.getId(), monitoredElementIds);
		}		
		super.updateLinkedEntities(monitoredElementIdsMap, ObjectEntities.EQUIPMENTMELINK_ENTITY, LINK_COLUMN_EQUIPMENT_ID, LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(equipment, modifierId, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(equipment, modifierId, false);
			break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(equipment);
		this.updateEquipmentMELinks(Collections.singletonList(equipment));
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
		throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, modifierId, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, modifierId, false);
			break;
		}		
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		this.updateEquipmentMELinks(storableObjects);
		characteristicDatabase.updateCharacteristics(storableObjects);
	}
/*
	private void setModified(Equipment equipment) throws UpdateObjectException {
		String eqIdStr = DatabaseIdentifier.toSQLString(equipment.getId());
		String sql = SQL_UPDATE
			+ ObjectEntities.EQUIPMENT_ENTITY + SQL_SET
			+ StorableObjectWrapper.COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(equipment.getModified()) + COMMA
			+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(equipment.getModifierId())
			+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + eqIdStr;

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.setModified | Cannot set modified for equipment " + eqIdStr;
			throw new UpdateObjectException(mesg, sqle);
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
*/
	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("EquipmentDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return objects;
	}

	public Collection retrieveByIds(Collection ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		Collection objects = null;
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);

    if (objects != null) {
			this.retrieveEquipmentPortIdsByOneQuery(objects);
			this.retrieveEquipmentMEIdsByOneQuery(objects); 

			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects,
					CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT);
			if (characteristicMap != null)
				for (Iterator iter = objects.iterator(); iter.hasNext();) {
					Equipment equipment = (Equipment) iter.next();
					List characteristics = (List) characteristicMap.get(equipment.getId());
					equipment.setCharacteristics0(characteristics);
				}
		}
		return objects;
	}
}
