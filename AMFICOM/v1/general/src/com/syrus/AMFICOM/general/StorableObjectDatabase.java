/*-
 * $Id: StorableObjectDatabase.java,v 1.164 2005/07/14 16:08:04 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.164 $, $Date: 2005/07/14 16:08:04 $
 * @author $Author: bass $
 * @module general_v1
 */

public abstract class StorableObjectDatabase {

	public static final String APOSTROPHE = "'";
	public static final String CLOSE_BRACKET = " ) ";

	public static final String DOT = " . ";
	public static final String COMMA = " , ";
	public static final String EQUALS = " = ";
	public static final String NOT_EQUALS = " <> ";
	public static final String NOT = " NOT ";
	public static final String OPEN_BRACKET = " ( ";
	public static final String QUESTION = "?";

	public static final String SQL_AND = " AND ";
	public static final String SQL_ASC = " ASC ";
	public static final String SQL_COUNT = " COUNT(*) ";
	public static final String SQL_DELETE_FROM = " DELETE FROM ";
	public static final String SQL_DESC = " DESC ";
	public static final String SQL_FROM = " FROM ";

	public static final String SQL_FUNCTION_MAX = " MAX ";
	public static final String SQL_IN = " IN ";
	public static final String SQL_NOT_IN = " NOT IN ";
	public static final String SQL_INSERT_INTO = " INSERT INTO ";
	public static final String SQL_NULL_TRIMMED = "NULL";
	public static final String SQL_NULL = ' ' + SQL_NULL_TRIMMED + ' ';
	public static final String SQL_OR = " OR ";
	public static final String SQL_ORDER_BY = " ORDER BY ";
	public static final String SQL_SELECT = " SELECT ";
	public static final String SQL_SET = " SET ";
	public static final String SQL_UPDATE = " UPDATE ";
	public static final String SQL_VALUES = " VALUES ";
	public static final String SQL_WHERE = " WHERE ";
	public static final String SQL_FUNCTION_EMPTY_BLOB = " EMPTY_BLOB() ";
	public static final String SQL_IS = " IS ";

	protected enum UpdateKind {UPDATE_TOTAL, UPDATE_FORCE, UPDATE_CHECK}

	protected enum ExecuteMode {MODE_INSERT, MODE_UPDATE}

	public static final int SIZE_CODENAME_COLUMN = 32;
	public static final int SIZE_NAME_COLUMN = 64;
	public static final int SIZE_DESCRIPTION_COLUMN = 256;

	private static final long MAX_LOCK_TIMEOUT = 1 * 60 * 1000; // 1 minuta
	private static final long LOCK_TIME_WAIT = 5 * 1000; // 5 sec

	/**
	 * @see "ORA-01795"
	 */
	public static final int MAXIMUM_EXPRESSION_NUMBER = 1000;

	private static String columns;
	private static String updateMultipleSQLValues;
	private String retrieveQuery;

	private static Set<Identifier> lockedObjectIds;

	static {
		lockedObjectIds = Collections.synchronizedSet(new HashSet<Identifier>());
	}

	// //////////////////// common /////////////////////////

	protected abstract short getEntityCode();

	protected final String getEntityName() {
		return ObjectEntities.codeToString(this.getEntityCode());
	}

	protected abstract String getColumnsTmpl();

	protected final String getColumns(final ExecuteMode mode) {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CREATED + COMMA
					+ StorableObjectWrapper.COLUMN_MODIFIED + COMMA
					+ StorableObjectWrapper.COLUMN_CREATOR_ID + COMMA
					+ StorableObjectWrapper.COLUMN_MODIFIER_ID + COMMA
					+ StorableObjectWrapper.COLUMN_VERSION + COMMA;
		}
		switch (mode) {
			case MODE_INSERT:
				return StorableObjectWrapper.COLUMN_ID + COMMA + columns + this.getColumnsTmpl();
			case MODE_UPDATE:
				return columns + this.getColumnsTmpl();
			default:
				Log.errorMessage(this.getEntityName() + "Database.getColumns | Unknown mode: " + mode);
				return null;
		}
	}

	protected final String getInsertMultipleSQLValues() {
		return QUESTION + COMMA + this.getUpdateMultipleSQLValues();
	}

	protected abstract String getUpdateMultipleSQLValuesTmpl();

	protected final String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA;
		}
		return updateMultipleSQLValues + this.getUpdateMultipleSQLValuesTmpl();
	}

	protected abstract String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException;

	protected final String getUpdateSingleSQLValues(final StorableObject storableObject, final ExecuteMode mode)
			throws IllegalDataException {
		String modeString;
		switch (mode) {
			case MODE_INSERT:
				modeString = DatabaseIdentifier.toSQLString(storableObject.getId()) + COMMA;
				break;
			case MODE_UPDATE:
				modeString = "";
				break;
			default:
				String msg = this.getEntityName() + "Database.getUpdateSingleSQLValues | Unknown mode: " + mode;
				throw new IllegalDataException(msg);

		}
		return modeString + DatabaseDate.toUpdateSubString(storableObject.getCreated()) + COMMA
				+ DatabaseDate.toUpdateSubString(storableObject.getModified()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getCreatorId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getModifierId()) + COMMA
				+ Long.toString(storableObject.getVersion()) + COMMA
				+ this.getUpdateSingleSQLValuesTmpl(storableObject);
	}

	protected String retrieveQuery(final String condition) {
		StringBuffer buffer;
		if (this.retrieveQuery == null) {
			buffer = new StringBuffer(SQL_SELECT);
			String cols = this.getColumns(ExecuteMode.MODE_INSERT);
			cols = cols.replaceFirst(StorableObjectWrapper.COLUMN_CREATED,
					DatabaseDate.toQuerySubString(StorableObjectWrapper.COLUMN_CREATED));
			cols = cols.replaceFirst(StorableObjectWrapper.COLUMN_MODIFIED,
					DatabaseDate.toQuerySubString(StorableObjectWrapper.COLUMN_MODIFIED));
			buffer.append(cols);
			buffer.append(SQL_FROM);
			buffer.append(this.getEntityName());
			this.retrieveQuery = buffer.toString();
		} else
			buffer = new StringBuffer(this.retrieveQuery);

		if (condition != null && condition.trim().length() > 0) {
			buffer.append(SQL_WHERE);
			buffer.append(condition);
		}

		return buffer.toString();
	}

	protected abstract int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException;

	protected final int setEntityForPreparedStatement(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			final ExecuteMode mode) throws IllegalDataException, SQLException {
		int i = 0;
		switch (mode) {
			case MODE_INSERT:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getId());
				break;
			case MODE_UPDATE:
				break;
			default:
				throw new IllegalDataException(this.getEntityName() + "Database.setEntityForPreparedStatement | Unknown mode " + mode);
		}
		preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getCreated().getTime()));
		preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getModified().getTime()));
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getCreatorId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getModifierId());
		preparedStatement.setLong(++i, storableObject.getVersion());
		return this.setEntityForPreparedStatementTmpl(storableObject, preparedStatement, i);
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

	// ////////////////////refresh /////////////////////////

	/**
	 *
	 * @param storableObjects
	 * @return List&lt;Identifier&gt; of changed storable objects
	 * @throws RetrieveObjectException
	 */
	public final Set<Identifier> refresh(final Set<?  extends StorableObject> storableObjects) throws RetrieveObjectException {
		if (storableObjects == null || storableObjects.isEmpty())
			return Collections.emptySet();

		Set<Identifier> changedObjectsIds = new HashSet<Identifier>();
		StorableObject storableObject;
		Identifier id;
		Map<Identifier, StorableObject> storableObjectsMap = new HashMap<Identifier, StorableObject>();

		StringBuffer stringBuffer = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_VERSION
				+ SQL_FROM + this.getEntityName()
				+ SQL_WHERE + DatabaseStorableObjectCondition.FALSE_CONDITION);
		Set<Identifier> refreshObjectIds = new HashSet<Identifier>();
		for (final Iterator<? extends StorableObject> it = storableObjects.iterator(); it.hasNext();) {
			storableObject = it.next();
			id = storableObject.getId();
			storableObjectsMap.put(id, storableObject);

			long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
			while (lockedObjectIds.contains(id) && System.currentTimeMillis() < deadtime) {
				try {
					Thread.sleep(LOCK_TIME_WAIT);
				} catch (InterruptedException ie) {
					Log.errorException(ie);
				}
			}

			if (!lockedObjectIds.contains(id)) {
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
			} else {
				lockedObjectIds.removeAll(refreshObjectIds);
				throw new RetrieveObjectException("Cannot obtain lock on object " + this.getEntityName() + " '" + id + "'");
			}
		}
		final String sql = stringBuffer.toString();

		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.refresh | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			long dbversion;
			while (resultSet.next()) {
				id = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID);
				storableObject = storableObjectsMap.get(id);
				dbversion = resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION);
				// Refresh only objects with older version, then in DB
				if (storableObject.hasOlderVersion(dbversion))
					changedObjectsIds.add(id);
			}
		} catch (SQLException sqle) {
			final String mesg = this.getEntityName() + "Database.refresh | Cannot execute query " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			lockedObjectIds.removeAll(refreshObjectIds);

			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		return changedObjectsIds;
	}

	// ////////////////////// retrieve /////////////////////////

	public abstract void retrieve(final StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException;

	public abstract Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	protected final void retrieveEntity(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final String strorableObjectIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());
		final String sql = this.retrieveQuery(StorableObjectWrapper.COLUMN_ID + EQUALS + strorableObjectIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				this.updateEntityFromResultSet(storableObject, resultSet);
			else
				throw new ObjectNotFoundException("No such " + getEntityName() + ": " + strorableObjectIdStr);
		} catch (SQLException sqle) {
			final String mesg = this.getEntityName() + "Database.retrieveEntity | Cannot retrieve " + getEntityName()
					+ " '" + strorableObjectIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null)
						resultSet.close();
				} finally {
					try {
						if (statement != null)
							statement.close();
					} finally {
						DatabaseConnection.releaseConnection(connection);
					}
				}
			} catch (SQLException sqle) {
				Log.errorException(sqle);
			}
		}
	}

	public final Set retrieveByCondition(final StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		return this.retrieveByCondition(this.getConditionQuery(condition));
	}

	protected Set retrieveByCondition(final String conditionQuery)
			throws RetrieveObjectException, IllegalDataException {
		final Set<StorableObject> storableObjects = new HashSet<StorableObject>();

		final String sql = this.retrieveQuery(conditionQuery);
		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveByCondition | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				StorableObject storableObject = this.updateEntityFromResultSet(null, resultSet);
				storableObjects.add(storableObject);
			}
		} catch (SQLException sqle) {
			String mesg = this.getEntityName() + "Database.retrieveByCondition | Cannot execute query -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		return storableObjects;
	}

	public final Set retrieveButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws RetrieveObjectException, IllegalDataException {
		StringBuffer stringBuffer = idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, false);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveByCondition(stringBuffer.toString());
	}

	public final Set retrieveAll() throws RetrieveObjectException {
		try {
			return this.retrieveByCondition((String) null);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}

	public final Set retrieveByIdsByCondition(Set<Identifier> ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		final StringBuffer stringBuffer = idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, true);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(this.getConditionQuery(condition));
		}

		return this.retrieveByCondition(stringBuffer.toString());
	}

	/**
	 * Map&lt;StorableObject, List&lt;Identifier&gt;&gt;
	 *
	 * @param storableObjects
	 *            List&lt;StorableObject&gt;
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 * @throws RetrieveObjectException
	 */
	protected final Map<Identifier, Set<Identifier>> retrieveLinkedEntityIds(Set<? extends StorableObject> storableObjects,
			String tableName,
			String idColumnName,
			String linkedIdColumnName)
			throws RetrieveObjectException {
		if (storableObjects == null || storableObjects.isEmpty())
			return Collections.emptyMap();

		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(idsEnumerationString(storableObjects, idColumnName, true));

		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveLinkedEntityIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			final Map<Identifier, Set<Identifier>> linkedEntityIdsMap = new HashMap<Identifier, Set<Identifier>>();
			Identifier storabeObjectId;
			Set<Identifier> linkedEntityIds;
			while (resultSet.next()) {
				storabeObjectId = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				linkedEntityIds = linkedEntityIdsMap.get(storabeObjectId);
				if (linkedEntityIds == null) {
					linkedEntityIds = new HashSet<Identifier>();
					linkedEntityIdsMap.put(storabeObjectId, linkedEntityIds);
				}
				linkedEntityIds.add(DatabaseIdentifier.getIdentifier(resultSet, linkedIdColumnName));
			}

			return linkedEntityIdsMap;
		} catch (SQLException sqle) {
			final String mesg = this.getEntityName()
					+ "Database.retrieveLinkedEntityIds | Cannot retrieve linked entity identifiers for entity -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	protected static boolean isPresentInDatabase(final Identifier id) throws RetrieveObjectException {
		final String aliasCount = "count";
		final String tableName = ObjectEntities.codeToString(id.getMajor());
		final String sql = SQL_SELECT + SQL_COUNT + aliasCount
				+ SQL_FROM + tableName
				+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.isPresentInDatabase | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			resultSet.next();
			final int count = resultSet.getInt(aliasCount);
			return count > 0;
		}
		catch (SQLException sqle) {
			final String mesg = "StorableObjectDatabase.isPresentInDatabase | Cannot check presence -- " + sqle.getMessage();
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

	// //////////////////// insert /////////////////////////

	public abstract void insert(final StorableObject storableObject) throws IllegalDataException, CreateObjectException;

	public abstract void insert(Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException;

	protected final void insertEntity(final StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		final Identifier id = storableObject.getId();
		try {
			if (isPresentInDatabase(id))
				return;
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}

		storableObject.setUpdated(storableObject.getCreatorId());

		final String sql = SQL_INSERT_INTO + this.getEntityName() + OPEN_BRACKET
				+ this.getColumns(ExecuteMode.MODE_INSERT)
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ this.getUpdateSingleSQLValues(storableObject, ExecuteMode.MODE_INSERT)
				+ CLOSE_BRACKET;
		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.insertEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
			storableObject.cleanupUpdate();
		} catch (SQLException sqle) {
			storableObject.rollbackUpdate();
			try {
				connection.rollback();
			} catch (SQLException sqle2) {
				Log.errorException(sqle2);
			}
			final String mesg = this.getEntityName() + "Database.insertEntity | Cannot insert " + this.getEntityName()
					+ " '" + id + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
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

	protected final void insertEntities(final Set<? extends StorableObject> storableObjects)
			throws IllegalDataException, CreateObjectException {
		if (storableObjects == null || storableObjects.isEmpty())
			return;

		if (storableObjects.size() == 1) {
			this.insertEntity(storableObjects.iterator().next());
			return;
		}

		final String sql = SQL_INSERT_INTO + this.getEntityName() + OPEN_BRACKET
				+ this.getColumns(ExecuteMode.MODE_INSERT)
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ this.getInsertMultipleSQLValues()
				+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		final Connection connection = DatabaseConnection.getConnection();
		Identifier id = null;
		final Set<StorableObject> setUpdatedStorableObjects = new HashSet<StorableObject>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage(this.getEntityName() + "Database.insertEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			for (final Iterator<? extends StorableObject> it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				id = storableObject.getId();
				try {
					if (isPresentInDatabase(id)) {
						continue;
					}
				}
				catch (RetrieveObjectException roe) {
					Log.errorException(roe);
				}

				storableObject.setUpdated(storableObject.getCreatorId());
				setUpdatedStorableObjects.add(storableObject);

				this.setEntityForPreparedStatement(storableObject, preparedStatement, ExecuteMode.MODE_INSERT);
				Log.debugMessage(this.getEntityName() + "Database.insertEntities | Inserting  " + this.getEntityName()
						+ " '" + id + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}

			connection.commit();
			for (final Iterator<StorableObject> it = setUpdatedStorableObjects.iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				storableObject.cleanupUpdate();
			}
		} catch (SQLException sqle) {
			for (final Iterator<StorableObject> it = setUpdatedStorableObjects.iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				storableObject.rollbackUpdate();
			}
			try {
				connection.rollback();
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			final String mesg = "StorableObejctDatabase.insertEntities | Cannot insert " + this.getEntityName()
					+ " '" + id + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	/**
	 * @param idLinkedObjectIdsMap
	 *            map of &lt;&lt;Identifier&gt; collectorId ,
	 *            List&lt;Identifier&gt; physicalLinkIds&gt;
	 * @throws CreateObjectException
	 */
	private final void insertLinkedEntityIds(final Map<Identifier, Set<Identifier>> idLinkedObjectIdsMap,
			final String tableName,
			final String idColumnName,
			final String linkedIdColumnName) throws CreateObjectException {
		if (idLinkedObjectIdsMap == null || idLinkedObjectIdsMap.isEmpty())
			return;

		final String sql = SQL_INSERT_INTO + tableName + OPEN_BRACKET
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier id = null;
		Identifier linkedId = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (final Iterator<Identifier> it1 = idLinkedObjectIdsMap.keySet().iterator(); it1.hasNext();) {
				id = it1.next();
				final Set<Identifier> linkedIds = idLinkedObjectIdsMap.get(id);
				for (final Iterator<Identifier> it2 = linkedIds.iterator(); it2.hasNext();) {
					linkedId = it2.next();
					DatabaseIdentifier.setIdentifier(preparedStatement, 1, id);
					DatabaseIdentifier.setIdentifier(preparedStatement, 2, linkedId);
					Log.debugMessage(this.getEntityName() + "Database.insertLinkedEntityIds | Inserting linked entity  '"
							+ linkedId + "' for '" + id + "'", Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
			}
			connection.commit();
		} catch (SQLException sqle) {
			final String mesg = this.getEntityName()
					+ "Database.insertLinkedEntityIds | Cannot insert linked entity  '" + linkedId
					+ "' for " + id + " -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	// //////////////////// update /////////////////////////

	public void update(final StorableObject storableObject, final Identifier modifierId, final UpdateKind updateKind)
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

	public void update(final Set<? extends StorableObject> storableObjects, final Identifier modifierId, final UpdateKind updateKind)
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

	protected final void checkAndUpdateEntity(final StorableObject storableObject, final Identifier modifierId, final boolean force)
			throws UpdateObjectException, VersionCollisionException {
		final Identifier id = storableObject.getId();
		final String idStr = DatabaseIdentifier.toSQLString(storableObject.getId());

		StorableObject dbstorableObject = null;
		final String sql = this.retrieveQuery(StorableObjectWrapper.COLUMN_ID + EQUALS + idStr);
		final Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.checkAndUpdateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				try {
					dbstorableObject = this.updateEntityFromResultSet(dbstorableObject, resultSet);
				} catch (RetrieveObjectException roe) {
					String mesg = "Cannot retrieve corresponding object from datebase for updating object '"
							+ this.getEntityName() + "', id: " + idStr;
					throw new UpdateObjectException(mesg, roe);
				} catch (IllegalDataException ide) {
					String mesg = "Cannot update entity from result set -- " + ide.getMessage();
					throw new UpdateObjectException(mesg, ide);
				}
			} else {
				final String mesg = this.getEntityName() + "Database.checkAndUpdateEntity | No such object '" + id + "'; will try to insert";
				Log.debugMessage(mesg, Log.DEBUGLEVEL08);
				try {
					this.insert(storableObject);
					return;
				} catch (CreateObjectException coe) {
					throw new UpdateObjectException("Cannot insert object " + idStr, coe);
				} catch (IllegalDataException ide) {
					throw new UpdateObjectException("Cannot insert object " + idStr, ide);
				}
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve from database object, corresponding for updating object '"
					+ this.getEntityName() + "', id: " + idStr;
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		final long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
		while (force && lockedObjectIds.contains(id) && System.currentTimeMillis() < deadtime) {
			try {
				Thread.sleep(LOCK_TIME_WAIT);
			} catch (InterruptedException ie) {
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
					throw new VersionCollisionException("Cannot update " + this.getEntityName() + " '" + id + "' -- version conflict",
							dbversion,
							version);
			} finally {
				lockedObjectIds.remove(id);
			}
		} else
			throw new UpdateObjectException("Cannot obtain lock on object " + this.getEntityName() + " '" + id + "'");
	}

	protected final void checkAndUpdateEntities(final Set<? extends StorableObject> storableObjects,
			final Identifier modifierId,
			final boolean force) throws UpdateObjectException, VersionCollisionException {
		if (storableObjects == null || storableObjects.isEmpty())
			return;

//		Identifier id;
//		StorableObject storableObject;

		final Set<Identifier> storableObjectIds = new HashSet<Identifier>();
		for (final Iterator<? extends StorableObject> it = storableObjects.iterator(); it.hasNext();) {
			final StorableObject storableObject = it.next();
			final Identifier id = storableObject.getId();
			if (!storableObjectIds.contains(id))
				storableObjectIds.add(id);
		}

		Set dbstorableObjects = null;
		try {
			dbstorableObjects = this.retrieveByIdsByCondition(storableObjectIds, null);
		} catch (ApplicationException e) {
			throw new UpdateObjectException(e);
		}
		final Map<Identifier, StorableObject> dbstorableObjectsMap = new HashMap<Identifier, StorableObject>();
		for (final Iterator it = dbstorableObjects.iterator(); it.hasNext();) {
			final StorableObject storableObject = (StorableObject) it.next();
			final Identifier id = storableObject.getId();
			dbstorableObjectsMap.put(id, storableObject);
		}

		Set<StorableObject> updateObjects = null;
		Set<Identifier> updateObjectsIds = null;
		Set<StorableObject> insertObjects = null;
		for (final Iterator<? extends StorableObject> it = storableObjects.iterator(); it.hasNext();) {
			final StorableObject storableObject = it.next();
			final Identifier id = storableObject.getId();
			if (dbstorableObjectsMap.containsKey(id)) {

				final long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
				while (force && lockedObjectIds.contains(id) && System.currentTimeMillis() < deadtime) {
					try {
						Thread.sleep(LOCK_TIME_WAIT);
					} catch (InterruptedException ie) {
						Log.errorException(ie);
					}
				}

				if (!lockedObjectIds.contains(id)) {
					lockedObjectIds.add(id);
					final long dbversion = dbstorableObjectsMap.get(id).getVersion();
					final long version = storableObject.getVersion();
					if (version == dbversion || force) {
						if (updateObjectsIds == null)
							updateObjectsIds = new HashSet<Identifier>();
						updateObjectsIds.add(id);
						if (updateObjects == null)
							updateObjects = new HashSet<StorableObject>();
						updateObjects.add(storableObject);
					} else {
						lockedObjectIds.remove(id);
						if (updateObjectsIds != null)
							lockedObjectIds.removeAll(updateObjectsIds);
						throw new VersionCollisionException("Cannot update " + this.getEntityName() + " '" + id + "' -- version conflict",
								dbversion,
								version);
					}
				} else {
					if (updateObjectsIds != null)
						lockedObjectIds.removeAll(updateObjectsIds);
					throw new UpdateObjectException("Cannot obtain lock on object " + this.getEntityName() + " '" + id + "'");
				}

			} else {
				if (insertObjects == null)
					insertObjects = new HashSet<StorableObject>();
				insertObjects.add(storableObject);
			}
		}

		if (updateObjects != null) {
			try {
				this.updateEntities(updateObjects, modifierId);
			} finally {
				lockedObjectIds.removeAll(updateObjectsIds);
			}
		}

		if (insertObjects != null) {
			try {
				this.insert(insertObjects);
			} catch (ApplicationException ae) {
				throw new UpdateObjectException(ae);
			}
		}

	}

	protected final void updateEntity(final StorableObject storableObject, final Identifier modifierId)
			throws UpdateObjectException {
		storableObject.setUpdated(modifierId);

		final String[] cols = this.getColumns(ExecuteMode.MODE_UPDATE).split(COMMA);
		String[] values = null;
		try {
			values = this.parseStringValues(this.getUpdateSingleSQLValues(storableObject, ExecuteMode.MODE_UPDATE), cols.length);
		} catch (IllegalDataException ide) {
			storableObject.rollbackUpdate();
			throw new UpdateObjectException("Cannot parce insert string values for storable object '" + storableObject.getId() + "'",
					ide);
		}
		if (cols.length != values.length) {
			storableObject.rollbackUpdate();
			throw new UpdateObjectException(this.getEntityName() + "Database.updateEntities | Count of columns ('" + cols.length
					+ "') is not equals count of values ('" + values.length + "')");
		}

		final String storableObjectIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());

		final StringBuffer sql = new StringBuffer(SQL_UPDATE + this.getEntityName() + SQL_SET);
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
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.updateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
			storableObject.cleanupUpdate();
		} catch (SQLException sqle) {
			storableObject.rollbackUpdate();
			try {
				connection.rollback();
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			final String mesg = this.getEntityName() + "Database.updateEntity | Cannot update " + this.getEntityName()
					+ storableObjectIdStr + " -- " + sqle.getMessage();
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

	protected final void updateEntities(final Set<? extends StorableObject> storableObjects, final Identifier modifierId)
			throws UpdateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		// Maybe not need this?
		if (storableObjects.size() == 1) {
			this.updateEntity(storableObjects.iterator().next(), modifierId);
			return;
		}

		final String[] cols = this.getColumns(ExecuteMode.MODE_UPDATE).split(COMMA);
		// String[] values =
		// this.parseStringValues(this.getUpdateMultipleSQLValues(), cols.length);
		// here we can split multyply sql values by COMMA because of it is only
		// QUESTIONS separeted by COMMA
		final String[] values = this.getUpdateMultipleSQLValues().split(COMMA);
		if (cols.length != values.length)
			throw new UpdateObjectException(this.getEntityName() + "Database.updateEntities | Count of columns ('" + cols.length
					+ "') is not equals count of values ('" + values.length + "')");

		final StringBuffer sql = new StringBuffer(SQL_UPDATE + this.getEntityName() + SQL_SET);
		for (int i = 0; i < cols.length; i++) {
			if (cols[i].equals(StorableObjectWrapper.COLUMN_ID))
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

		final Connection connection = DatabaseConnection.getConnection();
		PreparedStatement preparedStatement = null;
		Identifier id = null;
		final Set<StorableObject> setUpdatedStorableObjects = new HashSet<StorableObject>();
		try {
			preparedStatement = connection.prepareStatement(sql.toString());
			Log.debugMessage(this.getEntityName() + "Database.updateEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			for (final Iterator<? extends StorableObject> it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				id = storableObject.getId();

				storableObject.setUpdated(modifierId);
				setUpdatedStorableObjects.add(storableObject);

				try {
					int i = this.setEntityForPreparedStatement(storableObject, preparedStatement, ExecuteMode.MODE_UPDATE);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getId());
				} catch (IllegalDataException ide) {
					for (final Iterator<StorableObject> it1 = setUpdatedStorableObjects.iterator(); it1.hasNext();) {
						final StorableObject storableObject1 = it1.next();
						storableObject1.rollbackUpdate();
					}
					try {
						connection.rollback();
					} catch (SQLException sqle1) {
						Log.errorException(sqle1);
					}
					throw new UpdateObjectException("Cannot set entity for prepared statement -- " + ide.getMessage(), ide);
				}

				Log.debugMessage(this.getEntityName()
						+ "Database.updateEntities | Updating " + this.getEntityName() + " '" + id + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}

			connection.commit();
			for (final Iterator<StorableObject> it = setUpdatedStorableObjects.iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				storableObject.cleanupUpdate();
			}
		} catch (SQLException sqle) {
			for (final Iterator<StorableObject> it = setUpdatedStorableObjects.iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				storableObject.rollbackUpdate();
			}
			try {
				connection.rollback();
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			final String mesg = this.getEntityName() + "Database.updateEntities | Cannot update " + this.getEntityName()
					+ " '" + id + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

	}

	/**
	 * If a linked id exists in idLinkedMap but not exists in DB - insert it to
	 * DB If a linked id exists in DB but not exists in idLinkedMap - delete it
	 * from DB
	 *
	 * @param idLinkedIdMap
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 * @throws UpdateObjectException
	 */
	protected final void updateLinkedEntityIds(final Map<Identifier, Set<Identifier>> idLinkedIdMap,
			final String tableName,
			final String idColumnName,
			final String linkedIdColumnName) throws UpdateObjectException {
		if (idLinkedIdMap == null || idLinkedIdMap.isEmpty())
			return;

		final StringBuffer sql = new StringBuffer(SQL_SELECT + idColumnName + COMMA + linkedIdColumnName + SQL_FROM + tableName + SQL_WHERE);
		sql.append(idsEnumerationString(idLinkedIdMap.keySet(), idColumnName, true));

		final Map<Identifier, Set<Identifier>> dbLinkedObjIdsMap = new HashMap<Identifier, Set<Identifier>>();

		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.updateLinkedEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			while (resultSet.next()) {
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				Set<Identifier> dbLinkedObjIds = dbLinkedObjIdsMap.get(id);
				if (dbLinkedObjIds == null) {
					dbLinkedObjIds = new HashSet<Identifier>();
					dbLinkedObjIdsMap.put(id, dbLinkedObjIds);
				}
				dbLinkedObjIds.add(DatabaseIdentifier.getIdentifier(resultSet, linkedIdColumnName));
			}
		} catch (SQLException sqle) {
			String mesg = this.getEntityName() + "Database.updateLinkedEntities | SQLException: " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		final Map<Identifier, Set<Identifier>> insertIdsMap = new HashMap<Identifier, Set<Identifier>>();
		final Map<Identifier, Set<Identifier>> deleteIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Iterator<Identifier> it1 = idLinkedIdMap.keySet().iterator(); it1.hasNext();) {
			final Identifier id = it1.next();
			final Set<Identifier> linkedObjIds = idLinkedIdMap.get(id);
			final Set<Identifier> dbLinkedObjIds = dbLinkedObjIdsMap.get(id);

			if (dbLinkedObjIds != null) {

				// Prepare map for insertion
				for (Iterator<Identifier> it2 = linkedObjIds.iterator(); it2.hasNext();) {
					final Identifier linkedObjId = it2.next();
					if (!dbLinkedObjIds.contains(linkedObjId)) {
						Set<Identifier> alteringIds = insertIdsMap.get(id);
						if (alteringIds == null) {
							alteringIds = new HashSet<Identifier>();
							insertIdsMap.put(id, alteringIds);
						}
						alteringIds.add(linkedObjId);
					}
				}

				// Prepare map for deletion
				for (Iterator<Identifier> it2 = dbLinkedObjIds.iterator(); it2.hasNext();) {
					final Identifier linkedObjId = it2.next();
					if (!linkedObjIds.contains(linkedObjId)) {
						Set<Identifier> alteringIds = deleteIdsMap.get(id);
						if (alteringIds == null) {
							alteringIds = new HashSet<Identifier>();
							deleteIdsMap.put(id, alteringIds);
						}
						alteringIds.add(linkedObjId);
					}
				}

			} else
				insertIdsMap.put(id, linkedObjIds);

		}

		try {
			this.insertLinkedEntityIds(insertIdsMap, tableName, idColumnName, linkedIdColumnName);
			this.deleteLinkedEntityIds(deleteIdsMap, tableName, idColumnName, linkedIdColumnName);
		} catch (CreateObjectException e) {
			throw new UpdateObjectException(e);
		}
	}

	// //////////////////// delete /////////////////////////

	public final void delete(final Identifiable identifiable) {
		this.delete(identifiable.getId());
	}

	public void delete(final Identifier id) {
		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			final String sql = SQL_DELETE_FROM + this.getEntityName()
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(id);
			Log.debugMessage(this.getEntityName() + "Database.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	public void delete(Set<? extends Identifiable> identifiables) {
		if ((identifiables == null) || (identifiables.isEmpty()))
			return;

		final StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM + this.getEntityName() + SQL_WHERE);
		stringBuffer.append(idsEnumerationString(identifiables, StorableObjectWrapper.COLUMN_ID, true));

		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.delete(List) | Trying: " + stringBuffer, Log.DEBUGLEVEL09);
			statement.executeUpdate(stringBuffer.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	protected final void deleteLinkedEntityIds(final Map<Identifier, Set<Identifier>> idLinkedObjectIdsMap,
			final String tableName,
			final String idColumnName,
			final String linkedIdColumnName) {
		if (idLinkedObjectIdsMap == null || idLinkedObjectIdsMap.isEmpty())
			return;

		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + tableName
				+ SQL_WHERE + DatabaseStorableObjectCondition.FALSE_CONDITION);

		for (final Iterator<Identifier> it = idLinkedObjectIdsMap.keySet().iterator(); it.hasNext();) {
			final Identifier id = it.next();
			final Set<Identifier> linkedObjIds = idLinkedObjectIdsMap.get(id);

			sql.append(SQL_OR + OPEN_BRACKET + idColumnName + EQUALS + DatabaseIdentifier.toSQLString(id) + SQL_AND + OPEN_BRACKET);
			sql.append(idsEnumerationString(linkedObjIds, linkedIdColumnName, true));
			sql.append(CLOSE_BRACKET);
			sql.append(CLOSE_BRACKET);
		}

		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.deleteLinkedEntityIds | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	// //////////////////// misc /////////////////////////

	/**
	 * If <code>inList</code> is <code>true</code> returns a string like
	 * "idColumn IN ('id1', 'id2',... , 'idN') OR idColumn IN ('id3', 'id4',... ,
	 * 'idM') ..." If <code>inList</code> is <code>false</code> returns a
	 * string like "idColumn NOT IN ('id1', 'id2',... , 'idN') AND idColumn NOT
	 * IN ('id3', 'id4',... , 'idM') ..."
	 *
	 * @param identifiables
	 * @param idColumn
	 * @param inList
	 * @return String for "WHERE" subclause of SQL query
	 */
	protected static StringBuffer idsEnumerationString(final Set<? extends Identifiable> identifiables,
			final String idColumn,
			final boolean inList) {
		assert identifiables != null : NON_NULL_EXPECTED;

		final Set<Identifier> nonVoidIdentifiers = new HashSet<Identifier>(identifiables.size());
		boolean containsVoidIdentifier = false;
		for (final Identifiable identifiable : identifiables) {
			final Identifier id = identifiable.getId();
			if (id.isVoid()) {
				containsVoidIdentifier = true;
			} else {
				nonVoidIdentifiers.add(id);
			}
		}

		final StringBuffer voidSql = new StringBuffer(OPEN_BRACKET
				+ idColumn + SQL_IS +  (inList ? "" : NOT)
				+ SQL_NULL + CLOSE_BRACKET);
		if (nonVoidIdentifiers.isEmpty()) {
			if (containsVoidIdentifier) {
				return voidSql;
			}
			return new StringBuffer(inList
					? DatabaseStorableObjectCondition.FALSE_CONDITION
					: DatabaseStorableObjectCondition.TRUE_CONDITION);
		}

		final StringBuffer stringBuffer = new StringBuffer((containsVoidIdentifier ? OPEN_BRACKET : "") + OPEN_BRACKET + idColumn + (inList ? SQL_IN : SQL_NOT_IN) + OPEN_BRACKET);

		int i = 0;
		for (final Iterator<Identifier> it = nonVoidIdentifiers.iterator(); it.hasNext(); i++) {
			final Identifiable identifiable = it.next();
			final Identifier id = identifiable.getId();
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

		if (containsVoidIdentifier) {
			stringBuffer.append((inList ? SQL_OR : SQL_AND) + voidSql + CLOSE_BRACKET);
		}

		return stringBuffer;
	}

	private String[] parseStringValues(String values, int columnCount) {
		final int length = values.length();
		final Pattern pattern = Pattern.compile("(('(''|[^'])*')|([^',]+)|(\\w+\\s*\\([^)]+\\)))\\s*(,|$)");
		final Matcher matcher = pattern.matcher(values);
		final String[] parsedValues = new String[columnCount];
		int valueCounter = 0;
		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				int start = matcher.start(i);
				int end = matcher.end(i);
				if ((0 <= start) && (start < end) && (end <= length)) {
					// Log.debugMessage(i + ">\tstart:" + start + "\tend:" + end
					// + "\tlength:" + length, Log.DEBUGLEVEL08);
					if ((0 <= start) && (start < end) && (end <= length)) {
						// Log.debugMessage(i + ">\t\"" +
						// insertValues.substring(matcher.start(i),
						// matcher.end(i)) + '"', Log.DEBUGLEVEL08);
						if ((i == 2) || (i == 4) || (i == 5)) {
							parsedValues[valueCounter++] = values.substring(matcher.start(i), matcher.end(i));
						}
					}
				}
			}
		}

		return parsedValues;
	}

	private String getConditionQuery(final StorableObjectCondition condition) {
		final DatabaseStorableObjectCondition databaseStorableObjectCondition = this.reflectDatabaseCondition(condition);
		if (databaseStorableObjectCondition == null)
			return DatabaseStorableObjectCondition.TRUE_CONDITION;

		final short conditionCode = databaseStorableObjectCondition.getEntityCode().shortValue();
		assert (this.checkEntity(conditionCode)) : this.getEntityName() + "Database.retrieveByCondition | Incompatible condition ("
					+ ObjectEntities.codeToString(conditionCode) + ") and database (" + this.getEntityName() + ") classes";

		String conditionQuery;
		try {
			conditionQuery = databaseStorableObjectCondition.getSQLQuery();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			conditionQuery = DatabaseStorableObjectCondition.TRUE_CONDITION;
		}
		return conditionQuery;
	}
	
	private boolean checkEntity(final short conditionCode) {
		final String enityName = this.getEntityName().replaceAll("\"", "");
		return (ObjectEntities.stringToCode(enityName) == conditionCode);		
	}

	private DatabaseStorableObjectCondition reflectDatabaseCondition(final StorableObjectCondition condition) {
		DatabaseStorableObjectCondition databaseStorableObjectCondition = null;
		final String className = condition.getClass().getName();
		final int lastPoint = className.lastIndexOf('.');
		final String dbClassName = className.substring(0, lastPoint + 1) + "Database" + className.substring(lastPoint + 1);
		try {
			final Class clazz = Class.forName(dbClassName);
			final Constructor constructor = clazz.getDeclaredConstructor(new Class[] {condition.getClass()});
			constructor.setAccessible(true);
			databaseStorableObjectCondition = (DatabaseStorableObjectCondition) constructor.newInstance(new Object[] {condition});
		} catch (ClassNotFoundException e) {
			Log.errorException(e);
		} catch (SecurityException e) {
			Log.errorException(e);
		} catch (NoSuchMethodException e) {
			Log.errorException(e);
		} catch (IllegalArgumentException e) {
			Log.errorException(e);
		} catch (InstantiationException e) {
			Log.errorException(e);
		} catch (IllegalAccessException e) {
			Log.errorException(e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.errorException(e);
			}
		}
		return databaseStorableObjectCondition;
	}

}
