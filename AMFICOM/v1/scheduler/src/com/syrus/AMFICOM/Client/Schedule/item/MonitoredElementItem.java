/*
 * $Id: MonitoredElementItem.java,v 1.5 2005/03/30 14:26:20 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.5 $, $Date: 2005/03/30 14:26:20 $
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
			MonitoredElement monitoredElement = (MonitoredElement) ConfigurationStorableObjectPool.getStorableObject(
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
