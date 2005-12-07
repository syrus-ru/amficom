/*
* $Id: EquivalentCondition.java,v 1.20 2005/12/07 17:16:24 bass Exp $
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
 * @version $Revision: 1.20 $, $Date: 2005/12/07 17:16:24 $
 * @author ÷÷œ‘ $Author: bass $
 * @module general
 */
public final class EquivalentCondition implements StorableObjectCondition {
	private static final long serialVersionUID = -5534092579946240509L;

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

	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
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
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	public IdlStorableObjectCondition getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlStorableObjectCondition getIdlTransferable() {
		final IdlEquivalentCondition transferable = new IdlEquivalentCondition(this.entityCode.shortValue());

		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.equivalentCondition(transferable);
		return condition;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ", all " + ObjectEntities.codeToString(this.entityCode);
	}
}
