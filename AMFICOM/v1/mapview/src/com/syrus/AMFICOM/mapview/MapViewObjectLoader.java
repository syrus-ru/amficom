/*
* $Id: MapViewObjectLoader.java,v 1.4 2005/02/24 15:57:09 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/24 15:57:09 $
 * @author $Author: bob $
 * @module mapview_v1
 */
public interface MapViewObjectLoader {

	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection ids) throws IllegalDataException;

	MapView loadMapView(Identifier id) throws ApplicationException;

	Collection loadMapViews(Collection ids) throws ApplicationException;

	Collection loadMapViewsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;

	void saveMapView(MapView map, boolean force) throws ApplicationException;	

	void saveMapViews(Collection list, boolean force) throws ApplicationException;

}
