/*
* $Id: MapViewObjectLoader.java,v 1.10 2005/06/22 19:26:05 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/22 19:26:05 $
 * @author $Author: arseniy $
 * @module mapview_v1
 */
public interface MapViewObjectLoader {
	void delete(final Set<? extends Identifiable> objects);

	Set loadMapViews(final Set<Identifier> ids) throws ApplicationException;

	Set loadMapViewsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	void saveMapViews(final Set<MapView> objects, final boolean force) throws ApplicationException;
}
