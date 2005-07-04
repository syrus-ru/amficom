/*-
 * $Id: MapViewFactory.java,v 1.6 2005/07/04 13:00:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.mapview.corba.IdlMapView;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/07/04 13:00:47 $
 * @module mapview_v1
 */
final class MapViewFactory extends StorableObjectFactory {
	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	@Override
	protected IdlMapView[] allocateArrayOfTransferables(final int length) {
		return new IdlMapView[length];
	}
}
