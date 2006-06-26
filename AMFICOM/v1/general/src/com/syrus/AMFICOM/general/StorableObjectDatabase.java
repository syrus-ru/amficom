/*-
 * $Id: StorableObjectDatabase.java,v 1.212 2006/06/26 17:24:45 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.DatabaseStorableObjectCondition.FALSE_CONDITION;
import static com.syrus.AMFICOM.general.DatabaseStorableObjectCondition.TRUE_CONDITION;
import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

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
 * Драйвер для хранения в базе данных объектов {@link StorableObject}.
 * <p>
 * Для каждой реализации {@link StorableObject} необходимо написать свою
 * реализацию данного абстрактного класса.
 * <p>
 * Предпочтительный уровень отладочных сообщений: 9
 * 
 * @version $Revision: 1.212 $, $Date: 2006/06/26 17:24:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
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
	public static final String SQL_FUNCTION_EMPTY_BLOB = " EMPTY_BLOB() ";

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
	public static final String SQL_IS = " IS ";

	protected enum ExecuteMode {
		MODE_INSERT, MODE_UPDATE
	}

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


	// /////////////////////////////////////// Common ///////////////////////////////////////////

	/**
	 * Получить код сущности.
	 */
	protected abstract short getEntityCode();

	/**
	 * Получить имя сущности.
	 * 
	 * @return Имя сущности.
	 */
	protected final String getEntityName() {
		return ObjectEntities.codeToString(this.getEntityCode());
	}

	/**
	 * Получить перечисление через запятую столбцов таблицы, свойственных только
	 * для данной сущности. Столбцы, присущие всем объектам
	 * {@link StorableObject} (а именно -
	 * {@link StorableObjectWrapper#COLUMN_ID},
	 * {@link StorableObjectWrapper#COLUMN_CREATED},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIED},
	 * {@link StorableObjectWrapper#COLUMN_CREATOR_ID},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIER_ID},
	 * {@link StorableObjectWrapper#COLUMN_VERSION}) в этом списке не
	 * присутствуют.
	 * 
	 * @see #getColumns(com.syrus.AMFICOM.general.StorableObjectDatabase.ExecuteMode).
	 */
	protected abstract String getColumnsTmpl();

	/**
	 * Получить перечисление через запятую столбцов таблицы.
	 * 
	 * @param mode
	 *        Если {@link ExecuteMode#MODE_INSERT}, то в списке присутствуют
	 *        все столбцы, включая столбец идентификатора
	 *        {@link StorableObjectWrapper#COLUMN_ID}.
	 *        <p>
	 *        Если {@link ExecuteMode#MODE_UPDATE}, то в списке не присутствует
	 *        столбец идентификатора {@link StorableObjectWrapper#COLUMN_ID},
	 *        т. к. он должен быть указан в SQL-предложении после служебного
	 *        слова WHERE.
	 * @return Перечисление через запятую столбцов таблицы.
	 * @see #retrieveQuery(String)
	 * @see #insertEntities(Set)
	 * @see #updateEntities(Set)
	 */
	protected final String getColumns(final ExecuteMode mode) {
		if (columns == null) {
			columns = COLUMN_CREATED + COMMA
					+ COLUMN_MODIFIED + COMMA
					+ COLUMN_CREATOR_ID + COMMA
					+ COLUMN_MODIFIER_ID + COMMA
					+ COLUMN_VERSION + COMMA;
		}
		switch (mode) {
			case MODE_INSERT:
				return COLUMN_ID + COMMA + columns + this.getColumnsTmpl();
			case MODE_UPDATE:
				return columns + this.getColumnsTmpl();
			default:
				Log.errorMessage("Unknown mode: " + mode);
				return null;
		}
	}

	/**
	 * Получить вторую часть SQL-предложения для использования с
	 * {@link PreparedStatement}. В этой части содержатся вопросительные знаки,
	 * разделённые запятыми, по знаку на каждый столбец.
	 * 
	 * @see #insertEntities(Set)
	 */
	protected final String getInsertMultipleSQLValues() {
		return QUESTION + COMMA + this.getUpdateMultipleSQLValues();
	}

	/**
	 * Получить вторую часть SQL-предложения для использования с
	 * {@link PreparedStatement} для столбцов таблицы, свойственных только для
	 * данной сущности. Столбцы, присущие всем объектам {@link StorableObject}
	 * (а именно - {@link StorableObjectWrapper#COLUMN_ID},
	 * {@link StorableObjectWrapper#COLUMN_CREATED},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIED},
	 * {@link StorableObjectWrapper#COLUMN_CREATOR_ID},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIER_ID},
	 * {@link StorableObjectWrapper#COLUMN_VERSION}) в этом списке не
	 * присутствуют.
	 * 
	 * @see #getUpdateMultipleSQLValues().
	 */
	protected abstract String getUpdateMultipleSQLValuesTmpl();

	/**
	 * Получить вторую часть SQL-предложения для использования с
	 * {@link PreparedStatement}. В этой части содержатся вопросительные знаки,
	 * разделённые запятыми, по знаку на каждый столбец. Столбец
	 * {@link StorableObjectWrapper#COLUMN_ID} в этом перечислении отсутствует.
	 * 
	 * @see #updateEntities(Set)
	 * @see #getInsertMultipleSQLValues()
	 */
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

	/**
	 * Получить вторую часть SQL-предложения для использования с
	 * {@link Statement} для столбцов таблицы, свойственных только для данной
	 * сущности. Столбцы, присущие всем объектам {@link StorableObject} (а
	 * именно - {@link StorableObjectWrapper#COLUMN_ID},
	 * {@link StorableObjectWrapper#COLUMN_CREATED},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIED},
	 * {@link StorableObjectWrapper#COLUMN_CREATOR_ID},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIER_ID},
	 * {@link StorableObjectWrapper#COLUMN_VERSION}) в этом списке не
	 * присутствуют.
	 * 
	 * @see #getUpdateSingleSQLValues(StorableObject,
	 *      com.syrus.AMFICOM.general.StorableObjectDatabase.ExecuteMode).
	 */
	protected abstract String getUpdateSingleSQLValuesTmpl(T storableObject) throws IllegalDataException;

	/**
	 * Получить вторую часть SQL-предложения для использования с
	 * {@link Statement}. В этой части содержатся значения полей, перечисленные
	 * через запятую.
	 * <p>
	 * Этот метод сейчас уже нигде не используется, но ещё может быть полезен.
	 * 
	 * @param storableObject
	 * @param mode
	 *        Если {@link ExecuteMode#MODE_INSERT}, то в списке присутствуют
	 *        значения для всех столбцов, включая столбец идентификатора
	 *        {@link StorableObjectWrapper#COLUMN_ID}.
	 *        <p>
	 *        Если {@link ExecuteMode#MODE_UPDATE}, то в списке не присутствует
	 *        значение столбца идентификатора
	 *        {@link StorableObjectWrapper#COLUMN_ID}, т. к. он должен быть
	 *        указан в SQL-предложении после служебного слова WHERE.
	 * @throws IllegalDataException
	 */
	protected final String getUpdateSingleSQLValues(final T storableObject, final ExecuteMode mode) throws IllegalDataException {
		final String modeString;
		switch (mode) {
			case MODE_INSERT:
				modeString = DatabaseIdentifier.toSQLString(storableObject.getId()) + COMMA;
				break;
			case MODE_UPDATE:
				modeString = "";
				break;
			default:
				throw new IllegalDataException(("Unknown mode: " + mode));

		}
		return modeString
				+ DatabaseDate.toUpdateSubString(storableObject.getCreated()) + COMMA
				+ DatabaseDate.toUpdateSubString(storableObject.getModified()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getCreatorId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getModifierId()) + COMMA
				+ Long.toString(storableObject.getVersion().longValue()) + COMMA
				+ this.getUpdateSingleSQLValuesTmpl(storableObject);
	}

	/**
	 * Этот метод строит запрос SQL для условия, выраженного строкой
	 * {@code condition}. По этому запросу достаются все поля из таблицы для
	 * строк, удовлетворяющих заданному условию.
	 * 
	 * @param condition
	 *        Строка условия, которая в SQL-предложении ставится после
	 *        служебного слова WHERE. Если {@code null}, то строится запрос,
	 *        возвращающий все строки таблицы.
	 * @return Строка запроса SQL с условием {@code condition}.
	 * @see #retrieveByCondition(String)
	 */
	protected String retrieveQuery(final String condition) {
		final StringBuffer buffer;
		if (this.retrieveQuery == null) {
			buffer = new StringBuffer(SQL_SELECT);
			String cols = this.getColumns(ExecuteMode.MODE_INSERT);
			cols = cols.replaceFirst(COLUMN_CREATED, DatabaseDate.toQuerySubString(COLUMN_CREATED));
			cols = cols.replaceFirst(COLUMN_MODIFIED, DatabaseDate.toQuerySubString(COLUMN_MODIFIED));
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

	/**
	 * Метод заполняет поля приготовленного запроса {@code preparedStatement}
	 * значениями полей объекта {@code storableObject}. Заполнение происходит
	 * только для полей {@code storableObject}, присущих данной реализации
	 * {@link StorableObject}. Поля , присущие всем объектам
	 * {@link StorableObject} (а именно -
	 * {@link StorableObjectWrapper#COLUMN_ID},
	 * {@link StorableObjectWrapper#COLUMN_CREATED},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIED},
	 * {@link StorableObjectWrapper#COLUMN_CREATOR_ID},
	 * {@link StorableObjectWrapper#COLUMN_MODIFIER_ID},
	 * {@link StorableObjectWrapper#COLUMN_VERSION}) не заполняются.
	 * 
	 * @param storableObject
	 *        Объект, вставляемый в БД, либо обновляемый в ней.
	 * @param preparedStatement
	 *        Приготовленный запрос вставки или обновления.
	 * @param startParameterNumber
	 *        Номер последнего проставленного поля в запросе
	 *        {@code preparedStatement} перед началом работы метода.
	 * @return Номер последнего проставленного поля в запросе
	 *         {@code preparedStatement} после окончания работы метода.
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see #setEntityForPreparedStatement(StorableObject, PreparedStatement,
	 *      com.syrus.AMFICOM.general.StorableObjectDatabase.ExecuteMode).
	 */
	protected abstract int setEntityForPreparedStatementTmpl(T storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException;

	/**
	 * Метод заполняет поля приготовленного запроса {@code preparedStatement}
	 * значениями полей объекта {@code storableObject}.
	 * 
	 * @param storableObject
	 *        Объект, вставляемый в БД, либо обновляемый в ней.
	 * @param preparedStatement
	 *        Приготовленный запрос вставки или обновления.
	 * @param mode
	 *        Если {@link ExecuteMode#MODE_INSERT}, то заполняются все столбцы,
	 *        включая столбец идентификатора
	 *        {@link StorableObjectWrapper#COLUMN_ID}.
	 *        <p>
	 *        Если {@link ExecuteMode#MODE_UPDATE}, то столбец идентификатора
	 *        {@link StorableObjectWrapper#COLUMN_ID} не заполняется, т. к. он
	 *        должен быть указан в SQL-предложении после служебного слова WHERE.
	 * @return Номер текущего параметра в запросе {@code preparedStatement}.
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see #insertEntities(Set)
	 * @see #updateEntities(Set)
	 */
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
				throw new IllegalDataException("Unknown mode: " + mode);
		}
		preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getCreated().getTime()));
		preparedStatement.setTimestamp(++i, new Timestamp(storableObject.getModified().getTime()));
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getCreatorId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, storableObject.getModifierId());
		preparedStatement.setLong(++i, storableObject.getVersion().longValue());
		return this.setEntityForPreparedStatementTmpl(storableObject, preparedStatement, i);
	}

	/**
	 * Если {@code storableObject == null}, то создаётся новый объект с полями,
	 * проставленными из {@code resultSet}. Иначе - все поля объекта
	 * {@code storableObject} заполняются значениями из {@code resultSet}.
	 * 
	 * @param storableObject
	 *        Объект, заполняемый значениями из БД.
	 * @param resultSet
	 *        Выборка из БД для объекта {@code storableObject}
	 * @return Объект с полями, заполненными значениями из выборки
	 *         {@code resultSet}.
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 * @see {@link #retrieveByCondition(String)}
	 */
	protected abstract T updateEntityFromResultSet(T storableObject, ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException;


	// //////////////////////////////////////////// Retrieve Objects /////////////////////////////////////////////

	/**
	 * Достать объекты по данному условию.
	 * <p>
	 * Этот метод можно переопределять в подклассах, если требуется
	 * нестандартное заполнение полей объекта (например доставание из БД
	 * связанных идентификаторов).
	 * 
	 * @param conditionQuery
	 *        Строка условия.
	 * @return Набор объектов, удовлетворяющих данному условию.
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 */
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

	/**
	 * Достать объекты по данному условию.
	 * 
	 * @param condition
	 * @return Набор объектов, удовлетворяющих данному условию.
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 */
	public final Set<T> retrieveByCondition(final StorableObjectCondition condition)
			throws RetrieveObjectException,
				IllegalDataException {
		return this.retrieveByCondition(this.getConditionQuery(condition));
	}

	/**
	 * Достать объекты по данному условию, идентификаторы которых не принадлежат
	 * набору {@code ids}.
	 * 
	 * @param ids
	 * @param condition
	 * @return Набор объектов, удовлетворяющих данному условию, за исключением
	 *         {@code ids}.
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 */
	public final Set<T> retrieveButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition)
			throws RetrieveObjectException,
				IllegalDataException {
		assert StorableObject.hasSingleTypeEntities(ids) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, COLUMN_ID, false);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveByCondition(stringBuffer.toString());
	}

	/**
	 * Достать объекты по данному условию, идентификаторы которых принадлежат
	 * набору {@code ids}.
	 * 
	 * @param ids
	 * @param condition
	 * @return Набор объектов, удовлетворяющих данному условию, принадлежащих
	 *         набору {@code ids}.
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 */
	public final Set<T> retrieveByIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition)
			throws RetrieveObjectException,
				IllegalDataException {
		assert StorableObject.hasSingleTypeEntities(ids) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, COLUMN_ID, true);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveByCondition(stringBuffer.toString());
	}

	/**
	 * Достать объект с данным идентификатором.
	 * 
	 * @param id
	 * @return Объект с данным идентификатором.
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 *         Если не найден.
	 */
	public final T retrieveForId(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		assert id != null : NON_NULL_EXPECTED;
		assert id.getMajor() == this.getEntityCode() : ILLEGAL_ENTITY_CODE;

		final String condition = COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(id);
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

	/**
	 * Достать все объекты.
	 * 
	 * @return Набор всех объектов, хранящихся в БД.
	 * @throws RetrieveObjectException
	 */
	public final Set<T> retrieveAll() throws RetrieveObjectException {
		try {
			return this.retrieveByCondition((String) null);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}


	// //////////////////////////////////////// Retrieve Identifiers /////////////////////////////////////////////

	public final Set<Identifier> retrieveIdentifiersByCondition(final String conditionQuery) throws RetrieveObjectException {
		final Set<Identifier> identifiers = new HashSet<Identifier>();
		final String tableName = this.getEntityName();

		final StringBuffer sql = new StringBuffer(SQL_SELECT);
		sql.append(COLUMN_ID);
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
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
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

	public final Set<Identifier> retrieveIdentifiersByCondition(final StorableObjectCondition condition)
			throws RetrieveObjectException {
		return this.retrieveIdentifiersByCondition(this.getConditionQuery(condition));
	}

	public final Set<Identifier> retrieveIdentifiersButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws RetrieveObjectException {
		assert StorableObject.hasSingleTypeEntities(ids) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, COLUMN_ID, false);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveIdentifiersByCondition(stringBuffer.toString());
	}

	public final Set<Identifier> retrieveIdentifiersByIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws RetrieveObjectException {
		assert StorableObject.hasSingleTypeEntities(ids) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StringBuffer stringBuffer = idsEnumerationString(ids, COLUMN_ID, true);

		if (condition != null) {
			stringBuffer.append(SQL_AND);
			stringBuffer.append(OPEN_BRACKET);
			stringBuffer.append(this.getConditionQuery(condition));
			stringBuffer.append(CLOSE_BRACKET);
		}

		return this.retrieveIdentifiersByCondition(stringBuffer.toString());
	}


	// //////////////////////////////////////// Retrieve linked objects /////////////////////////////////////////////

	/**
	 * Достать идентификаторы объектов, связанных через промежуточную таблицу
	 * {@code tableName} с объектами из набора {@code identifiables}.
	 * <p>
	 * Этот метод может быть использован в наследниках данного класса, в
	 * переопределении метода {@link #retrieveByCondition(String)}, для
	 * восстановления полей-связанных идентификаторов каждого объекта из набора
	 * {@code identifiables}.
	 * 
	 * @param identifiables
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 * @return Карту вида <{@link Identifier} идентификатор_объекта,
	 *         {@link Set}<{@link Identifier}>
	 *         набор_идентификаторов_связанных_объектов>.
	 * @throws RetrieveObjectException
	 */
	protected final Map<Identifier, Set<Identifier>> retrieveLinkedEntityIds(final Set<? extends Identifiable> identifiables,
			final String tableName,
			final String idColumnName,
			final String linkedIdColumnName) throws RetrieveObjectException {
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
	 * Достать перечислимые объекты, связанные через промежуточную таблицу
	 * {@code tableName} с объектами {@code identifiables}.
	 * <p>
	 * Этот метод может быть использован в наследниках данного класса, в
	 * переопределении метода {@link #retrieveByCondition(String)}, для
	 * восстановления полей-перечислимых объектов каждого объекта из набора
	 * {@code identifiables}.
	 * <p>
	 * В настоящее время нигде не используется, но может быть полезен.
	 * 
	 * @param <E>
	 * @param identifiables
	 * @param enumClass
	 * @param tableName
	 * @param idColumnName
	 * @param linkedCodeColumnName
	 * @return Карту вида <{@link Identifier} идентификатор_объекта,
	 *         {@link EnumSet}<E> набор_связанных_перечислимых_объектов>.
	 * @throws RetrieveObjectException
	 */
	protected final <E extends Enum<E>> Map<Identifier, EnumSet<E>> retrieveLinkedEnums(final Set<? extends Identifiable> identifiables,
			final Class<E> enumClass,
			final String tableName,
			final String idColumnName,
			final String linkedCodeColumnName) throws RetrieveObjectException {
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

	/**
	 * Проверить, существует ли объект с данным идентификатором в базе данных.
	 * 
	 * @param id
	 *        Идентификатор объекта.
	 * @return {@code true}, если существует.
	 * @throws RetrieveObjectException
	 */
	public final boolean isPresentInDatabase(final Identifier id) throws RetrieveObjectException {
		final String aliasCount = "count";
		final String tableName = this.getEntityName();
		final String sql = SQL_SELECT + SQL_COUNT + aliasCount
				+ SQL_FROM + tableName
				+ SQL_WHERE + COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(id);
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
			assert count <= 1 : ONLY_ONE_EXPECTED;
			return count > 0;
		} catch (SQLException sqle) {
			final String mesg = "Cannot check presence -- " + sqle.getMessage();
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
	 * Статический вариант метода {@link #isPresentInDatabase(Identifier)}.
	 * 
	 * @param id
	 * @return {@code true}, если существует.
	 * @throws RetrieveObjectException
	 * @throws IllegalObjectEntityException
	 */
	public static boolean isObjectPresentInDatabase(final Identifier id)
			throws RetrieveObjectException,
				IllegalObjectEntityException {
		final short entityCode = id.getMajor();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<?> storableObjectDatabase = DatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase == null) {
			throw new IllegalObjectEntityException("Cannot find database for code " + entityCode,
					IllegalObjectEntityException.UNKNOWN_ENTITY_CODE);
		}
		return storableObjectDatabase.isPresentInDatabase(id);
	}

	/**
	 * Создать карту версий, т. е. - карту, где по ключу - идентификатор
	 * объекта, а по величине - его версия. Если объект не найден в БД, то в
	 * качестве его версии в карту помещается
	 * {@link StorableObjectVersion#ILLEGAL_VERSION}.
	 * 
	 * @param ids
	 *        Идентификаторы объектов. Должны быть одной сущности.
	 * @return Карта версий для объектов {@code ids}.
	 * @throws RetrieveObjectException
	 *         Ошибка при выборке из БД.
	 */
	public final Map<Identifier, StorableObjectVersion> retrieveVersions(final Set<Identifier> ids)
			throws RetrieveObjectException {
		assert StorableObject.hasSingleTypeEntities(ids) : OBJECTS_NOT_OF_THE_SAME_ENTITY;
		final String tableName = this.getEntityName();

		final Map<Identifier, StorableObjectVersion> versionsMap = new HashMap<Identifier, StorableObjectVersion>();

		final StringBuffer sql = new StringBuffer(SQL_SELECT);
		sql.append(COLUMN_ID);
		sql.append(COMMA);
		sql.append(COLUMN_VERSION);
		sql.append(SQL_FROM);
		sql.append(tableName);
		sql.append(SQL_WHERE);
		sql.append(idsEnumerationString(ids, COLUMN_ID, true));
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				final long version = resultSet.getLong(COLUMN_VERSION);
				versionsMap.put(id, StorableObjectVersion.valueOf(version));
			}
		} catch (SQLException sqle) {
			final String mesg = "Cannot check presence -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
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
					versionsMap.put(id, ILLEGAL_VERSION);
				}
			}
		}
		return versionsMap;
	}

	/**
	 * Вставить в БД объекты {@code storableObjects}.
	 * <p>
	 * Этот метод можно переопределить в наследниках для реализации более
	 * сложного поведения, чем просто вставление всех полей каждого объекта в
	 * таблицу (как это делает {@link #insertEntities(Set)}). Например, может
	 * возникнуть необходимость вставить в промежуточные таблицы-связки
	 * связанные идентификаторы.
	 * 
	 * @param storableObjects
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	protected void insert(final Set<T> storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	/**
	 * Вставить в БД объекты {@code storableObjects}.
	 * 
	 * @param storableObjects
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	private void insertEntities(final Set<T> storableObjects) throws IllegalDataException, CreateObjectException {
		assert storableObjects != null : NON_NULL_EXPECTED;
		assert !storableObjects.isEmpty() : NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(storableObjects) == this.getEntityCode() : ILLEGAL_ENTITY_CODE;

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
	 * Вставить в БД связанные идентификаторы.
	 * 
	 * @param idLinkedObjectIdsMap
	 *        Карта, где по ключу хранится идентификатор объекта, а по величине -
	 *        набор идентификаторов связанных с ним объектов.
	 * @param tableName
	 *        Проммежуточная таблица-связка.
	 * @param idColumnName
	 *        Столбец в таблице {@code tableName}, где хранятся идентификаторы
	 *        объектов.
	 * @param linkedIdColumnName
	 *        Столбец в таблице {@code tableName}, где хранятся идентификаторы
	 *        связанных объектов.
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
					Log.debugMessage("Inserting linked entity  '" + linkedId + "' for '" + id + "' into " + tableName,
							Log.DEBUGLEVEL09);
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
	 * Вставить в БД связанные перечислимые объекты.
	 * <p>
	 * Этот метод может быть использован в наследниках данного класса, в
	 * переопределении метода {@link #insert(Set)}, для вставки в БД
	 * полей-перечислимых объектов каждого объекта из набора
	 * {@code identifiables}.
	 * <p>
	 * В настоящее время нигде не используется, но может быть полезен.
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
	 * Сохранить объекты {@code storableObjects}.
	 * <p>
	 * Если объект из набора {@code storableObjects} не ещё существует в БД, то
	 * для него вызывается {@link #insert(Set)}. Если же он уже сохранён в БД,
	 * то для него вызывается {@link #update(Set)}.
	 * 
	 * @param storableObjects
	 * @throws RetrieveObjectException
	 * @throws CreateObjectException
	 * @throws IllegalDataException
	 * @throws UpdateObjectException
	 * @see #insert(Set)
	 * @see #update(Set)
	 */
	public final void save(final Set<T> storableObjects)
			throws RetrieveObjectException,
				CreateObjectException,
				IllegalDataException,
				UpdateObjectException {
		assert storableObjects != null : NON_NULL_EXPECTED;
		assert !storableObjects.isEmpty() : NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(storableObjects) == this.getEntityCode() : ILLEGAL_ENTITY_CODE;

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

	/**
	 * Обновить в БД объекты {@code storableObjects}.
	 * <p>
	 * Этот метод можно переопределить в наследниках для реализации более
	 * сложного поведения, чем просто вставление всех полей каждого объекта в
	 * таблицу (как это делает {@link #updateEntities(Set)}). Например, может
	 * возникнуть необходимость обновить в промежуточных таблицах-связках
	 * связанные идентификаторы.
	 * 
	 * @param storableObjects
	 * @throws UpdateObjectException
	 */
	protected void update(final Set<T> storableObjects) throws UpdateObjectException {
		this.updateEntities(storableObjects);
	}

	/**
	 * Обновить в БД объекты {@code storableObjects}.
	 * 
	 * @param storableObjects
	 * @throws UpdateObjectException
	 */
	private void updateEntities(final Set<T> storableObjects) throws UpdateObjectException {
		assert storableObjects != null : NON_NULL_EXPECTED;
		assert !storableObjects.isEmpty() : NON_EMPTY_EXPECTED;
		assert StorableObject.getEntityCodeOfIdentifiables(storableObjects) == this.getEntityCode() : ILLEGAL_ENTITY_CODE;

		final String[] cols = this.getColumns(ExecuteMode.MODE_UPDATE).split(COMMA);
		final String[] values = this.getUpdateMultipleSQLValues().split(COMMA);
		assert cols.length == values.length : this.getEntityName() + "Database.updateEntities | Count of columns ('" + cols.length
				+ "') is not equals count of values ('" + values.length + "')";

		final StringBuffer sql = new StringBuffer(SQL_UPDATE);
		sql.append(this.getEntityName());
		sql.append(SQL_SET);
		for (int i = 0; i < cols.length; i++) {
			if (cols[i].equals(COLUMN_ID)) {
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
		sql.append(COLUMN_ID);
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
		} catch (SQLException sqle) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
			final String mesg = "Cannot update " + this.getEntityName() + " '" + id + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
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
	 * Обновить в БД связанные идентификаторы.
	 * <p>
	 * Если связанный идентификатор существует в карте
	 * {@code idLinkedObjectIdsMap}, но не существует в БД - этот идентификатор
	 * вставляется в таблицу-связку в БД. Если связанный идентификатор
	 * существует в таблице-связке в БД, но не существует в
	 * {@code idLinkedObjectIdsMap} - он удаляется из таблицы-связки.
	 * 
	 * @param idLinkedIdMap
	 *        Карта, где по ключу хранится идентификатор объекта, а по величине -
	 *        набор идентификаторов связанных с ним объектов.
	 * @param tableName
	 *        Проммежуточная таблица-связка.
	 * @param idColumnName
	 *        Столбец в таблице {@code tableName}, где хранятся идентификаторы
	 *        объектов.
	 * @param linkedIdColumnName
	 *        Столбец в таблице {@code tableName}, где хранятся идентификаторы
	 *        связанных объектов.
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
	 * Обновить в БД связанные перечислимые объекты.
	 * <p>
	 * Этот метод может быть использован в наследниках данного класса, в
	 * переопределении метода {@link #update(Set)}, для обновления в БД
	 * полей-перечислимых объектов каждого объекта из набора
	 * {@code identifiables}.
	 * <p>
	 * В настоящее время нигде не используется, но может быть полезен.
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
			dbIdLinkedCodeMap = this.retrieveLinkedEnums(idLinkedCodeMap.keySet(),
					enumClass,
					tableName,
					idColumnName,
					linkedCodeColumnName);
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, EnumSet<E>> insertCodesMap = new HashMap<Identifier, EnumSet<E>>();
		final Map<Identifier, EnumSet<E>> deleteCodesMap = new HashMap<Identifier, EnumSet<E>>();
		for (final Identifier id : idLinkedCodeMap.keySet()) {
			final EnumSet<E> codes = idLinkedCodeMap.get(id);
			final EnumSet<E> dbCodes = dbIdLinkedCodeMap.get(id);
			if (dbCodes != null) {

				// Prepare map for insertion
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

				// Prepare map for delete
				for (final E code : dbCodes) {
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

	/**
	 * Получить набор идентификаторов объектов, версии которых старее, чем
	 * версии тех же объектов в БД.
	 * 
	 * @param versionsMap
	 *        Карта, где по ключу - идентификатор объекта, по величине - его
	 *        версия.
	 * @return Набор идентификаторов объектов, устаревших по сравнению с БД.
	 * @throws RetrieveObjectException
	 * @see {@link StorableObjectVersion#isOlder(StorableObjectVersion)}.
	 */
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

	/**
	 * Удалить объекты {@code identifiables} из БД.
	 * <p>
	 * Этот метод можно переопределить в подклассах, если, например, нужно
	 * удалить связаные объекты.
	 * 
	 * @param identifiables
	 */
	public void delete(Set<? extends Identifiable> identifiables) {
		if ((identifiables == null) || (identifiables.isEmpty())) {
			return;
		}

		final StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM);
		stringBuffer.append(this.getEntityName());
		stringBuffer.append(SQL_WHERE);
		stringBuffer.append(idsEnumerationString(identifiables, COLUMN_ID, true));

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

	/**
	 * Удалить связанные идентификаторы.
	 * 
	 * @param idLinkedObjectIdsMap
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 */
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
	 * Удалить связанные перечисляемые объекты.
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
	 * <p>
	 * If <code>inList</code> is <code>true</code> returns a string like
	 * "idColumn IN ('id1', 'id2',... , 'idN') OR idColumn IN ('id3', 'id4',... ,
	 * 'idM') ..." If <code>inList</code> is <code>false</code> returns a
	 * string like "idColumn NOT IN ('id1', 'id2',... , 'idN') AND idColumn NOT
	 * IN ('id3', 'id4',... , 'idM') ..."
	 * </p>
	 * <p>
	 * If {@code identifiables} contain
	 * {@link Identifier#VOID_IDENTIFIER VOID_IDENTIFIER}, it is handled
	 * correctly, e. g.:
	 * 
	 * <pre>
	 *  idColumn IN ('id1', 'id2', ..., 'isN') OR idColumn IS NULL
	 *  idColumn NOT IN ('id1', 'id2', ..., 'isN') AND idColumn IS NOT NULL
	 * </pre>
	 * 
	 * </p>
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
			return new StringBuffer(inList ? FALSE_CONDITION : TRUE_CONDITION);
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
	 * 
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
			final Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { condition.getClass() });
			constructor.setAccessible(true);
			databaseStorableObjectCondition = (DatabaseStorableObjectCondition) constructor.newInstance(new Object[] { condition });
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
