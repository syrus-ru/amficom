/*-
 * $Id: MapViewWrapper.java,v 1.2 2005/10/25 19:53:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:14 $
 * @module mapview
 */

public final class MapViewWrapper extends StorableObjectWrapper<MapView> {

//	 domain_id VARCHAR2(32),
	public static final String COLUMN_DOMAIN_ID = "domain_id";
	// name VARCHAR2(128),
	public static final String COLUMN_LONGITUDE = "longitude";
	// latitude FLOAT,
	public static final String COLUMN_LATITUDE = "latitude";
	// scale FLOAT,
	public static final String COLUMN_SCALE = "scale";
	// defaultScale FLOAT,
	public static final String COLUMN_DEFAULTSCALE = "defaultScale";
	// map_id VARCHAR2(32),
	public static final String COLUMN_MAP_ID = "map_id";

	// linked table ::
	public static final String MAPVIEW_SCHEME = "MapViewScheme";
	// mapview_id VARCHAR2(32),
	public static final String LINK_COLUMN_MAPVIEW_ID = "mapview_id";
	// scheme_id VARCHAR2(32),
	public static final String LINK_COLUMN_SCHEME_ID = "scheme_id";

	private static MapViewWrapper instance;

	private MapViewWrapper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(final MapView mapView, String key, Object value) {
		throw new UnsupportedOperationException();
	}

	public List<String> getKeys() {
		throw new UnsupportedOperationException();
	}

	public String getName(String key) {
		throw new UnsupportedOperationException();
	}

	public Object getPropertyValue(String key) {
		throw new UnsupportedOperationException();
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getValue(final MapView mapView, String key) {
		throw new UnsupportedOperationException();
	}

	public boolean isEditable(String key) {
		throw new UnsupportedOperationException();
	}

	public static MapViewWrapper getInstance() {
		if (instance == null) {
			instance = new MapViewWrapper();
		}
		return instance;
	}
}
