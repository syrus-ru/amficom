/*
* $Id: KISItem.java,v 1.5 2005/03/30 14:26:20 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;


/**
 * @version $Revision: 1.5 $, $Date: 2005/03/30 14:26:20 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class KISItem extends ElementItem {

	public KISItem(Identifier kisId) {
		super(kisId);
	}
	
	protected short getChildenEntityCode() {
		return ObjectEntities.MEASUREMENTPORT_ENTITY_CODE;
	}

	public String getName() {
		try {
			KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(super.identifier, true);
			return  kis.getName();
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
