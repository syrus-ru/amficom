/*
* $Id: EquivalentCondition.java,v 1.1 2005/01/21 08:21:41 bob Exp $
*
* Copyright ø 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.List;

import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;


/**
 * Equivalent (identical, allways true) condition
 * written with especial cynicism
 * @version $Revision: 1.1 $, $Date: 2005/01/21 08:21:41 $
 * @author ÷÷œ‘ $Author: bob $
 * @module general_v1
 */
public class EquivalentCondition implements StorableObjectCondition {

	private Short entityCode;
	
	public EquivalentCondition(short entityCode) {
		this(new Short(entityCode));
	}
	
	public EquivalentCondition(Short entityCode) {
		this.entityCode = entityCode;
	}

	public EquivalentCondition(EquivalentCondition_Transferable transferable) {
		this(transferable.entity_code);
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		return true;
	}

	public boolean isNeedMore(List list) throws ApplicationException {
		return true;
	}
	
	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(Short entityCode) {
		this.entityCode = entityCode;

	}

	public Object getTransferable() {
		return new EquivalentCondition_Transferable(this.entityCode.shortValue());
	}

}
