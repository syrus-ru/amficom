package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import oracle.sql.BLOB;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;

public class Set_Database extends StorableObject_Database {
	
//	 sort NUMBER(2, 0) NOT NULL,
	public static final String	COLUMN_SORT			= "sort";
//	 description VARCHAR2(256),
	public static final String	COLUMN_DESCRIPTION	= "description";
	
	public static final String LINK_COLUMN_SET_ID	= "set_id";
	public static final String LINK_COLUMN_ME_ID 	= "monitored_element_id";
	public static final String LINK_COLUMN_TYPE_ID	= "type_id";
	public static final String LINK_COLUMN_VALUE	= "value";

	private Set fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Set)
			return (Set)storableObject;
		else
			throw new Exception("Set_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		this.retrieveSet(set);
		this.retrieveSetParameters(set);
		this.retrieveSetMELinks(set);
	}

	private void retrieveSet(Set set) throws Exception {
		String set_id_str = set.getId().toSQLString();
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
			+ set_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.retrieveSet | Trying: " + sql, Log.DEBUGLEVEL05);
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
				throw new Exception("No such set: " + set_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.retrieveSet | Cannot retrieve set " + set_id_str;
			throw new Exception(mesg, sqle);
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
			catch (SQLException sqle1) {}
		}
	}

	private void retrieveSetParameters(Set set) throws Exception {
		String set_id_str = set.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID + COMMA			
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_VALUE
			+ SQL_FROM
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_SET_ID +EQUALS
			+ set_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.retrieveSetParameters | Trying: " + sql, Log.DEBUGLEVEL05);
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
			String mesg = "Set_Database.retrieveSetParameters | Cannot retrieve parameters for set " + set_id_str;
			throw new Exception(mesg, sqle);
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
			catch (SQLException sqle1) {}
		}
		set.setParameters((SetParameter[])arraylist.toArray(new SetParameter[arraylist.size()]));
	}

	private void retrieveSetMELinks(Set set) throws Exception {
		String set_id_str = set.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_ME_ID
			+ SQL_FROM + ObjectEntities.SETMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_SET_ID + EQUALS
			+ set_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.retrieveSetMELinks | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				arraylist.add(new Identifier(resultSet.getString(LINK_COLUMN_ME_ID)));
			}
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.retrieveSetMELinks | Cannot retrieve monitored element ids for set " + set_id_str;
			throw new Exception(mesg, sqle);
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
			catch (SQLException sqle1) {}
		}
		set.setMonitoredElementIds(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		try {
			this.insertSet(set);
			this.insertSetParameters(set);
			this.insertSetMELinks(set);
		}
		catch (Exception e) {
			this.delete(set);
			throw e;
		}
	}

	private void insertSet(Set set) throws Exception {
		String set_id_str = set.getId().toSQLString();
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
			+ set_id_str + COMMA
			+ DatabaseDate.toUpdateSubString(set.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(set.getModified()) + COMMA
			+ set.getCreatorId().toSQLString() + COMMA
			+ set.getModifierId().toSQLString() + COMMA
			+ Integer.toString(set.getSort().value()) + COMMA + APOSTOPHE
			+ set.getDescription() + APOSTOPHE + COMMA;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.insertSet | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.insertSet | Cannot insert set " + set_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	private void insertSetParameters(Set set) throws Exception {
		String set_id_str = set.getId().toString();
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
				Log.debugMessage("Set_Database.insertSetParameters | Inserting parameter " + setParameters[i].getTypeId().toString() + " for set " + set_id_str, Log.DEBUGLEVEL05);
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
			String mesg = "Set_Database.insertSetParameters | Cannot insert parameter " + setParameters[i].getId().toString() + " of type " + setParameters[i].getTypeId().toString() + " for set " + set_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	private void insertSetMELinks(Set set) throws Exception {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String set_id_code = set.getId().getCode();
		ArrayList me_ids = set.getMonitoredElementIds();
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
		String me_id_code = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = me_ids.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(1, set_id_code);
				me_id_code = ((Identifier)iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, me_id_code);
				Log.debugMessage("Set_Database.insertSetMELinks | Inserting link for set " + set_id_code + " and monitored element " + me_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.insertSetMELinks | Cannot insert link for monitored element " + me_id_code + " and set " + set_id_code;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		Set set = this.fromStorableObject(storableObject);
		switch (update_kind) {
			case Set.UPDATE_ATTACH_ME:
				this.createMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
			case Set.UPDATE_DETACH_ME:
				this.deleteMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
		}
	}

	private void createMEAttachment(Set set, Identifier monitored_element_id) throws Exception {
		String set_id_str = set.getId().toSQLString();
		String me_id_str = monitored_element_id.toSQLString();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLUMN_SET_ID + COMMA
			+ LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET			
			+ set_id_str + COMMA
			+ me_id_str
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set.createMEAttachment | Cannot attach set " + set_id_str + " to monitored element " + me_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	private void deleteMEAttachment(Set set, Identifier monitored_element_id) throws Exception {
		String set_id_str = set.getId().toSQLString();
		String me_id_str = monitored_element_id.toSQLString();
		String sql = SQL_DELETE_FROM 
					+ ObjectEntities.SETMELINK_ENTITY
					+ SQL_WHERE 
					+ LINK_COLUMN_SET_ID + EQUALS
					+ set_id_str
					+ SQL_AND
					+ LINK_COLUMN_ME_ID + EQUALS
					+ me_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.deleteMEAttachment | Cannot detach set " + set_id_str + " from monitored element " + me_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	private void setModified(Set set) throws Exception {
		String set_id_str = set.getId().toSQLString();
		String sql = SQL_UPDATE
					+ ObjectEntities.SET_ENTITY
					+ SQL_SET
					+ COLUMN_MODIFIED + EQUALS
					+ DatabaseDate.toUpdateSubString(set.getModified()) + COMMA
					+ COLUMN_MODIFIER_ID + EQUALS + set.getModifierId().toSQLString()
					+ SQL_WHERE + EQUALS + set_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set_Database.setModified | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set_Database.setModified | Cannot set modified for set " + set_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	private void delete(Set set) {
		String set_id_str = set.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
									+ ObjectEntities.SETMELINK_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_SET_ID + EQUALS
									+ set_id_str);
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SETPARAMETER_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_SET_ID + EQUALS
									+ set_id_str);									
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SET_ENTITY
									+ SQL_WHERE
									+ COLUMN_ID + EQUALS
									+ set_id_str);
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
			catch(SQLException _ex) { }
		}
	}
}