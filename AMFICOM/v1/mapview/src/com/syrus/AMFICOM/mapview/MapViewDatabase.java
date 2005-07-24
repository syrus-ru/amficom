/*-
 * $Id: MapViewDatabase.java,v 1.31 2005/07/24 17:39:22 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.31 $, $Date: 2005/07/24 17:39:22 $
 * @author $Author: arseniy $
 * @module mapview_v1
 */
public final class MapViewDatabase extends StorableObjectDatabase {
    // domain_id VARCHAR2(32),
    public static final String COLUMN_DOMAIN_ID     = "domain_id";
    // name VARCHAR2(128),
    public static final String COLUMN_NAME  = "name";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // longitude FLOAT,
    public static final String COLUMN_LONGITUDE     = "longitude";
    // latitude FLOAT,
    public static final String COLUMN_LATITUDE      = "latitude";
    // scale FLOAT,
    public static final String COLUMN_SCALE = "scale";
    // defaultScale FLOAT,
    public static final String COLUMN_DEFAULTSCALE  = "defaultScale";
    // map_id VARCHAR2(32),
    public static final String COLUMN_MAP_ID        = "map_id";

    // linked table ::
    private static final String MAPVIEW_SCHEME        = "MapViewScheme";
    // mapview_id VARCHAR2(32),
    private static final String LINK_COLUMN_MAPVIEW_ID    = "mapview_id";
    // scheme_id VARCHAR2(32),
    private static final String LINK_COLUMN_SCHEME_ID     = "scheme_id";


	private static String columns;
	
	private static String updateMultipleSQLValues;	


	private MapView fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MapView)
			return (MapView) storableObject;
		throw new IllegalDataException(this.getEntityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	@Override
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		final MapView mapView = this.fromStorableObject(storableObject);
		this.retrieveEntity(mapView);
		final Set<MapView> maps = Collections.singleton(mapView);

		final java.util.Map<Identifier, Set<Identifier>> schemeIdsMap = super.retrieveLinkedEntityIds(maps,
				MAPVIEW_SCHEME,
				LINK_COLUMN_MAPVIEW_ID,
				LINK_COLUMN_SCHEME_ID);
		for (final Identifier id : schemeIdsMap.keySet()) {
			final Set<Identifier> schemeIds = schemeIdsMap.get(id);
			if (id.equals(mapView.getId())) {
				try {
					final Set<Scheme> schemes = StorableObjectPool.getStorableObjects(schemeIds, true);
					mapView.setSchemes0(schemes);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(this.getEntityName() + "Database.retrieve | cannot retrieve schemes", ae);
				}
			}
		}
	}	
	
	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MAPVIEW_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_LONGITUDE + COMMA
				+ COLUMN_LATITUDE + COMMA
				+ COLUMN_SCALE + COMMA
				+ COLUMN_DEFAULTSCALE + COMMA
				+ COLUMN_MAP_ID;
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
		protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		MapView mapView = fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mapView.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mapView.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, mapView.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, mapView.getLongitude());
		preparedStatement.setDouble(++startParameterNumber, mapView.getLatitude());
		preparedStatement.setDouble(++startParameterNumber, mapView.getScale());
		preparedStatement.setDouble(++startParameterNumber, mapView.getDefaultScale());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mapView.getMap().getId());					
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		MapView mapView = fromStorableObject(storableObject);
		String values = DatabaseIdentifier.toSQLString(mapView.getDomainId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(mapView.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(mapView.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ mapView.getLongitude() + COMMA
			+ mapView.getLatitude() + COMMA
			+ mapView.getScale() + COMMA
			+ mapView.getDefaultScale() + COMMA
			+ DatabaseIdentifier.toSQLString(mapView.getMap().getId());
		return values;
	}
	
	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		MapView map = (storableObject == null) ?
				new MapView(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, 0.0, 0.0, 0.0, 0.0, null) :
					fromStorableObject(storableObject);				
		
		try {
			map.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
					DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
					resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
					DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID),
					DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
					DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
					resultSet.getDouble(COLUMN_LONGITUDE),
					resultSet.getDouble(COLUMN_LATITUDE),
					resultSet.getDouble(COLUMN_SCALE),
					resultSet.getDouble(COLUMN_DEFAULTSCALE),
					(com.syrus.AMFICOM.map.Map) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							COLUMN_MAP_ID), true));
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(this.getEntityName() + "Database.updateEntityFromResultSet | cannot retrieve map", ae);
		}
		return map;
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		try {
			this.updateSchemeIds(storableObjects);
		} catch (final UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateSchemeIds(storableObjects);
	}	

	private void updateSchemeIds(final Set<MapView> mapViews) throws UpdateObjectException {
		if (mapViews == null || mapViews.isEmpty())
			return;

		final Map<Identifier, Set<Identifier>> mapIdLinkedObjectIds = new HashMap<Identifier, Set<Identifier>>();

		for (final MapView mapView : mapViews) {
			final Set<Scheme> linkedObjectList = mapView.getSchemes();
			final Set<Identifier> linkedObjectIds = Identifier.createIdentifiers(linkedObjectList);
			mapIdLinkedObjectIds.put(mapView.getId(), linkedObjectIds);
		}
		//TODO: may be wrong correction
		//super.updateLinkedEntities(mapIdLinkedObjectIds, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
		super.updateLinkedEntityIds(mapIdLinkedObjectIds, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
	}
	
	@Override
	public void delete(final Identifier id) {
		this.delete(Collections.singleton(id));
	}
	
	@Override
	public void delete(final Set<? extends Identifiable> ids) {
		super.delete(ids);
		this.deleteSchemeIds(ids);
	}

	private void deleteSchemeIds(final Set<? extends Identifiable> ids) {
		final Set<Identifier> schemeIds = new HashSet<Identifier>();
		for (final Identifiable identifiable : ids) {
			final Identifier mapId = identifiable.getId();
			try {
				final MapView mapView = (MapView) StorableObjectPool.getStorableObject(mapId, true);
				schemeIds.addAll(Identifier.createIdentifiers(mapView.getSchemes()));
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		final StringBuffer stringBuffer = idsEnumerationString(schemeIds, LINK_COLUMN_SCHEME_ID, true);

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + MAPVIEW_SCHEME + SQL_WHERE + stringBuffer.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
			
	@Override
	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<MapView> maps = super.retrieveByCondition(conditionQuery);
		
		java.util.Map<Identifier, MapView> mapIds = new HashMap<Identifier, MapView>();
		for (final MapView map : maps) {
			mapIds.put(map.getId(), map);
		}
		
		final java.util.Map<Identifier, Set<Identifier>> schemeIdsMap = super.retrieveLinkedEntityIds(maps, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
		for (final Identifier id : schemeIdsMap.keySet()) {
			final MapView map = mapIds.get(id);
			final Set<Identifier> schemeIds = schemeIdsMap.get(id);				
			if (id.equals(map.getId())) {
				try {
					final Set<Scheme> schemes = StorableObjectPool.getStorableObjects(schemeIds, true);
					map.setSchemes0(schemes);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(this.getEntityName() + "Database.updateEntityFromResultSet | cannot retrieve schemes" ,  ae);
				}
			}
		}
		return maps;
		//return retriveByIdsPreparedStatement(ids, conditions);
	}


}
