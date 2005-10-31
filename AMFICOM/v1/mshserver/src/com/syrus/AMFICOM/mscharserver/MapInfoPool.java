/*-
 * $Id: MapInfoPool.java,v 1.7 2005/10/31 12:30:11 bass Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/10/31 12:30:11 $
 * @author $Author: bass $
 * @module mscharserver
 */

final class MapInfoPool {
	private MapInfoPool() {
		assert false : "Singleton";
	}
	
	private static HashMap <SessionKey, MapJLocalRenderer> keyRendererHashMap;
	private static final byte[] nullImageStub = {0};
	private static final MapFeature nullMapFeatureStub = new MapFeature(0,0,"");
	
	public static void init() {
		keyRendererHashMap = new HashMap <SessionKey, MapJLocalRenderer> ();
	}
	
	static byte[] getImage(final TopologicalImageQuery tiq, final SessionKey key) throws IllegalDataException {
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
		if (image == null) {
			image = nullImageStub;
		}
		return image;
	}
	
	static void cancelRendering(final SessionKey key) throws IllegalDataException {
		final MapJLocalRenderer mapJLocalRenderer = keyRendererHashMap.get(key);
		if(mapJLocalRenderer == null) {
			Log.errorMessage("Wrong sessionKey" + key);
			return;
		}
		try {
			Log.errorMessage("Stoping render");
			mapJLocalRenderer.cancelRendering();
		} catch (Exception e) {
			throw new IllegalDataException("MapInfoPool.cancelRendering | IllegalDataException " + e.getMessage());
		}
	}

	static List<MapFeature> findFeature(final String featureName, final SessionKey key) throws IllegalDataException {
		final MapJLocalRenderer mapJLocalRenderer = keyRendererHashMap.get(key);
		if(mapJLocalRenderer == null) {
			Log.errorMessage("Wrong sessionKey" + key);
			return null;
		}
		List<MapFeature> mapFeatures;
		try {
			Log.errorMessage("Trying to find");
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
