/*-
 * $Id: StorableObjectDatabase.java,v 1.207.2.2 2006/03/27 10:10:06 bass Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.util.EnumUtil;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.207.2.2 $, $Date: 2006/03/27 10:10:06 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 * Предпочтительный уровень отладочных сообщений: 9
 */

public abstract class StorableObjectDatabase<T extends StorableObject> {

	public static final String APOSTROPHE = "'";
	public static final String DOT = " . ";
	public static final String COMMA = " , ";
	public static final String NOT = " NOT ";
	public static final String OPEN_BRACKET = " ( ";
	public static final String CLOSE_BRACKET = " ) ";
	public static final String QUESTION = "?";
	public static final String SQL_PATTERN_SINGLE_CHARACTER = "_";
	public static final String SQL_PATTERN_CHARACTERS = "%";

	public static final String EQUALS = " = ";
	public static final String NOT_EQUALS = " <> ";
	public static final String GREAT_THAN = " > ";
	public static final String LESS_THAN = " < ";
	public static final String GREAT_THAN_OR_EQUALS = " >= ";
	public static final String LESS_THAN_OR_EQUALS = " <= ";
	public static final String SQL_LIKE = " LIKE ";
	
	public static final String SQL_AND = " AND ";
	public static final String SQL_ASC = " ASC ";
	public static final String SQL_COUNT = " COUNT(*) ";
	public static final String SQL_DELETE_FROM = " DELETE FROM ";
	public static final String SQL_DESC = " DESC ";
	public static final String SQL_FROM = " FROM ";

	public static final String SQL_FUNCTION_MAX = " MAX ";
	public static final String SQL_FUNCTION_UPPER = " UPPER ";
	
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

	protected enum ExecuteMode {MODE_INSERT, MODE_UPDATE}

	public static final int SIZE_CODENAME_COLUMN = 32;
	public static final int SIZE_NAME_COLUMN = 64;
	public static final int SIZE_DESCRIPTION_COLUMN = 256;

	/**
	 * @see "ORA-01795"
	 */
	public static final int MAXIMUM_EXPRESSION_NUMBER = 1000;

	private static String columns;
	private static String updateMultipleSQLValues;
	private String retrieveQuery;


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
				Log.errorMessage("Unknown mode: " + mode);
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

	protected abstract String getUpdateSingleSQLValuesTmpl(T storableObject) throws IllegalDataException;

	/**
	 * This method is not used any longer, but preserved in venerable memory
	 * of Arseniy's technical genius.
	 *
	 * @param storableObject
	 * @param mode
	 * @throws IllegalDataException
	 */
	protected final String getUpdateSingleSQLValues(final T storableObject, final ExecuteMode mode)
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
				final String msg = this.getEntityName() + "Database.getUpdateSingleSQLValues | Unknown mode: " + mode;
				throw new IllegalDataException(msg);

		}
		return modeString + DatabaseDate.toUpdateSubString(storableObject.getCreated()) + COMMA
				+ DatabaseDate.toUpdateSubString(storableObject.getModified()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getCreatorId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getModifierId()) + COMMA
				+ Long.toString(storableObject.getVersion().longValue()) + COMMA
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
		} else {
			buffer = new StringBuffer(this.retrieveQuery);
		}

		if (condition != null && condition.trim().length() > 0) {
			buffer.append(SQL_WHERE);
			buffer.append(condition);
		}

		return buffer.toString();
	}

	protected abstract int setEntityForPreparedStatementTmpl(T storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException;

	protected final int setEntityForPreparedStatement(final T storableObject,
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
		preparedStatement.setLong(++i, storableObject.getVersion().longValue());
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
	protected abstract T updateEntityFromResultSet(T storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException;


	////////////////////////////////////////////// Retrieve Objects /////////////////////////////////////////////

	protected Set<T> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<T> storableObjects = new HashSet<T>();

		final String sql = this.retrieveQuery(conditionQuery);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				final T storableObject = this.updateEntityFromResultSet(null, resultSet);
				storableObjects.add(storableObject);
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot execute query -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}

		return storableObjects;
	}

	public final Set<T> retrieveByCondition(final StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		return this.retrieveByCondition(this.getConditionQuery(condition));
	}

	public final Set<T> retrieveButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		assert StorableObject.hasSingleTypeEntities(ids) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, false);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveByCondition(stringBuffer.toString());
	}

	public final Set<T> retrieveByIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		assert StorableObject.hasSingleTypeEntities(ids) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, true);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveByCondition(stringBuffer.toString());
	}

	public final T retrieveForId(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		assert id != null : ErrorMessages.NON_NULL_EXPECTED;
		assert id.getMajor() == this.getEntityCode() : ErrorMessages.ILLEGAL_ENTITY_CODE;

		final String condition = StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(id);
		try {
			final Set<T> objects = this.retrieveByCondition(condition);
			if (!objects.isEmpty()) {
				return objects.iterator().next();
			}
			throw new ObjectNotFoundException("Object for id '" + id + "' not found");
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}

	public final Set<T> retrieveAll() throws RetrieveObjectException {
		try {
			return this.retrieveByCondition((String) null);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}


	////////////////////////////////////////// Retrieve Identifiers /////////////////////////////////////////////

	public final Set<Identifier> retrieveIdentifiersByCondition(final String conditionQuery) throws RetrieveObjectException {
		final Set<Identifier> identifiers = new HashSet<Identifier>();
		final String tableName = this.getEntityName();

		final StringBuffer sql = new StringBuffer(SQL_SELECT);
		sql.append(StorableObjectWrapper.COLUMN_ID);
		sql.append(SQL_FROM);
		sql.append(tableName);
		sql.append(SQL_WHERE);
		sql.append(conditionQuery);
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID);
				identifiers.add(id);
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot execute query -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
		
		return identifiers;
	}

	public final Set<Identifier> retrieveIdentifiersByCondition(final StorableObjectCondition condition) throws RetrieveObjectException {
		return this.retrieveIdentifiersByCondition(this.getConditionQuery(condition));
	}

	public final Set<Identifier> retrieveIdentifiersButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws RetrieveObjectException {
		assert StorableObject.hasSingleTypeEntities(ids) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, false);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveIdentifiersByCondition(stringBuffer.toString());
	}

	public final Set<Identifier> retrieveIdentifiersByIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws RetrieveObjectException {
		assert StorableObject.hasSingleTypeEntities(ids) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, true);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveIdentifiersByCondition(stringBuffer.toString());
	}


	////////////////////////////////////////// Retrieve linked objects /////////////////////////////////////////////

	/**
	 * Map&lt;StorableObject, List&lt;Identifier&gt;&gt;
	 *
	 * @param identifiables
	 *            List&lt;StorableObject&gt;
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 * @throws RetrieveObjectException
	 */
	protected final Map<Identifier, Set<Identifier>> retrieveLinkedEntityIds(final Set<? extends Identifiable> identifiables,
			final String tableName,
			final String idColumnName,
			final String linkedIdColumnName)
			throws RetrieveObjectException {
		if (identifiables == null || identifiables.isEmpty()) {
			return Collections.emptyMap();
		}

		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(idsEnumerationString(identifiables, idColumnName, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			final Map<Identifier, Set<Identifier>> linkedEntityIdsMap = new HashMap<Identifier, Set<Identifier>>();
			while (resultSet.next()) {
				final Identifier storabeObjectId = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				Set<Identifier> linkedEntityIds = linkedEntityIdsMap.get(storabeObjectId);
				if (linkedEntityIds == null) {
					linkedEntityIds = new HashSet<Identifier>();
					linkedEntityIdsMap.put(storabeObjectId, linkedEntityIds);
				}
				linkedEntityIds.add(DatabaseIdentifier.getIdentifier(resultSet, linkedIdColumnName));
			}

			return linkedEntityIdsMap;
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve linked entity identifiers for entity -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	/**
	 * 
	 * @param <E>
	 * @param identifiables
	 * @param enumClass
	 * @param tableName
	 * @param idColumnName
	 * @param linkedCodeColumnName
	 * @return Map of Identifiers and Enum sets 
	 * @throws RetrieveObjectException
	 */
	protected final <E extends Enum<E>> Map<Identifier, EnumSet<E>> retrieveLinkedEnums(final Set<? extends Identifiable> identifiables,
			final Class<E> enumClass,
			final String tableName,
			final String idColumnName,
			final String linkedCodeColumnName)
			throws RetrieveObjectException {
		if (identifiables == null || identifiables.isEmpty()) {
			return Collections.emptyMap();
		}

		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ idColumnName + COMMA
				+ linkedCodeColumnName
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(idsEnumerationString(identifiables, idColumnName, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			final Map<Identifier, EnumSet<E>> linkedCodesMap = new HashMap<Identifier, EnumSet<E>>();
			while (resultSet.next()) {
				final Identifier storabeObjectId = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				EnumSet<E> linkedCodes = linkedCodesMap.get(storabeObjectId);
				if (linkedCodes == null) {
					linkedCodes = EnumSet.noneOf(enumClass);
					linkedCodesMap.put(storabeObjectId, linkedCodes);
				}
				try {
					linkedCodes.add(EnumUtil.valueOf(enumClass, resultSet.getInt(linkedCodeColumnName)));
				} catch (IllegalArgumentException iae) {
					Log.errorMessage(iae);
				}
			}

			return linkedCodesMap;
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve linked entity identifiers for entity -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	public static boolean isPresentInDatabase(final Identifier id) throws RetrieveObjectException {
		final String aliasCount = "count";
		final String tableName = ObjectEntities.codeToString(id.getMajor());
		final String sql = SQL_SELECT + SQL_COUNT + aliasCount
				+ SQL_FROM + tableName
				+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			resultSet.next();
			final int count = resultSet.getInt(aliasCount);
			return count > 0;
		}
		catch (SQLException sqle) {
			final String mesg = "Cannot check presence -- " + sqle.getMessage();
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
				Log.errorMessage(sqle1);
			}
			finally {
				if (connection != null) {
					DatabaseConnection.releaseConnection(connection);
				}
			}
		}
	}

	final Map<Identifier, StorableObjectVersion> retrieveVersions(final Set<Identifier> ids) throws RetrieveObjectException {
		assert StorableObject.hasSingleTypeEntities(ids) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;
		final String tableName = this.getEntityName();

		final Map<Identifier, StorableObjectVersion> versionsMap = new HashMap<Identifier, StorableObjectVersion>();

		final StringBuffer sql = new StringBuffer(SQL_SELECT);
		sql.append(StorableObjectWrapper.COLUMN_ID);
		sql.append(COMMA);
		sql.append(StorableObjectWrapper.COLUMN_VERSION);
		sql.append(SQL_FROM);
		sql.append(tableName);
		sql.append(SQL_WHERE);
		sql.append(idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, true));
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID);
				final long version = resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION);
				versionsMap.put(id, StorableObjectVersion.valueOf(version));
			}
		}
		catch (SQLException sqle) {
			final String mesg = "Cannot check presence -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}

		if (versionsMap.size() < ids.size()) {
			for (final Identifier id : ids) {
				if (!versionsMap.containsKey(id)) {
					versionsMap.put(id, StorableObjectVersion.ILLEGAL_VERSION);
				}
			}
		}
		return versionsMap;
	}

	protected void insert(final Set<T> storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	private void insertEntities(final Set<T> storableObjects)
			throws IllegalDataException,
				CreateObjectException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		assert !storableObjects.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(storableObjects) == this.getEntityCode() : ErrorMessages.ILLEGAL_ENTITY_CODE;

		final String sql = SQL_INSERT_INTO + this.getEntityName() + OPEN_BRACKET
				+ this.getColumns(ExecuteMode.MODE_INSERT)
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ this.getInsertMultipleSQLValues()
				+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		Connection connection = null;
		Identifier id = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			for (final T storableObject : storableObjects) {
				id = storableObject.getId();
				this.setEntityForPreparedStatement(storableObject, preparedStatement, ExecuteMode.MODE_INSERT);
				Log.debugMessage("Inserting  " + this.getEntityName() + " '" + id + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		} catch (SQLException sqle) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
			final String mesg = "Cannot insert " + this.getEntityName() + " '" + id + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	/**
	 * @param idLinkedObjectIdsMap
	 *            map of &lt;&lt;Identifier&gt; collectorId ,
	 *            List&lt;Identifier&gt; physicalLinkIds&gt;
	 * @throws CreateObjectException
	 */
	protected final void insertLinkedEntityIds(final Map<Identifier, Set<Identifier>> idLinkedObjectIdsMap,
			final String tableName,
			final String idColumnName,
			final String linkedIdColumnName) throws CreateObjectException {
		if (idLinkedObjectIdsMap == null || idLinkedObjectIdsMap.isEmpty()) {
			return;
		}

		final String sql = SQL_INSERT_INTO + tableName + OPEN_BRACKET
				+ idColumnName + COMMA
				+ linkedIdColumnName
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		Identifier id = null;
		Identifier linkedId = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (final Iterator<Identifier> idIt = idLinkedObjectIdsMap.keySet().iterator(); idIt.hasNext();) {
				id = idIt.next();
				final Set<Identifier> linkedIds = idLinkedObjectIdsMap.get(id);
				for (final Iterator<Identifier> linkedIdIt = linkedIds.iterator(); linkedIdIt.hasNext();) {
					linkedId = linkedIdIt.next();
					DatabaseIdentifier.setIdentifier(preparedStatement, 1, id);
					DatabaseIdentifier.setIdentifier(preparedStatement, 2, linkedId);
					Log.debugMessage("Inserting linked entity  '" + linkedId + "' for '" + id + "'", Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
				connection.commit();
			}
		} catch (SQLException sqle) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException sqle1) {
					Log.errorMessage(sqle1);
				}
			}
			final String mesg = "Cannot insert linked entity  '" + linkedId + "' for '" + id + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	/**
	 * 
	 * @param <E>
	 * @param idLinkedCodeMap
	 * @param tableName
	 * @param idColumnName
	 * @param linkedCodeColumnName
	 * @throws CreateObjectException
	 */
	protected final <E extends Enum<E>> void insertLinkedEnums(final Map<Identifier, EnumSet<E>> idLinkedCodeMap,
			final String tableName,
			final String idColumnName,
			final String linkedCodeColumnName) throws CreateObjectException {
		if (idLinkedCodeMap == null || idLinkedCodeMap.isEmpty()) {
			return;
		}

		final String sql = SQL_INSERT_INTO + tableName + OPEN_BRACKET
				+ idColumnName + COMMA
				+ linkedCodeColumnName
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		Identifier id = null;
		E linkedEnum = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (final Iterator<Identifier> idIt = idLinkedCodeMap.keySet().iterator(); idIt.hasNext();) {
				id = idIt.next();
				final EnumSet<E> linkedCodes = idLinkedCodeMap.get(id);
				for (final Iterator<E> linkedCodeIt = linkedCodes.iterator(); linkedCodeIt.hasNext();) {
					linkedEnum = linkedCodeIt.next();
					DatabaseIdentifier.setIdentifier(preparedStatement, 1, id);
					preparedStatement.setInt(2, linkedEnum.ordinal());
					Log.debugMessage("Inserting linked enum  '" + linkedEnum + "' for '" + id + "'", Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
				connection.commit();
			}
		} catch (SQLException sqle) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException sqle1) {
					Log.errorMessage(sqle1);
				}
			}
			final String mesg = "Cannot insert linked enum  '" + linkedEnum + "' for '" + id + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	// //////////////////// update /////////////////////////

	/**
	 * @param storableObjects
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 * @throws UpdateObjectException 
	 */
	public final void save(final Set<T> storableObjects)
			throws RetrieveObjectException,
				CreateObjectException,
				IllegalDataException,
				UpdateObjectException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		assert !storableObjects.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(storableObjects) == this.getEntityCode() : ErrorMessages.ILLEGAL_ENTITY_CODE;

		final Set<Identifier> ids = Identifier.createIdentifiers(storableObjects);
		final Set<Identifier> dbIds = this.retrieveIdentifiersByIdsByCondition(ids, null);

		final Set<T> updateStorableObjects = new HashSet<T>();
		final Set<T> insertStorableObjects = new HashSet<T>();
		for (final T storableObject : storableObjects) {
			if (dbIds.contains(storableObject.getId())) {
				updateStorableObjects.add(storableObject);
			} else {
				insertStorableObjects.add(storableObject);
			}
		}

		if (!insertStorableObjects.isEmpty()) {
			this.insert(insertStorableObjects);
		}
		if (!updateStorableObjects.isEmpty()) {
			this.update(updateStorableObjects);
		}
	}

	protected void update(final Set<T> storableObjects) throws UpdateObjectException {
		this.updateEntities(storableObjects);
	}

	private void updateEntities(final Set<T> storableObjects) throws UpdateObjectException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		assert !storableObjects.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(storableObjects) == this.getEntityCode() : ErrorMessages.ILLEGAL_ENTITY_CODE;

		final String[] cols = this.getColumns(ExecuteMode.MODE_UPDATE).split(COMMA);
		final String[] values = this.getUpdateMultipleSQLValues().split(COMMA);
		assert cols.length == values.length : this.getEntityName() + "Database.updateEntities | Count of columns ('" + cols.length
				+ "') is not equals count of values ('" + values.length + "')";

		final StringBuffer sql = new StringBuffer(SQL_UPDATE);
		sql.append(this.getEntityName());
		sql.append(SQL_SET);
		for (int i = 0; i < cols.length; i++) {
			if (cols[i].equals(StorableObjectWrapper.COLUMN_ID)) {
				continue;
			}
			sql.append(cols[i]);
			sql.append(EQUALS);
			sql.append(values[i]);
			if (i < cols.length - 1) {
				sql.append(COMMA);
			}
		}
		sql.append(SQL_WHERE);
		sql.append(StorableObjectWrapper.COLUMN_ID);
		sql.append(EQUALS);
		sql.append(QUESTION);

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Identifier id = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql.toString());
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			for (final T storableObject : storableObjects) {
				id = storableObject.getId();

				try {
					int i = this.setEntityForPreparedStatement(storableObject, preparedStatement, ExecuteMode.MODE_UPDATE);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getId());
				} catch (IllegalDataException ide) {
					try {
						connection.rollback();
					} catch (SQLException sqle1) {
						Log.errorMessage(sqle1);
					}
					throw new UpdateObjectException("Cannot set entity for prepared statement -- " + ide.getMessage(), ide);
				}

				Log.debugMessage("Updating " + this.getEntityName() + " '" + id + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
			final String mesg = "Cannot update " + this.getEntityName() + " '" + id + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
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
		if (idLinkedIdMap == null || idLinkedIdMap.isEmpty()) {
			return;
		}

		Map<Identifier, Set<Identifier>> dbLinkedObjIdsMap = null;
		try {
			dbLinkedObjIdsMap = this.retrieveLinkedEntityIds(idLinkedIdMap.keySet(), tableName, idColumnName, linkedIdColumnName);
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, Set<Identifier>> insertIdsMap = new HashMap<Identifier, Set<Identifier>>();
		final Map<Identifier, Set<Identifier>> deleteIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Identifier id : idLinkedIdMap.keySet()) {
			final Set<Identifier> linkedObjIds = idLinkedIdMap.get(id);
			final Set<Identifier> dbLinkedObjIds = dbLinkedObjIdsMap.get(id);

			if (dbLinkedObjIds != null) {

				// Prepare map for insertion
				for (final Identifier linkedObjId : linkedObjIds) {
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
				for (final Identifier linkedObjId : dbLinkedObjIds) {
					if (!linkedObjIds.contains(linkedObjId)) {
						Set<Identifier> alteringIds = deleteIdsMap.get(id);
						if (alteringIds == null) {
							alteringIds = new HashSet<Identifier>();
							deleteIdsMap.put(id, alteringIds);
						}
						alteringIds.add(linkedObjId);
					}
				}

				// Insert all linked ids for this id
			} else {
				Set<Identifier> alteringIds = insertIdsMap.get(id);
				if (alteringIds == null) {
					alteringIds = new HashSet<Identifier>();
					insertIdsMap.put(id, alteringIds);
				}
				alteringIds.addAll(linkedObjIds);
			}

		}

		this.deleteLinkedEntityIds(deleteIdsMap, tableName, idColumnName, linkedIdColumnName);
		try {
			this.insertLinkedEntityIds(insertIdsMap, tableName, idColumnName, linkedIdColumnName);
		} catch (CreateObjectException e) {
			throw new UpdateObjectException(e);
		}
	}

	/**
	 * 
	 * @param <E>
	 * @param idLinkedCodeMap
	 * @param enumClass
	 * @param tableName
	 * @param idColumnName
	 * @param linkedCodeColumnName
	 * @throws UpdateObjectException
	 */
	protected final <E extends Enum<E>> void updateLinkedEnums(final Map<Identifier, EnumSet<E>> idLinkedCodeMap,
			final Class<E> enumClass,
			final String tableName,
			final String idColumnName,
			final String linkedCodeColumnName) throws UpdateObjectException {
		if (idLinkedCodeMap == null || idLinkedCodeMap.isEmpty()) {
			return;
		}

		Map<Identifier, EnumSet<E>> dbIdLinkedCodeMap = null;
		try {
			dbIdLinkedCodeMap = this.retrieveLinkedEnums(idLinkedCodeMap.keySet(), enumClass, tableName, idColumnName, linkedCodeColumnName);
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, EnumSet<E>> insertCodesMap = new HashMap<Identifier, EnumSet<E>>();
		final Map<Identifier, EnumSet<E>> deleteCodesMap = new HashMap<Identifier, EnumSet<E>>();
		for (final Identifier id : idLinkedCodeMap.keySet()) {
			final EnumSet<E> codes = idLinkedCodeMap.get(id);
			final EnumSet<E> dbCodes = dbIdLinkedCodeMap.get(id);
			if (dbCodes != null) {

				//Prepare map for insertion
				for (final E code : codes) {
					if (!dbCodes.contains(code)) {
						EnumSet<E> altCodes = insertCodesMap.get(id);
						if (altCodes == null) {
							altCodes = EnumSet.noneOf(enumClass);
							insertCodesMap.put(id, altCodes);
						}
						altCodes.add(code);
					}
				}

				//Prepare map for delete
				for (final E code :dbCodes) {
					if (!codes.contains(code)) {
						EnumSet<E> altCodes = deleteCodesMap.get(id);
						if (altCodes == null) {
							altCodes = EnumSet.noneOf(enumClass);
							deleteCodesMap.put(id, altCodes);
						}
						altCodes.add(code);
					}
				}

				// Insert all linked codes for this id
			} else {
				EnumSet<E> altCodes = insertCodesMap.get(id);
				if (altCodes == null) {
					altCodes = EnumSet.noneOf(enumClass);
					insertCodesMap.put(id, altCodes);
				}
				altCodes.addAll(codes);
			}

		}

		this.deleteLinkedEnums(deleteCodesMap, tableName, idColumnName, linkedCodeColumnName);
		try {
			this.insertLinkedEnums(insertCodesMap, tableName, idColumnName, linkedCodeColumnName);
		} catch (CreateObjectException e) {
			throw new UpdateObjectException(e);
		}
	}


	// ////////////////////refresh /////////////////////////

	public final Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap)
			throws RetrieveObjectException {
		final Set<Identifier> ids = versionsMap.keySet();
		final Map<Identifier, StorableObjectVersion> dbVersionsMap = this.retrieveVersions(ids);
		final Set<Identifier> olderVersionIds = new HashSet<Identifier>();
		for (final Identifier id : ids) {
			final StorableObjectVersion version = versionsMap.get(id);
			final StorableObjectVersion dbVersion = dbVersionsMap.get(id);
			if (version.isOlder(dbVersion)) {
				olderVersionIds.add(id);
			}
		}
		return olderVersionIds;
	}


	// //////////////////// delete /////////////////////////

	public void delete(Set<? extends Identifiable> identifiables) {
		if ((identifiables == null) || (identifiables.isEmpty())) {
			return;
		}

		final StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM);
		stringBuffer.append(this.getEntityName());
		stringBuffer.append(SQL_WHERE);
		stringBuffer.append(idsEnumerationString(identifiables, StorableObjectWrapper.COLUMN_ID, true));

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + stringBuffer, Log.DEBUGLEVEL09);
			statement.executeUpdate(stringBuffer.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorMessage(sqle1);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	protected final void deleteLinkedEntityIds(final Map<Identifier, Set<Identifier>> idLinkedObjectIdsMap,
			final String tableName,
			final String idColumnName,
			final String linkedIdColumnName) {
		if (idLinkedObjectIdsMap == null || idLinkedObjectIdsMap.isEmpty()) {
			return;
		}

		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM);
		sql.append(tableName);
		sql.append(SQL_WHERE);
		sql.append(DatabaseStorableObjectCondition.FALSE_CONDITION);

		for (final Identifier id : idLinkedObjectIdsMap.keySet()) {
			final Set<Identifier> linkedObjIds = idLinkedObjectIdsMap.get(id);

			sql.append(SQL_OR);
			sql.append(OPEN_BRACKET);
				sql.append(idColumnName);
				sql.append(EQUALS);
				sql.append(DatabaseIdentifier.toSQLString(id));
				sql.append(SQL_AND);
				sql.append(OPEN_BRACKET);
					sql.append(idsEnumerationString(linkedObjIds, linkedIdColumnName, true));
				sql.append(CLOSE_BRACKET);
			sql.append(CLOSE_BRACKET);
		}

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorMessage(sqle1);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	/**
	 * 
	 * @param <E>
	 * @param idLinkedCodeMap
	 * @param tableName
	 * @param idColumnName
	 * @param linkedCodeColumnName
	 */
	private final <E extends Enum<E>> void deleteLinkedEnums(final Map<Identifier, EnumSet<E>> idLinkedCodeMap,
			final String tableName,
			final String idColumnName,
			final String linkedCodeColumnName) {
		if (idLinkedCodeMap == null || idLinkedCodeMap.isEmpty()) {
			return;
		}

		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM);
		sql.append(tableName);
		sql.append(SQL_WHERE);
		sql.append(DatabaseStorableObjectCondition.FALSE_CONDITION);

		for (final Identifier id : idLinkedCodeMap.keySet()) {
			final EnumSet<E> linkedCodes = idLinkedCodeMap.get(id);

			sql.append(SQL_OR);
			sql.append(OPEN_BRACKET);
				sql.append(idColumnName);
				sql.append(EQUALS);
				sql.append(DatabaseIdentifier.toSQLString(id));
				sql.append(SQL_AND);
				sql.append(OPEN_BRACKET);
					sql.append(enumsEnumerationString(linkedCodes, linkedCodeColumnName, true));
				sql.append(CLOSE_BRACKET);
			sql.append(CLOSE_BRACKET);
		}

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorMessage(sqle1);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	// //////////////////// misc /////////////////////////

	/**
	 * <p>If <code>inList</code> is <code>true</code> returns a string like
	 * "idColumn IN ('id1', 'id2',... , 'idN') OR idColumn IN ('id3', 'id4',... ,
	 * 'idM') ..." If <code>inList</code> is <code>false</code> returns a
	 * string like "idColumn NOT IN ('id1', 'id2',... , 'idN') AND idColumn NOT
	 * IN ('id3', 'id4',... , 'idM') ..."</p>
	 * 
	 * <p>If {@code identifiables} contain
	 * {@link Identifier#VOID_IDENTIFIER VOID_IDENTIFIER}, it is handled
	 * correctly, e. g.:<pre>
	 * idColumn IN ('id1', 'id2', ..., 'isN') OR idColumn IS NULL
	 * idColumn NOT IN ('id1', 'id2', ..., 'isN') AND idColumn IS NOT NULL
	 * </pre></p>
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

		final StringBuffer voidSql = new StringBuffer(OPEN_BRACKET);
		voidSql.append(idColumn);
		voidSql.append(SQL_IS);
		voidSql.append(inList ? "" : NOT);
		voidSql.append(SQL_NULL);
		voidSql.append(CLOSE_BRACKET);
		if (nonVoidIdentifiers.isEmpty()) {
			if (containsVoidIdentifier) {
				return voidSql;
			}
			return new StringBuffer(inList
					? DatabaseStorableObjectCondition.FALSE_CONDITION
					: DatabaseStorableObjectCondition.TRUE_CONDITION);
		}

		final StringBuffer sql = new StringBuffer((containsVoidIdentifier ? OPEN_BRACKET : ""));
		sql.append(OPEN_BRACKET);
		sql.append(idColumn);
		sql.append(inList ? SQL_IN : SQL_NOT_IN);
		sql.append(OPEN_BRACKET);

		/*-
		 * The following block has been rewritten to utilize the new
		 * for-each loop. Everyone, do not roll back. Arseniy, I address
		 * you in person: do not ever roll back.
		 *
		 * Bass.
		 */
		int i = 0;
		final int nonVoidIdentifiersSize = nonVoidIdentifiers.size();
		for (final Identifier id : nonVoidIdentifiers) {
			sql.append(DatabaseIdentifier.toSQLString(id));
			if (++i < nonVoidIdentifiersSize) {
				if (i % MAXIMUM_EXPRESSION_NUMBER == 0) {
					sql.append(CLOSE_BRACKET);
					sql.append(inList ? SQL_OR : SQL_AND);
					sql.append(idColumn);
					sql.append(inList ? SQL_IN : SQL_NOT_IN);
					sql.append(OPEN_BRACKET);
				} else {
					sql.append(COMMA);
				}
			}
		}
		sql.append(CLOSE_BRACKET);
		sql.append(CLOSE_BRACKET);

		if (containsVoidIdentifier) {
			sql.append((inList ? SQL_OR : SQL_AND));
			voidSql.append(voidSql);
			voidSql.append(CLOSE_BRACKET);
		}

		return sql;
	}

	/**
	 * Supports only non-null codes
	 * @param enums
	 * @param codeColumn
	 * @param inList
	 * @return String for "WHERE" subclause of SQL query
	 */
	protected final <E extends Enum<E>> StringBuffer enumsEnumerationString(final EnumSet<E> enums,
			final String codeColumn,
			final boolean inList) {
		final StringBuffer stringBuffer = new StringBuffer(OPEN_BRACKET);
		stringBuffer.append(codeColumn);
		stringBuffer.append(inList ? SQL_IN : SQL_NOT_IN);
		stringBuffer.append(OPEN_BRACKET);
		int i = 0;
		for (final Iterator<E> it = enums.iterator(); it.hasNext(); i++) {
			final E code = it.next();
			stringBuffer.append(code.ordinal());
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0)) {
					stringBuffer.append(COMMA);
				} else {
					stringBuffer.append(CLOSE_BRACKET);
					stringBuffer.append(inList ? SQL_OR : SQL_AND);
					stringBuffer.append(codeColumn);
					stringBuffer.append((inList ? SQL_IN : SQL_NOT_IN));
					stringBuffer.append(OPEN_BRACKET);
				}
			}
		}
		stringBuffer.append(CLOSE_BRACKET);
		stringBuffer.append(CLOSE_BRACKET);
		return stringBuffer;
	}

	private String getConditionQuery(final StorableObjectCondition condition) {
		final DatabaseStorableObjectCondition databaseStorableObjectCondition = this.reflectDatabaseCondition(condition);
		if (databaseStorableObjectCondition == null) {
			return DatabaseStorableObjectCondition.FALSE_CONDITION;
		}

		final short conditionCode = databaseStorableObjectCondition.getEntityCode().shortValue();
		assert (this.checkEntity(conditionCode)) : "Incompatible condition ("
					+ ObjectEntities.codeToString(conditionCode) + ") and database (" + this.getEntityName() + ") classes";

		String conditionQuery;
		try {
			conditionQuery = databaseStorableObjectCondition.getSQLQuery();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorMessage(ioee);
			conditionQuery = DatabaseStorableObjectCondition.FALSE_CONDITION;
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
			final Class<?> clazz = Class.forName(dbClassName);
			final Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] {condition.getClass()});
			constructor.setAccessible(true);
			databaseStorableObjectCondition = (DatabaseStorableObjectCondition) constructor.newInstance(new Object[] {condition});
		} catch (ClassNotFoundException e) {
			Log.errorMessage(e);
		} catch (SecurityException e) {
			Log.errorMessage(e);
		} catch (NoSuchMethodException e) {
			Log.errorMessage(e);
		} catch (IllegalArgumentException e) {
			Log.errorMessage(e);
		} catch (InstantiationException e) {
			Log.errorMessage(e);
		} catch (IllegalAccessException e) {
			Log.errorMessage(e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null) {
					assert false;
				} else {
					assert false : message;
				}
			} else {
				Log.errorMessage(e);
			}
		}
		return databaseStorableObjectCondition;
	}

}
