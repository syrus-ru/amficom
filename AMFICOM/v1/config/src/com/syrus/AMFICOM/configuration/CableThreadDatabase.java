/*
 * $Id: CableThreadDatabase.java,v 1.14 2005/02/18 21:30:15 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.14 $, $Date: 2005/02/18 21:30:15 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class CableThreadDatabase extends StorableObjectDatabase  {
    
	private static String columns;
	private static String updateMultiplySQLValues;

	private CableThread fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableThread)
			return (CableThread) storableObject;
		throw new IllegalDataException("LinkDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.CABLETHREAD_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode)
					+ COMMA
					+ DomainMember.COLUMN_DOMAIN_ID
					+ COMMA
					+ StorableObjectWrapper.COLUMN_TYPE_ID
					+ COMMA
					+ StorableObjectWrapper.COLUMN_NAME
					+ COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode)
					+ COMMA
					+ QUESTION
					+ COMMA
					+ QUESTION
					+ COMMA
					+ QUESTION
					+ COMMA
					+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		CableThread cableThread = this.fromStorableObject(storableObject);

		String sql = super.getUpdateSingleSQLValues(storableObject)
				+ COMMA
				+ DatabaseIdentifier.toSQLString(cableThread.getDomainId())
				+ COMMA
				+ DatabaseIdentifier.toSQLString(cableThread.getType().getId())
				+ APOSTOPHE
				+ DatabaseString.toQuerySubString(cableThread.getName(), SIZE_NAME_COLUMN)
				+ APOSTOPHE
				+ COMMA
				+ APOSTOPHE
				+ DatabaseString.toQuerySubString(cableThread.getDescription(), SIZE_DESCRIPTION_COLUMN)
				+ APOSTOPHE;
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException,
				UpdateObjectException {
		CableThread cableThread = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableThread.getDomainId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableThread.getType().getId());
			preparedStatement.setString(++i, cableThread.getName());
			preparedStatement.setString(++i, cableThread.getDescription());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("LinkDatabase." + "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
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
		String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		CableThreadType cableThreadType;
		try {
			cableThreadType = (CableThreadType) ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID),
					true);
		}
		catch (ApplicationException ae) {
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

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		CableThread cableThread = this.fromStorableObject(storableObject);
		super.retrieveEntity(cableThread);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		//        CableThread cableThread = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CableThread cableThread = this.fromStorableObject(storableObject);
		super.insertEntity(cableThread);
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws IllegalDataException,
				VersionCollisionException,
				UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
		}
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException,
				VersionCollisionException,
				UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
		}
	}

	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("CableThreadDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return objects;
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null;
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);

		return objects;
	}

}
