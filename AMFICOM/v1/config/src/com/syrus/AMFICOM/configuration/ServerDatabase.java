/*
 * $Id: ServerDatabase.java,v 1.37 2005/01/11 16:18:22 arseniy Exp $
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.37 $, $Date: 2005/01/11 16:18:22 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class ServerDatabase extends StorableObjectDatabase {

	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// name VARCHAR2(64) NOT NULL,
	public static final String COLUMN_NAME  = "name";
	// type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_TYPE_ID       = "type_id";
	// hostname VARCHAR2(64) NOT NULL,
	public static final String COLUMN_HOSTNAME = "hostname";
	// user_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_USER_ID       = "user_id";

	protected static final int SIZE_HOSTNAME_COLUMN = 64;

  private static String columns;
	private static String updateMultiplySQLValues;
  
	private Server fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Server)
			return (Server) storableObject;
		throw new IllegalDataException("ServerDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {		
		return ObjectEntities.SERVER_ENTITY;
	}

	protected String getColumns(int mode) {		
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_HOSTNAME + COMMA
				+ COLUMN_USER_ID;		
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
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
		Server server = this.fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(server.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(server.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(server.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(server.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(server.getUserId());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Server server = this.fromStorableObject(storableObject);
		this.retrieveEntity(server);
		server.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(server.getId(), CharacteristicSort.CHARACTERISTIC_SORT_SERVER));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Server server = (storableObject==null)?
				new Server(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
								null,
								null,
								null,
								null,
								null,
								null) :
					this.fromStorableObject(storableObject);
		server.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
								DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),													
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_HOSTNAME)),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_USER_ID));
		return server;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
		throws IllegalDataException, UpdateObjectException {
		Server server = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, server.getDomainId());
			DatabaseString.setString(preparedStatement, ++i, server.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, server.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, server.getHostName(), SIZE_HOSTNAME_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, server.getUserId());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Server server = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case Server.RETRIEVE_MCM_IDS:
				return this.retrieveMCMIds(server);
			default:
				return null;
		}
	}

	private List retrieveMCMIds(Server server) throws RetrieveObjectException {
		List mcmIds = new ArrayList();

		String serverIdStr = DatabaseIdentifier.toSQLString(server.getId());
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.MCM_ENTITY
			+ SQL_WHERE + MCMDatabase.COLUMN_SERVER_ID + EQUALS + serverIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.retrieveServer | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				mcmIds.add(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID));
			}
		}
		catch (SQLException sqle) {
			String mesg = "ServerDatabase.retrieveServer | Cannot retrieve server " + serverIdStr;
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
		return mcmIds;
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Server server = this.fromStorableObject(storableObject);
		this.insertEntity(server);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(server);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Server server = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(server, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(server, true);		
				return;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(server);
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
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}	

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

		if (list != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_SERVER);
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Server server = (Server) iter.next();
				List characteristics = (List)characteristicMap.get(server);
				server.setCharacteristics0(characteristics);
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
			Log.debugMessage("ServerDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
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
			Log.errorMessage("ServerDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
