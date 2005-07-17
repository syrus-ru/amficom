/*-
 * $Id: CollectorDatabase.java,v 1.36 2005/07/17 05:20:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.36 $, $Date: 2005/07/17 05:20:43 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class CollectorDatabase extends StorableObjectDatabase {
	private static String columns;

	private static String updateMultipleSQLValues;

	private static final String COLLECTOR_PHYSICAL_LINK = "CollPhLink";

	 private Collector fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Collector)
			return (Collector) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	 @Override
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final Collector collector = this.fromStorableObject(storableObject);
		super.retrieveEntity(collector);
		this.retrievePhysicalLinks(Collections.singleton(collector));
	}

	private void retrievePhysicalLinks(final Set<Collector> collectors) throws RetrieveObjectException {
		if (collectors == null || collectors.isEmpty())
			return;
		final java.util.Map<Identifier, Set<Identifier>> map = super.retrieveLinkedEntityIds(collectors,
				COLLECTOR_PHYSICAL_LINK,
				CollectorWrapper.LINK_COLUMN_COLLECTOR_ID,
				CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
		for (final Collector collector : collectors) {
			final Identifier collectorId = collector.getId();
			final Set<Identifier> physicalLinkIds = map.get(collectorId);
			try {
				final Set<PhysicalLink> physicalLinks = StorableObjectPool.getStorableObjects(physicalLinkIds, true);
				collector.setPhysicalLinks0(physicalLinks);
			} catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.COLLECTOR_CODE;
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
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Collector collector = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, collector.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, collector.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		final Collector collector = fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(collector.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(collector.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return values;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final Collector collector = (storableObject == null) ? new Collector(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, null) : fromStorableObject(storableObject);

		collector.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return collector;
	}

	@Override
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException {
		final Collector collector = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + collector.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		super.insert(storableObject);
		try {
			this.updatePhysicalLinks(Collections.singleton(storableObject));
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}

	}

	@Override
	public void insert(final Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		try {
			this.updatePhysicalLinks(storableObjects);
		} catch (final UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final StorableObject storableObject, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException,
				UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		this.updatePhysicalLinks(Collections.singleton(storableObject));
	}

	@Override
	public void update(final Set<? extends StorableObject> storableObjects, final Identifier modifierId, final UpdateKind updateKind)
			throws VersionCollisionException,
				UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		this.updatePhysicalLinks(storableObjects);
	}	

	private void updatePhysicalLinks(final Set collectors) throws UpdateObjectException {
		if (collectors == null || collectors.isEmpty())
			return;
		final java.util.Map<Identifier, Set<Identifier>> collectorIdPhysicalLinkIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector;
			try {
				collector = this.fromStorableObject((StorableObject) it.next());
			} catch (IllegalDataException e) {
				throw new UpdateObjectException("CollectorDatabase.updatePhysicalLinks | cannot get collector ", e);
			}
			final Set<PhysicalLink> physicalLinks = collector.getPhysicalLinks();
			final Set<Identifier> physicalLinkIds = Identifier.createIdentifiers(physicalLinks);
			collectorIdPhysicalLinkIdsMap.put(collector.getId(), physicalLinkIds);
		}

		super.updateLinkedEntityIds(collectorIdPhysicalLinkIdsMap,
				COLLECTOR_PHYSICAL_LINK,
				CollectorWrapper.LINK_COLUMN_COLLECTOR_ID,
				CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
	}

	@Override
	public void delete(final Identifier id) {
		this.delete(Collections.singleton(id));
	}

	@Override
	public void delete(final Set<? extends Identifiable> ids) {
		final StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM + COLLECTOR_PHYSICAL_LINK + SQL_WHERE);
		stringBuffer.append(idsEnumerationString(ids, CollectorWrapper.LINK_COLUMN_COLLECTOR_ID, true));

		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("CollectorDatabase.delete(Collection) | Trying: " + stringBuffer, Log.DEBUGLEVEL09);
			statement.executeUpdate(stringBuffer.toString());
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorException(sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle) {
				Log.errorException(sqle);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		super.delete(ids);
	}	

	@Override
	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Collector> collection = super.retrieveByCondition(conditionQuery);
		this.retrievePhysicalLinks(collection);
		return collection;
	}
}


