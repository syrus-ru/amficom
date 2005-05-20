/*
 * $Id: TransmissionPathDatabase.java,v 1.60 2005/05/20 21:11:34 arseniy Exp $
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
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.60 $, $Date: 2005/05/20 21:11:34 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class TransmissionPathDatabase extends CharacterizableDatabase {

	// table :: TransmissionPathMELink
	// monitored_element_id Identifier,
//	public static final String	LINK_COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	// transmission_path_id Identifier,
//	public static final String	LINK_COLUMN_TRANSMISSION_PATH_ID	= "transmission_path_id";

	private static String		columns;
	private static String		updateMultipleSQLValues;

	private TransmissionPath fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TransmissionPath)
			return (TransmissionPath) storableObject;
		throw new IllegalDataException("TransmissionPathDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.TRANSPATH_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
					+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
					+ StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ TransmissionPathWrapper.COLUMN_START_PORT_ID + COMMA
					+ TransmissionPathWrapper.COLUMN_FINISH_PORT_ID;
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
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		return DatabaseIdentifier.toSQLString(transmissionPath.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getType().getId()) + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(transmissionPath.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getStartPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getFinishPortId());
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
												PreparedStatement preparedStatement,
												int startParameterNumber) throws IllegalDataException, SQLException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, transmissionPath.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getStartPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getFinishPortId());
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		TransmissionPath transmissionPath = (storableObject == null) ? new TransmissionPath(DatabaseIdentifier
				.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null)
				: this.fromStorableObject(storableObject);
		String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));

		TransmissionPathType type;
		try {
			type = (TransmissionPathType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		transmissionPath.setAttributes(
			DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
			DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
			DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
			DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
			resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
			DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
			(name != null) ? name : "",
			(description != null) ? description : "",
			type,
			DatabaseIdentifier.getIdentifier(resultSet,	TransmissionPathWrapper.COLUMN_START_PORT_ID),
			DatabaseIdentifier.getIdentifier(resultSet,	TransmissionPathWrapper.COLUMN_FINISH_PORT_ID));
		return transmissionPath;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  transmissionPath.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
