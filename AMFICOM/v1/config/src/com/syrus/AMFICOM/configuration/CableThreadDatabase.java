/*
 * $Id: CableThreadDatabase.java,v 1.32 2005/07/17 05:19:00 arseniy Exp $
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
 * @version $Revision: 1.32 $, $Date: 2005/07/17 05:19:00 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public final class CableThreadDatabase extends StorableObjectDatabase  {

	private static String columns;
	private static String updateMultipleSQLValues;

	private CableThread fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableThread)
			return (CableThread) storableObject;
		throw new IllegalDataException("CableThreadDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.CABLETHREAD_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID	+ COMMA
					+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
					+ StorableObjectWrapper.COLUMN_NAME	+ COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final CableThread cableThread = this.fromStorableObject(storableObject);

		final String sql = DatabaseIdentifier.toSQLString(cableThread.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableThread.getType().getId()) + COMMA
				+ APOSTROPHE	+ DatabaseString.toQuerySubString(cableThread.getName(), SIZE_NAME_COLUMN)	+ APOSTROPHE	+ COMMA
				+ APOSTROPHE	+ DatabaseString.toQuerySubString(cableThread.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final CableThread cableThread = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableThread.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableThread.getType().getId());
		preparedStatement.setString(++startParameterNumber, cableThread.getName());
		preparedStatement.setString(++startParameterNumber, cableThread.getDescription());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		CableThread cableThread = this.fromStorableObject(storableObject);
		if (cableThread == null) {
			cableThread = new CableThread(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null);
		}
		final String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		CableThreadType cableThreadType;
		try {
			cableThreadType = (CableThreadType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID),
					true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		cableThread.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				(name != null) ? name : "",
				(description != null) ? description : "",
				cableThreadType);

		return cableThread;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final CableThread cableThread = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  cableThread.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
