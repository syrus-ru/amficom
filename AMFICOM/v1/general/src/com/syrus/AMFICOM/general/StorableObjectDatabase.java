/*
 * $Id: StorableObjectDatabase.java,v 1.117 2005/03/10 11:44:18 arseniy Exp $
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
 * @version $Revision: 1.117 $, $Date: 2005/03/10 11:44:18 $
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
	public static final String SQL_FUNCTION_EMPTY_BLOB 		= " EMPTY_BLOB() ";
	
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
	private static String updateMultipleSQLValues;
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
			columns = StorableObjectWrapper.COLUMN_CREATED + COMMA
					+ StorableObjectWrapper.COLUMN_MODIFIED + COMMA
					+ StorableObjectWrapper.COLUMN_CREATOR_ID + COMMA
					+ StorableObjectWrapper.COLUMN_MODIFIER_ID + COMMA
					+ StorableObjectWrapper.COLUMN_VERSION;
		}
		switch (mode) {
			case MODE_INSERT:
				return StorableObjectWrapper.COLUMN_ID + COMMA + columns;
			case MODE_UPDATE:
				return columns;
			default:
				Log.errorMessage(this.getEnityName() + "Database.getColumns | Unknown mode: " + mode);
				return null;
		}
	}

	protected String getInsertMultipleSQLValues() {
		return QUESTION + COMMA + this.getUpdateMultipleSQLValues();
	}

	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
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
			throws IllegalDataException, SQLException {
		int i = 0;
		switch (mode) {
			case MODE_INSERT:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getId());
				break;
			case MODE_UPDATE:
				break;
			default:
				throw new IllegalDataException(this.getEnityName() + "Database.setEntityForPreparedStatement | Unknown mode " + mode);
		}
		preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getCreated().getTime()));
		preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getModified().getTime()));
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getCreatorId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getModifierId());
		preparedStatement.setLong(++i, storableObject.getVersion());
		return i;
	}

	/**
	 * If <code>storableObject</code> is <code>null</code> creates new
	 * StorableObject Else - fills it fields from <code>resultSet</code>
	 * 
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
			Log.debugMessage(this.getEnityName() + "Database.refresh | Trying: " + sql, Log.DEBUGLEVEL09);
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
			String mesg = this.getEnityName() + "Database.refresh | Cannot execute query " + sqle.getMessage();
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
		String strorableObjectIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());
		String sql = this.retrieveQuery(StorableObjectWrapper.COLUMN_ID + EQUALS + strorableObjectIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.retrieveEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				this.updateEntityFromResultSet(storableObject, resultSet);
			else
				throw new ObjectNotFoundException("No such " + getEnityName() + ": " + strorableObjectIdStr);
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retrieveEntity | Cannot retrieve " + getEnityName() + " '" + strorableObjectIdStr + "' -- " + sqle.getMessage();
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
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identifiable}&gt;
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	public Collection retrieveButIds(Collection ids) throws IllegalDataException, RetrieveObjectException {
		return this.retrieveButIds(ids, null);
	}

	/**
	 * retrive storable objects by additional condition and identifiers not in ids   
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identifiable}&gt;
	 * @param condition
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	protected Collection retrieveButIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if (ids == null || ids.isEmpty())
			return this.retrieveByIds(null, condition);

		StringBuffer stringBuffer = idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, false);
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
			throw new IllegalDataException(enityName + "Database.retrieveByCondition | Uncompatible condition ("
					+ ObjectEntities.codeToString(conditionCode) + ") and database (" + this.getEnityName() + ") classes");
		String conditionQuery = databaseStorableObjectCondition.getSQLQuery();
		Collection collection = this.retrieveButIds(ids, conditionQuery);
		return collection;
	}

	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
		return objects;
	}

	/**
	 * retrive storable objects by identifiers and additional condition
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identifiable}&gt;
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
			stringBuffer.append(idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, true));
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
			Log.debugMessage(this.getEnityName() + "Database.retrieveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				StorableObject storableObject = this.updateEntityFromResultSet(null, resultSet);
				storableObjects.add(storableObject);
			}
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retrieveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
		sql.append(idsEnumerationString(storableObjects, idColumnName, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.retrieveLinkedEntityIds | Trying: " + sql, Log.DEBUGLEVEL09);
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
			String mesg = this.getEnityName() + "Database.retrieveLinkedEntityIds | Cannot retrieve linked entity identifiers for entity -- " + sqle.getMessage();
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

		String sql = SQL_INSERT_INTO + this.getEnityName() + OPEN_BRACKET
				+ this.getColumns(MODE_INSERT)
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ this.getUpdateSingleSQLValues(storableObject)
				+ CLOSE_BRACKET;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.insertEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.insertEntity | Cannot insert "
					+ this.getEnityName() + " '" + storableObjectIdStr + "' -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

	}

	protected void insertEntities(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		if (storableObjects.size() == 1) {
			this.insertEntity((StorableObject) storableObjects.iterator().next());
			return;
		}

		Collection idsList = new HashSet();
		for(Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject)it.next();
			Identifier localId = storableObject.getId();
			if (idsList.contains(localId))
				throw new CreateObjectException(this.getEnityName() + "Database.insertEntities | Input collection contains entity with the same id " + localId.getIdentifierString());
			idsList.add(storableObject.getId());
		}
		idsList.clear();
		idsList = null;

		String sql = SQL_INSERT_INTO + this.getEnityName() + OPEN_BRACKET
			+ this.getColumns(MODE_INSERT)
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ this.getInsertMultipleSQLValues()
			+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		String storableObjectIdStr = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage(this.getEnityName() + "Database.insertEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObjectIdStr = storableObject.getId().toString();
				this.setEntityForPreparedStatement(storableObject, preparedStatement, MODE_INSERT);
				Log.debugMessage(this.getEnityName() + "Database.insertEntities | Inserting  " + this.getEnityName() + " " + storableObjectIdStr, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}

			connection.commit();			
		}
		catch (SQLException sqle) {
			String mesg = "StorableObejctDatabase.insertEntities | Cannot insert " + this.getEnityName() + " '" + storableObjectIdStr + "' -- " + sqle.getMessage();
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
		if (idLinkedObjectIdsMap == null || idLinkedObjectIdsMap.isEmpty())
			return;

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
					Log.debugMessage(this.getEnityName() + "Database.insertLinkedEntityIds | Inserting linked entity  " + linkedId + " for " + id,
							Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.insertLinkedEntityIds | Cannot insert linked entity  "
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

//	public abstract void update(StorableObject storableObject, Identifier modifierId, int updateKind)
//			throws VersionCollisionException, UpdateObjectException;
	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				this.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				this.checkAndUpdateEntity(storableObject, modifierId, true);
				return;
		}
	}

//	public abstract void update(Collection storableObjects, Identifier modifierId, int updateKind)
//			throws VersionCollisionException, UpdateObjectException;
	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				this.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				this.checkAndUpdateEntities(storableObjects, modifierId, true);
				return;
		}
	}

	protected void checkAndUpdateEntity(StorableObject storableObject, Identifier modifierId, final boolean force)
			throws UpdateObjectException, VersionCollisionException {
		Identifier id = storableObject.getId();
		String idStr = DatabaseIdentifier.toSQLString(storableObject.getId());

		StorableObject dbstorableObject = null;
		String sql = this.retrieveQuery(StorableObjectWrapper.COLUMN_ID + EQUALS + idStr);
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.checkAndUpdateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				try {
					dbstorableObject = this.updateEntityFromResultSet(dbstorableObject, resultSet);
				}
				catch (RetrieveObjectException roe) {
					String mesg = "Cannot retrieve corresponding object from datebase for updating object '" + this.getEnityName() + "', id: " + idStr;
					throw new UpdateObjectException(mesg, roe);
				}
				catch (IllegalDataException ide) {
					String mesg = "Cannot update entity from result set -- " + ide.getMessage();
					throw new UpdateObjectException(mesg, ide);
				}
			}
			else {
				String mesg = this.getEnityName() + "Database.checkAndUpdateEntity | No such object '" + id + "'; will try to insert";
				Log.debugMessage(mesg, Log.DEBUGLEVEL08);
				try {
					this.insert(storableObject);
					return;
				}
				catch (CreateObjectException coe) {
					throw new UpdateObjectException("Cannot insert object " + idStr, coe);
				}
				catch (IllegalDataException ide) {
					throw new UpdateObjectException("Cannot insert object " + idStr, ide);
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "Cannot retrieve from database object, corresponding for updating object '" + this.getEnityName() + "', id: " + idStr;
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

		long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
		while (force && lockedObjectIds.contains(id) && System.currentTimeMillis() < deadtime) {
			try {
				Thread.sleep(LOCK_TIME_WAIT);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}

		if (!lockedObjectIds.contains(id)) {
			try {
				lockedObjectIds.add(id);
				long dbversion = dbstorableObject.version;
				long version = storableObject.version;
				if (version == dbversion || force)
					this.updateEntity(storableObject, modifierId);
				else
					throw new VersionCollisionException("Cannot update " + this.getEnityName() + " '" + id + "' -- version conflict",
							dbversion,
							version);
			}
			finally {
				lockedObjectIds.remove(id);
			}
		}
		else
			throw new UpdateObjectException("Cannot obtain lock on object " + this.getEnityName() + " '" + id + "'");
	}

	protected void checkAndUpdateEntities(Collection storableObjects, Identifier modifierId, final boolean force)
			throws UpdateObjectException, VersionCollisionException {
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
		catch (ApplicationException e) {
			throw new UpdateObjectException(e);
		}
		Map dbstorableObjectsMap = new HashMap();
		for (Iterator it = dbstorableObjects.iterator(); it.hasNext();) {
			storableObject = (StorableObject) it.next();
			id = storableObject.getId();
			dbstorableObjectsMap.put(id, storableObject);
		}

		Collection updateObjects = null;
		Collection updateObjectsIds = null;
		Collection insertObjects = null;
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
					long dbversion = ((StorableObject) dbstorableObjectsMap.get(id)).getVersion();
					long version = storableObject.getVersion();
					if (version == dbversion || force) {
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
								dbversion,
								version);
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
				this.updateEntities(updateObjects, modifierId);
			}
			finally {
				lockedObjectIds.removeAll(updateObjectsIds);
			}
		}

		if (insertObjects != null) {
			try {
				this.insertEntities(insertObjects);
			}
			catch (ApplicationException ae) {
				throw new UpdateObjectException(ae);
			}
		}

	}

	protected void updateEntity(StorableObject storableObject, Identifier modifierId) throws UpdateObjectException {
		storableObject.setUpdated(modifierId);

		String[] cols = this.getColumns(MODE_UPDATE).split(COMMA);
		String[] values = null;
		try {
			values = this.parseInsertStringValues(this.getUpdateSingleSQLValues(storableObject), cols.length);
		}
		catch (IllegalDataException ide) {
			storableObject.rollbackUpdate();
			throw new UpdateObjectException("Cannot parce insert string values for storable object '" + storableObject.getId() + "'", ide);
		}
		if (cols.length != values.length) {
			storableObject.rollbackUpdate();
			throw new UpdateObjectException(this.getEnityName() + "Database.updateEntities | Count of columns ('"
					+ cols.length + "') is not equals count of values ('" + values.length + "')");
		}

		String storableObjectIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());

		StringBuffer sql = new StringBuffer(SQL_UPDATE + this.getEnityName() + SQL_SET);
		for (int i = 0; i < cols.length; i++) {
			sql.append(cols[i]);
			sql.append(EQUALS);
			sql.append(values[i]);
			if (i < cols.length - 1)
				sql.append(COMMA);
		}
		sql.append(SQL_WHERE);
		sql.append(StorableObjectWrapper.COLUMN_ID);
		sql.append(EQUALS);
		sql.append(storableObjectIdStr);

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.updateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
			storableObject.cleanupUpdate();
		}
		catch (SQLException sqle) {
			storableObject.rollbackUpdate();
			try {
				connection.rollback();
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			String mesg = this.getEnityName() + "Database.updateEntity | Cannot update "
				+ this.getEnityName() + storableObjectIdStr + " -- " + sqle.getMessage();
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

	protected void updateEntities(Collection storableObjects, Identifier modifierId) throws UpdateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

//Maybe not need this?
//		if (storableObjects.size() == 1) {
//			this.updateEntity((StorableObject) storableObjects.iterator().next(), modifierId);
//			return;
//		}

		String[] cols = this.getColumns(MODE_UPDATE).split(COMMA);
		// String[] values = this.parseInsertStringValues(this.getUpdateMultipleSQLValues(), cols.length);		
		// here we can split multyply sql values by COMMA because of it is only QUESTIONS separeted by COMMA
		String[] values = this.getUpdateMultipleSQLValues().split(COMMA);
		if (cols.length != values.length)
			throw new UpdateObjectException(this.getEnityName() + "Database.updateEntities | Count of columns ('"+cols.length+"') is not equals count of values ('"+values.length+"')");

		StringBuffer sql = new StringBuffer(SQL_UPDATE + this.getEnityName() + SQL_SET);
		for(int i = 0; i < cols.length; i++) {
			if(cols[i].equals(StorableObjectWrapper.COLUMN_ID))
				continue;
			sql.append(cols[i]);
			sql.append(EQUALS);
			sql.append(values[i]);
			if (i < cols.length - 1)
				sql.append(COMMA);
		}
		sql.append(SQL_WHERE);
		sql.append(StorableObjectWrapper.COLUMN_ID);
		sql.append(EQUALS);
		sql.append(QUESTION);

		Connection connection = DatabaseConnection.getConnection();
		PreparedStatement preparedStatement = null;
		Collection setUpdatedStorableObjects = new HashSet();
		StorableObject storableObject;
		String storableObjectIdCode = null;
		try {
			preparedStatement = connection.prepareStatement(sql.toString());
			Log.debugMessage(this.getEnityName() + "Database.updateEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				storableObject = (StorableObject) it.next();
				storableObjectIdCode = storableObject.getId().getIdentifierString();

				storableObject.setUpdated(modifierId);
				setUpdatedStorableObjects.add(storableObject);

				try {
					int i = this.setEntityForPreparedStatement(storableObject, preparedStatement, MODE_UPDATE);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getId());
				}
				catch (IllegalDataException ide) {
					for (Iterator it1 = setUpdatedStorableObjects.iterator(); it1.hasNext();)
						((StorableObject) it1.next()).rollbackUpdate();
					try {
						connection.rollback();
					}
					catch (SQLException sqle1) {
						Log.errorException(sqle1);
					}
					throw new UpdateObjectException("Cannot set entity for prepared statement -- " + ide.getMessage(), ide);
				}

				Log.debugMessage(this.getEnityName() + "Database.updateEntities | Updating " + this.getEnityName() + " " + storableObjectIdCode,
						Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}

			connection.commit();
			for (Iterator it1 = setUpdatedStorableObjects.iterator(); it1.hasNext();)
				((StorableObject) it1.next()).cleanupUpdate();
		}
		catch (SQLException sqle) {
			for (Iterator it1 = setUpdatedStorableObjects.iterator(); it1.hasNext();)
				((StorableObject) it1.next()).rollbackUpdate();
			try {
				connection.rollback();
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			String mesg = this.getEnityName() + "Database.updateEntities | Cannot update "
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
			throws UpdateObjectException {
		if (idLinkedIdMap == null || idLinkedIdMap.isEmpty())
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		try {
			sql.append(idsEnumerationString(idLinkedIdMap.keySet(), idColumnName, true));
		}
		catch (IllegalDataException ide) {
			throw new UpdateObjectException(ide);
		}

		Map dbLinkedObjIdsMap = new HashMap();
		Identifier id;
		Collection dbLinkedObjIds;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.updateLinkedEntities | Trying: " + sql, Log.DEBUGLEVEL09);
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
			String mesg = this.getEnityName() + "Database.updateLinkedEntities | SQLException: " + sqle.getMessage();
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
			Log.debugMessage(this.getEnityName() + "Database.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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

	public void delete(Collection objects) {
		if ((objects == null) || (objects.isEmpty()))
			return;

		StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM + this.getEnityName() + SQL_WHERE);
		try {
			stringBuffer.append(idsEnumerationString(objects, StorableObjectWrapper.COLUMN_ID, true));
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			return;
		}
		String sql = stringBuffer.toString();

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.delete(List) | Trying: " + sql, Log.DEBUGLEVEL09);
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

	private void deleteLinkedEntityIds(Map idLinkedObjectIdsMap, String tableName, String idColumnName, String linkedIdColumnName) {
		if (idLinkedObjectIdsMap == null || idLinkedObjectIdsMap.isEmpty())
			return;

		StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + tableName + SQL_WHERE + "1=0");

		Identifier id;
		List linkedObjIds;
		try {
			for (Iterator it = idLinkedObjectIdsMap.keySet().iterator(); it.hasNext();) {
				id = (Identifier) it.next();
				linkedObjIds = (List) idLinkedObjectIdsMap.get(id);

				sql.append(SQL_OR + OPEN_BRACKET + idColumnName + EQUALS + DatabaseIdentifier.toSQLString(id) + SQL_AND + OPEN_BRACKET);
				sql.append(idsEnumerationString(linkedObjIds, linkedIdColumnName, true));
				sql.append(CLOSE_BRACKET);
				sql.append(CLOSE_BRACKET);
			}
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			return;
		}

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.deleteLinkedEntityIds | Trying: " + sql, Log.DEBUGLEVEL09);
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
	protected static StringBuffer idsEnumerationString(Collection objects, String idColumn, boolean inList) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return new StringBuffer(inList ? "1=0" : "1=1");

		StringBuffer stringBuffer = new StringBuffer(OPEN_BRACKET + idColumn + (inList ? SQL_IN : SQL_NOT_IN) + OPEN_BRACKET);

		Object object;
		Identifier id;
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = it.next();
			if (object instanceof Identifier)
				id = (Identifier) object;
			else
				if (object instanceof Identifiable)
					id = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("StorableObjectDatabase.listIdsString | Object "
							+ object.getClass().getName()
							+ " isn't Identifier or Identifiable");

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
		stringBuffer.append(CLOSE_BRACKET);

		return stringBuffer;
	}

	private String[] parseInsertStringValues(String insertValues, int columnCount) {
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
