/*
 * $Id: TransmissionPathDatabase.java,v 1.39 2005/01/28 10:23:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.39 $, $Date: 2005/01/28 10:23:01 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class TransmissionPathDatabase extends StorableObjectDatabase {
	// table :: TransmissionPathMELink
	// monitored_element_id Identifier,
	public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";
	// transmission_path_id Identifier,
	public static final String LINK_COLUMN_TRANSMISSION_PATH_ID  = "transmission_path_id";    

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;
	
	protected static final int		UPDATE_ATTACH_ME	= 1;
	protected static final int		UPDATE_DETACH_ME	= 2;

	private TransmissionPath fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TransmissionPath)
			return (TransmissionPath)storableObject;
		throw new IllegalDataException("TransmissionPathDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	

	protected String getEnityName() {		
		return ObjectEntities.TRANSPATH_ENTITY;
	}	

	protected String getColumns(int mode) {		
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ TransmissionPathWrapper.COLUMN_TYPE_ID + COMMA
				+ TransmissionPathWrapper.COLUMN_NAME + COMMA
				+ TransmissionPathWrapper.COLUMN_DESCRIPTION + COMMA
				+ TransmissionPathWrapper.COLUMN_START_PORT_ID + COMMA
				+ TransmissionPathWrapper.COLUMN_FINISH_PORT_ID;		
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
				+ QUESTION;		
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(transmissionPath.getDomainId()) + COMMA
            + DatabaseIdentifier.toSQLString(transmissionPath.getType().getId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(transmissionPath.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(transmissionPath.getStartPortId()) + COMMA
			+ DatabaseIdentifier.toSQLString(transmissionPath.getFinishPortId());
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
					PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getDomainId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getType().getId());
			DatabaseString.setString(preparedStatement, ++i, transmissionPath.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, transmissionPath.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getStartPortId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, transmissionPath.getFinishPortId());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("KISDatabase." +
							"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.retrieveEntity(transmissionPath);
		this.retrieveTransmissionPathMELink(transmissionPath);
		transmissionPath.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(transmissionPath.getId(), CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		TransmissionPath transmissionPath = (storableObject == null) ?
				new TransmissionPath(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
								null,
								null,
								null,
								null,
								null,
								null,
								null) :
				this.fromStorableObject(storableObject);
		String name = DatabaseString.fromQuerySubString(resultSet.getString(TransmissionPathWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(TransmissionPathWrapper.COLUMN_DESCRIPTION));

		TransmissionPathType type;
		try {
			type = (TransmissionPathType)ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, TransmissionPathWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		transmissionPath.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
												DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
												DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
												DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
												DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
												(name != null) ? name : "",
												(description != null) ? description : "",
												type,
												DatabaseIdentifier.getIdentifier(resultSet, TransmissionPathWrapper.COLUMN_START_PORT_ID),
												DatabaseIdentifier.getIdentifier(resultSet, TransmissionPathWrapper.COLUMN_FINISH_PORT_ID));
		return transmissionPath;
	}

	private void retrieveTransmissionPathMELink(TransmissionPath transmissionPath) throws RetrieveObjectException{
		String tpIdStr = DatabaseIdentifier.toSQLString(transmissionPath.getId());
		String sql = SQL_SELECT
			+ LINK_COLUMN_MONITORED_ELEMENT_ID
			+ SQL_FROM + ObjectEntities.TRANSPATHMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_TRANSMISSION_PATH_ID + EQUALS + tpIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.retrieveEquipmentMELink | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			List meLink = new ArrayList();
			while (resultSet.next()) {				
				Identifier meId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MONITORED_ELEMENT_ID);
				meLink.add(meId);				
			}
			transmissionPath.setMonitoredElementIds0(meLink);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.retrieveEquipmentMELink | Cannot retrieve transmission path " + tpIdStr;
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

	private void retrieveTransmissionPathMELinkByOneQuery(List transmissionPaths) throws RetrieveObjectException {
		if ((transmissionPaths == null) || (transmissionPaths.isEmpty()))
			return;     

		StringBuffer sql = new StringBuffer(SQL_SELECT
			+ LINK_COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ LINK_COLUMN_TRANSMISSION_PATH_ID
			+ SQL_FROM + ObjectEntities.TRANSPATHMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_TRANSMISSION_PATH_ID
			+ SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = transmissionPaths.iterator(); it.hasNext();i++) {
			TransmissionPath transmissionPath = (TransmissionPath)it.next();
			sql.append(DatabaseIdentifier.toSQLString(transmissionPath.getId()));
			if (it.hasNext()) {
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(LINK_COLUMN_TRANSMISSION_PATH_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}                   
			}
		}
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.retrieveTransmissionPathMELinkByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			Map meIdMap = new HashMap();
			while (resultSet.next()) {
				TransmissionPath transmissionPath = null;
				Identifier transmissionPathId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_TRANSMISSION_PATH_ID);
				for (Iterator it = transmissionPaths.iterator(); it.hasNext();) {
					TransmissionPath transmissionPathToCompare = (TransmissionPath) it.next();
					if (transmissionPathToCompare.getId().equals(transmissionPathId)){
						transmissionPath = transmissionPathToCompare;
						break;
					}                   
				}

				if (transmissionPath == null) {
					String mesg = "TransmissionPathDatabase.retrieveTransmissionPathMELinkByOneQuery | Cannot found correspond result for '" + transmissionPathId.getIdentifierString() +"'" ;
					throw new RetrieveObjectException(mesg);
				}

				Identifier meId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MONITORED_ELEMENT_ID);
				List meIds = (List)meIdMap.get(transmissionPath);
				if (meIds == null) {
					meIds = new LinkedList();
					meIdMap.put(transmissionPath, meIds);
				}               
				meIds.add(meId);              
			}

			for (Iterator iter = transmissionPaths.iterator(); iter.hasNext();) {
				TransmissionPath transmissionPath = (TransmissionPath) iter.next();
				List meIds = (List)meIdMap.get(transmissionPath);
				transmissionPath.setMonitoredElementIds0(meIds);
			}

		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.retrieveTransmissionPathMELinkByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException{
//		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.insertEntity(transmissionPath);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(transmissionPath);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(transmissionPath, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(transmissionPath, true);		
				return;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(transmissionPath);
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {	
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	private void setModified(TransmissionPath transmissionPath) throws UpdateObjectException {		
		String tpIdStr = DatabaseIdentifier.toSQLString(transmissionPath.getId());
		String sql = SQL_UPDATE
			+ ObjectEntities.TRANSPATH_ENTITY
			+ SQL_SET
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(transmissionPath.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(transmissionPath.getModifierId())
			+ SQL_WHERE + COLUMN_ID + EQUALS + tpIdStr;

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.setModified | Cannot set modified for transmission path " + tpIdStr;
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {			
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

    if (list != null) {
			retrieveTransmissionPathMELinkByOneQuery(list);
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list,
					CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH);
			if (characteristicMap != null)
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					TransmissionPath transmissionPath = (TransmissionPath) iter.next();
					List characteristics = (List) characteristicMap.get(transmissionPath.getId());
					transmissionPath.setCharacteristics0(characteristics);
				}
		}
		return list;
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId());

		try {
			list = retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("TransmissionPathDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else {
			Log.errorMessage("TransmissionPathDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
