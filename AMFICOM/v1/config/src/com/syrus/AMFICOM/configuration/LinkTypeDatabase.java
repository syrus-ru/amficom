/*
 * $Id: LinkTypeDatabase.java,v 1.5 2004/11/16 12:33:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.5 $, $Date: 2004/11/16 12:33:17 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class LinkTypeDatabase extends StorableObjectDatabase {
	 // codename VARCHAR2(32) NOT NULL,
    public static final String COLUMN_CODENAME      = "codename";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
	public static final String COLUMN_NAME = "name";
    // sort NUMBER(2,0),
    public static final String COLUMN_SORT  = "sort";
    // manufacturer VARCHAR2(64),
    public static final String COLUMN_MANUFACTURER  = "manufacturer";
    // manufacturer_code VARCHAR2(64),
    public static final String COLUMN_MANUFACTURER_CODE     = "manufacturer_code";
    // image_id VARCHAR2(32),
    public static final String COLUMN_IMAGE_ID      = "image_id";
	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return ObjectEntities.LINKTYPE_ENTITY;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
	return updateMultiplySQLValues;
	}
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_SORT + COMMA
				+ COLUMN_MANUFACTURER + COMMA
				+ COLUMN_MANUFACTURER_CODE + COMMA
				+ COLUMN_IMAGE_ID;
		}
		return columns;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		LinkType linkType = fromStorableObject(storableObject);
		String name = DatabaseString.toQuerySubString(linkType.getName());
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getCodename()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getDescription()) + APOSTOPHE 
			+ APOSTOPHE + (name != null ? name : "") + APOSTOPHE + COMMA
			+ linkType.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getManufacturer()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getManufacturerCode()) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(linkType.getImageId());
		return sql;
	}
	
	private LinkType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof LinkType)
			return (LinkType)storableObject;
		throw new IllegalDataException("LinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		LinkType linkType = this.fromStorableObject(storableObject);
		super.retrieveEntity(linkType);
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		LinkType linkType = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, linkType.getCodename());
			preparedStatement.setString( ++i, linkType.getDescription());
			preparedStatement.setString( ++i, linkType.getName());
			preparedStatement.setInt( ++i, linkType.getSort().value());
			preparedStatement.setString( ++i, linkType.getManufacturer());
			preparedStatement.setString( ++i, linkType.getManufacturerCode());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, linkType.getImageId());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("LinkDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		LinkType linkType = storableObject == null ? null : fromStorableObject(storableObject);
		if (linkType == null){
			linkType = new LinkType(DatabaseIdentifier.getIdentifier(resultSet,COLUMN_ID), null, null, null, null, 0,
										 null, null, null);			
		}
		linkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),									
									DatabaseIdentifier.getIdentifier(resultSet,COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet,COLUMN_MODIFIER_ID),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
									resultSet.getInt(COLUMN_SORT),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MANUFACTURER)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MANUFACTURER_CODE)),									
									DatabaseIdentifier.getIdentifier(resultSet,COLUMN_IMAGE_ID));

		
		return linkType;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		LinkType linkType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		LinkType linkType = this.fromStorableObject(storableObject);
		super.insertEntity(linkType);		
	}

	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
	}
	
	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
		break;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
	}
	
		
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("LinkTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	public void delete(LinkType linkType) {
		String ltIdStr = DatabaseIdentifier.toSQLString(linkType.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.LINKTYPE_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ ltIdStr;
			Log.debugMessage("LinkTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
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
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
	}
	
	public List retrieveByIds(List ids, String condition) 
			throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return super.retrieveByIdsOneQuery(null, condition);
		return super.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		return this.retrieveButIds(ids);
	}
	
}
