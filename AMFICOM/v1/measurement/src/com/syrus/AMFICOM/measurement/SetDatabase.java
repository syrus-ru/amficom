package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import oracle.sql.BLOB;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;

public class SetDatabase extends StorableObjectDatabase {
	
	public static final String	COLUMN_SORT			= "sort";
	public static final String	COLUMN_DESCRIPTION	= "description";	
	public static final String LINK_COLUMN_SET_ID	= "set_id";
	public static final String LINK_COLUMN_ME_ID 	= "monitored_element_id";
	public static final String LINK_COLUMN_TYPE_ID	= "type_id";
	public static final String LINK_COLUMN_VALUE	= "value";

	private Set fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Set)
			return (Set)storableObject;
		throw new IllegalDataException("SetDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Set set = this.fromStorableObject(storableObject);
		this.retrieveSet(set);
		this.retrieveSetParameters(set);
		this.retrieveSetMELinks(set);
	}

	private void retrieveSet(Set set) throws ObjectNotFoundException, RetrieveObjectException {
		String setIdStr = set.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_DESCRIPTION 
			+ SQL_FROM 
			+ ObjectEntities.SET_ENTITY
			+ SQL_WHERE
			+ COLUMN_ID	+ EQUALS
			+ setIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSet | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				String description = resultSet.getString(COLUMN_DESCRIPTION);
				set.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/												
								  new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
								  resultSet.getInt(COLUMN_SORT),
								  (description != null)?description:"");
			}
			else
				throw new ObjectNotFoundException("No such set: " + setIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSet | Cannot retrieve set " + setIdStr;
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
		}
	}

	private void retrieveSetParameters(Set set) throws RetrieveObjectException {
		String setIdStr = set.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID + COMMA			
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_VALUE
			+ SQL_FROM
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_SET_ID +EQUALS
			+ setIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSetParameters | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())			
				arraylist.add(new SetParameter(
												/**
												 * @todo when change DB Identifier model ,change getString() to getLong()
												 */
												new Identifier(resultSet.getString(COLUMN_ID)),
												/**
												 * @todo when change DB Identifier model ,change getString() to getLong()
												 */
												new Identifier(resultSet.getString(LINK_COLUMN_TYPE_ID)),
												ByteArrayDatabase.toByteArray((BLOB)resultSet.getBlob(LINK_COLUMN_VALUE))));
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetParameters | Cannot retrieve parameters for set " + setIdStr;
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
		}
		set.setParameters((SetParameter[])arraylist.toArray(new SetParameter[arraylist.size()]));
	}

	private void retrieveSetMELinks(Set set) throws RetrieveObjectException {
		String setIdStr = set.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_ME_ID
			+ SQL_FROM + ObjectEntities.SETMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_SET_ID + EQUALS
			+ setIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSetMELinks | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				arraylist.add(new Identifier(resultSet.getString(LINK_COLUMN_ME_ID)));
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetMELinks | Cannot retrieve monitored element ids for set " + setIdStr;
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
		}
		set.setMonitoredElementIds(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Set set = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Set set = this.fromStorableObject(storableObject);
		try {
			this.insertSet(set);
			this.insertSetParameters(set);
			this.insertSetMELinks(set);
		}
		catch (CreateObjectException e) {
			this.delete(set);
			throw e;
		}
	}

	private void insertSet(Set set) throws CreateObjectException {
		String setIdStr = set.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.SET_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_SORT  + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ setIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(set.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(set.getModified()) + COMMA
			+ set.getCreatorId().toSQLString() + COMMA
			+ set.getModifierId().toSQLString() + COMMA
			+ Integer.toString(set.getSort().value()) + COMMA
			+ APOSTOPHE + set.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.insertSet | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSet | Cannot insert set " + setIdStr;
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
		}
	}

	private void insertSetParameters(Set set) throws CreateObjectException {
		String setIdStr = set.getId().toString();
		SetParameter[] setParameters = set.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID  + COMMA
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_SET_ID + COMMA
			+ LINK_COLUMN_VALUE + CLOSE_BRACKET
			+ SQL_VALUES 
			+ OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		int i = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(1, setParameters[i].getId().getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, setParameters[i].getTypeId().getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(3, set.getId().getCode());
				preparedStatement.setBlob(4, BLOB.empty_lob());
				Log.debugMessage("SetDatabase.insertSetParameters | Inserting parameter " + setParameters[i].getTypeId().toString() + " for set " + setIdStr, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
				ByteArrayDatabase badb = new ByteArrayDatabase(setParameters[i].getValue());
				badb.saveAsBlob(connection, 
								ObjectEntities.SETPARAMETER_ENTITY, 
								LINK_COLUMN_VALUE, 
								COLUMN_ID + EQUALS + setParameters[i].getId().toSQLString());
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetParameters | Cannot insert parameter " + setParameters[i].getId().toString() + " of type " + setParameters[i].getTypeId().toString() + " for set " + setIdStr;
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
		}
	}

	private void insertSetMELinks(Set set) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String setIdCode = set.getId().getCode();
		List meIds = set.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLUMN_SET_ID + COMMA 
			+ LINK_COLUMN_ME_ID 
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION 
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String meIdCode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(1, setIdCode);
				meIdCode = ((Identifier)iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, meIdCode);
				Log.debugMessage("SetDatabase.insertSetMELinks | Inserting link for set " + setIdCode + " and monitored element " + meIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetMELinks | Cannot insert link for monitored element " + meIdCode + " and set " + setIdCode;
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
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Set set = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Set.UPDATE_ATTACH_ME:
				this.createMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
			case Set.UPDATE_DETACH_ME:
				this.deleteMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
			default:
				return;
		}
	}

	private void createMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = set.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLUMN_SET_ID + COMMA
			+ LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET			
			+ setIdStr + COMMA
			+ meIdStr
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set.createMEAttachment | Cannot attach set " + setIdStr + " to monitored element " + meIdStr;
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
		}
	}

	private void deleteMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = set.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_DELETE_FROM 
					+ ObjectEntities.SETMELINK_ENTITY
					+ SQL_WHERE 
					+ LINK_COLUMN_SET_ID + EQUALS
					+ setIdStr
					+ SQL_AND
					+ LINK_COLUMN_ME_ID + EQUALS
					+ meIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.deleteMEAttachment | Cannot detach set " + setIdStr + " from monitored element " + meIdStr;
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
		}
	}

	private void setModified(Set set) throws UpdateObjectException {
		String setIdStr = set.getId().toSQLString();
		String sql = SQL_UPDATE
					+ ObjectEntities.SET_ENTITY
					+ SQL_SET
					+ COLUMN_MODIFIED + EQUALS
					+ DatabaseDate.toUpdateSubString(set.getModified()) + COMMA
					+ COLUMN_MODIFIER_ID + EQUALS + set.getModifierId().toSQLString()
					+ SQL_WHERE + EQUALS + setIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.setModified | Cannot set modified for set " + setIdStr;
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
		}
	}

	private void delete(Set set) {
		String setIdStr = set.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
									+ ObjectEntities.SETMELINK_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_SET_ID + EQUALS
									+ setIdStr);
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SETPARAMETER_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_SET_ID + EQUALS
									+ setIdStr);									
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SET_ENTITY
									+ SQL_WHERE
									+ COLUMN_ID + EQUALS
									+ setIdStr);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}
}
