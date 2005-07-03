/*-
 * $Id: MapInfoPool.java,v 1.2 2005/06/28 08:11:40 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.map.MapFeature;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/28 08:11:40 $
 * @author $Author: max $
 * @module mscharserver_v1
 */

public class MapInfoPool {
	
	private static HashMap <SessionKey, MapJLocalRenderer> keyRendererHashMap;
	private static final byte[] nullImageStub = {0};
	private static final MapFeature nullMapFeatureStub = new MapFeature(0,0,"");
	
	public static void init() {
		keyRendererHashMap = new HashMap <SessionKey, MapJLocalRenderer> ();
	}
	
	public static byte[] getImage(TopologicalImageQuery tiq, SessionKey key) throws IllegalDataException {
		MapJLocalRenderer mapJLocalRenderer= keyRendererHashMap.get(key);
		if (mapJLocalRenderer == null) {
			mapJLocalRenderer = new MapJLocalRenderer();
			keyRendererHashMap.put(key, mapJLocalRenderer);
		}
		byte[] image;
		try {
			image = mapJLocalRenderer.render(tiq);
		} catch (Exception e) {
			throw new IllegalDataException(e.getMessage());
		}
		if (image == null)
			image = nullImageStub;
		return image;
	}
	
	public static void cancelRendering(SessionKey key) throws IllegalDataException {
		MapJLocalRenderer mapJLocalRenderer = keyRendererHashMap.get(key);
		if(mapJLocalRenderer == null) {
			Log.errorMessage("MapInfoPool.cancelRendering | Wrong sessionKey" + key);
			return;
		}
		try {
			Log.errorMessage("MapInfoPool.cancelRendering | Stoping render");
			mapJLocalRenderer.cancelRendering();
		} catch (Exception e) {
			throw new IllegalDataException("MapInfoPool.cancelRendering | IllegalDataException " + e.getMessage());
		}
	}

	public static List<MapFeature> findFeature(String featureName, SessionKey key) throws IllegalDataException {
		MapJLocalRenderer mapJLocalRenderer = keyRendererHashMap.get(key);
		if(mapJLocalRenderer == null) {
			Log.errorMessage("MapInfoPool.findFeature | Wrong sessionKey" + key);
			return null;
		}
		List<MapFeature> mapFeatures;
		try {
			Log.errorMessage("MapInfoPool.findFeature | Trying to find");
			mapFeatures = mapJLocalRenderer.findFeature(featureName);
			if(mapFeatures == null || mapFeatures.isEmpty()) {
				mapFeatures = Collections.singletonList(nullMapFeatureStub);
			}
		} catch (Exception e) {
			throw new IllegalDataException("MapInfoPool.cancelRendering | IllegalDataException " + e.getMessage());
		}
		return mapFeatures;
	}
}
