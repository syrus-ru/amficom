package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class CharacteristicTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";
	public static final String COLUMN_DATA_TYPE				= "data_type";
	public static final String COLUMN_IS_EDITABLE			= "is_editable";
	public static final String COLUMN_IS_VISIBLE			= "is_visible";

	private CharacteristicType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CharacteristicType)
			return (CharacteristicType)storableObject;
		throw new IllegalDataException("CharacteristicTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		this.retrieveCharacteristicType(characteristicType);
	}

	private void retrieveCharacteristicType(CharacteristicType characteristicType) throws ObjectNotFoundException, RetrieveObjectException {
		String ctIdStr = characteristicType.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_DATA_TYPE + COMMA
			+ COLUMN_IS_EDITABLE + COMMA
			+ COLUMN_IS_VISIBLE + COMMA
			+ SQL_FROM + ObjectEntities.CHARACTERISTICTYPE_ENTITY
			+ SQL_WHERE	+ COLUMN_ID + EQUALS + ctIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicTypeDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				characteristicType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																				 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
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
																				 resultSet.getString(COLUMN_CODENAME),
																				 resultSet.getString(COLUMN_DESCRIPTION),
																				 resultSet.getInt(COLUMN_DATA_TYPE),
																				 (resultSet.getInt(COLUMN_IS_EDITABLE) == 0)?false:true,
																				 (resultSet.getInt(COLUMN_IS_VISIBLE) == 0)?false:true);
			else
				throw new ObjectNotFoundException("No such characteristic type: " + ctIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicTypeDatabase.retrieve | Cannot retrieve characteristic type " + ctIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		try {
			this.insertCharacteristicType(characteristicType);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertCharacteristicType(CharacteristicType characteristicType) throws CreateObjectException {
		String ctIdStr = characteristicType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MEASUREMENTTYPE_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_DATA_TYPE + COMMA
			+ COLUMN_IS_EDITABLE + COMMA
			+ COLUMN_IS_VISIBLE + COMMA
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ ctIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(characteristicType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(characteristicType.getModified()) + COMMA
			+ characteristicType.getCreatorId().toSQLString() + COMMA
			+ characteristicType.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + characteristicType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + characteristicType.getDescription() + APOSTOPHE + COMMA
			+ Integer.toString(characteristicType.getDataType().value()) + COMMA
			+ (characteristicType.getIsEditable()?"1":"0") + COMMA
			+ (characteristicType.getIsVisible()?"1":"0")
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicType_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicType_Database.insert | Cannot insert characteristic type " + ctIdStr;
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

	public void update(StorableObject storableObject, int update_kind, Object obj) throws IllegalDataException, UpdateObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}
