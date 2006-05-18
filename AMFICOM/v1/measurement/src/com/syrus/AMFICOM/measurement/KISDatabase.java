/*
 * $Id: KISDatabase.java,v 1.4 2005/12/02 11:24:09 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.4 $, $Date: 2005/12/02 11:24:09 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class KISDatabase extends StorableObjectDatabase<KIS> {
	private static final int SIZE_HOSTNAME_COLUMN = 256;

	private static String columns;
	private static String updateMultipleSQLValues;

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
	protected String getUpdateSingleSQLValuesTmpl(final KIS storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getDomainId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTROPHE + COMMA
			+ storableObject.getTCPPort() + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getEquipmentId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getMCMId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final KIS storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Identifier equipmentId = storableObject.getEquipmentId();
		final Identifier mcmId = storableObject.getMCMId();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getHostName(), SIZE_HOSTNAME_COLUMN);
		preparedStatement.setInt( ++startParameterNumber, storableObject.getTCPPort());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, equipmentId);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mcmId);
		return startParameterNumber;
	}

	@Override
	protected KIS updateEntityFromResultSet(final KIS storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		KIS kis = storableObject == null
				? new KIS(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						(short) 0,
						null,
						null)
				: storableObject;

		kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getString(KISWrapper.COLUMN_HOSTNAME),
				resultSet.getShort(KISWrapper.COLUMN_TCP_PORT),
				DatabaseIdentifier.getIdentifier(resultSet, KISWrapper.COLUMN_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, KISWrapper.COLUMN_MCM_ID));

		return kis;
	}

}
