/*
 * $Id: ImageResourceDatabase.java,v 1.4 2005/01/17 17:13:04 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;


import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author $Author: max $
 * @version $Revision: 1.4 $, $Date: 2005/01/17 17:13:04 $
 * @module resource_v1
 */

public final class ImageResourceDatabase extends StorableObjectDatabase {
	// table :: ImageResource
	/**
	 * Shadowing is ok, as this table does have codename 256 chars long.
	 * codename  VARCHAR2(256)
	 */
	private static final int SIZE_CODENAME_COLUMN = 256;

	/**
	 * codename  VARCHAR2(256)
	 */
	public static final String COLUMN_CODENAME = "codename"; //$NON-NLS-1$

	/**
	 * image BLOB
	 */
	public static final String COLUMN_IMAGE = "image"; //$NON-NLS-1$

	/**
	 * sort NUMBER(1)
	 */
	public static final String COLUMN_SORT = "sort"; //$NON-NLS-1$


	private static String columns;
	private static String updateMultiplySQLValues;
    
	private void insertImage(AbstractImageResource abstractImageResource) throws CreateObjectException {
		String sql = SQL_INSERT_INTO + getEnityName()		
			+ OPEN_BRACKET + COLUMN_IMAGE + CLOSE_BRACKET 
			+ SQL_VALUES 
			+ OPEN_BRACKET + SQL_EMPTY_BLOB + CLOSE_BRACKET
			+ SQL_WHERE + COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(abstractImageResource.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		byte[] image = abstractImageResource.getImage();
		try {
			statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
			ByteArrayDatabase.saveAsBlob(image, connection, ObjectEntities.IMAGE_RESOURCE_ENTITY, COLUMN_IMAGE, COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(abstractImageResource.getId()));
			connection.commit();
		} catch (SQLException sqle) {
			String mesg = getEnityName() + "Database.insertImage | Cannot insert blob " + sqle.getMessage(); //$NON-NLS-1$
			throw new CreateObjectException(mesg, sqle);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}
	}
    
	private void retrieveImage(AbstractImageResource abstractImageResource) throws RetrieveObjectException {
		String absIdStr = DatabaseIdentifier.toSQLString(abstractImageResource.getId());
		String sql = SQL_SELECT + COLUMN_IMAGE + SQL_FROM + getEnityName() + SQL_WHERE + COLUMN_ID + EQUALS + absIdStr;
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEnityName() + "Database.retrieveImage | Trying: " + sql, Log.DEBUGLEVEL09); //$NON-NLS-1$
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(COLUMN_IMAGE);
				byte[] image = ByteArrayDatabase.toByteArray(blob);
				if(abstractImageResource instanceof BitmapImageResource) { 
					BitmapImageResource bitmapImageResource = (BitmapImageResource) abstractImageResource;
					bitmapImageResource.setImage(image);
				} else {
					SchemeImageResource schemeImageResource = (SchemeImageResource) abstractImageResource;
					schemeImageResource.setImage(image);
				}
			}			
		} catch (SQLException sqle) {
			String mesg = getEnityName() + "Database.insertImage | Cannot insert blob " + sqle.getMessage(); //$NON-NLS-1$
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}
	}
    
	private void updateImage(AbstractImageResource abstractImageResource) throws UpdateObjectException {
		String absIdStr = DatabaseIdentifier.toSQLString(abstractImageResource.getId());
		String sql = SQL_UPDATE + getEnityName() + SQL_SET + COLUMN_IMAGE + EQUALS + SQL_EMPTY_BLOB + SQL_WHERE + COLUMN_ID + EQUALS + absIdStr;
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;		
		byte[] image = abstractImageResource.getImage();
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEnityName()
				+ "Database.checkAndUpdateEntity | Trying: " + sql, //$NON-NLS-1$
				Log.DEBUGLEVEL09);
			statement.executeQuery(sql);
			connection.commit();
			ByteArrayDatabase.saveAsBlob(image, connection, ObjectEntities.IMAGE_RESOURCE_ENTITY, COLUMN_IMAGE, COLUMN_ID + EQUALS + absIdStr);
			connection.commit();			
		} catch (SQLException sqle) {
			String mesg = getEnityName() + "Database.insertImage | Cannot insert blob " + sqle.getMessage(); //$NON-NLS-1$
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}
	}
    
	private AbstractImageResource fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AbstractImageResource)
			return (AbstractImageResource) storableObject;
		throw new IllegalDataException("ImageResourceDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName()); //$NON-NLS-1$
	}
    
	protected String getEnityName() {
		return ObjectEntities.IMAGE_RESOURCE_ENTITY;
	}    
    
	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_SORT;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		AbstractImageResource abstractImageResource = fromStorableObject(storableObject);
		ImageResourceSort sort = abstractImageResource.getSort();
		String codename = null;
		if(abstractImageResource instanceof BitmapImageResource) { 
			codename = ((BitmapImageResource) abstractImageResource).getCodename();        
		} else if (abstractImageResource instanceof FileImageResource) {
			codename = ((FileImageResource) abstractImageResource).getCodename();
		} else if (abstractImageResource instanceof SchemeImageResource) {
			// Do nothing
		} else {
			throw new IllegalDataException("ImageResourceDatabase.getUpdateSingleSQLValues | Illegal AbstractImageResource : " + abstractImageResource.getClass().getName()); //$NON-NLS-1$
		}            

		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + sort.value() + APOSTOPHE + COMMA;            
		return sql;
	}
    
	public int getSort(Identifier id) throws RetrieveObjectException {
		String absIdStr = DatabaseIdentifier.toSQLString(id);
		String sql = SQL_SELECT + COLUMN_SORT + SQL_FROM + getEnityName() + SQL_WHERE + COLUMN_ID + EQUALS + absIdStr;
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEnityName() + "Database.getSort | Trying: " + sql, Log.DEBUGLEVEL09); //$NON-NLS-1$
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return resultSet.getInt(COLUMN_SORT);
			
			String msg = getEnityName() + "Database.getSort couldn't find entity with id: " + id; //$NON-NLS-1$
			throw new RetrieveObjectException(msg);			
		} catch (SQLException sqle) {
			String mesg = getEnityName() + "Database.insertImage | Cannot insert blob " + sqle.getMessage(); //$NON-NLS-1$
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}    	
	}
            
	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		AbstractImageResource abstractImageResource = this.fromStorableObject(storableObject);
		super.insertEntity(abstractImageResource);
		if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource))
			this.insertImage(abstractImageResource);		
	}
    
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		for(Iterator it = storableObjects.iterator();it.hasNext();){
			AbstractImageResource abstractImageResource = this.fromStorableObject((StorableObject)it.next());
			if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource))
				this.insertImage(abstractImageResource);
		}

	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AbstractImageResource abstractImageResource = this.fromStorableObject(storableObject);
		super.retrieveEntity(abstractImageResource);
		if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource))
			this.retrieveImage(abstractImageResource);
	}

	public List retrieveByCondition(List ids,
			StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list = null;
		if (condition instanceof StringFieldCondition){
			StringFieldCondition stringFieldCondition = (StringFieldCondition)condition;
			String sqlCondition = new String();
			sqlCondition = COLUMN_SORT + EQUALS + stringFieldCondition.getSort(); 
			try {
				list = this.retrieveByIds(ids, sqlCondition);
			} catch (IllegalDataException e) {
				String msg = "ImageResourceDatabase.retrieveByCondition | IllegalDataException: " + e.getMessage(); //$NON-NLS-1$
				throw new RetrieveObjectException(msg, e);
			} catch (RetrieveObjectException e) {
				String msg = "ImageResourceDatabase.retrieveByCondition | RetrieveObjectException: " + e.getMessage(); //$NON-NLS-1$
				throw new RetrieveObjectException(msg, e);
			}			
		} else {
			Log.errorMessage("ImageResourceDatabase.retrieveByCondition | Unknown condition class: " + condition); //$NON-NLS-1$
			list = this.retrieveButIds(ids);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		list = super.retrieveByIdsOneQuery(ids, condition);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			AbstractImageResource abstractImageResource = this.fromStorableObject((AbstractBitmapImageResource)iter.next());
			if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource))
				this.retrieveImage(abstractImageResource);
		}
		return list;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		// AbstractImageResource abstractImageResource = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
		default:
			return null;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntities(storableObjects, true);
				return;
		}
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			AbstractImageResource abstractImageResource = fromStorableObject((AbstractImageResource) it.next());
			if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource))
				updateImage(abstractImageResource);
		}
		
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		AbstractImageResource abstractImageResource = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntity(storableObject, true);
				return;			
		}
		if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource))
			updateImage(fromStorableObject(storableObject));
	}
	

	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException,
			SQLException {
		int sort = resultSet.getInt(COLUMN_SORT);
		if (sort == ImageResourceSort._BITMAP) {
			BitmapImageResource bitmapImageResource;
			if (storableObject == null) {
				bitmapImageResource = new BitmapImageResource(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
					null, null, null, null, null, null);
			} else {
				bitmapImageResource = (BitmapImageResource) storableObject;
			}

			bitmapImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
				ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_IMAGE)));
			return bitmapImageResource;
		} else if (sort == ImageResourceSort._FILE) {
			FileImageResource fileImageResource;
			if (storableObject == null) {
				fileImageResource = new FileImageResource(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null, null, null, null, null);
			} else {
				fileImageResource = (FileImageResource) storableObject;
			}
			fileImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
					DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
					DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
					DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
					DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)));
			return fileImageResource;
		} else if (sort == ImageResourceSort._SCHEME) {
			SchemeImageResource schemeImageResource;
			if (storableObject == null) {
				schemeImageResource = new SchemeImageResource(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null, null, null, null);
			} else {
				schemeImageResource = (SchemeImageResource) storableObject;
			}			
			schemeImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
					DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
					DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
					DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
					ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_IMAGE)));
			return schemeImageResource;
		}
		throw new RetrieveObjectException(getEnityName() + "Database.updateEntityFromResultSet | wrong sort " + sort);				 //$NON-NLS-1$
	}
}
