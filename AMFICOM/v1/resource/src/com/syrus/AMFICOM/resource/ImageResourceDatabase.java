/*
 * $Id: ImageResourceDatabase.java,v 1.37 2005/10/30 14:48:46 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author $Author: bass $
 * @version $Revision: 1.37 $, $Date: 2005/10/30 14:48:46 $
 * @module resource
 */

public final class ImageResourceDatabase extends StorableObjectDatabase<AbstractImageResource> {
	// table :: ImageResource
	private static final int SIZE_CODENAME_COLUMN_IR = 32;
	private static final int SIZE_FILENAME_COLUMN_IR = 256;

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

	@Override
	protected short getEntityCode() {
		return ObjectEntities.IMAGERESOURCE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ ImageResourceWrapper.COLUMN_FILENAME + COMMA
				+ COLUMN_IMAGE + COMMA
				+ COLUMN_SORT;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final AbstractImageResource storableObject)
			throws IllegalDataException {
		final ImageResourceSort sort = storableObject.getSort();
		String codename = null;
		String fileName = null;
		if(storableObject instanceof BitmapImageResource) {
			codename = ((BitmapImageResource) storableObject).getCodename();
		} else if (storableObject instanceof FileImageResource) {
			codename = ((FileImageResource) storableObject).getCodename();
			fileName = ((FileImageResource) storableObject).getFileName();
		} else if (storableObject instanceof SchemeImageResource) {
			// Do nothing
		} else {
			throw new IllegalDataException("ImageResourceDatabase.getUpdateSingleSQLValues | Illegal AbstractImageResource : "
					+ storableObject.getClass().getName());
		}

		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN_IR) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(fileName, SIZE_FILENAME_COLUMN_IR) + APOSTROPHE + COMMA
				+ SQL_FUNCTION_EMPTY_BLOB + COMMA
				+ sort.value();
		return sql;
	}

	@Override
	protected AbstractImageResource updateEntityFromResultSet(final AbstractImageResource storableObject, final ResultSet resultSet)
			throws RetrieveObjectException {
		try {
			final int sort = resultSet.getInt(COLUMN_SORT);
			if (sort == ImageResourceSort._BITMAP) {
				final BitmapImageResource bitmapImageResource = (storableObject == null)
						? new BitmapImageResource(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								StorableObjectVersion.ILLEGAL_VERSION,
								null,
								null)
							: (BitmapImageResource) storableObject;
				bitmapImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
						DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
						DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
						DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
						new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
						DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
						ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_IMAGE)));

				return bitmapImageResource;
			} else if (sort == ImageResourceSort._FILE) {
				final FileImageResource fileImageResource = (storableObject == null)
						? new FileImageResource(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								StorableObjectVersion.ILLEGAL_VERSION,
								null,
								null)
							: (FileImageResource) storableObject;
				fileImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
						DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
						DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
						DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
						new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
						DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
						DatabaseString.fromQuerySubString(resultSet.getString(ImageResourceWrapper.COLUMN_FILENAME)));
				return fileImageResource;
			} else if (sort == ImageResourceSort._SCHEME) {
				final SchemeImageResource schemeImageResource = (storableObject == null)
						? new SchemeImageResource(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								StorableObjectVersion.ILLEGAL_VERSION)
							: (SchemeImageResource) storableObject;
				schemeImageResource.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
						DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
						DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
						DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
						new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
						ByteArrayDatabase.toByteArray(resultSet.getBlob(COLUMN_IMAGE)));
				return schemeImageResource;
			}
			throw new RetrieveObjectException(this.getEntityName() + "Database.updateEntityFromResultSet | wrong sort " + sort);
		} catch (SQLException e) {
			Log.errorMessage("ImageResourceDatabase.updateEntityFromResultSet " + e.getMessage());
			throw new RetrieveObjectException("ImageResourceDatabase.updateEntityFromResultSet ", e);
		}
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final AbstractImageResource storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException {
		final int sort = storableObject.getSort().value();
		try {
			if (sort == ImageResourceSort._BITMAP) {
				BitmapImageResource bitmapImageResource = (BitmapImageResource) storableObject;
				DatabaseString.setString(preparedStatement,
						++startParameterNumber,
						bitmapImageResource.getCodename(),
						SIZE_CODENAME_COLUMN_IR);
				DatabaseString.setString(preparedStatement,	++startParameterNumber,	"", SIZE_FILENAME_COLUMN_IR);
				preparedStatement.setInt(++startParameterNumber, ImageResourceSort._BITMAP);
			} else if (sort == ImageResourceSort._FILE) {
				FileImageResource fileImageResource = (FileImageResource) storableObject;
				DatabaseString.setString(preparedStatement,
						++startParameterNumber,
						fileImageResource.getCodename(),
						SIZE_CODENAME_COLUMN_IR);
				DatabaseString.setString(preparedStatement,
						++startParameterNumber,
						fileImageResource.getFileName(),
						SIZE_FILENAME_COLUMN_IR);
				preparedStatement.setInt(++startParameterNumber, ImageResourceSort._FILE);
			} else if (sort == ImageResourceSort._SCHEME) {
				DatabaseString.setString(preparedStatement, ++startParameterNumber, "", SIZE_CODENAME_COLUMN_IR);
				DatabaseString.setString(preparedStatement,	++startParameterNumber,	"", SIZE_FILENAME_COLUMN_IR);
				preparedStatement.setInt(++startParameterNumber, ImageResourceSort._SCHEME);
			} else {
				throw new IllegalDataException("Unsupported ImageResourse sort =" + sort);
			}			
		} catch (SQLException sqle) {
			throw new IllegalDataException(this.getEntityName() + "Database.setEntityForPreparedStatement | Error "
					+ sqle.getMessage(), sqle);
		}
		return startParameterNumber;
	}

	public int getSort(final Identifier id) throws RetrieveObjectException {
		final String absIdStr = DatabaseIdentifier.toSQLString(id);
		final String sql = SQL_SELECT + COLUMN_SORT
				+ SQL_FROM + this.getEntityName()
				+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + absIdStr;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.getSort | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				return resultSet.getInt(COLUMN_SORT);
			}

			final String msg = this.getEntityName() + "Database.getSort couldn't find entity with id: " + id;
			throw new RetrieveObjectException(msg);
		} catch (SQLException sqle) {
			final String mesg = this.getEntityName() + "Database.insertImage | Cannot insert blob " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
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
				}
			} catch (SQLException sqle) {
				Log.errorMessage(sqle);
			}
		}
	}

	@Override
	public void insert(final Set<AbstractImageResource> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		for (final AbstractImageResource abstractImageResource : storableObjects) {
			if((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource)) {
				try {
					this.updateImage(abstractImageResource);
				} catch (UpdateObjectException e) {
					throw new CreateObjectException(this.getEntityName() + "Database.insert ", e);
				}
			}
		}
	}

	@Override
	public void update(final Set<AbstractImageResource> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		for (final AbstractImageResource abstractImageResource : storableObjects) {
			if ((abstractImageResource instanceof BitmapImageResource) || (abstractImageResource instanceof SchemeImageResource)) {
				this.updateImage(abstractImageResource);
			}
		}		
	}

	private void updateImage(final AbstractImageResource abstractImageResource) throws UpdateObjectException {
		final String idStr = DatabaseIdentifier.toSQLString(abstractImageResource.getId());
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			ByteArrayDatabase.saveAsBlob(abstractImageResource.getImage(),
					connection,
					ObjectEntities.IMAGERESOURCE,
					COLUMN_IMAGE,
					StorableObjectWrapper.COLUMN_ID + EQUALS + idStr);
			connection.commit();			
		} catch (SQLException sqle) {
			final String mesg = this.getEntityName() + "Database.insertImage | Cannot update image for resource '" + idStr
					+ "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			if (connection != null) {
				DatabaseConnection.releaseConnection(connection);
				connection = null;
			}
		}
	}

}
