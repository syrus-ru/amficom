/*
* $Id: EquivalentCondition.java,v 1.2 2005/02/11 16:09:06 bob Exp $
*
* Copyright ø 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Collection;

import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;


/**
 * Equivalent (identical, allways true) condition
 * written with especial cynicism
 * @version $Revision: 1.2 $, $Date: 2005/02/11 16:09:06 $
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

	public boolean isNeedMore(Collection collection) throws ApplicationException {
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
