/*
 * $Id: StorableObjectDatabase.java,v 1.101 2005/02/18 16:39:47 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.101 $, $Date: 2005/02/18 16:39:47 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public abstract class StorableObjectDatabase {

	public static final String APOSTOPHE			= "'";
	public static final String CLOSE_BRACKET			= " ) ";

	public static final String DOT				= " . ";
	public static final String COMMA				= " , ";
	public static final String EQUALS				= " = ";
	public static final String NOT_EQUALS			= " <> ";
	public static final String NOT					= " NOT ";
	public static final String OPEN_BRACKET			= " ( ";
	public static final String QUESTION			= "?";

	public static final String SQL_AND				= " AND ";
	public static final String SQL_ASC				= " ASC ";
	public static final String SQL_COUNT			= " COUNT(*) ";
	public static final String SQL_DELETE_FROM			= " DELETE FROM ";
	public static final String SQL_DESC			= " DESC ";
	public static final String SQL_FROM			= " FROM ";

	public static final String SQL_FUNCTION_MAX		= " MAX ";
	public static final String SQL_IN				= " IN ";
	public static final String SQL_NOT_IN				= " NOT IN ";
	public static final String SQL_INSERT_INTO			= " INSERT INTO ";
	public static final String SQL_NULL			= " NULL ";
	public static final String SQL_OR				= " OR ";
	public static final String SQL_ORDER_BY			= " ORDER BY ";
	public static final String SQL_SELECT			= " SELECT ";
	public static final String SQL_SET				= " SET ";
	public static final String SQL_UPDATE			= " UPDATE ";
	public static final String SQL_VALUES			= " VALUES ";
	public static final String SQL_WHERE			= " WHERE ";
	public static final String SQL_EMPTY_BLOB 		= " EMPTY_BLOB() ";
	
	public static final int UPDATE_TOTAL 		= -1;
	public static final int UPDATE_FORCE 		= -2;
	public static final int UPDATE_CHECK 		= -3;

  public static final int MODE_INSERT = -10;
	public static final int MODE_UPDATE = -11;

  protected static final int SIZE_CODENAME_COLUMN 	= 32;
	protected static final int SIZE_NAME_COLUMN			= 64;
	protected static final int SIZE_DESCRIPTION_COLUMN 	= 256;
	
	private static final long MAX_LOCK_TIMEOUT = 1 * 60 * 1000;	//1 minuta
	private static final long LOCK_TIME_WAIT = 5 * 1000;	//5 sec

	/**
	 * @see "ORA-01795"
	 */
	public static final int MAXIMUM_EXPRESSION_NUMBER = 1000;


	private static String columns;
	private static String updateMultiplySQLValues;
	private String retrieveQuery;

	private static List lockedObjectIds; //List <Identifier>

	static {
		lockedObjectIds = Collections.synchronizedList(new LinkedList());
	}

	public StorableObjectDatabase() {
		//ёмпти нах
	}



////////////////////// common /////////////////////////

	protected abstract String getEnityName();

	protected String getColumns(int mode) {
		if (columns == null) {
			String s = new String();
			switch (mode) {
				case MODE_INSERT:
					s = StorableObjectWrapper.COLUMN_ID + COMMA;
					break;
				case MODE_UPDATE:
					break;
				default:
					Log.errorMessage("StorableObjectDatabase.getColumns | Unknown mode: " + mode);
			}
			columns = s
				+ StorableObjectWrapper.COLUMN_CREATED + COMMA
				+ StorableObjectWrapper.COLUMN_MODIFIED + COMMA
				+ StorableObjectWrapper.COLUMN_CREATOR_ID + COMMA
				+ StorableObjectWrapper.COLUMN_MODIFIER_ID + COMMA
				+ StorableObjectWrapper.COLUMN_VERSION;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			String s = new String();
			switch(mode) {
				case MODE_INSERT:
					s = QUESTION + COMMA;
					break;
				case MODE_UPDATE:
					break;
				default:
					Log.errorMessage("StorableObjectDatabase.getUpdateMultiplySQLValues | Unknown mode " + mode);
			}
			updateMultiplySQLValues = s
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;			
		}
		return updateMultiplySQLValues; 
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		return DatabaseIdentifier.toSQLString(storableObject.getId()) + COMMA
			+ DatabaseDate.toUpdateSubString(storableObject.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(storableObject.getModified()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getCreatorId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getModifierId()) + COMMA
			+ Long.toString(storableObject.getVersion());
	}

	protected String retrieveQuery(final String condition) {
		StringBuffer buffer;
		if (this.retrieveQuery == null) {
			buffer = new StringBuffer(SQL_SELECT);
			String cols = this.getColumns(MODE_INSERT);
			cols = cols.replaceFirst(StorableObjectWrapper.COLUMN_CREATED, DatabaseDate.toQuerySubString(StorableObjectWrapper.COLUMN_CREATED));
			cols = cols.replaceFirst(StorableObjectWrapper.COLUMN_MODIFIED, DatabaseDate.toQuerySubString(StorableObjectWrapper.COLUMN_MODIFIED));
			buffer.append(cols);
			buffer.append(SQL_FROM);
			buffer.append(this.getEnityName());
			this.retrieveQuery = buffer.toString();
		}
		else
			buffer = new StringBuffer(this.retrieveQuery);
		if (condition != null && condition.trim().length() > 0) {
			buffer.append(SQL_WHERE);
			buffer.append(condition);
		}
		return buffer.toString();
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		int i = 0;
		try {		
			switch(mode) {
				case MODE_INSERT:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i , storableObject.getId());
					break;
				case MODE_UPDATE:
					break;
				default:
					throw new IllegalDataException("StorableObjectDatabase.setEntityForPreparedStatement | Unknown mode " + mode);
			}
			preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getCreated().getTime()));
			preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getModified().getTime()));
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i , storableObject.getCreatorId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i , storableObject.getModifierId());
			preparedStatement.setLong(++i, storableObject.getVersion());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	/**
	 * If <code>storableObject</code> is <code>null</code> creates new StorableObject
	 * Else - fills it fields from <code>resultSet</code>
	 * @param storableObject
	 * @param resultSet
	 * @return Storable Object with filled fields
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 */
	protected abstract StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException;




//////////////////////refresh /////////////////////////

	/**
	 * 
	 * @param storableObjects
	 * @return List&lt;Identifier&gt; of changed storable objects
	 * @throws RetrieveObjectException
	 */
	public Set refresh(Set storableObjects) throws RetrieveObjectException {
		if (storableObjects == null || storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		Set changedObjectsIds = new HashSet();
		StorableObject storableObject;
		Identifier id;
		Map storableObjectsMap = new HashMap();

		StringBuffer stringBuffer = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_VERSION
				+ SQL_FROM + this.getEnityName()
				+ SQL_WHERE + "1=0");
		List refreshObjectIds = new LinkedList();
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			storableObject = (StorableObject) it.next();
			id = storableObject.getId();
			storableObjectsMap.put(id, storableObject);
			
			long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
			while (lockedObjectIds.contains(id) && System.currentTimeMillis() < deadtime) {
				try {
					Thread.sleep(LOCK_TIME_WAIT);
				}
				catch (InterruptedException ie) {
					Log.errorException(ie);
				}
			}

			if (! lockedObjectIds.contains(id)) {
				lockedObjectIds.add(id);
				refreshObjectIds.add(id);

				stringBuffer.append(SQL_OR);
				stringBuffer.append(OPEN_BRACKET);
				stringBuffer.append(StorableObjectWrapper.COLUMN_ID);
				stringBuffer.append(EQUALS);
				stringBuffer.append(DatabaseIdentifier.toSQLString(id));
				stringBuffer.append(SQL_AND);
				stringBuffer.append(StorableObjectWrapper.COLUMN_VERSION);
				stringBuffer.append(NOT_EQUALS);
				stringBuffer.append(storableObject.getVersion());
				stringBuffer.append(CLOSE_BRACKET);
			}
			else {
				lockedObjectIds.removeAll(refreshObjectIds);
				throw new RetrieveObjectException("Cannot obtain lock on object " + this.getEnityName() + " '" + id + "'");
			}
		}
		String sql = stringBuffer.toString();

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.refresh | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			long dbversion;
			while (resultSet.next()) {
				id = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID);
				storableObject = (StorableObject) storableObjectsMap.get(id);
				dbversion = resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION);
				//Refresh only objects with older version, then in DB
				if (storableObject.hasOlderVersion(dbversion))
					changedObjectsIds.add(id);
			}
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.refresh | Cannot execute query " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			lockedObjectIds.removeAll(refreshObjectIds);

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

		return changedObjectsIds;
	}




//////////////////////// retrieve /////////////////////////

	public abstract void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	public abstract Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException;

	public abstract Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	protected void retrieveEntity(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		String strorableObjectTypeIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());
		String sql = retrieveQuery(StorableObjectWrapper.COLUMN_ID + EQUALS + strorableObjectTypeIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.retrieveEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				this.updateEntityFromResultSet(storableObject, resultSet);
			else
				throw new ObjectNotFoundException("No such " + getEnityName() + ": " + strorableObjectTypeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.retrieveEntity | Cannot retrieve " + getEnityName() + " '" + strorableObjectTypeIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (resultSet != null)
						resultSet.close();
				}
				finally {
					try{
						if (statement != null)
							statement.close();
					}
					finally {
						DatabaseConnection.releaseConnection(connection);
					}
				}
			}
			catch (SQLException sqle) {
				Log.errorException(sqle);
			}
		}
	}

	/**
	 * retrive storable objects by identifiers not in ids
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	public Collection retrieveButIds(Collection ids) throws IllegalDataException, RetrieveObjectException {
		return this.retrieveButIds(ids, null);
	}

	/**
	 * retrive storable objects by additional condition and identifiers not in ids   
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @param condition
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	protected Collection retrieveButIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if (ids == null || ids.isEmpty())
			return this.retrieveByIds(null, null);

		StringBuffer stringBuffer = this.idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, false);
		if ((condition != null) && (condition.length() > 0)) {
			if (stringBuffer.length() != 0)
				stringBuffer.append(SQL_AND);
			stringBuffer.append(condition);
		}

		return this.retrieveByIds(null, stringBuffer.toString());
	}

	public final Collection retrieveByCondition(Collection ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {

		DatabaseStorableObjectCondition databaseStorableObjectCondition = this.reflectDatabaseCondition(condition);
		short conditionCode = databaseStorableObjectCondition.getEntityCode().shortValue();
		String enityName = this.getEnityName();
		enityName = enityName.replaceAll("\"", "");
		if (ObjectEntities.stringToCode(enityName) != conditionCode)
			throw new IllegalDataException(enityName
					+ "Database.retrieveByCondition | Uncompatible condition ("
					+ ObjectEntities.codeToString(conditionCode)
					+ ") and database ("
					+ this.getEnityName()
					+ ") classes");
		String conditionQuery = databaseStorableObjectCondition.getSQLQuery();
		Collection collection = this.retrieveButIds(ids, conditionQuery);
		return collection;
	}

	/**
	 * retrive storable objects by identifiers and additional condition
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @param condition
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	protected Collection retrieveByIdsOneQuery(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List storableObjects = new LinkedList();

		StringBuffer stringBuffer = new StringBuffer("1=1");
		if ( (ids != null) && (!ids.isEmpty())) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, true));
			stringBuffer.append(CLOSE_BRACKET);
		}

		if ((condition != null) && (condition.length() > 0)) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(condition);
		}

		String sql = retrieveQuery(stringBuffer.toString());
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.retrieveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				StorableObject storableObject = this.updateEntityFromResultSet(null, resultSet);
				storableObjects.add(storableObject);
			}
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.retrieveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}

		return storableObjects;
	}

	/**
	 * Map&lt;StorableObject, List&lt;Identifier&gt;&gt;
	 * @param storableObjects List&lt;StorableObject&gt;
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 */
	protected Map retrieveLinkedEntityIds(Collection storableObjects, String tableName, String idColumnName, String linkedIdColumnName) throws RetrieveObjectException, IllegalDataException{
		if (storableObjects == null || storableObjects.isEmpty())
			return Collections.EMPTY_MAP;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(this.idsEnumerationString(storableObjects, idColumnName, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.retrieveLinkedEntityIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map linkedEntityIdsMap = new HashMap();
			Identifier storabeObjectId;
			Collection linkedEntityIds;
			while (resultSet.next()) {
				storabeObjectId = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				linkedEntityIds = (Collection) linkedEntityIdsMap.get(storabeObjectId);
				if (linkedEntityIds == null) {
					linkedEntityIds = new HashSet();
					linkedEntityIdsMap.put(storabeObjectId, linkedEntityIds);
				}
				linkedEntityIds.add(DatabaseIdentifier.getIdentifier(resultSet, linkedIdColumnName));
			}

			return linkedEntityIdsMap;
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.retrieveLinkedEntityIds | Cannot retrieve linked entity identifiers for entity -- " + sqle.getMessage();
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
	}




////////////////////// insert /////////////////////////

	public abstract void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException;

	public abstract void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException;

	protected void insertEntity(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		String storableObjectIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());
		try{
			String sql = SQL_INSERT_INTO + this.getEnityName() + OPEN_BRACKET + this.getColumns(MODE_INSERT)
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ this.getUpdateSingleSQLValues(storableObject) + CLOSE_BRACKET;
			Statement statement = null;
			Connection connection = DatabaseConnection.getConnection();
			try {
				statement = connection.createStatement();
				Log.debugMessage("StorableObjectDatabase.insertEntity | Trying: " + sql, Log.DEBUGLEVEL09);
				statement.executeUpdate(sql);
				connection.commit();
			}
			catch (SQLException sqle) {
				String mesg = "StorableObjectDatabase.insertEntity | Cannot insert " + this.getEnityName() + " '" + storableObjectIdStr + "' -- " + sqle.getMessage();
				try {
					connection.rollback();
				}
				catch (SQLException sqle2) {
					Log.errorMessage("Exception in rolling back");
					Log.errorException(sqle2);
				}
				throw new CreateObjectException(mesg, sqle);
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
				finally{
					DatabaseConnection.releaseConnection(connection);
				}
			}
			
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	protected void insertEntities(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		if (storableObjects.size() == 1) {
			this.insertEntity((StorableObject) storableObjects.iterator().next());
			return;
		}

		List idsList = new LinkedList();
		for(Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject)it.next();
			Identifier localId = storableObject.getId();
			if (idsList.contains(localId))
				throw new CreateObjectException("StorableObejctDatabase.insertEntities | Input collection contains entity with the same id " + localId.getIdentifierString());
			idsList.add(storableObject.getId());
		}
		idsList.clear();
		idsList = null;

		String sql = SQL_INSERT_INTO + this.getEnityName() + OPEN_BRACKET
			+ this.getColumns(MODE_INSERT)
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET + this.getUpdateMultiplySQLValues(MODE_INSERT)
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		String storableObjectIdCode = null;

		Connection connection = DatabaseConnection.getConnection();

		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("StorableObejctDatabase.insertEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObjectIdCode = storableObject.getId().getIdentifierString();
				this.setEntityForPreparedStatement(storableObject, preparedStatement, MODE_INSERT);
				Log.debugMessage("StorableObejctDatabase.insertEntities | Inserting  " + this.getEnityName() + " " + storableObjectIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}

			connection.commit();			
		}
		catch (SQLException sqle) {
			String mesg = "StorableObejctDatabase.insertEntities | Cannot insert " + this.getEnityName() + " '" + storableObjectIdCode + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		catch(UpdateObjectException uoe){
			throw new CreateObjectException(uoe);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;				
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	/**
	 * @param idLinkedObjectIdsMap
	 *          map of &lt;&lt;Identifier&gt; collectorId , List&lt;Identifier&gt;
	 *          physicalLinkIds&gt;
	 * @throws CreateObjectException
	 */
	private void insertLinkedEntityIds(Map idLinkedObjectIdsMap, String tableName, String idColumnName, String linkedIdColumnName)
			throws CreateObjectException {
		String sql = SQL_INSERT_INTO + tableName + OPEN_BRACKET
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier id = null;
		Collection linkedIds = null;
		Identifier linkedId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator it1 = idLinkedObjectIdsMap.keySet().iterator(); it1.hasNext();) {
				id = (Identifier) it1.next();
				linkedIds = (Collection) idLinkedObjectIdsMap.get(id);
				for (Iterator it2 = linkedIds.iterator(); it2.hasNext();) {
					linkedId = (Identifier) it2.next();
					DatabaseIdentifier.setIdentifier(preparedStatement, 1, id);
					DatabaseIdentifier.setIdentifier(preparedStatement, 2, linkedId);
					Log.debugMessage("StorableObjectDatabase.insertLinkedEntityIds | Inserting linked entity  " + linkedId + " for " + id,
							Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.insertLinkedEntityIds | Cannot insert linked entity  "
					+ linkedId + " for " + id + " -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}




////////////////////// update /////////////////////////

	public abstract void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException;

	public abstract void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException;


	protected void checkAndUpdateEntity(StorableObject storableObject, Identifier modifierId, final boolean force)
			throws UpdateObjectException, VersionCollisionException, IllegalDataException {
		Identifier id = storableObject.getId();
		String idStr = DatabaseIdentifier.toSQLString(storableObject.getId());

		StorableObject dbstorableObject = null;
		String sql = this.retrieveQuery(StorableObjectWrapper.COLUMN_ID + EQUALS + idStr);
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEnityName() + "Database.checkAndUpdateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {

				try {
					this.updateEntityFromResultSet(storableObject, resultSet);
				}
				catch (RetrieveObjectException roe) {
					throw new UpdateObjectException(roe);
				}

				long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
				while (force && lockedObjectIds.contains(id) && System.currentTimeMillis() < deadtime)
					try {
						Thread.sleep(LOCK_TIME_WAIT);
					}
					catch (InterruptedException ie) {
						Log.errorException(ie);
					}

				if (! lockedObjectIds.contains(id)) {
					try {
						lockedObjectIds.add(id);
						long currentVersion = dbstorableObject.getVersion();
						long newVersion = storableObject.getVersion();
						if (newVersion == currentVersion || force) {
							this.updateEntity(storableObject);
							storableObject.setUpdated(modifierId);
						}
						else
							throw new VersionCollisionException("Cannot update " + this.getEnityName() + " '" + id + "' -- version conflict",
									currentVersion,
									newVersion);
					}
					finally {
						lockedObjectIds.remove(id);
					}
				}
				else
					throw new UpdateObjectException("Cannot obtain lock on object " + this.getEnityName() + " '" + id + "'");
			}
			else {
				String mesg = getEnityName() + "Database.checkAndUpdateEntity | No such object '" + id + "'; will try to insert";
				Log.debugMessage(mesg, Log.DEBUGLEVEL08);
				try {
					this.insert(storableObject);
				}
				catch (CreateObjectException e) {
					throw new UpdateObjectException(e);
				}
			}
		}
		catch (SQLException sqle) {			
			String mesg = "SorableObjectDatabase.checkAndUpdateEntity | Cannot update " + this.getEnityName() + " '"
					+ id + "' -- " + sqle.getMessage();
			try {
				connection.rollback();
			}
			catch (SQLException sqle2) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle2);
			}
			throw new UpdateObjectException(mesg, sqle);
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

	protected void checkAndUpdateEntities(Collection storableObjects, Identifier modifierId, final boolean force)
			throws UpdateObjectException, VersionCollisionException, IllegalDataException {
		if (storableObjects == null || storableObjects.isEmpty())
			return;

		Identifier id;
		StorableObject storableObject;

		List storableObjectIds = new LinkedList();
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			if (! storableObjectIds.contains(id))
				storableObjectIds.add(id);
		}

		Collection dbstorableObjects = null;
		try {
			dbstorableObjects = this.retrieveByIds(storableObjectIds, null);
		}
		catch (RetrieveObjectException e) {
			throw new UpdateObjectException(e);
		}
		Map dbstorableObjectsMap = new HashMap();
		for (Iterator it = dbstorableObjects.iterator(); it.hasNext();) {
			storableObject = (StorableObject) it.next();
			id = storableObject.getId();
			dbstorableObjectsMap.put(id, storableObject);
		}

		List updateObjects = null;
		List updateObjectsIds = null;
		List insertObjects = null;
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			storableObject = (StorableObject) it.next();
			id = storableObject.getId();
			if (dbstorableObjectsMap.containsKey(id)) {

				long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
				while (force && lockedObjectIds.contains(id) && System.currentTimeMillis() < deadtime) {
					try {
						Thread.sleep(LOCK_TIME_WAIT);
					}
					catch (InterruptedException ie) {
						Log.errorException(ie);
					}
				}

				if (! lockedObjectIds.contains(id)) {
					lockedObjectIds.add(id);
					long currentVersion = ((StorableObject) dbstorableObjectsMap.get(id)).getVersion();
					long newVersion = storableObject.getVersion();
					if (newVersion == currentVersion || force) {
						if (updateObjectsIds == null)
							updateObjectsIds = new LinkedList();
						updateObjectsIds.add(id);
						if (updateObjects == null)
							updateObjects = new LinkedList();
						updateObjects.add(storableObject);
					}
					else {
						lockedObjectIds.remove(id);
						if (updateObjectsIds != null)
							lockedObjectIds.removeAll(updateObjectsIds);
						throw new VersionCollisionException("Cannot update " + this.getEnityName() + " '" + id + "' -- version conflict",
								currentVersion,
								newVersion);
					}
				}
				else {
					if (updateObjectsIds != null)
						lockedObjectIds.removeAll(updateObjectsIds);
					throw new UpdateObjectException("Cannot obtain lock on object " + this.getEnityName() + " '" + id + "'");
				}

			}
			else {
				if (insertObjects == null)
					insertObjects = new LinkedList();
				insertObjects.add(storableObject);
			}
		}

		if (updateObjects != null) {
			try {
				this.updateEntities(updateObjects);
				for (Iterator it = updateObjects.iterator(); it.hasNext();) {
					storableObject = (StorableObject) it.next();
					storableObject.setUpdated(modifierId);
				}
			}
			finally {
				lockedObjectIds.removeAll(updateObjectsIds);
			}
		}

		if (insertObjects != null) {
			try {
				this.insertEntities(insertObjects);
			}
			catch (CreateObjectException coe) {
				throw new UpdateObjectException(coe);
			}
		}

	}

	protected void updateEntity(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		String storableObjectIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());

		String[] cols = this.getColumns(MODE_UPDATE).split(COMMA);
		String[] values = this.parseInsertStringValues(this.getUpdateSingleSQLValues(storableObject), cols.length);
		if (cols.length != values.length)
			throw new UpdateObjectException("StorableObjectDatabase.updateEntities | Count of columns ('"
					+ cols.length + "') is not equals count of values ('" + values.length + "')");
		String sql = null;
		{
			StringBuffer buffer = new StringBuffer(SQL_UPDATE);
			buffer.append(this.getEnityName());
			buffer.append(SQL_SET);
			for (int i = 0; i < cols.length; i++) {
				buffer.append(cols[i]);
				buffer.append(EQUALS);
				buffer.append(values[i]);
				if (i < cols.length - 1)
					buffer.append(COMMA);
			}
			buffer.append(SQL_WHERE);
			buffer.append(StorableObjectWrapper.COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(storableObjectIdStr);
			sql = buffer.toString();
		}

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.updateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.updateEntity | Cannot update "
					+ this.getEnityName()
					+ " '"
					+ storableObjectIdStr
					+ "' -- "
					+ sqle.getMessage();
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

	protected void updateEntities(Collection storableObjects) throws IllegalDataException, UpdateObjectException {

		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		if (storableObjects.size() == 1) {
			this.updateEntity((StorableObject) storableObjects.iterator().next());
			return;
		}

		String[] cols = this.getColumns(MODE_UPDATE).split(COMMA);
		// String[] values = this.parseInsertStringValues(this.getUpdateMultiplySQLValues(), cols.length);		
		// here we can split multyply sql values by COMMA because of it is only QUESTIONS separeted by COMMA
		String[] values = this.getUpdateMultiplySQLValues(MODE_INSERT).split(COMMA);
		if (cols.length != values.length)
			throw new UpdateObjectException("StorableObjectDatabase.updateEntities | Count of columns ('"+cols.length+"') is not equals count of values ('"+values.length+"')");
		String sql = null;
		{
			StringBuffer buffer = new StringBuffer(SQL_UPDATE);
			buffer.append(this.getEnityName());
			buffer.append(SQL_SET);
			for(int i = 0; i < cols.length; i++) {
				if(cols[i].equals(StorableObjectWrapper.COLUMN_ID))
					continue;
				buffer.append(cols[i]);
				buffer.append(EQUALS);
				buffer.append(values[i]);
				if (i < cols.length - 1)
					buffer.append(COMMA);
			}
			buffer.append(SQL_WHERE);
			buffer.append(StorableObjectWrapper.COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(QUESTION);
			sql = buffer.toString();
		}
		PreparedStatement preparedStatement = null;
		String storableObjectIdCode = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("StorableObjectDatabase.updateEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {

				StorableObject storableObject = (StorableObject) it.next();
				storableObjectIdCode = storableObject.getId().getIdentifierString();
				int i = this.setEntityForPreparedStatement(storableObject, preparedStatement, MODE_UPDATE);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getId());

				Log.debugMessage("StorableObjectDatabase.updateEntities | Updating "
						+ this.getEnityName() + " " + storableObjectIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.updateEntities | Cannot update "
					+ this.getEnityName() + " '" + storableObjectIdCode + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	/**
	 * If a linked id exists in idLinkedMap but not exists in DB - insert it to DB
	 * If a linked id exists in DB but not exists in idLinkedMap - delete it from DB
	 * @param idLinkedIdMap
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 * @throws UpdateObjectException
	 * @throws IllegalDataException
	 */
	protected void updateLinkedEntities(Map idLinkedIdMap, String tableName, String idColumnName, String linkedIdColumnName)
			throws UpdateObjectException,
				IllegalDataException {
		if (idLinkedIdMap == null || idLinkedIdMap.isEmpty())
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(this.idsEnumerationString(idLinkedIdMap.keySet(), idColumnName, true));

		Map dbLinkedObjIdsMap = new HashMap();
		Identifier id;
		Collection dbLinkedObjIds;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.updateLinkedEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			while (resultSet.next()) {
				id = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				dbLinkedObjIds = (Collection) dbLinkedObjIdsMap.get(id);
				if (dbLinkedObjIds == null) {
					dbLinkedObjIds = new LinkedList();
					dbLinkedObjIdsMap.put(id, dbLinkedObjIds);
				}
				dbLinkedObjIds.add(DatabaseIdentifier.getIdentifier(resultSet, linkedIdColumnName));
			}
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.updateLinkedEntities | SQLException: " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}

		Collection linkedObjIds;
		Map insertIdsMap = new HashMap();
		Map deleteIdsMap = new HashMap();
		Identifier linkedObjId;
		Collection alteringIds;
		for (Iterator it1 = idLinkedIdMap.keySet().iterator(); it1.hasNext();) {
			id = (Identifier) it1.next();
			linkedObjIds = (Collection) idLinkedIdMap.get(id);
			dbLinkedObjIds =  (Collection) dbLinkedObjIdsMap.get(id);

			if (dbLinkedObjIds != null) {

				//Prepare map for insertion
				for (Iterator it2 = linkedObjIds.iterator(); it2.hasNext();) {
					linkedObjId = (Identifier) it2.next();
					if (!dbLinkedObjIds.contains(linkedObjId)) {
						alteringIds = (List) insertIdsMap.get(id);
						if (alteringIds == null) {
							alteringIds = new LinkedList();
							insertIdsMap.put(id, alteringIds);
						}
						alteringIds.add(linkedObjId);
					}
				}

				//Prepare map for deletion
				for (Iterator it2 = dbLinkedObjIds.iterator(); it2.hasNext();) {
					linkedObjId = (Identifier) it2.next();
					if (! linkedObjIds.contains(linkedObjId)) {
						alteringIds = (List) deleteIdsMap.get(id);
						if (alteringIds == null) {
							alteringIds = new LinkedList();
							deleteIdsMap.put(id, alteringIds);
						}
						alteringIds.add(linkedObjId);
					}
				}

			}
			else
				insertIdsMap.put(id, linkedObjIds);

		}

		try {
			this.insertLinkedEntityIds(insertIdsMap, tableName, idColumnName, linkedIdColumnName);
			this.deleteLinkedEntityIds(deleteIdsMap, tableName, idColumnName, linkedIdColumnName);
		}
		catch (CreateObjectException e) {
			throw new UpdateObjectException(e);
		}
	}




////////////////////// delete /////////////////////////

	public void delete(StorableObject storableObject) throws IllegalDataException {
		this.delete(storableObject.getId());
	}

	public void delete(Identifier id) throws IllegalDataException {
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM + this.getEnityName()
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(id);
			Log.debugMessage("StorableObjectDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	public void delete(Collection objects) throws IllegalDataException {
		if ((objects == null) || (objects.isEmpty()))
			return;

		StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM + this.getEnityName() + SQL_WHERE);
		stringBuffer.append(this.idsEnumerationString(objects, StorableObjectWrapper.COLUMN_ID, true));
		String sql = stringBuffer.toString();

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.delete(List) | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	private void deleteLinkedEntityIds(Map idLinkedObjectIdsMap, String tableName, String idColumnName, String linkedIdColumnName)
			throws IllegalDataException {
		StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + tableName + SQL_WHERE + "1=0");

		Identifier id;
		List linkedObjIds;
		for (Iterator it = idLinkedObjectIdsMap.keySet().iterator(); it.hasNext();) {
			id = (Identifier) it.next();
			linkedObjIds = (List) idLinkedObjectIdsMap.get(id);

			sql.append(SQL_OR + OPEN_BRACKET + idColumnName + EQUALS + DatabaseIdentifier.toSQLString(id) + SQL_AND + OPEN_BRACKET);
			sql.append(this.idsEnumerationString(linkedObjIds, linkedIdColumnName, true));
			sql.append(CLOSE_BRACKET);
			sql.append(CLOSE_BRACKET);
		}

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.deleteLinedEntityIds | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
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



////////////////////// misc /////////////////////////

	/**
	 * If <code>inList</code> is <code>true</code> returns a string like
	 * "idColumn IN ('id1', 'id2',... , 'idN') OR idColumn IN ('id3', 'id4',... , 'idM') ..."
	 * If <code>inList</code> is <code>false</code> returns a string like
	 * "idColumn NOT IN ('id1', 'id2',... , 'idN') AND idColumn NOT IN ('id3', 'id4',... , 'idM') ..."
	 * @param objects
	 * @param idColumn
	 * @param inList
	 * @return String for "WHERE" subclause of SQL query 
	 * @throws IllegalDataException
	 */
	protected StringBuffer idsEnumerationString(Collection objects, String idColumn, boolean inList) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return null;

		StringBuffer stringBuffer = new StringBuffer(idColumn + (inList ? SQL_IN : SQL_NOT_IN) + OPEN_BRACKET);

		Object object;
		Identifier id;
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = it.next();
			if (object instanceof Identifier)
				id = (Identifier) object;
			else
				if (object instanceof Identified)
					id = ((Identified) object).getId();
				else
					throw new IllegalDataException("StorableObjectDatabase.listIdsString | Object "
							+ object.getClass().getName()
							+ " isn't Identifier or Identified");

			stringBuffer.append(DatabaseIdentifier.toSQLString(id));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					stringBuffer.append(COMMA);
				else {
					stringBuffer.append(CLOSE_BRACKET);
					stringBuffer.append(inList ? SQL_OR : SQL_AND);
					stringBuffer.append(idColumn);
					stringBuffer.append((inList ? SQL_IN : SQL_NOT_IN));
					stringBuffer.append(OPEN_BRACKET);
				}
			}
		}
		stringBuffer.append(CLOSE_BRACKET);

		return stringBuffer;
	}

	private String[] parseInsertStringValues(String insertValues, int columnCount){
		int length = insertValues.length();
		Pattern pattern = Pattern.compile("(('(''|[^'])*')|([^',\\s]+)|(\\w+\\([^)]+\\)))\\s*(,|$)");
		Matcher matcher = pattern.matcher(insertValues);
		String[] values = new String[columnCount];
		int valueCounter = 0;
		//Log.debugMessage("insertValue:\"" + insertValues + "\"", Log.DEBUGLEVEL08);
		//Log.debugMessage("columnCount:" + columnCount, Log.DEBUGLEVEL08);
		while (matcher.find()) {			
			for (int i = 1; i <= matcher.groupCount(); i++) {
				int start = matcher.start(i);
				int end = matcher.end(i);				
				if ((0 <= start) && (start < end) && (end <= length)) {					
					//Log.debugMessage(i + ">\tstart:" + start + "\tend:" + end + "\tlength:" + length, Log.DEBUGLEVEL08);
					if ((0 <= start) && (start < end) && (end <= length)) {
						//Log.debugMessage(i + ">\t\"" + insertValues.substring(matcher.start(i), matcher.end(i)) + '"', Log.DEBUGLEVEL08);
						if ((i == 2) || (i == 4) || (i == 5)) {
							values[valueCounter++] = insertValues.substring(matcher.start(i), matcher.end(i));
						}

					}


				}
			}			
		}
		
		return values;
	}

	private DatabaseStorableObjectCondition reflectDatabaseCondition(StorableObjectCondition condition) throws IllegalDataException {
		DatabaseStorableObjectCondition databaseStorableObjectCondition = null;
		String className = condition.getClass().getName();
		int lastPoint = className.lastIndexOf('.');
		String dbClassName = className.substring(0, lastPoint + 1) + "Database" + className.substring(lastPoint + 1);
		try {
			Class clazz = Class.forName(dbClassName);
			Constructor constructor = clazz.getDeclaredConstructor(new Class[] {condition.getClass()});
			constructor.setAccessible(true);
			databaseStorableObjectCondition = (DatabaseStorableObjectCondition) constructor.newInstance(new Object[] {condition});
		}
		catch (ClassNotFoundException e) {
			String msg = this.getEnityName() + "Database.reflectDatabaseCondition | Class " + dbClassName //$NON-NLS-1$
					+ " not found on the classpath";
			throw new IllegalDataException(msg, e);
		}
		catch (SecurityException e) {
			String msg = this.getEnityName() + "Database.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		}
		catch (NoSuchMethodException e) {
			String msg = this.getEnityName()
					+ "Database.reflectDatabaseCondition | Class  "
					+ dbClassName
					+ " haven't constructor ("
					+ className
					+ ")";
			throw new IllegalDataException(msg, e);
		}
		catch (IllegalArgumentException e) {
			String msg = this.getEnityName()
					+ "Database.reflectDatabaseCondition | Class  "
					+ dbClassName
					+ " haven't constructor ("
					+ className
					+ ")";
			throw new IllegalDataException(msg, e);
		}
		catch (InstantiationException e) {
			String msg = this.getEnityName() + "Database.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		}
		catch (IllegalAccessException e) {
			String msg = this.getEnityName() + "Database.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		}
		catch (InvocationTargetException e) {
			String msg = this.getEnityName() + "Database.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		}
		return databaseStorableObjectCondition;
	}

}
