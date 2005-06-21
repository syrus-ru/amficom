/*
* $Id: EquivalentCondition.java,v 1.11 2005/06/21 12:43:47 bass Exp $
*
* Copyright ø 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlEquivalentCondition;


/**
 * Equivalent (identical, allways true) condition
 * written with especial cynicism
 * @version $Revision: 1.11 $, $Date: 2005/06/21 12:43:47 $
 * @author ÷÷œ‘ $Author: bass $
 * @module general_v1
 */
public final class EquivalentCondition implements StorableObjectCondition {

	private Short entityCode;
	
	public EquivalentCondition(short entityCode) {
		this(new Short(entityCode));
	}
	
	public EquivalentCondition(Short entityCode) {
		this.entityCode = entityCode;
	}

	public EquivalentCondition(IdlEquivalentCondition transferable) {
		this(transferable.entityCode);
	}

	public boolean isConditionTrue(final StorableObject storableObject) {
		return true;
	}

	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
	
	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(final Short entityCode) {
		this.entityCode = entityCode;

	}
	
	public void setEntityCode(final short entityCode) {
		this.setEntityCode(new Short(entityCode));

	}

	public IdlStorableObjectCondition getTransferable() {
		final IdlEquivalentCondition transferable = new IdlEquivalentCondition(this.entityCode.shortValue());

		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.equivalentCondition(transferable);
		return condition;
	}
}
