/*-
 * $Id: MapViewWrapper.java,v 1.1 2005/09/06 14:16:19 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mapview;

import java.util.List;

import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/09/06 14:16:19 $
 * @module mapview
 */

public class MapViewWrapper extends StorableObjectWrapper {

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
	
	@Override
	public void setValue(StorableObject object, String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	public List getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPropertyValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		// TODO Auto-generated method stub
		
	}

	public Object getValue(Object object, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEditable(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setValue(Object object, String key, Object value) {
		// TODO Auto-generated method stub
		
	}

}
