/*-
 * $Id: MapDatabase.java,v 1.63 2005/11/30 15:15:32 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_CHILD_MAP_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_COLLECTOR_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_EXTERNAL_NODE_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_MAP_LIBRARY_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_MARK_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_NODE_LINK_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_PHYSICAL_LINK_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_SITE_NODE_ID;
import static com.syrus.AMFICOM.map.MapWrapper.LINK_COLUMN_TOPOLOGICAL_NODE_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.63 $, $Date: 2005/11/30 15:15:32 $
 * @author $Author: bass $
 * @module map
 */
public final class MapDatabase extends StorableObjectDatabase<Map> {
	 // linked tables ::
    //TODO: move to mapWrapper
	static final String MAP_COLLECTOR 			= "MapCollector";
    static final String MAP_MARK 				= "MapMark";
	static final String MAP_NODE_LINK 			= "MapNodeLink";
	static final String MAP_PHYSICAL_LINK 		= "MapPhysicalLink";
	static final String MAP_SITE_NODE 			= "MapSiteNode";
	static final String MAP_TOPOLOGICAL_NODE 	= "MapTopologicalNode";
	static final String MAP_MAP 				= "MapMapLink";
	static final String MAP_EXTERNAL_NODE 		= "MapExtNodeLink";
	static final String MAP_MAP_LIBRARY 		= "MapMapLibraryLink";
	
//    private static final int _MAP_COLLECTOR 			= 0;
//    private static final int _MAP_MARK 					= 1;
//	private static final int _MAP_NODE_LINK 			= 2;
//	private static final int _MAP_PHYSICAL_LINK 		= 3;
//	private static final int _MAP_SITE_NODE 			= 4;
//	private static final int _MAP_TOPOLOGICAL_NODE 		= 5;
//	private static final int _MAP_MAP 					= 6;
//	private static final int _MAP_EXTERNAL_NODE 		= 7;
//	private static final int _MAP_MAP_LIBRARY 			= 8;
	
	private enum LinkedEntities {
		MAP_COLLECTORS(MAP_COLLECTOR, LINK_COLUMN_COLLECTOR_ID),
		MAP_MARKS(MAP_MARK, LINK_COLUMN_MARK_ID),
		MAP_NODE_LINKS(MAP_NODE_LINK, LINK_COLUMN_NODE_LINK_ID),
		MAP_PHYSICAL_LINKS(MAP_PHYSICAL_LINK, LINK_COLUMN_PHYSICAL_LINK_ID),
		MAP_SITE_NODES(MAP_SITE_NODE, LINK_COLUMN_SITE_NODE_ID),
		MAP_TOPOLOGICAL_NODES(MAP_TOPOLOGICAL_NODE, LINK_COLUMN_TOPOLOGICAL_NODE_ID),
		MAP_MAPS(MAP_MAP, LINK_COLUMN_CHILD_MAP_ID),
		MAP_EXTERNAL_NODES(MAP_EXTERNAL_NODE, LINK_COLUMN_EXTERNAL_NODE_ID),
		MAP_MAP_LIBRARYS(MAP_MAP_LIBRARY, LINK_COLUMN_MAP_LIBRARY_ID);
		
		private final String tableName;
		private final String linkColumnName;
				
		
		private LinkedEntities(String tableName, String linkColumnName) {
			this.tableName = tableName;
			this.linkColumnName = linkColumnName;
		}
		
		public String getTableName() {
			return this.tableName;
		}
		
		public String getLinkColumnName() {
			return this.linkColumnName;
		}
		
		public static Set<Identifier> getLinkedIds(Map map, LinkedEntities linkedEntities) throws IllegalDataException {
			Set<Identifier> linkedIdsList;
			switch (linkedEntities) {
			case MAP_COLLECTORS:
				linkedIdsList = map.getCollectorIds();
				break;
			case MAP_MARKS:
				linkedIdsList = map.getMarkIds();
				break;
			case MAP_NODE_LINKS:
				linkedIdsList = map.getNodeLinkIds();
				break;
			case MAP_PHYSICAL_LINKS:
				linkedIdsList = map.getPhysicalLinkIds();
				break;
			case MAP_SITE_NODES:
				linkedIdsList = map.getSiteNodeIds();
				break;
			case MAP_TOPOLOGICAL_NODES:
				linkedIdsList = map.getTopologicalNodeIds();
				break;
			case MAP_MAPS:
				linkedIdsList = map.getMapIds();
				break;
			case MAP_EXTERNAL_NODES:
				linkedIdsList = map.getExternalNodeIds();
				break;
			case MAP_MAP_LIBRARYS:
				linkedIdsList = map.getMapLibraryIds();
				break;
			default:
				throw new IllegalDataException("MapDatabase.updateLinkedObjectIds | unknown linked entity: " + linkedEntities.getTableName());
			}
			return linkedIdsList;
		}
		
		public static void setLinkedEntities(Map map, Set<Identifier> linkedIds, LinkedEntities linkedEntities) throws IllegalDataException {
			switch (linkedEntities) {
			case MAP_COLLECTORS:
				map.setCollectorIds(linkedIds);
				break;
			case MAP_MARKS:
				map.setMarkIds(linkedIds);
				break;
			case MAP_NODE_LINKS:
				map.setNodeLinkIds(linkedIds);
				break;
			case MAP_PHYSICAL_LINKS:
				map.setPhysicalLinkIds(linkedIds);
				break;
			case MAP_SITE_NODES:
				map.setSiteNodeIds(linkedIds);
				break;
			case MAP_TOPOLOGICAL_NODES:
				map.setTopologicalNodeIds(linkedIds);
				break;
			case MAP_MAPS:
				map.setMapIds(linkedIds);
				break;
			case MAP_EXTERNAL_NODES:
				map.setExternalNodeIds(linkedIds);
				break;
			case MAP_MAP_LIBRARYS:
				map.setMapLibraryIds(linkedIds);
				break;
			default:
				throw new IllegalDataException("MapDatabase.updateLinkedObjectIds " +
						"| unknown type: "
						+ linkedEntities.getTableName());
			}
		}
	}

    //private static java.util.Map<String, String> dbTableColumnName;
	
	private static String columns;
	
	private static String updateMultipleSQLValues;
	
	private java.util.Map<Identifier, Set<Identifier>> retrieveLinkedObjects(final Set<Map> maps, LinkedEntities linkedEntities)
			throws RetrieveObjectException {
		if (maps == null || maps.isEmpty())
			return Collections.emptyMap();

		final String tableName = linkedEntities.getTableName();
		final String columnName = linkedEntities.getLinkColumnName();

		return super.retrieveLinkedEntityIds(maps, tableName, MapWrapper.LINK_COLUMN_MAP_ID, columnName);
	}

	@Override
	protected short getEntityCode() {		
		return MAP_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MapWrapper.COLUMN_DOMAIN_ID;
		}
		return columns;
	}	

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(Map storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(Map storableObject) throws IllegalDataException {
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getDomainId());
		return values;
	}

	@Override
	protected Map updateEntityFromResultSet(Map storableObject, ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final Map map = (storableObject == null)
				? new Map(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null)
					: storableObject;

		map.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, MapWrapper.COLUMN_DOMAIN_ID));
		return map;
	}

	@Override
	protected void insert(final Set<Map> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		try {
			for (int i = 0; i < LinkedEntities.values().length; i++) {
				this.updateLinkedObjectIds(storableObjects, LinkedEntities.values()[i]);
			}
		} catch (final UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	protected void update(final Set<Map> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		for (int i = 0; i < LinkedEntities.values().length; i++) {
			this.updateLinkedObjectIds(storableObjects, LinkedEntities.values()[i]);
		}		
	}	

	private void updateLinkedObjectIds(final Set<Map> maps, final LinkedEntities linkedEntities)
			throws UpdateObjectException {
		if (maps == null || maps.isEmpty()) {
			return;
		}

		final String tableName = linkedEntities.getTableName();
		final String columnName = linkedEntities.getLinkColumnName();

		final java.util.Map<Identifier, Set<Identifier>> mapIdLinkedObjectIds = new HashMap<Identifier, Set<Identifier>>();

		Set<Identifier> linkedObjectIds;
		for (final Map storableObject : maps) {
			try {
				linkedObjectIds = LinkedEntities.getLinkedIds(storableObject, linkedEntities);
			} catch (IllegalDataException e) {
				throw new UpdateObjectException(e);
			}
			mapIdLinkedObjectIds.put(storableObject.getId(), linkedObjectIds);
		}

		super.updateLinkedEntityIds(mapIdLinkedObjectIds, tableName, MapWrapper.LINK_COLUMN_MAP_ID, columnName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void delete(final Set<? extends Identifiable> ids) {

		Set<Map> dbMaps = null;
		try {
			dbMaps = this.retrieveByCondition(idsEnumerationString(ids, StorableObjectWrapper.COLUMN_ID, true).toString());
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return;
		}

		Set<Identifier> linkedIds = new HashSet<Identifier>();
		for (int i = 0; i < LinkedEntities.values().length; i++) {
			final LinkedEntities linkedEntities = LinkedEntities.values()[i];
			for (final Map map : dbMaps) {
				if (linkedEntities == LinkedEntities.MAP_MAPS 
						|| linkedEntities == LinkedEntities.MAP_EXTERNAL_NODES
						|| linkedEntities == LinkedEntities.MAP_MAP_LIBRARYS) {
					continue;
				}
				try {
					linkedIds.addAll(LinkedEntities.getLinkedIds(map, linkedEntities));
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
			}
			if(!linkedIds.isEmpty()) {
				final StorableObjectDatabase<? extends StorableObject<?>> database = DatabaseContext.getDatabase(linkedIds.iterator().next().getMajor());
				// Nothing to do but suppress a warning
				database.delete(linkedIds);
				linkedIds.clear();
			}
			
		}

		super.delete(ids);
	}	

	@Override
	protected Set<Map> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Map> maps = super.retrieveByCondition(conditionQuery);

		final java.util.Map<Identifier, Map> mapIds = new HashMap<Identifier, Map>();
		for (final Map map : maps) {
			mapIds.put(map.getId(), map);
		}

		for (int i = 0; i < LinkedEntities.values().length; i++) {
			LinkedEntities linkedEntities = LinkedEntities.values()[i];
			final java.util.Map<Identifier, Set<Identifier>> linkedMap = this.retrieveLinkedObjects(maps, linkedEntities);
			for (final Identifier id : linkedMap.keySet()) {
				final Map map = mapIds.get(id);
				final Set<Identifier> linkedIds = linkedMap.get(id);				
				if (id.equals(map.getId())) {
					LinkedEntities.setLinkedEntities(map, linkedIds, linkedEntities);
				}
			}
		}

		return maps;
		//return retriveByIdsPreparedStatement(ids, conditions);
	}	
	
}
