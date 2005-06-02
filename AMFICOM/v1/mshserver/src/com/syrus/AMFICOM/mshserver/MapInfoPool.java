/*-
 * $Id: MapInfoPool.java,v 1.2 2005/06/02 09:48:13 max Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/06/02 09:48:13 $
 * @author $Author: max $
 * @module misc
 */

public class MapInfoPool {
	
	private static TLongObjectHashMap userRenderer;
	private static byte[] nullStub = {0}; 
	
	public static void init() {
		userRenderer = new TLongObjectHashMap();
	}
	
	public static byte[] getImage(TopologicalImageQuery tiq) throws IllegalDataException {
		long userId = tiq.getUserID();
		MapJLocalRenderer mapJLocalRenderer= (MapJLocalRenderer) userRenderer.get(userId);
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
		if (image == null)
			image = nullStub;
		return image;
	}
	
	public static void cancelRendering(long userId) throws IllegalDataException {
		MapJLocalRenderer mapJLocalRenderer = (MapJLocalRenderer) userRenderer.get(userId);
		if(mapJLocalRenderer == null) {
			Log.errorMessage("MapInfoPool.cancelRendering | Wrong userId" + userId);
			return;
		}
		try {
			Log.errorMessage("MapInfoPool.cancelRendering | Stoping render");
			mapJLocalRenderer.cancelRendering();
		} catch (Exception e) {
			throw new IllegalDataException(e.getMessage());
		}
	}
}
