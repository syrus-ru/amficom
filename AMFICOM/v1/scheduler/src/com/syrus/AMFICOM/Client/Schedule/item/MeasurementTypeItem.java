/*
* $Id: MeasurementTypeItem.java,v 1.1 2005/03/15 11:42:12 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.measurement.MeasurementType;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/15 11:42:12 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class MeasurementTypeItem extends ElementItem {
	
	public MeasurementTypeItem(MeasurementType measurementType) {
		super(measurementType);
	}
	
	protected Class getChildenClass() {
		return KIS.class;
	}
	
	public String getName() {
		return ((MeasurementType)super.object).getDescription();
	}
}
