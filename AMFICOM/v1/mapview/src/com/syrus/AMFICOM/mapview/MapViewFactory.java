/*-
 * $Id: MapViewFactory.java,v 1.3 2005/06/01 18:49:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectFactory;
import com.syrus.AMFICOM.map.corba.MapView_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/01 18:49:43 $
 * @module mapview_v1
 */
final class MapViewFactory extends StorableObjectFactory {
	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see StorableObjectFactory#newInstance(IDLEntity)
	 */
	protected StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException {
		return new MapView((MapView_Transferable) transferable);
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#getId(org.omg.CORBA.portable.IDLEntity)
	 */
	protected Identifier getId(final IDLEntity transferable) {
		return new Identifier(((MapView_Transferable) transferable).header.id);
	}

	/**
	 * @param length
	 * @see com.syrus.AMFICOM.general.StorableObjectFactory#allocateArrayOfTransferables(int)
	 */
	protected IDLEntity[] allocateArrayOfTransferables(final int length) {
		return new MapView_Transferable[length];
	}
}
