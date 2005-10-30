/*
 * $Id: PhysicalLinkDatabase.java,v 1.42 2005/10/30 14:49:01 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.42 $, $Date: 2005/10/30 14:49:01 $
 * @author $Author: bass $
 * @module map
 */
public final class PhysicalLinkDatabase extends StorableObjectDatabase<PhysicalLink> {
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {		
		return PHYSICALLINK_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ PhysicalLinkWrapper.COLUMN_PHYSICAL_LINK_TYPE_ID + COMMA
				+ PhysicalLinkWrapper.COLUMN_CITY + COMMA
				+ PhysicalLinkWrapper.COLUMN_STREET + COMMA
				+ PhysicalLinkWrapper.COLUMN_BUILDING + COMMA
				+ PhysicalLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ PhysicalLinkWrapper.COLUMN_END_NODE_ID;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final PhysicalLink storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName0(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCity(), MarkDatabase.SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getStreet(), MarkDatabase.SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getStartNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEndNodeId());
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final PhysicalLink storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName0(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getCity(), MarkDatabase.SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getStreet(), MarkDatabase.SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(storableObject.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getStartNodeId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getEndNodeId());
		return values;
	}

	@Override
	protected PhysicalLink updateEntityFromResultSet(final PhysicalLink storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final PhysicalLink physicalLink = (storableObject == null) ? new PhysicalLink(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID),
				null,
				StorableObjectVersion.ILLEGAL_VERSION,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null) : storableObject;

		PhysicalLinkType type;
		try {
			type = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					PhysicalLinkWrapper.COLUMN_PHYSICAL_LINK_TYPE_ID), true);
		} catch (ApplicationException ae) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			final String msg = this.getEntityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		
		physicalLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				type,
				DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_CITY)),
				DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_STREET)),
				DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_BUILDING)),
				DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkWrapper.COLUMN_START_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkWrapper.COLUMN_END_NODE_ID));
		return physicalLink;
	}
	
	@Override
	protected void update(Set<PhysicalLink> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateLinkedEntities(storableObjects);	
	}
	
	private void updateLinkedEntities(Set<PhysicalLink> physicalLinks) throws UpdateObjectException {
		Map<Identifier, Set<Identifier>> map = new HashMap<Identifier, Set<Identifier>>();
		
		for (PhysicalLink link : physicalLinks) {
			map.put(link.getId(), Identifier.createIdentifiers(link.getBinding().getPipeBlocks()));
		}
		super.updateLinkedEntityIds(map, 
				PhysicalLinkWrapper.PIPE_BLOCK_TABLE, 
				PhysicalLinkWrapper.LINK_COLUMN_PHYSICALLINK_ID, 
				PhysicalLinkWrapper.LINK_COLUMN_PIPEBLOCK_ID);
	}
	
	@Override
	protected void insert(Set<PhysicalLink> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		try {
			this.update(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}
	
	@Override
	protected Set<PhysicalLink> retrieveByCondition(String conditionQuery) 
	throws RetrieveObjectException, IllegalDataException {
		
		Set<PhysicalLink> physicalLinks = super.retrieveByCondition(conditionQuery);
		
		final Map<Identifier, PhysicalLink> physicalLinkIds = new HashMap<Identifier, PhysicalLink>();
		for (final PhysicalLink physicalLink : physicalLinks) {
			physicalLinkIds.put(physicalLink.getId(), physicalLink);
		}
		
		Map<Identifier, Set<Identifier>> map = super.retrieveLinkedEntityIds(physicalLinks, 
				PhysicalLinkWrapper.PIPE_BLOCK_TABLE, 
				PhysicalLinkWrapper.LINK_COLUMN_PHYSICALLINK_ID, 
				PhysicalLinkWrapper.LINK_COLUMN_PIPEBLOCK_ID);
		
		for (Identifier physicalLinkId : map.keySet()) {
			PhysicalLink physicalLink = physicalLinkIds.get(physicalLinkId);
			StorableObjectDatabase<PipeBlock> database = DatabaseContext.getDatabase(ObjectEntities.PIPEBLOCK_CODE);
			Set<PipeBlock> pipeBlocks = database.retrieveByIdsByCondition(map.get(physicalLinkId), null);
			physicalLink.getBinding().setPipeBlocks(pipeBlocks);
		}
		return physicalLinks;
	}
	
	@Override
	public void delete(Set<? extends Identifiable> identifiables) {
		super.delete(identifiables);
		
		Set<PhysicalLink> dbPhysicalLinks = null;
		try {
			dbPhysicalLinks = this.retrieveByCondition(idsEnumerationString(identifiables, StorableObjectWrapper.COLUMN_ID, true).toString());
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return;
		}
		
		Set<PipeBlock> pipeBlocks = new HashSet<PipeBlock>();
		for (PhysicalLink link : dbPhysicalLinks) {
			pipeBlocks.addAll(link.getBinding().getPipeBlocks());
		}
		
		StorableObjectDatabase<PipeBlock> database = DatabaseContext.getDatabase(ObjectEntities.PIPEBLOCK_CODE);
		database.delete(pipeBlocks);	
	}
}
