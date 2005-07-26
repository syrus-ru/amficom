/*
 * $Id: SiteNodeDatabase.java,v 1.31 2005/07/26 11:41:05 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.31 $, $Date: 2005/07/26 11:41:05 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class SiteNodeDatabase extends StorableObjectDatabase {
	private static String columns;

	private static String updateMultipleSQLValues;

	private SiteNode fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SiteNode)
			return (SiteNode) storableObject;
		throw new IllegalDataException("SiteNodeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.SITENODE_CODE;
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
				+ SiteNodeWrapper.COLUMN_BUILDING;
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
				+ QUESTION
				+ QUESTION
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final SiteNode siteNode = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, siteNode.getLocation().getX());
		preparedStatement.setDouble(++startParameterNumber, siteNode.getLocation().getY());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, siteNode.getImageId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, siteNode.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getCity(), MarkDatabase.SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getStreet(), MarkDatabase.SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNode.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final SiteNode siteNode = this.fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(siteNode.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(siteNode.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ siteNode.getLocation().getX() + COMMA
			+ siteNode.getLocation().getY() + COMMA
			+ DatabaseIdentifier.toSQLString(siteNode.getImageId()) + COMMA
			+ DatabaseIdentifier.toSQLString(siteNode.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getCity(), MarkDatabase.SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getStreet(), MarkDatabase.SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		return values;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
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
				null) : this.fromStorableObject(storableObject);

		SiteNodeType type;
		try {
			type = (SiteNodeType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
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
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getDouble(SiteNodeWrapper.COLUMN_LONGITUDE),
				resultSet.getDouble(SiteNodeWrapper.COLUMN_LATIUDE),
				DatabaseIdentifier.getIdentifier(resultSet, SiteNodeWrapper.COLUMN_IMAGE_ID),
				type,
				DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_CITY)),
				DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_STREET)),
				DatabaseString.fromQuerySubString(resultSet.getString(SiteNodeWrapper.COLUMN_BUILDING)));
		return siteNode;
	}

}
