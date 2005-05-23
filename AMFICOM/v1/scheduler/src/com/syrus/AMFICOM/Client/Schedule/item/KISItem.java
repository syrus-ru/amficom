/*
* $Id: KISItem.java,v 1.6 2005/05/23 10:26:12 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Schedule.item;

import com.syrus.AMFICOM.configuration.KIS;
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
public class KISItem extends ElementItem {

	public KISItem(Identifier kisId) {
		super(kisId);
	}
	
	protected short getChildenEntityCode() {
		return ObjectEntities.MEASUREMENTPORT_ENTITY_CODE;
	}

	public String getName() {
		try {
			KIS kis = (KIS)StorableObjectPool.getStorableObject(super.identifier, true);
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
