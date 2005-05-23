/*
* $Id: MapViewObjectLoader.java,v 1.9 2005/05/23 13:51:16 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.9 $, $Date: 2005/05/23 13:51:16 $
 * @author $Author: bass $
 * @module mapview_v1
 */
public interface MapViewObjectLoader {
	void delete(final Set identifiables);

	Set loadMapViews(final Set ids) throws ApplicationException;

	Set loadMapViewsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException;

	Set refresh(final Set storableObjects) throws ApplicationException;

	void saveMapViews(final Set objects, final boolean force) throws ApplicationException;
}
