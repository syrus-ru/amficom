/*
* $Id: MapViewObjectLoader.java,v 1.1 2004/12/22 15:21:52 cvsadmin Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/22 15:21:52 $
 * @author $Author: cvsadmin $
 * @module mapview_v1
 */
public interface MapViewObjectLoader {

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(List ids) throws CommunicationException, DatabaseException;

	MapView loadMapView(Identifier id) throws DatabaseException, CommunicationException;

	List loadMapViews(List ids) throws DatabaseException, CommunicationException;


	List loadMapViewsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException, DatabaseException;

	void saveMapView(MapView map, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;	

	void saveMapViews(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

}
