/*
 * $Id: CableThreadTypeDatabase.java,v 1.37 2005/07/14 18:32:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.37 $, $Date: 2005/07/14 18:32:31 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class CableThreadTypeDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {		
		return ObjectEntities.CABLETHREAD_TYPE_CODE;
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
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ CableThreadTypeWrapper.COLUMN_COLOR + COMMA
				+ CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID + COMMA
				+ CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(cableThreadType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(cableThreadType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(cableThreadType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + cableThreadType.getColor() + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(cableThreadType.getLinkType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableThreadType.getCableLinkType().getId());
		return sql;
	}

	private CableThreadType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableThreadType)
			return (CableThreadType) storableObject;
		throw new IllegalDataException("CableThreadTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	public void retrieve(final StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		super.retrieveEntity(cableThreadType);
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		preparedStatement.setString(++startParameterNumber, cableThreadType.getCodename());
		preparedStatement.setString(++startParameterNumber, cableThreadType.getDescription());
		preparedStatement.setString(++startParameterNumber, cableThreadType.getName());
		preparedStatement.setInt(++startParameterNumber, cableThreadType.getColor());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableThreadType.getLinkType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableThreadType.getCableLinkType().getId());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		CableThreadType cableThreadType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (cableThreadType == null) {
			cableThreadType = new CableThreadType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					0,
					null,
					null);
		}
		LinkType linkType;
		CableLinkType cableLinkType;
		try {
			linkType = (LinkType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID), true);
			cableLinkType = (CableLinkType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID), true);
		} catch (final ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		cableThreadType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				resultSet.getInt(CableThreadTypeWrapper.COLUMN_COLOR),
				linkType,
				cableLinkType);

		return cableThreadType;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg) throws IllegalDataException {
		final CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  cableThreadType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		final CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		super.insertEntity(cableThreadType);		
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
	}

}
