/*
 * $Id: KISDatabase.java,v 1.63 2005/02/24 14:59:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.63 $, $Date: 2005/02/24 14:59:53 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class KISDatabase extends StorableObjectDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	protected static final int RETRIEVE_MONITORED_ELEMENTS = 1;

	private static final int SIZE_HOSTNAME_COLUMN = 256;
	private static String columns;
	private static String updateMultiplySQLValues;

	private KIS fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof KIS)
			return (KIS) storableObject;
		throw new IllegalDataException("KISDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.KIS_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ KISWrapper.COLUMN_HOSTNAME + COMMA
				+ KISWrapper.COLUMN_TCP_PORT + COMMA
				+ KISWrapper.COLUMN_EQUIPMENT_ID + COMMA
				+ KISWrapper.COLUMN_MCM_ID;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		KIS kis = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTOPHE + COMMA
			+ kis.getTCPPort() + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getEquipmentId()) + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getMCMId());
		return sql;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		super.retrieveEntity(kis);
		this.retrieveKISMeasurementPortIds(kis);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		kis.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(kis.getId(), CharacteristicSort.CHARACTERISTIC_SORT_KIS));
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		KIS kis = this.fromStorableObject(storableObject);
		int i;
		Identifier equipmentId = kis.getEquipmentId();
		Identifier mcmId = kis.getMCMId();
		i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, kis.getDomainId());
		DatabaseString.setString(preparedStatement, ++i, kis.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, kis.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, kis.getHostName(), SIZE_HOSTNAME_COLUMN);
		preparedStatement.setInt( ++i, kis.getTCPPort());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipmentId);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcmId);
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		KIS kis = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (kis == null) {
			kis = new KIS(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
										null,
										0L,
										null,
										null,
										null,
										null,
										(short)0,
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

	private void retrieveKISMeasurementPortIds(KIS kis) throws RetrieveObjectException {
		List measurementPortIds = new ArrayList();

		String kisIdStr = DatabaseIdentifier.toSQLString(kis.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
			+ SQL_WHERE + MeasurementPortWrapper.COLUMN_KIS_ID + EQUALS + kisIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKISMeasurementPortIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				measurementPortIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));
			}
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveKISMeasurementPortIds | Cannot retrieve measurement port ids for kis " + kisIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		kis.setMeasurementPortIds(measurementPortIds);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case RETRIEVE_MONITORED_ELEMENTS:
				return this.retrieveMonitoredElements(kis);
			default:
				return null;
		}
	}

  private void retrieveKISMeasurementPortIdsByOneQuery(Collection kiss) throws RetrieveObjectException {
  	if ((kiss == null) || (kiss.isEmpty()))
			return;
  	
  	Map measurementPortIdsMap = null;
  	try {
			measurementPortIdsMap = this.retrieveLinkedEntityIds(kiss, ObjectEntities.MEASUREMENTPORT_ENTITY, MeasurementPortWrapper.COLUMN_KIS_ID, StorableObjectWrapper.COLUMN_ID);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}
		
		KIS kis;
		Identifier kisId;
		Collection measurementPortIds;
		for (Iterator it = kiss.iterator(); it.hasNext();) {
			kis = (KIS) it.next();
			kisId = kis.getId();
			measurementPortIds = (Collection) measurementPortIdsMap.get(kisId);

			kis.setMeasurementPortIds0(measurementPortIds);
		}
	}

	private List retrieveMonitoredElements(KIS kis) throws RetrieveObjectException {
		List monitoredElements = new ArrayList();

		String kisIdStr = DatabaseIdentifier.toSQLString(kis.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.ME_ENTITY
			+ SQL_WHERE + MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
				+ SQL_WHERE + MeasurementPortWrapper.COLUMN_KIS_ID + EQUALS + kisIdStr
			+ CLOSE_BRACKET;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveMonitoredElements | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				try {
					monitoredElements.add(ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), true));
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveMonitoredElements | Cannot retrieve monitored elements for kis " + kisIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
                DatabaseConnection.releaseConnection(connection);
            }
		}
		return monitoredElements;
	}

  public Map retrieveMonitoredElementsByOneQuery(List kiss) throws RetrieveObjectException {
		if ((kiss == null) || (kiss.isEmpty()))
			return null;     

		StringBuffer sql = new StringBuffer(SQL_SELECT
		+ ObjectEntities.ME_ENTITY + DOT + StorableObjectWrapper.COLUMN_ID + COMMA
		+ ObjectEntities.MEASUREMENTPORT_ENTITY + DOT + MeasurementPortWrapper.COLUMN_KIS_ID
		+ SQL_FROM + ObjectEntities.ME_ENTITY + COMMA + ObjectEntities.MEASUREMENTPORT_ENTITY
		+ SQL_WHERE + ObjectEntities.ME_ENTITY + DOT + MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID + SQL_IN
		+ OPEN_BRACKET
			+ SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
			+ SQL_WHERE + MeasurementPortWrapper.COLUMN_KIS_ID + SQL_IN + OPEN_BRACKET);

		int i = 1;
		for (Iterator it = kiss.iterator(); it.hasNext();i++) {
			KIS kis = (KIS)it.next();
			sql.append(DatabaseIdentifier.toSQLString(kis.getId()));
			if (it.hasNext()) {
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(MeasurementPortWrapper.COLUMN_KIS_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
			}
		}
		sql.append(CLOSE_BRACKET);
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKISMonitoredElementsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map meIdMap = new HashMap();
			Identifier kisId;
			List meIds;
			while (resultSet.next()) {
				kisId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_KIS_ID);
				meIds = (List) meIdMap.get(kisId);
				if (meIds == null) {
					meIds = new LinkedList();
					meIdMap.put(kisId, meIds);
				}
				meIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));
			}

			return meIdMap;
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveKISMonitoredElementsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		super.insertEntity(kis);	
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(kis);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		characteristicDatabase.updateCharacteristics(storableObject);
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("KISDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
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

		if (objects != null && !objects.isEmpty()) {
			this.retrieveKISMeasurementPortIdsByOneQuery(objects);
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects,
					CharacteristicSort.CHARACTERISTIC_SORT_KIS);
			if (characteristicMap != null)
				for (Iterator iter = objects.iterator(); iter.hasNext();) {
					KIS kis = (KIS) iter.next();
					List characteristics = (List) characteristicMap.get(kis.getId());
					kis.setCharacteristics0(characteristics);
				}
		}
        
		return objects;
	}

}
