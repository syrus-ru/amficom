package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.configuration.corba.EquipmentSort;

public class MCMDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_DOMAIN_ID	= "domain_id";	
	public static final String	COLUMN_NAME			= "name";
	public static final String	COLUMN_DESCRIPTION	= "description";
	public static final String	COLUMN_TYPE_ID		= "type_id";
	
	public static final String	LINK_COLUMN_SORT		= "sort";
	public static final String	LINK_COLUMN_MCM_ID		= "mcm_id";

	private MCM fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		this.retrieveMCM(mcm);
		this.retrieveMCMKis(mcm);
	}

	private void retrieveMCM(MCM mcm) throws ObjectNotFoundException, RetrieveObjectException {
		String mIdStr = mcm.getId().toSQLString();
		String sql = SQL_SELECT +
				DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
				+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
				+ COLUMN_CREATOR_ID + COMMA
				+ COLUMN_MODIFIER_ID + COMMA 
				+ COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA 
				+ COLUMN_DESCRIPTION + COMMA 
				+ SQL_FROM + ObjectEntities.MCM_ENTITY + SQL_WHERE + COLUMN_ID + EQUALS + mIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				mcm.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED), DatabaseDate
						.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				new Identifier(resultSet.getString(COLUMN_DOMAIN_ID)),
				new Identifier(resultSet.getString(COLUMN_TYPE_ID)),
				resultSet.getString(COLUMN_NAME),
				resultSet.getString(COLUMN_DESCRIPTION));
			} else
				throw new ObjectNotFoundException("No such mcm: " + mIdStr);
		} catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieve | Cannot retrieve mcm " + mIdStr;
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
				// nothing yet.
			}
		}
	}
	
	private void retrieveMCMKis(MCM mcm) throws ObjectNotFoundException, RetrieveObjectException {
		String mIdStr = mcm.getId().toSQLString();
		String sql = SQL_SELECT 
				+ COLUMN_ID
				+ SQL_FROM 
				+ ObjectEntities.EQUIPMENT_ENTITY + SQL_WHERE 
				+ LINK_COLUMN_SORT + EQUALS + EquipmentSort._EQUIPMENT_SORT_KIS
				+ SQL_AND
				+ LINK_COLUMN_MCM_ID + EQUALS + mIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			List characteristicIds = new ArrayList();
			if (resultSet.next()) {				
				characteristicIds.add(resultSet.getString(COLUMN_ID));
			} else
				throw new ObjectNotFoundException("No such mcm: " + mIdStr);
		} catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieve | Cannot retrieve mcm " + mIdStr;
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
//				 nothing yet.
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		try {
			this.insertMCM(mcm);
		} catch (CreateObjectException e) {
			try {
				connection.rollback();
			} catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertMCM(MCM mcm) throws CreateObjectException {
		String cIdStr = mcm.getId().toSQLString();		
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.CHARACTERISTIC_ENTITY);
			buffer.append(OPEN_BRACKET);
			buffer.append(COLUMN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATED);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIED);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_DOMAIN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_NAME);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(cIdStr);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(mcm.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(mcm.getModified()));
			buffer.append(COMMA);
			buffer.append(mcm.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(mcm.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(mcm.getDomainId().toSQLString());			
			buffer.append(COMMA);
			buffer.append(mcm.getTypeId().toSQLString());
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(mcm.getName());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(mcm.getDescription());
			buffer.append(APOSTOPHE);			
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MCMDatabase.insert | Cannot insert mcm " + cIdStr;
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
//				 nothing yet.
			}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws IllegalDataException,
			UpdateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}