/*
 * $Id: TransmissionPathDatabase.java,v 1.67 2005/07/17 05:19:01 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseIdentifier;
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

public final class TransmissionPathDatabase extends StorableObjectDatabase {

	// table :: TransmissionPathMELink
	// monitored_element_id Identifier,
//	public static final String	LINK_COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	// transmission_path_id Identifier,
//	public static final String	LINK_COLUMN_TRANSMISSION_PATH_ID	= "transmission_path_id";

	private static String		columns;
	private static String		updateMultipleSQLValues;

	private TransmissionPath fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TransmissionPath)
			return (TransmissionPath) storableObject;
		throw new IllegalDataException("TransmissionPathDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.TRANSPATH_CODE;
	}

	@Override
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

	@Override
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

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		return DatabaseIdentifier.toSQLString(transmissionPath.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getType().getId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(transmissionPath.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getStartPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getFinishPortId());
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, transmissionPath.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getStartPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, transmissionPath.getFinishPortId());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final TransmissionPath transmissionPath = (storableObject == null) ? new TransmissionPath(DatabaseIdentifier
				.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null)
				: this.fromStorableObject(storableObject);
		final String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));

		TransmissionPathType type;
		try {
			type = (TransmissionPathType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID), true);
		} catch (ApplicationException ae) {
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

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  transmissionPath.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
