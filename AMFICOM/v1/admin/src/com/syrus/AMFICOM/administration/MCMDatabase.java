/*
 * $Id: MCMDatabase.java,v 1.30 2005/07/13 15:05:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.30 $, $Date: 2005/07/13 15:05:41 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public final class MCMDatabase extends CharacterizableDatabase {

	protected static final int SIZE_HOSTNAME_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	private MCM fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MCM_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
    		columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MCMWrapper.COLUMN_HOSTNAME + COMMA
				+ MCMWrapper.COLUMN_USER_ID + COMMA
				+ MCMWrapper.COLUMN_SERVER_ID;
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
		final MCM mcm = this.fromStorableObject(storableObject);
		final String sql = DatabaseIdentifier.toSQLString(mcm.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getUserId()) + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getServerId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final MCM mcm = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mcm.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mcm.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mcm.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mcm.getHostName(), SIZE_HOSTNAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mcm.getUserId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mcm.getServerId());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		MCM mcm = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (mcm == null) {
			mcm = new MCM(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null,
					null,
					null);
		}
		mcm.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(MCMWrapper.COLUMN_HOSTNAME)),
				DatabaseIdentifier.getIdentifier(resultSet, MCMWrapper.COLUMN_USER_ID),
				DatabaseIdentifier.getIdentifier(resultSet, MCMWrapper.COLUMN_SERVER_ID));

		return mcm;
	}

  @Override
public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
  	final MCM mcm = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  mcm.getId() + "'; argument: " + arg);
				return null;
		}
	}

  public Set<MCM> retrieveForServer(final Identifier serverId) throws RetrieveObjectException, IllegalDataException {
  	String serverIdStr = DatabaseIdentifier.toSQLString(serverId);
		String condition = MCMWrapper.COLUMN_SERVER_ID + EQUALS + serverIdStr;

		return this.retrieveByCondition(condition);
  }
}
