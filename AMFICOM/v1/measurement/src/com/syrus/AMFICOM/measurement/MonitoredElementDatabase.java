/*
 * $Id: MonitoredElementDatabase.java,v 1.11.2.2 2006/03/06 19:00:09 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.administration.DomainMember.COLUMN_DOMAIN_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.general.TableNames.EQUIPMENT_ME_LINK;
import static com.syrus.AMFICOM.general.TableNames.TRANSMISSIONPATH_ME_LINK;
import static com.syrus.AMFICOM.measurement.MonitoredElementWrapper.COLUMN_KIND;
import static com.syrus.AMFICOM.measurement.MonitoredElementWrapper.COLUMN_LOCAL_ADDRESS;
import static com.syrus.AMFICOM.measurement.MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID;
import static com.syrus.AMFICOM.measurement.MonitoredElementWrapper.LINK_COLUMN_EQUIPMENT_ID;
import static com.syrus.AMFICOM.measurement.MonitoredElementWrapper.LINK_COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.measurement.MonitoredElementWrapper.LINK_COLUMN_TRANSMISSION_PATH_ID;
import static com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.IdlMonitoredElementKind._MONITOREDELEMENT_KIND_EQUIPMENT;
import static com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.IdlMonitoredElementKind._MONITOREDELEMENT_KIND_TRANSMISSION_PATH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.11.2.2 $, $Date: 2006/03/06 19:00:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MonitoredElementDatabase extends StorableObjectDatabase<MonitoredElement> {
	private static final int SIZE_LOCAL_ADDRESS_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return MONITOREDELEMENT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_DOMAIN_ID + COMMA
					+ COLUMN_NAME + COMMA + COLUMN_MEASUREMENT_PORT_ID
					+ COMMA + COLUMN_KIND + COMMA
					+ COLUMN_LOCAL_ADDRESS;
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
				+ storableObject.getKind().value() + COMMA
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
		preparedStatement.setInt(++startParameterNumber, storableObject.getKind().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLocalAddress(),
			SIZE_LOCAL_ADDRESS_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected MonitoredElement updateEntityFromResultSet(final MonitoredElement storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		MonitoredElement monitoredElement = (storableObject == null)
				? monitoredElement = new MonitoredElement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						0,
						null,
						null)
				: storableObject;
		monitoredElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_PORT_ID),
				resultSet.getInt(COLUMN_KIND),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_LOCAL_ADDRESS)));
		return monitoredElement;
	}

	private void retrieveMonitoredDomainMemberIdsByOneQuery(final Set<MonitoredElement> monitoredElements)
			throws RetrieveObjectException {
		final Map<Integer, Set<MonitoredElement>> sortedMonitoredElements = new HashMap<Integer, Set<MonitoredElement>>();

		for (final MonitoredElement monitoredElement : monitoredElements) {
			final Integer meSort = new Integer(monitoredElement.getKind().value());
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
				case _MONITOREDELEMENT_KIND_EQUIPMENT:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							EQUIPMENT_ME_LINK,
							LINK_COLUMN_EQUIPMENT_ID);
					break;
				case _MONITOREDELEMENT_KIND_TRANSMISSION_PATH:
					this.retrieveMDMIdsByOneQuery(monitoredElementsOneSort,
							TRANSMISSIONPATH_ME_LINK,
							LINK_COLUMN_TRANSMISSION_PATH_ID);
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
				LINK_COLUMN_MONITORED_ELEMENT_ID,
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
		final int meSort = monitoredElement.getKind().value();
		
		final StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
		switch (meSort) {
			case _MONITOREDELEMENT_KIND_EQUIPMENT:
				buffer.append(EQUIPMENT_ME_LINK);
				break;
			case _MONITOREDELEMENT_KIND_TRANSMISSION_PATH:
				buffer.append(TRANSMISSIONPATH_ME_LINK);
				break;
			default:
				final String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
				throw new CreateObjectException(mesg);
		}
		buffer.append(OPEN_BRACKET);
		switch (meSort) {
			case _MONITOREDELEMENT_KIND_EQUIPMENT:
				buffer.append(LINK_COLUMN_EQUIPMENT_ID);
				break;
			case _MONITOREDELEMENT_KIND_TRANSMISSION_PATH:
				buffer.append(LINK_COLUMN_TRANSMISSION_PATH_ID);
				break;
			default:
				final String mesg = "ERROR: Unknown sort of monitoredelement: " + meSort;
				throw new CreateObjectException(mesg);
		}
		buffer.append(COMMA);
		buffer.append(LINK_COLUMN_MONITORED_ELEMENT_ID);
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
			final Integer meSort = new Integer(monitoredElement.getKind().value());
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
				case _MONITOREDELEMENT_KIND_EQUIPMENT:
					this.updateMDMIds(monitoredElementsOneSort,
							EQUIPMENT_ME_LINK,
							LINK_COLUMN_EQUIPMENT_ID);
					break;
				case _MONITOREDELEMENT_KIND_TRANSMISSION_PATH:
					this.updateMDMIds(monitoredElementsOneSort,
							TRANSMISSIONPATH_ME_LINK,
							LINK_COLUMN_TRANSMISSION_PATH_ID);
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

		super.updateLinkedEntityIds(mdmIdsMap, linkTable, LINK_COLUMN_MONITORED_ELEMENT_ID, linkColumn);
	}

	@Override
	protected Set<MonitoredElement> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<MonitoredElement> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveMonitoredDomainMemberIdsByOneQuery(objects);
		return objects;
	}

}
