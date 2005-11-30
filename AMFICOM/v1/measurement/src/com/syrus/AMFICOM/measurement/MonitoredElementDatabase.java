/*
 * $Id: MonitoredElementDatabase.java,v 1.10 2005/11/30 14:55:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.TableNames.EQUIPMENT_ME_LINK;
import static com.syrus.AMFICOM.general.TableNames.TRANSMISSIONPATH_ME_LINK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.DomainMember;
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
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.10 $, $Date: 2005/11/30 14:55:26 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MonitoredElementDatabase extends StorableObjectDatabase<MonitoredElement> {
	private static final int SIZE_LOCAL_ADDRESS_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MONITOREDELEMENT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
					+ StorableObjectWrapper.COLUMN_NAME + COMMA + MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID
					+ COMMA + MonitoredElementWrapper.COLUMN_SORT + COMMA
					+ MonitoredElementWrapper.COLUMN_LOCAL_ADDRESS;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final MonitoredElement storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getDomainId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementPortId()) + COMMA
				+ storableObject.getSort().value() + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final MonitoredElement storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementPortId());
		preparedStatement.setInt(++startParameterNumber, storableObject.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLocalAddress(),
			SIZE_LOCAL_ADDRESS_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected MonitoredElement updateEntityFromResultSet(final MonitoredElement storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		MonitoredElement monitoredElement = (storableObject == null)
				? monitoredElement = new MonitoredElement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						0,
						null,
						null)
				: storableObject;
		monitoredElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseIdentifier.getIdentifier(resultSet, MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID),
				resultSet.getInt(MonitoredElementWrapper.COLUMN_SORT),
				DatabaseString.fromQuerySubString(resultSet.getString(MonitoredElementWrapper.COLUMN_LOCAL_ADDRESS)));
		return monitoredElement;
	}

	private void retrieveMonitoredDomainMemberIdsByOneQuery(final Set<MonitoredElement> monitoredElements)
			throws RetrieveObjectException {
		final Map<Integer, Set<MonitoredElement>> sortedMonitoredElements = new HashMap<Integer, Set<MonitoredElement>>();

		for (final MonitoredElement monitoredElement : monitoredElements) {
			final Integer meSort = new Integer(monitoredElement.getSort().value());
			Set<MonitoredElement> monitoredElementsOneSort = sortedMonitoredElements.get(meSort);
			if (monitoredElementsOneSort == null) {
				monitoredElementsOneSort = new HashSet<MonitoredElement>();
				sortedMonitoredElements.put(meSort, monitoredElementsOneSort);
			}
			monitoredElementsOneSort.add(monitoredElement);
		}

		for (final Integer meSort : sortedMonitoredElements.keySet()) {
			final Set<MonitoredElement> monitoredElementsOneSort = sortedMonitoredElements.get(meSort);
			switch (meSort.intValue()) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							EQUIPMENT_ME_LINK,
							MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
					break;
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							TRANSMISSIONPATH_ME_LINK,
							MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new RetrieveObjectException(mesg);
			}
		}
	}

	private void retrieveMDMIdsByOneQuery(final Set<MonitoredElement> monitoredElements,
			final String linkTable,
			final String linkColumn) throws RetrieveObjectException {
		if (monitoredElements == null || monitoredElements.isEmpty())
			return;

		final Map<Identifier, Set<Identifier>> mdmIdsMap = this.retrieveLinkedEntityIds(monitoredElements,
				linkTable,
				MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID,
				linkColumn);

		for (final MonitoredElement monitoredElement : monitoredElements) {
			final Identifier meId = monitoredElement.getId();
			final Set<Identifier> mdmIds = mdmIdsMap.get(meId);

			monitoredElement.setMonitoredDomainMemberIds0(mdmIds);
		}
	}

	@Override
	protected void insert(final Set<MonitoredElement> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		for (final Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			final MonitoredElement monitoredElement = (MonitoredElement) iter.next();
			this.insertMonitoredDomainMemberIds(monitoredElement);
		}
	}

	private void insertMonitoredDomainMemberIds(final MonitoredElement monitoredElement) throws CreateObjectException {
		final Set<Identifier> mdmIds = monitoredElement.getMonitoredDomainMemberIds();
		final Identifier meId = monitoredElement.getId();
		final int meSort = monitoredElement.getSort().value();
		
		final StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
		switch (meSort) {
			case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
				buffer.append(EQUIPMENT_ME_LINK);
				break;
			case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
				buffer.append(TRANSMISSIONPATH_ME_LINK);
				break;
			default:
				final String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
				throw new CreateObjectException(mesg);
		}
		buffer.append(OPEN_BRACKET);
		switch (meSort) {
			case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
				buffer.append(MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
				break;
			case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
				buffer.append(MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
				break;
			default:
				final String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
				throw new CreateObjectException(mesg);
		}
		buffer.append(COMMA);
		buffer.append(MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID);
		buffer.append(CLOSE_BRACKET);
		buffer.append(SQL_VALUES);
		buffer.append(OPEN_BRACKET);
		buffer.append(QUESTION);
		buffer.append(COMMA);
		buffer.append(QUESTION);
		buffer.append(CLOSE_BRACKET);

		PreparedStatement preparedStatement = null;
		Identifier mdmId = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(buffer.toString());
			for (final Iterator<Identifier> it = mdmIds.iterator(); it.hasNext();) {
				mdmId = it.next();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, mdmId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, meId);
				Log.debugMessage("Inserting link for monitored element '"
						+ meId.getIdentifierString()
						+ "' and monitored domain member '" + mdmId.getIdentifierString() + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		} catch (SQLException sqle) {
			final String mesg = "Cannot insert link for monitored element '"
					+ meId.getIdentifierString()
					+ "' and monitored domain member '" + mdmId.getIdentifierString() + "'";
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	protected void update(final Set<MonitoredElement> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateMonitoredDomainMemberIds(storableObjects);
	}

	private void updateMonitoredDomainMemberIds(final Set monitoredElements) throws UpdateObjectException {
		if (monitoredElements == null || monitoredElements.isEmpty())
			return;

		final Map<Integer, Set<MonitoredElement>> sortedMonitoredElements = new HashMap<Integer, Set<MonitoredElement>>();

		for (final Iterator it = monitoredElements.iterator(); it.hasNext();) {
			final MonitoredElement monitoredElement = (MonitoredElement) it.next();
			final Integer meSort = new Integer(monitoredElement.getSort().value());
			Set<MonitoredElement> monitoredElementsOneSort = sortedMonitoredElements.get(meSort);
			if (monitoredElementsOneSort == null) {
				monitoredElementsOneSort = new HashSet<MonitoredElement>();
				sortedMonitoredElements.put(meSort, monitoredElementsOneSort);
			}
			monitoredElementsOneSort.add(monitoredElement);
		}

		for (final Integer meSort : sortedMonitoredElements.keySet()) {
			final Set<MonitoredElement> monitoredElementsOneSort = sortedMonitoredElements.get(meSort);
			switch (meSort.intValue()) {
				case MonitoredElementSort._MONITOREDELEMENT_SORT_EQUIPMENT:
					this.updateMDMIds(monitoredElementsOneSort,
							EQUIPMENT_ME_LINK,
							MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID);
					break;
				case MonitoredElementSort._MONITOREDELEMENT_SORT_TRANSMISSION_PATH:
					this.updateMDMIds(monitoredElementsOneSort,
							TRANSMISSIONPATH_ME_LINK,
							MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID);
					break;
				default:
					String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
					throw new UpdateObjectException(mesg);
			}
		}
	}

	private void updateMDMIds(final Set<MonitoredElement> monitoredElements, final String linkTable, final String linkColumn)
			throws UpdateObjectException {
		if (monitoredElements == null || monitoredElements.isEmpty())
			return;

		final Map<Identifier, Set<Identifier>> mdmIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final MonitoredElement monitoredElement : monitoredElements) {
			final Set<Identifier> mdmIds = monitoredElement.getMonitoredDomainMemberIds();
			mdmIdsMap.put(monitoredElement.getId(), mdmIds);
		}

		super.updateLinkedEntityIds(mdmIdsMap, linkTable, MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID, linkColumn);
	}

	@Override
	protected Set<MonitoredElement> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<MonitoredElement> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveMonitoredDomainMemberIdsByOneQuery(objects);
		return objects;
	}

}
