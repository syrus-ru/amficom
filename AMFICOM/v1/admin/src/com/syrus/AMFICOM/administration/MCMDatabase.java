/*
 * $Id: MCMDatabase.java,v 1.6 2005/02/08 12:23:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import java.util.List;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/08 12:23:41 $
 * @author $Author: bob $
 * @module administration_v1
 */

public class MCMDatabase extends StorableObjectDatabase {

	public static final String LINK_COLUMN_MCM_ID = "mcm_id";

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

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
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
			throws IllegalDataException, UpdateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		int i;
		try {
			i  = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getDomainId());
			DatabaseString.setString(preparedStatement, ++i, mcm.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, mcm.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, mcm.getHostName(), SIZE_HOSTNAME_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getUserId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getServerId());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("MCMDatabase.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
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
			+ SQL_WHERE + LINK_COLUMN_MCM_ID + EQUALS + mcmIdStr;
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

  private void retrieveKISIdsByOneQuery(List mcms) throws RetrieveObjectException {

    if ((mcms == null) || (mcms.isEmpty()))
			return;     

    StringBuffer sql = new StringBuffer(SQL_SELECT
                + StorableObjectWrapper.COLUMN_ID + COMMA
                + LINK_COLUMN_MCM_ID
                + SQL_FROM + ObjectEntities.KIS_ENTITY
                + SQL_WHERE + LINK_COLUMN_MCM_ID
                + SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = mcms.iterator(); it.hasNext(); i++) {
			MCM mcm = (MCM)it.next();
			sql.append(DatabaseIdentifier.toSQLString(mcm.getId()));
			if (it.hasNext()) {
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(LINK_COLUMN_MCM_ID);
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
			Log.debugMessage("MCMDatabase.retrieveKISIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			Map kisMap = new HashMap();

			while (resultSet.next()) {
				Identifier mcmId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MCM_ID);
				List kisIds = (List)kisMap.get(mcmId);
				if (kisIds == null) {
					kisIds = new LinkedList();
					kisMap.put(mcmId, kisIds);
				}
				kisIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));
			}
			
			for (Iterator it = mcms.iterator(); it.hasNext();) {
				MCM mcm = (MCM)it.next();
				List kisIds = (List)kisMap.get(mcm.getId());

				mcm.setKISIds0(kisIds);
			}
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieveKISIdsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
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
		MCM mcm = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(mcm, true);
				break;
			case UPDATE_CHECK: 					
			default:
				super.checkAndUpdateEntity(mcm, false);
				break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(mcm);
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else 
			list = this.retrieveByIdsOneQuery(ids, condition);

    if (list != null) {
			this.retrieveKISIdsByOneQuery(list);

      CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list,
					CharacteristicSort.CHARACTERISTIC_SORT_MCM);
			if (characteristicMap != null)
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					MCM mcm = (MCM) iter.next();
					List characteristics = (List) characteristicMap.get(mcm.getId());
					mcm.setCharacteristics0(characteristics);
				}
		}
		return list;	
		//return retriveByIdsPreparedStatement(ids);
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId());

		try {
				list = this.retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {           
				Log.debugMessage(this.getEnityName() + "Database.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}

	private List retrieveByKISs(List kisIds) throws RetrieveObjectException, IllegalDataException{
		if (kisIds == null || kisIds.isEmpty())
			return Collections.EMPTY_LIST;

		StringBuffer sql = new StringBuffer(
			StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET + 
				SQL_SELECT + LINK_COLUMN_MCM_ID + SQL_FROM
				+ ObjectEntities.KIS_ENTITY + SQL_WHERE	+ StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = kisIds.iterator(); it.hasNext(); i++) {
			Identifier kidId = (Identifier) it.next();
			sql.append(DatabaseIdentifier.toSQLString(kidId));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(StorableObjectWrapper.COLUMN_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
			}
		}
		sql.append(CLOSE_BRACKET);
		sql.append(CLOSE_BRACKET);

		return this.retrieveByIds(null, sql.toString());
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list = null;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else
			if (condition instanceof LinkedIdsCondition) {
				LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
				List kisIds = linkedIdsCondition.getLinkedIds();
				list = this.retrieveByKISs(kisIds);
			}
			else {
				Log.errorMessage(this.getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
				list = this.retrieveButIds(ids);
			}
		return list;
	}
}
