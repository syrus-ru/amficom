/*
 * $Id: MCMDatabase.java,v 1.17 2005/02/24 10:26:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
 * @version $Revision: 1.17 $, $Date: 2005/02/24 10:26:07 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public class MCMDatabase extends StorableObjectDatabase {

	protected static final int SIZE_HOSTNAME_COLUMN = 64;

	private static String columns;
	private static String updateMultiplySQLValues;

	private MCM fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MCM_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
    		columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MCMWrapper.COLUMN_HOSTNAME + COMMA
				+ MCMWrapper.COLUMN_USER_ID + COMMA
				+ MCMWrapper.COLUMN_SERVER_ID;
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

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		MCM mcm = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getUserId()) + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getServerId()); 
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		MCM mcm = this.fromStorableObject(storableObject);
		int i;
		i  = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getDomainId());
		DatabaseString.setString(preparedStatement, ++i, mcm.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, mcm.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, mcm.getHostName(), SIZE_HOSTNAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getUserId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getServerId());
		return i;
	}	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		MCM mcm = this.fromStorableObject(storableObject);
		this.retrieveEntity(mcm);
		this.retrieveKISIds(mcm);
		mcm.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(mcm.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MCM mcm = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (mcm == null){
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
						  DatabaseIdentifier.getIdentifier(resultSet, MCMWrapper.COLUMN_SERVER_ID)
						  );
		
		return mcm;
	}
		
	private void retrieveKISIds(MCM mcm) throws RetrieveObjectException {
		List kisIds = new ArrayList();
		String mcmIdStr = DatabaseIdentifier.toSQLString(mcm.getId());
		String sql = SQL_SELECT 
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.KIS_ENTITY
			+ SQL_WHERE + MCMWrapper.LINK_COLUMN_MCM_ID + EQUALS + mcmIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
    try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieveKISIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				kisIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieveKISIds | Cannot retrieve kis ids for mcm " + mcmIdStr;
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
		mcm.setKISIds0(kisIds);
	}

  private void retrieveKISIdsByOneQuery(Collection mcms) throws RetrieveObjectException {

    if ((mcms == null) || (mcms.isEmpty()))
			return;     

    Map kisIdsMap = null;
    try {
			kisIdsMap = this.retrieveLinkedEntityIds(mcms, ObjectEntities.KIS_ENTITY, MCMWrapper.LINK_COLUMN_MCM_ID, StorableObjectWrapper.COLUMN_ID);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		MCM mcm;
		Identifier mcmId;
		Collection kisIds;
		for (Iterator it = mcms.iterator(); it.hasNext();) {
			mcm = (MCM) it.next();
			mcmId = mcm.getId();
			kisIds = (Collection) kisIdsMap.get(mcmId);

			mcm.setKISIds0(kisIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		MCM mcm = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		super.insertEntity(mcm);	
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(mcm);
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
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(mcm, modifierId, true);
				break;
			case UPDATE_CHECK: 					
			default:
				super.checkAndUpdateEntity(mcm, modifierId, false);
				break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(mcm);
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, modifierId, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, modifierId, false);
			break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null;
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else 
			objects = this.retrieveByIdsOneQuery(ids, condition);

    if (objects != null) {
			this.retrieveKISIdsByOneQuery(objects);

      CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects,
					CharacteristicSort.CHARACTERISTIC_SORT_MCM);
			if (characteristicMap != null)
				for (Iterator iter = objects.iterator(); iter.hasNext();) {
					MCM mcm = (MCM) iter.next();
					List characteristics = (List) characteristicMap.get(mcm.getId());
					mcm.setCharacteristics0(characteristics);
				}
		}
		return objects;	
		//return retriveByIdsPreparedStatement(ids);
	}

}
