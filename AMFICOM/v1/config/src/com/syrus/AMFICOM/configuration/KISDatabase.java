/*
 * $Id: KISDatabase.java,v 1.80 2005/07/17 05:19:00 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.80 $, $Date: 2005/07/17 05:19:00 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class KISDatabase extends StorableObjectDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static final int SIZE_HOSTNAME_COLUMN = 256;
	private static String columns;
	private static String updateMultipleSQLValues;

	private KIS fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof KIS)
			return (KIS) storableObject;
		throw new IllegalDataException("KISDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.KIS_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ KISWrapper.COLUMN_HOSTNAME + COMMA
				+ KISWrapper.COLUMN_TCP_PORT + COMMA
				+ KISWrapper.COLUMN_EQUIPMENT_ID + COMMA
				+ KISWrapper.COLUMN_MCM_ID;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final KIS kis = this.fromStorableObject(storableObject);
		final String sql = DatabaseIdentifier.toSQLString(kis.getDomainId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(kis.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(kis.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(kis.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTROPHE + COMMA
			+ kis.getTCPPort() + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getEquipmentId()) + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getMCMId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final KIS kis = this.fromStorableObject(storableObject);
		final Identifier equipmentId = kis.getEquipmentId();
		final Identifier mcmId = kis.getMCMId();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, kis.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, kis.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, kis.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, kis.getHostName(), SIZE_HOSTNAME_COLUMN);
		preparedStatement.setInt( ++startParameterNumber, kis.getTCPPort());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, equipmentId);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mcmId);
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		KIS kis = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (kis == null) {
			kis = new KIS(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null,
					(short) 0,
					null,
					null);
		}

		kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getString(KISWrapper.COLUMN_HOSTNAME),
				resultSet.getShort(KISWrapper.COLUMN_TCP_PORT),
				DatabaseIdentifier.getIdentifier(resultSet, KISWrapper.COLUMN_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, KISWrapper.COLUMN_MCM_ID));

		return kis;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException, RetrieveObjectException {
		final KIS kis = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  kis.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
