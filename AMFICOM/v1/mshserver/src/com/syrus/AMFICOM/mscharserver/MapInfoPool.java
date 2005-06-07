/*-
 * $Id: MapInfoPool.java,v 1.1 2005/06/07 16:46:59 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

import gnu.trove.TLongObjectHashMap;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/07 16:46:59 $
 * @author $Author: bass $
 * @module mscharserver_v1
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
