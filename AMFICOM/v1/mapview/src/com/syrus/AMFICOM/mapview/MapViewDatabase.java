/*-
 * $Id: MapViewDatabase.java,v 1.40 2005/09/09 17:21:47 krupenn Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.MAPVIEW_SCHEME;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.LINK_COLUMN_MAPVIEW_ID;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.LINK_COLUMN_SCHEME_ID;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_LONGITUDE;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_LATITUDE;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_SCALE;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_DEFAULTSCALE;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_MAP_ID;
import static com.syrus.AMFICOM.mapview.MapViewWrapper.COLUMN_DOMAIN_ID;

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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.40 $, $Date: 2005/09/09 17:21:47 $
 * @author $Author: krupenn $
 * @module mapview
 */
public final class MapViewDatabase extends StorableObjectDatabase<MapView> {
	private static String columns;

	private static String updateMultipleSQLValues;	


	@Override
	public void retrieve(final MapView storableObject)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		this.retrieveEntity(storableObject);
		final Set<MapView> maps = Collections.singleton(storableObject);

		final java.util.Map<Identifier, Set<Identifier>> schemeIdsMap = super.retrieveLinkedEntityIds(maps,
				MAPVIEW_SCHEME,
				LINK_COLUMN_MAPVIEW_ID,
				LINK_COLUMN_SCHEME_ID);
		for (final Identifier id : schemeIdsMap.keySet()) {
			final Set<Identifier> schemeIds = schemeIdsMap.get(id);
			if (id.equals(storableObject.getId())) {
				try {
					final Set<Scheme> schemes = StorableObjectPool.getStorableObjects(schemeIds, true);
					storableObject.setSchemes0(schemes);
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
	protected int setEntityForPreparedStatementTmpl(final MapView storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, storableObject.getCenter().getX());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getCenter().getY());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getScale());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getDefaultScale());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMap().getId());
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final MapView storableObject) throws IllegalDataException {
		final MapView mapView = storableObject;
		final String values = DatabaseIdentifier.toSQLString(mapView.getDomainId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(mapView.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(mapView.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ mapView.getCenter().getX() + COMMA
				+ mapView.getCenter().getY() + COMMA
				+ mapView.getScale() + COMMA
				+ mapView.getDefaultScale() + COMMA
				+ DatabaseIdentifier.toSQLString(mapView.getMap().getId());
		return values;
	}

	@Override
	protected MapView updateEntityFromResultSet(final MapView storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		final MapView map = (storableObject == null)
				? new MapView(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						0.0,
						0.0,
						0.0,
						0.0,
						null)
					: storableObject;

		try {
			map.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
					DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
					DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
					new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
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
	public void insert(final Set<MapView> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);
		try {
			this.updateSchemeIds(storableObjects);
		} catch (final UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set<MapView> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateSchemeIds(storableObjects);
	}	

	private void updateSchemeIds(final Set<MapView> mapViews) throws UpdateObjectException {
		if (mapViews == null || mapViews.isEmpty()) {
			return;
		}

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
				final MapView mapView = StorableObjectPool.getStorableObject(mapId, true);
				schemeIds.addAll(Identifier.createIdentifiers(mapView.getSchemes()));
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		final StringBuffer stringBuffer = idsEnumerationString(schemeIds, LINK_COLUMN_SCHEME_ID, true);

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + MAPVIEW_SCHEME + SQL_WHERE + stringBuffer.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
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
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	@Override
	protected Set<MapView> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<MapView> maps = super.retrieveByCondition(conditionQuery);

		final java.util.Map<Identifier, MapView> mapIds = new HashMap<Identifier, MapView>();
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
