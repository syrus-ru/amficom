/*
* $Id: MapViewDatabase.java,v 1.5 2005/02/04 09:48:56 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.mapview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.5 $, $Date: 2005/02/04 09:48:56 $
 * @author $Author: bob $
 * @module mapview_v1
 */
public class MapViewDatabase extends StorableObjectDatabase {
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
	
	private static String updateMultiplySQLValues;	


	private MapView fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MapView)
			return (MapView) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MapView mapView = this.fromStorableObject(storableObject);
		this.retrieveEntity(mapView);
		List maps = Collections.singletonList(mapView);
		
		java.util.Map schemeIdsMap = super.retrieveLinkedEntityIds(maps, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
		for (Iterator it = schemeIdsMap.keySet().iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			List schemeIds = (List)schemeIdsMap.get(id);
			if (id.equals(mapView.getId())){
				try{
				mapView.setSchemes0(SchemeStorableObjectPool.getStorableObjects(schemeIds, true));
				}catch(DatabaseException de){
					throw new RetrieveObjectException(this.getEnityName() + "Database.retrieve | cannot retrieve schemes" ,  de);
				} catch (CommunicationException ce) {
					throw new RetrieveObjectException(this.getEnityName() + "Database.retrieve | cannot retrieve schemes" ,  ce);
				}
			}
		}		
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.MAPVIEW_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_DOMAIN_ID + COMMA
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
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		MapView mapView = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mapView.getDomainId());
			DatabaseString.setString(preparedStatement, ++i, mapView.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, mapView.getDescription(), SIZE_DESCRIPTION_COLUMN);
			preparedStatement.setDouble(++i, mapView.getLongitude());
			preparedStatement.setDouble(++i, mapView.getLatitude());
			preparedStatement.setDouble(++i, mapView.getScale());
			preparedStatement.setDouble(++i, mapView.getDefaultScale());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mapView.getMap().getId());					
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		MapView mapView = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(mapView.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mapView.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mapView.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ mapView.getLongitude() + COMMA
			+ mapView.getLatitude() + COMMA
			+ mapView.getScale() + COMMA
			+ mapView.getDefaultScale() + COMMA
			+ DatabaseIdentifier.toSQLString(mapView.getMap().getId());
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		MapView map = (storableObject == null) ? 
				new MapView(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, null, null, null, 0.0, 0.0, 0.0, 0.0, null) : 
					fromStorableObject(storableObject);				
		
		try{
		map.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				resultSet.getDouble(COLUMN_LONGITUDE),
				resultSet.getDouble(COLUMN_LATITUDE),
				resultSet.getDouble(COLUMN_SCALE),
				resultSet.getDouble(COLUMN_DEFAULTSCALE),
				(com.syrus.AMFICOM.map.Map)MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MAP_ID), true));
		}catch(DatabaseException de){
			throw new RetrieveObjectException(this.getEnityName() + "Database.updateEntityFromResultSet | cannot retrieve map" ,  de);
		} catch (CommunicationException ce) {
			throw new RetrieveObjectException(this.getEnityName() + "Database.updateEntityFromResultSet | cannot retrieve map" ,  ce);
		}
		return map;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		MapView mapView = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		MapView mapView = this.fromStorableObject(storableObject);
		super.insertEntity(mapView);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		List maps = Collections.singletonList(mapView);
		try {
			characteristicDatabase.updateCharacteristics(mapView);
			this.updateSchemeIds(maps);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
			this.updateSchemeIds(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MapView map = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(map, false);
				characteristicDatabase.updateCharacteristics(map);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(map, true);
				characteristicDatabase.updateCharacteristics(map);
				return;
		}
		List maps = Collections.singletonList(map);
		this.updateSchemeIds(maps);
	}
	
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				characteristicDatabase.updateCharacteristics(storableObjects);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				characteristicDatabase.updateCharacteristics(storableObjects);
				return;
		}
		this.updateSchemeIds(storableObjects);
	}	
	
	private void updateSchemeIds(List mapViews) throws UpdateObjectException, IllegalDataException {
		if (mapViews == null || mapViews.isEmpty())
			return;

		Map mapIdLinkedObjectIds = new HashMap();
		
		for (Iterator colIter = mapViews.iterator(); colIter.hasNext();) {
			StorableObject storableObject = (StorableObject) colIter.next();
	        MapView mapView = fromStorableObject(storableObject);
	        List linkedObjectList = mapView.getSchemes();
	
	        List linkedObjectIds = new ArrayList(linkedObjectList.size());
	        for (Iterator it = linkedObjectList.iterator(); it.hasNext();) 
	            linkedObjectIds.add(((Scheme) it.next()).id());
	        
	        mapIdLinkedObjectIds.put(mapView.getId(), linkedObjectIds);
		}
		
		super.updateLinkedEntities(mapIdLinkedObjectIds, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);

	}
	
	public void delete(Identifier id) throws IllegalDataException {
		this.delete(Collections.singletonList(id));
	}
	
	public void delete(List ids) throws IllegalDataException {
		super.delete(ids);
		
		java.util.Map linkedObjectIds = new HashMap();
		
		java.util.Map mapIds = new HashMap();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			try{
				MapView map = (MapView)MapViewStorableObjectPool.getStorableObject(mapId, true);
				mapIds.put(mapId, map);
			}catch(ApplicationException ae){
				throw new IllegalDataException(this.getEnityName()+"Database.delete | Couldn't found map for " + mapId);
			} 
		}
		
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			MapView mapView = (MapView) mapIds.get(mapId);
			linkedObjectIds.put(mapView, mapView.getSchemes());
		}
		
		this.deleteSchemeIds(linkedObjectIds);
		
	}	

	public void delete(StorableObject storableObject) throws IllegalDataException {
		MapView map = fromStorableObject(storableObject);
		this.delete(Collections.singletonList(map.getId()));
	}
	
	private void deleteSchemeIds(java.util.Map linkedObjectIds) {
		StringBuffer linkBuffer = new StringBuffer(LINK_COLUMN_SCHEME_ID);
		
		linkBuffer.append(SQL_IN);
		linkBuffer.append(OPEN_BRACKET);
		
		int i = 0;
		for (Iterator mvIter = linkedObjectIds.keySet().iterator(); mvIter.hasNext();) {
			Identifier mapViewId = (Identifier) mvIter.next();
			List schemes = (List)linkedObjectIds.get(mapViewId);
			for (Iterator it = schemes.iterator(); it.hasNext(); i++) {
				com.syrus.AMFICOM.general.corba.Identifier id = ((Scheme) it.next()).id();
	
				linkBuffer.append(DatabaseIdentifier.toSQLString(id));
				if (it.hasNext()) {
					if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0)){
						linkBuffer.append(COMMA);
					}
					else {
						linkBuffer.append(CLOSE_BRACKET);
						linkBuffer.append(SQL_AND);
						linkBuffer.append(LINK_COLUMN_SCHEME_ID);				
						linkBuffer.append(SQL_IN);
						linkBuffer.append(OPEN_BRACKET);
					}
				}
			
			}
		}
		
		linkBuffer.append(CLOSE_BRACKET);
		
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + MAPVIEW_SCHEME + SQL_WHERE
					+ linkBuffer.toString());
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

	public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		List maps;
		if ((ids == null) || (ids.isEmpty()))
			maps = retrieveByIdsOneQuery(null, conditions);
		else maps = retrieveByIdsOneQuery(ids, conditions);	
		
		java.util.Map mapIds = new HashMap();
		for (Iterator it = maps.iterator(); it.hasNext();) {
			MapView map = (MapView) it.next();
			mapIds.put(map.getId(), map);
		}
		
		java.util.Map schemeIdsMap = super.retrieveLinkedEntityIds(maps, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
		for (Iterator it = schemeIdsMap.keySet().iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			MapView map = (MapView) mapIds.get(id);
			List schemeIds = (List)schemeIdsMap.get(id);				
			if (id.equals(map.getId())){
				try {
					map.setSchemes0(SchemeStorableObjectPool.getStorableObjects(schemeIds, true));
				} catch (DatabaseException de) {
					throw new RetrieveObjectException(this.getEnityName() + "Database.updateEntityFromResultSet | cannot retrieve schemes" ,  de);
				} catch (CommunicationException ce) {
					throw new RetrieveObjectException(this.getEnityName() + "Database.updateEntityFromResultSet | cannot retrieve schemes" ,  ce);
				}
			}
		}
		return maps;
		//return retriveByIdsPreparedStatement(ids, conditions);
	}	
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		{
			Log.errorMessage(getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}

}
