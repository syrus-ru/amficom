/*
 * $Id: StorableObjectDatabase.java,v 1.81 2005/02/04 13:00:48 arseniy Exp $
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
import java.util.Collections;
import java.util.Date;
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
 * @version $Revision: 1.81 $, $Date: 2005/02/04 13:00:48 $
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


	//protected static Connection	connection;

	/**
	 * @see "ORA-01795"
	 */
	protected static final int MAXIMUM_EXPRESSION_NUMBER = 1000;


	private static String columns;
	private static String updateMultiplySQLValues;
	private String retrieveQuery;

	public StorableObjectDatabase() {
		//connection = DatabaseConnection.getConnection();
	}

	public void delete(Identifier id)  throws IllegalDataException {
		String storableObjectIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {			
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM + this.getEnityName() + SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS
			+ storableObjectIdStr;
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
			finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void delete(StorableObject storableObject)  throws IllegalDataException {
		this.delete(storableObject.getId());
	}

	public void delete(List ids) throws IllegalDataException {
		if ( (ids == null) || (ids.isEmpty()))
			return;
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_DELETE_FROM);
			buffer.append(this.getEnityName());
			buffer.append(SQL_WHERE);
			buffer.append(OPEN_BRACKET);
			buffer.append(StorableObjectWrapper.COLUMN_ID);
			int idsLength = ids.size();
			if (idsLength == 1) {
				buffer.append(EQUALS);
				Object object = ids.iterator().next();
				Identifier identifier = null;
				if (object instanceof Identifier)
					identifier = (Identifier)object;
				else if (object instanceof Identified)
					identifier = ((Identified)object).getId();
				else throw new IllegalDataException("StorableObjectDatabase.delete | Object " + 
													object.getClass().getName() 
													+ " isn't Identifier or Identified");
				buffer.append(DatabaseIdentifier.toSQLString(identifier));
			}
			else {
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);

				int i = 1;
				for (Iterator it = ids.iterator(); it.hasNext();i++) {						
					Object object = it.next();
					Identifier id = null;
					if (object instanceof Identifier)
						id = (Identifier)object;
					else if (object instanceof Identified)
						id = ((Identified)object).getId();
					else throw new IllegalDataException("StorableObjectDatabase.delete | Object " + 
														object.getClass().getName() 
														+ " isn't Identifier or Identified");
					buffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()) {
						if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
							buffer.append(COMMA);
						else {
							buffer.append(CLOSE_BRACKET);
							buffer.append(SQL_OR);
							buffer.append(StorableObjectWrapper.COLUMN_ID);
							buffer.append(SQL_IN);
							buffer.append(OPEN_BRACKET);
						}
					}
				}
				buffer.append(CLOSE_BRACKET);
			}
			buffer.append(CLOSE_BRACKET);			

			sql = buffer.toString();
		}
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {			
			statement = connection.createStatement();
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

	public abstract void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException;

	public abstract void insert(List storableObjects) throws IllegalDataException, CreateObjectException;

	public abstract void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	public abstract List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException;

	public abstract Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	public abstract void update(StorableObject storableObject, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException;

	public abstract void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException;

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
				+ StorableObjectWrapper.COLUMN_MODIFIER_ID;
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
				+ QUESTION;			
		}
		return updateMultiplySQLValues; 
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		return DatabaseIdentifier.toSQLString(storableObject.getId()) + COMMA
			+ DatabaseDate.toUpdateSubString(storableObject.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(storableObject.getModified()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getCreatorId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getModifierId());
	}

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

	protected void insertEntities(List storableObjects) throws IllegalDataException, CreateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		if (storableObjects.size() == 1) {
			insertEntity((StorableObject) storableObjects.get(0));
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
				updateEntityFromResultSet(storableObject, resultSet);
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
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected void checkAndUpdateEntity(StorableObject localStorableObject,final boolean force) 
					throws IllegalDataException, UpdateObjectException, VersionCollisionException {

		String atIdStr = DatabaseIdentifier.toSQLString(localStorableObject.getId());
		String sql = retrieveQuery(StorableObjectWrapper.COLUMN_ID + EQUALS + atIdStr);

		StorableObject storableObject = null;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEnityName() + "Database.checkAndUpdateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			try {
				if (resultSet.next()) {
					updateEntityFromResultSet(storableObject, resultSet);

					boolean update = force;
					if (!update)
						update =	((storableObject.getModifierId().equals(localStorableObject.getModifierId()))&&
						(Math.abs(storableObject.getModified().getTime()-localStorableObject.getModified().getTime())<1000));

					if (update) {
						localStorableObject.setAttributes(localStorableObject.getCreated(), new Date(System.currentTimeMillis()), 
														  localStorableObject.getCreatorId(), localStorableObject.getModifierId());
						updateEntity(localStorableObject);
					}
					else {
						String msg = "SorableObjectDatabase.checkAndUpdateEntity | " + getEnityName() + " conflict version ";
						throw new VersionCollisionException(msg);
					}

				}
				else {
					try {
						insert(localStorableObject);
					}
					catch (IllegalDataException ide) {
						String mesg = "SorableObjectDatabase.checkAndUpdateEntity | Cannot update "
								+ getEnityName() + " '" + atIdStr + "' -- "
								+ ide.getMessage();
						throw new UpdateObjectException(mesg, ide);
					}
					catch (CreateObjectException coe) {
						String mesg = "SorableObjectDatabase.checkAndUpdateEntity | Cannot update "
								+ getEnityName() + " '" + atIdStr + "' -- "
								+ coe.getMessage();
						throw new UpdateObjectException(mesg, coe);
					}
				}
			}
			catch (RetrieveObjectException roe) {
				String mesg = "SorableObjectDatabase.checkAndUpdateEntity | Cannot update "
						+ getEnityName() + " '" + atIdStr + "' -- " + roe.getMessage();
				throw new UpdateObjectException(mesg, roe);
			}
		}
		catch (SQLException sqle) {			
			String mesg = "SorableObjectDatabase.checkAndUpdateEntity | Cannot update " + getEnityName() + " '"
					+ atIdStr + "' -- " + sqle.getMessage();
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

	protected void checkAndUpdateEntities(List localStorableObjects, boolean force) throws IllegalDataException, UpdateObjectException, VersionCollisionException {
		if (localStorableObjects == null || localStorableObjects.isEmpty())
			return;
		List idsList = new LinkedList();
		for(Iterator it=localStorableObjects.iterator();it.hasNext();) {
			StorableObject storableObject = (StorableObject)it.next();
			Identifier localId = storableObject.getId();
			if (idsList.contains(localId))
				throw new UpdateObjectException(getEnityName()+"Database.checkAndUpdateEntities | Input collection contains entity with the same id " + localId.getIdentifierString());
			idsList.add(storableObject.getId());
		}

		List storableObjects = null;
		try {
			storableObjects = retrieveByIds(idsList, null);
		}
		catch(RetrieveObjectException roe){
			throw new UpdateObjectException(getEnityName()+"Database.checkAndUpdateEntities | Error during retrieving by ids -- " + roe.getMessage(), roe);
		}

		List insertList = null;
		List updateList = null;		
		StringBuffer versionCollisions = null;

		for (Iterator localIter=localStorableObjects.iterator();localIter.hasNext();) {
			StorableObject localStorableObject = (StorableObject)localIter.next();
			Identifier localId = localStorableObject.getId();
			StorableObject storableObject = null;

			for(Iterator it=storableObjects.iterator();it.hasNext();) {
				StorableObject stObj = (StorableObject)it.next();
				if (stObj.getId().equals(localId)){
					storableObject = stObj;
					// remove item to reduce searching time
					it.remove();
					break;
				}
			}

			if (storableObject == null) {
				if (insertList == null)
					insertList = new LinkedList();
				insertList.add(localStorableObject);
			}
			else {
				boolean update = force;
				if (!update)
					update = ((storableObject.getModifierId().equals(localStorableObject.getModifierId()))&&
					(Math.abs(storableObject.getModified().getTime()-localStorableObject.getModified().getTime())<1000));

				if (update) {
					localStorableObject.setAttributes(localStorableObject.getCreated(),
																	new Date(System.currentTimeMillis()),
																	localStorableObject.getCreatorId(),
																	localStorableObject.getModifierId());
					if (updateList == null)
						updateList = new LinkedList();
					updateList.add(localStorableObject);
				}
				else {
					if (versionCollisions == null) {
						versionCollisions = new StringBuffer();
						versionCollisions.append(getEnityName() + "Database.checkAndUpdateEntity | conflict version for '");
						versionCollisions.append(storableObject.getId().getIdentifierString());
						versionCollisions.append('\'');
					}
					else { 
						versionCollisions.append(", '");
						versionCollisions.append(storableObject.getId().getIdentifierString());	
						versionCollisions.append('\'');
					}
				}

			}

		}

		if (insertList!=null)
			try{
				insertEntities(insertList);
			}
			catch(CreateObjectException coe) {
				String msg = getEnityName() + "Database.checkAndUpdateEntity | Error during insering new entities -- " + coe.getMessage();
				throw new UpdateObjectException(msg, coe);
			}

		if (updateList!=null)
			updateEntities(updateList);

		if (versionCollisions != null && versionCollisions.length() > 0)
			throw new VersionCollisionException(versionCollisions.toString());		
	}

	protected abstract StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException;
	
	
	/**
	 * @param storableObjects
	 * @return List&lt;Identifier&gt; of changed storable objects
	 * @throws RetrieveObjectException
	 */
	public Set refresh(Set storableObjects) throws RetrieveObjectException {
		if (storableObjects == null || storableObjects.isEmpty())
			return Collections.EMPTY_SET;
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(StorableObjectWrapper.COLUMN_ID);
			buffer.append(SQL_FROM);
			buffer.append(this.getEnityName());
			buffer.append(SQL_WHERE);
			buffer.append(" 1=0 ");
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				buffer.append(SQL_OR);
				buffer.append(OPEN_BRACKET);
				buffer.append(StorableObjectWrapper.COLUMN_ID);
				buffer.append(EQUALS);
				buffer.append(DatabaseIdentifier.toSQLString(storableObject.getId()));
				buffer.append(SQL_AND);
				buffer.append(StorableObjectWrapper.COLUMN_MODIFIED);
				buffer.append(StorableObjectDatabase.NOT_EQUALS);
				buffer.append(DatabaseDate.toUpdateSubString(storableObject.getModified()));
				// buffer.append(SQL_AND);
				// buffer.append(COLUMN_MODIFIER_ID);
				// buffer.append(EQUALS);
				// buffer.append(DatabaseIdentifier.toSQLString(storableObject.getModifierId()));
				buffer.append(CLOSE_BRACKET);
			}
			sql = buffer.toString();
		}

		Set ids = new HashSet();
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.refresh | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Identifier identifier = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID);
				ids.add(identifier);
			}
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.refresh | Cannot execute query " + sqle.getMessage();
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
		return ids;
	}
	
	/**
	 * retrive storable objects by identifiers not in ids
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	public List retrieveButIds(List ids) throws IllegalDataException, RetrieveObjectException {
		return retrieveButIds(ids, null);
	}

	private DatabaseStorableObjectCondition reflectDatabaseCondition(StorableObjectCondition condition) throws IllegalDataException {
		DatabaseStorableObjectCondition databaseStorableObjectCondition = null;
		String className = condition.getClass().getName();
		int lastPoint = className.lastIndexOf('.');
		String dbClassName = className.substring(0, lastPoint + 1) + "Database" + className.substring(lastPoint + 1);
		// System.out.println("dbClassName:" + dbClassName);
		try {
			Class clazz = Class.forName(dbClassName);
			Constructor constructor = clazz.getConstructor(new Class[] {condition.getClass()});
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

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException,
				IllegalDataException {

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
		List list = retrieveButIds(ids, conditionQuery);
		return list;
	}

	/**
	 * retrive storable objects by additional condition and identifiers not in ids   
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @param condition
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	protected List retrieveButIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		String sql;
		{
			StringBuffer buffer = new StringBuffer();
			if (ids != null) {
				int idsLength = ids.size();
				if (idsLength > 0){					
					buffer.append(StorableObjectWrapper.COLUMN_ID);				
					buffer.append(NOT);
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);
					
					int i = 1;
					for (Iterator it = ids.iterator(); it.hasNext();i++) {
						Object object = it.next();
						Identifier id = null;
						if (object instanceof Identifier)
							id = (Identifier) object;
						else if (object instanceof Identified)
							id = ((Identified)object).getId();
						else throw new IllegalDataException("StorableObjectDatabase.retrieveButIds | Object " +
															object.getClass().getName()
															+ " isn't Identifier or Identified");
	
						if (id != null){
							buffer.append(DatabaseIdentifier.toSQLString(id));
							if (it.hasNext()) {
								if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
									buffer.append(COMMA);
								else {
									buffer.append(CLOSE_BRACKET);
									buffer.append(SQL_AND);
									buffer.append(StorableObjectWrapper.COLUMN_ID);				
									buffer.append(NOT);
									buffer.append(SQL_IN);
									buffer.append(OPEN_BRACKET);
								}
							}
						}
					}
					buffer.append(CLOSE_BRACKET);
				}
			}
			if ((condition != null) && (condition.length() > 0)) {
				if (buffer.length() != 0)
					buffer.append(SQL_AND);
				buffer.append(condition);
			}

			sql = buffer.toString();
		}
		
		List result = this.retrieveByIds(null, sql);

		return result;
	}


	/**
	 * retrive storable objects by identifiers and additional condition
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @param condition
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	protected List retrieveByIdsOneQuery(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			StringBuffer buffer = new StringBuffer("1=1");
			if ( (ids != null) && (!ids.isEmpty())) {
				buffer.append(SQL_AND);
				buffer.append(OPEN_BRACKET);
				buffer.append(StorableObjectWrapper.COLUMN_ID);
				int idsLength = ids.size();
				if (idsLength == 1) {
					buffer.append(EQUALS);
					Object object = ids.iterator().next();
					Identifier identifier = null;
					if (object instanceof Identifier)
						identifier = (Identifier)object;
					else
						if (object instanceof Identified)
							identifier = ((Identified)object).getId();
						else
							throw new IllegalDataException("StorableObjectDatabase.retrieveByIdsOneQuery | Object " + 
														object.getClass().getName() 
														+ " isn't Identifier or Identified");
					buffer.append(DatabaseIdentifier.toSQLString(identifier));
				}
				else {
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);

					int i = 1;
					for (Iterator it = ids.iterator(); it.hasNext(); i++) {						
						Object object = it.next();
						Identifier id = null;
						if (object instanceof Identifier)
							id = (Identifier)object;
						else
							if (object instanceof Identified)
								id = ((Identified)object).getId();
							else
								throw new IllegalDataException("StorableObjectDatabase.retrieveByIdsOneQuery | Object " + 
															object.getClass().getName() 
															+ " isn't Identifier or Identified");
						buffer.append(DatabaseIdentifier.toSQLString(id));
						if (it.hasNext()) {
							if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
								buffer.append(COMMA);
							else {
								buffer.append(CLOSE_BRACKET);
								buffer.append(SQL_OR);
								buffer.append(StorableObjectWrapper.COLUMN_ID);
								buffer.append(SQL_IN);
								buffer.append(OPEN_BRACKET);
							}
						}
					}

					buffer.append(CLOSE_BRACKET);
				}
				buffer.append(CLOSE_BRACKET);
			}

			if ((condition != null) && (condition.length() > 0)) {
				buffer.append(SQL_AND);
				buffer.append(condition);
			}

			sql = retrieveQuery(buffer.toString());
		}

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				StorableObject storableObject = this.updateEntityFromResultSet(null, resultSet);
				result.add(storableObject);
			}
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
		return result;
	}

	protected List retriveByIdsPreparedStatement(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List result = new LinkedList();
		String sql = null;
		{

			int idsLength = ids.size();
			if (idsLength == 1)
				return retrieveByIdsOneQuery(ids, null);
			StringBuffer buffer = new StringBuffer(StorableObjectWrapper.COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(QUESTION);
			if ((condition != null) && (condition.trim().length() > 0)) {
				buffer.append(SQL_AND);
				buffer.append(condition);
			}

			sql = retrieveQuery(buffer.toString());
		}

		PreparedStatement ptmt = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			ptmt = connection.prepareStatement(sql.toString());
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier)object;
				else if (object instanceof Identified)
					id = ((Identified)object).getId();
				else throw new IllegalDataException("StorableObjectDatabase.retriveByIdsPreparedStatement | Object " + 
													object.getClass().getName() 
													+ " isn't Identifier or Identified");
				DatabaseIdentifier.setIdentifier(ptmt, 1 , id);
				resultSet = ptmt.executeQuery();
				if (resultSet.next()) {
					StorableObject storableObject = updateEntityFromResultSet(null, resultSet);
					result.add(storableObject);
				} else {
					Log.errorMessage("StorableObjectDatabase.retriveByIdsPreparedStatement | No such "
							+ this.getEnityName() + " : " + id.getIdentifierString());
				}

			}
		} catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.retriveByIdsPreparedStatement | Cannot retrieve "
					+ this.getEnityName() + " " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (ptmt != null)
					ptmt.close();
				if (ptmt != null)
					ptmt.close();
				ptmt = null;
				resultSet = null;				
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}

		return result;
	}	

	protected void updateEntity(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		String storableObjectIdStr = DatabaseIdentifier.toSQLString(storableObject.getId());

		String[] cols = this.getColumns(MODE_UPDATE).split(COMMA);
		String[] values = this.parseInsertStringValues(this.getUpdateSingleSQLValues(storableObject), cols.length);
		if (cols.length != values.length)
			throw new UpdateObjectException("StorableObjectDatabase.updateEntities | Count of columns ('"+cols.length+"') is not equals count of values ('"+values.length+"')");
		String sql = null;
		{
			StringBuffer buffer = new StringBuffer(SQL_UPDATE);
			buffer.append(this.getEnityName());
			buffer.append(SQL_SET);
			for(int i = 0; i < cols.length; i++) {
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
			Log.debugMessage("StorableObjectDatabase.updateEntity | Trying: " + sql,
						Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		} catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.updateEntity | Cannot update "
					+ this.getEnityName() + " '" + storableObjectIdStr + "' -- "
					+ sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;				
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	protected void updateEntities(List storableObjects) throws IllegalDataException, UpdateObjectException {

		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		if (storableObjects.size() == 1) {
			updateEntity((StorableObject) storableObjects.get(0));
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
	
	/**
	 * Map&lt;StorableObject, List&lt;Identifier&gt;&gt;
	 * @param storableObjects List&lt;StorableObject&gt;
	 * @param tableName
	 * @param idColumnName
	 * @param linkedIdColumnName
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 */
	protected Map retrieveLinkedEntityIds(List storableObjects, String tableName, String idColumnName, String linkedIdColumnName) throws RetrieveObjectException, IllegalDataException{
		if (storableObjects == null || storableObjects.isEmpty())
			return Collections.EMPTY_MAP;

		StringBuffer buffer = new StringBuffer(SQL_SELECT);
		buffer.append(idColumnName);
		buffer.append(COMMA);
		buffer.append(linkedIdColumnName);
		buffer.append(SQL_FROM);
		buffer.append(tableName);
		buffer.append(SQL_WHERE);
		buffer.append(idColumnName);
		buffer.append(SQL_IN);
		buffer.append(OPEN_BRACKET);

		int i = 1;
		for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
			Object object = it.next();
			Identifier id = null;
			if (object instanceof Identifier)
				id = (Identifier) object;
			else
				if (object instanceof Identified)
					id = ((Identified) object).getId();
				else
					throw new IllegalDataException("StorableObjectDatabase.retrieveLinkedEntityIds | Object "
							+ object.getClass().getName()
							+ " isn't Identifier or Identified");

			if (id != null) {
				buffer.append(DatabaseIdentifier.toSQLString(id));
				if (it.hasNext()) {
					if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0)) {
						buffer.append(COMMA);
					}
					else {
						buffer.append(CLOSE_BRACKET);
						buffer.append(SQL_OR);
						buffer.append(idColumnName);
						buffer.append(SQL_IN);
						buffer.append(OPEN_BRACKET);
					}
				}
			}
		}
		buffer.append(CLOSE_BRACKET);

		String sql = buffer.toString();

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.retrieveLinkedEntityIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);

			Map linkedEntityIdsMap = new HashMap();
			Identifier storabeObjectId;
			List linkedEntityIds;
			while (resultSet.next()) {
				storabeObjectId = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				linkedEntityIds = (List) linkedEntityIdsMap.get(storabeObjectId);
				if (linkedEntityIds == null) {
					linkedEntityIds = new LinkedList();
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
	
	/**
	 * 
	 * @param idLinkedIdMap map of &lt;Identifier id, List&lt;Identifier linkedId&gt;&gt;
	 * @param tableName name of linked table
	 * @param idColumnName column name of main entity id
	 * @param linkedIdColumnName column name of linked entity id
	 * @throws UpdateObjectException
	 */
	protected void updateLinkedEntities(Map idLinkedIdMap, String tableName, String idColumnName, String linkedIdColumnName) throws UpdateObjectException {
		if (idLinkedIdMap == null || idLinkedIdMap.isEmpty())
			return;

		StringBuffer buffer = new StringBuffer(SQL_SELECT);
		buffer.append(idColumnName);
		buffer.append(COMMA);
		buffer.append(linkedIdColumnName);
		buffer.append(SQL_FROM);
		buffer.append(tableName);
		buffer.append(SQL_WHERE);
		buffer.append(idColumnName);
		buffer.append(SQL_IN);
		buffer.append(OPEN_BRACKET);

		int i = 0;
		for (Iterator iter = idLinkedIdMap.keySet().iterator(); iter.hasNext(); i++) {
			Identifier id = (Identifier) iter.next();
			buffer.append(DatabaseIdentifier.toSQLString(id));
			if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
				buffer.append(COMMA);
			else {
				buffer.append(CLOSE_BRACKET);
				buffer.append(SQL_OR);
				buffer.append(idColumnName);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
			}
		}
		buffer.append(CLOSE_BRACKET);

		java.util.Map dbLinkedObjIdsMap = new HashMap();

		String sql = buffer.toString();
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("StorableObjectDatabase.updateLinkedEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				Identifier id = DatabaseIdentifier.getIdentifier(resultSet, idColumnName);
				Identifier linkedObjId = DatabaseIdentifier.getIdentifier(resultSet, linkedIdColumnName);
				List linkedObjIdList = (List) dbLinkedObjIdsMap.get(id);
				if (linkedObjIdList == null) {
					linkedObjIdList = new LinkedList();
					dbLinkedObjIdsMap.put(id, linkedObjIdList);
				}
				linkedObjIdList.add(linkedObjId);
			}
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.updateLinkedEntities | SQLException: " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}

		java.util.Map insertMap = new HashMap();
		java.util.Map deleteMap = new HashMap();
		for (Iterator iter = idLinkedIdMap.keySet().iterator(); iter.hasNext();) {
			Identifier id = (Identifier) iter.next();
			List linkedObjIdList = (List) idLinkedIdMap.get(id);
			List dbPhysicalLinkIds = (List) dbLinkedObjIdsMap.get(id);

			List deleteList = null;
			List insertList = null;
			// prepare list for deleting
			for (Iterator it = linkedObjIdList.iterator(); it.hasNext();) {
				Identifier linkedObjectId = (Identifier) it.next();
				if (!dbPhysicalLinkIds.contains(linkedObjectId)) {
					if (insertList == null) {
						insertList = new LinkedList();
						insertMap.put(id, insertList);
					}
					insertList.add(linkedObjectId);
				}
			}

			// prepare list for inserting
			for (Iterator it = dbPhysicalLinkIds.iterator(); it.hasNext();) {
				Identifier dbPhysicalLinkId = (Identifier) it.next();
				if (!linkedObjIdList.contains(dbPhysicalLinkId)) {
					if (deleteList == null) {
						deleteList = new LinkedList();
						deleteMap.put(id, insertList);
					}
					deleteList.add(dbPhysicalLinkId);
				}
			}
		}
		try {
			this.insertLinkedEntityIds(insertMap, tableName, idColumnName, linkedIdColumnName);
			this.deleteLinedEntityIds(deleteMap, tableName, linkedIdColumnName);
		}
		catch (CreateObjectException e) {
			throw new UpdateObjectException(e);
		}
	}
	
	/**
	 * @param idLinkedObjectIdsMap
	 *          map of &lt;&lt;Identifier&gt; collectorId , List&lt;Identifier&gt;
	 *          physicalLinkIds&gt;
	 * @throws CreateObjectException
	 */
	private void insertLinkedEntityIds(Map idLinkedObjectIdsMap, String tableName, String idColumnName, String linkedIdColumnName) throws CreateObjectException{
		String sql = SQL_INSERT_INTO 
		+ tableName
		+ OPEN_BRACKET
		+ idColumnName + COMMA 
		+ linkedIdColumnName
		+ CLOSE_BRACKET
		+ SQL_VALUES + OPEN_BRACKET
		+ QUESTION + COMMA
		+ QUESTION 
		+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier id = null;
		Identifier linkedId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = idLinkedObjectIdsMap.keySet().iterator(); iterator.hasNext();) {
				id = (Identifier)iterator.next();
				linkedId = (Identifier)idLinkedObjectIdsMap.get(id);
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, id);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, linkedId);
				Log.debugMessage("StorableObjectDatabase.insertLinkedEntityIds | Inserting linked entity  " + linkedId + " for " + id, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "StorableObjectDatabase.insertLinkedEntityIds | Cannot insert linked entity  " + linkedId + " for " + id + " -- " + sqle.getMessage();
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
			}  finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	private void deleteLinedEntityIds(Map idLinkedObjectIdsMap, String tableName, String linkedIdColumnName) {	
		StringBuffer linkBuffer = new StringBuffer(linkedIdColumnName);
		
		linkBuffer.append(SQL_IN);
		linkBuffer.append(OPEN_BRACKET);
		
		int i = 0;
		for (Iterator colIter = idLinkedObjectIdsMap.keySet().iterator(); colIter.hasNext();) {
			Identifier collectorId = (Identifier) colIter.next();
			List physicalLinkIds = (List)idLinkedObjectIdsMap.get(collectorId);
			for (Iterator it = physicalLinkIds.iterator(); it.hasNext(); i++) {
				Identifier linkedObjId = (Identifier) it.next();
	
				linkBuffer.append(DatabaseIdentifier.toSQLString(linkedObjId));
				if (it.hasNext()) {
					if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0)){
						linkBuffer.append(COMMA);
					}
					else {
						linkBuffer.append(CLOSE_BRACKET);
						linkBuffer.append(SQL_AND);
						linkBuffer.append(linkedIdColumnName);				
						linkBuffer.append(SQL_IN);
						linkBuffer.append(OPEN_BRACKET);
					}
				}
			
			}
		}
		
		linkBuffer.append(CLOSE_BRACKET);
		
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + tableName + SQL_WHERE
					+ linkBuffer.toString());
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

}

