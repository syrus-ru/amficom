/*
 * $Id: TransmissionPathDatabase.java,v 1.48 2005/02/19 20:34:06 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.48 $, $Date: 2005/02/19 20:34:06 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class TransmissionPathDatabase extends StorableObjectDatabase {

	// table :: TransmissionPathMELink
	// monitored_element_id Identifier,
	public static final String	LINK_COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	// transmission_path_id Identifier,
	public static final String	LINK_COLUMN_TRANSMISSION_PATH_ID	= "transmission_path_id";

	public static final int		CHARACTER_NUMBER_OF_RECORDS			= 1;

	private static String		columns;
	private static String		updateMultiplySQLValues;

	protected static final int	UPDATE_ATTACH_ME					= 1;
	protected static final int	UPDATE_DETACH_ME					= 2;

	private TransmissionPath fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TransmissionPath)
			return (TransmissionPath) storableObject;
		throw new IllegalDataException("TransmissionPathDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.TRANSPATH_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA + DomainMember.COLUMN_DOMAIN_ID + COMMA
					+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA + StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA + TransmissionPathWrapper.COLUMN_START_PORT_ID
					+ COMMA + TransmissionPathWrapper.COLUMN_FINISH_PORT_ID;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA + QUESTION + COMMA + QUESTION
					+ COMMA + QUESTION + COMMA + QUESTION + COMMA + QUESTION + COMMA + QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getType().getId()) + COMMA + APOSTOPHE
				+ DatabaseString.toQuerySubString(transmissionPath.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE
				+ DatabaseString.toQuerySubString(transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN)
				+ APOSTOPHE + COMMA + DatabaseIdentifier.toSQLString(transmissionPath.getStartPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(transmissionPath.getFinishPortId());
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
												PreparedStatement preparedStatement,
												int mode) throws IllegalDataException, SQLException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		int i;
		i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getType().getId());
		DatabaseString.setString(preparedStatement, ++i, transmissionPath.getName(), SIZE_NAME_COLUMN);
		DatabaseString
				.setString(preparedStatement, ++i, transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getStartPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getFinishPortId());
		return i;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext
				.getCharacteristicDatabase());
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.retrieveEntity(transmissionPath);
		this.retrieveTransmissionPathMEIdsByOneQuery(Collections.singletonList(transmissionPath));
		transmissionPath.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(transmissionPath.getId(),
			CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		TransmissionPath transmissionPath = (storableObject == null) ? new TransmissionPath(DatabaseIdentifier
				.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null)
				: this.fromStorableObject(storableObject);
		String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet
				.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));

		TransmissionPathType type;
		try {
			type = (TransmissionPathType) ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier
					.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
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

	private void retrieveTransmissionPathMEIdsByOneQuery(Collection transmissionPaths) throws RetrieveObjectException,
			IllegalDataException {
		if ((transmissionPaths == null) || (transmissionPaths.isEmpty()))
			return;

		Map linkedEntityIdsMap = this.retrieveLinkedEntityIds(transmissionPaths, ObjectEntities.TRANSPATHMELINK_ENTITY,
			LINK_COLUMN_TRANSMISSION_PATH_ID, LINK_COLUMN_MONITORED_ELEMENT_ID);
		TransmissionPath transmissionPath;
		List meIds;
		for (Iterator it = transmissionPaths.iterator(); it.hasNext();) {
			transmissionPath = (TransmissionPath) it.next();
			meIds = (List) linkedEntityIdsMap.get(transmissionPath.getId());

			transmissionPath.setMonitoredElementIds0(meIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.insertEntity(transmissionPath);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext
				.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(transmissionPath);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext
				.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(transmissionPath, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntity(transmissionPath, modifierId, true);
				return;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext
				.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(transmissionPath);
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				return;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext
				.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}
/*
	private void setModified(TransmissionPath transmissionPath) throws UpdateObjectException {
		String tpIdStr = DatabaseIdentifier.toSQLString(transmissionPath.getId());
		String sql = SQL_UPDATE + ObjectEntities.TRANSPATH_ENTITY + SQL_SET + StorableObjectWrapper.COLUMN_MODIFIED
				+ EQUALS + DatabaseDate.toUpdateSubString(transmissionPath.getModified()) + COMMA
				+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS
				+ DatabaseIdentifier.toSQLString(transmissionPath.getModifierId()) + SQL_WHERE
				+ StorableObjectWrapper.COLUMN_ID + EQUALS + tpIdStr;

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		} catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.setModified | Cannot set modified for transmission path " + tpIdStr;
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
*/
	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		} catch (IllegalDataException ide) {
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

		if (objects != null) {
			this.retrieveTransmissionPathMEIdsByOneQuery(objects);
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext
					.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects,
				CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH);
			if (characteristicMap != null)
				for (Iterator iter = objects.iterator(); iter.hasNext();) {
					TransmissionPath transmissionPath = (TransmissionPath) iter.next();
					List characteristics = (List) characteristicMap.get(transmissionPath.getId());
					transmissionPath.setCharacteristics0(characteristics);
				}
		}
		return objects;
	}

}
