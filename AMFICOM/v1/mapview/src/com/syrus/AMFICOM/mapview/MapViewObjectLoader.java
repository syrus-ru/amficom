/*
* $Id: MapViewObjectLoader.java,v 1.8 2005/04/22 14:45:58 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.8 $, $Date: 2005/04/22 14:45:58 $
 * @author $Author: arseniy $
 * @module mapview_v1
 */
public interface MapViewObjectLoader {

	void delete(Identifier id);

	void delete(final Set identifiables);

	MapView loadMapView(Identifier id) throws ApplicationException;

	Set loadMapViews(Set ids) throws ApplicationException;

	Set loadMapViewsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;

	void saveMapView(MapView map, boolean force) throws ApplicationException;	

	void saveMapViews(Set objects, boolean force) throws ApplicationException;

}
