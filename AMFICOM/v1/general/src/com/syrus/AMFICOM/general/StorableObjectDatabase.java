/*
 * $Id: StorableObjectDatabase.java,v 1.30 2004/09/20 13:16:08 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.30 $, $Date: 2004/09/20 13:16:08 $
 * @author $Author: bob $
 * @module general_v1
 */

public abstract class StorableObjectDatabase {

	public static final String	APOSTOPHE			= "'";
	public static final String	CLOSE_BRACKET			= " ) ";

	public static final String	COLUMN_CREATED			= "created";
	public static final String	COLUMN_CREATOR_ID		= "creator_id";
	public static final String	COLUMN_ID			= "id";
	public static final String	COLUMN_MODIFIED			= "modified";
	public static final String	COLUMN_MODIFIER_ID		= "modifier_id";
	public static final String	COMMA				= " , ";
	public static final String	EQUALS				= " = ";
	public static final String	NOT_EQUALS			= " <> ";
	public static final String	NOT					= " NOT ";
	public static final String	LINK_COLUMN_PARAMETER_MODE	= "parameter_mode";

	public static final String	LINK_COLUMN_PARAMETER_TYPE_ID	= "parameter_type_id";
	public static final String	OPEN_BRACKET			= " ( ";
	public static final String	QUESTION			= "?";

	public static final String	SQL_AND				= " AND ";
	public static final String	SQL_ASC				= " ASC ";
	public static final String	SQL_COUNT			= " COUNT(*) ";
	public static final String	SQL_DELETE_FROM			= " DELETE FROM ";
	public static final String	SQL_DESC			= " DESC ";
	public static final String	SQL_FROM			= " FROM ";

	public static final String	SQL_FUNCTION_MAX		= " MAX ";
	public static final String	SQL_IN				= " IN ";
	public static final String	SQL_INSERT_INTO			= " INSERT INTO ";
	public static final String	SQL_NULL			= " NULL ";
	public static final String	SQL_OR				= " OR ";
	public static final String	SQL_ORDER_BY			= " ORDER BY ";
	public static final String	SQL_SELECT			= " SELECT ";
	public static final String	SQL_SET				= " SET ";
	public static final String	SQL_UPDATE			= " UPDATE ";
	public static final String	SQL_VALUES			= " VALUES ";
	public static final String	SQL_WHERE			= " WHERE ";
	
	public static final int 	UPDATE_TOTAL 		= -1;
	public static final int 	UPDATE_FORCE 		= -2;
	public static final int 	UPDATE_CHECK 		= -3;

	//protected static Connection	connection;
	
	private String updateColumnsString;
	private String updateMultiplySQLValuesString;
	private String retrieveQueryString;

	public StorableObjectDatabase() {
		//connection = DatabaseConnection.getConnection();
	}

	public void delete(StorableObject storableObject) {
		String storableObjectIdStr = storableObject.getId().toSQLString();
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {			
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + this.getTableName() + SQL_WHERE + COLUMN_ID + EQUALS
					+ storableObjectIdStr);			
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;				
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public abstract void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException;
	
	public abstract void insert(List storableObjects) throws IllegalDataException, CreateObjectException;

	public abstract void retrieve(StorableObject storableObject) throws IllegalDataException,
			ObjectNotFoundException, RetrieveObjectException;

	public abstract List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException;

	public abstract Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	public abstract void update(StorableObject storableObject, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException;
	
	public abstract void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException;

	protected abstract String getEnityName();

	protected abstract String getTableName();

	protected String getUpdateColumns(){
		if (this.updateColumnsString == null){
			this.updateColumnsString = COLUMN_ID + COMMA
				+ COLUMN_CREATED + COMMA
				+ COLUMN_MODIFIED + COMMA
				+ COLUMN_CREATOR_ID + COMMA
				+ COLUMN_MODIFIER_ID;
		}
		return this.updateColumnsString;
	}

	protected String getUpdateMultiplySQLValues(){
		if (this.updateMultiplySQLValuesString == null){
			this.updateMultiplySQLValuesString = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;			
		}
		return this.updateMultiplySQLValuesString; 
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException{
		return storableObject.getId().toSQLString() + COMMA
		+ DatabaseDate.toUpdateSubString(storableObject.getCreated()) + COMMA
		+ DatabaseDate.toUpdateSubString(storableObject.getModified()) + COMMA
		+ storableObject.getCreatorId().toSQLString() + COMMA 
		+ storableObject.getModifierId().toSQLString();
	}

	protected void insertEntity(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		String storableObjectIdStr = storableObject.getId().toSQLString();
		try{
			String sql = SQL_INSERT_INTO + this.getTableName() + OPEN_BRACKET + this.getUpdateColumns()
					+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET 
					+ this.getUpdateSingleSQLValues(storableObject) + CLOSE_BRACKET;
			Statement statement = null;
			Connection connection = DatabaseConnection.getConnection();
			try {
				statement = connection.createStatement();
				Log.debugMessage(this.getEnityName() + "Database.insertEntity | Trying: " + sql,
							Log.DEBUGLEVEL09);
				statement.executeUpdate(sql);
				connection.commit();
			} catch (SQLException sqle) {
				String mesg = this.getEnityName() + "Database.insertEntity | Cannot insert "
						+ this.getEnityName() + " '" + storableObjectIdStr + "' -- "
						+ sqle.getMessage();
				try {
					connection.rollback();
				}
				catch (SQLException sqle2) {
					Log.errorMessage("Exception in rolling back");
					Log.errorException(sqle2);
				}
				throw new CreateObjectException(mesg, sqle);
			} finally {
				try {
					if (statement != null)
						statement.close();
					statement = null;					
				} catch (SQLException sqle1) {
					Log.errorException(sqle1);
				} finally{
					DatabaseConnection.closeConnection(connection);
				}
			}
			
		} catch(UpdateObjectException uoe){
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
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			StorableObject storableObject = (StorableObject)it.next();
			Identifier localId = storableObject.getId();
			if (idsList.contains(localId))
				throw new CreateObjectException(getEnityName()+"Database.insertEntities | Input collection contains entity with the same id " + localId.getCode());
			idsList.add(storableObject.getId());
		}
		idsList.clear();
		idsList = null;


		String sql = SQL_INSERT_INTO + this.getTableName() + OPEN_BRACKET				
				+ this.getUpdateColumns()
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET + this.getUpdateMultiplySQLValues()
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		String storableObjectIdCode = null;
		
		Connection connection = DatabaseConnection.getConnection();
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage(this.getEnityName() + "Database.insertEntities | Trying: " + sql,
								Log.DEBUGLEVEL09);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObjectIdCode = storableObject.getId().getCode();
				this.setEntityForPreparedStatement(storableObject, preparedStatement);
				Log.debugMessage(this.getEnityName() + "Database.insertEntities | Inserting  "
						+ this.getEnityName() + " " + storableObjectIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			
			connection.commit();			
		} catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.insertEntities | Cannot insert "
					+ this.getEnityName() + " '" + storableObjectIdCode + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} catch(UpdateObjectException uoe){
			throw new CreateObjectException(uoe);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;				
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	protected void retrieveEntity(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		String strorableObjectTypeIdStr = storableObject.getId().toSQLString();
		String sql = retrieveQuery(COLUMN_ID + EQUALS + strorableObjectTypeIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.retrieveEntity | Trying: " + sql,
						Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				updateEntityFromResultSet(storableObject, resultSet);
			else
				throw new ObjectNotFoundException("No such " + getEnityName() + ": "
						+ strorableObjectTypeIdStr);
		} catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retrieveEntity | Cannot retrieve "
					+ getEnityName() + " '" + strorableObjectTypeIdStr + "' -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null)
						resultSet.close();
				} finally {
					try{
					if (statement != null)
						statement.close();
					} finally {
						DatabaseConnection.closeConnection(connection);
					}
				}
			} catch (SQLException sqle) {
				Log.errorException(sqle);
			}
		}
	}

	protected String retrieveQuery(final String condition){
		if (this.retrieveQueryString == null){
			this.retrieveQueryString = SQL_SELECT
				+ COLUMN_ID + COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
				+ COLUMN_CREATOR_ID + COMMA
				+ COLUMN_MODIFIER_ID; 
		}
		return this.retrieveQueryString; 
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
								PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException{
		try {
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(1, storableObject.getId().getCode());
			preparedStatement.setTimestamp(2, new Timestamp(storableObject.getCreated().getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(storableObject.getModified().getTime()));
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(4, storableObject.getCreatorId().getCode());
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(5, storableObject.getModifierId().getCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return 5;
	}

	protected void checkAndUpdateEntity(StorableObject localStorableObject,final boolean force) 
					throws IllegalDataException, UpdateObjectException, VersionCollisionException {

		String atIdStr = localStorableObject.getId().toSQLString();
		String sql = retrieveQuery(COLUMN_ID + EQUALS + atIdStr);

		StorableObject storableObject = null;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEnityName() + "Database.checkAndUpdateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			try {
				if (resultSet.next()){
					updateEntityFromResultSet(storableObject, resultSet);
					
					boolean update = force;
					if (!update)
						update =	((storableObject.getModifierId().equals(localStorableObject.getModifierId()))&&
						(Math.abs(storableObject.getModified().getTime()-localStorableObject.getModified().getTime())<1000));
					
					if (update){
						localStorableObject.setAttributes(localStorableObject.getCreated(), new Date(System.currentTimeMillis()), 
														  localStorableObject.getCreatorId(), localStorableObject.getModifierId());
						update(localStorableObject, UPDATE_TOTAL, null);
					} else{
						String msg = getEnityName() + "Database.checkAndUpdateEntity | " + getEnityName() + " conflict version ";
						throw new VersionCollisionException(msg);
					}
					
				}
				else {
					try {
						insert(localStorableObject);
					} catch (IllegalDataException ide) {
						String mesg = getEnityName() + "Database.checkAndUpdateEntity | Cannot update "
								+ getEnityName() + " '" + atIdStr + "' -- "
								+ ide.getMessage();
						throw new UpdateObjectException(mesg, ide);
					} catch (CreateObjectException coe) {
						String mesg = getEnityName() + "Database.checkAndUpdateEntity | Cannot update "
								+ getEnityName() + " '" + atIdStr + "' -- "
								+ coe.getMessage();
						throw new UpdateObjectException(mesg, coe);
					}
				}
			} catch (RetrieveObjectException roe) {
				String mesg = getEnityName() + "Database.checkAndUpdateEntity | Cannot update "
						+ getEnityName() + " '" + atIdStr + "' -- " + roe.getMessage();
				throw new UpdateObjectException(mesg, roe);
			}
		} catch (SQLException sqle) {			
			String mesg = getEnityName() + "Database.checkAndUpdateEntity | Cannot update " + getEnityName() + " '"
					+ atIdStr + "' -- " + sqle.getMessage();
			try {
				connection.rollback();
			} catch (SQLException sqle2) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle2);
			}
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}
	
	protected void checkAndUpdateEntities(List localStorableObjects, boolean force) throws IllegalDataException, UpdateObjectException, VersionCollisionException{
		
		List idsList = new LinkedList();
		for(Iterator it=localStorableObjects.iterator();it.hasNext();){
			StorableObject storableObject = (StorableObject)it.next();
			Identifier localId = storableObject.getId();
			if (idsList.contains(localId))
				throw new UpdateObjectException(getEnityName()+"Database.checkAndUpdateEntities | Input collection contains entity with the same id " + localId.getCode());
			idsList.add(storableObject.getId());
		}
		
		
		List storableObjects = null;
		try{
			storableObjects = retrieveByIds(idsList, null);
		}catch(RetrieveObjectException roe){
			throw new UpdateObjectException(getEnityName()+"Database.checkAndUpdateEntities | Error during retrieving by ids -- " + roe.getMessage(), roe);
		}
		
		List insertList = null;
		List updateList = null;		
		
		for(Iterator localIter=localStorableObjects.iterator();localIter.hasNext();){
			StorableObject localStorableObject = (StorableObject)localIter.next();
			Identifier localId = localStorableObject.getId();
			StorableObject storableObject = null;
			
			for(Iterator it=storableObjects.iterator();it.hasNext();){
				StorableObject stObj = (StorableObject)it.next();
				if (stObj.getId().equals(localId)){
					storableObject = stObj;
					// remove item to reduce searching time
					it.remove();
					break;
				}
			}
			
			if (storableObject == null){
				if (insertList == null)
					insertList = new LinkedList();
				insertList.add(localStorableObject);
			} else{
				boolean update = force;
				if (!update)
					update = ((storableObject.getModifierId().equals(localStorableObject.getModifierId()))&&
					(Math.abs(storableObject.getModified().getTime()-localStorableObject.getModified().getTime())<1000));
				
				if (update){
					localStorableObject.setAttributes(localStorableObject.getCreated(), new Date(System.currentTimeMillis()), 
													  localStorableObject.getCreatorId(), localStorableObject.getModifierId());
					if (updateList == null)
						updateList = new LinkedList();
					updateList.add(localStorableObject);
				} else{
					String msg = getEnityName() + "Database.checkAndUpdateEntity | " + getEnityName() + " conflict version ";
					throw new VersionCollisionException(msg);
				}

			}
				
		}
		
		if (insertList!=null)
			try{		
			insertEntities(insertList);
			}catch(CreateObjectException coe){
				String msg = getEnityName() + "Database.checkAndUpdateEntity | Error during insering new entities -- " + coe.getMessage();
				throw new UpdateObjectException(msg, coe);
			}
		
		if (updateList!=null)
			updateEntities(updateList);
		
		
	}

	protected abstract StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException;
	
	/**
	 * retrive storable objects by identifiers not in ids
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @return
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	public List retrieveButIds(List ids) throws IllegalDataException, RetrieveObjectException {
		return retrieveButIds(ids, null);
	}
	
	/**
	 * retrive storable objects by additional condition and identifiers not in ids   
	 * @param ids List&lt;{@link Identifier}&gt; or List&lt;{@link Identified}&gt;
	 * @param condition
	 * @return
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	protected List retrieveButIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		String sql;
		{
			StringBuffer buffer = new StringBuffer("1=1");
			if (ids != null) {
				int idsLength = ids.size();
				buffer.append(SQL_AND);
				buffer.append(COLUMN_ID);				
				buffer.append(NOT);
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);

				int i = 1;
				for (Iterator it = ids.iterator(); it.hasNext(); i++) {
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
						buffer.append(id.toSQLString());
						if (i < idsLength)
							buffer.append(COMMA);
					}
				}
				buffer.append(CLOSE_BRACKET);
			}
			if ((condition != null) && (condition.length() > 0)) {
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
	 * @return
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 */
	protected List retrieveByIdsOneQuery(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			StringBuffer buffer = new StringBuffer("1=1");
			if (ids != null) {
				buffer.append(SQL_AND);
				buffer.append(COLUMN_ID);
				int idsLength = ids.size();
				if (idsLength == 1) {
					buffer.append(EQUALS);
					Object object = ids.iterator().next();
					Identifier identifier = null;
					if (object instanceof Identifier)
						identifier = (Identifier)object;
					else if (object instanceof Identified)
						identifier = ((Identified)object).getId();
					else throw new IllegalDataException("StorableObjectDatabase.retrieveByIdsOneQuery | Object " + 
														object.getClass().getName() 
														+ " isn't Identifier or Identified");
					buffer.append(identifier.toSQLString());
				} else {
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);

					int i = 1;
					for (Iterator it = ids.iterator(); it.hasNext(); i++) {						
						Object object = it.next();
						Identifier id = null;
						if (object instanceof Identifier)
							id = (Identifier)object;
						else if (object instanceof Identified)
							id = ((Identified)object).getId();
						else throw new IllegalDataException("StorableObjectDatabase.retrieveByIdsOneQuery | Object " + 
															object.getClass().getName() 
															+ " isn't Identifier or Identified");
						buffer.append(id.toSQLString());
						if (i < idsLength)
							buffer.append(COMMA);
					}

					buffer.append(CLOSE_BRACKET);
				}
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
			Log.debugMessage(this.getEnityName() + "Database.retriveByIdsOneQuery | Trying: " + sql,
						Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				StorableObject storableObject = updateEntityFromResultSet(null, resultSet);
				result.add(storableObject);
			}
		} catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retriveByIdsOneQuery | Cannot execute query "
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
			} finally{
				DatabaseConnection.closeConnection(connection);
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
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(QUESTION);
			if ((condition != null) && (condition.trim().length() > 0)) {
				buffer.append(SQL_AND);
				buffer.append(condition);
			}

			sql = retrieveQuery(buffer.toString());
		}

		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			stmt = connection.prepareStatement(sql.toString());
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
				String idStr = id.getIdentifierString();
				/**
				 * @todo when change DB Identifier model ,change
				 *       setString() to setLong()
				 */				
				stmt.setString(1, idStr);
				resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					StorableObject storableObject = updateEntityFromResultSet(null, resultSet);
					result.add(storableObject);
				} else {
					Log.errorMessage(this.getEnityName()
							+ "Database.retriveByIdsPreparedStatement | No such "
							+ this.getEnityName() + " : " + idStr);
				}

			}
		} catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retriveByIdsPreparedStatement | Cannot retrieve "
					+ this.getEnityName() + " " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt != null)
					stmt.close();
				stmt = null;
				resultSet = null;				
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}

		return result;
	}
	
	protected void updateEntity(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		String storableObjectIdStr = storableObject.getId().toSQLString();
		
		String[] columns = this.getUpdateColumns().split(COMMA);
		String[] values = this.getUpdateSingleSQLValues(storableObject).split(COMMA);
		if (columns.length != values.length)
			throw new UpdateObjectException(this.getEnityName() + "Database.updateEntities | Count of columns ('"+columns.length+"') is not equals count of values ('"+values.length+"')");
		String sql = null;
		{
			StringBuffer buffer = new StringBuffer(SQL_UPDATE);
			buffer.append(this.getTableName());
			buffer.append(SQL_SET);
			for(int i=0;i<columns.length;i++){
				buffer.append(columns[i]);
				buffer.append(EQUALS);
				buffer.append(values[i]);
				if (i<columns.length-1)
					buffer.append(COMMA);
			}
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(storableObjectIdStr);
			sql = buffer.toString();
		}
		
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.updateEntity | Trying: " + sql,
						Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		} catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.updateEntity | Cannot update "
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
				DatabaseConnection.closeConnection(connection);
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

		String[] columns = this.getUpdateColumns().split(COMMA);
		String[] values = this.getUpdateMultiplySQLValues().split(COMMA);
		if (columns.length != values.length)
			throw new UpdateObjectException(this.getEnityName() + "Database.updateEntities | Count of columns ('"+columns.length+"') is not equals count of values ('"+values.length+"')");
		String sql = null;
		{
			StringBuffer buffer = new StringBuffer(SQL_UPDATE);
			buffer.append(this.getTableName());
			buffer.append(SQL_SET);
			for(int i=0;i<columns.length;i++){
				buffer.append(columns[i]);
				buffer.append(EQUALS);
				buffer.append(values[i]);
				if (i<columns.length-1)
					buffer.append(COMMA);
			}
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(QUESTION);
			sql = buffer.toString();
		}
		PreparedStatement preparedStatement = null;
		String storableObjectIdCode = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage(this.getEnityName() + "Database.updateEntities | Trying: " + sql,
								Log.DEBUGLEVEL09);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {

				StorableObject storableObject = (StorableObject) it.next();
				storableObjectIdCode = storableObject.getId().getCode();
				int i = this.setEntityForPreparedStatement(storableObject, preparedStatement);
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(++i, storableObject.getId().getCode());
				
				Log.debugMessage(this.getEnityName() + "Database.updateEntities | Inserting  "
						+ this.getEnityName() + " " + storableObjectIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		} catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.updateEntities | Cannot update "
					+ this.getEnityName() + " '" + storableObjectIdCode + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

}

