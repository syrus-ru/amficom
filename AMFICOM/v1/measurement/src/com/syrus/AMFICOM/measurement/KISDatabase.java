/*
 * $Id: KISDatabase.java,v 1.4.2.2 2006/03/07 10:42:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_EQUIPMENT_ID;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_HOSTNAME;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_MCM_ID;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_ON_SERVICE;
import static com.syrus.AMFICOM.measurement.KISWrapper.COLUMN_TCP_PORT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.4.2.2 $, $Date: 2006/03/07 10:42:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class KISDatabase extends StorableObjectDatabase<KIS> {
	private static final int SIZE_HOSTNAME_COLUMN = 256;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return KIS_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_HOSTNAME + COMMA
				+ COLUMN_TCP_PORT + COMMA
				+ COLUMN_EQUIPMENT_ID + COMMA
				+ COLUMN_MCM_ID + COMMA
				+ COLUMN_ON_SERVICE;
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
			+ DatabaseIdentifier.toSQLString(storableObject.getMCMId()) + COMMA
			+ (storableObject.isOnService() ? "1" : "0");
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
		preparedStatement.setInt(++startParameterNumber, storableObject.isOnService() ? 1 : 0);
		return startParameterNumber;
	}

	@Override
	protected KIS updateEntityFromResultSet(final KIS storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final KIS kis = (storableObject == null)
				? new KIS(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						(short) 0,
						null,
						null,
						false)
				: storableObject;
		kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				resultSet.getString(COLUMN_HOSTNAME),
				resultSet.getShort(COLUMN_TCP_PORT),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MCM_ID),
				(resultSet.getInt(COLUMN_ON_SERVICE) == 0) ? false : true);
		return kis;
	}

}
