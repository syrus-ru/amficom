/*
* $Id: MapViewObjectLoader.java,v 1.5 2005/04/01 13:08:48 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/01 13:08:48 $
 * @author $Author: bob $
 * @module mapview_v1
 */
public interface MapViewObjectLoader {

	void delete(Identifier id) throws IllegalDataException;

	void delete(Set ids) throws IllegalDataException;

	MapView loadMapView(Identifier id) throws ApplicationException;

	Set loadMapViews(Set ids) throws ApplicationException;

	Set loadMapViewsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;

	void saveMapView(MapView map, boolean force) throws ApplicationException;	

	void saveMapViews(Set list, boolean force) throws ApplicationException;

}
