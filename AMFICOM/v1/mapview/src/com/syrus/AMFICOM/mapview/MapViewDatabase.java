/*
* $Id: MapViewDatabase.java,v 1.20 2005/04/13 20:14:22 arseniy Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.20 $, $Date: 2005/04/13 20:14:22 $
 * @author $Author: arseniy $
 * @module mapview_v1
 */
public class MapViewDatabase extends CharacterizableDatabase {
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
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MapView mapView = this.fromStorableObject(storableObject);
		this.retrieveEntity(mapView);
		Set maps = Collections.singleton(mapView);
		
		java.util.Map schemeIdsMap = super.retrieveLinkedEntityIds(maps, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
		for (Iterator it = schemeIdsMap.keySet().iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			Set schemeIds = (Set)schemeIdsMap.get(id);
			if (id.equals(mapView.getId())){
				try{
				mapView.setSchemes0(SchemeStorableObjectPool.getStorableObjects(schemeIds, true));
				}catch(ApplicationException ae){
					throw new RetrieveObjectException(this.getEnityName() + "Database.retrieve | cannot retrieve schemes" ,  ae);
				}
			}
		}		
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.MAPVIEW_ENTITY;
	}	
	
	protected String getColumnsTmpl() {
		if (columns == null){
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
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){
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
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		MapView mapView = fromStorableObject(storableObject);
		String values = DatabaseIdentifier.toSQLString(mapView.getDomainId()) + COMMA
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
				new MapView(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, 0.0, 0.0, 0.0, 0.0, null) : 
					fromStorableObject(storableObject);				
		
		try{
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
				(com.syrus.AMFICOM.map.Map)MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MAP_ID), true));
		} catch(ApplicationException ae){
			throw new RetrieveObjectException(this.getEnityName() + "Database.updateEntityFromResultSet | cannot retrieve map" ,  ae);
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
	}
	
	
	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			this.updateSchemeIds(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				return;
		}
		Set maps = Collections.singleton(storableObject);
		this.updateSchemeIds(maps);
	}
	
	
	public void update(Set storableObjects, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				return;
		}
		this.updateSchemeIds(storableObjects);
	}	
	
	private void updateSchemeIds(Set mapViews) throws UpdateObjectException {
		if (mapViews == null || mapViews.isEmpty())
			return;

		Map mapIdLinkedObjectIds = new HashMap();
		
		for (Iterator colIter = mapViews.iterator(); colIter.hasNext();) {
			StorableObject storableObject = (StorableObject) colIter.next();
	        MapView mapView;
			try {
				mapView = fromStorableObject(storableObject);
			} catch (IllegalDataException e) {
				throw new UpdateObjectException("MapViewDatabase.updateSchemeIds | storableObject isn't map view");
			}
			Set linkedObjectList = mapView.getSchemes();
	
			Set linkedObjectIds = new HashSet(linkedObjectList.size());
	        for (Iterator it = linkedObjectList.iterator(); it.hasNext();) 
	            linkedObjectIds.add(((Scheme) it.next()).getId());
	        
	        mapIdLinkedObjectIds.put(mapView.getId(), linkedObjectIds);
		}
		//TODO: may be wrong correction
		//super.updateLinkedEntities(mapIdLinkedObjectIds, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
		super.updateLinkedEntityIds(mapIdLinkedObjectIds, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
	}
	
	public void delete(Identifier id) {
		this.delete(Collections.singleton(id));
	}
	
	public void delete(Set ids) {
		super.delete(ids);
		
		java.util.Map linkedObjectIds = new HashMap();
		
		java.util.Map mapIds = new HashMap();
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			try{
				MapView map = (MapView)MapViewStorableObjectPool.getStorableObject(mapId, true);
				mapIds.put(mapId, map);
			}catch(ApplicationException ae){
				Log.errorMessage(this.getEnityName()+"Database.delete | Couldn't found map for " + mapId);
			} 
		}
		
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier mapId = (Identifier) it.next();
			MapView mapView = (MapView) mapIds.get(mapId);
			linkedObjectIds.put(mapView, mapView.getSchemes());
		}
		
		this.deleteSchemeIds(linkedObjectIds);
		
	}	

	private void deleteSchemeIds(java.util.Map linkedObjectIds) {
		StringBuffer linkBuffer = new StringBuffer(LINK_COLUMN_SCHEME_ID);
		
		linkBuffer.append(SQL_IN);
		linkBuffer.append(OPEN_BRACKET);
		
		int i = 0;
		for (Iterator mvIter = linkedObjectIds.keySet().iterator(); mvIter.hasNext();) {
			Identifier mapViewId = (Identifier) mvIter.next();
			Set schemes = (Set)linkedObjectIds.get(mapViewId);
			for (Iterator it = schemes.iterator(); it.hasNext(); i++) {
				Identifier id = ((Scheme) it.next()).getId();
	
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

	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Set maps = super.retrieveByCondition(conditionQuery);
		
		java.util.Map mapIds = new HashMap();
		for (Iterator it = maps.iterator(); it.hasNext();) {
			MapView map = (MapView) it.next();
			mapIds.put(map.getId(), map);
		}
		
		java.util.Map schemeIdsMap = super.retrieveLinkedEntityIds(maps, MAPVIEW_SCHEME, LINK_COLUMN_MAPVIEW_ID, LINK_COLUMN_SCHEME_ID);
		for (Iterator it = schemeIdsMap.keySet().iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			MapView map = (MapView) mapIds.get(id);
			Set schemeIds = (Set)schemeIdsMap.get(id);				
			if (id.equals(map.getId())){
				try {
					map.setSchemes0(SchemeStorableObjectPool.getStorableObjects(schemeIds, true));
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(this.getEnityName() + "Database.updateEntityFromResultSet | cannot retrieve schemes" ,  ae);
				}
			}
		}
		return maps;
		//return retriveByIdsPreparedStatement(ids, conditions);
	}


}
