/*
 * $Id: ImageResourceDatabase.java,v 1.29 2005/07/24 17:38:34 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;


import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.29 $, $Date: 2005/07/24 17:38:34 $
 * @module resource_v1
 */

public final class ImageResourceDatabase extends StorableObjectDatabase {
	// table :: ImageResource
	private static final int SIZE_CODENAME_COLUMN_IR = 256;

	/**
	 * codename  VARCHAR2(256)
	 */
	/**
	 * image BLOB
	 */
	public static final String COLUMN_IMAGE = "image";

	/**
	 * sort NUMBER(1)
	 */
	public static final String COLUMN_SORT = "sort";


	private static String columns;
	private static String updateMultipleSQLValues;

	private byte[] retrieveImage(final AbstractImageResource abstractImageResource) throws RetrieveObjectException {
		String absIdStr = DatabaseIdentifier.toSQLString(abstractImageResource.getId());
		String sql = SQL_SELECT + COLUMN_IMAGE + SQL_FROM + getEntityName() + SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + absIdStr;
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		byte[] image = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEntityName() + "Database.retrieveImage | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(COLUMN_IMAGE);
				image = ByteArrayDatabase.toByteArray(blob);
				if(abstractImageResource instanceof BitmapImageResource) {
					BitmapImageResource bitmapImageResource = (BitmapImageResource) abstractImageResource;
					bitmapImageResource.setImage(image);
				} else {
					SchemeImageResource schemeImageResource = (SchemeImageResource) abstractImageResource;
					schemeImageResource.setImage(image);
				}				
			}			
		} catch (SQLException sqle) {
			String mesg = getEntityName() + "Database.insertImage | Cannot insert blob " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return image;
		
	}

	private void updateImage(final AbstractImageResource abstractImageResource) throws UpdateObjectException {
		String absIdStr = DatabaseIdentifier.toSQLString(abstractImageResource.getId());
		String sql = SQL_UPDATE + getEntityName() + SQL_SET + COLUMN_IMAGE + EQUALS + SQL_FUNCTION_EMPTY_BLOB + SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + absIdStr;
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;		
		byte[] image = abstractImageResource.getImage();
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEntityName()
				+ "Database.updateImage | Trying: " + sql,
				Log.DEBUGLEVEL09);
			statement.executeQuery(sql);
			connection.commit();
			ByteArrayDatabase.saveAsBlob(image, connection, ObjectEntities.IMAGERESOURCE, COLUMN_IMAGE, StorableObjectWrapper.COLUMN_ID + EQUALS + absIdStr);
			connection.commit();			
		} catch (SQLException sqle) {
			String mesg = getEntityName() + "Database.insertImage | Cannot update blob " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}
	}

	private AbstractImageResource fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AbstractImageResource)
			return (AbstractImageResource) storableObject;
		throw new IllegalDataException("ImageResourceDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {
		return ObjectEntities.IMAGERESOURCE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ COLUMN_SORT;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject)
			throws IllegalDataException {
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
			throw new IllegalDataException("ImageResourceDatabase.getUpdateSingleSQLValues | Illegal AbstractImageResource : " + abstractImageResource.getClass().getName());
		}

		String sql = APOSTROPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN_IR) + APOSTROPHE + COMMA
				+ sort.value();
		return sql;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws RetrieveObjectException {
		try {
			final int sort = resultSet.getInt(COLUMN_SORT);
		if (sort == ImageResourceSort._BITMAP) {
			BitmapImageResource bitmapImageResource;
			if (storableObject == null) {
				bitmapImageResource = new BitmapImageResource(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null, 0L, null, null);
			} else {
				bitmapImageResource = (BitmapImageResource) storableObject;
			}
			final byte[] image = retrieveImage(bitmapImageResource);
			bitmapImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				image);
			
			return bitmapImageResource;
		} else if (sort == ImageResourceSort._FILE) {
			FileImageResource fileImageResource;
			if (storableObject == null) {
				fileImageResource = new FileImageResource(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
							null,
							0L,
							null);
			} else {
				fileImageResource = (FileImageResource) storableObject;
			}
			fileImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
					DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
					resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
					DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)));
			return fileImageResource;
		} else if (sort == ImageResourceSort._SCHEME) {
			SchemeImageResource schemeImageResource;
			if (storableObject == null) {
				schemeImageResource = new SchemeImageResource(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null, 0L);
			} else {
				schemeImageResource = (SchemeImageResource) storableObject;
			}
			final byte[] image = retrieveImage(schemeImageResource);
			schemeImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
					DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
					resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
					image);			
			return schemeImageResource;
		}
		throw new RetrieveObjectException(getEntityName() + "Database.updateEntityFromResultSet | wrong sort " + sort);				
	} catch (SQLException e) {
		Log.errorMessage("ImageResourceDatabase.updateEntityFromResultSet " + e.getMessage() );
		throw new RetrieveObjectException("ImageResourceDatabase.updateEntityFromResultSet ", e);
	}
	}
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException {
		final AbstractImageResource abstractImageResource = this.fromStorableObject(storableObject);
		final int sort = abstractImageResource.getSort().value();
		try {
			if(sort == ImageResourceSort._BITMAP) {
				BitmapImageResource bitmapImageResource = (BitmapImageResource) abstractImageResource;
				DatabaseString.setString(preparedStatement, ++startParameterNumber, bitmapImageResource.getCodename(), SIZE_CODENAME_COLUMN_IR);
				preparedStatement.setInt(++startParameterNumber, ImageResourceSort._BITMAP);
			} else if(sort == ImageResourceSort._FILE) {
				FileImageResource fileImageResource = (FileImageResource) abstractImageResource;
				DatabaseString.setString(preparedStatement, ++startParameterNumber, fileImageResource.getFileName(), SIZE_CODENAME_COLUMN_IR);
				preparedStatement.setInt(++startParameterNumber, ImageResourceSort._FILE);
			} else if(sort == ImageResourceSort._SCHEME) {
				DatabaseString.setString(preparedStatement, ++startParameterNumber, "", SIZE_CODENAME_COLUMN_IR);
				preparedStatement.setInt(++startParameterNumber, ImageResourceSort._SCHEME);
			} else {
				throw new IllegalDataException("Unsupported ImageResourse sort =" + sort);
			}			
		} catch (SQLException sqle) {
			throw new IllegalDataException(getEntityName()
					+ "Database.setEntityForPreparedStatement | Error "
					+ sqle.getMessage(), sqle);
		}
		return startParameterNumber;
	}

	public int getSort(final Identifier id) throws RetrieveObjectException {
		final String absIdStr = DatabaseIdentifier.toSQLString(id);
		final String sql = SQL_SELECT + COLUMN_SORT + SQL_FROM + getEntityName() + SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + absIdStr;
		final Connection connection = DatabaseConnection.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEntityName() + "Database.getSort | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return resultSet.getInt(COLUMN_SORT);

			final String msg = getEntityName() + "Database.getSort couldn't find entity with id: " + id;
			throw new RetrieveObjectException(msg);			
		} catch (SQLException sqle) {
			String mesg = getEntityName() + "Database.insertImage | Cannot insert blob " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			DatabaseConnection.releaseConnection(connection);
		}    	
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		for(final Iterator it = storableObjects.iterator();it.hasNext();){
			final AbstractImageResource abstractImageResource = this.fromStorableObject((StorableObject)it.next());
			if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource)) {
				try {
					this.updateImage(abstractImageResource);
				} catch (UpdateObjectException e) {
					throw new CreateObjectException(getEntityName() + "Database.insert ", e);
				}
			}
		}

	}

	@Override
	public void update(final Set storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		for (final Iterator it = storableObjects.iterator(); it.hasNext();) {
			AbstractImageResource abstractImageResource;
			try {
				abstractImageResource = fromStorableObject((AbstractImageResource) it.next());
			} catch (IllegalDataException e) {
				throw new UpdateObjectException("ImageResourceDatabase.update | object isn't AbstractImageResource");
			}
			if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource))
				updateImage(abstractImageResource);
		}
		
	}

}
