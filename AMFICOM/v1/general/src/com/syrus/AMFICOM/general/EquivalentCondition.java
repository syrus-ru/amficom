/*
* $Id: EquivalentCondition.java,v 1.7 2005/04/04 13:07:03 bass Exp $
*
* Copyright ø 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;


/**
 * Equivalent (identical, allways true) condition
 * written with especial cynicism
 * @version $Revision: 1.7 $, $Date: 2005/04/04 13:07:03 $
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

	public IDLEntity getTransferable() {
		return new EquivalentCondition_Transferable(this.entityCode.shortValue());
	}

}
