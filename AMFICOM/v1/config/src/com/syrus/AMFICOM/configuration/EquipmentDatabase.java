/*
 * $Id: EquipmentDatabase.java,v 1.99 2005/12/02 11:24:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.99 $, $Date: 2005/12/02 11:24:19 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public final class EquipmentDatabase extends StorableObjectDatabase<Equipment> {
	private static final int SIZE_SUPPLIER_COLUMN   = 128;
	private static final int SIZE_SUPPLIER_CODE_COLUMN = 128;
	private static final int SIZE_HW_SERIAL_COLUMN  = 64;
	private static final int SIZE_HW_VERSION_COLUMN = 64;
	private static final int SIZE_SW_SERIAL_COLUMN  = 64;
	private static final int SIZE_SW_VERSION_COLUMN = 64;
	private static final int SIZE_INVENTOY_NUMBER_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return EQUIPMENT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ EquipmentWrapper.COLUMN_PROTO_EQUIPMENT_ID + COMMA
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
	protected String getUpdateSingleSQLValuesTmpl(final Equipment storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getProtoEquipmentId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getImageId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getSupplier(), SIZE_SUPPLIER_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN) + APOSTROPHE + COMMA
			+ storableObject.getLatitude() + COMMA
			+ storableObject.getLongitude() + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getHwSerial(), SIZE_HW_SERIAL_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getHwVersion(), SIZE_HW_VERSION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getSwSerial(), SIZE_SW_SERIAL_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getSwVersion(), SIZE_SW_VERSION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Equipment storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getProtoEquipmentId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getImageId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getSupplier(), SIZE_SUPPLIER_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
		preparedStatement.setFloat(++startParameterNumber, storableObject.getLatitude());
		preparedStatement.setFloat(++startParameterNumber, storableObject.getLongitude());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getHwSerial(), SIZE_HW_SERIAL_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getHwVersion(), SIZE_HW_VERSION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getSwSerial(), SIZE_SW_SERIAL_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getSwVersion(), SIZE_SW_VERSION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected Equipment updateEntityFromResultSet(final Equipment storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final Equipment equipment = (storableObject == null)
				? new Equipment(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
						null)
				: storableObject;
		final String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		equipment.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, EquipmentWrapper.COLUMN_PROTO_EQUIPMENT_ID),
				(name != null) ? name : "",
				(description != null) ? description : "",
				DatabaseIdentifier.getIdentifier(resultSet, EquipmentWrapper.COLUMN_IMAGE_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SUPPLIER)),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SUPPLIER_CODE)),
				resultSet.getFloat(EquipmentWrapper.COLUMN_LATITUDE),
				resultSet.getFloat(EquipmentWrapper.COLUMN_LONGITUDE),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_HW_SERIAL)),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_HW_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SW_SERIAL)),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_SW_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentWrapper.COLUMN_INVENTORY_NUMBER)));

		return equipment;
	}

}
