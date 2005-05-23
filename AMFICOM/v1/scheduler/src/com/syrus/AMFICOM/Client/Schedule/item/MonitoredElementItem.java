/*
 * $Id: MonitoredElementItem.java,v 1.6 2005/05/23 10:26:12 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.MonitoredElement;
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
public class MonitoredElementItem extends ElementItem {

	public MonitoredElementItem(Identifier monitoredElementId) {
		super(monitoredElementId);
	}

	protected short getChildenEntityCode() {
		return ObjectEntities.UNKNOWN_ENTITY_CODE;
	}

	public int getMaxChildrenCount() {
		return 0;
	}

	public String getName() {
		try {
			MonitoredElement monitoredElement = (MonitoredElement) StorableObjectPool.getStorableObject(
				super.identifier, true);
			return monitoredElement.getName();
		} catch (ApplicationException e) {
			// nothing
		}
		return null;
	}

	public boolean canHaveParent() {
		return true;
	}
	
	public boolean canHaveChildren() {
		return false;
	}

}
