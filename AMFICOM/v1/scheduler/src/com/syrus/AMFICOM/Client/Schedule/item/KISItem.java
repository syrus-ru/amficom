/*
* $Id: KISItem.java,v 1.2 2005/03/16 12:40:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;


/**
 * @version $Revision: 1.2 $, $Date: 2005/03/16 12:40:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class KISItem extends ElementItem {

	public KISItem(KIS kis) {
		super(kis);
	}
	
	protected Class getChildenClass() {
		return MeasurementPort.class;
	}

	public String getName() {
		KIS kis = (KIS)super.object;
		return kis == null ? "" : kis.getName();
	}	
}
