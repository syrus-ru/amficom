/*
 * $Id: EquipmentTypeDatabase.java,v 1.56 2005/08/30 16:35:09 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;

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
 * @version $Revision: 1.56 $, $Date: 2005/08/30 16:35:09 $
 * @author $Author: bass $
 * @module config
 */

public final class EquipmentTypeDatabase extends StorableObjectDatabase<EquipmentType> {
	private static final int SIZE_MANUFACTURER_COLUMN = 64;

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return EQUIPMENT_TYPE_CODE;
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
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ EquipmentTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ EquipmentTypeWrapper.COLUMN_MANUFACTURER_CODE;
		}
		return columns;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final EquipmentType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final EquipmentType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getManufacturer(), SIZE_MANUFACTURER_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected EquipmentType updateEntityFromResultSet(final EquipmentType storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		EquipmentType equipmentType = storableObject == null
				? new EquipmentType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		equipmentType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentTypeWrapper.COLUMN_MANUFACTURER)),
				DatabaseString.fromQuerySubString(resultSet.getString(EquipmentTypeWrapper.COLUMN_MANUFACTURER_CODE)));

		return equipmentType;
	}

}
