/*
 * $Id: MeasurementPortItem.java,v 1.6 2005/05/23 10:26:12 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/23 10:26:12 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class MeasurementPortItem extends ElementItem {

	public MeasurementPortItem(Identifier measurementPortId) {
		super(measurementPortId);
	}

	protected short getChildenEntityCode() {
		return ObjectEntities.ME_ENTITY_CODE;
	}

	public String getName() {
		try {
			MeasurementPort measurementPort = (MeasurementPort) StorableObjectPool.getStorableObject(
				super.identifier, true);
			return measurementPort.getName();
		} catch (ApplicationException e) {
			// nothing
		}
		return null;
	}

	public boolean canHaveParent() {
		return true;
	}

	public boolean canHaveChildren() {
		return true;
	}
}
