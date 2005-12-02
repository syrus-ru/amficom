/*-
 * $Id: CollectorDatabase.java,v 1.52 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.52 $, $Date: 2005/12/02 11:24:13 $
 * @author $Author: bass $
 * @module map
 */
public final class CollectorDatabase extends StorableObjectDatabase<Collector> {
	private static String columns;

	private static String updateMultipleSQLValues;

	private void retrievePhysicalLinks(final Set<Collector> collectors) throws RetrieveObjectException {
		if (collectors == null || collectors.isEmpty())
			return;
		final java.util.Map<Identifier, Set<Identifier>> map = super.retrieveLinkedEntityIds(collectors,
				CollectorWrapper.COLLECTOR_PHYSICAL_LINK,
				CollectorWrapper.LINK_COLUMN_COLLECTOR_ID,
				CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
		for (final Collector collector : collectors) {
			final Identifier collectorId = collector.getId();
			final Set<Identifier> physicalLinkIds = map.get(collectorId);
			if(physicalLinkIds != null) {
				collector.setPhysicalLinkIds0(physicalLinkIds);
			}
		}
	}

	@Override
	protected short getEntityCode() {		
		return COLLECTOR_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA + StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA + QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Collector storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Collector storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return values;
	}

	@Override
	protected Collector updateEntityFromResultSet(final Collector storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final Collector collector = (storableObject == null)
				? new Collector(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null)
					: storableObject;

		collector.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return collector;
	}

	@Override
	protected void insert(final Set<Collector> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		try {
			this.updatePhysicalLinks(storableObjects);
		} catch (final UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	protected void update(final Set<Collector> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updatePhysicalLinks(storableObjects);
	}	

	private void updatePhysicalLinks(final Set<Collector> collectors) throws UpdateObjectException {
		if (collectors == null || collectors.isEmpty())
			return;
		final java.util.Map<Identifier, Set<Identifier>> collectorIdPhysicalLinkIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (Collector collector: collectors) {
			final Set<PhysicalLink> physicalLinks = collector.getPhysicalLinks();
			final Set<Identifier> physicalLinkIds = Identifier.createIdentifiers(physicalLinks);
			collectorIdPhysicalLinkIdsMap.put(collector.getId(), physicalLinkIds);
		}

		super.updateLinkedEntityIds(collectorIdPhysicalLinkIdsMap,
				CollectorWrapper.COLLECTOR_PHYSICAL_LINK,
				CollectorWrapper.LINK_COLUMN_COLLECTOR_ID,
				CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
	}

	@Override
	public void delete(final Set<? extends Identifiable> ids) {
		final StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM + CollectorWrapper.COLLECTOR_PHYSICAL_LINK + SQL_WHERE);
		stringBuffer.append(idsEnumerationString(ids, CollectorWrapper.LINK_COLUMN_COLLECTOR_ID, true));

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("CollectorDatabase.delete(Collection) | Trying: " + stringBuffer, Log.DEBUGLEVEL09);
			statement.executeUpdate(stringBuffer.toString());
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorMessage(sqle);
		} finally {
			try {
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
			} catch (SQLException sqle) {
				Log.errorMessage(sqle);
			}
		}

		super.delete(ids);
	}	

	@Override
	protected Set<Collector> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Collector> collection = super.retrieveByCondition(conditionQuery);
		this.retrievePhysicalLinks(collection);
		return collection;
	}
}


