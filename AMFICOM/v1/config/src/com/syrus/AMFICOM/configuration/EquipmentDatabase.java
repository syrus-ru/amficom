/*
 * $Id: EquipmentDatabase.java,v 1.81 2005/03/11 10:17:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.81 $, $Date: 2005/03/11 10:17:12 $
 * @author $Author: bob $
 * @module config_v1
 */

public class EquipmentDatabase extends CharacterizableDatabase {
	private static final int SIZE_SUPPLIER_COLUMN   = 128;

	private static final int SIZE_SUPPLIER_CODE_COLUMN = 128;

	private static final int SIZE_HW_SERIAL_COLUMN  = 64;

	private static final int SIZE_HW_VERSION_COLUMN = 64;

	private static final int SIZE_SW_SERIAL_COLUMN  = 64;

	private static final int SIZE_SW_VERSION_COLUMN = 64;

	private static final int SIZE_INVENTOY_NUMBER_COLUMN = 64;


	// table :: EquipmentMELink
	// equipment_id Identifier,
//	public static final String LINK_COLUMN_EQUIPMENT_ID  = "equipment_id";
	// monitored_element_id Identifier,
//	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultipleSQLValues;

	private Equipment fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Equipment)
			return (Equipment)storableObject;
		throw new IllegalDataException("EquipmentDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.EQUIPMENT_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
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
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Equipment equipment = this.fromStorableObject(storableObject);
		String sql = DatabaseIdentifier.toSQLString(equipment.getDomainId()) + COMMA
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

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		Equipment equipment = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, equipment.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, equipment.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, equipment.getImageId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getSupplier(), SIZE_SUPPLIER_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
		preparedStatement.setFloat(++startParameterNumber, equipment.getLatitude());
		preparedStatement.setFloat(++startParameterNumber, equipment.getLongitude());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getHwSerial(), SIZE_HW_SERIAL_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getHwVersion(), SIZE_HW_VERSION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getSwSerial(), SIZE_SW_SERIAL_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getSwVersion(), SIZE_SW_VERSION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, equipment.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN);
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		Equipment equipment = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (equipment == null) {
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
			equipmentType = (EquipmentType) ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID),
					true);
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  equipment.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
