/*
 * $Id: EquipmentDatabase.java,v 1.92 2005/07/25 20:49:45 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.administration.DomainMember;
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
 * @version $Revision: 1.92 $, $Date: 2005/07/25 20:49:45 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class EquipmentDatabase extends StorableObjectDatabase {
	private static final int SIZE_SUPPLIER_COLUMN   = 128;
	private static final int SIZE_SUPPLIER_CODE_COLUMN = 128;
	private static final int SIZE_HW_SERIAL_COLUMN  = 64;
	private static final int SIZE_HW_VERSION_COLUMN = 64;
	private static final int SIZE_SW_SERIAL_COLUMN  = 64;
	private static final int SIZE_SW_VERSION_COLUMN = 64;
	private static final int SIZE_INVENTOY_NUMBER_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	private Equipment fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Equipment)
			return (Equipment)storableObject;
		throw new IllegalDataException("EquipmentDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.EQUIPMENT_CODE;
	}

	@Override
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
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Equipment equipment = this.fromStorableObject(storableObject);
		final String sql = DatabaseIdentifier.toSQLString(equipment.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(equipment.getType().getId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(equipment.getImageId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getSupplier(), SIZE_SUPPLIER_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN) + APOSTROPHE + COMMA
			+ equipment.getLatitude() + COMMA
			+ equipment.getLongitude() + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getHwSerial(), SIZE_HW_SERIAL_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getHwVersion(), SIZE_HW_VERSION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getSwSerial(), SIZE_SW_SERIAL_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getSwVersion(), SIZE_SW_VERSION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(equipment.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Equipment equipment = this.fromStorableObject(storableObject);
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

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		Equipment equipment = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (equipment == null) {
			equipment = new Equipment(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					StorableObjectVersion.ILLEGAL_VERSION,
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
		final String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		EquipmentType equipmentType;
		try {
			equipmentType = (EquipmentType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID),
					true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		equipment.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
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

}
