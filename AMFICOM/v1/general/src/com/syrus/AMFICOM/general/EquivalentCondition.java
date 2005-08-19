/*
* $Id: EquivalentCondition.java,v 1.14 2005/08/19 07:56:51 bob Exp $
*
* Copyright ø 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlEquivalentCondition;


/**
 * Equivalent (identical, allways true) condition
 * written with especial cynicism
 * @version $Revision: 1.14 $, $Date: 2005/08/19 07:56:51 $
 * @author ÷÷œ‘ $Author: bob $
 * @module general
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

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlStorableObjectCondition getTransferable(final ORB orb) {
		return this.getTransferable();
	}

	public IdlStorableObjectCondition getTransferable() {
		final IdlEquivalentCondition transferable = new IdlEquivalentCondition(this.entityCode.shortValue());

		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.equivalentCondition(transferable);
		return condition;
	}
	
	@Override
	public String toString() {
		return "all " + ObjectEntities.codeToString(this.entityCode);
	}
}
