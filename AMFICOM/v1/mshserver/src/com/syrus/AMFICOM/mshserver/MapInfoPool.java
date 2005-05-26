/*-
 * $Id: MapInfoPool.java,v 1.1 2005/05/26 11:15:04 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

import gnu.trove.TLongObjectHashMap;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/26 11:15:04 $
 * @author $Author: max $
 * @module misc
 */

public class MapInfoPool {
	
	private static TLongObjectHashMap userRenderer;
	
	public static void init() {
		userRenderer = new TLongObjectHashMap();
	}
	
	public static byte[] getImage(TopologicalImageQuery tiq) throws IllegalDataException {
		long userId = tiq.getUserID();
		MapJLocalRenderer mapJLocalRenderer= (MapJLocalRenderer) userRenderer.get(tiq.getUserID());
		if (mapJLocalRenderer == null) {
			mapJLocalRenderer = new MapJLocalRenderer();
			userRenderer.put(userId, mapJLocalRenderer);
		}
		byte[] image;
		try {
			image = mapJLocalRenderer.render(tiq);
		} catch (Exception e) {
			throw new IllegalDataException(e.getMessage());
		}
		return image;
	}
	
	public static void cancelRendering(long userId) throws IllegalDataException {
		MapJLocalRenderer mapJLocalRenderer = (MapJLocalRenderer) userRenderer.get(userId);
		if(mapJLocalRenderer == null)
			Log.errorMessage("MapInfoPool.cancelRendering | wrong userId");
		try {
			mapJLocalRenderer.cancelRendering();
		} catch (Exception e) {
			throw new IllegalDataException(e.getMessage());
		}
	}
}
