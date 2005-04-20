/*
 * $Id: CollectorDatabase.java,v 1.27 2005/04/20 07:53:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
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
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.27 $, $Date: 2005/04/20 07:53:47 $
 * @author $Author: bass $
 * @module map_v1
 */
public class CollectorDatabase extends CharacterizableDatabase {
	private static String columns;

	private static String updateMultipleSQLValues;

	private static final String COLLECTOR_PHYSICAL_LINK = "CollPhLink";

	 private Collector fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Collector)
			return (Collector) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Collector collector = this.fromStorableObject(storableObject);
		super.retrieveEntity(collector);
		this.retrievePhysicalLinks(Collections.singleton(collector));
	}

	private void retrievePhysicalLinks(Set collectors) throws RetrieveObjectException {
		if (collectors == null || collectors.isEmpty())
			return;
		java.util.Map map = super.retrieveLinkedEntityIds(collectors,
				COLLECTOR_PHYSICAL_LINK,
				CollectorWrapper.LINK_COLUMN_COLLECTOR_ID,
				CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Collector collector = (Collector) it.next();
			Set physicalLinkIds = (Set) map.get(collector);
			try {
				collector.setPhysicalLinks0(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));
			}
			catch (ApplicationException e) {
				throw new RetrieveObjectException(e);
			}
		}
	}

	protected String getEnityName() {
		return ObjectEntities.COLLECTOR_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA + StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA + QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		Collector collector = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, collector.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, collector.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Collector collector = fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(collector.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(collector.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		Collector collector = (storableObject == null) ? new Collector(DatabaseIdentifier.getIdentifier(resultSet,
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Collector collector = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + collector.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		Collector collector = this.fromStorableObject(storableObject);
		super.insertEntity(collector);
		try {
			this.updatePhysicalLinks(Collections.singleton(storableObject));
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}

	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			this.updatePhysicalLinks(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException,
				UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				return;
		}
		this.updatePhysicalLinks(Collections.singleton(storableObject));
	}

	public void update(Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException,
				UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				return;
		}
		this.updatePhysicalLinks(storableObjects);
	}	

	private void updatePhysicalLinks(Set collectors) throws UpdateObjectException {
		if (collectors == null || collectors.isEmpty())
			return;
		java.util.Map collectorIdPhysicalLinkIdsMap = new HashMap();
		for (Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector;
			try {
				collector = this.fromStorableObject((StorableObject) it.next());
			}
			catch (IllegalDataException e) {
				throw new UpdateObjectException("CollectorDatabase.updatePhysicalLinks | cannot get collector ", e);
			}
			Set physicalLinks = collector.getPhysicalLinks();
			Set physicalLinkIds = new HashSet(physicalLinks.size());
			for (Iterator iter = physicalLinks.iterator(); iter.hasNext();) {
				PhysicalLink physicalLink = (PhysicalLink) iter.next();
				physicalLinkIds.add(physicalLink.getId());
			}
			collectorIdPhysicalLinkIdsMap.put(collector.getId(), physicalLinkIds);
		}

		super.updateLinkedEntityIds(collectorIdPhysicalLinkIdsMap,
				COLLECTOR_PHYSICAL_LINK,
				CollectorWrapper.LINK_COLUMN_COLLECTOR_ID,
				CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID);
	}

	public void delete(Identifier id) {
		this.delete(Collections.singleton(id));
	}

	public void delete(Set ids) {
		StringBuffer stringBuffer = new StringBuffer(SQL_DELETE_FROM + COLLECTOR_PHYSICAL_LINK + SQL_WHERE);
		stringBuffer.append(idsEnumerationString(ids, CollectorWrapper.LINK_COLUMN_COLLECTOR_ID, true));

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("CollectorDatabase.delete(Collection) | Trying: " + stringBuffer, Log.DEBUGLEVEL09);
			statement.executeUpdate(stringBuffer.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorException(sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle) {
				Log.errorException(sqle);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		super.delete(ids);
	}	

	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Set collection = super.retrieveByCondition(conditionQuery);
		this.retrievePhysicalLinks(collection);
		return collection;
	}
}


