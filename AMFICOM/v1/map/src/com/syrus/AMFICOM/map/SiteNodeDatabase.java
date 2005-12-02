/*
 * $Id: SiteNodeDatabase.java,v 1.37 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.37 $, $Date: 2005/12/02 11:24:13 $
 * @author $Author: bass $
 * @module map
 */
public final class SiteNodeDatabase extends StorableObjectDatabase<SiteNode> {
	private static String columns;

	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return SITENODE_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ SiteNodeWrapper.COLUMN_LONGITUDE + COMMA
				+ SiteNodeWrapper.COLUMN_LATIUDE + COMMA
				+ SiteNodeWrapper.COLUMN_IMAGE_ID + COMMA
				+ SiteNodeWrapper.COLUMN_SITE_NODE_TYPE_ID + COMMA
				+ SiteNodeWrapper.COLUMN_CITY + COMMA
				+ SiteNodeWrapper.COLUMN_STREET + COMMA
				+ SiteNodeWrapper.COLUMN_BUILDING + COMMA
				+ SiteNodeWrapper.COLUMN_ATTACHMENT_SITE_NODE_ID;
		}
		return columns;
	}	

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final SiteNode storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLocation().getX());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLocation().getY());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getImageId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCity(), MarkDatabase.SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getStreet(), MarkDatabase.SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAttachmentSiteNodeId());
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final SiteNode storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ storableObject.getLocation().getX() + COMMA
			+ storableObject.getLocation().getY() + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getImageId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getCity(), MarkDatabase.SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getStreet(), MarkDatabase.SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getAttachmentSiteNodeId());
		return values;
	}

	@Override
	protected SiteNode updateEntityFromResultSet(final SiteNode storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final SiteNode siteNode = (storableObject == null) ? new SiteNode(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID),
				null,
				StorableObjectVersion.ILLEGAL_VERSION,
				null,
				null,
				null,
				null,
				0.0,
				0.0,
				null,
				null,
				null) : storableObject;

		SiteNodeType type;
		try {
			type = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					SiteNodeWrapper.COLUMN_SITE_NODE_TYPE_ID), true);
		} catch (ApplicationException ae) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		siteNode.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getDouble(SiteNodeWrapper.COLUMN_LONGITUDE),
				resultSet.getDouble(SiteNodeWrapper.COLUMN_LATIUDE),
				DatabaseIdentifier.getIdentifier(resultSet, SiteNodeWrapper.COLUMN_IMAGE_ID),
				type,
				DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_CITY)),
				DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_STREET)),
				DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_BUILDING)),
				DatabaseIdentifier.getIdentifier(resultSet, SiteNodeWrapper.COLUMN_ATTACHMENT_SITE_NODE_ID));
		return siteNode;
	}

}
