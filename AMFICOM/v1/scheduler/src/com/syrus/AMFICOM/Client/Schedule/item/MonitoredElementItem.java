/*
* $Id: MonitoredElementItem.java,v 1.2 2005/03/16 12:40:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.MonitoredElement;


/**
 * @version $Revision: 1.2 $, $Date: 2005/03/16 12:40:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class MonitoredElementItem extends ElementItem {

	public MonitoredElementItem(MonitoredElement monitoredElement) {
		super(monitoredElement);
	}
	
	protected Class getChildenClass() {
		return null;
	}
	
	public int getMaxChildrenCount() {
		return 0;
	}

	public String getName() {
		return ((MonitoredElement)super.object).getName();
	}
}
