/*
 * $Id: PortDatabase.java,v 1.53 2005/03/05 21:37:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
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
 * @version $Revision: 1.53 $, $Date: 2005/03/05 21:37:24 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class PortDatabase extends CharacterizableDatabase {
	// table :: Port

	private static String columns;
	private static String updateMultipleSQLValues;

	private Port fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Port)
			return (Port)storableObject;
		throw new IllegalDataException("PortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {		
		return ObjectEntities.PORT_ENTITY;
	}

	protected String getColumns(int mode) {		
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ PortWrapper.COLUMN_EQUIPMENT_ID + COMMA
				+ PortWrapper.COLUMN_SORT;		
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;

		}
		return updateMultipleSQLValues;
	}	

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Port port = this.fromStorableObject(storableObject);
		Identifier typeId = port.getType().getId();
		Identifier equipmentId = port.getEquipmentId();
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(typeId) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(port.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE	+ COMMA
			+ DatabaseIdentifier.toSQLString(equipmentId) + COMMA 
			+ port.getSort();
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Port port = (storableObject == null) ? new Port(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
				null,
				0L,
				null,
				null,
				null,
				0) : this.fromStorableObject(storableObject);
		PortType portType;
		try {
			Identifier portTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID);
			portType = (portTypeId != null) ? (PortType) ConfigurationStorableObjectPool.getStorableObject(portTypeId, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		port.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				portType,
				(description != null) ? description : "",
				DatabaseIdentifier.getIdentifier(resultSet, PortWrapper.COLUMN_EQUIPMENT_ID),
				resultSet.getInt(PortWrapper.COLUMN_SORT));
		return port;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Port port = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  port.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
		throws IllegalDataException, SQLException {
		Port port = this.fromStorableObject(storableObject);
		Identifier typeId = port.getType().getId();
		Identifier equipmentId = port.getEquipmentId();

		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, typeId);
		DatabaseString.setString(preparedStatement, ++i, port.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipmentId);
		preparedStatement.setInt(++i, port.getSort());
		return i;
	}

}
