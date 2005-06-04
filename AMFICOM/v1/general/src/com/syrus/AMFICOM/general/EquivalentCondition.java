/*
* $Id: EquivalentCondition.java,v 1.9 2005/06/04 16:56:18 bass Exp $
*
* Copyright ø 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.EquivalentCondition_Transferable;


/**
 * Equivalent (identical, allways true) condition
 * written with especial cynicism
 * @version $Revision: 1.9 $, $Date: 2005/06/04 16:56:18 $
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

	public EquivalentCondition(EquivalentCondition_Transferable transferable) {
		this(transferable.entity_code);
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

	public IDLEntity getTransferable() {
		final EquivalentCondition_Transferable transferable = new EquivalentCondition_Transferable(this.entityCode.shortValue());

		final StorableObjectCondition_Transferable condition = new StorableObjectCondition_Transferable();
		condition.equivalentCondition(transferable);
		return condition;
	}
}
