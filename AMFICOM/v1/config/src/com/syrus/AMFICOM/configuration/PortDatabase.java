/*-
 * $Id: PortDatabase.java,v 1.67 2005/07/17 05:19:01 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.67 $, $Date: 2005/07/17 05:19:01 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public final class PortDatabase extends StorableObjectDatabase {
	// table :: Port

	private static String columns;
	private static String updateMultipleSQLValues;

	private Port fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Port)
			return (Port)storableObject;
		throw new IllegalDataException("PortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.PORT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {		
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ PortWrapper.COLUMN_EQUIPMENT_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;

		}
		return updateMultipleSQLValues;
	}	

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Port port = this.fromStorableObject(storableObject);
		final Identifier typeId = port.getType().getId();
		final Identifier equipmentId = port.getEquipmentId();
		return DatabaseIdentifier.toSQLString(typeId) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(port.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE	+ COMMA
			+ DatabaseIdentifier.toSQLString(equipmentId);
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Port port = (storableObject == null) ? new Port(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
				null,
				0L,
				null,
				null,
				null) : this.fromStorableObject(storableObject);
		PortType portType;
		try {
			final Identifier portTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID);
			portType = (portTypeId != null) ? (PortType) StorableObjectPool.getStorableObject(portTypeId, true) : null;
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		port.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				portType,
				(description != null) ? description : "",
				DatabaseIdentifier.getIdentifier(resultSet, PortWrapper.COLUMN_EQUIPMENT_ID));
		return port;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Port port = this.fromStorableObject(storableObject);
		final Identifier typeId = port.getType().getId();
		final Identifier equipmentId = port.getEquipmentId();

		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, typeId);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, port.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, equipmentId);
		return startParameterNumber;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg) throws IllegalDataException {
		final Port port = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  port.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
