/*
* $Id: MapViewObjectLoader.java,v 1.2 2005/02/18 14:29:31 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.Collection;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/18 14:29:31 $
 * @author $Author: bob $
 * @module mapview_v1
 */
public interface MapViewObjectLoader {

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(Collection ids) throws CommunicationException, DatabaseException;

	MapView loadMapView(Identifier id) throws DatabaseException, CommunicationException;

	Collection loadMapViews(Collection ids) throws DatabaseException, CommunicationException;


	Collection loadMapViewsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException;

	void saveMapView(MapView map, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;	

	void saveMapViews(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

}
